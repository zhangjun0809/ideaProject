package com.atguigu.servicevod.handler;

import com.aliyun.oss.ClientException;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;

/**
 * @author Zhangjun
 * @create 2020-02-14 9:31
 */
public class AliyunVodSDKUtils {
    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) throws ClientException {
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, "LTAI4FkefUCDSYfyzFa3sV4R","GV7fH2TPksnmF9v23p2SLhZ0GmAPTP");
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
}
