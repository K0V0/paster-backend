package com.kovospace.paster.item.mappings.conversions.v1;

import com.kovospace.paster.item.dtos.v1.ItemResponseDTO;
import com.kovospace.paster.item.mappings.conversions.ItemToItemResponseDTOConversionUtil;
import com.kovospace.paster.item.models.Item;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("v1ItemToItemResponseDTOConversion")
public class ItemToItemResponseDTOConversion implements Function<Item, ItemResponseDTO> {

    private ItemToItemResponseDTOConversionUtil conversionContext;
    private ItemResponseDTO response;
    private Item entity;

    @Override
    public ItemResponseDTO apply(Item entity) {
        if (entity == null) return null;
        this.entity = entity;
        conversionContext = new ItemToItemResponseDTOConversionUtil(entity);
        response = new ItemResponseDTO();

        response.setId(entity.getId());
        response.setText(entity.getData());
        response.setPreview(conversionContext.getPreviewText());
        response.setTimestamp(conversionContext.getTimestamp());
        response.setLarge(conversionContext.isLongText());
        response.setPlatform(entity.getPlatform());
        response.setDeviceName(conversionContext.getDeviceName());

        return response;
    }

}
