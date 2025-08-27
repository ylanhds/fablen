package com.ylanhds.consumer;

import java.io.Serial;
import java.io.Serializable;

// 消息实体类
public class OrderMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String userId;
    private Long deliveryTag;

    // 构造函数、getter和setter
    public OrderMessage() {}

    public OrderMessage(String orderId, String userId) {
        this.orderId = orderId;
        this.userId = userId;
    }

    // getters and setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Long getDeliveryTag() { return deliveryTag; }
    public void setDeliveryTag(Long deliveryTag) { this.deliveryTag = deliveryTag; }

    @Override
    public String toString() {
        return "OrderMessage{" +
                "orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", deliveryTag=" + deliveryTag +
                '}';
    }
}