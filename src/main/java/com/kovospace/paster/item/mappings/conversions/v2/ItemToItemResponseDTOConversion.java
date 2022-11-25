package com.kovospace.paster.item.mappings.conversions.v2;

import com.kovospace.paster.item.dtos.v2.ItemResponseDTO;
import com.kovospace.paster.item.mappings.conversions.ItemToItemResponseDTOConversionUtil;
import com.kovospace.paster.item.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("v2ItemToItemResponseDTOConversion")
public class ItemToItemResponseDTOConversion implements Function<Item, ItemResponseDTO> {

    private final FileToFilePreviewConversion fileToFilePreviewConversion;

    private ItemToItemResponseDTOConversionUtil conversionUtil;
    private ItemResponseDTO response;
    private Item entity;

    @Autowired
    public ItemToItemResponseDTOConversion(FileToFilePreviewConversion fileToFilePreviewConversion) {
        this.fileToFilePreviewConversion = fileToFilePreviewConversion;
    }

    @Override
    public ItemResponseDTO apply(Item entity) {
        if (entity == null) return null;
        this.entity = entity;
        conversionUtil = new ItemToItemResponseDTOConversionUtil(entity);
        response = new ItemResponseDTO();

        response.setId(entity.getId());
        response.setData(getData());
        response.setDataPreview(getPreviewData());
        response.setTimestamp(conversionUtil.getTimestamp());
        response.setPlatform(entity.getPlatform());
        response.setDeviceName(conversionUtil.getDeviceName());
        response.setFile(conversionUtil.isFile());

        return response;
    }

    private String getData() {
        if (!conversionUtil.isLongText()) return entity.getData();
        return null;
    }

    private String getPreviewData() {
        if (conversionUtil.isLongText()) return conversionUtil.getPreviewText();
        if (conversionUtil.isFileWithPreview()) return fileToFilePreviewConversion.apply(entity.getData());
        return null;
    }

}
