package com.atguigu.wxservice.service;

import com.atguigu.wxservice.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-18
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    UcenterMember getwxInfoById(String openid);
}
