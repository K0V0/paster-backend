package com.kovospace.paster.item.controllerHelpers;

import com.kovospace.paster.base.controllerHelpers.ControllerAdvice;
import com.kovospace.paster.base.dtos.ErrorResponseDTO;
import com.kovospace.paster.item.exceptions.ItemNotFoundException;
import com.kovospace.paster.item.exceptions.UserNotFoundException;
import com.kovospace.paster.user.exceptions.UserRegisterAlreadyOccupiedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ItemControllerAdvice extends ControllerAdvice {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponseDTO noUserOrAccess(UserRegisterAlreadyOccupiedException e) {
        return new ErrorResponseDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ItemNotFoundException.class)
    public ErrorResponseDTO noItemForUser(UserRegisterAlreadyOccupiedException e) {
        return new ErrorResponseDTO(e.getMessage());
    }

}
