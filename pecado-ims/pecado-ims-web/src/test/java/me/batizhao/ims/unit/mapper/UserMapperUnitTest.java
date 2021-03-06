package me.batizhao.ims.unit.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.ims.api.vo.UserVO;
import me.batizhao.ims.domain.User;
import me.batizhao.ims.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

/**
 * @author batizhao
 * @since 2020-02-07
 */
public class UserMapperUnitTest extends BaseMapperUnitTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testFindByName() {
        //查找老师
        List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getName, "孙波波"));
        //确认数量
        assertThat(users, hasSize(2));

        //确认记录内容，这里注意 AnyOrder、Order、allOf、anyOf 的区别
        assertThat(users, containsInAnyOrder(allOf(hasProperty("id", is(5L)),
                                                      hasProperty("email", is("bob@qq.com")),
                                                      hasProperty("username", is("bob"))),
                                                allOf(hasProperty("id", is(6L)),
                                                      hasProperty("email", is("bob2@qq.com")),
                                                      hasProperty("username", is("bob2")))));

        //匹配任意记录中单个属性
        assertThat(users, hasItem(hasProperty("username", is("bob"))));

        //匹配单条记录中的多个属性
        assertThat(users, hasItem(allOf(hasProperty("id", is(5L)),
                                         hasProperty("email", is("bob@qq.com")),
                                         hasProperty("username", is("bob")))));

    }

    @Test
    public void testSelectUserPage() {
        IPage<UserVO> users = userMapper.selectUserPage(new Page<>(), new User().setUsername("tom"));
        assertThat(users.getRecords(), hasSize(1));

        users = userMapper.selectUserPage(new Page<>(), new User().setUsername("bob"));
        assertThat(users.getRecords(), hasSize(2));
    }

    @Test
    public void testUpdateUserStatusById() {
        assertThat(userMapper.updateUserStatusById(3L, 1), is(1));
    }
}