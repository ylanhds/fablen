package com.ylanhds.consumer;


import com.rabbitmq.client.Channel;
import com.ylanhds.rabbitmq.annotation.RabbitComponent;
import com.ylanhds.rabbitmq.core.GenericMessageHandler;
import com.ylanhds.rabbitmq.messaging.MessagingConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;

/**
 * 队列: ORDER_CHECK_QUEUE (order.check.queue)
 * 职责: 延迟检查订单状态或执行后续操作
 * 触发时机: 订单创建后延迟一段时间执行
 * 处理逻辑: 订单的后续检查或补偿操作
 */

@RabbitComponent
@Slf4j
public class OrderCheckMessageConsumer {

    @Autowired
    private GenericMessageHandler messageHandler;

    @RabbitListener(queues = MessagingConstants.ORDER_CHECK_QUEUE)
    public void handleOrderCheckMessage(OrderMessage message, Channel channel,
                                        @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        messageHandler.handleMessage(message, deliveryTag, channel, (msg, ch) -> processOrderCheck(msg));
    }

    private void processOrderCheck(OrderMessage orderMessage) throws Exception {
        log.info("执行订单检查: 订单ID={}, 用户ID={}", orderMessage.getOrderId(), orderMessage.getUserId());

        // 检查订单状态
        checkOrderStatus(orderMessage.getOrderId());

        // 可以执行其他检查逻辑
        // 例如：检查支付状态、库存状态、物流状态等

        log.info("订单检查完成: {}", orderMessage.getOrderId());
    }

    private void checkOrderStatus(String orderId) {
        // 实际的订单检查逻辑
        log.info("检查订单状态: {}", orderId);

        // 示例检查逻辑：
        // 1. 查询订单在数据库中的状态
        // 2. 检查是否需要进行后续处理
        // 3. 发送通知或执行其他业务操作

        // 模拟检查逻辑
        if (orderId == null || orderId.isEmpty()) {
            throw new RuntimeException("订单ID无效");
        }

        // 模拟检查失败情况（用于测试死信队列）
        if ("CHECK_FAIL".equals(orderId)) {
            throw new RuntimeException("订单检查失败");
        }
    }
}
