package com.kovospace.paster.base.swagger;

import com.kovospace.paster.base.swagger.base.DocumentGenerator;
import com.kovospace.paster.base.swagger.documentationMapperAdapter.DocumentationMapperAdapteeV3;
import com.kovospace.paster.base.swagger.documentationMapperAdapter.DocumentationMapperAdapter;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentGeneratorV3 extends DocumentGenerator<OpenAPI> {

    private final DocumentationMapperAdapteeV3 mapper;

    @Autowired
    public DocumentGeneratorV3(DocumentationMapperAdapteeV3 mapper) {
        this.mapper = mapper;
    }

    @Override
    protected DocumentationMapperAdapter<OpenAPI> getMapper() {
        return mapper;
    }

    @Override
    protected DocumentVersion getDocumentVersion() {
        return DocumentVersion.V3;
    }
}
