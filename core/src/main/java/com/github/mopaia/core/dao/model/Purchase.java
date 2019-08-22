package com.github.mopaia.core.dao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {

    private UUID id;
    private String provider;
    private String providerUrl;
    private String providerOrderId;
    private String trackingCode;
    private Instant arrivedAt;
    private Instant createdAt;
}
