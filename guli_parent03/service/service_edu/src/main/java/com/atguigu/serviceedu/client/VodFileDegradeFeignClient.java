package com.atguigu.serviceedu.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zhangjun
 * @create 2020-02-15 10:45
 */
@Service
public class VodFileDegradeFeignClient implements  VodClient {
    @Override
    public R deleteRideoAliyun(String videoId) {
        return R.error();
    }

    @Override
    public R deleteVideoAliyunList(List<String> videoIdList) {
        return R.error();
    }
}
