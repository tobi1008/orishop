package com.orishop.config;

import com.orishop.model.Category;
import com.orishop.model.Product;
import com.orishop.repository.CategoryRepository;
import com.orishop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class DataFixer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- STARTING DATA FIXER: Checking for invalid slugs ---");
        fixProductSlugs();
        fixCategorySlugs();
        System.out.println("--- DATA FIXER COMPLETED ---");
    }

    private void fixProductSlugs() {
        List<Product> products = productRepository.findAll();
        for (Product p : products) {
            String originalSlug = p.getSlug();
            String fixedSlug = toSlug(p.getName());

            // Re-generate checks if slug is null OR if it is invalid (contains special
            // chars that shouldn't be there)
            // But main issue is existing 'đ' which toSlug logic might have missed in the
            // past
            // Or if existing slug != new generated safe slug

            if (originalSlug == null || !originalSlug.equals(fixedSlug)) {
                // Only update if it looks invalid or different (simple check)
                // However, we want to be aggressive with 'đ'
                if (originalSlug != null && (originalSlug.contains("đ") || originalSlug.contains("Đ"))) {
                    System.out.println("Fixing Product Slug: " + p.getName() + " | Old: " + originalSlug + " -> New: "
                            + fixedSlug);
                    p.setSlug(fixedSlug);
                    productRepository.save(p);
                }
            }
        }
    }

    private void fixCategorySlugs() {
        List<Category> categories = categoryRepository.findAll();
        for (Category c : categories) {
            String originalSlug = c.getSlug();
            if (originalSlug != null && (originalSlug.contains("đ") || originalSlug.contains("Đ"))) {
                String fixedSlug = toSlug(c.getName());
                System.out.println(
                        "Fixing Category Slug: " + c.getName() + " | Old: " + originalSlug + " -> New: " + fixedSlug);
                c.setSlug(fixedSlug);
                categoryRepository.save(c);
            }
        }
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
}
