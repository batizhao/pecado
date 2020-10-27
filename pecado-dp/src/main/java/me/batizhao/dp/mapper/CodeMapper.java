package me.batizhao.dp.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public interface CodeMapper {

    /**
     * 根据数据源查询表
     * @param page
     * @return
     */
    IPage<List<Map<String, String>>> selectTableByDs(Page page, @Param("tableName") String tableName);

    /**
     * 查询表元数据
     * @param tableName 表名称
     * @return
     */
    Map<String, String> selectMetaByTableName(@Param("tableName") String tableName);

    /**
     * 查询表列信息
     * @param tableName 表名称
     * @return
     */
    List<Map<String, String>> selectColumnsByTableName(@Param("tableName") String tableName);

}
