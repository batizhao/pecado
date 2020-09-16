package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.ims.domain.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author batizhao
 * @since 2020-09-14
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Delete("DELETE FROM user_role WHERE userId = #{userId}")
    void deleteByUserId(@Param("userId") Long userId);

}
