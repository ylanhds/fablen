package com.fablen.rabbitmq.controller;

import com.fablen.rabbitmq.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    // 用户下单后立即处理
    @PostMapping("/create")
    public String createOrder(@RequestParam String orderId, @RequestParam String userId) {
        try {
            // 1. 创建订单记录
            // 2. 发送到 order.processing.queue 立即处理
            orderService.createOrder(orderId, userId);
            return "订单创建成功";
        } catch (Exception e) {
            log.error("创建订单失败: orderId={}, userId={}", orderId, userId, e);
            return "订单创建失败: " + e.getMessage();
        }
    }

    // 用户下单后延迟检查
    @PostMapping("/delayed-check")
    public String sendDelayedCheck(@RequestParam String orderId, @RequestParam String userId) {
        try {
            // 1. 发送到 order.check.queue 延迟10秒处理
            orderService.sendDelayedOrderCheck(orderId, userId);
            return "延迟检查消息发送成功";
        } catch (Exception e) {
            log.error("发送延迟检查消息失败: orderId={}, userId={}", orderId, userId, e);
            return "发送失败: " + e.getMessage();
        }
    }


    @PostMapping("/sendFailedOrderMessage")
    public String sendFailedOrderMessage() {
        try {
            orderService.sendFailedOrderMessage();
            return "发送测试失败消息成功";
        } catch (Exception e) {
            return "发送失败: " + e.getMessage();
        }
    }
}
