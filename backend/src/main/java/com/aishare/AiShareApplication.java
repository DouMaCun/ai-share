package com.aishare;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.aishare.mapper")
public class AiShareApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiShareApplication.class, args);
    }
}
