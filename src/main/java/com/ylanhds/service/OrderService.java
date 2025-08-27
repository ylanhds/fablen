package com.ylanhds.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private RabbitMqService rabbitMqService;

    /**
     * 创建订单并发送消息到队列
     */
    public void createOrder(String orderId, String userId) {
        try {
            // 创建订单的业务逻辑
            log.info("创建订单: orderId={}, userId={}", orderId, userId);

            // 发送消息到RabbitMQ
            rabbitMqService.sendOrderProcessingMessage(orderId, userId);

            log.info("订单消息已发送到队列: {}", orderId);
        } catch (Exception e) {
            log.error("发送订单消息失败: orderId={}", orderId, e);
            throw new RuntimeException("订单创建失败", e);
        }
    }

    /**
     * 发送延迟消息
     */
    public void sendDelayedOrderCheck(String orderId, String userId) {
        try {
            rabbitMqService.sendDelayedOrderCheckMessage(orderId, userId);
            log.info("延迟订单检查消息已发送: {}, 延迟10秒", orderId);
        } catch (Exception e) {
            log.error("发送延迟订单检查消息失败: orderId={}", orderId, e);
        }
    }

    // 在 OrderService 中添加测试方法
    public void sendFailedOrderMessage() {
        try {
            // 发送一个会失败的消息（订单ID为"FAIL"）
            rabbitMqService.sendTestFailedMessage();
            log.info("发送测试失败消息成功");
        } catch (Exception e) {
            log.error("发送测试失败消息失败", e);
        }
    }
}
