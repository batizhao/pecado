package me.batizhao.dp.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.dp.domain.Code;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author batizhao
 * @date 2020/10/10
 */
@Mapper
public interface CodeMapper extends BaseMapper<Code> {

    /**
     * 根据数据源查询表
     * @param page
     * @return
     */
    IPage<Code> selectTablePageByDs(Page<Code> page, @Param("code") Code code);

//    /**
//     * 查询表元数据
//     * @param tableName 表名称
//     * @param dsName
//     * @return
//     */
//    @DS("#last")
//    Map<String, String> selectMetaByTableName(@Param("tableName") String tableName, String dsName);

//    /**
//     * 查询表列信息
//     * @param tableName 表名称
//     * @param dsName
//     * @return
//     */
//    @DS("#last")
//    List<Map<String, String>> selectColumnsByTableName(@Param("tableName") String tableName, String dsName);

}
