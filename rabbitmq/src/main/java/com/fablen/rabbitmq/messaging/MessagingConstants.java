package com.fablen.rabbitmq.messaging;

/**
 * 消息队列相关常量定义
 * 包含队列名称、交换机名称等常量
 */
public interface MessagingConstants {

    // ==================== 交换机相关常量 ====================

    /**
     * 默认延迟消息交换机名称
     * 用于处理需要延迟投递的消息
     */
    String DEFAULT_DELAY_EXCHANGE = "fablen.delayed.exchange";

    /**
     * 直连交换机名称
     * 用于处理普通的消息投递
     */
    String DELAY_EXCHANGE = "fablen.direct.exchange";

    // ==================== 队列相关常量 ====================

    /**
     * 订单处理队列名称
     * 用于处理订单创建后的业务逻辑
     */
    String ORDER_PROCESSING_QUEUE = "order.processing.queue";

    /**
     * 订单检查队列名称
     * 用于延迟检查订单状态
     */
    String ORDER_CHECK_QUEUE = "order.check.queue";

    // ==================== 死信队列相关常量 ====================

    /**
     * 死信交换机名称
     * 用于处理无法正常消费的消息
     */
    String DEAD_LETTER_EXCHANGE = "dlx.exchange";

    /**
     * 死信队列名称
     * 存放无法正常消费的消息，便于后续分析和处理
     */
    String DEAD_LETTER_QUEUE = "dlx.queue";
}
