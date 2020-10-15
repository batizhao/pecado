package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.ims.api.vo.UserVO;
import me.batizhao.ims.domain.User;
import org.apache.ibatis.annotations.*;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    IPage<UserVO> selectUserPage(Page<UserVO> page, @Param("user") User user);

    @Update("update user set locked = #{locked} where id= #{id}")
    int updateUserStatusById(@Param("id") Long id, @Param("locked") Integer locked);
}
