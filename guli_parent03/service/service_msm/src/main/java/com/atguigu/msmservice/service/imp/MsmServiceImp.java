package com.atguigu.msmservice.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.msmservice.service.MsmService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Zhangjun
 * @create 2020-02-17 16:06
 */
@Service
public class MsmServiceImp implements MsmService {
    @Override
    public boolean sendMsm(String phone, Map<String, String> map) {
        //1创建初始化对象
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI4FkefUCDSYfyzFa3sV4R", "GV7fH2TPksnmF9v23p2SLhZ0GmAPTP");
        //2拿到客户端
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "我的谷粒学院在线教育网站");
        request.putQueryParameter("TemplateCode", "SMS_183791339");
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));

        try {
            CommonResponse response = client.getCommonResponse(request);
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
