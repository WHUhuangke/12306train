package com.example.ticket.service;

import com.example.common.validation.IdRules;
import com.example.ticket.model.TicketRequest;
import com.example.ticket.model.TicketResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TicketingService {
    private final StringRedisTemplate redisTemplate;
    private final RequestCollapser requestCollapser;

    public TicketingService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.requestCollapser = new RequestCollapser();
    }

    public Map<String, String> querySeatAndStock(String date, String trainNo, String from, String to) {
        String cacheKey = "stock:" + date + ':' + trainNo + ':' + from + ':' + to;
        return requestCollapser.collapsedGet(cacheKey, () -> {
            String seats = redisTemplate.opsForValue().get("seat:" + cacheKey);
            String stock = redisTemplate.opsForValue().get(cacheKey);
            return Map.of("seatBitmap", seats == null ? "" : seats, "stock", stock == null ? "0" : stock);
        });
    }

    public TicketResponse occupy(TicketRequest request) {
        if (!IdRules.validUserId(request.userId())) {
            return new TicketResponse(null, "REJECTED", List.of(), "userId 非法，拒绝处理");
        }
        String orderId = IdRules.buildOrderId(Long.parseLong(request.userId()), System.nanoTime() % 10000,
                LocalDate.parse(request.travelDate()).format(DateTimeFormatter.BASIC_ISO_DATE));

        List<String> keys = List.of("seat:lock:" + request.travelDate() + ':' + request.trainNo());
        DefaultRedisScript<Long> lua = new DefaultRedisScript<>();
        lua.setLocation(new ClassPathResource("lua/preoccupy_seat.lua"));
        lua.setResultType(Long.class);
        Long result = redisTemplate.execute(lua, keys, String.join(",", request.preferredSeats()),
                String.valueOf(request.passengerCount()));
        if (result == null || result < 0) {
            return new TicketResponse(orderId, "FAILED", List.of(), "占座失败，请重新抢票");
        }
        return new TicketResponse(orderId, "PENDING", request.preferredSeats(), "正在出票，请稍候");
    }

    static class RequestCollapser {
        private final ConcurrentHashMap<String, Object> guards = new ConcurrentHashMap<>();

        public Map<String, String> collapsedGet(String key, java.util.concurrent.Callable<Map<String, String>> supplier) {
            Object lock = guards.computeIfAbsent(key, k -> new Object());
            synchronized (lock) {
                try {
                    return supplier.call();
                } catch (Exception e) {
                    return Map.of("error", e.getMessage());
                } finally {
                    guards.remove(key);
                }
            }
        }
    }
}
