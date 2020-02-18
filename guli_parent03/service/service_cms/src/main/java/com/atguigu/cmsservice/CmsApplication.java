package com.atguigu.cmsservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Zhangjun
 * @create 2020-02-15 15:18
 */
@SpringBootApplication
@ComponentScan({"com.atguigu"})
@MapperScan("com.atguigu.cmsservice.mapper")
@EnableEurekaClient
@EnableFeignClients
public class CmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsApplication.class);
    }
}
