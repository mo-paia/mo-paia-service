package com.github.mopaia.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PurchaseItem {

    private UUID id;

    @JsonBackReference
    private Purchase purchase;
    private Product product;
    private Integer quantity;
    private BigDecimal purchasePrice;

}
