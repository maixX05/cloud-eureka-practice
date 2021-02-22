package com.msr.better.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MaiShuRen
 * @date 2020-10-23
 */
@RestController
@RequestMapping("query")
public class ClientController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private EurekaClientConfigBean clientConfigBean;

    /**
     * 服务实例信息
     *
     * @return
     */
    @GetMapping("info")
    public Map<String, Object> info() {
        Map<String, Object> map = new HashMap<>(16);
        List<String> services = discoveryClient.getServices();
        String description = discoveryClient.description();
        int order = discoveryClient.getOrder();

        map.put("services", services);
        map.put("description", description);
        map.put("order", order);

        return map;
    }

    /**
     * 根据服务名称获取服务实例列表
     *
     * @param name 服务名
     * @return 返回结果
     */
    @GetMapping("instance/{name}")
    public List<ServiceInstance> getIns(@PathVariable String name) {
        return discoveryClient.getInstances(name);
    }

    /**
     * 获取Eureka Server的url
     *
     * @return
     */
    @GetMapping("eureka-server")
    public Object getServiceUrl() {
        return clientConfigBean.getServiceUrl();
    }
}
