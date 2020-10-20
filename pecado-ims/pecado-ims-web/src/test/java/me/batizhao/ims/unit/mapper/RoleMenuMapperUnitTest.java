package me.batizhao.ims.unit.mapper;

import me.batizhao.ims.mapper.RoleMenuMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

/**
 * @author batizhao
 * @since 2020-02-07
 */
public class RoleMenuMapperUnitTest extends BaseMapperUnitTest {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Test
    public void testDeleteByRoleId() {
        assertThat(roleMenuMapper.deleteByRoleId(2L), greaterThan(0));
    }

}