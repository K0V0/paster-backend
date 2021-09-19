package com.kovospace.paster.user.controllers;

import com.kovospace.paster.user.controllerHelpers.UserControllerResponder;
import com.kovospace.paster.user.dtos.UserLoginRequestDTO;
import com.kovospace.paster.user.dtos.UserLoginResponseDTO;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  private UserControllerResponder responder;

  @Autowired
  public UserController(UserControllerResponder responder) {
    this.responder = responder;
  }

  @PostMapping("/login")
  public ResponseEntity<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO user) {
    return responder.login(user);
  }

}
