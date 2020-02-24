package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.orderservice.service.TPayLogService;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-22
 */
@Api(description = "支付管理")
@CrossOrigin
@RestController
@RequestMapping("/orderservice/pay")
public class TPayLogController {

    @Autowired
    TPayLogService payLogService;

    @ApiOperation(value="根据订单号生成二维码")
    @GetMapping("createNative/{orderId}")
    public R createNative(@PathVariable String orderId){
        Map map = payLogService.createNative(orderId);

        return R.ok().data(map);

    }
    @ApiOperation(value="查询支付状态")
    @GetMapping("queryOrderStatue/{orderId}")
    public R queryOrderStatue(@PathVariable String orderId){
        Map<String,String> map = payLogService.queryOrderStatue(orderId);
        if (map.get("trade_state").equals("SUCCESS")){
            payLogService.updateOrderState(map);
            return R.ok().message("支付成功");
        }

        return R.ok().code(25000).message("支付中");

    }
}

