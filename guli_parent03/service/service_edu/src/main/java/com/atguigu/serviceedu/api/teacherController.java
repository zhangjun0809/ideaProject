package com.atguigu.serviceedu.api;

import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhangjun
 * @create 2020-02-19 15:20
 */
@Api(description = "前台讲师列表展示")
@CrossOrigin
@RestController
@RequestMapping("/eduservice/frontteacher")
public class teacherController {
    @Autowired
    EduTeacherService teacherService;

    @Autowired
    EduCourseService courseService;

    @ApiOperation(value="前台讲师分页查询功能")
    @GetMapping("getfrontteacherList/{current}/{limit}")
    public R getfrontteacherList(@PathVariable long current,@PathVariable long limit){
        Page<EduTeacher> page = new Page<>(current, limit);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper();
        wrapper.orderByDesc("gmt_create");
        teacherService.page(page,wrapper);

        List<EduTeacher> records = page.getRecords();
        long current1 = page.getCurrent();//当前页
        long total = page.getTotal();//总记录数
        long pages = page.getPages();//总页数
        long size = page.getSize();

        boolean hasNext = page.hasNext();//有没有下一页
        boolean hasPrevious = page.hasPrevious();//有没有上一页

        Map<String,Object> map = new HashMap<>();
        map.put("records",records);
        map.put("current",current1);
        map.put("total",total);
        map.put("pages",pages);
        map.put("size",size);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);

        return R.ok().data(map);
    }

    @ApiOperation(value="根据id查询讲师详情")
    @GetMapping("getTeacherInfo/{id}")
    public R getTeacherInfo(@PathVariable String id){
        //1查询讲师基本信息
        EduTeacher eduTeacher = teacherService.getById(id);
        //2查询讲师所讲课程信息
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",id);
        List<EduCourse> list = courseService.list(wrapper);
        return R.ok().data("eduTeacher",eduTeacher).data("list",list);
    }
}
