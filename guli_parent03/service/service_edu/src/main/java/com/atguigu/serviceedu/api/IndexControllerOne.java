package com.atguigu.serviceedu.api;

import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Zhangjun
 * @create 2020-02-17 9:28
 */
@Api(description = "首页banner展示")
@CrossOrigin
@RestController
@RequestMapping("/eduservice/index")
public class IndexControllerOne {

    @Autowired
    EduCourseService courseService;

    @Autowired
    EduTeacherService teacherService;

    @ApiOperation(value="首页展示8条课程，4个名师")
    @GetMapping
    public R getIndex(){
        //查询8个课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        //查询8条数据
        wrapper.last("limit 8");
        List<EduCourse> courselist = courseService.list(wrapper);

        //查询4个讲师
        QueryWrapper<EduTeacher> wrapper1 = new QueryWrapper<>();
        wrapper1.orderByDesc("id");
        wrapper1.last("limit 4");
        List<EduTeacher> teacherList = teacherService.list(wrapper1);
        return R.ok().data("courselist",courselist).data("teacherList",teacherList);
    }
}
