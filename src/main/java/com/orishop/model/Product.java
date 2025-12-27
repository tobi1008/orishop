package com.orishop.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Giá gốc
    @Column(nullable = false)
    private BigDecimal price;

    // Giá khuyến mãi (nếu có)
    private BigDecimal discountPrice;

    private Integer stockQuantity;

    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Transient
    private BigDecimal flashSalePrice;

    @Transient
    private Date flashSaleEndTime;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    public double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return Math.round((sum / reviews.size()) * 10.0) / 10.0;
    }

    public int getReviewCount() {
        return reviews == null ? 0 : reviews.size();
    }
}
