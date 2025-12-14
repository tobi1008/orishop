package com.orishop.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "flash_sales")
public class FlashSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date endTime;

    private boolean status = true; // active/inactive

    @OneToMany(mappedBy = "flashSale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlashSaleProduct> flashSaleProducts;
}
