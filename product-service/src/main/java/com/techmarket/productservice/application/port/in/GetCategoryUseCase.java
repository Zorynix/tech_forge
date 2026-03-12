package com.techmarket.productservice.application.port.in;

import com.techmarket.productservice.domain.model.Category;

import java.util.List;
import java.util.UUID;

public interface GetCategoryUseCase {

    List<Category> getAll();

    Category getById(UUID id);
}
