package com.fablen.rabbitmq.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import java.util.UUID;

/**
 * 消息队列配置类
 *
 * @author zyf
 */
@Slf4j
@Configuration
public class RabbitMqConfig {
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        //设置忽略声明异常
        rabbitAdmin.setIgnoreDeclarationExceptions(true);
        return rabbitAdmin;
    }


    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //手动确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //当前的消费者数量
        container.setConcurrentConsumers(1);
        //最大的消费者数量
        container.setMaxConcurrentConsumers(10);

        container.setPrefetchCount(1);
        container.setRetryDeclarationInterval(10000);
        container.setDeclarationRetries(3);
        //是否重回队列
        container.setDefaultRequeueRejected(true);
        //消费端的标签策略
        container.setConsumerTagStrategy(queue -> queue + "_" + UUID.randomUUID());
        return container;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);


        RetryInterceptorBuilder<RetryInterceptorBuilder.StatelessRetryInterceptorBuilder, RetryOperationsInterceptor> retryBuilder = RetryInterceptorBuilder.stateless()
                //重试次数
                .maxAttempts(16)
                //初始重试2s 倍数递增 最大间隔 60s 超过16次不在重试
                .backOffOptions(2000, 2.0, 60000)
                .recoverer((message, cause) -> {
                    log.error("消息重试失败，将进入死信队列: {}", message, cause);
                    throw new AmqpRejectAndDontRequeueException(cause);
                });


        factory.setAdviceChain(retryBuilder.build());
        //禁止自动重新入队
        factory.setDefaultRequeueRejected(false);

        return factory;
    }
}
