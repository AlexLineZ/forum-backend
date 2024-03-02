package com.example.forumcore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Category extends BaseEntity {
    private String name;

    @ManyToOne
    private Category parentCategory;
}
