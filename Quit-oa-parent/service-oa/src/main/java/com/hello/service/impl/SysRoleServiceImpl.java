package com.hello.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.mapper.SysRoleMapper;
import com.hello.model.system.SysRole;
import com.hello.model.system.SysUserRole;
import com.hello.result.Result;
import com.hello.service.SysRoleService;
import com.hello.service.SysUserRoleService;
import com.hello.vo.system.AssginRoleVo;
import com.hello.vo.system.SysRoleQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Resource
    SysUserRoleService sysUserRoleService;
    @Override
    public Result pageQueryRole(Long pageNum, Long pageSize, SysRoleQueryVo queryVo) {
        LambdaQueryWrapper<SysRole> wrapper=new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(queryVo.getRoleName())){
            wrapper.like(SysRole::getRoleName, queryVo.getRoleName());
        }
        Page<SysRole> rolePage=new Page<>(pageNum, pageSize);
        page(rolePage, wrapper);
        return Result.ok(rolePage);
    }

    @Override
    public Map<String, Object> findRoleByAdminId(Long userId) {
        List<SysRole> sysRoleList=list();
        LambdaQueryWrapper<SysUserRole> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> userRoles = sysUserRoleService.list(wrapper);
        List<Long> existRoleIds = userRoles.stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
        List<SysRole> assginRoleList = new ArrayList<>();
        for(SysRole role : sysRoleList){
            if(existRoleIds.contains(role.getId())){
                assginRoleList.add(role);
            }
        }
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assginRoleList", assginRoleList);
        roleMap.put("allRolesList", sysRoleList);
        return roleMap;
    }
    @Transactional
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        LambdaQueryWrapper<SysUserRole> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);
        for(Long roleId : assginRoleVo.getRoleIdList()){
            if(StringUtils.isEmpty(roleId)){
                continue;
            }
            SysUserRole sysUserRole=new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleService.save(sysUserRole);
        }
    }
}
