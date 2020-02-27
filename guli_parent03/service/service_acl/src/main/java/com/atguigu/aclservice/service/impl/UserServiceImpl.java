package com.atguigu.aclservice.service.impl;

import com.atguigu.aclservice.entity.Role;
import com.atguigu.aclservice.entity.User;
import com.atguigu.aclservice.entity.UserRole;
import com.atguigu.aclservice.mapper.UserMapper;
import com.atguigu.aclservice.service.RoleService;
import com.atguigu.aclservice.service.UserRoleService;
import com.atguigu.aclservice.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleService roleService;
    @Override
    public Map<String, Object> getRoleAssignByUid(String uid) {
        //查询所有角色
        List<Role> roleList = roleService.list(new QueryWrapper<Role>().orderByDesc("id"));
        //2根据用户id查询用户关联的角色id
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",uid);
        wrapper.select("role_id");
        List<UserRole> userRoleList = userRoleService.list(wrapper);

        //3userRoleList中的role_id去除放到另外一个list
        List<String> exisToleList = userRoleList.stream().map(c->c.getRoleId()).collect(Collectors.toList());
        
        //4遍历所有角色，判断
        List<Role> userFinalRole = new ArrayList<>();
        for (Role role : roleList) {
            if (exisToleList.contains(role.getId())){
                userFinalRole.add(role);
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("assignRoles", userFinalRole);
        map.put("allRolesList", roleList);
        return map;
    }
}
