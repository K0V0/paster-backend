package com.kovospace.paster.item.converters.itemResponseDTOConverter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class DeviceNameToNonNullStringConverter implements Converter<String, String> {

    @Override
    public String convert(MappingContext<String, String> mappingContext) {
        return mappingContext.getSource() == null ? "" : mappingContext.getSource();
    }
}
