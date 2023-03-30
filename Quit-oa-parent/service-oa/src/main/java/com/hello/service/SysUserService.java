package com.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.model.system.SysUser;
import com.hello.result.Result;
import com.hello.vo.system.SysUserQueryVo;

import java.util.Map;

public interface SysUserService extends IService<SysUser> {
    /**
     * 条件分页查询用户信息
     * @param pageNum
     * @param pageSize
     * @param sysUserQueryVo
     * @return
     */
    Result pageQueryUser(int pageNum, int pageSize, SysUserQueryVo sysUserQueryVo);

    /**
     * 更改用户状态
     * @param id
     * @param status
     */
    void updateStatus(Long id, Integer status);

    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    SysUser getByUsername(String username);

    /**
     * 获取和用户有关的信息
     * @param username
     * @return
     */
    Map<String, Object> getUserInfo(String username);

    /**
     * 查询当前用户信息
     * @return
     */
    Result getCurrentUser();
}