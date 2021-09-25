package com.kovospace.paster.user.controllerHelpers;

import com.kovospace.paster.user.dtos.UserLoginRequestDTO;
import com.kovospace.paster.user.dtos.UserLoginResponseDTO;
import com.kovospace.paster.user.dtos.UserRegisterRequestDTO;
import com.kovospace.paster.user.exceptions.UserException;
import com.kovospace.paster.user.exceptions.UserLoginBadCredentialsException;
import com.kovospace.paster.user.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserControllerResponderImpl implements UserControllerResponder {

  private UserService userService;
  private ModelMapper modelMapper;

  @Autowired
  public UserControllerResponderImpl(UserService userService, ModelMapper modelMapper) {
    this.userService = userService;
    this.modelMapper = modelMapper;
  }

  @Override
  public ResponseEntity<UserLoginResponseDTO> login(UserLoginRequestDTO dto)
      throws UserException {
    return ResponseEntity.ok(
        modelMapper.map(
          userService.login(dto.getName(), dto.getPass()),
          UserLoginResponseDTO.class
      )
    );
  }

  @Override
  public ResponseEntity<UserLoginResponseDTO> register(UserRegisterRequestDTO dto)
      throws UserException {
    return new ResponseEntity<>(
        modelMapper.map(
            userService.register(dto.getName(), dto.getPass(), dto.getPass2()),
            UserLoginResponseDTO.class
        ),
        HttpStatus.CREATED
    );
  }
}
