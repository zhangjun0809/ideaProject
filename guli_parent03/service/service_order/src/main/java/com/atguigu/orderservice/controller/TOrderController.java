package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.service.TOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.tracing.dtrace.ProviderAttributes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-22
 */
@Api(description = "订单管理")
@RestController
@CrossOrigin
@RequestMapping("/orderservice/order")
public class TOrderController {

    @Autowired
    TOrderService orderService;

    @ApiOperation(value="生成课程支付订单")
    @GetMapping("creatOrder/{courseId}")
    public R creatOrder(@PathVariable String courseId, HttpServletRequest request){
        //token获取用户ID
        //String memberId = JwtUtils.getMemberIdByJwtToken(request);
        String memberId="1231020763377610754";
        //生成订单，返回订单号。 参数 课程id，用户id
        String orderNo = orderService.creatOrders(courseId,memberId);
        return R.ok().data("orderNo",orderNo);
    }

    @ApiOperation(value="根据订单id获取订单信息")
    @GetMapping("getOrderInfo/{orderNo}")
    public R getOrderInfo(@PathVariable String orderNo){
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        TOrder orderInfo = orderService.getOne(wrapper);
        return R.ok().data("orderInfo",orderInfo);
    }
    @ApiOperation(value="查询课程是否被用户购买")
    @GetMapping("isByCourse/{cid}/{mid}")
    public boolean isByCourse(@PathVariable String cid,@PathVariable String mid){
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",cid);
        wrapper.eq("member_id",mid);
        wrapper.eq("status",1);
        int count = orderService.count(wrapper);
        return count>0;
    }
}

