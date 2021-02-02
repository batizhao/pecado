/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.batizhao.dp.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.GenConstants;
import me.batizhao.dp.domain.*;
import me.batizhao.common.core.constant.PecadoConstants;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器 工具类 copy
 * elunez/eladmin/blob/master/eladmin-generator/src/main/java/me/zhengjie/utils/GenUtil.java
 *
 * @author Zheng Jie
 * @author lengleng
 * @date 2020-03-13
 */
@Slf4j
@UtilityClass
public class CodeGenUtils {

    public final String CRUD_PREFIX = "export const tableOption =";

    private final String ENTITY_JAVA_VM = "Entity.java.vm";

    private final String MAPPER_JAVA_VM = "Mapper.java.vm";

    private final String SERVICE_JAVA_VM = "Service.java.vm";

    private final String SERVICE_IMPL_JAVA_VM = "ServiceImpl.java.vm";

    private final String CONTROLLER_JAVA_VM = "Controller.java.vm";

    private final String MAPPER_XML_VM = "Mapper.xml.vm";

    private final String CONTROLLER_UNIT_TEST_JAVA_VM = "ControllerUnitTest.java.vm";

    private final String SERVICE_UNIT_TEST_JAVA_VM = "ServiceUnitTest.java.vm";

    private final String MAPPER_UNIT_TEST_JAVA_VM = "MapperUnitTest.java.vm";

    private final String API_TEST_JAVA_VM = "ApiTest.java.vm";

    private final String MENU_SQL_VM = "menu.sql.vm";

    private final String AVUE_INDEX_VUE_VM = "avue/index.vue.vm";

    private final String AVUE_API_JS_VM = "avue/api.js.vm";

    private final String AVUE_CRUD_JS_VM = "avue/crud.js.vm";

    /**
     * 初始化数据
     * @param code 生成代码
     */
    public static void initData(Code code) {
        code.setClassName(columnToJava(code.getTableName()));
        code.setClassComment(replaceText(code.getTableComment()));
        code.setClassAuthor("batizhao");
        code.setModuleName("system");
        code.setPackageName("me.batizhao");
        code.setTemplate("crud");
        code.setCreateTime(LocalDateTime.now());
        code.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 初始化列属性字段
     * @param codeMeta
     */
    public static void initColumnField(CodeMeta codeMeta) {
        String dataType = getDbType(codeMeta.getColumnType());
        String columnName = codeMeta.getColumnName();
        // 设置java字段名
        codeMeta.setJavaField(getJavaField(columnName));
        // 设置默认类型
        codeMeta.setJavaType(GenConstants.TYPE_STRING);

        if (arraysContains(GenConstants.COLUMNTYPE_STR, dataType) || arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType))
        {
            // 字符串长度超过500设置为文本域
            Integer columnLength = getColumnLength(codeMeta.getColumnType());
            String htmlType = columnLength >= 500 || arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType) ? GenConstants.HTML_TEXTAREA : GenConstants.HTML_INPUT;
            codeMeta.setHtmlType(htmlType);
        }
        else if (arraysContains(GenConstants.COLUMNTYPE_TIME, dataType))
        {
            codeMeta.setJavaType(GenConstants.TYPE_DATE);
            codeMeta.setHtmlType(GenConstants.HTML_DATETIME);
        }
        else if (arraysContains(GenConstants.COLUMNTYPE_NUMBER, dataType))
        {
            codeMeta.setHtmlType(GenConstants.HTML_INPUT);

            // 如果是浮点型 统一用BigDecimal
            String[] str = StringUtils.split(StringUtils.substringBetween(codeMeta.getColumnType(), "(", ")"), ",");
            if (str != null && str.length == 2 && Integer.parseInt(str[1]) > 0)
            {
                codeMeta.setJavaType(GenConstants.TYPE_BIGDECIMAL);
            }
            // 如果是整形
            else if (str != null && str.length == 1 && Integer.parseInt(str[0]) <= 10)
            {
                codeMeta.setJavaType(GenConstants.TYPE_INTEGER);
            }
            // 长整形
            else
            {
                codeMeta.setJavaType(GenConstants.TYPE_LONG);
            }
        }

        // 插入字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_SAVE, columnName) && !codeMeta.getPrimaryKey())
        {
            codeMeta.setSave(true);
        }
        // 编辑字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_EDIT, columnName) && !codeMeta.getPrimaryKey())
        {
            codeMeta.setEdit(true);
        }
        // 列表字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_LIST, columnName) && !codeMeta.getPrimaryKey())
        {
            codeMeta.setDisplay(true);
        }

        // 默认都不可查
        codeMeta.setSearch(false);

        // 查询字段类型
        if (StringUtils.endsWithIgnoreCase(columnName, "name"))
        {
            codeMeta.setSearch(true);
            codeMeta.setSearchType(GenConstants.QUERY_LIKE);
        }
        // 状态字段设置单选框
        if (StringUtils.endsWithIgnoreCase(columnName, "status"))
        {
            codeMeta.setHtmlType(GenConstants.HTML_RADIO);
        }
        // 类型&性别字段设置下拉框
        else if (StringUtils.endsWithIgnoreCase(columnName, "type")
                || StringUtils.endsWithIgnoreCase(columnName, "sex"))
        {
            codeMeta.setHtmlType(GenConstants.HTML_SELECT);
        }
        // 图片字段设置图片上传控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "image"))
        {
            codeMeta.setHtmlType(GenConstants.HTML_IMAGE_UPLOAD);
        }
        // 文件字段设置文件上传控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "file"))
        {
            codeMeta.setHtmlType(GenConstants.HTML_FILE_UPLOAD);
        }
        // 内容字段设置富文本控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "content"))
        {
            codeMeta.setHtmlType(GenConstants.HTML_EDITOR);
        }
    }

    /**
     * 关键字替换
     *
     * @param text 需要被替换的名字
     * @return 替换后的名字
     */
    public static String replaceText(String text)
    {
        return RegExUtils.replaceAll(text, "(?:表|若依)", "");
    }

    /**
     * 获取数据库类型字段
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static String getDbType(String columnType)
    {
        if (StringUtils.indexOf(columnType, "(") > 0)
        {
            return StringUtils.substringBefore(columnType, "(");
        }
        else
        {
            return columnType;
        }
    }

    /**
     * 获取Java类型字段
     *
     * @param columnName 列名
     * @return 截取后的列类型
     */
    public static String getJavaField(String columnName)
    {
        if (StringUtils.indexOf(columnName, "_") > 0)
        {
            return StringUtils.uncapitalize(columnToJava(columnName));
        }
        else
        {
            return columnName;
        }
    }

    /**
     * 校验数组是否包含指定值
     *
     * @param arr 数组
     * @param targetValue 值
     * @return 是否包含
     */
    public static boolean arraysContains(String[] arr, String targetValue)
    {
        return Arrays.asList(arr).contains(targetValue);
    }

    /**
     * 获取字段长度
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static Integer getColumnLength(String columnType)
    {
        if (StringUtils.indexOf(columnType, "(") > 0)
        {
            String length = StringUtils.substringBetween(columnType, "(", ")");
            return Integer.valueOf(length);
        }
        else
        {
            return 0;
        }
    }

    /**
     * 配置
     *
     * @return
     */
    private List<String> getTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add("templates/java/Entity.java.vm");
        templates.add("templates/java/Mapper.java.vm");
        templates.add("templates/java/Service.java.vm");
        templates.add("templates/java/ServiceImpl.java.vm");
        templates.add("templates/java/Controller.java.vm");

        templates.add("templates/xml/Mapper.xml.vm");

        templates.add("templates/test/MapperUnitTest.java.vm");
        templates.add("templates/test/ServiceUnitTest.java.vm");
        templates.add("templates/test/ControllerUnitTest.java.vm");
        templates.add("templates/test/ApiTest.java.vm");

//		templates.add("templates/sql/sql.vm");

//		templates.add("template/js/api.js.vm");
//		templates.add("template/vue/index.vue.vm");
//        templates.add("template/vue/index-tree.vue.vm");
        return templates;
    }

    /**
     * 生成代码
     */
    @SneakyThrows
    public void generatorCode(Code code, List<CodeMeta> codeMetas, ZipOutputStream zip) {
        // 配置信息
//        Configuration config = getConfig();
//        boolean hasBigDecimal = false;
//        // 表信息
//        TableEntity tableEntity = new TableEntity();
//        tableEntity.setTableName(table.get("tableName"));
//
//        if (StringUtils.isNotBlank(genConfig.getComments())) {
//            tableEntity.setComments(genConfig.getComments());
//        } else {
//            tableEntity.setComments(table.get("tableComment"));
//        }
//
//        String tablePrefix;
//        if (StringUtils.isNotBlank(genConfig.getTablePrefix())) {
//            tablePrefix = genConfig.getTablePrefix();
//        } else {
//            tablePrefix = config.getString("tablePrefix");
//        }
//
//        // 表名转换成Java类名
//        String className = tableToJava(tableEntity.getTableName(), tablePrefix);
//        tableEntity.setCaseClassName(className);
//        tableEntity.setLowerClassName(StringUtils.uncapitalize(className));
//
//        // 列信息
//        List<ColumnEntity> columnList = new ArrayList<>();
//        for (Map<String, String> column : columns) {
//            ColumnEntity columnEntity = new ColumnEntity();
//            columnEntity.setColumnName(column.get("columnName"));
//            columnEntity.setDataType(column.get("dataType"));
//            columnEntity.setComments(column.get("columnComment"));
//            columnEntity.setExtra(column.get("extra"));
//            columnEntity.setNullable("NO".equals(column.get("isNullable")));
//            columnEntity.setColumnType(column.get("columnType"));
//            columnEntity.setHidden(Boolean.FALSE);
//            // 列名转换成Java属性名
//            String attrName = columnEntity.getColumnName();
//            columnEntity.setCaseAttrName(attrName);
//            columnEntity.setLowerAttrName(StringUtils.uncapitalize(attrName));
//
//            // 列的数据类型，转换成Java类型
//            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
//            columnEntity.setAttrType(attrType);
//            if (!hasBigDecimal && "BigDecimal".equals(attrType)) {
//                hasBigDecimal = true;
//            }
//            // 是否主键
//            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
//                tableEntity.setPk(columnEntity);
//            }
//
//            columnList.add(columnEntity);
//        }
//        tableEntity.setColumns(columnList);
//
//        // 没主键，则第一个字段为主键
//        if (tableEntity.getPk() == null) {
//            tableEntity.setPk(tableEntity.getColumns().get(0));
//        }

        // 设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        // 封装模板数据
        Map<String, Object> map = new HashMap<>(16);
        map.put("tableName", code.getTableName());
        map.put("pk", codeMetas.get(0));
        map.put("className", code.getClassName());
        map.put("classname", StringUtils.uncapitalize(code.getClassName()));
        map.put("pathName", StringUtils.uncapitalize(code.getClassName()).toLowerCase());
        map.put("columns", codeMetas);
//        map.put("hasBigDecimal", hasBigDecimal);
        map.put("date", DateUtil.today());
        map.put("comments", code.getClassComment());
        map.put("author", code.getClassAuthor());
        map.put("moduleName", code.getModuleName());
        map.put("package", code.getPackageName());

        VelocityContext context = new VelocityContext(map);

        // 获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
//			// 如果是crud
//			if (template.contains(AVUE_CRUD_JS_VM) && formConf != null) {
//				zip.putNextEntry(
//						new ZipEntry(Objects.requireNonNull(getFileName(template, tableEntity.getCaseClassName(),
//								map.get("package").toString(), map.get("moduleName").toString()))));
//				IoUtil.write(zip, StandardCharsets.UTF_8, false, CRUD_PREFIX + formConf.getFormInfo());
//				zip.closeEntry();
//				continue;
//			}

            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, CharsetUtil.UTF_8);
            tpl.merge(context, sw);

            // 添加到zip
            zip.putNextEntry(new ZipEntry(Objects.requireNonNull(getFileName(template, code.getClassName(),
                    map.get("package").toString(), map.get("moduleName").toString()))));
            IoUtil.write(zip, StandardCharsets.UTF_8, false, sw.toString());
            IoUtil.close(sw);
            zip.closeEntry();
        }
    }

    /**
     * 列名转换成Java属性名
     */
    public String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    private String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replaceFirst(tablePrefix, "");
        }
        return columnToJava(tableName);
    }

    /**
     * 设置主键列信息
     *
     * @param table 业务表信息
     */
//    public void setPkColumn(List<CodeMeta> codeMetas)
//    {
//        for (CodeMeta column : codeMetas)
//        {
//            if (column.getPrimaryKey())
//            {
//                table.setPkColumn(column);
//                break;
//            }
//        }
//        if (org.apache.commons.lang3.StringUtils.isNull(table.getPkColumn()))
//        {
//            table.setPkColumn(table.getColumns().get(0));
//        }
//    }

    /**
     * 获取配置信息
     */
    private Configuration getConfig() {
        try {
            FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                    new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                            .configure(new Parameters().properties()
                                    .setFileName("generator.properties")
                                    .setThrowExceptionOnMissing(true)
                                    .setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
                                    .setIncludesAllowed(false));
            return builder.getConfiguration();
        } catch (ConfigurationException e) {
            throw new RuntimeException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    private String getFileName(String template, String className, String packageName, String moduleName) {
        String packageRootPath = PecadoConstants.BACK_END_PROJECT + File.separator + "src" + File.separator;

        String packageSrcPath = packageRootPath + "main" + File.separator + "java" + File.separator;

        String packageTestPath = packageRootPath + "test" + File.separator + "java" + File.separator;

        if (StringUtils.isNotBlank(packageName)) {
			String packagePath = packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
            packageSrcPath += packagePath;
            packageTestPath += packagePath;
        }

        if (template.contains(ENTITY_JAVA_VM)) {
            return packageSrcPath + "domain" + File.separator + className + ".java";
        }

        if (template.contains(MAPPER_JAVA_VM)) {
            return packageSrcPath + "mapper" + File.separator + className + "Mapper.java";
        }

        if (template.contains(MAPPER_UNIT_TEST_JAVA_VM)) {
            return packageTestPath + "unit" + File.separator + "mapper" + File.separator + className + "MapperUnitTest.java";
        }

        if (template.contains(SERVICE_JAVA_VM)) {
            return packageSrcPath + "service" + File.separator + className + "Service.java";
        }

        if (template.contains(SERVICE_IMPL_JAVA_VM)) {
            return packageSrcPath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains(SERVICE_UNIT_TEST_JAVA_VM)) {
            return packageTestPath + "unit" + File.separator + "service" + File.separator + className + "ServiceUnitTest.java";
        }

        if (template.contains(CONTROLLER_JAVA_VM)) {
            return packageSrcPath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains(CONTROLLER_UNIT_TEST_JAVA_VM)) {
            return packageTestPath + "unit" + File.separator + "controller" + File.separator + className + "ControllerUnitTest.java";
        }

        if (template.contains(API_TEST_JAVA_VM)) {
            return packageTestPath + "api" + File.separator + className + "ApiTest.java";
        }

        if (template.contains(MAPPER_XML_VM)) {
            return PecadoConstants.BACK_END_PROJECT + File.separator + "src" + File.separator + "main" + File.separator
                    + "resources" + File.separator + "mapper" + File.separator + className + "Mapper.xml";
        }

        if (template.contains(MENU_SQL_VM)) {
            return className.toLowerCase() + "_menu.sql";
        }

        if (template.contains(AVUE_INDEX_VUE_VM)) {
            return PecadoConstants.FRONT_END_PROJECT + File.separator + "src" + File.separator + "views"
                    + File.separator + moduleName + File.separator + className.toLowerCase() + File.separator
                    + "index.vue";
        }

        if (template.contains(AVUE_API_JS_VM)) {
            return PecadoConstants.FRONT_END_PROJECT + File.separator + "src" + File.separator + "api" + File.separator
                    + className.toLowerCase() + ".js";
        }

        if (template.contains(AVUE_CRUD_JS_VM)) {
            return PecadoConstants.FRONT_END_PROJECT + File.separator + "src" + File.separator + "const"
                    + File.separator + "crud" + File.separator + className.toLowerCase() + ".js";
        }

        return null;
    }

}
