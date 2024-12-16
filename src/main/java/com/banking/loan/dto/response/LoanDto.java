package com.banking.loan.dto.response;

import com.banking.loan.model.Loan;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class LoanDto {
    private Long id;
    private BigDecimal loanAmount;
    private int numberOfInstallment;

    public LoanDto(Loan loan) {
        this.id = loan.getId();
        this.loanAmount = loan.getLoanAmount();
        this.numberOfInstallment = loan.getNumberOfInstallment();
    }
}
