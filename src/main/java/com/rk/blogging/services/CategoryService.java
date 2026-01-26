package com.rk.blogging.services;

import com.rk.blogging.exceptions.HandleRuntimeException;
import com.rk.blogging.model.Category;
import com.rk.blogging.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Long id, Category category) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new HandleRuntimeException("Category not found"));

        existing.setName(category.getName());
        existing.setSlug(category.getSlug());
        return categoryRepository.save(existing);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new HandleRuntimeException("Category not found"));
    }
}
