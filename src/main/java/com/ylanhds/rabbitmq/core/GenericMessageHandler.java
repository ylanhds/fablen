package com.ylanhds.rabbitmq.core;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class GenericMessageHandler {


    public <T> void handleMessage(T message, Long deliveryTag, Channel channel,
                                  MessageProcessor<T> processor) {
        try {
            processor.process(message, channel);
            channel.basicAck(deliveryTag, false);
            log.info("消息处理成功: {}", message);
        } catch (Exception e) {
            log.error("接收消息失败: {}", message, e);
            try {
                /**
                 * void basicNack(long deliveryTag, boolean multiple, boolean requeue)
                 * @param deliveryTag 消息标签
                 *              作用：唯一标识一条消息
                 *              来源：RabbitMQ 服务器为每条投递的消息分配的唯一标识符
                 *              用途：告诉 RabbitMQ 要拒绝哪条具体的消息
                 * @param multiple 是否批量处理
                 *              false：只拒绝当前这一条消息
                 *              true：拒绝所有小于等于当前 deliveryTag 的消息（批量拒绝）
                 * @param requeue 是否重新入队
                 *              false：不重新入队，消息会被丢弃或进入死信队列
                 *              true：重新放回队列，等待下次消费
                 */
                channel.basicNack(deliveryTag, false, false);
                throw new AmqpRejectAndDontRequeueException("消息处理失败: " + e.getMessage(), e);
            } catch (IOException ex) {
                log.error("NACK失败: {}", message, ex);
            }
        }
    }

    @FunctionalInterface
    public interface MessageProcessor<T> {
        void process(T message, Channel channel) throws Exception;
    }
}

