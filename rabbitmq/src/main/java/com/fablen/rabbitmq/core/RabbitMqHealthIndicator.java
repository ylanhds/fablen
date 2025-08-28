package com.fablen.rabbitmq.core;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMqHealthIndicator implements HealthIndicator {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public Health health() {
        try {
            // 尝试执行一个简单的操作来检查连接
            rabbitTemplate.execute(channel -> {
                // 检查连接是否打开
                return channel.getConnection().isOpen() && channel.isOpen();
            });
            return Health.up()
                    .withDetail("rabbitmq", "连接正常")
                    .withDetail("status", "available")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("rabbitmq", "连接异常")
                    .withDetail("error", e.getMessage())
                    .withException(e)
                    .build();
        }
    }
}
