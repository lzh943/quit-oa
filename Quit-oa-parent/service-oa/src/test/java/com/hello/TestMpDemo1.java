package com.hello;

import com.hello.mapper.SysRoleMapper;
import com.hello.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class TestMpDemo1 {
    @Resource
    SysRoleMapper  sysRoleMapper;
    @Test
    public void test1(){
        System.out.println(("----- selectAll method test ------"));
        //***Mapper 中的 selectList() 方法的参数为 MP 内置的条件封装器 Wrapper
        //所以不填写就是无任何条件
        List<SysRole> users = sysRoleMapper.selectList(null);
        users.forEach(System.out::println);
    }
}
