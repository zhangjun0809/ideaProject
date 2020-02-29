package com.atguigu.aclservice.service.impl;


import com.atguigu.aclservice.entity.User;
import com.atguigu.aclservice.service.PermissionService;
import com.atguigu.aclservice.service.UserService;
import com.atguigu.security.entity.SecurityUser;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author Zhangjun
 * @create 2020-02-28 11:04
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1根据用户名获取用户信息
        User username1 = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if (username1 == null) {
            throw new GuliException(20001,"用户不存在");
        }
        com.atguigu.security.entity.User user = new com.atguigu.security.entity.User();
        BeanUtils.copyProperties(username1,user);
        //2把获取的用户信息对象转换成权限框架的用户对象

        //3根据用户id获取用户权限信息（菜单值）
        List<String> authorities = permissionService.selectPermissionValueByUserId(username1.getId());

        //4封装得到的信息返回给权限框架
        SecurityUser securityUser = new SecurityUser(user);
        securityUser.setPermissionValueList(authorities);
        return securityUser;
    }
}
