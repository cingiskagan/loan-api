package com.banking.loan.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CustomerDto {
    private Long id;
    private String name;
    private String surname;
    private BigDecimal creditLimit;
    private BigDecimal usedCreditLimit;

    // Constructor to map Customer entity to DTO
    public CustomerDto(com.banking.loan.model.Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.surname = customer.getSurname();
        this.creditLimit = customer.getCreditLimit();
        this.usedCreditLimit = customer.getUsedCreditLimit();
    }
}
