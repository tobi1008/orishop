package com.orishop.config;

import com.orishop.model.Category;
import com.orishop.model.Product;
import com.orishop.model.ProductImage;
import com.orishop.repository.CategoryRepository;
import com.orishop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ProductDataSeeder implements CommandLineRunner {

        private final CategoryRepository categoryRepository;
        private final ProductRepository productRepository;

        // Hardcoded high-quality Unsplash image IDs
        private final List<String> lipstickImages = Arrays.asList(
                        "https://images.unsplash.com/photo-1586495777744-4413f21062fa?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1627384113743-6bd5a479fffd?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1596462502278-27bfdd403ea2?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1625093742435-6fa192b6fb10?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1617391986616-a36c92d54406?auto=format&fit=crop&w=600&q=80");

        private final List<String> skincareImages = Arrays.asList(
                        "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1556228720-191739c23b2d?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1608248597279-f99d160bfbc8?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1616683693504-3ea7e9ad6fec?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1598440947619-2c35fc9aa908?auto=format&fit=crop&w=600&q=80");

        private final List<String> makeupImages = Arrays.asList(
                        "https://images.unsplash.com/photo-1512496015851-a90fb38ba796?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1522337660859-02fbefca4702?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1516975080664-ed2fc6a32937?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1503236823255-943cb751f9c4?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1596462502278-27bfdd403ea2?auto=format&fit=crop&w=600&q=80");

        private final List<String> perfumeImages = Arrays.asList(
                        "https://images.unsplash.com/photo-1523293182086-7651a899d37f?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1541643600914-78b084683601?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1594035910387-fea4779426e9?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1592945403244-b3fbafd7f539?auto=format&fit=crop&w=600&q=80",
                        "https://images.unsplash.com/photo-1585386959984-a4155224a1ad?auto=format&fit=crop&w=600&q=80");

        @Override
        public void run(String... args) throws Exception {
                if (productRepository.count() == 0) {
                        seedData();
                }
        }

        private void seedData() {
                // Categories
                Category c1 = createCategory("Son môi", "son-moi", "Các loại son môi cao cấp",
                                "https://images.unsplash.com/photo-1586495777744-4413f21062fa?auto=format&fit=crop&w=300&q=80");
                Category c2 = createCategory("Chăm sóc da", "cham-soc-da", "Sản phẩm skincare chính hãng",
                                "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&w=300&q=80");
                Category c3 = createCategory("Trang điểm mặt", "trang-diem-mat", "Phấn phủ, kem nền, má hồng",
                                "https://images.unsplash.com/photo-1522337660859-02fbefca4702?auto=format&fit=crop&w=300&q=80");
                Category c4 = createCategory("Nước hoa", "nuoc-hoa", "Nước hoa nam nữ chính hãng",
                                "https://images.unsplash.com/photo-1523293182086-7651a899d37f?auto=format&fit=crop&w=300&q=80");

                // Products - Son môi
                createProductWithImages("Son MAC Chili", "son-mac-chili", new BigDecimal("650000"), c1,
                                "Son MAC màu đỏ gạch cực hot.", lipstickImages);
                createProductWithImages("Son Black Rouge A12", "son-black-rouge-a12", new BigDecimal("180000"), c1,
                                "Son kem lì Black Rouge màu đỏ đất.", lipstickImages);
                createProductWithImages("Son 3CE Taupe", "son-3ce-taupe", new BigDecimal("320000"), c1,
                                "Màu đỏ nâu cực sang chảnh.", lipstickImages);
                createProductWithImages("Son YSL 212", "son-ysl-212", new BigDecimal("890000"), c1,
                                "Dòng son cao cấp YSL màu cam cháy.", lipstickImages);
                createProductWithImages("Son Dior 999", "son-dior-999", new BigDecimal("950000"), c1,
                                "Huyền thoại đỏ tươi của Dior.", lipstickImages);

                // Products - Chăm sóc da
                createProductWithImages("Sữa rửa mặt Cetaphil", "srm-cetaphil", new BigDecimal("250000"), c2,
                                "Dịu nhẹ cho mọi loại da.", skincareImages);
                createProductWithImages("Toner Klairs", "toner-klairs", new BigDecimal("310000"), c2,
                                "Nước hoa hồng không mùi dưỡng ẩm sâu.", skincareImages);
                createProductWithImages("Serum Estee Lauder ANR", "serum-estee-lauder", new BigDecimal("2500000"), c2,
                                "Serum phục hồi ban đêm thần thánh.", skincareImages);
                createProductWithImages("Kem dưỡng La Roche-Posay B5", "kem-duong-b5", new BigDecimal("350000"), c2,
                                "Phục hồi da, giảm kích ứng.", skincareImages);
                createProductWithImages("Tẩy trang Bioderma hồng", "tay-trang-bioderma", new BigDecimal("450000"), c2,
                                "Tẩy trang dịu nhẹ cho da nhạy cảm.", skincareImages);

                // Products - Trang điểm mặt
                createProductWithImages("Phấn phủ Innisfree", "phan-phu-innisfree", new BigDecimal("150000"), c3,
                                "Kiềm dầu cực tốt.", makeupImages);
                createProductWithImages("Kem nền Maybelline Fit Me", "kem-nen-fitme", new BigDecimal("220000"), c3,
                                "Che phủ tốt, tiệp màu da.", makeupImages);
                createProductWithImages("Má hồng Nars Orgasm", "ma-hong-nars", new BigDecimal("780000"), c3,
                                "Màu hồng cam nhũ vàng iconic.", makeupImages);
                createProductWithImages("Che khuyết điểm The Saem", "ckd-the-saem", new BigDecimal("90000"), c3,
                                "Che phủ hoàn hảo vết thâm mụn.", makeupImages);
                createProductWithImages("Kẻ mắt Kiss Me", "ke-mat-kissme", new BigDecimal("280000"), c3,
                                "Lâu trôi, không lem.", makeupImages);

                // Products - Nước hoa
                createProductWithImages("Nước hoa Chanel No.5", "chanel-no5", new BigDecimal("3500000"), c4,
                                "Hương thơm cổ điển, sang trọng.", perfumeImages);
                createProductWithImages("Nước hoa Dior Sauvage", "dior-sauvage", new BigDecimal("2800000"), c4,
                                "Mùi hương nam tính, mạnh mẽ.", perfumeImages);
                createProductWithImages("Nước hoa YSL Libre", "ysl-libre", new BigDecimal("3200000"), c4,
                                "Tự do, quyến rũ cho phái nữ.", perfumeImages);
                createProductWithImages("Nước hoa Le Labo 33", "le-labo-33", new BigDecimal("5500000"), c4,
                                "Hương gỗ đàn hương độc đáo.", perfumeImages);
                createProductWithImages("Nước hoa Narciso Rodriguez For Her", "narciso-hong", new BigDecimal("2600000"),
                                c4, "Xạ hương quyến rũ.", perfumeImages);

                System.out.println("--- Product Data Seeded ---");
        }

        private Category createCategory(String name, String slug, String description, String image) {
                return categoryRepository.findBySlug(slug).orElseGet(() -> {
                        Category c = new Category();
                        c.setName(name);
                        c.setSlug(slug);
                        c.setDescription(description);
                        c.setImage(image);
                        return categoryRepository.save(c);
                });
        }

        private void createProductWithImages(String name, String slug, BigDecimal price, Category category, String desc,
                        List<String> imageUrls) {
                Product p = Product.builder()
                                .name(name)
                                .slug(slug)
                                .price(price)
                                .stockQuantity(100)
                                .category(category)
                                .description(desc)
                                .build();

                // Select random image from list deterministically based on slug to keep it
                // consistent
                int index1 = Math.abs(slug.hashCode()) % imageUrls.size();
                int index2 = (index1 + 1) % imageUrls.size();

                ProductImage img1 = ProductImage.builder()
                                .product(p)
                                .imageUrl(imageUrls.get(index1))
                                .isPrimary(true)
                                .build();

                ProductImage img2 = ProductImage.builder()
                                .product(p)
                                .imageUrl(imageUrls.get(index2))
                                .isPrimary(false)
                                .build();

                p.setImages(Arrays.asList(img1, img2));

                productRepository.save(p);
        }
}
