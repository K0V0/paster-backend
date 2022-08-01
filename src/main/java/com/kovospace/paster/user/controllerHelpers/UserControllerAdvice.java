package com.kovospace.paster.user.controllerHelpers;

import com.kovospace.paster.base.controllerHelpers.ControllerAdvice;
import com.kovospace.paster.base.dtos.ErrorResponseDTO;
import com.kovospace.paster.base.services.StringsService;
import com.kovospace.paster.user.controllers.UserController;
import com.kovospace.paster.user.exceptions.UserLoginBadCredentialsException;
import com.kovospace.paster.user.exceptions.UserNotFoundException;
import com.kovospace.paster.user.exceptions.UserProfileNothingUpdatedException;
import com.kovospace.paster.user.exceptions.UserRegisterAlreadyOccupiedException;
import com.kovospace.paster.user.exceptions.UserRegisterPasswordsNotMatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = {UserController.class})
public class UserControllerAdvice extends ControllerAdvice {

  @Autowired
  protected UserControllerAdvice(StringsService stringsService) {
    super(stringsService);
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(UserRegisterPasswordsNotMatchException.class)
  public ErrorResponseDTO passwordConfirmDidNotMatch(UserRegisterPasswordsNotMatchException e) {
    return stringsService.getErrorResponseDTO(e.getMessage());
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(UserRegisterAlreadyOccupiedException.class)
  public ErrorResponseDTO usernameAlreadyTaken(UserRegisterAlreadyOccupiedException e) {
    return stringsService.getErrorResponseDTO(e.getMessage());
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(UserLoginBadCredentialsException.class)
  public ErrorResponseDTO wrongUserOrPass() {
    return stringsService.getErrorResponseDTO("user.login.credentials.wrong");
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(UserNotFoundException.class)
  public ErrorResponseDTO illegalAccess(UserNotFoundException e) {
    return stringsService.getErrorResponseDTO(e.getMessage());
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ExceptionHandler(UserProfileNothingUpdatedException.class)
  public ErrorResponseDTO nothingChanged(UserNotFoundException e) {
    return stringsService.getErrorResponseDTO(e.getMessage());
  }

}
