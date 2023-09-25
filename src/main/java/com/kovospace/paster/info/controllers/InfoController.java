package com.kovospace.paster.info.controllers;

import com.kovospace.paster.base.annotations.swagger.PublicEndpoint;
import com.kovospace.paster.base.controllers.BaseController;
import com.kovospace.paster.item.dtos.PlatformEnum;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/info")
@OpenAPIDefinition(
        info = @Info(
                title = "Application info API",
                version = "v1"))
public class InfoController extends BaseController {

    @PublicEndpoint
    @PostMapping("/checkPlatform")
    @Operation(
            summary = "Check Platform",
            description = "Given platform is listed and supported"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Platform checked successfully",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(type = "string")
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Given platform does not exists",
            content = @Content
    )
    public ResponseEntity<String> checkPlatform(

            @Parameter(
                    name = "platformEnum",
                    description = "The platform enum"//,
                    //schema = @Schema(implementation = PlatformEnum.class, defaultValue = "UNKNOWN")

            )
            @RequestParam(name = "platformEnum")
            final PlatformEnum platformEnum
            //TODO catch exception if enum value does not exist
    ) {
        return new ResponseEntity<>("passed", HttpStatus.OK);
    }
}
