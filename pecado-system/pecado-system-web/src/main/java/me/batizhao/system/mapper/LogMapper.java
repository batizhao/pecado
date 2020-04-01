package me.batizhao.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.system.domain.Log;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {

}
