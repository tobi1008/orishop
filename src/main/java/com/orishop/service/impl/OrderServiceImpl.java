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
            String shippingAddress, String note, String couponCode, BigDecimal discountAmount, String paymentMethod) {
        // 1. Tính tổng tiền
        BigDecimal totalAmount = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Trừ giảm giá nếu có
        if (discountAmount != null) {
            totalAmount = totalAmount.subtract(discountAmount);
        }

        // 2. Tạo Order
        Order order = new Order();
        order.setUser(user); // Có thể null nếu mua không cần đăng nhập (tùy logic sau này)
        order.setShippingName(shippingName);
        order.setShippingPhone(shippingPhone);
        order.setShippingAddress(shippingAddress);
        order.setNote(note);
        order.setTotalAmount(totalAmount);
        order.setCouponCode(couponCode);
        order.setDiscountAmount(discountAmount);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(paymentMethod != null ? paymentMethod : "COD");
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

    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void updateOrderStatus(Long orderId, com.orishop.model.OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }

    @Override
    public void updatePaymentStatus(Long orderId, boolean paymentStatus) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setPaymentStatus(paymentStatus);
            orderRepository.save(order);
        }
    }

    @Override
    public void completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null && order.getStatus() == OrderStatus.SHIPPING) {
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Đơn hàng chưa được giao hoặc trạng thái không hợp lệ!");
        }
    }

    @Override
    public void requestReturn(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null && order.getStatus() == OrderStatus.COMPLETED) {
            order.setStatus(OrderStatus.RETURN_REQUESTED);
            order.setReturnReason(reason);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Chỉ có thể yêu cầu hoàn trả khi đơn hàng đã hoàn thành!");
        }
    }
}
