package com.github.mopaia.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import java.util.UUID;

@Data
public class SaleItem {

    private UUID id;

    @JsonBackReference
    private Sale sale;
    private StockItem stockItem;
    private Integer quantity;

}
