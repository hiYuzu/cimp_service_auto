package com.sinosoft.cimp_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author hiYuzu
 * @version V1.0
 * @date 2021/9/10 15:47
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableScheduling
public class CimpServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CimpServiceApplication.class, args);
    }
}
