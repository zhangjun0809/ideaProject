package com.atguigu.cmsservice.service.impl;

import com.atguigu.cmsservice.entity.CrmBanner;
import com.atguigu.cmsservice.mapper.CrmBannerMapper;
import com.atguigu.cmsservice.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-15
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {


    //前台查询Banner的方法
    //
    @Cacheable(value = "banner", key = "'selectIndexList'")
    @Override
    public List<CrmBanner> getAllBanner() {
        List<CrmBanner> crmBanners = baseMapper.selectList(null);
        return crmBanners;
    }
    @CacheEvict(value = "banner", allEntries=true)
    @Override
    public void saveBanner(CrmBanner banner) {
        baseMapper.insert(banner);
    }
}
