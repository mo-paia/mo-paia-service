package com.github.mopaia.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @PostMapping
    public ResponseEntity<String> createSale() {
        return ResponseEntity.ok("sale created");
    }
}
