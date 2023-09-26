package com.kovospace.paster.base.configurations;

import com.kovospace.paster.base.springdoc.OnlyPublicMethodFilter;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.filters.OpenApiMethodFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class SpringdocConfig {

    static {
        io.swagger.v3.core.jackson.ModelResolver.enumsAsRef = true;
    }

    private static final String BASE_PACKAGE = "com.kovospace.paster";
    private static final String PUBLIC_API_GROUPNAME = "public-api";
    private static final String PRIVATE_API_GROUPNAME = "devel-api";
    private static final HashSet<String> RETURN_TYPES_POSSIBLE = new HashSet<String>() {{
        add(MediaType.APPLICATION_JSON_VALUE);
    }};

    private final OpenApiMethodFilter onlyPublicFilter;

    @Autowired
    SpringdocConfig(final OnlyPublicMethodFilter onlyPublicFilter) {
        this.onlyPublicFilter = onlyPublicFilter;
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group(PUBLIC_API_GROUPNAME)
                .packagesToScan(BASE_PACKAGE)
                .addOpenApiMethodFilter(onlyPublicFilter)
                .build();
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group(PRIVATE_API_GROUPNAME)
                .packagesToScan(BASE_PACKAGE)
                .build();
    }

}
