package com.kovospace.paster.item.controllerHelpers;

import com.kovospace.paster.base.controllerHelpers.ControllerAdvice;
import com.kovospace.paster.base.dtos.ErrorResponseDTO;
import com.kovospace.paster.base.services.StringsService;
import com.kovospace.paster.item.exceptions.ItemNotFoundException;
import com.kovospace.paster.item.exceptions.ItemNotOwnedByUserException;
import com.kovospace.paster.item.exceptions.NoItemToDeleteException;
import com.kovospace.paster.item.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ItemControllerAdvice extends ControllerAdvice {

    @Autowired
    protected ItemControllerAdvice(StringsService stringsService) {
        super(stringsService);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponseDTO noUserOrAccess(UserNotFoundException e) {
        return stringsService.getErrorResponseDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ItemNotFoundException.class)
    public ErrorResponseDTO noItemForUser(ItemNotFoundException e) {
        return stringsService.getErrorResponseDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ItemNotOwnedByUserException.class)
    public ErrorResponseDTO notOwnedByUser(ItemNotOwnedByUserException e) {
        return stringsService.getErrorResponseDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(NoItemToDeleteException.class)
    public void alreadyDeleted() {}

}
