package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.vo.CourseInfoForm;
import com.atguigu.serviceedu.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}

