package com.github.mopaia.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Payment {

    private UUID id;

    private Method method;
    private Origin origin;
    private Destination destination;

    private BigDecimal totalValue;

    public enum Method {
        CASH, TED, BOLETO
    }

    public enum Origin {
        CUSTOMER, SAME_AS_DESTINATION
    }

    public enum Destination {
        NUCONTA, IN_HANDS
    }
}
