package com.fablen.rabbitmq.exchange;

import com.fablen.rabbitmq.messaging.MessagingConstants;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟交换器构造器
 */
@Component
public class DelayExchangeBuilder {
    /**
     * 获取默认延迟消息交换器名称
     *
     * @return 交换器名称
     */
    public static String getDefaultDelayExchange() {
        return MessagingConstants.DEFAULT_DELAY_EXCHANGE;
    }

    /**
     * 获取普通交换器名称
     *
     * @return 交换器名称
     */
    public static String getDelayExchange() {
        return MessagingConstants.DELAY_EXCHANGE;
    }

    /**
     * 构建延迟消息交换器
     *
     * @return CustomExchange
     */
    public static CustomExchange buildExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(MessagingConstants.DEFAULT_DELAY_EXCHANGE, "x-delayed-message", true, false, args);
    }
}
