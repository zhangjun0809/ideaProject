package com.atguigu.aclservice.service;

import com.atguigu.aclservice.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-26
 */
public interface PermissionService extends IService<Permission> {

    List<Permission> queryAllMenu();

    void removePermissionChild(String id);

    List<Permission> selectMenuByroleId(String roleId);

    void doRoleAssignPermission(String roleId, String[] permissionId);
}
