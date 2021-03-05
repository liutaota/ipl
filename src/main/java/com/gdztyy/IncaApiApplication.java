package com.gdztyy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动类
 */
@SpringBootApplication
@EnableAsync
public class IncaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IncaApiApplication.class, args);
    }

}
