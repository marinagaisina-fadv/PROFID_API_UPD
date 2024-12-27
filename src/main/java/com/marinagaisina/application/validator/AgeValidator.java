package com.marinagaisina.application.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class AgeValidator implements ConstraintValidator<Age, LocalDate> {

    private long minAge;
    private long maxAge;

    @Override
    public void initialize(Age age) {
        this.minAge = age.min();
        this.maxAge = age.max();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.nonNull(localDate) &&
                isGreaterThan(localDate) &&
                isLessThan(localDate);
    }

    private boolean isLessThan(LocalDate localDate) {
        return ChronoUnit.YEARS.between(localDate, LocalDate.now()) < maxAge;
    }

    private boolean isGreaterThan(LocalDate localDate) {
        return ChronoUnit.YEARS.between(localDate, LocalDate.now()) >= minAge;
    }
}
