package com.orishop.service;

import com.orishop.model.Review;
import java.util.List;

public interface ReviewService {
    Review saveReview(Review review);

    List<Review> getReviewsByProduct(Long productId);

    List<Review> getAllReviews();

    void deleteReview(Long id);

    Review getReviewById(Long id);
}
