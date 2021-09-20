package com.kovospace.paster.user.controllerHelpers;

import com.kovospace.paster.base.dtos.ErrorResponseDTO;
import com.kovospace.paster.base.dtos.ErrorsResponseDTO;
import com.kovospace.paster.user.controllers.UserController;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
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
  public ErrorsResponseDTO someFieldMissing(MethodArgumentNotValidException ex) {
    Map<String, String> errorMessages = ex.getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            v -> v.getDefaultMessage() == null ? "" : v.getDefaultMessage(),
            (e1, e2) -> e1 + " " + e2
        ));
    System.out.println(errorMessages);
    return new ErrorsResponseDTO(errorMessages);
  }

}
