package com.atguigu.aclservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.aclservice.entity.Role;
import com.atguigu.aclservice.entity.User;
import com.atguigu.aclservice.service.IndexService;
import com.atguigu.aclservice.service.PermissionService;
import com.atguigu.aclservice.service.RoleService;
import com.atguigu.aclservice.service.UserService;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Zhangjun
 * @create 2020-02-28 14:13
 */
@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> getUserInfo(String username) {
        //根据用户查询用户信息
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new GuliException(2000,"用户不存在");
        }
        //根据用户id获取角色信息(角色名称)
        List<Role> roleList = roleService.selectByUserId(user.getId());
        List<String> roleName = roleList.stream().map(it -> it.getRoleName()).collect(Collectors.toList());
        if (roleName.size() == 0) {
            roleName.add("");
        }
        //根据用户id获取权限信息
        List<String> permissionValue = permissionService.selectPermissionValueByUserId(user.getId());

        //权限信息存入redis
        redisTemplate.opsForValue().set(username,permissionValue);
        //返回封装好的数据返回封装好的数据
        Map<String,Object> result = new HashMap<>();
        result.put("name", user.getUsername());
        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        result.put("roles", roleName);
        result.put("permissionValueList", permissionValue);
        return result;
    }

    //根据用户名查询菜单信息//15236095384
    @Override
    public List<JSONObject> getMenu(String username) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        List<JSONObject> permissionList = permissionService.selectPermissionById(user.getId());
        return permissionList;
    }
}
