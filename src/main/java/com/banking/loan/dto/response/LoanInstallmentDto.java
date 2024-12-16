package com.banking.loan.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class LoanInstallmentDto {
    private Long id;
    private BigDecimal amount;
    private BigDecimal paidAmount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private boolean isPaid;

    // Constructor to map LoanInstallment entity to DTO
    public LoanInstallmentDto(com.banking.loan.model.LoanInstallment installment) {
        this.id = installment.getId();
        this.amount = installment.getAmount();
        this.paidAmount = installment.getPaidAmount();
        this.dueDate = installment.getDueDate();
        this.paymentDate = installment.getPaymentDate();
        this.isPaid = installment.getIsPaid();
    }
}
