package com.kovospace.paster.base.controllerHelpers;

import com.kovospace.paster.base.dtos.ErrorResponseDTO;
import com.kovospace.paster.user.exceptions.UserLoginBadCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ControllerAdvice {

  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED) // 405
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ErrorResponseDTO wrongMethod() {
    return new ErrorResponseDTO("Wrong HTTP method used.");
  }

  @ResponseStatus(HttpStatus.NOT_FOUND) // 404
  @ExceptionHandler(NoHandlerFoundException.class)
  public ErrorResponseDTO notFoundMethod() {
    return new ErrorResponseDTO("Endpoint not found.");
  }

  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  @ExceptionHandler(UnsupportedMediaTypeStatusException.class)
  public ErrorResponseDTO notSupportedMedia() {
    return new ErrorResponseDTO("Wrong request media type.");
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(UserLoginBadCredentialsException.class)
  public ErrorResponseDTO wrongUserOrPass() {
    return new ErrorResponseDTO("Wrong username or password.");
  }

}
