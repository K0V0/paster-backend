package com.kovospace.paster.base.validators.platform;

import com.kovospace.paster.base.validators.platform.PlatformStringValidatorImpl;
import com.kovospace.paster.item.dtos.PlatformEnum;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PlatformStringValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@ReportAsSingleViolation
public @interface PlatformValidator {

    Class<? extends Enum<PlatformEnum>> enumClazz();

    String message() default "item.request.platform.wrong";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}