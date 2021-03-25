# 代码生成

## 说明

现有生成模板支持：

* Spring Boot
* Mybatis + Mybatis-plus
* 后端统一返回 JSON 格式
* 前端 Vue.js

如果架构不同，可以创建自定义模板。

## 表设计

建议：

* 表名使用全小写字母，下划线组成
* 字段名使用驼峰命名
* 表名、字段都有 COMMENT
* 树结构使用 id + pid

## 后端

* 依赖 pecado-commons-core 
* 状态开关 open/close

## 前端

* 批量删除：https://github.com/batizhao/pecado-vue-ui/commit/94e9d27b17fa8bc6ebf52b06df2be4b1a2497338
* 一对多手动增加路由（router/index.js）

## 模板环境变量

模板使用 Velocity 语法。

### 数据库
* pk 主键

* tableName 表名

* relationTable 一对多表中的“一”

  ```velocity
  #if(!$relationTable.isEmpty())
      ${relationTable[0].moduleName}:${relationTable[0].className}:delete
  #end
  ```

  ```java
  public class Code extends Model<Code> {
  
      private static final long serialVersionUID = 1L;
  
      /**
       * 主键
       */
      @TableId
      @ApiModelProperty(value="id")
      private Long id;
  
      /**
       * 数据源
       */
      @ApiModelProperty(value="数据源")
      private String dsName;
  
      /**
       * 表名
       */
      @ApiModelProperty(value="表名")
      private String tableName;
  
      /**
       * 表说明
       */
      @ApiModelProperty(value="表说明")
      private String tableComment;
  
      /**
       * 引擎
       */
      @ApiModelProperty(value="引擎")
      private String engine;
  
      /**
       * 类名
       */
      @ApiModelProperty(value="类名")
      private String className;
  
      /**
       * 类注释
       */
      @ApiModelProperty(value="类注释")
      private String classComment;
  
      /**
       * 作者
       */
      @ApiModelProperty(value="作者")
      private String classAuthor;
  
      /**
       * 包名
       */
      @ApiModelProperty(value="包名")
      private String packageName;
  
      /**
       * 所属模块/微服务/系统名，英文
       */
      @ApiModelProperty(value="所属模块/微服务/系统名，英文")
      private String moduleName;
  
      /**
       * API后端路由
       */
      @ApiModelProperty(value="API后端路由")
      private String mappingPath;
  
      /**
       * 模板类型
       */
      @ApiModelProperty(value="模板类型")
      private String template;
  
      /**
       * 父菜单ID
       */
      @ApiModelProperty(value="父菜单ID")
      private Long parentMenuId;
  
      /**
       * 生成代码方式（zip压缩包 path自定义路径）
       */
      @ApiModelProperty(value="生成代码方式（zip压缩包 path自定义路径）")
      private String type;
  
      /**
       * 生成路径（不填默认项目路径）
       */
      @ApiModelProperty(value="生成路径（不填默认项目路径）")
      private String path;
  
      /**
       * 关联子表的code.id
       */
      @ApiModelProperty(value="关联子表的code.id")
      private Long subTableId;
  
      /**
       * 子表关联的属性名
       */
      @ApiModelProperty(value="子表关联的属性名")
      private String subTableFkName;
  
      /**
       * 附加选项
       */
      @ApiModelProperty(value="附加选项")
      private String options;
  
      /**
       * 创建时间
       */
      @ApiModelProperty(value="创建时间")
      private LocalDateTime createTime;
  
      /**
       * 创建时间
       */
      @ApiModelProperty(value="修改时间")
      private LocalDateTime updateTime;
  
      /**
       * 表元数据
       */
      @ApiModelProperty(value="表元数据")
      private transient List<CodeMeta> codeMetaList;
  
      /**
       * 子表元数据
       */
      @ApiModelProperty(value="子表元数据")
      private transient Code subCode;
  
      /**
       * 关联表元数据
       */
      @ApiModelProperty(value="关联表元数据")
      private transient List<Code> relationCode;
  
  }
  ```

* subTableFkName 一对多表关联属性

* subMappingPath 子表路由

* columns 生成表属性集合

  ```velocity
  #foreach ($column in $columns)
  	$column.columnName
  #end
  ```

  ```java
  public class CodeMeta extends Model<CodeMeta> {
  
      private static final long serialVersionUID = 1L;
  
      /**
       * id
       */
      @TableId
      @ApiModelProperty(value="pk")
      private Long id;
  
      /**
       * code表ID
       */
      @ApiModelProperty(value="code表ID")
      private Long codeId;
  
      /**
       * 列名
       */
      @ApiModelProperty(value="列名")
      private String columnName;
  
      /**
       * 列注释
       */
      @ApiModelProperty(value="列注释")
      private String columnComment;
  
      /**
       * 列类型
       */
      @ApiModelProperty(value="列类型")
      private String columnType;
  
      /**
       * Java类型
       */
      @ApiModelProperty(value="Java类型")
      private String javaType;
  
      /**
       * Java属性名
       */
      @ApiModelProperty(value="Java属性名")
      private String javaField;
  
      /**
       * 是否主键
       */
      @ApiModelProperty(value="是否主键")
      private Boolean primaryKey;
  
      /**
       * 是否自增
       */
      @ApiModelProperty(value="是否自增")
      private Boolean increment;
  
      /**
       * 是否必须
       */
      @ApiModelProperty(value="是否必须")
      private Boolean required;
  
      /**
       * 是否可插入
       */
      @ApiModelProperty(value="是否可插入")
      private Boolean save;
  
      /**
       * 是否可编辑
       */
      @ApiModelProperty(value="是否可编辑")
      private Boolean edit;
  
      /**
       * 是否在列表显示
       */
      @ApiModelProperty(value="是否在列表显示")
      private Boolean display;
  
      /**
       * 是否可查询
       */
      @ApiModelProperty(value="是否可查询")
      private Boolean search;
  
      /**
       * 查询方式（等于、不等于、大于、小于、范围）
       */
      @ApiModelProperty(value="查询方式（等于、不等于、大于、小于、范围）")
      private String searchType;
  
      /**
       * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
       */
      @ApiModelProperty(value="显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）")
      private String htmlType;
  
      /**
       * 字典类型
       */
      @ApiModelProperty(value="字典类型")
      private String dictType;
  
      /**
       * 排序
       */
      @ApiModelProperty(value="排序")
      private Integer sort;
  
      /**
       * 创建时间
       */
      @ApiModelProperty(value="创建时间")
      private LocalDateTime createTime;
  
      /**
       * 修改时间
       */
      @ApiModelProperty(value="修改时间")
      private LocalDateTime updateTime;
  }
  ```

* parentMenuId 父菜单 Id

### 类
* className 类名
* classname 类名首字母小写
* date 生成日期（2021-01-28）
* comments 类注释，默认取表注释
* author 类作者
* mappingPath Controller @GetMapping
* moduleName 模块名
* package 包名

### 控制
* template
  * crud（单表）
  * tree（树结构）
  * onetomany（一对多）
  
  ```velocity
  public class ${className}#if($template == "tree") extends TreeNode#end
  ```






