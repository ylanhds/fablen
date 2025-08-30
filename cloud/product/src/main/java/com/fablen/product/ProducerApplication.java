package com.fablen.product;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class ProducerApplication {
    private static final Logger logger = LoggerFactory.getLogger(ProducerApplication.class);

    public static void main(String[] args) {
        logger.info("🚀 商品服务启动了！v1.0.10");
        SpringApplication.run(ProducerApplication.class, args);
    }

}
