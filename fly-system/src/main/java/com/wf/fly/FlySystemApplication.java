package com.wf.fly;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.wf.fly.mapper")
public class FlySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlySystemApplication.class, args);
    }

}
