package com.hello.controller;

import com.hello.exception.SystemException;
import com.hello.jwt.JwtHelper;
import com.hello.model.system.SysUser;
import com.hello.result.Result;
import com.hello.service.SysUserService;
import com.hello.utils.MD5;
import com.hello.vo.system.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system")
public class LoginController {
    @Resource
    SysUserService sysUserService;
    @ApiOperation("登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        SysUser sysUser = sysUserService.getByUsername(loginVo.getUsername());
        if(null == sysUser) {
            throw new SystemException(500,"用户不存在");
        }
        if(!MD5.encrypt(loginVo.getPassword()).equals(sysUser.getPassword())) {
            throw new SystemException(500,"密码错误");
        }
        if(sysUser.getStatus().intValue() == 0) {
            throw new SystemException(500,"用户被禁用");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("token", JwtHelper.createToken(sysUser.getId(), sysUser.getUsername()));
        return Result.ok(map);
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        String username = JwtHelper.getUsername(request.getHeader("token"));
        Map<String, Object> map = sysUserService.getUserInfo(username);
        return Result.ok(map);
    }
    @ApiOperation("退出")
    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }
}
