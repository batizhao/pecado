package me.batizhao.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.system.api.domain.Config;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统配置
 *
 * @author batizhao
 * @since 2022-05-16
 */
@Mapper
public interface ConfigMapper extends BaseMapper<Config> {

}
