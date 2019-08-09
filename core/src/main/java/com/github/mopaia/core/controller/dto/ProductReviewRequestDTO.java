package com.github.mopaia.core.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
public class ProductReviewRequestDTO {

    @Min(1)
    @Max(5)
    private int score;

    private String name;
    private String comment;
}
