package com.atguigu.serviceedu.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Zhangjun
 * @create 2020-02-24 15:27
 */
@Component
@FeignClient(value = "service-order", fallback=OrderFileDegradeFeignClient.class)
public interface OrderQuery {

    @GetMapping("/orderservice/order/isByCourse/{cid}/{mid}")
    public boolean isByCourse(@PathVariable("cid") String cid, @PathVariable("mid") String mid);

    }
