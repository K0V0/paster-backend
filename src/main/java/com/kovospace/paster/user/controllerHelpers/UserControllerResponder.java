package com.kovospace.paster.user.controllerHelpers;

import com.kovospace.paster.user.dtos.UserLoginRequestDTO;
import com.kovospace.paster.user.dtos.UserLoginResponseDTO;
import com.kovospace.paster.user.dtos.UserRegisterRequestDTO;
import com.kovospace.paster.user.exceptions.UserException;
import com.kovospace.paster.user.exceptions.UserLoginBadCredentialsException;
import org.springframework.http.ResponseEntity;

public interface UserControllerResponder {

  ResponseEntity<UserLoginResponseDTO> login(UserLoginRequestDTO dto)
      throws UserLoginBadCredentialsException, UserException;

  ResponseEntity<UserLoginResponseDTO> register(UserRegisterRequestDTO dto) throws UserException;

}
