package com.kovospace.paster.item.dtos.v1;

import com.kovospace.paster.base.dtoHelpers.FirstOrder;
import com.kovospace.paster.base.dtoHelpers.SecondOrder;
import com.kovospace.paster.base.dtoHelpers.ThirdOrder;
import com.kovospace.paster.base.validators.EnumValidator;
import com.kovospace.paster.item.dtos.PlatformEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@GroupSequence({
        ItemRequestDTO.class,
        FirstOrder.class,
        SecondOrder.class,
        ThirdOrder.class
})
public class ItemRequestDTO {

  @EnumValidator(enumClazz = PlatformEnum.class, groups = FirstOrder.class)
  private String platform;

  private String deviceName;

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
