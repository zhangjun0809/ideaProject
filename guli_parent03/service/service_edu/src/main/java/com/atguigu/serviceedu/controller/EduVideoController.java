package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-11
 */
@Api(description = "小节管理")
@RestController
@CrossOrigin
@RequestMapping("/eduservice/eduvideo")
public class EduVideoController {

    @Autowired
    EduVideoService eduVideoService;

    @ApiOperation(value="添加小节")
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }
    @ApiOperation(value="删除小节")
    @DeleteMapping("{id}")
    //TODO 删除小节，需要删除阿里云对应的视频
    public R deleteVideo(@PathVariable String id){

        eduVideoService.removeById(id);
        return R.ok();
    }

    @ApiOperation(value="根据id查询小节")
    @GetMapping("{id}")
    public R getVideoInfo(@PathVariable String id){
        EduVideo eduVideo = eduVideoService.getById(id);
        return R.ok().data("eduVideo",eduVideo);
    }

    @ApiOperation(value="修改小节")
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }
}

