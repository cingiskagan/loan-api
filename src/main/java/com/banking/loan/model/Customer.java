package com.banking.loan.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String surname;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal creditLimit;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal usedCreditLimit;

    public Customer(String name, String surname, BigDecimal creditLimit, BigDecimal usedCreditLimit) {
        this.name = name;
        this.surname = surname;
        this.creditLimit = creditLimit;
        this.usedCreditLimit = usedCreditLimit;
    }
}
