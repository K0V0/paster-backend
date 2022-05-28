package com.kovospace.paster.base.filters;

import com.kovospace.paster.base.exceptions.ApiKeyInvalidException;
import com.kovospace.paster.base.exceptions.ApiKeyMissingException;
import com.kovospace.paster.base.services.ApiKeyService;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;
public class ApiKeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

    private String API_KEY_HEADER = "x-auth-token";

    private ApiKeyService apiKeyService;

    public ApiKeyAuthFilter(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String token = request.getHeader(API_KEY_HEADER);
        String ipAddress = request.getRemoteAddr();

        if (token == null || token.equals("")) {
            throw new ApiKeyMissingException();
        }

        boolean isValid = (ipAddress == null) ? apiKeyService.isValid(token) : apiKeyService.isValid(token, ipAddress);
        if (!isValid) { throw new ApiKeyInvalidException(); }

        return token;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }

}