package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.RandomUtil;
import com.atguigu.msmservice.service.MsmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Zhangjun
 * @create 2020-02-17 16:04
 */
@Api(description = "短信服务")
@CrossOrigin
@RestController
@RequestMapping("/edumsm")
public class MsmApiController {

    @Autowired
    private MsmService msmService;
    //注入 redis
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @ApiOperation(value="根据手机号发送验证码")
    @GetMapping("send/{phone}")
    public R sendMsmPhone(@PathVariable String phone){
        //1 从redis中根据手机号获取数据
        String rphone = redisTemplate.opsForValue().get(phone);
        //2如果能取出来数据直接返回
        if (!StringUtils.isEmpty(rphone)){
            return R.ok();
        }
        //3如果取不出来，调用接口发送短信
        //3.1生成验证码 随机四位数（使用工具，）
        String code = RandomUtil.getFourBitRandom();
        //3.2把生成的校验码封装到map中传递
        Map<String,String> map = new HashMap<>();
        map.put("code",code);
        //3.3调用service方法发送短信
        boolean isSuccess = msmService.sendMsm(phone,map);
        //3.4发送成功  把校验码放到redis中。key是手机号，value是验证码
        if (isSuccess){
            redisTemplate.opsForValue().set(phone,code,10, TimeUnit.MINUTES);
            return R.ok();
        }else{
            return R.error().message("发送信息失败");
        }


    }


}
