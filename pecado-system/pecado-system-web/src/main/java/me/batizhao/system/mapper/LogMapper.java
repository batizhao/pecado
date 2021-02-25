package me.batizhao.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.system.api.domain.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {

    IPage<Log> selectLogPage(Page<Log> page, @Param("log") Log log);
}
