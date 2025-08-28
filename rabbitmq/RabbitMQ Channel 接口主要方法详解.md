# RabbitMQ Channel 接口主要方法详解

## 1. 连接和基础管理方法

### 1.1 连接状态检查
```java
// 检查Channel是否打开
boolean isOpen();

// 获取Channel编号
int getChannelNumber();

// 关闭Channel
void close() throws IOException, TimeoutException;
void close(int closeCode, String closeMessage) throws IOException, TimeoutException;
```

### 1.2 基础配置
```java
// 设置QoS（服务质量）
void basicQos(int prefetchCount) throws IOException;
void basicQos(int prefetchCount, boolean global) throws IOException;
void basicQos(int prefetchSize, int prefetchCount, boolean global) throws IOException;

// 示例：设置每个消费者预取10条消息
channel.basicQos(10);
```

## 2. 交换机操作方法

### 2.1 声明交换机
```java
// 基本声明
Exchange.DeclareOk exchangeDeclare(String exchange, String type) throws IOException;

// 完整声明
Exchange.DeclareOk exchangeDeclare(
    String exchange, 
    String type, 
    boolean durable,          // 是否持久化
    boolean autoDelete,       // 是否自动删除
    boolean internal,         // 是否内部交换机
    Map<String, Object> arguments  // 额外参数
) throws IOException;

// 示例：声明持久化的直连交换机
Map<String, Object> args = new HashMap<>();
args.put("alternate-exchange", "my-ae");
channel.exchangeDeclare("order.exchange", "direct", true, false, false, args);
```

### 2.2 删除交换机
```java
// 删除交换机
Exchange.DeleteOk exchangeDelete(String exchange) throws IOException;
Exchange.DeleteOk exchangeDelete(String exchange, boolean ifUnused) throws IOException;

// 示例：删除未使用的交换机
channel.exchangeDelete("temp.exchange", true);
```

### 2.3 绑定操作
```java
// 交换机绑定（联邦插件等）
Exchange.BindOk exchangeBind(String destination, String source, String routingKey) throws IOException;
Exchange.BindOk exchangeBind(String destination, String source, String routingKey, 
                           Map<String, Object> arguments) throws IOException;

// 解绑交换机
Exchange.UnbindOk exchangeUnbind(String destination, String source, String routingKey) throws IOException;
```

## 3. 队列操作方法

### 3.1 声明队列
```java
// 基本声明
Queue.DeclareOk queueDeclare() throws IOException;  // 创建匿名队列
Queue.DeclareOk queueDeclare(String queue, boolean durable, 
                           boolean exclusive, boolean autoDelete, 
                           Map<String, Object> arguments) throws IOException;

// 被动声明（检查队列是否存在）
Queue.DeclareOk queueDeclarePassive(String queue) throws IOException;

// 示例：声明持久化队列，带死信交换机配置
Map<String, Object> args = new HashMap<>();
args.put("x-dead-letter-exchange", "dlx.exchange");
args.put("x-message-ttl", 60000); // 60秒TTL

Queue.DeclareOk result = channel.queueDeclare(
    "order.queue", 
    true,      // 持久化
    false,     // 非排他
    false,     // 不自动删除
    args       // 参数
);
String queueName = result.getQueue(); // 获取队列名
```

### 3.2 删除队列
```java
// 删除队列
Queue.DeleteOk queueDelete(String queue) throws IOException;
Queue.DeleteOk queueDelete(String queue, boolean ifUnused, boolean ifEmpty) throws IOException;

// 清空队列（删除并重新声明）
Queue.PurgeOk queuePurge(String queue) throws IOException;

// 示例：删除空队列
channel.queueDelete("temp.queue", false, true);
```

## 4. 绑定操作方法

### 4.1 队列绑定
```java
// 绑定队列到交换机
Queue.BindOk queueBind(String queue, String exchange, String routingKey) throws IOException;
Queue.BindOk queueBind(String queue, String exchange, String routingKey, 
                     Map<String, Object> arguments) throws IOException;

// 解绑队列
Queue.UnbindOk queueUnbind(String queue, String exchange, String routingKey) throws IOException;
Queue.UnbindOk queueUnbind(String queue, String exchange, String routingKey, 
                         Map<String, Object> arguments) throws IOException;

// 示例：绑定队列，添加头部匹配参数
Map<String, Object> bindArgs = new HashMap<>();
bindArgs.put("x-match", "all");
bindArgs.put("category", "order");
channel.queueBind("order.queue", "headers.exchange", "", bindArgs);
```

## 5. 消息发布方法

### 5.1 基本消息发布
```java
// 发布消息
void basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body) throws IOException;

// 发布消息（带强制标志）
void basicPublish(String exchange, String routingKey, boolean mandatory, 
                 BasicProperties props, byte[] body) throws IOException;

// 发布消息（完整参数）
void basicPublish(String exchange, String routingKey, boolean mandatory, boolean immediate,
                 BasicProperties props, byte[] body) throws IOException;
```

### 5.2 BasicProperties 配置
```java
// 创建消息属性
AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
    .contentType("application/json")    // 内容类型
    .contentEncoding("UTF-8")           // 内容编码
    .deliveryMode(2)                    // 2=持久化，1=非持久化
    .priority(5)                        // 优先级（0-9）
    .correlationId(UUID.randomUUID().toString())  // 关联ID
    .replyTo("reply.queue")             // 回复队列
    .expiration("60000")                // 消息TTL（毫秒）
    .messageId(UUID.randomUUID().toString())      // 消息ID
    .timestamp(new Date())              // 时间戳
    .type("order.created")              // 消息类型
    .userId("producer-app")             // 用户ID
    .appId("order-service")             // 应用ID
    .headers(headersMap)                // 自定义头部
    .build();

// 示例：发布持久化消息
channel.basicPublish("order.exchange", "order.create", true, false, props, messageBytes);
```

## 6. 消费者相关方法

### 6.1 消费者注册
```java
// 注册消费者
String basicConsume(String queue, Consumer callback) throws IOException;

// 完整参数注册
String basicConsume(String queue, boolean autoAck, Consumer callback) throws IOException;
String basicConsume(String queue, boolean autoAck, Map<String, Object> arguments, Consumer callback) throws IOException;
String basicConsume(String queue, boolean autoAck, String consumerTag, Consumer callback) throws IOException;
String basicConsume(String queue, boolean autoAck, String consumerTag, 
                   boolean noLocal, boolean exclusive, Map<String, Object> arguments, 
                   Consumer callback) throws IOException;

// 示例：注册消费者，手动确认，设置消费者标签
String consumerTag = channel.basicConsume("order.queue", false, "order-consumer-1", 
    new DefaultConsumer(channel) {
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope,
                                 AMQP.BasicProperties properties, byte[] body) {
            // 处理消息
            String message = new String(body, StandardCharsets.UTF_8);
            try {
                processMessage(message);
                channel.basicAck(envelope.getDeliveryTag(), false);
            } catch (Exception e) {
                channel.basicNack(envelope.getDeliveryTag(), false, true);
            }
        }
    });
```

### 6.2 消费者管理
```java
// 取消消费者
void basicCancel(String consumerTag) throws IOException;

// 获取消费者数量
Consumer[] getConsumers();

// 示例：优雅关闭消费者
channel.basicCancel(consumerTag);
```

## 7. 消息确认方法

### 7.1 确认机制
```java
// 确认单条消息
void basicAck(long deliveryTag, boolean multiple) throws IOException;

// 拒绝消息（可重新入队）
void basicReject(long deliveryTag, boolean requeue) throws IOException;

// 拒绝多条消息
void basicNack(long deliveryTag, boolean multiple, boolean requeue) throws IOException;

// 恢复消息流（流量控制后）
void basicRecover(boolean requeue) throws IOException;

// 示例：批量确认消息
long lastDeliveryTag = 0;
for (int i = 0; i < 10; i++) {
    // 处理消息...
    lastDeliveryTag = envelope.getDeliveryTag();
}
channel.basicAck(lastDeliveryTag, true); // 确认到lastDeliveryTag的所有消息
```

## 8. 事务方法

### 8.1 事务控制
```java
// 开启事务
void txSelect() throws IOException;

// 提交事务
void txCommit() throws IOException;

// 回滚事务
void txRollback() throws IOException;

// 示例：事务性消息发布
channel.txSelect();
try {
    channel.basicPublish("exchange1", "key1", props, message1);
    channel.basicPublish("exchange2", "key2", props, message2);
    channel.txCommit(); // 两条消息要么都成功，要么都失败
} catch (Exception e) {
    channel.txRollback();
    throw e;
}
```

## 9. 高级特性方法

### 9.1 消息追踪
```java
// 确认监听器
void addConfirmListener(ConfirmListener listener);
void removeConfirmListener(ConfirmListener listener);

// 返回监听器
void addReturnListener(ReturnListener listener);
void removeReturnListener(ReturnListener listener);

// 流量控制监听器
void addFlowListener(FlowListener listener);
void removeFlowListener(FlowListener listener);

// 示例：添加确认监听器
channel.addConfirmListener(new ConfirmListener() {
    @Override
    public void handleAck(long deliveryTag, boolean multiple) {
        log.info("消息确认: deliveryTag={}, multiple={}", deliveryTag, multiple);
    }
    
    @Override
    public void handleNack(long deliveryTag, boolean multiple) {
        log.error("消息未确认: deliveryTag={}, multiple={}", deliveryTag, multiple);
    }
});
```

### 9.2 流控方法
```java
// 启用/禁用流控
void basicFlow(boolean active) throws IOException;

// 获取Channel状态
boolean flowBlocked();
```

## 10. 工具方法

### 10.1 监控和诊断
```java
// 获取消息数量
AMQP.Queue.DeclareOk queueDeclarePassive(String queue) throws IOException;

// 示例：检查队列状态
AMQP.Queue.DeclareOk result = channel.queueDeclarePassive("order.queue");
int messageCount = result.getMessageCount();
int consumerCount = result.getConsumerCount();

// 获取连接信息
AMQP.Connection.StartOk getConnection() throws IOException;
```

### 10.2 异常处理
```java
// 获取最后错误
IOException getCloseReason();

// 检查特定错误
boolean isOpen();
boolean isFlowBlocked();
```

## 11. 使用最佳实践

### 11.1 Channel 生命周期管理
```java
public class ChannelManager {
    private final Connection connection;
    private Channel channel;
    
    public Channel getChannel() throws IOException {
        if (channel == null || !channel.isOpen()) {
            channel = connection.createChannel();
            // 配置Channel
            channel.basicQos(10); // 预取数量
            channel.confirmSelect(); // 开启确认模式
        }
        return channel;
    }
    
    public void close() {
        if (channel != null && channel.isOpen()) {
            try {
                channel.close();
            } catch (Exception e) {
                log.warn("关闭Channel失败", e);
            }
        }
    }
}
```

### 11.2 异常处理模板
```java
public void executeWithChannel(ChannelOperation operation) {
    Channel channel = null;
    try {
        channel = connection.createChannel();
        operation.execute(channel);
        channel.close();
    } catch (Exception e) {
        if (channel != null) {
            try { channel.close(); } catch (IOException ex) { /* ignore */ }
        }
        throw new RuntimeException("Channel操作失败", e);
    }
}

// 使用函数式接口
@FunctionalInterface
public interface ChannelOperation {
    void execute(Channel channel) throws IOException;
}
```

## 12. 重要注意事项

1. **线程安全**：Channel不是线程安全的，每个线程应该使用独立的Channel
2. **资源泄漏**：确保正确关闭Channel，避免资源泄漏
3. **性能影响**：合理设置QoS和预取值，避免性能问题
4. **错误处理**：正确处理IOException和TimeoutException
5. **连接状态**：定期检查Channel和Connection的状态

通过熟练掌握这些Channel方法，可以更好地控制RabbitMQ的消息流，构建高效可靠的消息系统。