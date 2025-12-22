package com.orishop.controller.web;

import com.orishop.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final SettingService settingService;

    @GetMapping("/pages/{slug}")
    public String showPage(@PathVariable String slug, Model model) {
        String contentKey = "page_" + slug.replace("-", "_"); // e.g., privacy-policy -> page_privacy_policy
        String content = settingService.getSetting(contentKey);

        if (content == null) {
            return "error/404";
        }

        String titleKey = "";
        switch (slug) {
            case "privacy-policy":
                titleKey = "Chính sách bảo mật";
                break;
            case "terms-of-use":
                titleKey = "Điều khoản sử dụng";
                break;
            case "return-policy":
                titleKey = "Chính sách đổi trả";
                break;
            default:
                titleKey = "Trang thông tin";
        }

        model.addAttribute("pageTitle", titleKey);
        model.addAttribute("pageContent", content);
        return "web/page-detail";
    }
}
