package com.atguigu.orderservice.client;

import com.atguigu.commonutils.VO.UcenterMemberPay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Zhangjun
 * @create 2020-02-22 15:22
 */
@Component
@FeignClient("service-ucenter")
public interface UcenterClient {

    @GetMapping("/ucenterservice/ucentermember/getUcenterPay/{memberId}")
    public UcenterMemberPay getUcenterPay(@PathVariable("memberId") String memberId);

}
