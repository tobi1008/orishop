package com.orishop.controller.site;

import com.orishop.model.Product;
import com.orishop.service.impl.ProductService;
import com.orishop.service.impl.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final ProductService productService;
    private final CartService cartService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "web/index"; // templates/web/index.html
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        return "web/product-detail";
    }

    @GetMapping("/product")
    public String allProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "web/product-list";
    }

    @GetMapping("/product/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("products", productService.searchProducts(keyword));
        return "web/product-list";
    }

    @GetMapping("/product/category/{categoryId}")
    public String productsByCategory(@PathVariable Long categoryId, Model model) {
        model.addAttribute("products", productService.getProductsByCategory(categoryId));
        return "web/product-list";
    }

    // --- Cart Actions ---
    @GetMapping("/cart")
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCart());
        model.addAttribute("cartTotal", cartService.getCartTotal());
        return "web/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.addToCart(productId, quantity);
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

    private final com.orishop.service.OrderService orderService;

    // ... (các API Cart cũ giữ nguyên) ...

    @GetMapping("/cart/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }

    // --- Checkout ---
    @GetMapping("/checkout")
    public String checkoutForm(Model model) {
        if (cartService.getCart().isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("cartItems", cartService.getCart());
        model.addAttribute("cartTotal", cartService.getCartTotal());
        return "web/checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(@RequestParam String fullName,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam(required = false) String note) {

        // Hiện tại user đang để null (khách lẻ). Sau này dùng SecurityContextHolder lấy
        // user
        orderService.placeOrder(null, cartService.getCart(), fullName, phone, address, note);

        cartService.clearCart();
        return "redirect:/checkout/success";
    }

    @GetMapping("/checkout/success")
    public String checkoutSuccess() {
        return "web/checkout-success";
    }
}
