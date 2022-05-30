package com.kovospace.paster.base.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovospace.paster.base.dtos.ErrorResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Order(1)
@Component
public class FiltersExceptionHandler extends OncePerRequestFilter {

    private ObjectMapper objectMapper;

    @Autowired
    public FiltersExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException
    {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            HttpStatus httpStatus;
            switch (e.getMessage()) {
                case "general.endpoint.authentication.apikey.wrong":
                case "general.endpoint.authentication.apikey.missing":
                    httpStatus = HttpStatus.UNAUTHORIZED;
                    break;
                default:
                    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(e.getMessage());
            response.setStatus(httpStatus.value());
            response.getWriter().write(convertObjectToJson(errorResponseDTO));
        }
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        return objectMapper.writeValueAsString(object);
    }
}
