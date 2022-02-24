package com.kovospace.paster.user.dtos;

import com.kovospace.paster.base.dtoHelpers.FifthOrder;
import com.kovospace.paster.base.dtoHelpers.FirstOrder;
import com.kovospace.paster.base.dtoHelpers.FourthOrder;
import com.kovospace.paster.base.dtoHelpers.SecondOrder;
import com.kovospace.paster.base.dtoHelpers.SeventhOrder;
import com.kovospace.paster.base.dtoHelpers.SixthOrder;
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
    UserRegisterRequestDTO.class,
    FirstOrder.class,
    SecondOrder.class,
    ThirdOrder.class,
    FourthOrder.class,
    FifthOrder.class,
    SixthOrder.class,
    SeventhOrder.class
})
public class UserRegisterRequestDTO extends RequestDTO {

  @NotNull(message = "user.register.username.required", groups = FirstOrder.class)
  @NotBlank(message = "user.register.username.empty", groups = SecondOrder.class)
  @Pattern(regexp = "(^\\S+.+)|(.+\\S+$)", message = "user.register.username.format.whitespaces.around.denied", groups = ThirdOrder.class)
  @Pattern(regexp = "^\\S+.+", message = "user.register.username.format.whitespaces.beginning.denied", groups = FourthOrder.class)
  @Pattern(regexp = ".+\\S+$", message = "user.register.username.format.whitespaces.end.denied", groups = FifthOrder.class)
  @Pattern(regexp = "^\\S+$", message = "user.register.username.format.whitespaces.denied", groups = SixthOrder.class)
  @Pattern(regexp = "^[a-zA-Z0-9-_.]+$", message = "user.register.username.format.characters.denied", groups = SeventhOrder.class)
  private String name;

  @NotNull(message = "user.register.password.required", groups = FirstOrder.class)
  @NotBlank(message = "user.register.password.empty", groups = SecondOrder.class)
  @Pattern(regexp = "^\\S+.+\\S+$", message = "user.register.password.format.whitespaces.around.denied", groups = ThirdOrder.class)
  @Size(min = 8, message = "user.register.password.format.length.short", groups = FourthOrder.class)
  @Size(max = 32, message = "user.register.password.format.length.long", groups = FifthOrder.class)
  private String pass;

  @NotNull(message = "user.register.passwordConfirmation.required", groups = FirstOrder.class)
  @NotBlank(message = "user.register.passwordConfirmation.empty", groups = SecondOrder.class)
  private String pass2;

  @NotNull(message = "user.register.email.required", groups = FirstOrder.class)
  @NotBlank(message = "user.register.email.empty", groups = SecondOrder.class)
  @Pattern(
      regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
      message = "user.register.email.format.wrong",
      groups = ThirdOrder.class
  )
  private String email;

}