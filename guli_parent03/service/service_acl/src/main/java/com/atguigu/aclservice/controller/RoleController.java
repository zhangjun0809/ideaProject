package com.atguigu.aclservice.controller;


import com.atguigu.aclservice.entity.Role;
import com.atguigu.aclservice.service.RoleService;
import com.atguigu.commonutils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-26
 */
@CrossOrigin
@Api(description = "角色管理")
@RestController
@RequestMapping("/aclservice/role")
public class RoleController {
    @Autowired
    RoleService roleService;

    @ApiOperation(value="1、角色带条件的分页查询")
    @PostMapping("{page}/{limit}")
    public R index(@PathVariable long page,
                   @PathVariable long limit,
                   @RequestBody(required = false) Role role){

        Page<Role> pageRole = new Page<>(page,limit);
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(role.getRoleName())){
            wrapper.like("role_name",role.getRoleName());
        }
        roleService.page(pageRole,wrapper);
        long total = pageRole.getTotal();
        List<Role> records = pageRole.getRecords();
        return R.ok().data("total",total).data("records",records);
    }

    @ApiOperation(value="2添加角色")
    @PostMapping("save")
    public R save(@RequestBody Role role) {
        roleService.save(role);
        return R.ok();
    }
    @ApiOperation(value="3根据角色id查询")
    @GetMapping("getRole/{id}")
    public R getRole(@PathVariable String id)   {
        Role role = roleService.getById(id);
        return R.ok().data("role",role);
    }
    @ApiOperation(value="3修改角色")
    @PostMapping("update")
    public R update(@RequestBody Role role) {
        roleService.updateById(role);
        return R.ok();
    }

    @ApiOperation(value="4删除角色")
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id) {
        roleService.removeById(id);
        return R.ok();
    }

    @ApiOperation(value = "5批量删除角色")
    @DeleteMapping("batchRemove")
    public R batchRemove(@RequestBody List<String> idList) {
        roleService.removeByIds(idList);
        return R.ok();
    }
}

