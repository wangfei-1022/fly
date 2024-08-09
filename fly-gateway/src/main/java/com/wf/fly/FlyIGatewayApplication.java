package com.wf.fly;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.wf.fly.mapper")
public class FlyIGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlyIGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("sys-service", r -> r
                        .path("/api/system/**")
                        .uri("http://localhost:9001") // 用户服务的地址
                ).route("imaotai-service", r -> r
                        .path("/api/imaotai/**")
                        .uri("http://localhost:9002") // 用户服务的地址
                )
                // 添加其他路由规则
                .build();
    }

}
