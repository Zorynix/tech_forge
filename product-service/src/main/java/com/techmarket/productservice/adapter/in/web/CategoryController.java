package com.techmarket.productservice.adapter.in.web;

import com.techmarket.productservice.adapter.in.web.dto.CategoryResponse;
import com.techmarket.productservice.application.port.in.GetCategoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final GetCategoryUseCase getCategoryUseCase;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        var categories = getCategoryUseCase.getAll().stream()
                .map(CategoryResponse::from)
                .toList();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable UUID id) {
        var category = getCategoryUseCase.getById(id);
        return ResponseEntity.ok(CategoryResponse.from(category));
    }
}
