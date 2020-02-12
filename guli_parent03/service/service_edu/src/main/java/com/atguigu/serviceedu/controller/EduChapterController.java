package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduChapter;
import com.atguigu.serviceedu.entity.vo.ChapterVo;
import com.atguigu.serviceedu.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-11
 */
@Api(description = "章节管理")
@RestController
@CrossOrigin
@RequestMapping("/eduservice/educhapter")
public class EduChapterController {

    @Autowired
    EduChapterService eduChapterService;

    @ApiOperation(value="根据id查询章节信息")
    @GetMapping("getChapterVideoById/{courseId}")
    public R getChapterVideoById(@PathVariable String courseId){
        List<ChapterVo>  eduChapterVolist = eduChapterService.getChapterVideoById(courseId);
        return R.ok().data("eduChapterVolist",eduChapterVolist);
    }
    @ApiOperation(value="新增章节")
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        boolean save = eduChapterService.save(eduChapter);
        return R.ok();
    }
    @ApiOperation(value="删除章节")
    @DeleteMapping("{id}")
    public R deleteChapter(@PathVariable String id){
        eduChapterService.removeChapterById(id);
        return R.ok();
    }

    @ApiOperation(value="根据ID查询章节")
    @GetMapping("{id}")
    public R getChapterInfo(@PathVariable String id){
        EduChapter eduChapter = eduChapterService.getById(id);
        return R.ok().data("eduChapter",eduChapter);
    }

    @ApiOperation(value="修改章节")
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        boolean b = eduChapterService.updateById(eduChapter);
        return R.ok();
    }
}

