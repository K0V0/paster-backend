package com.kovospace.paster.item.mappings.attributeConverters;

import com.kovospace.paster.item.dtos.PlatformEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;

@Converter
public class PlatformEnumConverter implements AttributeConverter<PlatformEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PlatformEnum platformEnum) {
        if (platformEnum == null) {
            return PlatformEnum.UNKNOWN.ordinal();
        }
        return platformEnum.ordinal();
    }

    @Override
    public PlatformEnum convertToEntityAttribute(Integer integer) {
        return Arrays.stream(PlatformEnum.values())
                .filter(v -> v.ordinal() == integer)
                .findFirst()
                .orElse(PlatformEnum.UNKNOWN);
    }

}