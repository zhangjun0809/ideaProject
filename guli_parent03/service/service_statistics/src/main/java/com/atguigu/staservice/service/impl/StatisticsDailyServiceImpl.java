package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-25
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    UcenterClient ucenterClient;
    @Override
    public void createData(String day) {
        //远程调用ucenter接口 得到注册人数
        R r = ucenterClient.countRegiste(day);
        Integer count = (Integer)r.getData().get("count");

        //删除之前 相同日期的统计
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);

        //存入数据库
        StatisticsDaily statisticsDaily1 = new StatisticsDaily();
        statisticsDaily1.setRegisterNum(count);
        statisticsDaily1.setDateCalculated(day);
        statisticsDaily1.setCourseNum(100);
        statisticsDaily1.setLoginNum(100);
        statisticsDaily1.setVideoViewNum(100);

        baseMapper.insert(statisticsDaily1);
    }

    //获取图表显示需要的数据
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        //根据时间范围查询数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.select("date_calculated",type);
        wrapper.between("date_calculated",begin,end);
        List<StatisticsDaily> list = baseMapper.selectList(wrapper);

        //创建两个list集合  封装数据
        ArrayList<String> dataCalclatedList = new ArrayList<>();
        ArrayList<Integer> dataList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            StatisticsDaily sta = list.get(i);
            String date = sta.getDateCalculated();
            dataCalclatedList.add(date);

            switch (type){
                case "login_num":
                    Integer loginNum = sta.getLoginNum();
                    dataList.add(loginNum);
                    break;
                case "register_num":
                    Integer registerNum = sta.getRegisterNum();
                    dataList.add(registerNum);
                    break;
                case "video_view_num":
                    Integer videoViewNum = sta.getVideoViewNum();
                    dataList.add(videoViewNum);
                    break;
                case "course_num":
                    Integer courseNum = sta.getCourseNum();
                    dataList.add(courseNum);
                    break;
                default:
                    break;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("dataCalclatedList",dataCalclatedList);
        map.put("dataList",dataList);
        return map;
    }
}
