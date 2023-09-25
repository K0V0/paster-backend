package com.kovospace.paster.base.swagger;

import com.kovospace.paster.base.swagger.base.DocumentGenerator;
import com.kovospace.paster.base.swagger.documentationMapperAdapter.DocumentationMapperAdapteeV2;
import com.kovospace.paster.base.swagger.documentationMapperAdapter.DocumentationMapperAdapter;
import io.swagger.models.Swagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentGeneratorV2 extends DocumentGenerator<Swagger> {

    private final DocumentationMapperAdapteeV2 mapper;

    @Autowired
    public DocumentGeneratorV2(DocumentationMapperAdapteeV2 mapper) {
        this.mapper = mapper;
    }

    @Override
    protected DocumentationMapperAdapter<Swagger> getMapper() {
        return mapper;
    }

    @Override
    protected DocumentVersion getDocumentVersion() {
        return DocumentVersion.V2;
    }
}
