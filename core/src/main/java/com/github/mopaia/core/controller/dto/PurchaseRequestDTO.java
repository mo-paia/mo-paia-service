package com.github.mopaia.core.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
public class PurchaseRequestDTO {

    @NotNull
    private String provider;
    private String providerUrl;
    private String providerOrderId;
    private String trackingCode;
    private Instant arrivedAt;

}
