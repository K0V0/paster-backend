package com.kovospace.paster.user.controllerHelpers;

import com.kovospace.paster.base.controllerHelpers.ControllerAdvice;
import com.kovospace.paster.base.dtos.ErrorResponseDTO;
import com.kovospace.paster.user.controllers.UserController;
import com.kovospace.paster.user.exceptions.UserLoginBadCredentialsException;
import com.kovospace.paster.user.exceptions.UserRegisterAlreadyOccupiedException;
import com.kovospace.paster.user.exceptions.UserRegisterPasswordsNotMatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = {UserController.class})
public class UserControllerAdvice extends ControllerAdvice {

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(UserRegisterPasswordsNotMatchException.class)
  public ErrorResponseDTO passwordConfirmDidNotMatch(UserRegisterPasswordsNotMatchException e) {
    return new ErrorResponseDTO(e.getMessage());
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(UserRegisterAlreadyOccupiedException.class)
  public ErrorResponseDTO usernameAlreadyTaken(UserRegisterAlreadyOccupiedException e) {
    return new ErrorResponseDTO(e.getMessage());
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(UserLoginBadCredentialsException.class)
  public ErrorResponseDTO wrongUserOrPass() {
    return new ErrorResponseDTO("Wrong username or password.");
  }

}
