package com.atguigu.aclservice.controller;


import com.atguigu.aclservice.entity.Permission;
import com.atguigu.aclservice.service.PermissionService;
import com.atguigu.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-26
 */
@Api(description = "菜单管理")
@RestController
@CrossOrigin
@RequestMapping("/admin/acl/permission")
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    @ApiOperation(value="1、获取所有菜单")
    @GetMapping
    public R queryAll(){
        List<Permission> list = permissionService.queryAllMenu();
        return R.ok().data("children",list);
    }

    @ApiOperation(value="添加菜单")
    @PostMapping("save")
    public R save(@RequestBody Permission permission){
        boolean save = permissionService.save(permission);
        return R.ok();
    }

    @ApiOperation(value="3.1 根据id查询菜单")
    @GetMapping("getPemissionById/{id}")
    public R getPemissionById(@PathVariable String id){
        Permission permission = permissionService.getById(id);
        return R.ok().data("permission",permission);
    }

    @ApiOperation(value="3.2 修改菜单")
    @PostMapping("update")
    public R update(@RequestBody Permission permission){
        permissionService.updateById(permission);
        return R.ok();
    }

    //递归删除菜单
    @ApiOperation(value="4 递归删除菜单")
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id) {
        permissionService.removePermissionChild(id);
        return R.ok();
    }

    @ApiOperation(value="5、根据角色id获取菜单")
    @GetMapping("toAssign/{roleId}")
    public R toAssign(@PathVariable String roleId){
        List<Permission> list = permissionService.selectMenuByroleId(roleId);
        return R.ok().data("children",list);
    }

    @ApiOperation(value="6、给角色分配权限菜单")
    @PostMapping("doAssign")
    public R doAssign(String roleId,String[] permissionId){
        permissionService.doRoleAssignPermission(roleId,permissionId);
        return R.ok();
    }
}

