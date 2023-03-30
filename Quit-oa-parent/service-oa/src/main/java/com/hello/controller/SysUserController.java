package com.hello.controller;

import com.hello.model.system.SysUser;
import com.hello.result.Result;
import com.hello.service.SysUserService;
import com.hello.utils.MD5;
import com.hello.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Resource
    SysUserService sysUserService;

    @GetMapping("{pageNum}/{pageSize}")
    @ApiOperation("用户条件分页查询")
    public Result pageQueryUser(@PathVariable int pageNum, @PathVariable int pageSize,
                                SysUserQueryVo sysUserQueryVo){
        return sysUserService.pageQueryUser(pageNum,pageSize,sysUserQueryVo);
    }
    @ApiOperation(value = "获取用户")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }

    @ApiOperation(value = "保存用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser user) {
        sysUserService.save(user);
        return Result.ok();
    }

    @ApiOperation(value = "更新用户")
    @PutMapping("update")
    public Result updateById(@RequestBody SysUser user) {
        String password = user.getPassword();
        String userPassword = MD5.encrypt(password);
        user.setPassword(userPassword);
        sysUserService.updateById(user);
        return Result.ok();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.ok();
    }
    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        sysUserService.removeByIds(idList);
        return Result.ok();
    }
    @ApiOperation(value = "更新状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        sysUserService.updateStatus(id, status);
        return Result.ok();
    }
}
