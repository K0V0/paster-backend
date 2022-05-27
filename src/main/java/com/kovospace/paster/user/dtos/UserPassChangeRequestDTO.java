package com.kovospace.paster.user.dtos;

import com.kovospace.paster.base.dtos.RequestDTO;

public class UserPassChangeRequestDTO extends RequestDTO {
    private String email;
    private String oldPass;
    private String newPass;
    private String newPass2;
}
