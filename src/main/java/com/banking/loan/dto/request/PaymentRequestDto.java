package com.banking.loan.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentRequestDto {
    private BigDecimal amount;
}
