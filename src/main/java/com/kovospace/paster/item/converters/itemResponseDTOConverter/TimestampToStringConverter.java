package com.kovospace.paster.item.converters.itemResponseDTOConverter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.sql.Timestamp;

public class TimestampToStringConverter implements Converter<Timestamp, Long> {

    @Override
    public Long convert(MappingContext<Timestamp, Long> context) {
      return context.getSource().getTime();
    }
}
