package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.constant.MenuTypeEnum;
import me.batizhao.common.core.util.BeanCopyUtil;
import me.batizhao.ims.api.util.TreeUtil;
import me.batizhao.ims.api.vo.MenuVO;
import me.batizhao.ims.domain.Menu;
import me.batizhao.ims.domain.RoleMenu;
import me.batizhao.ims.mapper.MenuMapper;
import me.batizhao.ims.service.MenuService;
import me.batizhao.ims.service.RoleMenuService;
import me.batizhao.ims.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
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
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<MenuVO> findMenusByRoleId(Long roleId) {
        List<Menu> menus = menuMapper.findMenusByRoleId(roleId);
        return BeanCopyUtil.copyListProperties(menus, MenuVO::new);
    }

    @Override
    public Set<MenuVO> findMenusByUserId(Long userId) {
        Set<MenuVO> all = new HashSet<>();
        roleService.findRolesByUserId(userId).forEach(roleVO -> all.addAll(findMenusByRoleId(roleVO.getId())));
        return all;
    }

    @Override
    public List<MenuVO> findMenuTreeByUserId(Long userId) {
        Set<MenuVO> all = new HashSet<>();
        roleService.findRolesByUserId(userId).forEach(roleVO -> all.addAll(findMenusByRoleId(roleVO.getId())));
        return filterMenu(all, null);
    }

    @Override
    public List<MenuVO> findMenuTree() {
        List<Menu> menus = menuMapper.selectList(Wrappers.<Menu>lambdaQuery().orderByAsc(Menu::getSort));
//        List<MenuTree> menuTrees = new ArrayList<>();
//        MenuTree menuTree;
//        for (Menu menu : menus) {
//            menuTree = new MenuTree();
//            menuTree.setTitle(menu.getName());
//            menuTree.setKey(menu.getId().toString());
//            menuTree.setPid(menu.getPid());
//            menuTree.setId(menu.getId());
//            menuTrees.add(menuTree);
//        }
        return TreeUtil.build(BeanCopyUtil.copyListProperties(menus, MenuVO::new), 0);
    }

    @Override
    public List<MenuVO> filterMenu(Set<MenuVO> all, Integer parentId) {
        List<MenuVO> menuTreeList = all.stream()
                .filter(vo -> MenuTypeEnum.MENU.getType().equals(vo.getType()))
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
    @Transactional
    public MenuVO saveOrUpdateMenu(Menu menu) {
        if (menu.getId() == null) {
            menu.setCreateTime(LocalDateTime.now());
            menu.setUpdateTime(LocalDateTime.now());
            menuMapper.insert(menu);
        } else {
            menu.setUpdateTime(LocalDateTime.now());
            menuMapper.updateById(menu);
        }

        MenuVO menuVO = new MenuVO();
        BeanUtils.copyProperties(menu, menuVO);

        return menuVO;
    }

    @Override
    public Boolean deleteByIds(List<Long> ids) {
        this.removeByIds(ids);
        ids.forEach(i -> roleMenuService.remove(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getMenuId, i)));
        return true;
    }

    @Override
    @Transactional
    public Boolean updateStatus(Menu menu) {
        LambdaUpdateWrapper<Menu> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Menu::getId, menu.getId()).set(Menu::getStatus, menu.getStatus());
        return menuMapper.update(null, wrapper) == 1;
    }
}
