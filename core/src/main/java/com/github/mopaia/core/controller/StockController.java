package com.github.mopaia.core.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @GetMapping
    public ResponseEntity<String> getAvailableStocks() {
        return ResponseEntity.ok("list stocks");
    }

    @PostMapping
    public ResponseEntity<String> createStock() {
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> stockDetails(@PathVariable("id") UUID stockId) {
        return ResponseEntity.ok("Details of stock " + stockId);
    }

    @PutMapping("/{stockId}/items/{itemId}:breakdown")
    public ResponseEntity<String> breakdownItem(@PathVariable("stockId") UUID stockId,
                                                @PathVariable("itemId") UUID itemId) {
        return ResponseEntity.ok("Breakdown item " + itemId + " from stock " + stockId + " into other products");
    }
}
