package com.atguigu.serviceedu.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.client.VodClient;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.mapper.EduVideoMapper;
import com.atguigu.serviceedu.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-11
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    VodClient vodClient;
    @Override
    public boolean removeByCourseId(String courseId) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        List<EduVideo> eduVideos = baseMapper.selectList(wrapper);

        //封装视频id的集合
        ArrayList<String> videoIdList = new ArrayList<>();
        for (int i = 0; i < eduVideos.size(); i++) {
            EduVideo eduVideo = eduVideos.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            if (!StringUtils.isEmpty(videoSourceId)){
                videoIdList.add(videoSourceId);
            }
        }
        if (videoIdList.size()>0){
            vodClient.deleteVideoAliyunList(videoIdList);
        }
        QueryWrapper<EduVideo> wrapper2 = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        Integer delete = baseMapper.delete(wrapper);

        return null!= delete && delete>0;
    }
}
