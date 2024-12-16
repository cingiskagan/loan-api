package com.banking.loan.annotation;

import com.banking.loan.validator.InstallmentsCountValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InstallmentsCountValidator.class)
public @interface ValidInstallmentsCount {
    String message() default "Invalid number of installments, must be 6, 9, 12, or 24";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

