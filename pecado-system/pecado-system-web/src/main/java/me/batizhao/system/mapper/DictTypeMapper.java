package me.batizhao.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.system.api.domain.DictType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典类型
 *
 * @author batizhao
 * @since 2021-02-07
 */
@Mapper
public interface DictTypeMapper extends BaseMapper<DictType> {

}
