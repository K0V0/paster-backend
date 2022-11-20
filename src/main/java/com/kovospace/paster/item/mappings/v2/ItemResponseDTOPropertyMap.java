package com.kovospace.paster.item.mappings.v2;

import com.kovospace.paster.item.converters.itemResponseDTOConverter.DeviceNameToNonNullStringConverter;
import com.kovospace.paster.item.converters.itemResponseDTOConverter.TimestampToStringConverter;
import com.kovospace.paster.item.dtos.v2.ItemResponseDTO;
import com.kovospace.paster.item.models.File;
import com.kovospace.paster.item.models.Item;
import org.modelmapper.Condition;
import org.modelmapper.PropertyMap;

public class ItemResponseDTOPropertyMap extends PropertyMap<Item, ItemResponseDTO> {

  private final TimestampToStringConverter timestampToStringConverter;
  private final DeviceNameToNonNullStringConverter deviceNameToNonNullStringConverter;
  private final Condition<File, Boolean> isFile;

  public ItemResponseDTOPropertyMap() {
    super();
    this.timestampToStringConverter = new TimestampToStringConverter();
    this.deviceNameToNonNullStringConverter = new DeviceNameToNonNullStringConverter();
    //TODO conditions for file - return olny fully uploaded
    this.isFile = ctx -> ctx.getSource() != null;
  }

  @Override
  protected void configure() {
    using(timestampToStringConverter).map(source.getCreatedAt(), destination.getTimestamp());
    using(deviceNameToNonNullStringConverter).map(source.getDeviceName(), destination.getDeviceName());
    /** item is file scenario */
    when(isFile).map(true, destination.isFile());
    //TODO set ULR to data
    /** short text scenario */

    /** large text scenario */

  }
}
