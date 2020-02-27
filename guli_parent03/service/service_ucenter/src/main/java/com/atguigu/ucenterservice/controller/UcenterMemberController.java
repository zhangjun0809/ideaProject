package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.VO.UcenterMemberPay;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-17
 */
@Api(description = "前端登录注册管理")
@RestController
@CrossOrigin
@RequestMapping("/ucenterservice/ucentermember")
public class UcenterMemberController {

    @Autowired
    UcenterMemberService memberService;

    @ApiOperation(value="注册方法")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo member){
        memberService.register(member);
        return R.ok();
    }
    @ApiOperation(value="登录方法")
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member ){
        //返回token字符串 包含用户信息
        String token = memberService.login(member);
        return R.ok().data("token",token);
    }

    @ApiOperation(value="根据token字符串获取用户信息")
    @PostMapping("getInfoToken")
    public R getInfoToken(HttpServletRequest request){
        //根据jwt字符串获取id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //根据用户id获得用户信息
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("member",member);
    }
    @ApiOperation(value = "根据用户id获取用户信息，供订单模块调用")
    @GetMapping("getUcenterPay/{memberId}")
    public UcenterMemberPay getUcenterPay(@PathVariable("memberId") String memberId){
        UcenterMember member = memberService.getById(memberId);
        UcenterMemberPay ucenterMemberPay = new UcenterMemberPay();
        BeanUtils.copyProperties(member,ucenterMemberPay);
        return ucenterMemberPay;
    }

    @ApiOperation(value="统计某一天的注册人数，远程调用")
    @GetMapping("countRegister/{day}")
    public R countRegiste(@PathVariable String day){
        Integer count = memberService.countRegiste(day);
        return R.ok().data("count",count);
    }

}

