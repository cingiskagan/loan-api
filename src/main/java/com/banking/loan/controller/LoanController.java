package com.banking.loan.controller;

import com.banking.loan.dto.request.LoanRequestDto;
import com.banking.loan.dto.request.PaymentRequestDto;
import com.banking.loan.dto.response.LoanDto;
import com.banking.loan.dto.response.LoanInstallmentDto;
import com.banking.loan.dto.response.PaymentResultDto;
import com.banking.loan.model.Loan;
import com.banking.loan.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a new loan",
            description = "Creates a loan for a customer with the specified amount, interest rate, and number of installments. "
                    + "It ensures the customer has enough credit limit, validates the number of installments (6, 9, 12, or 24), "
                    + "validates the interest rate (0.1-0.5) and calculates the total amount as `amount * (1 + interest rate)`.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or insufficient credit limit")
    })
    @PostMapping
    public ResponseEntity<?> createLoan(@Valid @RequestBody LoanRequestDto loanRequest) {
        Loan loan = loanService.createLoan(loanRequest);
        return ResponseEntity.ok(Map.of("message", "Loan created successfully", "loanId", loan.getId()));
    }

    @Operation(summary = "List loans for a customer",
            description = "Lists all loans for a given customer. Allows optional filtering by payment status (`isPaid`).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of loans retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<List<LoanDto>> listLoans(
            @Parameter(description = "ID of the customer to retrieve loans for") @PathVariable Long customerId,
            @Parameter(description = "Optional: Filter by payment status") @RequestParam(required = false) Boolean isPaid) {
        List<LoanDto> loans = loanService.listLoans(customerId, isPaid)
                .stream()
                .map(LoanDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(loans);
    }

    @Operation(summary = "List installments for a loan",
            description = "Retrieves the list of installments for a specific loan.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of installments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    @GetMapping("/{loanId}/installments")
    public ResponseEntity<List<LoanInstallmentDto>> listInstallments(
            @Parameter(description = "ID of the loan to retrieve installments for") @PathVariable Long loanId) {
        List<LoanInstallmentDto> installments = loanService.listInstallments(loanId)
                .stream()
                .map(LoanInstallmentDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(installments);
    }

    @Operation(summary = "Pay installments for a loan",
            description = "Pays loan installments based on the specified amount. The payment logic ensures:"
                    + "\n- Installments are paid wholly or not at all."
                    + "\n- Payments start from the earliest unpaid installment."
                    + "\n- Payments cannot be made for installments with a due date more than 3 months in the future.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Installments paid successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment amount or other business rule violations")
    })
    @PostMapping("/{loanId}/pay")
    public ResponseEntity<PaymentResultDto> payLoan(
            @Parameter(description = "ID of the loan to make payment for") @PathVariable Long loanId,
            @Parameter(description = "Payment amount for the loan installments") @RequestBody PaymentRequestDto paymentRequest) {
        PaymentResultDto result = loanService.payLoan(loanId, paymentRequest.getAmount());
        return ResponseEntity.ok(result);
    }
}
