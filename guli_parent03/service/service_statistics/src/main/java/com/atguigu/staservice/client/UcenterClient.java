package com.atguigu.staservice.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Zhangjun
 * @create 2020-02-25 9:23
 */
@Component
@FeignClient(value = "service-ucenter")
public interface UcenterClient {

    @GetMapping("/ucenterservice/ucentermember/countRegister/{day}")
    public R countRegiste(@PathVariable("day") String day);

    }
