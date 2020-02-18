package com.atguigu.msmservice.service;

import java.util.Map;

/**
 * @author Zhangjun
 * @create 2020-02-17 16:05
 */
public interface MsmService {
    boolean sendMsm(String phone, Map<String, String> map);
}
