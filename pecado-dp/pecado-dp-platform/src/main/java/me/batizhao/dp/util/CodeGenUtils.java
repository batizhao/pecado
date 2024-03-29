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
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.GenConstants;
import me.batizhao.common.core.constant.PecadoConstants;
import me.batizhao.dp.config.GenConfig;
import me.batizhao.dp.domain.Code;
import me.batizhao.dp.domain.CodeMeta;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    private final String ENTITY_JAVA_VM = "Domain.java";

    private final String ENTITY_DTO_JAVA_VM = "DomainDTO.java";

    private final String ENTITY_FORM_JAVA_VM = "DomainForm.java";

    private final String MAPPER_JAVA_VM = "Mapper.java";

    private final String SERVICE_JAVA_VM = "Service.java";

    private final String SERVICE_IMPL_JAVA_VM = "ServiceImpl.java";

    private final String CONTROLLER_JAVA_VM = "Controller.java";

    private final String CONTROLLER_BASE_JAVA_VM = "BaseController.java";

    private final String MAPPER_XML_VM = "Mapper.xml";

    private final String CONTROLLER_UNIT_TEST_JAVA_VM = "ControllerUnitTest.java";

    private final String SERVICE_UNIT_TEST_JAVA_VM = "ServiceUnitTest.java";

    private final String MAPPER_UNIT_TEST_JAVA_VM = "MapperUnitTest.java";

    private final String API_TEST_JAVA_VM = "ApiTest.java";

    private final String MENU_SQL_VM = "menu.sql";

    private final String VUE_INDEX_VUE_VM = "index.vue";

    private final String VUE_TREE_INDEX_VUE_VM = "index-tree.vue";

    private final String VUE_API_JS_VM = "api.js";

    private final String JSP_COMMENT = "comment.jsp";
    private final String JSP_INDEX = "index.jsp";
    private final String JSP_INPUT = "input.jsp";
    private final String JSP_STATISTICAL = "statistical.jsp";
    private final String JSP_VIEW = "view.jsp";

    /**
     * 初始化数据
     *
     * @param code 生成代码
     */
    public static void initData(Code code) {
        code.setClassName(columnToJava(code.getTableName()));
        code.setClassComment(replaceText(code.getTableComment()));
        code.setClassAuthor(GenConfig.getAuthor());
        code.setModuleName(GenConfig.getModuleName());
        code.setPackageName(GenConfig.getPackageName());
        code.setTemplate(GenConstants.TPL_CRUD);
        code.setMappingPath(StringUtils.uncapitalize(code.getClassName()));
        code.setCreateTime(LocalDateTime.now());
        code.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 初始化列属性字段
     *
     * @param codeMeta
     */
    public static void initColumnField(CodeMeta codeMeta) {
        String dataType = getDbType(codeMeta.getColumnType());
        String columnName = codeMeta.getColumnName();
        // 设置java字段名
        codeMeta.setJavaField(getJavaField(columnName));
        // 设置默认类型
        codeMeta.setJavaType(GenConstants.TYPE_STRING);

        if (arraysContains(GenConstants.COLUMNTYPE_STR, dataType) || arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType)) {
            // 字符串长度超过500设置为文本域
            Integer columnLength = getColumnLength(codeMeta.getColumnType());
            String htmlType = columnLength >= 500 || arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType) ? GenConstants.HTML_TEXTAREA : GenConstants.HTML_INPUT;
            codeMeta.setHtmlType(htmlType);
        } else if (arraysContains(GenConstants.COLUMNTYPE_TIME, dataType)) {
            codeMeta.setJavaType(GenConstants.TYPE_DATE);
            codeMeta.setHtmlType(GenConstants.HTML_DATETIME);
        } else if (arraysContains(GenConstants.COLUMNTYPE_NUMBER, dataType)) {
            codeMeta.setHtmlType(GenConstants.HTML_INPUT);

            // 如果是浮点型 统一用BigDecimal
            String[] str = StringUtils.split(StringUtils.substringBetween(codeMeta.getColumnType(), "(", ")"), ",");
            if (str != null && str.length == 2 && Integer.parseInt(str[1]) > 0) {
                codeMeta.setJavaType(GenConstants.TYPE_BIGDECIMAL);
            }
            // 如果是整形
            else if (str != null && str.length == 1 && Integer.parseInt(str[0]) <= 10) {
                codeMeta.setJavaType(GenConstants.TYPE_INTEGER);
            }
            // 长整形
            else {
                codeMeta.setJavaType(GenConstants.TYPE_LONG);
            }
        }

        // 插入字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_SAVE, columnName) && !codeMeta.getPrimaryKey()) {
            codeMeta.setSave(true);
        }
        // 编辑字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_EDIT, columnName) && !codeMeta.getPrimaryKey()) {
            codeMeta.setEdit(true);
        }
        // 列表字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_LIST, columnName) && !codeMeta.getPrimaryKey()) {
            codeMeta.setDisplay(true);
        }

        // 默认都不可查
        codeMeta.setSearch(false);

        // 查询字段类型
        if (StringUtils.endsWithIgnoreCase(columnName, "name")) {
            codeMeta.setSearch(true);
            codeMeta.setSearchType(GenConstants.QUERY_LIKE);
        }
        // 状态字段设置单选框
        if (StringUtils.endsWithIgnoreCase(columnName, "status")) {
            codeMeta.setHtmlType(GenConstants.HTML_SWITCH);
        }
        // 类型&性别字段设置下拉框
        else if (StringUtils.endsWithIgnoreCase(columnName, "type")
                || StringUtils.endsWithIgnoreCase(columnName, "sex")) {
            codeMeta.setHtmlType(GenConstants.HTML_SELECT);
        }
        // 图片字段设置图片上传控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "image")) {
            codeMeta.setHtmlType(GenConstants.HTML_IMAGE_UPLOAD);
        }
        // 文件字段设置文件上传控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "file")) {
            codeMeta.setHtmlType(GenConstants.HTML_FILE_UPLOAD);
        }
        // 内容字段设置富文本控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "content")) {
            codeMeta.setHtmlType(GenConstants.HTML_EDITOR);
        }
    }

    /**
     * 关键字替换
     *
     * @param text 需要被替换的名字
     * @return 替换后的名字
     */
    private String replaceText(String text) {
        return RegExUtils.replaceAll(text, "(?:表|若依)", "");
    }

    /**
     * 获取数据库类型字段
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    private String getDbType(String columnType) {
        if (StringUtils.indexOf(columnType, "(") > 0) {
            return StringUtils.substringBefore(columnType, "(");
        } else {
            return columnType;
        }
    }

    /**
     * 获取Java类型字段
     *
     * @param columnName 列名
     * @return 截取后的列类型
     */
    private String getJavaField(String columnName) {
        if (StringUtils.indexOf(columnName, "_") > 0) {
            return StringUtils.uncapitalize(columnToJava(columnName));
        } else {
            return columnName;
        }
    }

    /**
     * 校验数组是否包含指定值
     *
     * @param arr         数组
     * @param targetValue 值
     * @return 是否包含
     */
    private boolean arraysContains(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    /**
     * 获取字段长度
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    private Integer getColumnLength(String columnType) {
        if (StringUtils.indexOf(columnType, "(") > 0) {
            String length = StringUtils.substringBetween(columnType, "(", ")");
            return Integer.valueOf(length);
        } else {
            return 0;
        }
    }

    public Map<String, Object> prepareContext(Code code) {
        Map<String, Object> map = new HashMap<>(21);
        map.put("tableName", code.getTableName());
        map.put("pk", code.getCodeMetaList().get(0));
        map.put("className", code.getClassName());
        map.put("classname", StringUtils.uncapitalize(code.getClassName()));
        map.put("classNameLower", StringUtils.lowerCase(code.getClassName()));
        map.put("mappingPath", code.getMappingPath());
        map.put("columns", code.getCodeMetaList());
        map.put("date", DateUtil.today());
        map.put("comments", code.getClassComment());
        map.put("author", code.getClassAuthor());
        map.put("moduleName", code.getModuleName());
        map.put("package", code.getPackageName());
        map.put("parentMenuId", code.getParentMenuId());
        map.put("template", code.getTemplate());
        map.put("relationTable", code.getRelationCode());
        map.put("subTableFkName", code.getSubTableFkName());
        map.put("subMappingPath", code.getSubCode() != null ? code.getSubCode().getMappingPath() : "");
        map.put("form", code.getForm());
        map.put("formKey", code.getFormKey());
        map.put("workflow", code.getWorkflow());
        map.put("workflowKey", code.getWorkflowKey());
        return map;
    }

    /**
     * 列名转换成Java属性名
     */
    public String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 获取文件名
     */
    public String getFileName(Code code, String template) {
        String packageRootPath = "src" + File.separator;

        String packageSrcPath = packageRootPath + "main" + File.separator + "java" + File.separator;

        String packageTestPath = packageRootPath + "test" + File.separator + "java" + File.separator;

        if (StringUtils.isNotBlank(code.getPackageName())) {
            String packagePath = code.getPackageName().replace(".", File.separator) + File.separator + code.getModuleName() + File.separator;
            if (GenConfig.getProjectKey().equals("jsoa")) packagePath = packagePath + StringUtils.lowerCase(code.getClassName()) + File.separator;
            packageSrcPath += packagePath;
            packageTestPath += packagePath;
        }

        if (template.contains(ENTITY_JAVA_VM)) {
            return packageSrcPath + GenConfig.getPojoPackageName() + File.separator + code.getClassName() + ".java";
        }

        if (template.contains(MAPPER_JAVA_VM)) {
            return packageSrcPath + GenConfig.getMapperPackageName() + File.separator + code.getClassName() + "Mapper.java";
        }

        if (template.contains(SERVICE_JAVA_VM)) {
            return packageSrcPath + "service" + File.separator + code.getClassName() + "Service.java";
        }

        if (template.contains(SERVICE_IMPL_JAVA_VM)) {
            return packageSrcPath + "service" + File.separator + "impl" + File.separator + code.getClassName() + "ServiceImpl.java";
        }

        if (template.contains(CONTROLLER_JAVA_VM)) {
            return packageSrcPath + "controller" + File.separator + code.getClassName() + "Controller.java";
        }

        if (template.contains(CONTROLLER_BASE_JAVA_VM)) {
            return packageSrcPath + "controller" + File.separator + code.getClassName() + "BaseController.java";
        }

        if (template.contains(ENTITY_DTO_JAVA_VM)) {
            return packageSrcPath + "controller" + File.separator + code.getClassName() + "DTO.java";
        }

        if (template.contains(ENTITY_FORM_JAVA_VM)) {
            return packageSrcPath + "controller" + File.separator + code.getClassName() + "Form.java";
        }

        if (template.contains(MAPPER_XML_VM)) {
            return "src" + File.separator + "main" + File.separator
                    + "resources" + File.separator + "mapper" + File.separator + code.getClassName() + "Mapper.xml";
        }

        if (template.contains(MENU_SQL_VM)) {
            return "db" + File.separator + code.getClassName().toLowerCase() + "_menu.sql";
        }

        if (code.getTestcase().equals("yes")) {
            if (template.contains(MAPPER_UNIT_TEST_JAVA_VM)) {
                return packageTestPath + "unit" + File.separator + "mapper" + File.separator + code.getClassName() + "MapperUnitTest.java";
            }

            if (template.contains(SERVICE_UNIT_TEST_JAVA_VM)) {
                return packageTestPath + "unit" + File.separator + "service" + File.separator + code.getClassName() + "ServiceUnitTest.java";
            }

            if (template.contains(CONTROLLER_UNIT_TEST_JAVA_VM)) {
                return packageTestPath + "unit" + File.separator + "controller" + File.separator + code.getClassName() + "ControllerUnitTest.java";
            }

            if (template.contains(API_TEST_JAVA_VM)) {
                return packageTestPath + "api" + File.separator + code.getClassName() + "ApiTest.java";
            }
        }


        if (template.contains(VUE_INDEX_VUE_VM)) {
            return "src" + File.separator + "views" + File.separator
                    + code.getModuleName() + File.separator + code.getMappingPath() + File.separator
                    + "index.vue";
        }

        if (code.getTemplate().equals(GenConstants.TPL_TREE) && template.contains(VUE_TREE_INDEX_VUE_VM)) {
            return "src" + File.separator + "views" + File.separator
                    + code.getModuleName() + File.separator + code.getMappingPath() + File.separator
                    + "index.vue";
        }

        if (template.contains(VUE_API_JS_VM)) {
            return "src" + File.separator + "api" + File.separator
                    + code.getModuleName() + File.separator + code.getMappingPath() + ".js";
        }

        if (template.contains(JSP_INDEX)) {
            return PecadoConstants.FRONT_END_PROJECT + File.separator + "src" + File.separator + "main" + File.separator
                    + "webapp" + File.separator + "jsp" + File.separator + "frontend" + File.separator
                    + StringUtils.lowerCase(code.getClassName()) + File.separator + "index.jsp";
        }

        if (template.contains(JSP_COMMENT)) {
            return PecadoConstants.FRONT_END_PROJECT + File.separator + "src" + File.separator + "main" + File.separator
                    + "webapp" + File.separator + "jsp" + File.separator + "frontend" + File.separator
                    + StringUtils.lowerCase(code.getClassName()) + File.separator + "comment.jsp";
        }

        if (template.contains(JSP_INPUT)) {
            return PecadoConstants.FRONT_END_PROJECT + File.separator + "src" + File.separator + "main" + File.separator
                    + "webapp" + File.separator + "jsp" + File.separator + "frontend" + File.separator
                    + StringUtils.lowerCase(code.getClassName()) + File.separator + "input.jsp";
        }

        if (template.contains(JSP_STATISTICAL)) {
            return PecadoConstants.FRONT_END_PROJECT + File.separator + "src" + File.separator + "main" + File.separator
                    + "webapp" + File.separator + "jsp" + File.separator + "frontend" + File.separator
                    + StringUtils.lowerCase(code.getClassName()) + File.separator + "statistical.jsp";
        }

        if (template.contains(JSP_VIEW)) {
            return PecadoConstants.FRONT_END_PROJECT + File.separator + "src" + File.separator + "main" + File.separator
                    + "webapp" + File.separator + "jsp" + File.separator + "frontend" + File.separator
                    + StringUtils.lowerCase(code.getClassName()) + File.separator + "view.jsp";
        }

        return null;
    }

}
