package com.atguigu.serviceedu.service.impl;

import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.serviceedu.client.VodClient;
import com.atguigu.serviceedu.entity.EduChapter;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduCourseDescription;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.entity.vo.CourseInfoForm;
import com.atguigu.serviceedu.entity.vo.CoursePublishVo;
import com.atguigu.serviceedu.entity.vo.CourseQuery;
import com.atguigu.serviceedu.mapper.EduCourseMapper;
import com.atguigu.serviceedu.service.EduChapterService;
import com.atguigu.serviceedu.service.EduCourseDescriptionService;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.serviceedu.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    @Autowired
    EduChapterService chapterService;
    @Autowired
    EduVideoService videoService;
    @Autowired
    VodClient vodClient;


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

    //修改课程信息
    @Override
    public String updataCourse(CourseInfoForm courseInfoForm) {
        //1修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm,eduCourse);
        int i = baseMapper.updateById(eduCourse);
        if (i==0){
            throw new GuliException(20001,"修改课程信息失败");
        }
        //修改描述表
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(eduCourse.getId());
        eduCourseDescription.setDescription(courseInfoForm.getDescription());
        descriptionService.updateById(eduCourseDescription);

        return courseInfoForm.getId();
    }

    //根据ID查询课程发布信息
    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {
        CoursePublishVo coursePublishVo = baseMapper.getCoursePublishVoById(id);
        return coursePublishVo;
    }

    //获取所有课程信息 和查询功能
    @Override
    public void queryCourse(Page<EduCourse> pageParam, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("gmt_create");
        if (courseQuery==null){
            baseMapper.selectPage(pageParam,null);
            return;
        }
        if (!StringUtils.isEmpty(courseQuery.getSubjectId())){
            wrapper.eq("subject_id",courseQuery.getSubjectId());
        }
        if (!StringUtils.isEmpty(courseQuery.getSubjectParentId())){
            wrapper.eq("subject_parent_id",courseQuery.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(courseQuery.getTeacherId())){
            wrapper.eq("teacher_id",courseQuery.getTeacherId());
        }
        if (!StringUtils.isEmpty(courseQuery.getTitle())){
            wrapper.like("title",courseQuery.getTitle());
        }
        baseMapper.selectPage(pageParam,wrapper);

    }

    //删除课程信息
    @Override
    public void deleteCourse(String courseId) {
        //1根据课程id删除小节
        //TODO 删除小节同时删除视频
        //调用方法删除
        boolean b = videoService.removeByCourseId(courseId);

        //2根据课程id删除章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        chapterService.remove(wrapperChapter);

        //3根据课程id删除描述
        descriptionService.removeById(courseId);

        //4删除课程
        int i = baseMapper.deleteById(courseId);
        if (i==0){
            throw new GuliException(20001,"删除课程失败");
        }

    }
}
