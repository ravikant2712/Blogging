package com.rk.blogging.controller;

import com.rk.blogging.model.SubCategory;
import com.rk.blogging.services.SubCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
@RequiredArgsConstructor
@Tag(name = "SubCategories", description = "Blog Sub Categories CRUD APIs")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @Operation(
            summary = "Create Sub Category",
            description = "Create new Sub Category",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/category/{categoryId}")
    public ResponseEntity<SubCategory> create(
            @PathVariable Long categoryId,
            @RequestBody SubCategory subCategory) {
        return ResponseEntity.ok(
                subCategoryService.create(categoryId, subCategory)
        );
    }

    @Operation(
            summary = "Update Sub Category",
            description = "Update Sub Category by id",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}")
    public ResponseEntity<SubCategory> update(
            @PathVariable Long id,
            @RequestBody SubCategory subCategory) {
        return ResponseEntity.ok(
                subCategoryService.update(id, subCategory)
        );
    }

    @Operation(
            summary = "Delete Subcategory",
            description = "Delete Subcategory by id",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get Sub Category ID",
            description = "Returns list of all sub category by category id",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<SubCategory>> getByCategory(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(
                subCategoryService.getByCategory(categoryId)
        );
    }
}
