package com.atguigu.serviceedu.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

/**
 * @author Zhangjun
 * @create 2020-02-24 15:33
 */
@Component
public class OrderFileDegradeFeignClient  implements OrderQuery{
    @Override
    public boolean isByCourse(String cid, String mid) {
        return false;
    }
}
