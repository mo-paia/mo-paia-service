package com.github.mopaia.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class StockItem {

    private UUID id;

    @JsonBackReference
    private Stock stock;
    private PurchaseItem purchaseItem;
    private Integer quantity;
    private BigDecimal salePrice;

}
