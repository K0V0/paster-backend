package com.kovospace.paster.item.dtos.v2;

import com.kovospace.paster.base.dtoHelpers.FirstOrder;
import com.kovospace.paster.base.validators.EnumValidator;
import com.kovospace.paster.item.dtos.PlatformEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.GroupSequence;

import static com.kovospace.paster.base.utils.Utils.getConvertedPlatformValue;

@GroupSequence({
        ItemRequestDTO.class,
        FirstOrder.class
})
public abstract class ItemRequestDTO<PLATFORM_FORMAT> {

  @Getter
  @Setter
  @EnumValidator(enumClazz = PlatformEnum.class, groups = FirstOrder.class)
  private PLATFORM_FORMAT platform;

  @Getter
  @Setter
  private String deviceName;

}
