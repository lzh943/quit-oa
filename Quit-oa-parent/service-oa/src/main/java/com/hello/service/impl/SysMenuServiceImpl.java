package com.hello.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.exception.SystemException;
import com.hello.mapper.SysMenuMapper;
import com.hello.model.system.SysMenu;
import com.hello.model.system.SysRoleMenu;
import com.hello.service.SysMenuService;
import com.hello.service.SysRoleMenuService;
import com.hello.utils.MenuHelper;
import com.hello.vo.system.AssginMenuVo;
import com.hello.vo.system.MetaVo;
import com.hello.vo.system.RouterVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Resource
    SysRoleMenuService sysRoleMenuService;
    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> sysMenuList = list();
        List<SysMenu> resultList = MenuHelper.buildTree(sysMenuList);
        return resultList;
    }

    @Override
    public void removeMenuById(Long id) {
        LambdaQueryWrapper<SysMenu> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, id);
        int count = count(wrapper);
        if(count>0){
            throw new SystemException(500, "需要先删除子菜单");
        }
        removeById(id);
    }

    @Override
    public List<SysMenu> findSysMenuByRoleId(Long roleId) {
        LambdaQueryWrapper<SysMenu> wrapperMenu=new LambdaQueryWrapper<>();
        wrapperMenu.eq(SysMenu::getStatus, 1);
        List<SysMenu> sysMenus = list(wrapperMenu);
        LambdaQueryWrapper<SysRoleMenu> wrapperRoleMenu=new LambdaQueryWrapper<>();
        wrapperRoleMenu.eq(SysRoleMenu::getRoleId, roleId);
        List<Long> menuIds = sysRoleMenuService.list(wrapperRoleMenu).stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.toList());
        sysMenus.stream()
                .forEach(sysMenu -> {
                    if(menuIds.contains(sysMenu.getId())){
                        sysMenu.setSelect(true);
                    }else {
                        sysMenu.setSelect(false);
                    }
                });
        List<SysMenu> resultList = MenuHelper.buildTree(sysMenus);
        return resultList;
    }
    @Transactional
    @Override
    public void doAssign(AssginMenuVo assignMenuVo) {
        LambdaQueryWrapper<SysRoleMenu> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, assignMenuVo.getRoleId());
        sysRoleMenuService.remove(wrapper);
        for(Long menuId : assignMenuVo.getMenuIdList()){
            if(StringUtils.isEmpty(menuId)){
                continue;
            }
            SysRoleMenu sysRoleMenu=new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assignMenuVo.getRoleId());
            sysRoleMenuService.save(sysRoleMenu);
        }
    }

    @Override
    public List<RouterVo> findUserMenuList(Long id) {
        List<SysMenu> sysMenuList = null;
        if(id.longValue()==1){
            sysMenuList =list(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getStatus, 1).orderByAsc(SysMenu::getSortValue));
        }else {
            sysMenuList = baseMapper.findListByUserId(id);
        }
        List<SysMenu> buildTree = MenuHelper.buildTree(sysMenuList);
        List<RouterVo> routerVoList = this.buildMenus(buildTree);
        return routerVoList;
    }

    private List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers=new ArrayList<>();
        for (SysMenu menu:menus){
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getRouterPath(menu));
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            List<SysMenu> menuChildren = menu.getChildren();
            if (menu.getType().intValue()==1){
                 List<SysMenu> hiddenMenuList = menuChildren.stream()
                        .filter(c -> !StringUtils.isEmpty(c.getComponent()))
                        .collect(Collectors.toList());
                 for (SysMenu hiddenMenu : hiddenMenuList) {
                        RouterVo hiddenRouter = new RouterVo();
                        hiddenRouter.setHidden(true);
                        hiddenRouter.setAlwaysShow(false);
                        hiddenRouter.setPath(getRouterPath(hiddenMenu));
                        hiddenRouter.setComponent(hiddenMenu.getComponent());
                        hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                        routers.add(hiddenRouter);
                }
            }else {
                if(!CollectionUtils.isEmpty(menuChildren)){
                    if(menuChildren.size() > 0) {
                        router.setAlwaysShow(true);
                    }
                    router.setChildren(buildMenus(menuChildren));
                }
            }
            routers.add(router);
        }
        return routers;
    }
    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if(menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }
    @Override
    public List<String> findUserPermsList(Long id) {
        List<SysMenu> sysMenus=null;
        if(id.longValue()==1){
            sysMenus=list(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getStatus, 1));
        }else {
            sysMenus = baseMapper.findListByUserId(id);
        }
        List<String> permsList = sysMenus.stream()
                .filter(menu -> menu.getType() == 2)
                .map(SysMenu::getPerms)
                .collect(Collectors.toList());
        return permsList;
    }
}
