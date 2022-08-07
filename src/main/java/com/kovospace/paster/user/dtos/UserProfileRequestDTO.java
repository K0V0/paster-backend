package com.kovospace.paster.user.dtos;

import com.kovospace.paster.base.dtos.RequestDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileRequestDTO extends RequestDTO {
    private long id;
    private String userName;
    private String avatarFileName;
    private String email;

}
