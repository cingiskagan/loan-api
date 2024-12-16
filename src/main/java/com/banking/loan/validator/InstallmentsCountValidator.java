package com.banking.loan.validator;

import com.banking.loan.annotation.ValidInstallmentsCount;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InstallmentsCountValidator implements ConstraintValidator<ValidInstallmentsCount, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && (value == 6 || value == 9 || value == 12 || value == 24);
    }
}