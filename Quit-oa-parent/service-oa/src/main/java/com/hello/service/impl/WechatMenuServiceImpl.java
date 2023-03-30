package com.hello.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.mapper.WechatMenuMapper;
import com.hello.model.wechat.Menu;
import com.hello.result.Result;
import com.hello.service.WechatMenuService;
import com.hello.vo.wechat.MenuVo;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WechatMenuServiceImpl extends ServiceImpl<WechatMenuMapper, Menu>
        implements WechatMenuService {
    @Autowired
    private WxMpService wxMpService;
    @Override
    public Result findMenuInfo() {
        List<MenuVo> menuVos=findMenuvoList();
        return Result.ok(menuVos);
    }

    @Override
    public void syncMenu() {
        List<MenuVo> menuVoList = findMenuvoList();
        //菜单
        JSONArray buttonList = new JSONArray();
        for(MenuVo oneMenuVo : menuVoList) {
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            if(CollectionUtils.isEmpty(oneMenuVo.getChildren())) {
                one.put("type", oneMenuVo.getType());
                //前端工程对应的域名
                one.put("url", "http://oa.quit.cn/#"+oneMenuVo.getUrl());
            } else {
                JSONArray subButton = new JSONArray();
                for(MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                    JSONObject view = new JSONObject();
                    view.put("type", twoMenuVo.getType());
                    if(twoMenuVo.getType().equals("view")) {
                        view.put("name", twoMenuVo.getName());
                        //前端工程对应的域名
                        view.put("url", "http://oa.quit.cn#"+twoMenuVo.getUrl());
                    } else {
                        view.put("name", twoMenuVo.getName());
                        view.put("key", twoMenuVo.getMeunKey());
                    }
                    subButton.add(view);
                }
                one.put("sub_button", subButton);
            }
            buttonList.add(one);
        }
        //菜单
        JSONObject button = new JSONObject();
        button.put("button", buttonList);
        try {
            wxMpService.getMenuService().menuCreate(button.toJSONString());
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    private List<MenuVo> findMenuvoList(){
        List<MenuVo> menuVos=new ArrayList();
        List<Menu> menus = list();
        List<Menu> oneMenuList = menus.stream()
                .filter(menu -> menu.getParentId().longValue() == 0)
                .collect(Collectors.toList());
        for(Menu oneMenu : oneMenuList){
            MenuVo menuVo=new MenuVo();
            BeanUtils.copyProperties(oneMenu, menuVo);
            List<Menu> twoMenuList = menus.stream()
                    .filter(menu -> menu.getParentId().longValue() == menuVo.getId())
                    .sorted(Comparator.comparing(Menu::getSort))
                    .collect(Collectors.toList());
            List<MenuVo> chilrden=new ArrayList<>();
            for(Menu twoMenu : twoMenuList){
                MenuVo twoMenuVo=new MenuVo();
                BeanUtils.copyProperties(twoMenu, twoMenuVo);
                chilrden.add(twoMenuVo);
            }
            menuVo.setChildren(chilrden);
            menuVos.add(menuVo);
        }
        return menuVos;
    }
}
