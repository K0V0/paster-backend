package com.kovospace.paster.item.converters;

import com.kovospace.paster.item.dtos.PlatformEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PlatformEnumConverter implements AttributeConverter<PlatformEnum, String> {

    @Override
    public String convertToDatabaseColumn(PlatformEnum platformEnum) {
        if (platformEnum == null) {
            return PlatformEnum.UNKNOWN.name();
        }
        return platformEnum.name();
    }

    @Override
    public PlatformEnum convertToEntityAttribute(String code) {
        return PlatformEnum.getByName(code);
    }
}
