package com.msr.better.config;

import com.netflix.discovery.EurekaClientConfig;
import com.netflix.eureka.EurekaServerConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author MaiShuRen
 * @site http://www.maishuren.top
 * @since 2021-04-03 14:19
 **/
@Configuration
@AutoConfigureBefore(EurekaServerAutoConfiguration.class)
public class RegionConfig {
    @Bean
    @ConditionalOnMissingBean
    public EurekaServerConfig eurekaServerConfig(EurekaClientConfig clientConfig) {
        EurekaServerConfigBean serverConfigBean = new EurekaServerConfigBean();
        if (clientConfig.shouldRegisterWithEureka()) {
            serverConfigBean.setRegistrySyncRetries(6);
        }
        serverConfigBean.setRemoteRegionAppWhitelist(new HashMap<>());
        return serverConfigBean;
    }
}
