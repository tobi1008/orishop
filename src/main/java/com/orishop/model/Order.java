package com.orishop.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    private String couponCode;
    private BigDecimal discountAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String shippingAddress;
    private String shippingPhone;
    private String shippingName; // Người nhận

    private String paymentMethod; // COD, VNPay...

    private boolean paymentStatus; // true = PAID, false = UNPAID

    private String note;

    private String returnReason;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(unique = true)
    private String orderCode;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        if (orderCode == null) {
            // Generate random order code: ORD + timestamp(last 6) + random(3 chars)
            long timestamp = System.currentTimeMillis();
            String timePart = String.valueOf(timestamp).substring(7); // Last 6 digits roughly
            String randomPart = java.util.UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            orderCode = "ORD" + timePart + randomPart;
        }
    }
}
