package me.batizhao.dp.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author batizhao
 * @date 2020/10/10
 */
@Mapper
public interface CodeGeneratorMapper {

    /**
     * 查询表列信息
     * @param tableName 表名称
     * @return
     */
    @Select("select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra ,is_nullable as isNullable,column_type as columnType " +
            "from information_schema.columns " +
            "where table_name = #{tableName} and table_schema = (select database()) order by ordinal_position")
    List<Map<String, String>> selectColumns(@Param("tableName") String tableName);

    @Select("select table_name tableName, engine, table_comment tableComment, create_time createTime " +
            "from information_schema.tables " +
            "where table_schema = (select database()) and table_name = #{tableName}")
    Map<String, String> selectTable(@Param("tableName") String tableName);
}
