package com.banking.loan.validator;

import com.banking.loan.annotation.ValidInterestRate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InterestRateValidator implements ConstraintValidator<ValidInterestRate, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return value != null && value >= 0.1 && value <= 0.5;
    }
}
