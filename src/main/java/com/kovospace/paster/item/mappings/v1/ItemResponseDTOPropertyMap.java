package com.kovospace.paster.item.mappings.v1;

import com.kovospace.paster.item.converters.itemResponseDTOConverter.DeviceNameToNonNullStringConverter;
import com.kovospace.paster.item.converters.itemResponseDTOConverter.TimestampToStringConverter;
import com.kovospace.paster.item.dtos.v2.ItemResponseDTO;
import com.kovospace.paster.item.models.Item;
import org.modelmapper.PropertyMap;

public class ItemResponseDTOPropertyMap extends PropertyMap<Item, ItemResponseDTO> {

  private final TimestampToStringConverter timestampToStringConverter;
  private final DeviceNameToNonNullStringConverter deviceNameToNonNullStringConverter;

  public ItemResponseDTOPropertyMap() {
    super();
    this.timestampToStringConverter = new TimestampToStringConverter();
    this.deviceNameToNonNullStringConverter = new DeviceNameToNonNullStringConverter();
  }

  @Override
  protected void configure() {
    using(timestampToStringConverter).map(source.getCreatedAt(), destination.getTimestamp());
    using(deviceNameToNonNullStringConverter).map(source.getDeviceName(), destination.getDeviceName());
  }
}
