package com.gabrielluciano.incometaxservice.domain.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidThresholdsValidator.class)
public @interface ValidThresholds {

    String message() default "invalid thresholds";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
