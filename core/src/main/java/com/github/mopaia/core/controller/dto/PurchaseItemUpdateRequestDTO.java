package com.github.mopaia.core.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class PurchaseItemUpdateRequestDTO {

    @Min(1)
    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal unitPrice;
}
