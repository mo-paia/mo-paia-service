package com.github.mopaia.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/investments")
public class InvestmentsController {

    @GetMapping
    public ResponseEntity<String> getAvailableInvestments() {
        return ResponseEntity.ok("MoPaia");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getInvestmentDetails(@PathVariable("id") UUID investmentId) {
        return ResponseEntity.ok("detalhes das entradas e saidas do investimento " + investmentId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> putMoneyOnInvestment(@PathVariable("id") UUID investmentId) {
        return ResponseEntity.ok("Investido em " + investmentId);
    }
}
