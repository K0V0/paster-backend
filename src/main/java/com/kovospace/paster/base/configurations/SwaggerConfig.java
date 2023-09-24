package com.kovospace.paster.base.configurations;

import com.kovospace.paster.base.annotations.swagger.PublicEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    //TODO do konstant alebo properties
    private static final String BASE_PACKAGE = "com.kovospace.paster";
    private static final String PUBLIC_API_GROUPNAME = "public-api";
    private static final String PRIVATE_API_GROUPNAME = "devel-api";
    private static final HashSet<String> RETURN_TYPES_POSSIBLE = new HashSet<String>() {{
        add(MediaType.APPLICATION_JSON_VALUE);
    }};

    @Bean
    public Docket publicApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName(PUBLIC_API_GROUPNAME)
                .produces(RETURN_TYPES_POSSIBLE)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(PublicEndpoint.class))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket developerApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName(PRIVATE_API_GROUPNAME)
                .produces(RETURN_TYPES_POSSIBLE)
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }

}
