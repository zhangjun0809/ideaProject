package com.atguigu.serviceedu.service.impl;

import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduCourseDescription;
import com.atguigu.serviceedu.entity.vo.CourseInfoForm;
import com.atguigu.serviceedu.mapper.EduCourseMapper;
import com.atguigu.serviceedu.service.EduCourseDescriptionService;
import com.atguigu.serviceedu.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-10
 */
@Service

public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    EduCourseDescriptionService descriptionService;
    @Override
    public String addCourseInfo(CourseInfoForm courseInfoForm) {
        //1向课程表添加数据
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if(insert==0){//添加课程信息失败
            throw new GuliException(20001,"添加课程信息失败");
        }
        //获取添加成功后的课程ID
        String id = eduCourse.getId();
        //2向课程描述表添加数据
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(id);
        eduCourseDescription.setDescription(courseInfoForm.getDescription());
        descriptionService.save(eduCourseDescription);
        return id;
    }

    @Override
    public CourseInfoForm getCourseIn(String id) {
        //查询课程信息
        EduCourse eduCourse = baseMapper.selectById(id);
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(eduCourse,courseInfoForm);
        //查询课程描述信息
        EduCourseDescription byId = descriptionService.getById(id);
        courseInfoForm.setDescription(byId.getDescription());
        return courseInfoForm;
    }
}
