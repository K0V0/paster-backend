package com.kovospace.paster.item.controllerHelpers.v2;

import com.kovospace.paster.base.controllerHelpers.ControllerAdvice;
import com.kovospace.paster.base.dtos.ErrorResponseDTO;
import com.kovospace.paster.base.services.StringsService;
import com.kovospace.paster.item.exceptions.v2.FileNotCompleteException;
import com.kovospace.paster.item.exceptions.v2.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FileItemControllerAdvice extends ControllerAdvice {
    @Autowired
    protected FileItemControllerAdvice(StringsService stringsService) {
        super(stringsService);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(FileNotCompleteException.class)
    public ErrorResponseDTO fileNotCompletedYet(FileNotCompleteException e) {
        return stringsService.getErrorResponseDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FileNotFoundException.class)
    public ErrorResponseDTO fileNotFound(FileNotFoundException e) {
        return stringsService.getErrorResponseDTO(e.getMessage());
    }

}
