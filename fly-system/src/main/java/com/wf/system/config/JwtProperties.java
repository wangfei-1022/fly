package com.wf.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wf.jwt")
@Data
public class JwtProperties {

    private String SecretKey;
    private long Ttl;
    private String TokenName;

}