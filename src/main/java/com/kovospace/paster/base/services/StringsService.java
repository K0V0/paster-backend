package com.kovospace.paster.base.services;

import com.kovospace.paster.base.dtos.ErrorResponseDTO;

public interface StringsService {

    void setLocale(String locale);
    String getLocale();
    String getTranslation(String code);
    ErrorResponseDTO getErrorResponseDTO(String code);

}
