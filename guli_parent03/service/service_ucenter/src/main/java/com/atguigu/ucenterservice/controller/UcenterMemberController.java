package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}

