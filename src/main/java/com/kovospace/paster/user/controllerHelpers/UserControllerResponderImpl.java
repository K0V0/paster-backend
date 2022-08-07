package com.kovospace.paster.user.controllerHelpers;

import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.user.dtos.UserLoginRequestDTO;
import com.kovospace.paster.user.dtos.UserLoginResponseDTO;
import com.kovospace.paster.user.dtos.UserProfileRequestDTO;
import com.kovospace.paster.user.dtos.UserProfileResponseDTO;
import com.kovospace.paster.user.dtos.UserRegisterRequestDTO;
import com.kovospace.paster.user.exceptions.UserException;
import com.kovospace.paster.user.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserControllerResponderImpl implements UserControllerResponder {

  private final UserService userService;
  private final ModelMapper modelMapper;
  private final JwtService jwtService;

  @Autowired
  public UserControllerResponderImpl(UserService userService, ModelMapper modelMapper, JwtService jwtService) {
    this.userService = userService;
    this.modelMapper = modelMapper;
    this.jwtService = jwtService;
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
            userService.register(dto.getName(), dto.getPass(), dto.getPass2(), dto.getEmail()),
            UserLoginResponseDTO.class
        ),
        HttpStatus.CREATED
    );
  }

  @Override
  public ResponseEntity<UserProfileResponseDTO> getProfile(String jwtToken)
      throws UserException {
    long userId = jwtService.parse(jwtToken);
    return new ResponseEntity<UserProfileResponseDTO>(
        modelMapper.map(
            userService.getProfile(userId),
            UserProfileResponseDTO.class
        ),
        HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<UserProfileResponseDTO> updateProfile(UserProfileRequestDTO dto)
      throws UserException {
    return new ResponseEntity<>(
        modelMapper.map(
            userService.updateProfile(dto.getId(), dto.getAvatarFileName(), dto.getEmail()),
            UserProfileResponseDTO.class
        ),
        HttpStatus.ACCEPTED
    );
  }
}
