package com.github.mopaia.core.dao.model;

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
public class ProductReview {

    private UUID id;
    private UUID productId;
    private int score;
    private String name;
    private String comment;
    private Instant createdAt;
}
