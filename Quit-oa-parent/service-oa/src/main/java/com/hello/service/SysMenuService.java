package com.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.model.system.SysMenu;
import com.hello.vo.system.AssginMenuVo;
import com.hello.vo.system.RouterVo;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    /**
     * 菜单树形数据
     * @return
     */
    List<SysMenu> findNodes();

    /**
     * 根据id删除菜单，需要判断是否有子菜单
     * @param id
     */
    void removeMenuById(Long id);
    /**
     * 查询所有菜单信息和当前角色的菜单信息
     * @param roleId
     * @return
     */
    List<SysMenu> findSysMenuByRoleId(Long roleId);
    /**
     * 为当前角色分配菜单
     * @param assignMenuVo
     */
    void doAssign(AssginMenuVo assignMenuVo);

    /**
     * 根据用户id获取菜单权限值
     * @param id
     * @return
     */
    List<RouterVo> findUserMenuList(Long id);

    /**
     * 根据用户id获取用户按钮权限
     * @param id
     * @return
     */
    List<String> findUserPermsList(Long id);
}
