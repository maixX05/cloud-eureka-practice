package com.msr.better;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author MaiShuRen
 * @site http://www.maishuren.top
 * @since 2021-04-03 14:18
 **/
@SpringBootApplication
@EnableEurekaServer
public class HttpBasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpBasicApplication.class, args);
    }
}
