package com.github.mopaia.core.controller.dto;

import com.github.mopaia.core.dao.model.PurchaseExpense;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class PurchaseExpenseCreateRequestDTO {

    @NotNull
    private PurchaseExpense.Type type;

    @Min(1)
    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal unitPrice;
}
