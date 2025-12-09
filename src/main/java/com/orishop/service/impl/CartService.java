package com.orishop.service.impl;

import com.orishop.dto.CartItem;
import com.orishop.model.Product;
import com.orishop.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import com.orishop.model.Coupon;
import com.orishop.service.CouponService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;
    private final CouponService couponService;
    private final HttpSession session;
    private static final String CART_SESSION_KEY = "CART";
    private static final String COUPON_SESSION_KEY = "COUPON_CODE";

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

    public void removeCoupon() {
        session.removeAttribute(COUPON_SESSION_KEY);
        saveCart(getCart()); // Recalculate totals
    }

    public void applyCoupon(String code) {
        if (couponService.isCouponValid(code)) {
            session.setAttribute(COUPON_SESSION_KEY, code);
            saveCart(getCart()); // Recalculate totals
        } else {
            throw new RuntimeException("Invalid or expired coupon code");
        }
    }

    public BigDecimal getDiscountAmount() {
        String code = (String) session.getAttribute(COUPON_SESSION_KEY);
        if (code == null) {
            return BigDecimal.ZERO;
        }

        Coupon coupon = couponService.getCouponByCode(code);
        if (coupon == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = getCartTotal();
        BigDecimal discount = BigDecimal.ZERO;

        if ("PERCENT".equalsIgnoreCase(coupon.getDiscountType())) {
            discount = total.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100));
        } else if ("AMOUNT".equalsIgnoreCase(coupon.getDiscountType())) {
            discount = coupon.getDiscountValue();
        }

        // Discount cannot exceed total
        return discount.compareTo(total) > 0 ? total : discount;
    }

    public BigDecimal getFinalTotal() {
        return getCartTotal().subtract(getDiscountAmount());
    }

    public String getAppliedCoupon() {
        return (String) session.getAttribute(COUPON_SESSION_KEY);
    }

    public void clearCart() {
        session.removeAttribute(CART_SESSION_KEY);
        session.removeAttribute(COUPON_SESSION_KEY);
        session.removeAttribute("cartCount");
        session.removeAttribute("cartTotal");
        session.removeAttribute("discountAmount");
        session.removeAttribute("finalTotal");
        session.removeAttribute("appliedCoupon");
    }

    private void saveCart(List<CartItem> cart) {
        session.setAttribute(CART_SESSION_KEY, cart);
        // Lưu thêm các biến tiện ích để hiển thị trên frontend
        session.setAttribute("cartCount", cart.size());
        session.setAttribute("cartTotal", getCartTotal());
        session.setAttribute("discountAmount", getDiscountAmount());
        session.setAttribute("finalTotal", getFinalTotal());
        session.setAttribute("appliedCoupon", getAppliedCoupon());
    }
}
