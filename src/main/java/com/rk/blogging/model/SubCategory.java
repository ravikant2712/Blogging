package com.rk.blogging.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

@Setter
@Getter
@Entity
@Table(name = "sub_categories")
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String slug;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

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
