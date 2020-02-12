package com.atguigu.serviceedu.service.impl;

import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.serviceedu.entity.EduChapter;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.entity.vo.ChapterVo;
import com.atguigu.serviceedu.entity.vo.VideoVo;
import com.atguigu.serviceedu.mapper.EduChapterMapper;
import com.atguigu.serviceedu.service.EduChapterService;
import com.atguigu.serviceedu.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-11
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    EduVideoService eduVideoService;
    @Override
    public List<ChapterVo> getChapterVideoById(String courseId) {
        //获取课程章节信息
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        List<EduChapter> chaptersList = baseMapper.selectList(wrapper);

        //获取课程小结信息
        QueryWrapper<EduVideo> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("course_id",courseId);
        List<EduVideo> videoList = eduVideoService.list(wrapper1);

        //返回的最终集合
        ArrayList<ChapterVo> objects = new ArrayList<>();
        Map<String,ChapterVo> map = new HashMap<>();

        for (int i = 0; i < chaptersList.size(); i++) {
            EduChapter eduChapter = chaptersList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            objects.add(chapterVo);
            map.put(chapterVo.getId(),chapterVo);

            //创建list用于封装小节信息
            List<VideoVo> videoVoList = new ArrayList<>();

            for (int m = 0; m <videoList.size() ; m++) {
                //4.1 得到每个小节
                EduVideo eduVideo = videoList.get(m);
                //4.2判断小节的chapterid和章节id是否相同
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoVoList.add(videoVo);
                }
            }

            //5 把封装好的小节存入对应章节
            chapterVo.setChildren(videoVoList);

        }
        return objects;
    }

    //章节里面有小节时不能删除
    @Override
    public void removeChapterById(String id) {
        //查询章节中有没有小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",id);
        int count = eduVideoService.count(wrapper);
        if (count==0){
            baseMapper.deleteById(id);
        }else{
            throw new GuliException(20001,"章节下有小节，不能删除");
        }
    }
}
