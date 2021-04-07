package com.msr.better;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author MaiShuRen
 * @site http://www.maishuren.top
 * @date 2020-10-23
 */
@SpringBootApplication
@EnableZuulProxy
public class ZuulGatewayRegionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulGatewayRegionApplication.class, args);
    }
}
