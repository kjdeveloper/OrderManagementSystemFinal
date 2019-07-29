package com.app.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class CategoryDto {

    private Long id;
    private String name;
    private Set<ProductDto> productDtos;
}
