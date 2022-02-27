package com.kovospace.paster.user.dtos;

import com.kovospace.paster.base.dtoHelpers.FirstOrder;
import com.kovospace.paster.base.dtoHelpers.FourthOrder;
import com.kovospace.paster.base.dtoHelpers.SecondOrder;
import com.kovospace.paster.base.dtoHelpers.ThirdOrder;
import com.kovospace.paster.base.dtos.RequestDTO;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@GroupSequence({
    UserLoginRequestDTO.class,
    FirstOrder.class,
    SecondOrder.class,
    ThirdOrder.class,
    FourthOrder.class
})
public class UserLoginRequestDTO extends RequestDTO {

  @NotNull(message = "user.login.username.required", groups = FirstOrder.class)
  @NotBlank(message = "user.login.username.empty", groups = SecondOrder.class)
  @Pattern(regexp = "^\\S+$", message = "user.login.username.format.whitespaces.denied", groups = FourthOrder.class)
  private String name;

  @NotNull(message = "user.login.password.required", groups = FirstOrder.class)
  @NotBlank(message = "user.login.password.empty", groups = SecondOrder.class)
  @Size(min = 8, max = 32, message = "user.login.credentials.wrong", groups = ThirdOrder.class)
  private String pass;

}


