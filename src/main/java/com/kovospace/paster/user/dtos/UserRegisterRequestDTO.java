package com.kovospace.paster.user.dtos;

import com.kovospace.paster.base.dtoHelpers.FirstOrder;
import com.kovospace.paster.base.dtoHelpers.FourthOrder;
import com.kovospace.paster.base.dtoHelpers.SecondOrder;
import com.kovospace.paster.base.dtoHelpers.ThirdOrder;
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
    UserRegisterRequestDTO.class,
    FirstOrder.class,
    SecondOrder.class,
    ThirdOrder.class,
    FourthOrder.class
})
public class UserRegisterRequestDTO {

  @NotNull(message = "Username is required.", groups = FirstOrder.class)
  @NotBlank(message = "Username field is empty.", groups = SecondOrder.class)
  @Pattern(regexp = "^\\S+$", message = "Whitespaces not allowed anywhere.", groups = FourthOrder.class)
  private String name;

  @NotNull(message = "Password is required.", groups = FirstOrder.class)
  @NotBlank(message = "Password field is empty.", groups = SecondOrder.class)
  @Size(min = 8, message = "Password must have at least 8 characters.", groups = ThirdOrder.class)
  private String pass;

  @NotNull(message = "Password confirmation is required.", groups = FirstOrder.class)
  @NotBlank(message = "Password confirmation field is empty.", groups = SecondOrder.class)
  private String pass2;

}
