package com.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.model.wechat.Menu;
import com.hello.result.Result;

public interface WechatMenuService extends IService<Menu> {
    /**
     * 获取全部菜单
     * @return
     */
    Result findMenuInfo();

    /**
     * 同步菜单
     */
    void syncMenu();

    /**
     * 删除同步菜单
     */
    void removeMenu();
}
