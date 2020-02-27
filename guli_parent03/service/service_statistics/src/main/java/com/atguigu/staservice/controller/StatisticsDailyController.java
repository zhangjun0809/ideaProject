package com.atguigu.staservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.staservice.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-25
 */
@Api(description = "统计分析")
@RestController
@CrossOrigin
@RequestMapping("/staservice/statistics")
public class StatisticsDailyController {

    @Autowired
    StatisticsDailyService  dailyService;

    @ApiOperation(value="获得统计数据")
    @PostMapping("createData/{day}")
    public R createData(@PathVariable String day){
        dailyService.createData(day);
        return R.ok();
    }
    @ApiOperation(value="返回图表显示数据")
    @GetMapping("showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type,
                      @PathVariable String begin,
                      @PathVariable String end){
        Map<String,Object> map = dailyService.getShowData(type,begin,end);
        return R.ok().data(map);
    }
}

