package com.kovospace.paster.board.dtos;

import com.kovospace.paster.base.dtoHelpers.FirstOrder;
import com.kovospace.paster.base.dtoHelpers.SecondOrder;
import com.kovospace.paster.base.dtoHelpers.ThirdOrder;
import com.kovospace.paster.user.dtos.UserLoginRequestDTO;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@GroupSequence({
    UserLoginRequestDTO.class,
    FirstOrder.class,
    SecondOrder.class,
    ThirdOrder.class
})
public class ItemRequestDTO {
  @NotNull(message = "Item not presented.", groups = FirstOrder.class)
  @NotBlank(message = "Nothing pasted.", groups = SecondOrder.class)
  @Size(
      min = 1,
      max = Integer.MAX_VALUE - 1,
      message = "Maximum allowed size exceeded.",
      groups = ThirdOrder.class
  )
  private String text;
}
