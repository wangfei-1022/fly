package com.wf.imaotai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        // 对所有 RestTemplate 的消息转换器进行遍历
        restTemplate.getMessageConverters().forEach(httpMessageConverter -> {
            // 找到 httpMessageConverter，修改默认的字符编码
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(java.nio.charset.StandardCharsets.UTF_8);
            }
        });
        return restTemplate;
    }
}
