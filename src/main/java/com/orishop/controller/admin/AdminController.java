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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.List;

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

    @GetMapping
    public String dashboard(Model model) {
        // Thống kê thực tế từ DB
        model.addAttribute("totalProducts", productService.getAllProducts().size());
        // model.addAttribute("totalCategories", categoryRepository.count()); // Không
        // dùng trong design mới
        model.addAttribute("totalOrders", orderRepository.count());
        model.addAttribute("totalUsers", userRepository.count());

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
    public String orderList(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
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
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
        return "redirect:/admin/orders/" + id;
    }

    // --- Users ---
    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/user-list";
    }
}
