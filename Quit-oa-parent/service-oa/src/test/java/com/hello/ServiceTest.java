package com.hello;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hello.model.system.SysMenu;
import com.hello.service.SysMenuService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class ServiceTest {
    @Resource
    private SysMenuService sysMenuService;

    @Test
    public void testMenuService(){
        LambdaQueryWrapper<SysMenu> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getType, 0).or()
                .eq(SysMenu::getType, 1)
                .eq(SysMenu::getStatus, 1);
        List<SysMenu> sysMenus = sysMenuService.list(wrapper);
        System.out.println(sysMenus);
        System.out.println("*********************");
        List<SysMenu> sysMenuList = sysMenuService.list(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getStatus, 1)
                .eq(SysMenu::getType, 2));
        System.out.println(sysMenuList);
    }
}
