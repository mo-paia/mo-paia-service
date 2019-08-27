package com.github.mopaia.core.controller;

import com.github.mopaia.core.controller.dto.PurchaseExpenseCreateRequestDTO;
import com.github.mopaia.core.controller.dto.PurchaseExpenseUpdateRequestDTO;
import com.github.mopaia.core.dao.PurchaseExpensesRepository;
import com.github.mopaia.core.dao.model.Purchase;
import com.github.mopaia.core.dao.model.PurchaseExpense;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseExpenseController {

    private final PurchaseExpensesRepository purchaseExpensesRepository;

    @GetMapping("/expenses/types")
    public ResponseEntity<List<PurchaseExpense.Type>> getTypes() {
        return ResponseEntity.ok(Arrays.asList(PurchaseExpense.Type.values()));
    }

    @GetMapping("/{id}/expenses")
    public ResponseEntity<List<PurchaseExpense>> getExtraExpensesFromPurchase(@PathVariable("id") UUID purchaseId) {
        return ResponseEntity.ok(purchaseExpensesRepository.listByPurchaseId(purchaseId));
    }

    @PostMapping("/{id}/expenses")
    public ResponseEntity<Purchase> createExtraExpensesOnPurchase(@PathVariable("id") UUID purchaseId,
                                                                  @RequestBody @Valid PurchaseExpenseCreateRequestDTO purchaseExpenseCreateRequestDTO) {
        return purchaseExpensesRepository.createOnPurchase(purchaseId, purchaseExpenseCreateRequestDTO.getType(),
                purchaseExpenseCreateRequestDTO.getQuantity(), purchaseExpenseCreateRequestDTO.getUnitPrice())
                .map(purchaseExpense -> ResponseEntity.created(URI.create(String.format("/purchases/%s/expenses/%s", purchaseId, purchaseExpense.getId()))))
                .orElseGet(ResponseEntity::badRequest)
                .build();
    }

    @PutMapping("/{id}/expenses/{expenseId}")
    public ResponseEntity<PurchaseExpense> updateExtraExpensesOnPurchase(@PathVariable("id") UUID purchaseId,
                                                                         @PathVariable("expenseId") UUID expenseId,
                                                                         @RequestBody PurchaseExpenseUpdateRequestDTO purchaseExpenseUpdateRequestDTO) {
        return purchaseExpensesRepository.update(purchaseId, expenseId, purchaseExpenseUpdateRequestDTO.getQuantity(), purchaseExpenseUpdateRequestDTO.getUnitPrice())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}/expenses/{expenseId}")
    public ResponseEntity<Object> deleteExtraExpensesOnPurchase(@PathVariable("id") UUID purchaseId,
                                                                @PathVariable("expenseId") UUID expenseId) {
        return purchaseExpensesRepository.delete(purchaseId, expenseId)
                .map((UUID t) -> ResponseEntity.noContent().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
