package com.atguigu.serviceedu.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhangjun
 * @create 2020-02-08 15:43
 */
@Data
public class OnesubjuecVo {
    private String id;
    private String title;
    private List<TwoSubjectVo> children = new ArrayList<>();
}
