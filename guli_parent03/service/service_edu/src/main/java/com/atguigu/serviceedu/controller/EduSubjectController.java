package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.vo.OnesubjuecVo;
import com.atguigu.serviceedu.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-08
 */
@CrossOrigin
@RestController
@Api(description = "课程分离管理")
@RequestMapping("/eduservice/edusubject")
public class EduSubjectController {

    @Autowired
    EduSubjectService eduSubjectService;



    @ApiOperation(value="导入添加课程分类")
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        //调用接口读取文件存入数据库
        List<String> magList = eduSubjectService.importSubjectData(file);
        if(magList.size()==0){//无错误
            return R.ok();
        }else{

            return R.error().data("magList", magList);
        }
    }

    @ApiOperation(value="查询课程分类")
    @GetMapping
    public R getAllSubject(){
        List<OnesubjuecVo> allSubject=eduSubjectService.getAllSubject();
        return R.ok().data("allSubject",allSubject);
    }

}

