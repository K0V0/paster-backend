package com.kovospace.paster.item.mappings.v1;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component("v1ItemModelMapper")
public class ItemModelMapper extends ModelMapper {

    public ItemModelMapper() {
        addMappings(new ItemResponseDTOPropertyMap());
    }
}
