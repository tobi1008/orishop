package com.orishop.service;

import com.orishop.model.Order;
import com.orishop.dto.CartItem;
import com.orishop.model.User;
import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    Order placeOrder(User user, List<CartItem> cartItems, String shippingName, String shippingPhone,
            String shippingAddress, String note, String couponCode, BigDecimal discountAmount, String paymentMethod);

    List<Order> getOrdersByUser(User user);

    Order getOrderById(Long id);

    void deleteOrder(Long id);

    void updateOrderStatus(Long orderId, com.orishop.model.OrderStatus status);

    void updatePaymentStatus(Long orderId, boolean paymentStatus);

    void completeOrder(Long orderId);

    void requestReturn(Long orderId, String reason, String accountNumber);

    void cancelOrder(Long orderId, String reason, String accountNumber);
}
