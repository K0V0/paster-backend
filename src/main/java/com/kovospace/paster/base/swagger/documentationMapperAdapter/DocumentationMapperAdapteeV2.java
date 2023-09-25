package com.kovospace.paster.base.swagger.documentationMapperAdapter;

import io.swagger.models.Swagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.service.Documentation;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

@Component
public class DocumentationMapperAdapteeV2 implements DocumentationMapperAdapter<Swagger> {

    private final ServiceModelToSwagger2Mapper mapper;

    @Autowired
    public DocumentationMapperAdapteeV2(ServiceModelToSwagger2Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Swagger mapDocumentation(Documentation documentation) {
        return mapper.mapDocumentation(documentation);
    }
}
