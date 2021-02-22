package com.msr.better.registry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MaiShuRen
 * @date 2020-12-18
 */


public class ServerController {

    @Autowired
    EurekaClientConfigBean eurekaClientConfigBean;

    @GetMapping("eureka-server")
    public Object getEurekaServerUrl() {
        return eurekaClientConfigBean.getServiceUrl();
    }
}
