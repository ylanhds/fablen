# Spring Boot RabbitMQ 项目文档

## 1. 项目概述

这是一个基于 Spring Boot 和 RabbitMQ 的消息队列示例项目，展示了如何在企业级应用中使用 RabbitMQ 实现可靠的消息传递机制。项目实现了消息的发送、接收、死信队列处理、延迟消息等功能。

## 2. 项目结构

```
src/main/java/com/ylanhds
├── consumer/                    # 消息消费者
│   ├── DeadLetterMessageConsumer.java    # 死信消息消费者
│   ├── OrderMessage.java                # 订单消息实体
│   └── OrderMessageConsumer.java        # 订单消息消费者
├── controller/                  # REST 控制器
│   └── OrderController.java             # 订单相关 API
├── rabbitmq/                    # RabbitMQ 相关组件
│   ├── annotation/              # 自定义注解
│   │   └── RabbitComponent.java         # 消息队列组件注解
│   ├── client/                  # RabbitMQ 客户端
│   │   └── RabbitMqClient.java          # RabbitMQ 客户端主类
│   ├── config/                  # 配置类
│   │   └── RabbitMqConfig.java          # RabbitMQ 配置
│   ├── core/                    # 核心组件
│   │   ├── RabbitMqHealthIndicator.java # 健康检查
│   │   └── RetryBaseRabbiMqHandler.java # 消息处理基类
│   ├── exchange/                # 交换机相关
│   │   └── DelayExchangeBuilder.java    # 延迟交换机构建器
│   └── messaging/               # 消息相关
│       ├── MessageListener.java         # 消息监听器接口
│       └── MessagingConstants.java      # 消息常量
├── service/                     # 业务服务
│   └── OrderService.java                # 订单服务
└── SpringBootRabbitmqApplication.java   # 应用启动类
```


## 3. 核心功能

### 3.1 消息队列初始化

项目通过 `RabbitMqClient` 类自动初始化所有带有 `@RabbitComponent` 注解的消费者队列，并配置死信队列机制。

```java
@RabbitComponent
@Slf4j
public class OrderMessageConsumer extends RetryBaseRabbiMqHandler<OrderMessage> {
    @RabbitListener(queues = MessagingConstants.ORDER_PROCESSING_QUEUE)
    public void handleOrderMessage(OrderMessage message, Channel channel) {
        // 处理消息
    }
}
```


### 3.2 死信队列机制

所有业务队列都配置了死信机制：
- 消息TTL: 60秒
- 死信交换机: `dlx.exchange`
- 死信路由键: `队列名.dlq`

当消息处理失败时，会自动进入死信队列，由 [DeadLetterMessageConsumer](file://D:\projet\spring-boot-rabbitmq\src\main\java\com\ylanhds\consumer\DeadLetterMessageConsumer.java#L9-L43) 处理。

### 3.3 消息重试机制

通过 [RetryBaseRabbiMqHandler](file://D:\projet\spring-boot-rabbitmq\src\main\java\com\ylanhds\rabbitmq\core\RetryBaseRabbiMqHandler.java#L9-L48) 实现消息的统一处理和失败重试：

```java
public void onMessage(T t, Long deliveryTag, Channel channel, MessageListener<T> messageListener) {
    try {
        messageListener.handler(t, channel);
        channel.basicAck(deliveryTag, false); // 确认消息
    } catch (Exception e) {
        channel.basicNack(deliveryTag, false, false); // 拒绝消息，不重新入队
        throw new AmqpRejectAndDontRequeueException("消息处理失败", e);
    }
}
```


### 3.4 延迟消息

支持发送延迟消息，通过自定义的延迟交换机实现：

```java
public void sendDelayedOrderCheck(String orderId, String userId) {
    OrderMessage message = new OrderMessage(orderId, userId);
    // 发送延迟消息，10秒后处理
    rabbitMqClient.sendMessage(MessagingConstants.ORDER_CHECK_QUEUE, message, 10000);
}
```


## 4. 主要组件说明

### 4.1 RabbitMqClient

负责队列的自动创建和管理：
- 自动清理冲突队列
- 创建带死信配置的队列
- 管理交换机和绑定关系
- 提供消息发送接口

### 4.2 RetryBaseRabbiMqHandler

消息处理基类，提供统一的消息处理流程：
- 手动确认机制
- 异常处理和日志记录
- 死信队列集成

### 4.3 消费者组件

#### OrderMessageConsumer
处理订单相关消息，演示正常业务处理流程。

#### DeadLetterMessageConsumer
处理死信消息，记录死信原因并进行相应处理。

## 5. API 接口

### 5.1 创建订单
```
POST /api/order/create?orderId=123&userId=user1
```


### 5.2 发送延迟检查消息
```
POST /api/order/delayed-check?orderId=123&userId=user1
```


### 5.3 发送测试失败消息
```
POST /api/order/sendFailedOrderMessage
```


## 6. 配置说明

### 6.1 application.yml 配置

```yaml
spring:
  rabbitmq:
    host: 192.168.0.13
    port: 5672
    username: youyu-dev
    password: youyu-dev
    virtual-host: /youyu-cloud-dev
    listener:
      simple:
        prefetch: 1              # 每次获取1条消息
        acknowledge-mode: manual # 手动确认
        retry:
          enabled: true          # 启用重试
          max-attempts: 5        # 最大重试次数
```


### 6.2 队列配置

- **业务队列**: `order.processing.queue`, `order.check.queue`
- **死信交换机**: `dlx.exchange`
- **死信队列**: `dlx.queue`

## 7. 使用流程

### 7.1 启动项目
```bash
mvn spring-boot:run
```


### 7.2 测试正常消息处理
```bash
curl -X POST "http://localhost:8080/api/order/create?orderId=ORDER001&userId=USER001"
```


### 7.3 测试延迟消息
```bash
curl -X POST "http://localhost:8080/api/order/delayed-check?orderId=ORDER002&userId=USER002"
```


### 7.4 测试死信队列
```bash
curl -X POST "http://localhost:8080/api/order/sendFailedOrderMessage"
```


## 8. 错误处理机制

### 8.1 队列参数冲突处理

项目通过 [cleanupConflictingQueues()](file://D:\projet\spring-boot-rabbitmq\src\main\java\com\ylanhds\rabbitmq\client\RabbitMqClient.java#L71-L97) 方法在启动时自动清理可能存在的队列参数冲突问题。

### 8.2 消息处理失败

1. 消息处理异常时，通过 [basicNack](file://com\rabbitmq\client\Channel.java#L80-L80) 拒绝消息
2. 消息进入死信队列
3. [DeadLetterMessageConsumer](file://D:\projet\spring-boot-rabbitmq\src\main\java\com\ylanhds\consumer\DeadLetterMessageConsumer.java#L9-L43) 记录死信原因并处理

### 8.3 连接异常处理

通过 [RabbitMqHealthIndicator](file://D:\projet\spring-boot-rabbitmq\src\main\java\com\ylanhds\rabbitmq\core\RabbitMqHealthIndicator.java#L8-L34) 提供健康检查接口。

## 9. 扩展性设计

### 9.1 新增队列
1. 创建新的消费者类并添加 [@RabbitComponent](file://D:\projet\spring-boot-rabbitmq\src\main\java\com\ylanhds\rabbitmq\annotation\RabbitComponent.java#L10-L20) 注解
2. 实现消息处理逻辑
3. 项目启动时自动创建队列

### 9.2 自定义死信处理
扩展 [DeadLetterMessageConsumer](file://D:\projet\spring-boot-rabbitmq\src\main\java\com\ylanhds\consumer\DeadLetterMessageConsumer.java#L9-L43) 类，添加特定的死信处理逻辑。

### 9.3 配置化管理
通过配置文件管理队列参数，便于不同环境部署。

## 10. 监控和运维

### 10.1 健康检查
通过 `/actuator/health` 端点检查 RabbitMQ 连接状态。

### 10.2 日志监控
详细的日志记录便于问题排查和系统监控。

### 10.3 死信消息监控
死信消息消费者会记录所有死信消息的详细信息。

## 11. 注意事项

1. **队列清理**: 项目启动时会自动清理冲突队列，生产环境需谨慎使用
2. **消息确认**: 使用手动确认机制确保消息可靠处理
3. **异常处理**: 完善的异常处理机制防止消息丢失
4. **重试机制**: 合理配置重试次数和间隔时间
5. **死信队列**: 及时处理死信消息，防止队列堆积

## 12. 性能优化

1. **连接池配置**: 合理配置连接池大小
2. **预取计数**: 设置合适的预取消息数量
3. **并发消费**: 配置合适的消费者数量
4. **消息TTL**: 根据业务需求设置合适的消息过期时间