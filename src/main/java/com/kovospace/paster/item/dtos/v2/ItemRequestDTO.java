package com.kovospace.paster.item.dtos.v2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kovospace.paster.base.dtoHelpers.FirstOrder;
import com.kovospace.paster.base.dtoHelpers.SecondOrder;
import com.kovospace.paster.base.dtoHelpers.ThirdOrder;
import com.kovospace.paster.base.validators.platform.PlatformValidator;
import com.kovospace.paster.item.dtos.PlatformEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.GroupSequence;

@Getter
@Setter
@GroupSequence({
        ItemRequestDTO.class,
        FirstOrder.class,
        SecondOrder.class,
        ThirdOrder.class
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ItemRequestDTO {

  @PlatformValidator(enumClazz = PlatformEnum.class, groups = ThirdOrder.class)
  private String platform = PlatformEnum.UNKNOWN.name();

  private String deviceName;

}
