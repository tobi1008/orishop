package com.orishop.service;

import com.orishop.model.Order;
import com.orishop.dto.CartItem;
import com.orishop.model.User;
import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    Order placeOrder(User user, List<CartItem> cartItems, String shippingName, String shippingPhone,
            String shippingAddress, String note, String couponCode, BigDecimal discountAmount);

    List<Order> getOrdersByUser(Long userId);

    void deleteOrder(Long id);
}
