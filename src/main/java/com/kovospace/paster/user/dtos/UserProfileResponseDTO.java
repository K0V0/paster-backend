package com.kovospace.paster.user.dtos;

import com.kovospace.paster.base.dtos.ResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileResponseDTO extends ResponseDTO {
    private long id;
    private String name;
    //private String avatarFileName;
}
