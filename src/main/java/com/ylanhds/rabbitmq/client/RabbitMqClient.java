package com.ylanhds.rabbitmq.client;

import com.ylanhds.rabbitmq.annotation.RabbitComponent;
import com.ylanhds.rabbitmq.exchange.DelayExchangeBuilder;
import com.ylanhds.rabbitmq.messaging.MessagingConstants;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * 消息队列客户端 - 专注于底层RabbitMQ操作
 */
@Slf4j
@Configuration
public class RabbitMqClient {

    private final RabbitAdmin rabbitAdmin;
    private final RabbitTemplate rabbitTemplate;

    @Resource
    private ApplicationContext applicationContext;

    @PostConstruct
    public void initQueue() {
        // 清理已存在的冲突队列，避免参数不匹配问题
        cleanupConflictingQueues();

        Map<String, Object> beansWithRabbitComponentMap = this.applicationContext.getBeansWithAnnotation(RabbitComponent.class);

        for (Map.Entry<String, Object> entry : beansWithRabbitComponentMap.entrySet()) {
            log.info("初始化队列............");
            //获取到实例对象的class信息
            Class<?> clazz = entry.getValue().getClass();

            // 检查类级别的RabbitListener注解
            RabbitListener classRabbitListener = clazz.getAnnotation(RabbitListener.class);
            if (classRabbitListener != null) {
                createQueue(classRabbitListener);
            }

            // 检查方法级别的RabbitListener注解
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                RabbitListener methodRabbitListener = method.getAnnotation(RabbitListener.class);
                if (methodRabbitListener != null) {
                    createQueue(methodRabbitListener);
                }
            }
        }
    }

    /**
     * 清理存在参数冲突的队列
     */
    private void cleanupConflictingQueues() {
        try {
            log.info("开始清理可能存在的冲突队列...");

            // 要清理的队列列表
            String[] queuesToClean = {
                    MessagingConstants.DEAD_LETTER_QUEUE,      // "dlx.queue"
                    MessagingConstants.ORDER_PROCESSING_QUEUE, // "order.processing.queue"
                    MessagingConstants.ORDER_CHECK_QUEUE       // "order.check.queue"
            };

            for (String queueName : queuesToClean) {
                forceDeleteQueue(queueName);
            }

            // 清理死信交换机（如果存在）
            forceDeleteExchange(MessagingConstants.DEAD_LETTER_EXCHANGE);

            // 清理延迟交换机
            forceDeleteExchange(DelayExchangeBuilder.getDefaultDelayExchange());
            forceDeleteExchange(DelayExchangeBuilder.getDelayExchange());

            log.info("队列清理完成");
        } catch (Exception e) {
            log.warn("清理队列时发生异常: {}", e.getMessage());
        }
    }

    /**
     * 强制删除队列
     */
    private void forceDeleteQueue(String queueName) {
        try {
            // 检查队列是否存在
            Properties properties = rabbitAdmin.getQueueProperties(queueName);
            if (properties != null) {
                log.info("队列 {} 存在，开始删除", queueName);
                // 先尝试正常删除
                try {
                    if (rabbitAdmin.deleteQueue(queueName)) {
                        log.info("成功删除队列: {}", queueName);
                        return;
                    }
                } catch (Exception e) {
                    log.debug("正常删除队列 {} 失败: {}", queueName, e.getMessage());
                }

                // 如果正常删除失败，尝试强制删除
                try {
                    rabbitAdmin.deleteQueue(queueName, false, false);
                    log.info("强制删除队列成功: {}", queueName);
                } catch (Exception e) {
                    log.warn("强制删除队列 {} 失败: {}", queueName, e.getMessage());
                }
            } else {
                log.debug("队列 {} 不存在", queueName);
            }
        } catch (Exception e) {
            log.debug("检查队列 {} 状态时发生异常: {}", queueName, e.getMessage());
        }
    }

    /**
     * 强制删除交换机
     */
    private void forceDeleteExchange(String exchangeName) {
        try {
            try {
                if (rabbitAdmin.deleteExchange(exchangeName)) {
                    log.info("成功删除交换机: {}", exchangeName);
                }
            } catch (Exception e) {
                log.debug("删除交换机 {} 失败: {}", exchangeName, e.getMessage());
            }
        } catch (Exception e) {
            log.debug("处理交换机 {} 时发生异常: {}", exchangeName, e.getMessage());
        }
    }

    /**
     * 初始化队列
     * 方法中添加死信队列支持
     *
     * @param rabbitListener RabbitListener注解
     */
    private void createQueue(RabbitListener rabbitListener) {
        String[] queues = rabbitListener.queues();
        DirectExchange directExchange = createExchange(DelayExchangeBuilder.getDelayExchange());

        // 创建交换机（如果不存在）
        rabbitAdmin.declareExchange(directExchange);

        // 创建死信交换机和队列
        createDeadLetterComponents();

        if (queues != null && queues.length > 0) {
            for (String queueName : queues) {
                if (queueName != null && !queueName.trim().isEmpty()) {
                    try {
                        // 尝试创建带死信配置的队列
                        createQueueWithDLX(queueName, directExchange);
                    } catch (Exception e) {
                        log.warn("创建带死信配置的队列失败，尝试兼容模式: {}", queueName, e);
                        try {
                            // 兼容模式：如果队列已存在但参数不匹配，则删除后重新创建
                            handleQueueRecreation(queueName, directExchange);
                        } catch (Exception ex) {
                            log.error("兼容模式创建队列也失败: {}", queueName, ex);
                            throw new RuntimeException("队列创建失败: " + queueName, ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * 创建带死信配置的队列
     */
    private void createQueueWithDLX(String queueName, DirectExchange directExchange) {
        try {
            // 检查队列是否已存在
            Properties queueProperties = rabbitAdmin.getQueueProperties(queueName);
            if (queueProperties != null) {
                log.warn("队列 {} 已存在，删除后重建", queueName);
                rabbitAdmin.deleteQueue(queueName);
            }

            // 创建带死信配置的队列
            Queue queue = QueueBuilder.durable(queueName)
                    .withArgument("x-message-ttl", 60000) // 消息TTL
                    .withArgument("x-dead-letter-exchange", MessagingConstants.DEAD_LETTER_EXCHANGE) // 死信交换机
                    .withArgument("x-dead-letter-routing-key", queueName + ".dlq") // 死信路由键
                    .build();
            rabbitAdmin.declareQueue(queue);

            // 绑定队列
            Binding binding = BindingBuilder.bind(queue).to(directExchange).with(queueName);
            rabbitAdmin.declareBinding(binding);
            log.info("创建带死信配置的队列成功: {}", queueName);
        } catch (Exception e) {
            log.error("创建队列 {} 失败: {}", queueName, e.getMessage());
            throw e;
        }
    }

    /**
     * 处理队列重建（当参数不匹配时）
     */
    private void handleQueueRecreation(String queueName, DirectExchange directExchange) {
        try {
            log.info("开始重建队列: {}", queueName);

            // 检查队列是否存在
            Properties queueProperties = rabbitAdmin.getQueueProperties(queueName);
            if (queueProperties != null) {
                log.info("队列 {} 已存在，删除旧队列", queueName);
                // 删除现有队列
                rabbitAdmin.deleteQueue(queueName);
                log.info("已删除旧队列: {}", queueName);
            }

            // 重新创建带死信配置的队列
            createQueueWithDLX(queueName, directExchange);
            log.info("队列重建完成: {}", queueName);

        } catch (Exception e) {
            log.error("重建队列失败: {}", queueName, e);
            throw e;
        }
    }

    /**
     * 创建死信交换机和队列
     */
    private void createDeadLetterComponents() {
        try {
            // 创建死信交换机
            DirectExchange dlxExchange = new DirectExchange(MessagingConstants.DEAD_LETTER_EXCHANGE, true, false);
            rabbitAdmin.declareExchange(dlxExchange);

            // 检查死信队列是否已存在
            Properties dlxQueueProperties = rabbitAdmin.getQueueProperties(MessagingConstants.DEAD_LETTER_QUEUE);
            if (dlxQueueProperties != null) {
                log.warn("死信队列已存在，删除后重建");
                rabbitAdmin.deleteQueue(MessagingConstants.DEAD_LETTER_QUEUE);
            }

            // 创建死信队列
            Queue dlxQueue = QueueBuilder.durable(MessagingConstants.DEAD_LETTER_QUEUE).build();
            rabbitAdmin.declareQueue(dlxQueue);

            // 绑定死信队列到死信交换机
            Binding dlxBinding = BindingBuilder.bind(dlxQueue).to(dlxExchange).with("#"); // 通配符绑定
            rabbitAdmin.declareBinding(dlxBinding);

            log.info("死信交换机和队列创建成功");
        } catch (Exception e) {
            log.error("死信组件创建失败: {}", e.getMessage());
            throw new RuntimeException("死信组件创建失败", e);
        }
    }

    @Autowired
    public RabbitMqClient(RabbitAdmin rabbitAdmin, RabbitTemplate rabbitTemplate) {
        this.rabbitAdmin = rabbitAdmin;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 转换Message对象
     *
     * @param messageType 返回消息类型 MessageProperties类中常量
     * @param msg         消息内容
     * @return Message对象
     */
    public Message getMessage(String messageType, Object msg) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(messageType);
        return new Message(msg.toString().getBytes(), messageProperties);
    }

    /**
     * 有绑定Key的Exchange发送
     *
     * @param topicExchange TopicExchange交换机
     * @param routingKey    路由键
     * @param msg           消息内容
     */
    public void sendMessageToExchange(TopicExchange topicExchange, String routingKey, Object msg) {
        Message message = getMessage(MessageProperties.CONTENT_TYPE_JSON, msg);
        rabbitTemplate.send(topicExchange.getName(), routingKey, message);
    }

    /**
     * 没有绑定KEY的Exchange发送
     *
     * @param topicExchange TopicExchange交换机
     * @param exchange      交换机
     * @param msg           消息内容
     */
    public void sendMessageToExchange(TopicExchange topicExchange, AbstractExchange exchange, String msg) {
        addExchange(exchange);
        log.info("RabbitMQ send {}->{}", exchange.getName(), msg);
        rabbitTemplate.convertAndSend(topicExchange.getName(), msg);
    }

    /**
     * 发送消息 - 确保队列存在并发送消息
     *
     * @param queueName 队列名称
     * @param params    消息内容
     */
    public void sendMessage(String queueName, Object params) {
        if (queueName == null || queueName.trim().isEmpty()) {
            log.warn("队列名称不能为空");
            throw new IllegalArgumentException("队列名称不能为空");
        }

        log.info("发送消息到mq, 队列: {}, 参数: {}", queueName, params);
        try {
            // 确保队列存在，如果不存在则创建带死信配置的队列
            ensureQueueExists(queueName);

            rabbitTemplate.convertAndSend(DelayExchangeBuilder.getDelayExchange(), queueName, params, message -> message);
            log.info("消息发送成功, 队列: {}, 参数: {}", queueName, params);
        } catch (Exception e) {
            log.error("发送消息失败, 队列: {}, 参数: {}", queueName, params, e);
            throw new RuntimeException("消息发送失败: " + e.getMessage(), e);
        }
    }

    /**
     * 延迟发送消息
     *
     * @param queueName  队列名称
     * @param params     消息内容params
     * @param expiration 延迟时间 单位毫秒
     */
    public void sendDelayedMessage(String queueName, Object params, Integer expiration) {
        if (queueName == null || queueName.trim().isEmpty()) {
            log.warn("队列名称不能为空");
            return;
        }

        this.sendDelayed(queueName, params, expiration);
    }

    private void sendDelayed(String queueName, Object params, Integer expiration) {
        log.info("发送延迟消息,队列名称===》{},参数===》{},过期时间===》{}", queueName, params, expiration);

        try {
            // 确保队列存在，如果不存在则创建带死信配置的队列
            ensureQueueExists(queueName);

            // 创建并声明交换机
            CustomExchange customExchange = DelayExchangeBuilder.buildExchange();
            rabbitAdmin.declareExchange(customExchange);

            // 创建绑定关系
            Binding binding = BindingBuilder.bind(new Queue(queueName)).to(customExchange).with(queueName).noargs();
            rabbitAdmin.declareBinding(binding);

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            log.debug("发送时间：{}", sf.format(new Date()));

            // 发送延迟消息
            rabbitTemplate.convertAndSend(DelayExchangeBuilder.getDefaultDelayExchange(), queueName, params, message -> {
                if (expiration != null && expiration > 0) {
                    message.getMessageProperties().setHeader("x-delay", expiration);
                }
                return message;
            });
        } catch (Exception e) {
            log.error("发送延迟消息失败, 队列: {}, 参数: {}, 过期时间: {}", queueName, params, expiration, e);
            throw new RuntimeException("延迟消息发送失败: " + e.getMessage(), e);
        }
    }

    /**
     * 确保队列存在，如果不存在则创建带死信配置的队列
     *
     * @param queueName 队列名称
     */
    private void ensureQueueExists(String queueName) {
        try {
            // 检查队列是否已存在
            Properties queueProperties = rabbitAdmin.getQueueProperties(queueName);
            if (queueProperties == null) {
                log.info("队列 {} 不存在，创建带死信配置的队列", queueName);
                // 创建带死信配置的队列
                Queue queue = QueueBuilder.durable(queueName)
                        .withArgument("x-message-ttl", 60000) // 消息TTL
                        .withArgument("x-dead-letter-exchange", MessagingConstants.DEAD_LETTER_EXCHANGE) // 死信交换机
                        .withArgument("x-dead-letter-routing-key", queueName + ".dlq") // 死信路由键
                        .build();
                rabbitAdmin.declareQueue(queue);

                // 绑定队列到交换机
                DirectExchange directExchange = createExchange(DelayExchangeBuilder.getDelayExchange());
                Binding binding = BindingBuilder.bind(queue).to(directExchange).with(queueName);
                rabbitAdmin.declareBinding(binding);
            }
        } catch (Exception e) {
            log.error("确保队列 {} 存在时发生错误", queueName, e);
            throw new RuntimeException("队列创建失败: " + e.getMessage(), e);
        }
    }

    /**
     * 从队列接收消息
     *
     * @param queueName 队列名称
     * @return 消息内容
     */
    public String receiveFromQueue(String queueName) {
        return receiveFromQueue(DirectExchange.DEFAULT, queueName);
    }

    /**
     * 从指定交换机的队列接收消息
     *
     * @param directExchange 直连交换机
     * @param queueName      队列名称
     * @return 消息内容
     */
    public String receiveFromQueue(DirectExchange directExchange, String queueName) {
        if (queueName == null || queueName.trim().isEmpty()) {
            log.warn("队列名称不能为空");
            return null;
        }

        try {
            // 确保队列存在
            ensureQueueExists(queueName);

            Binding binding = BindingBuilder.bind(new Queue(queueName)).to(directExchange).withQueueName();
            rabbitAdmin.declareBinding(binding);

            Object message = rabbitTemplate.receiveAndConvert(queueName);
            if (message != null) {
                log.info("接收消息成功: {}", message);
                return message.toString();
            }
            return null;
        } catch (Exception e) {
            log.error("接收消息失败, 队列: {}", queueName, e);
            throw new RuntimeException("消息接收失败", e);
        }
    }

    /**
     * 创建Exchange
     *
     * @param exchange 交换机
     */
    public void addExchange(AbstractExchange exchange) {
        rabbitAdmin.declareExchange(exchange);
    }

    /**
     * 删除一个Exchange
     *
     * @param exchangeName 交换机名称
     * @return true if the exchange existed and was deleted.
     */
    public boolean deleteExchange(String exchangeName) {
        if (exchangeName == null || exchangeName.trim().isEmpty()) {
            log.warn("交换机名称不能为空");
            return false;
        }

        try {
            return rabbitAdmin.deleteExchange(exchangeName);
        } catch (Exception e) {
            log.error("删除交换机失败: {}", exchangeName, e);
            return false;
        }
    }

    /**
     * 声明其名称自动命名的队列。它是用exclusive=true、autoDelete=true和 durable = false
     *
     * @return Queue
     */
    public Queue addQueue() {
        return rabbitAdmin.declareQueue();
    }

    /**
     * 创建一个指定的Queue
     *
     * @param queue 队列
     * @return queueName
     */
    public String addQueue(Queue queue) {
        if (queue == null) {
            log.warn("队列不能为空");
            return null;
        }

        return rabbitAdmin.declareQueue(queue);
    }

    /**
     * 删除一个队列
     *
     * @param queueName the name of the queue.
     * @param unused    true if the queue should be deleted only if not in use.
     * @param empty     true if the queue should be deleted only if empty.
     */
    public void deleteQueue(String queueName, boolean unused, boolean empty) {
        if (queueName == null || queueName.trim().isEmpty()) {
            log.warn("队列名称不能为空");
            return;
        }

        try {
            rabbitAdmin.deleteQueue(queueName, unused, empty);
        } catch (Exception e) {
            log.error("删除队列失败: {}", queueName, e);
            throw new RuntimeException("队列删除失败", e);
        }
    }

    /**
     * 删除一个队列
     *
     * @param queueName 队列名称
     * @return true if the queue existed and was deleted.
     */
    public boolean deleteQueue(String queueName) {
        if (queueName == null || queueName.trim().isEmpty()) {
            log.warn("队列名称不能为空");
            return false;
        }

        try {
            return rabbitAdmin.deleteQueue(queueName);
        } catch (Exception e) {
            log.error("删除队列失败: {}", queueName, e);
            return false;
        }
    }

    /**
     * 绑定一个队列到一个匹配型交换器使用一个routingKey
     *
     * @param queue      队列
     * @param exchange   交换机
     * @param routingKey 路由键
     */
    public void addBinding(Queue queue, TopicExchange exchange, String routingKey) {
        if (queue == null || exchange == null || routingKey == null) {
            log.warn("参数不能为空: queue={}, exchange={}, routingKey={}", queue, exchange, routingKey);
            return;
        }

        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);
    }

    /**
     * 绑定一个Exchange到一个匹配型Exchange 使用一个routingKey
     *
     * @param exchange      交换机
     * @param topicExchange Topic交换机
     * @param routingKey    路由键
     */
    public void addBinding(Exchange exchange, TopicExchange topicExchange, String routingKey) {
        if (exchange == null || topicExchange == null || routingKey == null) {
            log.warn("参数不能为空: exchange={}, topicExchange={}, routingKey={}", exchange, topicExchange, routingKey);
            return;
        }

        Binding binding = BindingBuilder.bind(exchange).to(topicExchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);
    }

    /**
     * 去掉一个binding
     *
     * @param binding 绑定关系
     */
    public void removeBinding(Binding binding) {
        if (binding == null) {
            log.warn("绑定关系不能为空");
            return;
        }

        rabbitAdmin.removeBinding(binding);
    }

    /**
     * 创建交换器
     *
     * @param exchangeName 交换机名称
     * @return DirectExchange
     */
    public DirectExchange createExchange(String exchangeName) {
        if (exchangeName == null || exchangeName.trim().isEmpty()) {
            throw new IllegalArgumentException("交换机名称不能为空");
        }

        return new DirectExchange(exchangeName, true, false);
    }
}
