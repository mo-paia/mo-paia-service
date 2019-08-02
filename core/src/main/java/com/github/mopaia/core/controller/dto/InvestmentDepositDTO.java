package com.github.mopaia.core.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InvestmentDepositDTO {
    private String investorName;
    private BigDecimal amount;
}
