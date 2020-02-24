package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.TPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-22
 */
public interface TPayLogService extends IService<TPayLog> {

    Map createNative(String orderId);


    void updateOrderState(Map<String,String> map);

    Map<String, String> queryOrderStatue(String orderId);
}
