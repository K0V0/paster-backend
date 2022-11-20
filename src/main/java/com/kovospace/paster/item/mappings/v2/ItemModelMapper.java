package com.kovospace.paster.item.mappings.v2;

import com.kovospace.paster.item.mappings.v1.ItemResponseDTOPropertyMap;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component("v2ItemModelMapper")
public class ItemModelMapper extends ModelMapper {

    public ItemModelMapper() {
        addMappings(new ItemResponseDTOPropertyMap());
    }
}