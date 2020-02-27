package com.atguigu.staservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Zhangjun
 * @create 2020-02-25 9:19
 */
@SpringBootApplication
@MapperScan("com.atguigu.staservice.mapper")
@ComponentScan({"com.atguigu"})
@EnableFeignClients
@EnableEurekaClient
@EnableScheduling
public class StaApplication {
    public static void main(String[] args) {
        SpringApplication.run(StaApplication.class,args);
    }
}
