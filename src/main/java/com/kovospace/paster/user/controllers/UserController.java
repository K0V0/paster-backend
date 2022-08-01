package com.kovospace.paster.user.controllers;

import com.kovospace.paster.base.controllers.BaseController;
import com.kovospace.paster.base.exceptions.FeatureNotImplementedException;
import com.kovospace.paster.base.exceptions.WrongArgumentTypeException;
import com.kovospace.paster.user.controllerHelpers.UserControllerResponder;
import com.kovospace.paster.user.dtos.UserInfoChangeRequestDTO;
import com.kovospace.paster.user.dtos.UserLoginRequestDTO;
import com.kovospace.paster.user.dtos.UserLoginResponseDTO;
import com.kovospace.paster.user.dtos.UserPassChangeRequestDTO;
import com.kovospace.paster.user.dtos.UserProfileRequestDTO;
import com.kovospace.paster.user.dtos.UserProfileResponseDTO;
import com.kovospace.paster.user.dtos.UserRegisterRequestDTO;
import com.kovospace.paster.user.dtos.UserRemoveRequestDTO;
import com.kovospace.paster.user.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

  /** registration and login process */

  @PostMapping("/register")
  public ResponseEntity<UserLoginResponseDTO> register(@Valid @RequestBody UserRegisterRequestDTO user)
  throws UserException
  {
    return responder.register(user);
  }

  @PostMapping("/login")
  public ResponseEntity<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO user)
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

  /** user account management */

  @GetMapping("/profile/{id}")
  public ResponseEntity<?> getUserProfile(
          @RequestHeader(value = "Authorization") String token,
          @PathVariable String id
  ) throws UserException, WrongArgumentTypeException {
    try {
      return responder.getProfile(Long.parseLong(id));
    } catch (NumberFormatException e) {
      throw new WrongArgumentTypeException();
    }
  }

  @PatchMapping("/profile")
  public ResponseEntity<UserProfileResponseDTO> updateUserProfile(
          @RequestHeader(value = "Authorization") String token,
          @Valid @RequestBody UserProfileRequestDTO dto
  ) throws UserException {
    return responder.updateProfile(dto);
  }

  //TODO zmazanie uctu potvrdzovat e-mailom
  @DeleteMapping("/login")
  public ResponseEntity<?> remove(@Valid @RequestBody UserRemoveRequestDTO userRemove)
  throws FeatureNotImplementedException
  {
    throw new FeatureNotImplementedException();
  }

}
