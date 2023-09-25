package com.kovospace.paster.base.swagger.documentationMapperAdapter;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.oas.mappers.ServiceModelToOpenApiMapper;
import springfox.documentation.service.Documentation;

@Component
public class DocumentationMapperAdapteeV3 implements DocumentationMapperAdapter<OpenAPI> {

    private final ServiceModelToOpenApiMapper mapper;

    @Autowired
    public DocumentationMapperAdapteeV3(ServiceModelToOpenApiMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public OpenAPI mapDocumentation(Documentation documentation) {
        return mapper.mapDocumentation(documentation);
    }
}
