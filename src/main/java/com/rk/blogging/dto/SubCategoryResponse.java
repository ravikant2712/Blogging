package com.rk.blogging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubCategoryResponse {
    private Long id;
    private String name;
    private Long categoryId;
}