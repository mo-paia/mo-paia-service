package com.github.mopaia.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Purchase {

    private UUID id;
    private String provider;
    private String providerOrderId;
    private String providerOrderUrl;

    @JsonManagedReference
    private List<PurchaseItem> purchaseItems;

}
