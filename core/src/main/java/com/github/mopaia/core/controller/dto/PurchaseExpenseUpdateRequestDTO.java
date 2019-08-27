package com.github.mopaia.core.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class PurchaseExpenseUpdateRequestDTO {

    @Min(1)
    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal unitPrice;
}
