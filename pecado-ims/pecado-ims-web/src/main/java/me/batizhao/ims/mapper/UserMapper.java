package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.ims.api.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据部门ID查询领导
     * @param id
     * @param type
     * @return
     */
    List<User> selectLeadersByDepartmentId(@Param("id") Long id, @Param("type") String type);
}
