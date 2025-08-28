package com.fablen.service;

import com.fablen.consumer.OrderMessage;
import com.fablen.rabbitmq.client.RabbitMqClient;
import com.fablen.rabbitmq.messaging.MessagingConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMqService {

    @Autowired
    private RabbitMqClient rabbitMqClient;

    /**
     * 发送订单处理消息
     */
    public void sendOrderProcessingMessage(String orderId, String userId) {
        OrderMessage message = new OrderMessage(orderId, userId);
        rabbitMqClient.sendMessage(MessagingConstants.ORDER_PROCESSING_QUEUE, message);
    }

    /**
     * 发送延迟订单检查消息
     */
    public void sendDelayedOrderCheckMessage(String orderId, String userId) {
        OrderMessage message = new OrderMessage(orderId, userId);
        rabbitMqClient.sendDelayedMessage(MessagingConstants.ORDER_CHECK_QUEUE, message, 10000);
    }

    /**
     * 发送测试失败消息
     */
    public void sendTestFailedMessage() {
        OrderMessage message = new OrderMessage("FAIL", "testUser");
        rabbitMqClient.sendMessage(MessagingConstants.ORDER_PROCESSING_QUEUE, message);
    }
}
