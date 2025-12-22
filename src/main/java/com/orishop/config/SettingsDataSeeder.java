package com.orishop.config;

import com.orishop.repository.SettingRepository;
import com.orishop.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SettingsDataSeeder implements CommandLineRunner {

    private final SettingRepository settingRepository;
    private final SettingService settingService;

    @Override
    public void run(String... args) throws Exception {
        if (settingRepository.count() == 0) {
            Map<String, String> defaultSettings = new HashMap<>();

            // Branding
            defaultSettings.put("site_name", "OriShop");
            defaultSettings.put("site_logo", "https://placehold.co/200x50?text=OriShop"); // Default placeholder as
                                                                                          // current is HTML
            defaultSettings.put("primary_color", "#ec407a"); // Current pink color from header.html
            defaultSettings.put("footer_desc", "OriShop – Thiên đường mua sắm mỹ phẩm chính hãng, web bán mỹ phẩm số 1 Việt Nam.\n" + //
                                "OriShop cung cấp đa dạng các sản phẩm mỹ phẩm chính hãng từ những thương hiệu hàng đầu trong và ngoài nước. Cam kết 100% hàng thật, giá cạnh tranh, cập nhật xu hướng làm đẹp mới nhất, giao hàng nhanh chóng trên toàn quốc và dịch vụ chăm sóc khách hàng tận tâm. OriShop – nơi tôn vinh vẻ đẹp tự nhiên của bạn mỗi ngày.");

            // SEO
            defaultSettings.put("seo_title", "OriShop - Mỹ phẩm chính hãng");
            defaultSettings.put("seo_keywords", "my pham, son moi, duong da, orishop");
            defaultSettings.put("seo_desc",
                    "OriShop cung cấp các sản phẩm mỹ phẩm chính hãng, chất lượng cao với giá tốt nhất.");

            // Contact
            defaultSettings.put("contact_address", "123 Đường ABC, Quận XYZ, TP.HCM");
            defaultSettings.put("contact_phone", "0123456789");
            defaultSettings.put("contact_email", "support@orishop.com");
            defaultSettings.put("map_embed", "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3918.471676880053!2d106.77259277570497!3d10.851724657805167!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3175276e7ea103df%3A0xb6cf10bb7d719327!2zSFVURUNIIEtoHU9uZyBOZ2jhu4cgQ2Fv!5e0!3m2!1svi!2s!4v1710000000000!5m2!1svi!2s");

            // Social
            defaultSettings.put("social_facebook", "https://facebook.com");
            defaultSettings.put("social_instagram", "https://instagram.com");
            defaultSettings.put("social_youtube", "https://youtube.com");
            defaultSettings.put("social_tiktok", "https://tiktok.com");

            // Other
            defaultSettings.put("currency_symbol", "₫");

            // Pages
            defaultSettings.put("page_privacy_policy", "<h1>Chính sách bảo mật</h1><p>Nội dung đang cập nhật...</p>");
            defaultSettings.put("page_terms_of_use", "<h1>Điều khoản sử dụng</h1><p>Nội dung đang cập nhật...</p>");
            defaultSettings.put("page_return_policy", "<h1>Chính sách đổi trả</h1><p>Nội dung đang cập nhật...</p>");

            settingService.saveSettings(defaultSettings);
            System.out.println("--- Settings Data Seeded ---");
        }
    }
}
