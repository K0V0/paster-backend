package com.kovospace.paster.base.filters;

import com.kovospace.paster.base.exceptions.ApiKeyInvalidException;
import com.kovospace.paster.base.exceptions.ApiKeyMissingException;
import com.kovospace.paster.base.services.ApiKeyService;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ApiKeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

    private String API_KEY_HEADER = "x-auth-token";

    private ApiKeyService apiKeyService;

    public ApiKeyAuthFilter(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        // standart request
        String token = request.getHeader(API_KEY_HEADER);
        if (token == null) {
            // websockets handshake
            token = getParametersFromURL(request.getQueryString()).get("apiKey");
        }
        if (token == null || token.equals("")) {
            throw new ApiKeyMissingException();
        }

        String ipAddress = request.getRemoteAddr();
        boolean isValid = (ipAddress == null) ? apiKeyService.isValid(token) : apiKeyService.isValid(token, ipAddress);
        if (!isValid) { throw new ApiKeyInvalidException(); }

        return token;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
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