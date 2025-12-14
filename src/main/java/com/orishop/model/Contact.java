package com.orishop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "contacts")
@Getter
@Setter
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
}
