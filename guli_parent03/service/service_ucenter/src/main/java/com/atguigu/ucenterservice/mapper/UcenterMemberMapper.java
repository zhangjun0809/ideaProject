package com.atguigu.ucenterservice.mapper;

import com.atguigu.ucenterservice.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2020-02-17
 */
@Component
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    Integer countRegiste(String day);
}
