package com.github.mopaia.core.controller;

import com.github.mopaia.core.controller.dto.PurchaseRequestDTO;
import com.github.mopaia.core.dao.PurchaseRepository;
import com.github.mopaia.core.dao.model.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseRepository purchaseRepository;

    @GetMapping
    public ResponseEntity<List<Purchase>> getAllPurchases() {
        return ResponseEntity.ok(purchaseRepository.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseDetails(@PathVariable("id") UUID purchaseId) {
        return purchaseRepository.getById(purchaseId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        return purchaseRepository.create(purchaseRequestDTO.getProvider(), purchaseRequestDTO.getProviderUrl(),
                purchaseRequestDTO.getProviderOrderId(), purchaseRequestDTO.getTrackingCode(), purchaseRequestDTO.getArrivedAt())
                .map(purchase -> ResponseEntity.created(URI.create(String.format("/purchases/%s", purchase.getId()))))
                .orElseGet(ResponseEntity::badRequest)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Purchase> updatePurchase(@PathVariable("id") UUID purchaseId, @RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        return purchaseRepository.update(purchaseId, purchaseRequestDTO.getProvider(), purchaseRequestDTO.getProviderUrl(),
                purchaseRequestDTO.getProviderOrderId(), purchaseRequestDTO.getTrackingCode(), purchaseRequestDTO.getArrivedAt())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
