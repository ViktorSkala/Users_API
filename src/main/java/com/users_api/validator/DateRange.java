package com.users_api.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
@Documented
public @interface DateRange {

    String message() default "date of birth range should be in format: 2020-01-30,2020-12-31";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
