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
public class PurchaseExpense {

    private UUID id;
    private UUID purchaseId;
    private Type type;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Instant createdAt;

    public enum Type {
        DELIVERY
    }
}
