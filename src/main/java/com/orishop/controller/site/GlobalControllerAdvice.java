package com.orishop.controller.site;

import com.orishop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.orishop.controller.site") // Chỉ áp dụng cho các controller phía người dùng
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CategoryRepository categoryRepository;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        // Luôn có danh sách categories ở mọi trang để render menu
        model.addAttribute("globalCategories", categoryRepository.findAll());
    }
}
