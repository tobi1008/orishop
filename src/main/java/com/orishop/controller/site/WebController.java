package com.orishop.controller.site;

import com.orishop.model.Product;
import com.orishop.model.Category;
import com.orishop.repository.CategoryRepository;
import com.orishop.service.impl.ProductService;
import com.orishop.service.impl.CartService;
import com.orishop.service.ReviewService;
import com.orishop.repository.UserRepository;
import com.orishop.model.Review;
import com.orishop.model.User;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final ProductService productService;
    private final CartService cartService;
    private final CategoryRepository categoryRepository;
    private final com.orishop.service.OrderService orderService;
    private final ReviewService reviewService;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "web/index"; // templates/web/index.html
    }

    @GetMapping("/product/{slug}")
    public String productDetail(@PathVariable String slug, Model model) {
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

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviewService.getReviewsByProduct(product.getId()));
        return "web/product-detail";
    }

    @PostMapping("/product/review")
    public String addReview(@RequestParam Long productId,
            @RequestParam int rating,
            @RequestParam String comment,
            Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }

        Product product = productService.getProductById(productId).orElse(null);
        if (product == null) {
            return "redirect:/";
        }

        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if (user == null) {
            return "redirect:/auth/login";
        }

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(rating);
        review.setComment(comment);

        reviewService.saveReview(review);

        String slug = product.getSlug();
        if (slug == null || slug.isEmpty()) {
            slug = String.valueOf(product.getId());
        }

        return "redirect:/product/" + slug;
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

        model.addAttribute("products", productService.getProductsByCategory(category.getId()));
        model.addAttribute("currentCategory", category);
        return "web/product-list";
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
    public String checkoutForm(Model model) {
        if (cartService.getCart().isEmpty()) {
            return "redirect:/cart";
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
            Principal principal) {

        User user = null;
        if (principal != null) {
            user = userRepository.findByEmail(principal.getName()).orElse(null);
        }

        orderService.placeOrder(user, cartService.getCart(), fullName, phone, address, note,
                cartService.getAppliedCoupon(), cartService.getDiscountAmount());

        cartService.clearCart();
        return "redirect:/checkout/success";
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

        model.addAttribute("orders", orderService.getOrdersByUser(user.getId()));
        model.addAttribute("orders", orderService.getOrdersByUser(user.getId()));
        return "web/order-list";
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
        return "web/profile";
    }

    @GetMapping("/checkout/success")
    public String checkoutSuccess() {
        return "web/checkout-success";
    }
}
