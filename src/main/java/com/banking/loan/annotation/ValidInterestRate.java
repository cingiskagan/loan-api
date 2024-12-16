package com.banking.loan.annotation;

import com.banking.loan.validator.InterestRateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InterestRateValidator.class)
public @interface ValidInterestRate {
    String message() default "Interest rate must be between 0.1 and 0.5";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
