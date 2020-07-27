package me.batizhao.ims.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.ims.domain.Menu;
import me.batizhao.ims.mapper.MenuMapper;
import me.batizhao.ims.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
@Service
public class MenuServiceIml extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> findMenusByRoleId(Long roleId) {
        return menuMapper.findMenusByRoleId(roleId);
    }
}
