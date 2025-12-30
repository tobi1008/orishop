package com.orishop.controller.admin;

import com.orishop.model.Category;
import com.orishop.model.Order;
import com.orishop.model.OrderStatus;
import com.orishop.model.Product;
import com.orishop.model.ProductImage;
import com.orishop.repository.CategoryRepository;
import com.orishop.repository.OrderRepository;
import com.orishop.repository.ProductImageRepository;
import com.orishop.repository.UserRepository;
import com.orishop.service.impl.CloudinaryService;
import com.orishop.service.impl.ProductService;
import com.orishop.service.impl.ProductService;
import com.orishop.service.ReviewService;
import com.orishop.service.CouponService;
import com.orishop.service.OrderService;
import com.orishop.model.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.List;
import java.text.Normalizer;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // Service upload ảnh & Repository lưu ảnh
    private final CloudinaryService cloudinaryService;
    private final ProductImageRepository productImageRepository;
    private final ReviewService reviewService;
    private final OrderService orderService; // Injected OrderService
    private final CouponService couponService;

    @GetMapping
    public String dashboard(Model model) {
        // Thống kê thực tế từ DB
        model.addAttribute("totalProducts", productService.getAllProducts().size());
        // model.addAttribute("totalCategories", categoryRepository.count()); // Không
        // dùng trong design mới
        model.addAttribute("totalOrders", orderRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalCoupons", couponService.getAllCoupons().size());

        // Tính tổng doanh thu
        BigDecimal totalRevenue = orderRepository.findAll().stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalRevenue", totalRevenue);

        // Đơn hàng gần đây
        model.addAttribute("recentOrders", orderRepository.findTop5ByOrderByCreatedAtDesc());

        return "admin/dashboard";
    }

    // --- Products ---
    @GetMapping("/products")
    public String productList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/product-list";
    }

    @GetMapping("/products/new")
    public String createProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product-form";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id).orElse(null);
        if (product == null) {
            return "redirect:/admin/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product-form";
    }

    @PostMapping("/products")
    public String saveProduct(@ModelAttribute Product product,
            @RequestParam("fileImage") MultipartFile[] files,
            @RequestParam(required = false) java.util.List<Long> deleteImageIds) {

        Product productToSave;

        if (product.getSlug() == null || product.getSlug().trim().isEmpty()) {
            product.setSlug(toSlug(product.getName()));
        }

        // 1. Kiểm tra xem là Thêm mới hay Cập nhật
        if (product.getId() != null) {
            // Trường hợp Edit: Lấy sản phẩm cũ từ DB ra để giữ lại các quan hệ (Images)
            Product existingProduct = productService.getProductById(product.getId()).orElse(null);
            if (existingProduct != null) {
                // Cập nhật các thông tin từ Form vào object cũ
                existingProduct.setName(product.getName());
                existingProduct.setPrice(product.getPrice());
                existingProduct.setStockQuantity(product.getStockQuantity());
                existingProduct.setCategory(product.getCategory());
                existingProduct.setDescription(product.getDescription());
                existingProduct.setDiscountPrice(product.getDiscountPrice());
                existingProduct.setSlug(product.getSlug()); // Should be non-empty now
                // KHÔNG set lại images thành null, giữ nguyên images cũ

                productToSave = existingProduct;
            } else {
                // Nếu ID gửi lên không tồn tại (hiếm), coi như tạo mới
                productToSave = product;
            }
        } else {
            // Trường hợp Tạo mới
            productToSave = product;
        }

        // 2. Lưu sản phẩm
        Product savedProduct = productService.saveProduct(productToSave);

        // 2.1 Xử lý xoá ảnh nếu có yêu cầu
        if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
            productImageRepository.deleteAllById(deleteImageIds);
        }

        // 3. Xử lý upload ảnh nếu có file mới
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String imageUrl = cloudinaryService.uploadFile(file);
                    if (imageUrl != null) {
                        ProductImage image = new ProductImage();
                        image.setProduct(savedProduct);
                        image.setImageUrl(imageUrl);
                        // Logic đơn giản: Ảnh đầu tiên trong danh sách hiện tại (hoặc mới thêm) có thể
                        // set là primary nếu chưa có
                        // Nhưng hiện tại để đơn giản cứ lưu vào đã.
                        productImageRepository.save(image);
                    }
                }
            }
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    // --- Categories ---
    @GetMapping("/categories")
    public String categoryList(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/category-list";
    }

    @GetMapping("/categories/new")
    public String createCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category-form";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return "redirect:/admin/categories";
        }
        model.addAttribute("category", category);
        return "admin/category-form";
    }

    @PostMapping("/categories")
    public String saveCategory(@ModelAttribute Category category) {
        if (category.getSlug() == null || category.getSlug().trim().isEmpty()) {
            category.setSlug(toSlug(category.getName()));
        }

        if (category.getId() != null) {
            // Edit mode: fetch existing to keep relationships if any
            Category existingCategory = categoryRepository.findById(category.getId()).orElse(null);
            if (existingCategory != null) {
                existingCategory.setName(category.getName());
                existingCategory.setSlug(category.getSlug());
                // Keep other fields like products
                categoryRepository.save(existingCategory);
            }
        } else {
            // New mode
            categoryRepository.save(category);
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        // Cần xử lý logic: nếu category có sản phẩm thì không cho xóa hoặc set sản phẩm
        // về null
        // Tạm thời xóa cứng
        try {
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            // Có thể thêm flash message báo lỗi ràng buộc
        }
        return "redirect:/admin/categories";
    }

    // --- Orders ---
    @GetMapping("/orders")
    public String orderList(@RequestParam(required = false) OrderStatus status, Model model) {
        if (status != null) {
            model.addAttribute("orders", orderRepository.findByStatus(status));
            model.addAttribute("currentStatus", status);
        } else {
            model.addAttribute("orders", orderRepository.findAll());
        }
        return "admin/order-list";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return "redirect:/admin/orders";
        }
        model.addAttribute("order", order);
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/order-detail";
    }

    @PostMapping("/orders/update-status")
    public String updateOrderStatus(@RequestParam Long id, @RequestParam OrderStatus status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/admin/orders/" + id;
    }

    @PostMapping("/orders/update-payment-status")
    public String updatePaymentStatus(@RequestParam Long id, @RequestParam boolean paymentStatus,
            @RequestHeader(value = "Referer", required = false) String referer) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setPaymentStatus(paymentStatus);
            orderRepository.save(order);
        }
        return "redirect:" + (referer != null ? referer : "/admin/orders/" + id);
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return "redirect:/admin/orders";
    }

    // --- Users ---
    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/user-list";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    // --- System Utilities ---
    @GetMapping("/system/fix-slugs")
    public String fixSlugs() {
        // Fix Products
        List<Product> products = productService.getAllProducts();
        for (Product p : products) {
            if (p.getSlug() == null || p.getSlug().trim().isEmpty()) {
                p.setSlug(toSlug(p.getName()));
                productService.saveProduct(p);
            }
        }

        // Fix Categories
        List<Category> categories = categoryRepository.findAll();
        for (Category c : categories) {
            if (c.getSlug() == null || c.getSlug().trim().isEmpty()) {
                c.setSlug(toSlug(c.getName()));
                categoryRepository.save(c);
            }
        }

        return "redirect:/admin?fixedSlugs=true";
    }

    private String toSlug(String input) {
        if (input == null)
            return "";
        String nowhitespace = input.trim().replaceAll("\\s+", "-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String slug = pattern.matcher(normalized).replaceAll("");
        return slug.toLowerCase().replaceAll("đ", "d").replaceAll("Đ", "d");
    }

    // --- Reviews ---
    @GetMapping("/reviews")
    public String reviewList(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviews());
        return "admin/review-list";
    }

    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return "redirect:/admin/reviews";
    }

    // --- Coupons ---
    @GetMapping("/coupons")
    public String couponList(Model model) {
        model.addAttribute("coupons", couponService.getAllCoupons());
        return "admin/coupon-list";
    }

    @GetMapping("/coupons/new")
    public String createCouponForm(Model model) {
        model.addAttribute("coupon", new Coupon());
        return "admin/coupon-form";
    }

    @GetMapping("/coupons/edit/{id}")
    public String editCouponForm(@PathVariable Long id, Model model) {
        Coupon coupon = couponService.getCouponById(id);
        if (coupon == null) {
            return "redirect:/admin/coupons";
        }
        model.addAttribute("coupon", coupon);
        return "admin/coupon-form";
    }

    @PostMapping("/coupons")
    public String saveCoupon(@ModelAttribute Coupon coupon) {
        // Basic validation/setting defaults if needed
        if (coupon.getUsedCount() == null) {
            coupon.setUsedCount(0);
        }
        couponService.saveCoupon(coupon);
        return "redirect:/admin/coupons";
    }

    @GetMapping("/coupons/delete/{id}")
    public String deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return "redirect:/admin/coupons";
    }

    // --- Flash Sales ---
    @Autowired
    private com.orishop.service.FlashSaleService flashSaleService;

    @GetMapping("/flash-sales")
    public String flashSaleList(Model model) {
        model.addAttribute("flashSales", flashSaleService.getAllFlashSales());
        return "admin/flash-sale-list";
    }

    @GetMapping("/flash-sales/new")
    public String createFlashSaleForm(Model model) {
        model.addAttribute("flashSale", new com.orishop.model.FlashSale());
        return "admin/flash-sale-form";
    }

    @GetMapping("/flash-sales/edit/{id}")
    public String editFlashSaleForm(@PathVariable Long id, Model model) {
        com.orishop.model.FlashSale flashSale = flashSaleService.getFlashSaleById(id);
        if (flashSale == null) {
            return "redirect:/admin/flash-sales";
        }
        model.addAttribute("flashSale", flashSale);
        return "admin/flash-sale-form";
    }

    @PostMapping("/flash-sales")
    public String saveFlashSale(@ModelAttribute com.orishop.model.FlashSale flashSale,
            @RequestParam(value = "status", defaultValue = "false") boolean status) {
        flashSale.setStatus(status); // Explicitly set status from checkbox (or default false if unchecked)

        if (flashSale.getId() != null) {
            com.orishop.model.FlashSale existing = flashSaleService.getFlashSaleById(flashSale.getId());
            if (existing != null) {
                // Update specific fields to avoid overwriting relations if any
                existing.setName(flashSale.getName());
                existing.setDescription(flashSale.getDescription());
                existing.setStartTime(flashSale.getStartTime());
                existing.setEndTime(flashSale.getEndTime());
                existing.setStatus(flashSale.isStatus());
                flashSaleService.saveFlashSale(existing);
            } else {
                flashSaleService.saveFlashSale(flashSale);
            }
        } else {
            flashSaleService.saveFlashSale(flashSale);
        }
        return "redirect:/admin/flash-sales";
    }

    @GetMapping("/flash-sales/delete/{id}")
    public String deleteFlashSale(@PathVariable Long id) {
        flashSaleService.deleteFlashSale(id);
        return "redirect:/admin/flash-sales";
    }

    @GetMapping("/flash-sales/{id}/products")
    public String flashSaleProducts(@PathVariable Long id, Model model) {
        com.orishop.model.FlashSale flashSale = flashSaleService.getFlashSaleById(id);
        if (flashSale == null) {
            return "redirect:/admin/flash-sales";
        }
        model.addAttribute("flashSale", flashSale);
        model.addAttribute("products", productService.getAllProducts()); // For selection
        model.addAttribute("categories", categoryRepository.findAll()); // For category selection
        return "admin/flash-sale-products";
    }

    @PostMapping("/flash-sales/{id}/products")
    public String addProductToFlashSale(@PathVariable Long id,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) BigDecimal salePrice,
            @RequestParam(defaultValue = "FIXED") String discountType,
            @RequestParam(required = false) Double discountValue) {

        flashSaleService.addProductToFlashSale(id, productId, salePrice, discountType, discountValue);
        return "redirect:/admin/flash-sales/" + id + "/products";
    }

    @PostMapping("/flash-sales/{id}/add-category")
    public String addCategoryToFlashSale(@PathVariable Long id,
            @RequestParam Long categoryId,
            @RequestParam Double discountPercent) {
        flashSaleService.addCategoryToFlashSale(id, categoryId, discountPercent);
        return "redirect:/admin/flash-sales/" + id + "/products";
    }

    @GetMapping("/flash-sales/products/remove/{id}")
    public String removeProductFromFlashSale(@PathVariable Long id, @RequestParam Long flashSaleId) {
        flashSaleService.removeProductFromFlashSale(id);
        return "redirect:/admin/flash-sales/" + flashSaleId + "/products";
    }
}
