package com.kovospace.paster.item.dtos;

import com.kovospace.paster.base.dtoHelpers.FirstOrder;
import com.kovospace.paster.base.dtoHelpers.SecondOrder;
import com.kovospace.paster.base.dtoHelpers.ThirdOrder;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@GroupSequence({
    ItemRequestDTO.class,
    FirstOrder.class,
    SecondOrder.class,
    ThirdOrder.class
})
public class ItemRequestDTO {
  @NotNull(message = "item.request.missing", groups = FirstOrder.class)
  @NotBlank(message = "item.request.empty", groups = SecondOrder.class)
  @Size(
      min = 1,
      max = 4194304,
      message = "item.request.maxsize.reached",
      groups = ThirdOrder.class
  )
  private String text;
}
