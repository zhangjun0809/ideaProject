package com.atguigu.servicevod.handler;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Zhangjun
 * @create 2020-02-14 9:24
 */
public class ConstantPropertiesUtil implements InitializingBean{


    @Value("${aliyun.vod.file.keyid}")
    private String keyid;

    @Value("${aliyun.vod.file.keysecret}")
    private String keysecret;

    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID=keyid;
        ACCESS_KEY_SECRET=keysecret;
    }
}
