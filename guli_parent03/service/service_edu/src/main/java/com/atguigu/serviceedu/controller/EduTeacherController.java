package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.entity.vo.TeacherQuery;
import com.atguigu.serviceedu.service.EduTeacherService;
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
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-04
 */
@Api(description="讲师管理")
@RestController
@RequestMapping("/serviceedu/eduteacher")
@CrossOrigin
public class EduTeacherController {

    @Autowired

    EduTeacherService eduTeacherService;

    @ApiOperation(value = "所有讲师列表")

    @GetMapping
    public R getAllTeacher(){
        List<EduTeacher> list = eduTeacherService.list(null);
        return  R.ok().data("items",list);
    }

    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R deleteById(@PathVariable("id") String id){
        eduTeacherService.removeById(id);
        return R.ok();
    }

    @ApiOperation(value="分页查询讲师")
    @GetMapping("/getTeacherPage/{current}/{limit}")
    public R getTeacherPage(@PathVariable Long current,@PathVariable Long limit){
        Page<EduTeacher> page = new Page<>(current,limit);

        eduTeacherService.page(page,null);
        List<EduTeacher> records = page.getRecords();
        long total = page.getTotal();
        return  R.ok().data("total", total).data("items", records);
    }

    /*@ApiOperation(value="条件分页查询讲师")
    @PostMapping("/getTeacherPage/{current}/{limit}")
    public R getTeacherPageVo(@PathVariable Long current,
                              @PathVariable Long limit,
                              @RequestBody TeacherQuery teacherQuery){
        Page<EduTeacher> page = new Page<>(current,limit);
        String name = teacherQuery.getName();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        Integer level = teacherQuery.getLevel();
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(name)){
            wrapper.eq("level",level);
        }
        if (!StringUtils.isEmpty(name)){
            wrapper.ge("gmt_create",begin);
        }
        if (!StringUtils.isEmpty(name)){
            wrapper.le("gmt_create",end);
        }
        //@RequestBody 必须使用@PostMapping
        eduTeacherService.page(page,wrapper);
        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        return  R.ok().data("total", total).data("items", records);
    }*/
    @ApiOperation(value = "分页查询讲师")
    @PostMapping("/queryTeacherPageVo/{current}/{limit}")
    //@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public R queryTeacherPageVo(@PathVariable Long current,
                              @PathVariable Long limit,
                              @RequestBody TeacherQuery teacherQuery) {

        //1创建page对象
        Page<EduTeacher> page = new Page<>(current, limit);
        //取出参数
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        //2调方法获取数据
        eduTeacherService.page(page, wrapper);
        //3 从page获取数据
        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        //4 返回数据

        return R.ok().data("total", total).data("items", records);
    }
    @ApiOperation(value = "添加讲师")
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = eduTeacherService.save(eduTeacher);
        if (save){
           return  R.ok();
        }else{
            return R.error();
        }
    }
    @ApiOperation(value = "根据ID查询讲师")
    @PostMapping("/queryTeacherById/{id}")
    public R queryTeacherById(@PathVariable String id){
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("eduTeacher",eduTeacher);
    }

    @ApiOperation(value = "修改讲师")
    @PostMapping("/updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean b = eduTeacherService.updateById(eduTeacher);
        if (b){
            return R.ok();
        }else {
            return R.error();
        }
    }

}

