package com.banking.loan.dto.request;

import com.banking.loan.annotation.ValidInstallmentsCount;
import com.banking.loan.annotation.ValidInterestRate;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanRequestDto {
    private Long customerId;
    private BigDecimal amount;
    @ValidInterestRate
    private double interestRate;
    @ValidInstallmentsCount
    private int numberOfInstallments;
}
