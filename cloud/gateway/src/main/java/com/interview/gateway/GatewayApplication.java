package com.cloud.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhangbaosheng
 */

@SpringBootApplication
public class GatewayApplication {
    private static final Logger logger = LoggerFactory.getLogger(GatewayApplication.class);

    public static void main(String[] args) {
        logger.info("🚀 网关服务启动了！v1.0.10");
        SpringApplication.run(GatewayApplication.class, args);
    }

}
