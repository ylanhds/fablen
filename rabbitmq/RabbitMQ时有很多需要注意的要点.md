使用RabbitMQ时有很多需要注意的要点，以下是关键注意事项：

## 1. **消息可靠性保证**

### 消息持久化
```java
@Configuration
public class RabbitConfig {
    
    // 队列持久化
    @Bean
    public Queue durableQueue() {
        return QueueBuilder.durable("durable-queue").build();
    }
    
    // 交换机持久化
    @Bean
    public TopicExchange durableExchange() {
        return new TopicExchange("durable-exchange", true, false);
    }
    
    // 消息持久化发送
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void sendPersistentMessage(Object message) {
        rabbitTemplate.convertAndSend("exchange", "routing.key", message, 
            msg -> {
                msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return msg;
            });
    }
}
```


### 发布确认机制
```java
// 配置发布确认
@Configuration
@EnableRabbit
public class RabbitMQConfig {
    
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        factory.setPublisherReturns(true);
        return factory;
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        
        // 发布确认回调
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("消息发送成功: " + correlationData);
            } else {
                System.err.println("消息发送失败: " + cause);
            }
        });
        
        // 返回回调（路由失败）
        template.setReturnsCallback(returned -> {
            System.err.println("消息路由失败: " + returned);
        });
        
        return template;
    }
}
```


## 2. **消费者可靠性**

### 手动确认模式
```java
@Component
public class ReliableConsumer {
    
    @RabbitListener(queues = "reliable-queue", ackMode = "MANUAL")
    public void handleMessage(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            // 处理业务逻辑
            String payload = new String(message.getBody());
            processMessage(payload);
            
            // 手动确认
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            try {
                // 拒绝消息并重新入队
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    
    private void processMessage(String message) {
        // 业务处理逻辑
    }
}
```


### 幂等性处理
```java
@Service
public class IdempotentMessageService {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    public void processMessage(String messageId, String payload) {
        // 使用Redis实现幂等性检查
        String key = "message:processed:" + messageId;
        Boolean alreadyProcessed = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofHours(24));
        
        if (Boolean.TRUE.equals(alreadyProcessed)) {
            // 消息已处理过，直接返回
            return;
        }
        
        // 处理消息
        doProcess(payload);
    }
}
```


## 3. **死信队列配置**

```java
@Configuration
public class DeadLetterConfig {
    
    // 死信交换机
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("dlx.exchange");
    }
    
    // 死信队列
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("dlx.queue").build();
    }
    
    // 正常队列（配置死信）
    @Bean
    public Queue normalQueue() {
        return QueueBuilder.durable("normal.queue")
            .withArgument("x-dead-letter-exchange", "dlx.exchange")
            .withArgument("x-dead-letter-routing-key", "dlx.routing.key")
            .withArgument("x-message-ttl", 60000) // 1分钟过期
            .build();
    }
    
    // 绑定
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(deadLetterQueue())
            .to(deadLetterExchange())
            .with("dlx.routing.key");
    }
}
```


## 4. **性能优化**

### 批量发送
```java
@Service
public class BatchMessageService {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void sendBatchMessages(List<String> messages) {
        // 开启批量确认
        rabbitTemplate.setBatchingStrategy(new SimpleBatchingStrategy(10, 1024, 5000));
        
        for (String message : messages) {
            rabbitTemplate.convertAndSend("exchange", "routing.key", message);
        }
    }
}
```


### 连接池配置
```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    # 连接池配置
    cache:
      connection:
        mode: channel
        size: 10
      channel:
        size: 25
    # 消费者配置
    listener:
      simple:
        concurrency: 5
        max-concurrency: 10
        prefetch: 10
```


## 5. **监控和告警**

### 健康检查
```java
@Component
public class RabbitMQHealthIndicator implements HealthIndicator {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Override
    public Health health() {
        try {
            // 测试连接
            rabbitTemplate.execute(channel -> {
                channel.getConnection().isOpen();
                return null;
            });
            return Health.up().build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}
```


### 消息积压监控
```java
@Component
public class QueueMonitor {
    
    @Autowired
    private AmqpAdmin amqpAdmin;
    
    @Scheduled(fixedRate = 30000) // 30秒检查一次
    public void checkQueueDepth() {
        Properties queueInfo = amqpAdmin.getQueueProperties("important.queue");
        if (queueInfo != null) {
            Integer messageCount = (Integer) queueInfo.get("QUEUE_MESSAGE_COUNT");
            if (messageCount != null && messageCount > 1000) {
                // 发送告警
                alertService.sendAlert("队列积压警告: important.queue 有 " + messageCount + " 条消息");
            }
        }
    }
}
```


## 6. **常见陷阱和解决方案**

### 路由失败处理
```java
// 配置返回回调处理路由失败
@Bean
public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMandatory(true); // 强制路由
    
    template.setReturnsCallback(returnedMessage -> {
        System.err.println("消息路由失败: " + new String(returnedMessage.getMessage().getBody()));
        // 记录日志或重新处理
        handleUnroutedMessage(returnedMessage);
    });
    
    return template;
}
```


### 避免循环依赖
```java
// 错误示例：可能导致循环依赖
@Component
public class OrderService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void processOrder(Order order) {
        // 处理订单
        // ...
        
        // 发送消息可能触发其他服务调用回到OrderService
        rabbitTemplate.convertAndSend("order.processed", order);
    }
}

// 正确做法：使用事件驱动解耦
@Component
public class OrderEventHandler {
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        rabbitTemplate.convertAndSend("order.created", event.getOrder());
    }
}
```


## 7. **安全注意事项**

### 权限控制
```java
// 为不同应用配置不同的用户权限
// 在RabbitMQ管理界面或通过命令行配置：
// rabbitmqctl add_user app1_user app1_password
// rabbitmqctl set_permissions -p / app1_user "app1_queue.*" "app1_exchange.*" "app1_queue.*"
```


### 消息加密
```java
// 敏感消息加密处理
@Service
public class SecureMessageService {
    
    public void sendSecureMessage(Object sensitiveData) {
        // 加密敏感数据
        String encryptedData = encrypt(sensitiveData.toString());
        
        // 发送加密消息
        rabbitTemplate.convertAndSend("secure.exchange", "secure.key", encryptedData);
    }
}
```


## 总结

使用RabbitMQ时需要重点关注：
1. **可靠性**：消息持久化、发布确认、消费者确认
2. **性能**：批量处理、连接池、预取数量
3. **监控**：健康检查、队列深度监控
4. **容错**：死信队列、重试机制、幂等性
5. **安全**：权限控制、消息加密

这些注意事项能帮助你构建稳定、可靠的RabbitMQ消息系统。