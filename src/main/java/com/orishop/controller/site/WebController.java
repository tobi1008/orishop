package com.orishop.controller.site;

import com.orishop.model.Product;
import com.orishop.model.Category;
import com.orishop.repository.CategoryRepository;
import com.orishop.service.impl.ProductService;
import com.orishop.service.impl.CartService;
import com.orishop.service.ReviewService;
import com.orishop.repository.UserRepository;

import com.orishop.model.User;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final ProductService productService;
    private final CartService cartService;
    private final CategoryRepository categoryRepository;
    private final com.orishop.service.OrderService orderService;
    private final ReviewService reviewService;
    private final UserRepository userRepository;
    private final com.orishop.repository.ContactRepository contactRepository;
    private final com.orishop.service.VnPayService vnPayService;
    private final com.orishop.service.MoMoService moMoService;
    private final com.orishop.service.impl.RecentlyViewedService recentlyViewedService;
    private final com.orishop.repository.AddressRepository addressRepository;
    private final com.orishop.service.CouponService couponService;

    @GetMapping("/")
    public String home(Model model) {
        java.util.List<Product> products = productService.getAllProducts();
        populateFlashSaleInfo(products);
        model.addAttribute("products", products);

        java.util.List<Product> bestSellers = productService.getBestSellingProducts(5);
        populateFlashSaleInfo(bestSellers);
        model.addAttribute("bestSellers", bestSellers);

        model.addAttribute("activeFlashSales", flashSaleService.findActiveFlashSales(new java.util.Date()));
        model.addAttribute("coupons", couponService.getAllCoupons());

        // Latest Products for Banner
        model.addAttribute("bannerLatestProducts", productService.getLatestProducts(3));

        // Flash Sale Products for Banner
        java.util.List<com.orishop.model.FlashSale> activeSales = flashSaleService
                .findActiveFlashSales(new java.util.Date());
        List<com.orishop.model.FlashSaleProduct> bannerFlashSaleProducts = new ArrayList<>();
        if (activeSales != null && !activeSales.isEmpty()) {
            for (com.orishop.model.FlashSale sale : activeSales) {
                if (sale.getFlashSaleProducts() != null) {
                    bannerFlashSaleProducts.addAll(sale.getFlashSaleProducts());
                }
            }
        }
        // Limit to 3
        if (bannerFlashSaleProducts.size() > 3) {
            bannerFlashSaleProducts = bannerFlashSaleProducts.subList(0, 3);
        }
        model.addAttribute("bannerFlashSaleProducts", bannerFlashSaleProducts);

        return "web/index"; // templates/web/index.html
    }

    @GetMapping("/product/{slug}")
    public String productDetail(@PathVariable String slug, Model model, Principal principal,
            HttpServletRequest request) {
        // Force session creation to avoid "response committed" error during CSRF
        // generation in view
        request.getSession(true);

        Product product = productService.getProductBySlug(slug).orElse(null);

        if (product == null) {
            try {
                Long id = Long.parseLong(slug);
                product = productService.getProductById(id).orElse(null);
            } catch (NumberFormatException e) {
                // Ignore, slug is not an ID
            }
        }

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName()).orElse(null);
            if (user != null) {
                recentlyViewedService.recordView(user, product);
            }
        }

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviewService.getReviewsByProduct(product.getId()));

        // Check flash sale for single product
        populateFlashSaleInfo(java.util.List.of(product));

        return "web/product-detail";
    }

    @GetMapping("/product")
    public String allProducts(Model model) {
        java.util.List<Product> products = productService.getAllProducts();
        populateFlashSaleInfo(products);
        model.addAttribute("products", products);
        return "web/product-list";
    }

    @GetMapping("/product/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        java.util.List<Product> products = productService.searchProducts(keyword);
        populateFlashSaleInfo(products);
        model.addAttribute("products", products);
        return "web/product-list";
    }

    @GetMapping("/category/{slug}")
    public String productsByCategory(@PathVariable String slug, Model model) {
        Category category = categoryRepository.findBySlug(slug).orElse(null);

        if (category == null) {
            try {
                Long id = Long.parseLong(slug);
                category = categoryRepository.findById(id).orElse(null);
            } catch (NumberFormatException e) {
                // Ignore
            }
        }

        if (category == null) {
            throw new RuntimeException("Category not found");
        }

        java.util.List<Product> products = productService.getProductsByCategory(category.getId());
        populateFlashSaleInfo(products);
        model.addAttribute("products", products);
        model.addAttribute("currentCategory", category);
        return "web/product-list";
    }

    // --- Flash Sale Page ---
    @GetMapping("/flash-sale")
    public String flashSalePage(Model model) {
        java.util.List<com.orishop.model.FlashSale> activeSales = flashSaleService
                .findActiveFlashSales(new java.util.Date());

        // Flatten all products from all active sales
        List<com.orishop.model.FlashSaleProduct> flashSaleProducts = new ArrayList<>();
        if (activeSales != null && !activeSales.isEmpty()) {
            for (com.orishop.model.FlashSale sale : activeSales) {
                if (sale.getFlashSaleProducts() != null) {
                    flashSaleProducts.addAll(sale.getFlashSaleProducts());
                }
            }
            // Pass the first active sale for timer if needed, or just finding max end time
            model.addAttribute("flashSale", activeSales.get(0));
        }

        model.addAttribute("flashSaleProducts", flashSaleProducts);
        return "web/flash-sale";
    }

    // --- Cart Actions ---
    @GetMapping("/cart")
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCart());
        model.addAttribute("cartTotal", cartService.getCartTotal());
        model.addAttribute("discountAmount", cartService.getDiscountAmount());
        model.addAttribute("finalTotal", cartService.getFinalTotal());
        model.addAttribute("appliedCoupon", cartService.getAppliedCoupon());
        return "web/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.addToCart(productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/cart/coupon")
    public String applyCoupon(@RequestParam String code,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            cartService.applyCoupon(code);
            redirectAttributes.addFlashAttribute("successMessage", "Đã áp dụng mã giảm giá thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart/coupon/remove")
    public String removeCoupon() {
        cartService.removeCoupon();
        return "redirect:/cart";
    }

    // Hỗ trợ thêm nhanh bằng link GET (ví dụ từ nút Mua ngay ở trang danh sách)
    @GetMapping("/cart/add/{productId}")
    public String addToCartQuick(@PathVariable Long productId) {
        cartService.addToCart(productId, 1);
        return "redirect:/cart";
    }

    @GetMapping("/cart/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.updateQuantity(productId, quantity);
        return "redirect:/cart";
    }

    // private final com.orishop.service.OrderService orderService; // Moved to top

    // ... (các API Cart cũ giữ nguyên) ...

    @GetMapping("/cart/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }

    // --- Checkout ---
    @GetMapping("/checkout")
    public String checkoutForm(Model model, Principal principal) {
        if (cartService.getCart().isEmpty()) {
            return "redirect:/cart";
        }

        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName()).orElse(null);
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("addresses", addressRepository.findByUser(user));
            }
        }

        model.addAttribute("cartItems", cartService.getCart());
        model.addAttribute("cartTotal", cartService.getCartTotal());
        model.addAttribute("discountAmount", cartService.getDiscountAmount());
        model.addAttribute("finalTotal", cartService.getFinalTotal());
        return "web/checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(@RequestParam String fullName,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam(required = false) String note,
            @RequestParam(defaultValue = "COD") String paymentMethod,
            Principal principal,
            HttpServletRequest request,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        User user = null;
        if (principal != null) {
            user = userRepository.findByEmail(principal.getName()).orElse(null);
        }

        com.orishop.model.Order order = orderService.placeOrder(user, cartService.getCart(), fullName, phone, address,
                note,
                cartService.getAppliedCoupon(), cartService.getDiscountAmount(), paymentMethod);

        cartService.clearCart();

        if ("VNPAY".equals(paymentMethod)) {
            String paymentUrl = vnPayService.createPaymentUrl(order, request);
            return "redirect:" + paymentUrl;
        } else if ("MOMO".equals(paymentMethod)) {
            String paymentUrl = moMoService.createPayment(order);
            if (paymentUrl != null) {
                return "redirect:" + paymentUrl;
            }
        }

        redirectAttributes.addFlashAttribute("successMessage",
                "Đặt hàng thành công! Mã đơn hàng: "
                        + (order.getOrderCode() != null ? order.getOrderCode() : order.getId()));
        return "redirect:/checkout/success";
    }

    @GetMapping("/payment/vnpay-return")
    public String vnpayReturn(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        if (paymentStatus == 1) {
            try {
                Long orderId = Long.parseLong(transactionId);
                String vnpTxnRef = request.getParameter("vnp_TxnRef");
                Long refId = Long.parseLong(vnpTxnRef);
                orderService.updatePaymentStatus(refId, true);
            } catch (NumberFormatException e) {
            }
            model.addAttribute("successMessage", "Thanh toán VNPay thành công!");
            return "web/order-success";
        } else {
            return "web/order-fail";
        }
    }

    @GetMapping("/payment/momo-return")
    public String momoReturn(HttpServletRequest request, Model model) {
        String orderInfo = request.getParameter("orderInfo");
        String requestId = request.getParameter("requestId");
        String errorCode = request.getParameter("errorCode");
        String message = request.getParameter("message");
        String amount = request.getParameter("amount");
        String transId = request.getParameter("transId");
        String orderIdStr = request.getParameter("orderId");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", amount);
        model.addAttribute("paymentTime", new java.util.Date().toString());
        model.addAttribute("transactionId", transId);

        if ("0".equals(errorCode)) {
            // Success
            try {
                Long orderId = Long.parseLong(orderIdStr);
                orderService.updatePaymentStatus(orderId, true);
            } catch (Exception e) {
            }
            model.addAttribute("successMessage", "Thanh toán MoMo thành công!");
            return "web/order-success";
        } else {
            return "web/order-fail";
        }
    }

    @PostMapping("/payment/momo-ipn")
    @ResponseBody
    public org.springframework.http.ResponseEntity<Void> momoIpn(@RequestBody Map<String, Object> payload) {
        // MoMo sends POST request with JSON body
        // Verify signature here usually, but for simple integration we take errorCode

        try {
            String errorCode = String.valueOf(payload.get("errorCode"));
            String orderIdStr = String.valueOf(payload.get("orderId"));

            if ("0".equals(errorCode)) {
                Long orderId = Long.parseLong(orderIdStr);
                orderService.updatePaymentStatus(orderId, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return org.springframework.http.ResponseEntity.noContent().build();
    }

    @GetMapping("/orders")
    public String myOrders(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("orders", orderService.getOrdersByUser(user));
        return "web/order-list";
    }

    @GetMapping("/user/order/{id}")
    public String orderDetail(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if (user == null) {
            return "redirect:/auth/login";
        }

        com.orishop.model.Order order = orderService.getOrderById(id);
        if (order == null) {
            return "redirect:/orders"; // Order not found
        }

        // Security check: Order must belong to the logged-in user
        if (!order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders"; // Access denied
        }

        model.addAttribute("order", order);
        return "web/order-detail";
    }

    @PostMapping("/orders/complete")
    public String completeOrder(@RequestParam Long orderId, Principal principal,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        try {
            orderService.completeOrder(orderId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xác nhận nhận hàng thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/orders";
    }

    @PostMapping("/orders/return")
    public String requestReturn(@RequestParam Long orderId, @RequestParam String reason,
            @RequestParam String accountNumber,
            Principal principal,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        try {
            orderService.requestReturn(orderId, reason, accountNumber);
            redirectAttributes.addFlashAttribute("successMessage", "Đã gửi yêu cầu hoàn trả thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/orders";
    }

    @PostMapping("/orders/cancel")
    public String cancelOrder(@RequestParam Long orderId, @RequestParam(required = false) String reason,
            @RequestParam(required = false) String accountNumber,
            Principal principal,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        try {
            if (reason == null || reason.trim().isEmpty()) {
                reason = "Không có lý do";
            }
            orderService.cancelOrder(orderId, reason, accountNumber);
            redirectAttributes.addFlashAttribute("successMessage", "Đã gửi yêu cầu hủy đơn hàng thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/orders";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("addresses", addressRepository.findByUser(user));
        return "web/profile";
    }

    @PostMapping("/profile/address/add")
    public String addAddress(@ModelAttribute com.orishop.model.Address address,
            @RequestParam(defaultValue = "false") boolean defaultAddress,
            Principal principal,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        if (principal == null)
            return "redirect:/auth/login";
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if (user == null)
            return "redirect:/auth/login";

        address.setUser(user);

        if (addressRepository.findByUser(user).isEmpty()) {
            address.setDefault(true);
        } else if (defaultAddress) {
            // Unset other defaults
            java.util.List<com.orishop.model.Address> addresses = addressRepository.findByUser(user);
            for (com.orishop.model.Address addr : addresses) {
                addr.setDefault(false);
            }
            addressRepository.saveAll(addresses);
            address.setDefault(true);
        }

        addressRepository.save(address);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm địa chỉ thành công!");
        return "redirect:/profile";
    }

    @GetMapping("/profile/address/delete/{id}")
    public String deleteAddress(@PathVariable Long id, Principal principal) {
        if (principal == null)
            return "redirect:/auth/login";
        User user = userRepository.findByEmail(principal.getName()).orElse(null);

        com.orishop.model.Address address = addressRepository.findById(id).orElse(null);
        if (address != null && address.getUser().getId().equals(user.getId())) {
            addressRepository.delete(address);
        }
        return "redirect:/profile";
    }

    @GetMapping("/profile/address/setdefault/{id}")
    public String setDefaultAddress(@PathVariable Long id, Principal principal) {
        if (principal == null)
            return "redirect:/auth/login";
        User user = userRepository.findByEmail(principal.getName()).orElse(null);

        com.orishop.model.Address address = addressRepository.findById(id).orElse(null);
        if (address != null && address.getUser().getId().equals(user.getId())) {
            java.util.List<com.orishop.model.Address> addresses = addressRepository.findByUser(user);
            for (com.orishop.model.Address addr : addresses) {
                addr.setDefault(false);
            }
            addressRepository.saveAll(addresses);

            address.setDefault(true);
            addressRepository.save(address);
        }
        return "redirect:/profile";
    }

    @GetMapping("/recently-viewed")
    public String recentlyViewed(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if (user == null) {
            return "redirect:/auth/login";
        }

        java.util.List<Product> recentlyViewed = recentlyViewedService.getRecentlyViewedProducts(user, 20); // Limit 20
        populateFlashSaleInfo(recentlyViewed);
        model.addAttribute("recentlyViewed", recentlyViewed);

        return "web/recently-viewed";
    }

    @GetMapping("/contact")
    public String contact() {
        return "web/contact";
    }

    @GetMapping("/about")
    public String about() {
        return "web/about";
    }

    @PostMapping("/contact")
    public String submitContact(@RequestParam String name,
            @RequestParam String email,
            @RequestParam String subject,
            @RequestParam String message,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        com.orishop.model.Contact contact = new com.orishop.model.Contact();
        contact.setName(name);
        contact.setEmail(email);
        contact.setSubject(subject);
        contact.setMessage(message);

        contactRepository.save(contact);

        redirectAttributes.addFlashAttribute("successMessage",
                "Cảm ơn bạn đã liên hệ. Chúng tôi sẽ phản hồi sớm nhất có thể!");
        return "redirect:/contact";
    }

    @GetMapping("/checkout/success")
    public String checkoutSuccess() {
        return "web/checkout-success";
    }

    @Autowired
    private com.orishop.service.FlashSaleService flashSaleService;

    // Helper to populate sale info
    private void populateFlashSaleInfo(java.util.List<Product> products) {
        java.util.List<com.orishop.model.FlashSale> activeSales = flashSaleService
                .findActiveFlashSales(new java.util.Date());
        if (activeSales.isEmpty())
            return;

        for (Product p : products) {
            for (com.orishop.model.FlashSale sale : activeSales) {
                for (com.orishop.model.FlashSaleProduct fsp : sale.getFlashSaleProducts()) {
                    if (fsp.getProduct().getId().equals(p.getId())) {
                        if (p.getFlashSalePrice() == null || fsp.getSalePrice().compareTo(p.getFlashSalePrice()) < 0) {
                            p.setFlashSalePrice(fsp.getSalePrice());
                            p.setFlashSaleEndTime(sale.getEndTime());
                        }
                    }
                }
            }
        }
    }
}
