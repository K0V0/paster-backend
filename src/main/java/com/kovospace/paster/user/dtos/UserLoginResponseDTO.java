package com.kovospace.paster.user.dtos;

import com.kovospace.paster.base.dtos.OkResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "User (Login) Response DTO")
public class UserLoginResponseDTO extends OkResponseDTO {

  @Schema(description = "JWT Token containing user ID")
  private String jwtToken;

  public UserLoginResponseDTO() { super(); }

}
