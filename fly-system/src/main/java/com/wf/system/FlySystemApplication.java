package com.wf.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.wf.system.mapper")
public class FlySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlySystemApplication.class, args);
    }

}
