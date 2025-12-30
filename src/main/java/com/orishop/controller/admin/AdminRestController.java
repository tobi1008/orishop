package com.orishop.controller.admin;

import com.orishop.model.Order;
import com.orishop.model.OrderStatus;
import com.orishop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/api/dashboard")
@RequiredArgsConstructor
public class AdminRestController {

    private final OrderRepository orderRepository;

    @GetMapping("/order-status")
    public ResponseEntity<Map<String, Long>> getOrderStatusData() {
        List<Order> orders = orderRepository.findAll();
        Map<String, Long> statusCounts = new HashMap<>();

        // Initialize all statuses with 0
        for (OrderStatus status : OrderStatus.values()) {
            statusCounts.put(status.name(), 0L);
        }

        // Count actual data
        for (Order order : orders) {
            String statusName = order.getStatus().name();
            statusCounts.put(statusName, statusCounts.getOrDefault(statusName, 0L) + 1);
        }

        return ResponseEntity.ok(statusCounts);
    }

    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueData(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate == null)
            startDate = LocalDate.now().minusDays(30);
        if (endDate == null)
            endDate = LocalDate.now();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // Fetch orders within range (Assuming createdAt is the field)
        // Note: Ideally use a custom repository method for range query, but filtering
        // in memory for now for simplicity if dataset is small.
        // Or better: use findAll and filter.
        List<Order> orders = orderRepository.findAll();

        // Use TreeMap with LocalDate keys for correct chronological sorting
        Map<LocalDate, BigDecimal> revenueMap = new TreeMap<>();

        // Populate map with 0 for all days in range
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            revenueMap.put(current, BigDecimal.ZERO);
            current = current.plusDays(1);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Order order : orders) {
            if (order.getCreatedAt() != null) {
                LocalDateTime orderDateTime = order.getCreatedAt().toInstant()
                        .atZone(java.time.ZoneId.of("Asia/Ho_Chi_Minh"))
                        .toLocalDateTime();

                LocalDate orderDate = orderDateTime.toLocalDate();

                if (!orderDateTime.isBefore(startDateTime) &&
                        !orderDateTime.isAfter(endDateTime) &&
                        (order.getStatus() == OrderStatus.COMPLETED)) {

                    if (revenueMap.containsKey(orderDate)) {
                        revenueMap.put(orderDate, revenueMap.get(orderDate).add(order.getTotalAmount()));
                    }
                }
            }
        }

        // Separate into lists for Chart.js
        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();

        for (Map.Entry<LocalDate, BigDecimal> entry : revenueMap.entrySet()) {
            labels.add(entry.getKey().format(formatter));
            data.add(entry.getValue());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("data", data);

        return ResponseEntity.ok(result);
    }
}
