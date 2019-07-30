package com.github.mopaia.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Stock {

    private UUID id;
    private String name;

    @JsonManagedReference
    private List<StockItem> stockItems;
}
