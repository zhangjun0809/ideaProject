package com.atguigu.orderservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.commonutils.HttpClient;
import com.atguigu.commonutils.R;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.entity.TPayLog;
import com.atguigu.orderservice.mapper.TPayLogMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.orderservice.service.TPayLogService;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-22
 */
@Service
public class TPayLogServiceImpl extends ServiceImpl<TPayLogMapper, TPayLog> implements TPayLogService {

    @Autowired
    TOrderService orderService;
    @Override
    public Map createNative(String orderNo) {
        try {
            //1根据订单号获取订单信息
            QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no",orderNo);
            TOrder order = orderService.getOne(wrapper);
            if (order==null){
                throw new GuliException(20001,"订单失效");
            }
            //2封装参数值，用map集合
            Map m = new HashMap();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getCourseTitle());
            m.put("out_trade_no", orderNo);
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            m.put("spbill_create_ip", "127.0.0.1");
            //支付后的回调地址
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            m.put("trade_type", "NATIVE");

            //3、HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //4向HttpClient设置xml格式的参数,key是微信商户的key
            client.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //5发送请求得到返回结果
            String content = client.getContent();
            //xml转换成Map集合
            Map<String, String> map = WXPayUtil.xmlToMap(content);
            //从Map获取需要的值
            Map resultMap = new HashMap();
            resultMap.put("out_trade_no", orderNo);
            resultMap.put("course_id", order.getCourseId());
            resultMap.put("total_fee", order.getTotalFee());
            resultMap.put("result_code", map.get("result_code"));
            resultMap.put("code_url", map.get("code_url"));
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"生成二维码失败");
        }
    }

    @Override
    public void updateOrderState(Map<String,String> map) {
        //1 修改订单状态 改为1
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",map.get("out_trade_no"));
        TOrder order = orderService.getOne(wrapper);
        if (order==null){
            throw new GuliException(20001,"订单失效");
        }
        order.setStatus(1);
        orderService.updateById(order);
        //向订单支付日志表添加日志
        TPayLog payLog = new TPayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);//插入到支付日志表
    }

    //查询支付状态
    @Override
    public Map<String, String> queryOrderStatue(String orderNo) {
        try {
            //封装参数
           Map map = new HashMap();
            map.put("appid", "wx74862e0dfcf69954");
            map.put("mch_id", "1558950191");
            map.put("out_trade_no", orderNo);
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            //设置请求
            //2、HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");

            //client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(map, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            System.out.println("xml:"+xml);
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"支付失败");
        }
    }

}
