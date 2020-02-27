package com.atguigu.serviceedu.api;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;

import com.atguigu.commonutils.VO.CourseFrontInfoPay;
import com.atguigu.serviceedu.client.OrderQuery;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.vo.*;
import com.atguigu.serviceedu.service.EduChapterService;
import com.atguigu.serviceedu.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Zhangjun
 * @create 2020-02-21 10:12
 */
@Api(description = "前台课程展示")
@CrossOrigin
@RestController
@RequestMapping("/eduservice/frontcourse")
public class CourseController {
    @Autowired
    EduCourseService courseService;
    @Autowired
    EduChapterService chapterService;
    @Autowired
    OrderQuery orderQuery;

    @ApiOperation(value="前台课程条件分页查询功能")
    @PostMapping("getFrontCourseList/{current}/{limit}")
    public R getFrontCourseList(@PathVariable long current,
                                @PathVariable long limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo){
        Page<EduCourse> page = new Page<>(current,limit);
        Map<String,Object> map = courseService.getCouresePageList(page,courseFrontVo);

        return R.ok().data(map);
    }

    @ApiOperation(value = "根据ID查询课程详情")
    @GetMapping("getCourseInfo/{id}")
    public R  getCourseInfo(@PathVariable String id, HttpServletRequest request){
        //1查询课程基本信息
        CourseFrontInfo CourseFrontInfo = courseService.getFrontCourseInfo(id);
        //2课程相关大纲数据
        List<ChapterVo> chapterVideo = chapterService.getChapterVideoById(id);
        //3查询课程是否被购买
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        boolean isbByCourse = orderQuery.isByCourse(id, memberId);
        return R.ok().data("CourseFrontInfo",CourseFrontInfo)
                .data("chapterVideo",chapterVideo)
                .data("isbByCourse",isbByCourse);
    }

    @ApiOperation(value="根据ID查询课程详情,支付订单远程调用")
    @GetMapping("getCourseInfoPay/{courseId}")
    public CourseFrontInfoPay getCourseInfoPay(@PathVariable String courseId){

        CourseFrontInfo CourseFrontInfo = courseService.getFrontCourseInfo(courseId);
        CourseFrontInfoPay courseFrontInfoPay = new CourseFrontInfoPay();
        BeanUtils.copyProperties(CourseFrontInfo,courseFrontInfoPay);
        return courseFrontInfoPay;
    }
}
