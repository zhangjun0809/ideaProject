package com.atguigu.aclservice.mapper;

import com.atguigu.aclservice.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2020-02-26
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    //查询所有权限值
    List<String> selectAllPermissionValue();

    //根据用户id查询权限值
    List<String> selectPermissionValueByUserId(String userId);

    List<Permission> selectPermissionByUserId(String userId);
}
