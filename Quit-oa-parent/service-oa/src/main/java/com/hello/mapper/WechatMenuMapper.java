package com.hello.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hello.model.wechat.Menu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WechatMenuMapper extends BaseMapper<Menu> {

}
