package com.kovospace.paster.item.validators.v2.fileItemInitiateRequestDTOvalidator;

import com.kovospace.paster.item.dtos.v2.FileItemInitiateRequestDTO;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoChunkParametersOnContinueRequestValidatorImpl implements ConstraintValidator<NoChunkParametersOnContinueRequestValidator, FileItemInitiateRequestDTO> {

    @Override
    public boolean isValid(FileItemInitiateRequestDTO value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        Object chunksCount = beanWrapper.getPropertyValue("chunksCount");
        Object chunkSize = beanWrapper.getPropertyValue("chunkSize");
        Object fileId = beanWrapper.getPropertyValue("fileId");
        Object itemId = beanWrapper.getPropertyValue("itemId");

        if (fileId == null && itemId == null) {
            /** skip validation, is initialization request */
            return true;
        }

        return (chunksCount == null && chunkSize == null) && (fileId != null && itemId != null);
    }
}
