package com.fablen.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.fablen.rabbitmq.annotation.RabbitComponent;
import com.fablen.rabbitmq.core.GenericMessageHandler;
import com.fablen.rabbitmq.messaging.MessagingConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;

/**
 * 队列: ORDER_PROCESSING_QUEUE (order.processing.queue)
 * 职责: 处理订单创建后的即时业务逻辑
 * 触发时机: 订单创建时立即发送消息
 * 处理逻辑: 订单的主业务流程处理
 */
@RabbitComponent
@Slf4j
public class OrderMessageConsumer {
    @Autowired
    private GenericMessageHandler messageHandler;

    @RabbitListener(queues = MessagingConstants.ORDER_PROCESSING_QUEUE)
    public void handleOrderMessage(OrderMessage message, Channel channel,
                                   @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        // 修复方法引用问题，使用 lambda 表达式
        messageHandler.handleMessage(message, deliveryTag, channel, (msg, ch) -> processOrder(msg));
    }

    private void processOrder(OrderMessage orderMessage) throws Exception {
        log.info("处理订单消息: 订单ID={}, 用户ID={}", orderMessage.getOrderId(), orderMessage.getUserId());

        // 模拟业务处理
        if (orderMessage.getOrderId() == null) {
            throw new RuntimeException("订单ID不能为空");
        }

        // 模拟处理失败的情况（用于测试死信队列）
        if ("FAIL".equals(orderMessage.getOrderId())) {
            throw new RuntimeException("模拟处理失败");
        }

        // 实际的业务逻辑处理
        // ...

        log.info("订单处理完成: {}", orderMessage.getOrderId());
    }
}
