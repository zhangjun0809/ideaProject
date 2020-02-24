package com.atguigu.orderservice.client;

import com.atguigu.commonutils.VO.CourseFrontInfoPay;
import com.atguigu.commonutils.VO.UcenterMemberPay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Zhangjun
 * @create 2020-02-22 14:43
 */
@Component
@FeignClient("service-edu")

public interface EduClient {

    @GetMapping("/eduservice/frontcourse/getCourseInfoPay/{courseId}")
    public CourseFrontInfoPay getCourseInfoPay(@PathVariable("courseId") String courseId);



}
