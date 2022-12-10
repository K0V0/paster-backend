package com.kovospace.paster.base.validators;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class FieldsNullOrFilledImpl implements ConstraintValidator<FieldsSameStateValidator, Object> {

    private String[] fields;

    @Override
    public void initialize(FieldsSameStateValidator constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        AtomicReference<Boolean> isNull = new AtomicReference<Boolean>(null);
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);

        return Arrays.stream(fields).allMatch(field -> {
            boolean isFieldNull = beanWrapper.getPropertyValue(field) == null;
            if (isNull.get() == null) isNull.set(isFieldNull);
            return isNull.get() == isFieldNull;
        });
    }
}
