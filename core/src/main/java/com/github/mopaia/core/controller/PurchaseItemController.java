package com.github.mopaia.core.controller;

import com.github.mopaia.core.controller.dto.PurchaseItemCreateRequestDTO;
import com.github.mopaia.core.controller.dto.PurchaseItemUpdateRequestDTO;
import com.github.mopaia.core.dao.PurchaseItemRepository;
import com.github.mopaia.core.dao.dto.PurchaseItemDTO;
import com.github.mopaia.core.dao.model.Purchase;
import com.github.mopaia.core.dao.model.PurchaseItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseItemController {

    private final PurchaseItemRepository purchaseItemRepository;

    @GetMapping("/{id}/items")
    public ResponseEntity<List<PurchaseItemDTO>> getItemsFromPurchase(@PathVariable("id") UUID purchaseId) {
        return ResponseEntity.ok(purchaseItemRepository.listByPurchaseId(purchaseId));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<Purchase> createItemOnPurchase(@PathVariable("id") UUID purchaseId, @RequestBody @Valid PurchaseItemCreateRequestDTO purchaseItemCreateRequestDTO) {
        return purchaseItemRepository.createOnPurchase(purchaseId, purchaseItemCreateRequestDTO.getProductId(), purchaseItemCreateRequestDTO.getQuantity(), purchaseItemCreateRequestDTO.getUnitPrice())
                .map(purchaseItem -> ResponseEntity.created(URI.create(String.format("/purchases/%s/items/%s", purchaseId, purchaseItem.getId()))))
                .orElseGet(ResponseEntity::badRequest)
                .build();
    }

    @PutMapping("/{id}/items/{productItemId}")
    public ResponseEntity<PurchaseItem> updateItemOnPurchase(@PathVariable("id") UUID purchaseId, @PathVariable("productItemId") UUID productItemId, @RequestBody PurchaseItemUpdateRequestDTO purchaseItemUpdateRequestDTO) {
        return purchaseItemRepository.update(purchaseId, productItemId, purchaseItemUpdateRequestDTO.getQuantity(), purchaseItemUpdateRequestDTO.getUnitPrice())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}/items/{productItemId}")
    public ResponseEntity<Object> deleteItemOnPurchase(@PathVariable("id") UUID purchaseId, @PathVariable("productItemId") UUID productItemId) {
        return purchaseItemRepository.delete(purchaseId, productItemId)
                .map((UUID t) -> ResponseEntity.noContent().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
