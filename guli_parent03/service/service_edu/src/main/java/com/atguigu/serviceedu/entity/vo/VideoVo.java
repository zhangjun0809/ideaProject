package com.atguigu.serviceedu.entity.vo;

/**
 * @author Zhangjun
 * @create 2020-02-11 10:18
 */

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;


@ApiModel(value = "课时信息")
@Data
public class VideoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private Boolean isFree;
}