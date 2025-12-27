package com.orishop.controller.site;

import com.orishop.model.Product;
import com.orishop.model.Review;
import com.orishop.model.User;
import com.orishop.repository.UserRepository;
import com.orishop.service.ReviewService;
import com.orishop.service.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ProductService productService;
    private final UserRepository userRepository;

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
}
