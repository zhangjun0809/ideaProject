package com.atguigu.aclservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.aclservice.service.IndexService;
import com.atguigu.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Zhangjun
 * @create 2020-02-28 14:10
 */
@Api(description = "登录后管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/acl/index")
public class IndexController {
    @Autowired
    IndexService indexService;
    //根据用户名获取用户信息
    @ApiOperation(value="根据用户名获取用户信息")
    @GetMapping("info")
    public R info(){
        //获取当前登录用户用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据用户名获取用户信息
        Map<String,Object> map = indexService.getUserInfo(username);
        return R.ok().data(map);
    }
    //根据用户名获取菜单信息
    @ApiOperation(value="根据用户名获取菜单信息")
    @GetMapping("menu")
    public R getMenu(){
        //获取当前登录用户用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<JSONObject> permissionList = indexService.getMenu(username);
        return R.ok().data("permissionList", permissionList);
    }

    @PostMapping("logout")
    public R logout(){
        return R.ok();
    }
}
