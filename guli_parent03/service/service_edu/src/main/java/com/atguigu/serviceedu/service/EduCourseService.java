package com.atguigu.serviceedu.service;

import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-10
 */
public interface EduCourseService extends IService<EduCourse> {

    String addCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseIn(String id);

    String updataCourse(CourseInfoForm courseInfoForm);

    CoursePublishVo getCoursePublishVoById(String courseId);

    void queryCourse(Page<EduCourse> pageParam, CourseQuery courseQuery);

    void deleteCourse(String courseId);

    Map<String, Object> getCouresePageList(Page<EduCourse> page, CourseFrontVo courseFrontVo);

    CourseFrontInfo getFrontCourseInfo(String id);
}
