package com.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.model.system.SysRole;
import com.hello.result.Result;
import com.hello.vo.system.AssginRoleVo;
import com.hello.vo.system.SysRoleQueryVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {
    /**
     * 条件分页查询角色信息
     * @param pageNum
     * @param pageSize
     * @param queryVo
     * @return
     */
    Result pageQueryRole(Long pageNum, Long pageSize, SysRoleQueryVo queryVo);

    /**
     * 查询所有角色信息和当前用户的角色信息
     * @param userId
     * @return
     */
    Map<String, Object> findRoleByAdminId(Long userId);

    /**
     * 为当前用户分配角色
     * @param assginRoleVo
     */
    void doAssign(AssginRoleVo assginRoleVo);
}
