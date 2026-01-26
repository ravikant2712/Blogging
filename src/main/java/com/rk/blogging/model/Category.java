package com.rk.blogging.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Setter
@Getter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String slug;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<SubCategory> subCategories;

    @PrePersist
    protected void onCreate() {
    //    createdAt = LocalDateTime.now();
    //    updatedAt = LocalDateTime.now();
        generateSlug();
    }

    @PreUpdate
    protected void onUpdate() {
    //    updatedAt = LocalDateTime.now();
        generateSlug();
    }

    private void generateSlug() {
        if (name != null && !name.isEmpty()) {
            String nowhitespace = Pattern.compile("\\s").matcher(name).replaceAll("-");
            String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
            slug = Pattern.compile("[^\\w\\-]").matcher(normalized).replaceAll("").toLowerCase(Locale.ENGLISH);
        }
    }
}