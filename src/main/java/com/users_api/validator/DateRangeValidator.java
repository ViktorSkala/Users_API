package com.users_api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateRangeValidator implements ConstraintValidator<DateRange, String> {

    @Override
    public boolean isValid(String dateRangeString, ConstraintValidatorContext constraintValidatorContext) {
        if (dateRangeString == null) {
            return true;
        }
        try {
            String[] dates = dateRangeString.split(",");
            LocalDate from = LocalDate.parse(dates[0], DateTimeFormatter.ISO_DATE);
            LocalDate to = LocalDate.parse(dates[1], DateTimeFormatter.ISO_DATE);
            return from.isBefore(to);
        } catch (Exception e) {
            return false;
        }
    }
}
