package com.atguigu.aclservice.service;

import com.atguigu.aclservice.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-26
 */
public interface RoleService extends IService<Role> {

    void saveUserRoleRealtionShip(String uid, String[] roleIds);

    List<Role> selectByUserId(String id);
}
