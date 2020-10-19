package me.batizhao.dp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.dp.domain.Ds;
import org.apache.ibatis.annotations.*;

/**
 * 数据源
 *
 * @author batizhao
 * @since 2020-10-19
 */
@Mapper
public interface DsMapper extends BaseMapper<Ds> {

    /**
     * 分页查询
     * @param page 分页对象
     * @param ds 数据源
     * @return IPage<Ds>
     */
    IPage<Ds> selectDsPage(Page<Ds> page, @Param("Ds") Ds ds);

}
