package com.banking.loan.controller;

import com.banking.loan.dto.request.LoanRequestDto;
import com.banking.loan.dto.request.PaymentRequestDto;
import com.banking.loan.dto.response.LoanDto;
import com.banking.loan.dto.response.LoanInstallmentDto;
import com.banking.loan.dto.response.PaymentResultDto;
import com.banking.loan.model.Loan;
import com.banking.loan.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping
    public ResponseEntity<?> createLoan(@Valid @RequestBody LoanRequestDto loanRequest) {
        Loan loan = loanService.createLoan(loanRequest);
        return ResponseEntity.ok(Map.of("message", "Loan created successfully", "loanId", loan.getId()));
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<List<LoanDto>> listLoans(@PathVariable Long customerId,
                                                   @RequestParam(required = false) Boolean isPaid) {
        List<LoanDto> loans = loanService.listLoans(customerId, isPaid)
                .stream()
                .map(LoanDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{loanId}/installments")
    public ResponseEntity<List<LoanInstallmentDto>> listInstallments(@PathVariable Long loanId) {
        List<LoanInstallmentDto> installments = loanService.listInstallments(loanId)
                .stream()
                .map(LoanInstallmentDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(installments);
    }

    @PostMapping("/{loanId}/pay")
    public ResponseEntity<PaymentResultDto> payLoan(@PathVariable Long loanId, @RequestBody PaymentRequestDto paymentRequest) {
        PaymentResultDto result = loanService.payLoan(loanId, paymentRequest.getAmount());
        return ResponseEntity.ok(result);
    }
}

