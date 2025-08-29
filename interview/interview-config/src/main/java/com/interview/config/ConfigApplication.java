package com.interview.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigApplication {
    private static final Logger logger = LoggerFactory.getLogger(ConfigApplication.class);
    public static void main(String[] args) {
        logger.info("🚀 配置服务启动了！v1.0.10");
        SpringApplication.run(ConfigApplication.class, args);
    }

}
