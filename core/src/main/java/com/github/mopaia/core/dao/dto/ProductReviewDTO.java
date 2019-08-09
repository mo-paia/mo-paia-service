package com.github.mopaia.core.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewDTO {

    private UUID id;
    private UUID productId;
    private String productName;
    private int score;
    private String name;
    private String comment;
    private Instant createdAt;
}
