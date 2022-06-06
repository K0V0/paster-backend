package com.kovospace.paster.base.filters;

import com.kovospace.paster.base.services.StringsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class HeaderLocaleFilter extends OncePerRequestFilter {

    private StringsService stringsService;

    @Autowired
    public HeaderLocaleFilter(StringsService stringsService) {
        this.stringsService = stringsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException
    {
        System.out.println("localeeeeeeee");
        stringsService.setLocale("sk");
        filterChain.doFilter(request, response);
    }
}
