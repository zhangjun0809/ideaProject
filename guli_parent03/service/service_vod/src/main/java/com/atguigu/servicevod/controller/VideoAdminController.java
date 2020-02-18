package com.atguigu.servicevod.controller;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.servicevod.handler.AliyunVodSDKUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Zhangjun
 * @create 2020-02-14 9:33
 */
@CrossOrigin
@RestController
@Api(description = "视频管理")
@RequestMapping("/eduvod/video")
public class VideoAdminController {
    @ApiOperation(value="上传视频")

    @PostMapping("uploadVideo")
    public R uploadVideo(MultipartFile file){
        //获取文件的文件名
        try {
            String fileName=file.getOriginalFilename();
            //2阿里云显示的名称
            String title=fileName.substring(0,fileName.lastIndexOf("."));
            //3获取文件流
            InputStream in = file.getInputStream();
            //4创建request
            UploadStreamRequest request = new UploadStreamRequest("LTAI4FkefUCDSYfyzFa3sV4R", "GV7fH2TPksnmF9v23p2SLhZ0GmAPTP", title, fileName, in);

            //5创建对象  提交请求  获得响应
            UploadVideoImpl uploadVideo = new UploadVideoImpl();
            UploadStreamResponse response = uploadVideo.uploadStream(request);

            String videoId="";
            if (response.isSuccess()){
                videoId = response.getVideoId();
            }else{
                videoId = response.getVideoId();
            }
            return R.ok().data("videoId",videoId);
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20001,"上传失败");
        }
    }
    @ApiOperation(value="删除视频")
    @DeleteMapping("{videoId}")
    public R deleteRideoAliyun(@PathVariable("videoId") String videoId){
        try {
//            String regionId = "cn-shanghai";  // 点播服务接入区域
//            DefaultProfile profile = DefaultProfile.getProfile(regionId, "LTAI4FkefUCDSYfyzFa3sV4R","GV7fH2TPksnmF9v23p2SLhZ0GmAPTP");
//            DefaultAcsClient client = new DefaultAcsClient(profile);
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient("LTAI4FkefUCDSYfyzFa3sV4R", "GV7fH2TPksnmF9v23p2SLhZ0GmAPTP");

            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(videoId);
            client.getAcsResponse(request);
            return R.ok();
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001,"删除失败");
        }

    }

    @ApiOperation(value="删除多个小节视频")
    @DeleteMapping("deleteVideoAliyunList")
    public R deleteVideoAliyunList(@RequestParam("videoIdList") List<String> videoIdList) {

        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient("LTAI4FkefUCDSYfyzFa3sV4R", "GV7fH2TPksnmF9v23p2SLhZ0GmAPTP");
            DeleteVideoRequest request = new DeleteVideoRequest();
            //
            String videoIds = StringUtils.join(videoIdList.toArray(),",");

            request.setVideoIds(videoIds);
            client.getAcsResponse(request);

            return R.ok();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return R.error();
    }
}
