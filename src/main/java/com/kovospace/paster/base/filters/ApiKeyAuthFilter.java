package com.kovospace.paster.base.filters;

import com.kovospace.paster.base.exceptions.ApiKeyInvalidException;
import com.kovospace.paster.base.exceptions.ApiKeyMissingException;
import com.kovospace.paster.base.services.ApiKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyAuthFilter.class);

    private final String API_KEY_HEADER = "x-auth-token";

    private final ApiKeyService apiKeyService;

    @Autowired
    public ApiKeyAuthFilter(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
        logger.debug("api key filter construction");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException
    {
//        logger.debug("api key filter run");
//
//        // standart request
//        String token = request.getHeader(API_KEY_HEADER);
//        if (token == null) {
//            // websockets handshake
//            token = getParametersFromURL(request.getQueryString()).get("apiKey");
//        }
//        if (token == null || token.equals("")) {
//            throw new ApiKeyMissingException();
//        }
//
//        String ipAddress = request.getRemoteAddr();
//        boolean isValid = (ipAddress == null) ? apiKeyService.isValid(token) : apiKeyService.isValid(token, ipAddress);
//        if (!isValid) { throw new ApiKeyInvalidException(); }

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