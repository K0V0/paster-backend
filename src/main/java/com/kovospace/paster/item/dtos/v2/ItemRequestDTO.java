package com.kovospace.paster.item.dtos.v2;

import com.kovospace.paster.base.dtoHelpers.FirstOrder;
import com.kovospace.paster.base.validators.EnumValidator;
import com.kovospace.paster.item.dtos.PlatformEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.GroupSequence;
import java.util.Optional;

@Getter
@Setter
@GroupSequence({
        ItemRequestDTO.class,
        FirstOrder.class
})
public abstract class ItemRequestDTO {

  @EnumValidator(enumClazz = PlatformEnum.class, groups = FirstOrder.class)
  private String platform;

  private String deviceName;

  public void setPlatform(String platform) {
    this.platform = Optional.ofNullable(platform)
            .map(String::toUpperCase)
            .orElse(PlatformEnum.UNKNOWN.name());
  }

}
