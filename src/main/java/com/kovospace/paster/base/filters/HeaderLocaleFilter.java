package com.kovospace.paster.base.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovospace.paster.base.services.StringsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class HeaderLocaleFilter extends BaseFilter {

    private static final String LANGUAGE_HEADER = "Accept-Language";
    private StringsService stringsService;

    @Autowired
    public HeaderLocaleFilter(ObjectMapper objectMapper, StringsService stringsService) {
        super(objectMapper, stringsService);
        this.stringsService = stringsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException
    {
        String language = request.getHeader(LANGUAGE_HEADER);
        stringsService.setLocale(language);
        filterChain.doFilter(request, response);
    }
}
