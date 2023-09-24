package com.kovospace.paster.base.filters;

import com.kovospace.paster.base.exceptions.ApiKeyInvalidException;
import com.kovospace.paster.base.exceptions.ApiKeyMissingException;
import com.kovospace.paster.base.services.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Order(2)
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final String API_KEY_HEADER = "x-auth-token";

    private final ApiKeyService apiKeyService;

    @Autowired
    public ApiKeyAuthFilter(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException
    {
        System.out.println("API key filter called");
        System.out.println(request.getRequestURL());

        // standart request
        String token = request.getHeader(API_KEY_HEADER);
        if (token == null) {
            // websockets handshake
            token = getParametersFromURL(request.getQueryString()).get("apiKey");
            System.out.println("API key filter - token not found in headers, try url params");
        }
        if (token == null || token.equals("")) {
            System.out.println("API key filter - token is NULL");
            //TODO docasne deaktivovane kvoli swagger api
            //throw new ApiKeyMissingException();
        }

        String ipAddress = request.getRemoteAddr();
        System.out.println("API key filter - ipAddress: " + request.getRemoteAddr());
        boolean isValid = (ipAddress == null) ? apiKeyService.isValid(token) : apiKeyService.isValid(token, ipAddress);
        if (!isValid && token != null) { //TODO swagger UI hack
            System.out.println("API key filter - token is invalid - " + token);
            throw new ApiKeyInvalidException();
        }

        System.out.println("API key filter - continue filterChain");
        filterChain.doFilter(request, response);
    }

    private Map<String, String> getParametersFromURL(String queryString) {
        return Optional.ofNullable(queryString)
                .map(qs -> qs.split("&"))
                .map(parts -> Arrays.stream(parts)
                        .map(part -> part.split("="))
                        .collect(Collectors.toMap(k -> k[0], v -> v[1])))
                .orElseGet(HashMap::new);
    }
}