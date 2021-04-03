package com.nsr.better.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 注册中心服务端
 *
 * @author MaiShuRen
 * @date 2020-10-23
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerMultiApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerMultiApplication.class, args);
    }
}
