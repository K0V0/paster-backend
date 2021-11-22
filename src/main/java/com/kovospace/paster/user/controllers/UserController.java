package com.kovospace.paster.user.controllers;

import com.kovospace.paster.base.controllers.BaseController;
import com.kovospace.paster.user.controllerHelpers.UserControllerResponder;
import com.kovospace.paster.user.dtos.UserLoginRequestDTO;
import com.kovospace.paster.user.dtos.UserLoginResponseDTO;
import com.kovospace.paster.user.dtos.UserRegisterRequestDTO;
import com.kovospace.paster.user.exceptions.UserException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends BaseController {

  private UserControllerResponder responder;

  @Autowired
  public UserController(UserControllerResponder responder) {
    this.responder = responder;
  }

  @PostMapping("/register")
  public ResponseEntity<UserLoginResponseDTO> register(@Valid @RequestBody UserRegisterRequestDTO user)
      throws UserException {
    return responder.register(user);
  }

  @PostMapping("/login")
  public ResponseEntity<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO user)
      throws UserException {
    return responder.login(user);
  }

}
