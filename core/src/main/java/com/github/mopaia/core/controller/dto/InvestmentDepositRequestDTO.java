package com.github.mopaia.core.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class InvestmentDepositRequestDTO {

    @NotNull
    @NotEmpty
    private String investorName;

    @NotNull
    @Min(1)
    private BigDecimal amount;
}
