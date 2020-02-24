package com.atguigu.wxservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.wxservice.entity.UcenterMember;
import com.atguigu.wxservice.service.UcenterMemberService;
import com.atguigu.wxservice.utils.ConstantPropertiesUtil;
import com.atguigu.wxservice.utils.HttpClientUtils;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-18
 */
@CrossOrigin
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    UcenterMemberService memberService;

    //生成二维码

    @ResponseBody
    @GetMapping("login")
    public R genQrConnect() {

        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 回调地址
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL; //获取业务服务器重定向地址
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(20001, e.getMessage());
        }

        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        String state = "imhelen";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
        System.out.println("state = " + state);

        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟

        //生成qrcodeUrl
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state);
        System.out.println(qrcodeUrl);

        //return "redirect:" + qrcodeUrl;
        return R.ok().data("qrcodeUrl",qrcodeUrl);
    }
    @ApiOperation(value="微信扫码后回调")
    @GetMapping("callback")
    public String callback(String code,String state){
        System.out.println("code"+code);
        System.out.println(state);
        //1拿着code请求微信固定地址，得到openid和accessToken
        //向认证服务器发送请求换取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);
        System.out.println("accessTokenUrl:"+accessTokenUrl);
        try {
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo:"+accessTokenInfo);

            //解析json
            Gson gson = new Gson();
            HashMap hashMap = gson.fromJson(accessTokenInfo, HashMap.class);
            //从map中获取参数
            String openid = (String)hashMap.get("openid");
            String accessToken = (String)hashMap.get("access_token");

            //那着openid，access_token请求微信固定地址
            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);

            String userInfo = HttpClientUtils.get(userInfoUrl);
            //userInfo:{"openid":"o3_SC5052Nov_s1zdXOVIMisGmX8","nickname":"张俊","sex":1,"language":"zh_CN","city":"","province":"Beijing","country":"CN","headimgurl":"http:\/\/thirdwx.qlogo.cn\/mmopen\/vi_32\/Q0j4TwGTfTLz6VKIGCa3EKln2FEeIKQMHhwr79NiaENJIR926EHC7DowrIS7gaQWG4vfOFgWyBWeGLDs0oyUpFg\/132","privilege":[],"unionid":"oWgGz1NpzxZqy_IXigw7mkVZeLMs"}

            //System.out.println("userInfo:"+userInfo);
            //解析JSON，获取用户信息
            HashMap hashMap1 = gson.fromJson(userInfo, HashMap.class);
            String nickname = (String)hashMap1.get("nickname");
            String headimgurl = (String)hashMap1.get("headimgurl");

            //根据openid查询用户
            UcenterMember member = memberService.getwxInfoById(openid);
            if (member==null){
                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }
            //生成token，返回
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            return "redirect:http://localhost:3000?token="+jwtToken;

        } catch (Exception e) {
            e.printStackTrace();
            throw  new GuliException(20001,"扫描失败");
        }


    }
}
