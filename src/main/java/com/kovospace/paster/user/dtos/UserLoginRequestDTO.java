package com.kovospace.paster.user.dtos;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginRequestDTO {

  @NotBlank(message = "Username is required.")
  private String name;

  @NotBlank(message = "Password is required.")
  private String pass;

}
