package com.atguigu.serviceedu.controller;



import com.atguigu.commonutils.R;

import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.entity.vo.TeacherQuery;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
 * @author testjava
 * @since 2020-01-13
 */

@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/eduteacher")
@CrossOrigin
public class EduTeacherController {

    public static void main(String[] args) {
        System.out.println("根据id来查询");
    }

    @Autowired
    EduTeacherService teacherService;


    @ApiOperation(value = "根据ID来查询讲师")
    @GetMapping("getTeacherById/{id}")
    public R getTeacherById(@PathVariable String id){
        //int i = 10/0;
        try {
            //int i = 10/0;
        }catch (Exception e){
            throw new GuliException(20001,"出现自定义异常");
        }
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("eduTeacher",eduTeacher);
    }

    @ApiOperation(value = "所有讲师列表")
    @GetMapping
    public R getAllTeahcer() {
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }


    @ApiOperation(value = "分页查询讲师")
    @GetMapping("getTeacherPage/{current}/{limit}")
    public R getTeacherPage(@PathVariable Long current,
                            @PathVariable Long limit) {

        Page<EduTeacher> page = new Page<>(current, limit);
        teacherService.page(page, null);
        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        return R.ok().data("total", total).data("items", records);
    }

    @ApiOperation(value = "带条件分页查询讲师")
    @PostMapping("getTeacherPageVo/{current}/{limit}")
    public R getTeacherPageVo(@PathVariable Long current,
                              @PathVariable Long limit,
                              @RequestBody TeacherQuery teacherQuery) {

        Page<EduTeacher> page = new Page<>(current, limit);

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
        wrapper.orderByDesc("gmt_create");
        teacherService.page(page, wrapper);

        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();


        return R.ok().data("total", total).data("items", records);

    }

    @ApiOperation(value = "根据ID逻辑删除讲师")
    @DeleteMapping("{id}")
    public R deleteTeacherId(@PathVariable String id) {
        boolean remove = teacherService.removeById(id);
        return R.ok();
    }

    @ApiOperation(value = "新增讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {

        boolean save = teacherService.save(eduTeacher);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){

        boolean update = teacherService.updateById(eduTeacher);
        if(update){
            return R.ok();
        }else{
            return R.error();
        }
    }
}

