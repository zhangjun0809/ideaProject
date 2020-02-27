package com.atguigu.ucenterservice.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.mapper.UcenterMemberMapper;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.stream.BaseStream;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-17
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Override
    public void register(RegisterVo member) {
        //获取注册信息，进行校验
        String nickname = member.getNickname();
        String mobile = member.getMobile();
        String password = member.getPassword();
        String code = member.getCode();

        if (StringUtils.isEmpty(nickname)||StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)||StringUtils.isEmpty(code)){
            throw new GuliException(20001,"注册失败,参数为空");
        }
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count>0){
            throw  new GuliException(20001,"该手机号已经注册过");
        }
        String  o = redisTemplate.opsForValue().get(mobile);

        if (!code.equals(o)){
            throw new GuliException(20001,"验证码有误");
        }
        UcenterMember ucenterMember = new UcenterMember();
       // BeanUtils.copyProperties(member,ucenterMember);方法二 复制对象属性
        ucenterMember.setMobile(mobile);
        ucenterMember.setNickname(nickname);
        ucenterMember.setPassword(MD5.encrypt(password));

        ucenterMember.setAvatar("https://gulizhangjun.oss-cn-beijing.aliyuncs.com/null/2020/02/07/b50011e3-f74f-4f30-ba67-ed5d135b9308.png");
        ucenterMember.setIsDisabled(false);

        int insert = baseMapper.insert(ucenterMember);
        if (insert==0){
            throw new GuliException(20001,"注册失败");
        }

    }

    @Override
    public String login(UcenterMember member) {

        String mobile = member.getMobile();
        String password = member.getPassword();
        //1 判断手机号和密码是否为空
        if (StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
            throw new GuliException(20001,"手机号或密码有误");
        }
        //根据手机号查询
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        if (ucenterMember==null){
            throw new GuliException(20001,"手机号或密码有误");
        }
        //3判断密码
        String password1 = ucenterMember.getPassword();
        String encrypt = MD5.encrypt(password);
        if (!password1.equals(encrypt)) {
            throw new GuliException(20001,"手机号或密码有误");
        }
        //4判断用户是否被禁用
        Boolean isDisabled = ucenterMember.getIsDisabled();
        if (isDisabled) {
            throw new GuliException(20001,"手机号或密码有误(禁用)");
        }
        //5生成token字符串
        String id = ucenterMember.getId();
        String nickname = ucenterMember.getNickname();
        String jwtToken = JwtUtils.getJwtToken(id, nickname);
        return jwtToken;
    }

    @Override
    public Integer countRegiste(String day) {
        Integer count = baseMapper.countRegiste(day);

        return count;
    }
}
