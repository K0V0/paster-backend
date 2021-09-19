package com.kovospace.paster.user.dtos;

import com.kovospace.paster.user.dtos.UserLoginRequestDTO.FirstOrder;
import com.kovospace.paster.user.dtos.UserLoginRequestDTO.FourthOrder;
import com.kovospace.paster.user.dtos.UserLoginRequestDTO.SecondOrder;
import com.kovospace.paster.user.dtos.UserLoginRequestDTO.ThirdOrder;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@GroupSequence({UserLoginRequestDTO.class, FirstOrder.class, SecondOrder.class, ThirdOrder.class,
    FourthOrder.class})
public class UserLoginRequestDTO {

  @NotNull(message = "Username is required.", groups = FirstOrder.class)
  @NotBlank(message = "Username field is empty.", groups = SecondOrder.class)
  @Pattern(regexp = "^\\S+$", message = "Whitespaces not allowed anywhere.", groups = FourthOrder.class)
  private String name;

  @NotNull(message = "Password is required.", groups = FirstOrder.class)
  @NotBlank(message = "Password field is empty.", groups = SecondOrder.class)
  @Size(min = 8, message = "Password must have at least 8 characters.", groups = ThirdOrder.class)
  private String pass;

  public interface FirstOrder{}
  public interface SecondOrder{}
  public interface ThirdOrder{}
  public interface FourthOrder{}
}


