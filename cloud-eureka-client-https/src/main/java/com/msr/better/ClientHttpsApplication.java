package com.msr.better;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author MaiShuRen
 * @site http://www.maishuren.top
 * @date 2020-10-23
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ClientHttpsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientHttpsApplication.class, args);
    }
}
