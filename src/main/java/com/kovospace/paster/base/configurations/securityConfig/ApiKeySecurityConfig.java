package com.kovospace.paster.base.configurations.securityConfig;

import com.kovospace.paster.base.exceptions.ApiKeyInvalidException;
import com.kovospace.paster.base.exceptions.ApiKeyMissingException;
import com.kovospace.paster.base.filters.ApiKeyAuthFilter;
import com.kovospace.paster.base.services.ApiKeyService;
import com.kovospace.paster.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@Order(1)
public class ApiKeySecurityConfig extends WebSecurityConfigurerAdapter {

    private BaseService baseService;
    private ApiKeyService apiKeyService;

    @Value("${app.api-key-header}")
    private String principalRequestHeader;

    @Autowired
    public ApiKeySecurityConfig(ApiKeyService apiKeyService, BaseService baseService) {
        this.apiKeyService = apiKeyService;
        this.baseService = baseService;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        ApiKeyAuthFilter filter = new ApiKeyAuthFilter(principalRequestHeader);

        filter.setAuthenticationManager(authentication -> {
            String principal = (String) authentication.getPrincipal();
            if (principal == null || principal.equals("")) {
                throw new ApiKeyMissingException();
            }
            if (!apiKeyService.isValid(principal)) {
                throw new ApiKeyInvalidException();
            }
            authentication.setAuthenticated(true);
            return authentication;
        });

        httpSecurity
                .antMatcher("/**")
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(filter).authorizeRequests().anyRequest().authenticated();
    }

}
