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
@RequestMapping("/purchases")
public class PurchasesController {

    @GetMapping
    public ResponseEntity<String> getAllPurchases() {
        return ResponseEntity.ok("all purchases");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getPurchaseDetails(@PathVariable("purchaseId") UUID purchaseId) {
        return ResponseEntity.ok("details of purchase " + purchaseId);
    }

    @PostMapping
    public ResponseEntity<String> createPurchase() {
        return ResponseEntity.ok("Ok");
    }

    @GetMapping("/items:unassigned")
    public ResponseEntity<String> getAvailableItems() {
        return ResponseEntity.ok("Available items (unassigned from stock)");
    }

    @PutMapping("/{purchaseId}/items/{itemId}:assign/stock/{stockId}")
    public ResponseEntity<String> assignItemToStock(@PathVariable("purchaseId") UUID purchaseId,
                                                    @PathVariable("itemId") UUID itemId,
                                                    @PathVariable("stockId") UUID stockId) {
        return ResponseEntity.ok("purchase id " + purchaseId + " item id " + itemId + " to stock id " + stockId);
    }
}
