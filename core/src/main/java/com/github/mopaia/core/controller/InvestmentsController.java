package com.github.mopaia.core.controller;

import com.github.mopaia.core.controller.dto.InvestmentDepositRequestDTO;
import com.github.mopaia.core.dao.InvestmentRepository;
import com.github.mopaia.core.dao.model.Investment;
import com.github.mopaia.core.dao.model.InvestmentDeposit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/investments")
@RequiredArgsConstructor
public class InvestmentsController {

    private final InvestmentRepository investmentRepository;

    @GetMapping
    public ResponseEntity<List<Investment>> getAvailableInvestments() {
        return ResponseEntity.ok(investmentRepository.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Investment> getInvestmentDetails(@PathVariable("id") UUID investmentId) {
        return investmentRepository.getById(investmentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/deposits")
    public ResponseEntity<List<InvestmentDeposit>> getInvestmentDeposits(@PathVariable("id") UUID investmentId) {
        return ResponseEntity.ok(investmentRepository.listDeposits(investmentId));
    }

    @PostMapping("/{id}/deposits")
    public ResponseEntity<Object> putMoneyOnInvestment(@PathVariable("id") UUID investmentId, @RequestBody @Valid InvestmentDepositRequestDTO investmentDepositRequestDTO) {
        return investmentRepository.deposit(investmentId, investmentDepositRequestDTO.getInvestorName(), investmentDepositRequestDTO.getAmount())
                .map(deposit -> ResponseEntity.created(URI.create(String.format("/investments/%s/deposits/%s", investmentId, deposit.getId()))))
                .orElseGet(ResponseEntity::badRequest)
                .build();
    }
}
