package com.github.mopaia.core.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class InvestmentWithdrawRequestDTO {

    @NotNull
    @Min(1)
    private BigDecimal amount;

    @NotNull
    private Destination destination;

    @NotNull
    private UUID destinationId;

    public enum Destination {
        PURCHASE
    }
}
