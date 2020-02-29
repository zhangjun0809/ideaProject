package com.atguigu.aclservice.service.impl;

import com.atguigu.aclservice.entity.Role;
import com.atguigu.aclservice.entity.UserRole;
import com.atguigu.aclservice.mapper.RoleMapper;
import com.atguigu.aclservice.service.RoleService;
import com.atguigu.aclservice.service.UserRoleService;
import com.atguigu.aclservice.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-26
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;
    //给用户分配角色
    @Override
    public void saveUserRoleRealtionShip(String uid, String[] roleIds) {
        //删除老的
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",uid);
        userRoleService.remove(wrapper);

        List<UserRole> list = new ArrayList<>();
        for (String roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(uid);
            list.add(userRole);
        }
        userRoleService.saveBatch(list);
    }

    //根据用户id获取角色信息
    @Override
    public List<Role> selectByUserId(String userId) {
        //根据用户id查询角色id
        List<UserRole> userRoles = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", userId).select("role_id"));
        List<String> roleIdList = userRoles.stream().map(it -> it.getRoleId()).collect(Collectors.toList());

        List<Role> roleList = new ArrayList<>();
        if (roleIdList.size()>0) {
            roleList = baseMapper.selectBatchIds(roleIdList);
        }
        return roleList;
    }
}

