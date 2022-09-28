package com.kovospace.paster.user.dtos;

import com.kovospace.paster.base.dtoHelpers.FifthOrder;
import com.kovospace.paster.base.dtoHelpers.FirstOrder;
import com.kovospace.paster.base.dtoHelpers.FourthOrder;
import com.kovospace.paster.base.dtoHelpers.SecondOrder;
import com.kovospace.paster.base.dtoHelpers.ThirdOrder;
import com.kovospace.paster.base.dtos.RequestDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@GroupSequence({
        UserRegisterRequestDTO.class,
        FirstOrder.class,
        SecondOrder.class,
        ThirdOrder.class,
        FourthOrder.class,
        FifthOrder.class
})
public class UserPassChangeRequestDTO extends RequestDTO {

    /** identification - can be name or email */
    private String identification;

    private String oldPass;

    @NotNull(message = "user.register.password.required", groups = FirstOrder.class)
    @NotBlank(message = "user.register.password.empty", groups = SecondOrder.class)
    @Pattern(regexp = "^\\S+.+\\S+$", message = "user.register.password.format.whitespaces.around.denied", groups = ThirdOrder.class)
    @Size(min = 8, message = "user.register.password.format.length.short", groups = FourthOrder.class)
    @Size(max = 32, message = "user.register.password.format.length.long", groups = FifthOrder.class)
    private String newPass;

    @NotNull(message = "user.register.passwordConfirmation.required", groups = FirstOrder.class)
    @NotBlank(message = "user.register.passwordConfirmation.empty", groups = SecondOrder.class)
    private String newPass2;

}
