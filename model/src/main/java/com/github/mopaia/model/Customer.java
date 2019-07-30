package com.github.mopaia.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Customer {

    private UUID id;
    private String name;
    private String email;
    private String phone;

}
