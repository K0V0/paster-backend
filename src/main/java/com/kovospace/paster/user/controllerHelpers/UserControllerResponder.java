package com.kovospace.paster.user.controllerHelpers;

import com.kovospace.paster.user.dtos.UserLoginRequestDTO;
import com.kovospace.paster.user.dtos.UserLoginResponseDTO;
import org.springframework.http.ResponseEntity;

public interface UserControllerResponder {

  ResponseEntity<UserLoginResponseDTO> login(UserLoginRequestDTO dto);

}
