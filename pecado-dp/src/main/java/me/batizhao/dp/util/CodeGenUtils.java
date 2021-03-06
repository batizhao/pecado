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
import me.batizhao.dp.domain.ColumnEntity;
import me.batizhao.dp.domain.GenConfig;
import me.batizhao.dp.domain.TableEntity;
import me.batizhao.common.core.constant.PecadoConstants;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
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
     * 配置
     *
     * @return
     */
    private List<String> getTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add("templates/Entity.java.vm");
        templates.add("templates/Mapper.java.vm");
        templates.add("templates/MapperUnitTest.java.vm");
        templates.add("templates/Mapper.xml.vm");
        templates.add("templates/Service.java.vm");
        templates.add("templates/ServiceImpl.java.vm");
        templates.add("templates/ServiceUnitTest.java.vm");
        templates.add("templates/Controller.java.vm");
        templates.add("templates/ControllerUnitTest.java.vm");
        templates.add("templates/ApiTest.java.vm");
//		templates.add("templates/menu.sql.vm");
//		templates.add("template/avue/api.js.vm");
//		templates.add("template/avue/index.vue.vm");
//		templates.add("template/avue/crud.js.vm");
        return templates;
    }

    /**
     * 生成代码
     */
    @SneakyThrows
    public void generatorCode(GenConfig genConfig, Map<String, String> table, List<Map<String, String>> columns,
                              ZipOutputStream zip) {
        // 配置信息
        Configuration config = getConfig();
        boolean hasBigDecimal = false;
        // 表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName"));

        if (StringUtils.isNotBlank(genConfig.getComments())) {
            tableEntity.setComments(genConfig.getComments());
        } else {
            tableEntity.setComments(table.get("tableComment"));
        }

        String tablePrefix;
        if (StringUtils.isNotBlank(genConfig.getTablePrefix())) {
            tablePrefix = genConfig.getTablePrefix();
        } else {
            tablePrefix = config.getString("tablePrefix");
        }

        // 表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), tablePrefix);
        tableEntity.setCaseClassName(className);
        tableEntity.setLowerClassName(StringUtils.uncapitalize(className));

        // 列信息
        List<ColumnEntity> columnList = new ArrayList<>();
        for (Map<String, String> column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName"));
            columnEntity.setDataType(column.get("dataType"));
            columnEntity.setComments(column.get("columnComment"));
            columnEntity.setExtra(column.get("extra"));
            columnEntity.setNullable("NO".equals(column.get("isNullable")));
            columnEntity.setColumnType(column.get("columnType"));
            columnEntity.setHidden(Boolean.FALSE);
            // 列名转换成Java属性名
            String attrName = columnEntity.getColumnName();
            columnEntity.setCaseAttrName(attrName);
            columnEntity.setLowerAttrName(StringUtils.uncapitalize(attrName));

            // 列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && "BigDecimal".equals(attrType)) {
                hasBigDecimal = true;
            }
            // 是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columnList.add(columnEntity);
        }
        tableEntity.setColumns(columnList);

        // 没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        // 设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        // 封装模板数据
        Map<String, Object> map = new HashMap<>(16);
        map.put("tableName", tableEntity.getTableName());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getCaseClassName());
        map.put("classname", tableEntity.getLowerClassName());
        map.put("pathName", tableEntity.getLowerClassName().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("date", DateUtil.today());

        if (StringUtils.isNotBlank(genConfig.getComments())) {
            map.put("comments", genConfig.getComments());
        } else {
            map.put("comments", tableEntity.getComments());
        }

        if (StringUtils.isNotBlank(genConfig.getAuthor())) {
            map.put("author", genConfig.getAuthor());
        } else {
            map.put("author", config.getString("author"));
        }

        if (StringUtils.isNotBlank(genConfig.getModuleName())) {
            map.put("moduleName", genConfig.getModuleName());
        } else {
            map.put("moduleName", config.getString("moduleName"));
        }

        if (StringUtils.isNotBlank(genConfig.getPackageName())) {
            map.put("package", genConfig.getPackageName());
//			map.put("mainPath", genConfig.getPackageName());
        } else {
            map.put("package", config.getString("package"));
//			map.put("mainPath", config.getString("mainPath"));
        }
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
            zip.putNextEntry(new ZipEntry(Objects.requireNonNull(getFileName(template, tableEntity.getCaseClassName(),
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
//			packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
            packageSrcPath += packageName.replace(".", File.separator) + File.separator + File.separator;
            packageTestPath += packageName.replace(".", File.separator) + File.separator + File.separator;
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
