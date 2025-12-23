package com.orishop.service.impl;

import com.orishop.model.Product;
import com.orishop.model.RecentlyViewed;
import com.orishop.model.User;
import com.orishop.repository.RecentlyViewedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecentlyViewedService {

    private final RecentlyViewedRepository recentlyViewedRepository;

    public void recordView(User user, Product product) {
        if (user == null || product == null) {
            return;
        }

        Optional<RecentlyViewed> existing = recentlyViewedRepository.findByUserAndProduct(user, product);
        if (existing.isPresent()) {
            RecentlyViewed rv = existing.get();
            rv.setViewedAt(new Date());
            recentlyViewedRepository.save(rv);
        } else {
            RecentlyViewed rv = RecentlyViewed.builder()
                    .user(user)
                    .product(product)
                    .viewedAt(new Date())
                    .build();
            recentlyViewedRepository.save(rv);
        }
    }

    public List<Product> getRecentlyViewedProducts(User user, int limit) {
        if (user == null) {
            return List.of();
        }
        Pageable pageable = PageRequest.of(0, limit);
        return recentlyViewedRepository.findDidViewProducts(user, pageable);
    }
}
