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
* 树结构使用 id + pid，并且初始化一条  id=1,pid=0 的根数据。

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
  
      @TableId
      @ApiModelProperty(value="id")
      private Long id;
  
      @ApiModelProperty(value="数据源")
      private String dsName;
  
      @ApiModelProperty(value="表名")
      private String tableName;
  
      @ApiModelProperty(value="表说明")
      private String tableComment;
  
      @ApiModelProperty(value="引擎")
      private String engine;
  
      @ApiModelProperty(value="类名")
      private String className;
  
      @ApiModelProperty(value="类注释")
      private String classComment;
  
      @ApiModelProperty(value="作者")
      private String classAuthor;
  
      @ApiModelProperty(value="包名")
      private String packageName;
  
      @ApiModelProperty(value="所属模块/微服务/系统名，英文")
      private String moduleName;
  
      @ApiModelProperty(value="API后端路由")
      private String mappingPath;
  
      @ApiModelProperty(value="模板类型")
      private String template;
  
      @ApiModelProperty(value="父菜单ID")
      private Long parentMenuId;
  
      @ApiModelProperty(value="生成代码方式（zip压缩包 path自定义路径）")
      private String type;
  
      @ApiModelProperty(value="生成路径（不填默认项目路径）")
      private String path;
  
      @ApiModelProperty(value="关联子表的code.id")
      private Long subTableId;
  
      @ApiModelProperty(value="子表关联的属性名")
      private String subTableFkName;
  
      @ApiModelProperty(value="附加选项")
      private String options;
  
      @ApiModelProperty(value="创建时间")
      private LocalDateTime createTime;
  
      @ApiModelProperty(value="修改时间")
      private LocalDateTime updateTime;
  
      @ApiModelProperty(value="表元数据")
      private transient List<CodeMeta> codeMetaList;
  
      @ApiModelProperty(value="子表元数据")
      private transient Code subCode;
  
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
  
      @TableId
      @ApiModelProperty(value="pk")
      private Long id;
  
      @ApiModelProperty(value="code表ID")
      private Long codeId;
  
      @ApiModelProperty(value="列名")
      private String columnName;
  
      @ApiModelProperty(value="列注释")
      private String columnComment;
  
      @ApiModelProperty(value="列类型")
      private String columnType;
  
      @ApiModelProperty(value="Java类型")
      private String javaType;
  
      @ApiModelProperty(value="Java属性名")
      private String javaField;
  
      @ApiModelProperty(value="是否主键")
      private Boolean primaryKey;
  
      @ApiModelProperty(value="是否自增")
      private Boolean increment;
  
      @ApiModelProperty(value="是否必须")
      private Boolean required;
  
      @ApiModelProperty(value="是否可插入")
      private Boolean save;
  
      @ApiModelProperty(value="是否可编辑")
      private Boolean edit;
  
      @ApiModelProperty(value="是否在列表显示")
      private Boolean display;
  
      @ApiModelProperty(value="是否可查询")
      private Boolean search;
  
      @ApiModelProperty(value="查询方式（等于、不等于、大于、小于、范围）")
      private String searchType;
  
      @ApiModelProperty(value="显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）")
      private String htmlType;
  
      @ApiModelProperty(value="字典类型")
      private String dictType;
  
      @ApiModelProperty(value="排序")
      private Integer sort;
  
      @ApiModelProperty(value="创建时间")
      private LocalDateTime createTime;
  
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

### 表单

* form 生成表单类型，两种方式（visual可视化，meta元数据）
* options 可视化表单JSON串

## 表单可视化设计

### 动态数据

三种方式：

* [赋值变量](https://www.yuque.com/ln7ccx/ntgo8q/rilith#zZXRi)
* [方法函数](https://www.yuque.com/ln7ccx/ntgo8q/rilith#9mnBV)
* [数据源](https://www.yuque.com/ln7ccx/ntgo8q/rilith#b4pEX)

前两种需要自己实现 dynamicData 或者 remoteFuncs 里的数据获取。

以使用赋值变量获取字典中的 status 为例：

```json
data() {
  return {
    dynamicData: {
      statusOptions : [], //在设计器中输入的那个赋值变量名称
    },
  };
},
mounted () {
  this.listDictDataByCode("status").then(response => {
    this.dynamicData.statusOptions = response.data;
  });
},
```

