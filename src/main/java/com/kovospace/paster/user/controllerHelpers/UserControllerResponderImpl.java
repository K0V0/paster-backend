package com.kovospace.paster.user.controllerHelpers;

import com.kovospace.paster.user.dtos.UserLoginRequestDTO;
import com.kovospace.paster.user.dtos.UserLoginResponseDTO;
import com.kovospace.paster.user.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
  public ResponseEntity<UserLoginResponseDTO> login(UserLoginRequestDTO dto) {
    return ResponseEntity.ok(
        modelMapper.map(
          userService.login(dto.getName(), dto.getPass()),
          UserLoginResponseDTO.class
      )
    );
  }
}
