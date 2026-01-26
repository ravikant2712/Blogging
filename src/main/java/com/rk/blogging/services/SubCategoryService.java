package com.rk.blogging.services;

import com.rk.blogging.exceptions.PostNotFoundException;
import com.rk.blogging.exceptions.RecordNotFoundException;
import com.rk.blogging.model.Category;
import com.rk.blogging.model.SubCategory;
import com.rk.blogging.repository.CategoryRepository;
import com.rk.blogging.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubCategoryService {


    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public SubCategory findSubCategoryById(Long id) {
        return subCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("SubCategory not found with id " + id));
    }


    public SubCategory create(Long categoryId, SubCategory subCategory) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RecordNotFoundException("Category not found"));

        subCategory.setCategory(category);
        return subCategoryRepository.save(subCategory);
    }

    public SubCategory update(Long id, SubCategory subCategory) {
        SubCategory existing = subCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("SubCategory not found"));

        existing.setName(subCategory.getName());
        existing.setSlug(subCategory.getSlug());
        return subCategoryRepository.save(existing);
    }

    public void delete(Long id) {
        subCategoryRepository.deleteById(id);
    }

    public List<SubCategory> getByCategory(Long categoryId) {
        return subCategoryRepository.findByCategoryId(categoryId);
    }
}
