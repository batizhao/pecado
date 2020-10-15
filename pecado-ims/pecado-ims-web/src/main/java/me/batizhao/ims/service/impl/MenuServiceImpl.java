package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.constant.MenuTypeEnum;
import me.batizhao.common.core.util.BeanCopyUtil;
import me.batizhao.ims.api.util.TreeUtil;
import me.batizhao.ims.api.vo.MenuTree;
import me.batizhao.ims.api.vo.MenuVO;
import me.batizhao.ims.domain.Menu;
import me.batizhao.ims.mapper.MenuMapper;
import me.batizhao.ims.service.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<MenuVO> findMenusByRoleId(Long roleId) {
        List<Menu> menus = menuMapper.findMenusByRoleId(roleId);
        return BeanCopyUtil.copyListProperties(menus, MenuVO::new);
    }

    @Override
    public List<MenuTree> findMenuTree() {
        List<Menu> menus = menuMapper.selectList(Wrappers.<Menu>lambdaQuery().orderByAsc(Menu::getSort));
        List<MenuTree> menuTrees = new ArrayList<>();
        MenuTree menuTree;
        for (Menu menu : menus) {
            menuTree = new MenuTree();
            menuTree.setTitle(menu.getName());
            menuTree.setKey(menu.getId().toString());
            menuTree.setPid(menu.getPid());
            menuTree.setId(menu.getId());
            menuTrees.add(menuTree);
        }
        return TreeUtil.build(menuTrees, 0);
    }

    @Override
    public List<MenuVO> filterMenu(Set<MenuVO> all, Integer parentId) {
        List<MenuVO> menuTreeList = all.stream()
                .filter(vo -> MenuTypeEnum.LEFT_MENU.getType().equals(vo.getType()))
                .map(MenuVO::new)
                .sorted(Comparator.comparingInt(MenuVO::getSort))
                .collect(Collectors.toList());

        Integer parent = parentId == null ? 0 : parentId;
        return TreeUtil.build(menuTreeList, parent);
    }

    @Override
    public MenuVO findMenuById(int menuId) {
        Menu menu = menuMapper.selectById(menuId);
        MenuVO menuVO = new MenuVO();
        BeanUtils.copyProperties(menu, menuVO);
        return menuVO;
    }

    @Override
    public MenuVO saveOrUpdateMenu(Menu menu) {
        if (menu.getId() == null) {
            menuMapper.insert(menu);
        } else {
            menuMapper.updateById(menu);
        }

        MenuVO menuVO = new MenuVO();
        BeanUtils.copyProperties(menu, menuVO);

        return menuVO;
    }
}
