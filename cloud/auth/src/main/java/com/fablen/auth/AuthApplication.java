package com.fablen.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhangbaosheng
 */
@SpringBootApplication
public class AuthApplication {
    private static final Logger logger = LoggerFactory.getLogger(AuthApplication.class);

    public static void main(String[] args) {
        logger.info("🚀 鉴权服务启动了！v1.0.10");
        SpringApplication.run(AuthApplication.class, args);
    }
}
