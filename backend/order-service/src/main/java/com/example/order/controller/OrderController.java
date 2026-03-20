package com.example.order.controller;

import com.example.order.model.OrderEntity;
import com.example.order.repo.OrderRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderRepository orderRepository;
    private final StringRedisTemplate redisTemplate;

    public OrderController(OrderRepository orderRepository, StringRedisTemplate redisTemplate) {
        this.orderRepository = orderRepository;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/{id}")
    public Optional<OrderEntity> query(@PathVariable String id) {
        String cacheKey = "order:query:" + id;
        String status = redisTemplate.opsForValue().get(cacheKey);
        if (status != null) {
            OrderEntity cached = new OrderEntity();
            cached.setId(id);
            cached.setStatus(status);
            return Optional.of(cached);
        }
        Optional<OrderEntity> order = orderRepository.findById(id);
        order.ifPresent(o -> redisTemplate.opsForValue().set(cacheKey, o.getStatus(), Duration.ofMinutes(3)));
        return order;
    }
}
