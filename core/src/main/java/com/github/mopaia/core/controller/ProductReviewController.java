package com.github.mopaia.core.controller;

import com.github.mopaia.core.controller.dto.ProductReviewRequestDTO;
import com.github.mopaia.core.dao.ProductReviewRepository;
import com.github.mopaia.core.dao.dto.ProductReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductReviewController {

    private final ProductReviewRepository productReviewRepository;

    @GetMapping("/reviews")
    public ResponseEntity<List<ProductReviewDTO>> getReviews(@RequestParam(value = "productId", required = false) UUID productId) {
        return ResponseEntity.ok(Optional.ofNullable(productId)
                .map(productReviewRepository::filter)
                .orElseGet(productReviewRepository::list));
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<Object> create(@PathVariable("id") UUID productId, @RequestBody @Valid ProductReviewRequestDTO productReviewRequestDTO) {
        return productReviewRepository.create(
                productId,
                productReviewRequestDTO.getScore(),
                productReviewRequestDTO.getName(),
                productReviewRequestDTO.getComment())
                .map(productReview -> ResponseEntity.created(URI.create(String.format("/reviews/products/%s", productReview.getId()))))
                .orElseGet(ResponseEntity::badRequest)
                .build();
    }
}
