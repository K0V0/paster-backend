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

/*@Getter
@Setter*/
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

  @NotNull(message = "Username is required.", groups = FirstOrder.class)
  @NotBlank(message = "Username field is empty.", groups = SecondOrder.class)
  @Pattern(regexp = "^\\S+.+", message = "Your username begins with space.", groups = FourthOrder.class)
  @Pattern(regexp = ".+\\S+$", message = "Your username ends with space.", groups = FifthOrder.class)
  @Pattern(regexp = "^\\S+$", message = "Your username contains space(s).", groups = SixthOrder.class)
  @Pattern(
      regexp = "^[a-zA-Z0-9-_\\.]+$",
      message = "Your username contains not allowed characters.\n"
          + "Allowed characters are letters, numbers, underscores, dashes and dots.",
      groups = SeventhOrder.class
  )
  private String name;

  @NotNull(message = "Password is required.", groups = FirstOrder.class)
  @NotBlank(message = "Password field is empty.", groups = SecondOrder.class)
  @Pattern(regexp = "^\\S+.+\\S+$", message = "Your password is starting or ending with space(s).", groups = ThirdOrder.class)
  @Size(min = 8, message = "Password must have at least 8 characters.", groups = FourthOrder.class)
  private String pass;

  @NotNull(message = "Password confirmation is required.", groups = FirstOrder.class)
  @NotBlank(message = "Password confirmation field is empty.", groups = SecondOrder.class)
  private String pass2;

  @NotNull(message = "E-mail is required.", groups = FirstOrder.class)
  @NotBlank(message = "E-mail field is empty.", groups = SecondOrder.class)
  @Pattern(
      regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
      message = "E-mail address is not valid.",
      groups = ThirdOrder.class
  )
  private String email;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPass() {
    return pass;
  }

  public void setPass(String pass) {
    this.pass = pass;
  }

  public String getPass2() {
    return pass2;
  }

  public void setPass2(String pass2) {
    this.pass2 = pass2;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}

//TODO najst sposob kde dat stringy, pripadne s I18n
