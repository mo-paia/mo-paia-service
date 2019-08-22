package com.github.mopaia.core.dao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentWithdraw {

    private UUID id;
    private UUID investmentId;
    private BigDecimal amount;
    private String destination;
    private UUID destinationId;
    private Instant createdAt;
}
