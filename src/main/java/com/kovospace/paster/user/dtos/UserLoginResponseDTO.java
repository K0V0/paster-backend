package com.kovospace.paster.user.dtos;

import com.kovospace.paster.base.dtos.OkResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginResponseDTO extends OkResponseDTO {

  private String user;
  private String jwtToken;

  public UserLoginResponseDTO() { super(); }

}
