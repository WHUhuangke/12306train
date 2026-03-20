package com.example.order.consumer;

import com.example.order.model.OrderEntity;
import com.example.order.repo.OrderRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SeatUpdateConsumer {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final StringRedisTemplate redisTemplate;

    public SeatUpdateConsumer(OrderRepository orderRepository, KafkaTemplate<String, String> kafkaTemplate,
                              StringRedisTemplate redisTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.redisTemplate = redisTemplate;
    }

    @KafkaListener(topics = "seat-update", groupId = "order-service")
    @Transactional
    public void onSeatEvent(String payload) {
        String eventId = "seat:event:" + payload.hashCode();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(eventId))) {
            return;
        }
        try {
            OrderEntity order = new OrderEntity();
            order.setId(payload.split(",")[0]);
            order.setMemberId(payload.split(",")[1]);
            order.setStatus("SEAT_CONFIRMED");
            orderRepository.save(order);
            redisTemplate.opsForValue().set(eventId, "1");
            kafkaTemplate.send("stock-update", payload);
        } catch (DataIntegrityViolationException ex) {
            kafkaTemplate.send("seat-update-retry", payload);
        }
    }
}
