package com.orishop.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String slug; // Ví dụ: sua-rua-mat, son-moi

    private String description;

    private String image;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
