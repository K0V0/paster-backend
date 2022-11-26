package com.kovospace.paster.item.validators.v2.fileItemInitiateRequestDTOvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = NoChunkParametersOnContinueRequestValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ReportAsSingleViolation
public @interface NoChunkParametersOnContinueRequestValidator {

    String message() default "general.request.fields.notSameState";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

}
