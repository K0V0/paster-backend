package com.kovospace.paster;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@OpenAPIDefinition
@EnableSwagger2
public class PasterApplication {

  public static void main(String[] args) {
    SpringApplication.run(PasterApplication.class, args);
  }

}
