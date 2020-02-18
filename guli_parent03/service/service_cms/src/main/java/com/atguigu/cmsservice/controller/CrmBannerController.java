package com.atguigu.cmsservice.controller;


import com.atguigu.cmsservice.entity.CrmBanner;
import com.atguigu.cmsservice.service.CrmBannerService;
import com.atguigu.commonutils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-15
 */
@Api(description = "banner管理")
@CrossOrigin
@RestController
@RequestMapping("/cmsservice/banner")
public class CrmBannerController {
    @Autowired
    CrmBannerService bannerService;

    @ApiOperation(value="分页查询")
    @GetMapping("getBannerList/{current}/{limit}")
    public R getBannerList(@PathVariable long current,@PathVariable long limit){
        Page<CrmBanner> page = new Page<>(current,limit);
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        bannerService.page(page,wrapper);
        List<CrmBanner> records = page.getRecords();
        long total = page.getTotal();

        return R.ok().data("records",records).data("total",total);
    }
    @ApiOperation(value="添加banner")
    @PostMapping("addBanner")
    public R addBanner(@RequestBody CrmBanner  banner){
        boolean save = bannerService.save(banner);
        return R.ok();
    }
    @ApiOperation(value="删除Banner")
    @DeleteMapping
    public R deleteBanner(){
        return R.ok();
    }
    @ApiOperation(value="根据ID查询")
    @GetMapping("{id}")
    public R getBanner(@PathVariable String id){
        CrmBanner banner = bannerService.getById(id);
        return R.ok().data("banner",banner);
    }
    @ApiOperation(value="修改banner")
    @PostMapping("updataBanner")
    public R updataBanner(@RequestBody CrmBanner  banner ){
        bannerService.updateById(banner);
        return R.ok();
    }

}

