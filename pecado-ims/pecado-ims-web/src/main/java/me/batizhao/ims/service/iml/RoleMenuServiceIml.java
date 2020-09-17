package me.batizhao.ims.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.ims.domain.RoleMenu;
import me.batizhao.ims.mapper.RoleMenuMapper;
import me.batizhao.ims.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author batizhao
 * @since 2020-09-14
 **/
@Service
public class RoleMenuServiceIml extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public Boolean updateRoleMenus(Long id, List<String> menus) {
        roleMenuMapper.deleteByRoleId(id);

        List<RoleMenu> roleMenus = new ArrayList<>();
        menus.forEach(item -> {
            roleMenus.add(new RoleMenu().setRoleId(id).setMenuId(Long.parseLong(item)));
        });
        return saveBatch(roleMenus);
    }
}
