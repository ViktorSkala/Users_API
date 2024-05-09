package com.users_api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

@Data
public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

    @Value("${age.limit}")
    private int ageLimit;

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext constraintValidatorContext) {
        if (dateOfBirth == null) {
            return true;
        }
        return dateOfBirth.isBefore(LocalDate.now().minusYears(ageLimit));
    }
}
