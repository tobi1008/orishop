package com.orishop.controller.admin;

import com.orishop.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.orishop.service.impl.CloudinaryService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
@RequestMapping("/admin/settings")
@RequiredArgsConstructor
public class AdminSettingController {

    private final SettingService settingService;
    private final CloudinaryService cloudinaryService;

    @GetMapping
    public String showSettings(Model model) {
        Map<String, String> settings = settingService.getAllSettings();
        model.addAttribute("settings", settings);
        return "admin/settings";
    }

    @PostMapping
    public String saveSettings(@RequestParam Map<String, String> allParams,
            @RequestParam(value = "site_logo_file", required = false) MultipartFile logoFile,
            RedirectAttributes redirectAttributes) {

        // Handle logo file upload
        if (logoFile != null && !logoFile.isEmpty()) {
            String logoUrl = cloudinaryService.uploadFile(logoFile);
            if (logoUrl != null) {
                allParams.put("site_logo", logoUrl);
            }
        }

        // allParams contains all form data
        settingService.saveSettings(allParams);
        redirectAttributes.addFlashAttribute("message", "Cập nhật cấu hình thành công!");
        return "redirect:/admin/settings";
    }
}
