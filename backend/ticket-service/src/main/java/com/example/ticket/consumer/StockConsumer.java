package com.example.ticket.consumer;

import com.example.ticket.service.StockDeductionPlanner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockConsumer {
    private final StringRedisTemplate redisTemplate;
    private final StockDeductionPlanner planner = new StockDeductionPlanner();

    public StockConsumer(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @KafkaListener(topics = "stock-update", groupId = "ticket-service")
    public void onStockUpdate(String payload) {
        String dedupKey = "stock:event:" + payload.hashCode();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(dedupKey))) {
            return;
        }
        List<StockDeductionPlanner.StockRange> all = List.of(
                new StockDeductionPlanner.StockRange(0, 2),
                new StockDeductionPlanner.StockRange(1, 3),
                new StockDeductionPlanner.StockRange(2, 4));
        List<StockDeductionPlanner.StockRange> affected = planner.affectedRanges(0, 4, 1, 3, all);
        for (StockDeductionPlanner.StockRange range : affected) {
            redisTemplate.opsForValue().decrement("stock:" + range.fromIndex() + ':' + range.toIndex());
        }
        redisTemplate.opsForValue().set(dedupKey, "1");
    }
}
