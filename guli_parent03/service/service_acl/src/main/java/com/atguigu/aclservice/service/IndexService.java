package com.atguigu.aclservice.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @author Zhangjun
 * @create 2020-02-28 14:13
 */
public interface IndexService {
    Map<String, Object> getUserInfo(String username);

    List<JSONObject> getMenu(String username);
}
