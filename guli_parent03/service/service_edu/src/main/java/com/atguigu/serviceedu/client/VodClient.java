package com.atguigu.serviceedu.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Zhangjun
 * @create 2020-02-14 16:41
 */
@Component
@FeignClient(value = "service-vod",fallback = VodFileDegradeFeignClient.class)
public interface VodClient {

    //原封不动复制要调用的方法 包括注解。
    //url 拼全名
    //@pathVariable 必须添加参数名称
    @DeleteMapping("/eduvod/video/{videoId}")
    public R deleteRideoAliyun(@PathVariable("videoId") String videoId);

    @DeleteMapping("/eduvod/video/deleteVideoAliyunList")
    public R deleteVideoAliyunList(@RequestParam("videoIdList") List<String> videoIdList) ;

    }

