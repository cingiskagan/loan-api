package com.banking.loan.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentResultDto {
    private int installmentsPaid;
    private BigDecimal totalPaid;
    private boolean isLoanFullyPaid;

    public PaymentResultDto(int installmentsPaid, BigDecimal totalPaid, boolean isLoanFullyPaid) {
        this.installmentsPaid = installmentsPaid;
        this.totalPaid = totalPaid;
        this.isLoanFullyPaid = isLoanFullyPaid;
    }

}
