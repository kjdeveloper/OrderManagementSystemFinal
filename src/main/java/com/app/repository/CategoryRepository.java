package com.app.repository;

import com.app.dto.CategoryDto;
import com.app.model.Category;
import com.app.repository.generic.GenericRepository;

import java.util.Optional;

public interface CategoryRepository extends GenericRepository<Category> {
    Optional<Category> findByName(String categoryName);
}
