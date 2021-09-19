package com.kovospace.paster.user.controllerHelpers;

import com.kovospace.paster.base.dtos.ErrorResponseDTO;
import com.kovospace.paster.user.controllers.UserController;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = {UserController.class})
public class UserControllerAdvice {

  @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ErrorResponseDTO requestBodyMissing() {
    return new ErrorResponseDTO("Request body malformed or missing.");
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponseDTO someFieldMissing(MethodArgumentNotValidException ex) {
    List<String> errorMessages = ex.getBindingResult().getAllErrors()
        .stream()
        .map(ObjectError::getDefaultMessage)
        .collect(Collectors.toList());
    if (errorMessages.size() > 1
        && errorMessages.contains("Password is required.")
        && errorMessages.contains("Username is required.")) {
      return new ErrorResponseDTO("Username and password are required.");
    } else if (errorMessages.size() > 1
        && errorMessages.contains("Password field is empty.")
        && errorMessages.contains("Username field is empty.")) {
      return new ErrorResponseDTO("Username and password fields are empty.");
    }
    return new ErrorResponseDTO(errorMessages.get(0));
  }

}
