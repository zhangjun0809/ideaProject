package com.atguigu.serviceoss.controller;

import com.atguigu.commonutils.R;
import com.atguigu.serviceoss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zhangjun
 * @create 2020-02-07 18:44
 */
@Api(description="阿里云文件管理")
@CrossOrigin //跨域
@RestController
@RequestMapping("/serviceoss/oss")
public class OSSController {

    @Autowired
    FileService fileService;
    @ApiOperation(value="上传文件")
    @PostMapping("fileUpload")
    public R fileUploadOss(MultipartFile file){

        String url = fileService.uploadFileOss(file);
        return R.ok().data("url",url);
    }

}
