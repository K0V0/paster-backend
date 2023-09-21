package com.kovospace.paster.base.configurations;

import com.kovospace.paster.base.annotations.swagger.PublicEndpoint;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@OpenAPIDefinition
public class SwaggerConfig {

    @Bean
    public Docket publicApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("public-api")
                .select()
                //.apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.kovospace.paster.user.controllers"))
                //.apis(RequestHandlerSelectors.withMethodAnnotation(PublicEndpoint.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
                //.produces(Collections.singleton("application/json"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Your API Documentation")
                .description("Description of your API")
                .version("1.0")
                .build();
    }
}

//TODO
// Just an add-on if your application have spring security enabled !!
// Then you will need to whitelist swagger-endpoint to not use authentication
//  httpSecurity.csrf().disable().authorizeHttpRequests(auth -> {
//            auth.requestMatchers("/v3/**", "/swagger-ui/**").permitAll();
//            auth.anyRequest().authenticated();
//        });
