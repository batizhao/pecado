package me.batizhao.ims.unit.mapper;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.ims.domain.Menu;
import me.batizhao.ims.domain.Role;
import me.batizhao.ims.domain.RoleMenu;
import me.batizhao.ims.mapper.MenuMapper;
import me.batizhao.ims.mapper.RoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Slf4j
public class MenuMapperUnitTest extends BaseMapperUnitTest {

    @Autowired
    private MenuMapper menuMapper;

    @Test
    public void testFindMenusByRoleId() {
        List<Menu> menus = menuMapper.findMenusByRoleId(1L);

        assertThat(menus, hasItem(allOf(hasProperty("id", is(1L)),
                hasProperty("permission", is("user_dashboard")))));

        menus = menuMapper.findMenusByRoleId(2L);
        assertThat(menus.size(), is(5));
    }

}
