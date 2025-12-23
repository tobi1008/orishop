package com.orishop.repository;

import com.orishop.model.Product;
import com.orishop.model.RecentlyViewed;
import com.orishop.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecentlyViewedRepository extends JpaRepository<RecentlyViewed, Long> {
    Optional<RecentlyViewed> findByUserAndProduct(User user, Product product);

    @Query("SELECT rv.product FROM RecentlyViewed rv WHERE rv.user = :user ORDER BY rv.viewedAt DESC")
    List<Product> findDidViewProducts(@Param("user") User user, Pageable pageable);

    void deleteByUser(User user);
}
