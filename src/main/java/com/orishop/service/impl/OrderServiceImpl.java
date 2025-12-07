package com.orishop.service.impl;

import com.orishop.dto.CartItem;
import com.orishop.model.Order;
import com.orishop.model.OrderItem;
import com.orishop.model.OrderStatus;
import com.orishop.model.Product;
import com.orishop.model.User;
import com.orishop.repository.OrderItemRepository;
import com.orishop.repository.OrderRepository;
import com.orishop.repository.ProductRepository;
import com.orishop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Order placeOrder(User user, List<CartItem> cartItems, String shippingName, String shippingPhone,
            String shippingAddress, String note) {
        // 1. Tính tổng tiền
        BigDecimal totalAmount = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Tạo Order
        Order order = new Order();
        order.setUser(user); // Có thể null nếu mua không cần đăng nhập (tùy logic sau này)
        order.setShippingName(shippingName);
        order.setShippingPhone(shippingPhone);
        order.setShippingAddress(shippingAddress);
        // order.setNote(note); // Nếu Entity Order có field note
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod("COD"); // Mặc định COD
        // order.setCreatedAt(LocalDateTime.now()); // Đã có @PrePersist xử lý

        Order savedOrder = orderRepository.save(order);

        // 3. Tạo Order Items & Trừ tồn kho (nếu muốn)
        for (CartItem item : cartItems) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());

            orderItemRepository.save(orderItem);

            // TODO: Trừ tồn kho product (product.setStockQuantity...)
        }

        return savedOrder;
    }
}
