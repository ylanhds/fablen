package com.fablen.rabbitmq.consumer;

import com.fablen.rabbitmq.annotation.RabbitComponent;
import com.fablen.rabbitmq.messaging.MessagingConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.nio.charset.StandardCharsets;

@RabbitComponent
@Slf4j
public class DeadLetterMessageConsumer {


    @RabbitListener(queues = MessagingConstants.DEAD_LETTER_QUEUE)
    public void handleDeadLetterMessage(Message message) {
        try {
            String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            String originalQueue = message.getMessageProperties().getReceivedRoutingKey();
            String exchange = message.getMessageProperties().getReceivedExchange();

            log.error("收到死信消息 - 原始队列: {}, 原始交换机: {}, 消息内容: {}",
                    originalQueue, exchange, messageBody);

            // 记录死信消息的详细信息
            log.error("死信原因: {}", message.getMessageProperties().getHeaders().get("x-death"));

            // 可以选择将死信消息存储到数据库或进行其他处理
            processDeadLetterMessage(messageBody, originalQueue, exchange);

        } catch (Exception e) {
            log.error("处理死信消息失败", e);
        }
    }

    private void processDeadLetterMessage(String messageBody, String originalQueue, String exchange) {
        // 实现死信消息的处理逻辑
        // 例如：存储到数据库、发送告警、人工处理等
        log.info("处理死信消息: 队列={} 交换机={} 内容={}", originalQueue, exchange, messageBody);

        // 示例：将死信消息转发到监控系统或存储到专门的表中
        // deadLetterMessageService.saveDeadLetterMessage(messageBody, originalQueue, exchange);
    }
}
