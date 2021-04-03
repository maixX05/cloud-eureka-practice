package com.msr.better;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 注册中心服务端
 *
 * @author MaiShuRen
 * @date 2020-10-23
 */
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaClientMultiApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClientMultiApplication.class, args);
    }
}
