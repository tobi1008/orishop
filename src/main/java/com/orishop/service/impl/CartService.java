package com.orishop.service.impl;

import com.orishop.dto.CartItem;
import com.orishop.model.Product;
import com.orishop.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;
    private final HttpSession session;
    private static final String CART_SESSION_KEY = "CART";

    public List<CartItem> getCart() {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_SESSION_KEY);
        return cart == null ? new ArrayList<>() : cart;
    }

    public void addToCart(Long productId, int quantity) {
        List<CartItem> cart = getCart();
        Optional<CartItem> existingItem = cart.stream().filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            CartItem newItem = new CartItem(
                    product.getId(),
                    product.getName(),
                    (product.getImages() != null && !product.getImages().isEmpty())
                            ? product.getImages().get(0).getImageUrl()
                            : "placeholder.jpg",
                    product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice(),
                    quantity);
            cart.add(newItem);
        }
        session.setAttribute(CART_SESSION_KEY, cart);
        session.setAttribute("cartCount", cart.size());
    }

    public BigDecimal getCartTotal() {
        return getCart().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void removeFromCart(Long productId) {
        List<CartItem> cart = getCart();
        cart.removeIf(item -> item.getProductId().equals(productId));
        saveCart(cart);
    }

    public void updateQuantity(Long productId, int quantity) {
        List<CartItem> cart = getCart();
        cart.stream().filter(item -> item.getProductId().equals(productId)).findFirst()
                .ifPresent(item -> {
                    if (quantity > 0) {
                        item.setQuantity(quantity);
                    } else {
                        // Nếu số lượng <= 0 thì xóa luôn
                        cart.remove(item);
                    }
                });
        saveCart(cart);
    }

    public void clearCart() {
        session.removeAttribute(CART_SESSION_KEY);
        session.removeAttribute("cartCount");
        session.removeAttribute("cartTotal");
    }

    private void saveCart(List<CartItem> cart) {
        session.setAttribute(CART_SESSION_KEY, cart);
        // Lưu thêm các biến tiện ích để hiển thị trên frontend
        session.setAttribute("cartCount", cart.size());
        session.setAttribute("cartTotal", getCartTotal());
    }
}
