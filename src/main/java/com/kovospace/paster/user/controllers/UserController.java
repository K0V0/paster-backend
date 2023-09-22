package com.kovospace.paster.user.controllers;

import com.kovospace.paster.base.annotations.swagger.PublicEndpoint;
import com.kovospace.paster.base.controllers.BaseController;
import com.kovospace.paster.base.exceptions.FeatureNotImplementedException;
import com.kovospace.paster.user.controllerHelpers.UserControllerResponder;
import com.kovospace.paster.user.dtos.UserInfoChangeRequestDTO;
import com.kovospace.paster.user.dtos.UserLoginRequestDTO;
import com.kovospace.paster.user.dtos.UserLoginResponseDTO;
import com.kovospace.paster.user.dtos.UserPassChangeRequestDTO;
import com.kovospace.paster.user.dtos.UserRegisterRequestDTO;
import com.kovospace.paster.user.dtos.UserRemoveRequestDTO;
import com.kovospace.paster.user.exceptions.UserException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/user")
public class UserController extends BaseController {

  private final UserControllerResponder responder;

  @Autowired
  public UserController(UserControllerResponder responder) {
    this.responder = responder;
  }

  @PostMapping("/register")
  public ResponseEntity<UserLoginResponseDTO> register(@Valid @RequestBody UserRegisterRequestDTO user)
  throws UserException
  {
    return responder.register(user);
  }

  @PublicEndpoint
  @PostMapping("/login")
  @Operation(
          summary = "Login user",
          description = "Logs in a user with the provided credentials.")
  @ApiResponse(
          responseCode = "200",
          description = "User logged in successfully")
  @ApiResponse(
          responseCode = "400",
          description = "Bad request")
  @ApiResponse(
          responseCode = "401",
          description = "Unauthorized")
  public ResponseEntity<UserLoginResponseDTO> login(

          @Valid @RequestBody
          final UserLoginRequestDTO user

  )
          throws UserException
  {
    return responder.login(user);
  }

  @PatchMapping("/login")
  public ResponseEntity<?> passChange(@Valid @RequestBody UserPassChangeRequestDTO passChange)
  throws FeatureNotImplementedException
  {
    throw new FeatureNotImplementedException();
  }

  //TODO zmazanie uctu potvrdzovat e-mailom
  @DeleteMapping("/login")
  public ResponseEntity<?> remove(@Valid @RequestBody UserRemoveRequestDTO userRemove)
  throws FeatureNotImplementedException
  {
    throw new FeatureNotImplementedException();
  }

  @PatchMapping("/user")
  public ResponseEntity<?> remove(@Valid @RequestBody UserInfoChangeRequestDTO details)
  throws FeatureNotImplementedException
  {
    throw new FeatureNotImplementedException();
  }

}
