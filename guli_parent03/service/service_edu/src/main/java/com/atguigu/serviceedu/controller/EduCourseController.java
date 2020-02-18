package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.vo.CourseInfoForm;
import com.atguigu.serviceedu.entity.vo.CoursePublishVo;
import com.atguigu.serviceedu.entity.vo.CourseQuery;
import com.atguigu.serviceedu.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-10
 */
@Api(description = "课程管理")
@RestController
@CrossOrigin
@RequestMapping("/eduservice/course")
public class EduCourseController {

    @Autowired
    EduCourseService eduCourseService;


    @ApiOperation(value = "添加课程信息")
    @PostMapping("/addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoForm courseInfoForm){
        String id = eduCourseService.addCourseInfo(courseInfoForm);
        System.out.println(id);

        return R.ok().data("id",id);
    }
    @ApiOperation(value="根据课程ID查询课程信息")
    @GetMapping("/nimalegebi/{id}")
    public R getCourseInfoId(@PathVariable("id") String id){
        CourseInfoForm courseInfoForm = eduCourseService.getCourseIn(id);

        return R.ok().data("courseInfoForm",courseInfoForm);
    }
    @ApiOperation(value="修改课程信息")
    @PostMapping("updataCourse")
    public R updataCourse(@RequestBody CourseInfoForm courseInfoForm){
        String id = eduCourseService.updataCourse(courseInfoForm);

        return R.ok().data("id",id);
    }

    @ApiOperation(value = "根据ID获取课程发布信息")
    @GetMapping("getCoursePublishVoById/{courseId}")
    public R getCoursePublishVoById(@PathVariable String courseId){
        CoursePublishVo coursePublishVo = eduCourseService.getCoursePublishVoById(courseId);
        return R.ok().data("coursePublishVo", coursePublishVo);

    }

    @ApiOperation(value="发布课程信息")
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse = eduCourseService.getById(id);
        eduCourse.setStatus("Normal");
        eduCourseService.updateById(eduCourse);
        return R.ok();
    }

    @ApiOperation(value="获取所有课程详情")
    @PostMapping("queryCourse/{page}/{limit}")
    public R queryCourse(@PathVariable long page, @PathVariable long limit,@RequestBody CourseQuery courseQuery){
        Page<EduCourse> pageParam = new Page<>(page,limit);

        eduCourseService.queryCourse(pageParam,courseQuery);

        List<EduCourse> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("records",records).data("total",total);
    }
    @ApiOperation(value="删除课程以及相关信息")
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId){
        eduCourseService.deleteCourse(courseId);
        return R.ok();
    }
}

