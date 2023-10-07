package com.kovospace.paster.user.dtos;

import com.kovospace.paster.base.dtos.OkResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO for user login response")
public class UserLoginResponseDTO extends OkResponseDTO {

  @Schema(description = "JWT token for user authentication")
  private String jwtToken;

  public UserLoginResponseDTO() { super(); }

}
