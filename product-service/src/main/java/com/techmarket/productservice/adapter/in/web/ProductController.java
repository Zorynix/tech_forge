package com.techmarket.productservice.adapter.in.web;

import com.techmarket.productservice.adapter.in.web.dto.PageResponse;
import com.techmarket.productservice.adapter.in.web.dto.ProductResponse;
import com.techmarket.productservice.application.port.in.GetProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final GetProductUseCase getProductUseCase;

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getAll(
            @PageableDefault(size = 20) Pageable pageable) {
        var page = getProductUseCase.getAll(pageable);
        return ResponseEntity.ok(PageResponse.from(page, ProductResponse::from));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable UUID id) {
        var product = getProductUseCase.getById(id);
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductResponse>> search(
            @RequestParam("q") String query,
            @PageableDefault(size = 20) Pageable pageable) {
        var page = getProductUseCase.searchByName(query, pageable);
        return ResponseEntity.ok(PageResponse.from(page, ProductResponse::from));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageResponse<ProductResponse>> getByCategory(
            @PathVariable UUID categoryId,
            @PageableDefault(size = 20) Pageable pageable) {
        var page = getProductUseCase.getByCategory(categoryId, pageable);
        return ResponseEntity.ok(PageResponse.from(page, ProductResponse::from));
    }
}
