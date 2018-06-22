package com.xietong.demo.config;

import com.xietong.phoenix.eaas.service.api.EAASServiceAPIRestProxy;
import com.xietong.phoenix.eaas.service.api.IEAASServiceAPI;
import com.xietong.phoenix.eaas.service.core.OAuthClientDetail;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dell on 2017/11/3.
 */
@Configuration
public class EAASCallerConfig {

    @Value("${eaas.config.clientId}")
    private String clientId;
    @Value("${eaas.config.clientSecret}")
    private String clientSecret;
    @Value("${eaas.config.server.dns}")
    private String authIp;

    @Bean
    public IEAASServiceAPI eaasServiceAPIRestProxy(){
        if(StringUtils.isNotBlank(clientId)&&StringUtils.isNotBlank(clientSecret)) {
            return new EAASServiceAPIRestProxy(authIp, new OAuthClientDetail(clientId, clientSecret));
        } else {
            return new EAASServiceAPIRestProxy(authIp);
        }
    }
}
