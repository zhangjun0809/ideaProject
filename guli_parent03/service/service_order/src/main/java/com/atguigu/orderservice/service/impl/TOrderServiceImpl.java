package com.atguigu.orderservice.service.impl;

import com.atguigu.commonutils.OrderNoUtil;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.VO.CourseFrontInfoPay;
import com.atguigu.commonutils.VO.UcenterMemberPay;
import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.UcenterClient;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.mapper.TOrderMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-22
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    EduClient eduClient;
    @Autowired
    UcenterClient ucenterClient;
    @Autowired
    TOrderService orderService;
    @Override
    public String creatOrders(String courseId, String memberId) {
        //收到生成订单号. 使用工具类（后加）
        String orderNo = OrderNoUtil.getOrderNo();
        //获得订单信息中的课程信息
        CourseFrontInfoPay courseInfoPay = eduClient.getCourseInfoPay(courseId);
        if (courseInfoPay==null) {
            throw new GuliException(20001,"课程获取失败");
        }
        //获得订单信息中的用户信息
        UcenterMemberPay ucenterPay = ucenterClient.getUcenterPay(memberId);
        if (ucenterPay==null) {
            throw new GuliException(20001,"用户信息获取失败");
        }
        //把订单数据添加到数据库
        TOrder order = new TOrder();

        order.setOrderNo(orderNo);
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoPay.getTitle());
        order.setCourseCover(courseInfoPay.getCover());
        order.setTeacherName("test");
        order.setTotalFee(courseInfoPay.getPrice());
        order.setMemberId(memberId);
        order.setMobile(ucenterPay.getMobile());
        order.setNickname(ucenterPay.getNickname());
        order.setStatus(0);//0未支付 1已支付
        order.setPayType(1);//支付类型 1微信
        baseMapper.insert(order);

        return orderNo;
    }


}
