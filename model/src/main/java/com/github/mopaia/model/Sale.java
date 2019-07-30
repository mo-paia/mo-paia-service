package com.github.mopaia.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Sale {

    private UUID id;
    private Customer customer;
    private Payment payment;

    @JsonManagedReference
    private List<SaleItem> items;

}
