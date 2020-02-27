package com.atguigu.aclservice;

import io.lettuce.core.dynamic.annotation.CommandNaming;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Zhangjun
 * @create 2020-02-26 10:43
 */
@SpringBootApplication
@MapperScan("com.atguigu.aclservice.mapper")
@ComponentScan({"com.atguigu"})
@EnableEurekaClient

public class AclApplication {
    public static void main(String[] args) {
        SpringApplication.run(AclApplication.class,args);
    }
}
