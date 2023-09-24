package com.kovospace.paster.base.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig /*implements WebMvcConfigurer*/ {

//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .setGroup("springshop-public")
//                .pathsToMatch("/public/**")
//                .build();
//    }

//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30)
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build()
//                .forCodeGeneration(true);
//    }

//    @Value("${bezkoder.openapi.dev-url}")
//    private String devUrl;
//
//    @Value("${bezkoder.openapi.prod-url}")
//    private String prodUrl;
//
//    @Bean
//    public OpenAPI myOpenAPI() {
//        Server devServer = new Server();
//        devServer.setUrl(devUrl);
//        devServer.setDescription("Server URL in Development environment");
//
//        Server prodServer = new Server();
//        prodServer.setUrl(prodUrl);
//        prodServer.setDescription("Server URL in Production environment");
//
//        Contact contact = new Contact();
//        contact.setEmail("bezkoder@gmail.com");
//        contact.setName("BezKoder");
//        contact.setUrl("https://www.bezkoder.com");
//
//        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");
//
//        Info info = new Info()
//                .title("Tutorial Management API")
//                .version("1.0")
//                .contact(contact)
//                .description("This API exposes endpoints to manage tutorials.").termsOfService("https://www.bezkoder.com/terms")
//                .license(mitLicense);
//
//        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
//    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Bookstore API")
                        .description(
                                "A bookstore project implemented with Spring Boot and Java 17.")
                        .contact(new Contact().name("Senorita Developer").url("https://github.com/senoritadeveloper01")));
    }

//    private SecurityScheme basicAuthScheme() {
//        return new BasicAuth("basicAuth");
//    }
//
//    private SecurityReference basicAuthReference() {
//        return new SecurityReference("basicAuth", new AuthorizationScope[0]);
//    }


}
