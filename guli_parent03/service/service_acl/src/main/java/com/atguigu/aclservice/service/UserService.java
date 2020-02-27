package com.atguigu.aclservice.service;

import com.atguigu.aclservice.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-26
 */
public interface UserService extends IService<User> {

    Map<String, Object> getRoleAssignByUid(String uid);
}
