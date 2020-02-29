package com.atguigu.aclservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.aclservice.entity.Permission;
import com.atguigu.aclservice.entity.RolePermission;
import com.atguigu.aclservice.entity.User;
import com.atguigu.aclservice.mapper.PermissionMapper;
import com.atguigu.aclservice.service.PermissionService;
import com.atguigu.aclservice.service.RolePermissionService;
import com.atguigu.aclservice.service.UserService;
import com.atguigu.aclservice.utils.MenuHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-26
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    UserService userService;

    @Autowired
    RolePermissionService rolePermissionService;

    //给角色分配菜单
    @Override
    public void doRoleAssignPermission(String roleId, String[] permissionId) {
        //插入新到的之前，把以前权限全部清除。再插入
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id",roleId);
        rolePermissionService.remove(wrapper);
        //保存角色与菜单关系
        List<RolePermission> rpList = new ArrayList<>();
        for (String s : permissionId) {
            //创建对象 赋值
            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermissionId(s);
            rolePermission.setRoleId(roleId);
            rpList.add(rolePermission);
        }
        rolePermissionService.saveBatch(rpList);
    }

    @Override
    public List<String> selectPermissionValueByUserId(String userid) {
        List<String> selectPermissionValueList=null;
        //判断当前用户是否是管理员，用户名是Admin
        //如果是管理员 查询所有的菜单值
        if (isAdmin(userid)) {
            selectPermissionValueList = baseMapper.selectAllPermissionValue();
        }else {
            selectPermissionValueList = baseMapper.selectPermissionValueByUserId(userid);
        }

        //如果不是管理员，根据用户Id查询用户菜单值
        return selectPermissionValueList;
    }

    //根据用户id获取菜单权限
    @Override
    public List<JSONObject> selectPermissionById(String id) {
        List<Permission> permissionList = null;
        //如果是管理员 查询所有菜单
        if (this.isAdmin(id)) {
            permissionList = baseMapper.selectList(null);
        }else {
            permissionList = baseMapper.selectPermissionByUserId(id);
        }
        //转化成数型对象
        List<Permission> bulid = bulid(permissionList);
        //转化成List<JSONObject>
        List<JSONObject> result = MenuHelper.bulid(bulid);
        return result;
    }

    //判断用户是否是管理员
    private boolean isAdmin(String userid){
        User user = userService.getById(userid);
        if (user != null && "admin".equals(user.getUsername())) {
            return true;
        }
        return false;
    }

    @Override
    public List<Permission> selectMenuByroleId(String roleId) {
        //1根据角色id 查询角色菜单关系表，查询角色关联所有菜单id
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id",roleId);
        List<RolePermission> rolePermissionList = rolePermissionService.list(wrapper);

        //2查询所有菜单
        List<Permission> allPermission = baseMapper.selectList(new QueryWrapper<Permission>().orderByDesc("id"));
        //3遍历所有菜单
        for (int i = 0; i <allPermission.size() ; i++) {
            Permission permission = allPermission.get(i);
            //遍历角色菜单List集合
            for (RolePermission rp : rolePermissionList) {
                if (rp.getPermissionId().equals(permission.getId())){
                    //让菜单选择是true
                    permission.setSelect(true);
                }
            }
        }
        //递归封装
        List<Permission> result = bulid(allPermission);
        return result;
    }

    //递归删除菜单
    @Override
    public void removePermissionChild(String id) {
        //吧所有要删除的菜单id获取到，放到list集合中
        List<String> idList = new ArrayList<>();
        this.selectIdList(id,idList);

        //把当前菜单id放到list集合中
        idList.add(id);
        //批量删除
        baseMapper.deleteBatchIds(idList);
    }

    private void selectIdList(String id, List<String> idList) {
        //根据当前菜单id 查询子菜单
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("pid",id);
        List<Permission> childList = baseMapper.selectList(wrapper);

        //遍历菜单id 放到list中，再进行下一级的查询
        childList.stream().forEach(item->{
            idList.add(item.getId());
            //递归
            this.selectIdList(item.getId(),idList);
        });
    }

    //递归获取所有
    @Override
    public List<Permission> queryAllMenu() {
        //1查询出所有的数据
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> permissionsList = baseMapper.selectList(wrapper);

        //2封装成需要的结构
        List<Permission> result = bulid(permissionsList);
        return result;
    }

    //封装成需要的结构
    private List<Permission> bulid(List<Permission> permissionsList) {
        //创建list集合，用于最终的返回数据
        List<Permission> trees = new ArrayList<>();
        //遍历所有的集合
        for (Permission permission : permissionsList) {
            //找出一级目录
            if ("0".equals(permission.getPid())){
                permission.setLevel(1);
                trees.add(findChildren(permission,permissionsList));
            }
        }
        return trees;
    }

    //递归方法
    private Permission findChildren(Permission permission, List<Permission> permissionsList) {

        permission.setChildren(new ArrayList<Permission>());

        for (Permission it  : permissionsList) {
            if (permission.getId().equals(it.getPid())){
                int level = permission.getLevel() + 1;
                it.setLevel(level);
                if (permission.getChildren() == null){
                    permission.setChildren(new ArrayList<>());
                }
                permission.getChildren().add(findChildren(it,permissionsList));
            }
        }
        return permission;
    }
}
