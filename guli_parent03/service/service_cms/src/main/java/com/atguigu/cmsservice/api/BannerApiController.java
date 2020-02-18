package com.atguigu.cmsservice.api;

import com.atguigu.cmsservice.entity.CrmBanner;
import com.atguigu.cmsservice.service.CrmBannerService;
import com.atguigu.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Zhangjun
 * @create 2020-02-15 16:08
 */
@Api(description = "首页banner展示")
@CrossOrigin
@RestController
@RequestMapping("/cmsservice/bannerapi")
public class BannerApiController {
    @Autowired
    CrmBannerService bannerService;

    @ApiOperation(value="前台查询banner方法")
    @GetMapping("getAllBanner")
    public R getAllBanner(){
        List<CrmBanner> list = bannerService.getAllBanner();
        return R.ok().data("list",list);
    }

}
