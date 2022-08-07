package com.kovospace.paster.base.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovospace.paster.base.dtos.ErrorResponseDTO;
import com.kovospace.paster.base.services.StringsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseFilter extends OncePerRequestFilter {

    protected final ObjectMapper objectMapper;
    protected final StringsService stringsService;

    public BaseFilter(ObjectMapper objectMapper, StringsService stringsService) {
        this.objectMapper = objectMapper;
        this.stringsService = stringsService;
    }

    protected void handleError(HttpServletResponse response, String code, HttpStatus httpStatus)
            throws IOException
    {
        ErrorResponseDTO errorResponse = stringsService.getErrorResponseDTO(code);
        response.setContentType("application/json");
        response.setStatus(httpStatus.value());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
