package com.kovospace.paster.base.swagger.documentationMapperAdapter;

import springfox.documentation.service.Documentation;

public interface DocumentationMapperAdapter<RESULT_OBJ> {
    RESULT_OBJ mapDocumentation(Documentation documentation);
}
