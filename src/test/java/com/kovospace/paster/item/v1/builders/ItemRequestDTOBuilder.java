package com.kovospace.paster.item.v1.builders;

import com.kovospace.paster.item.dtos.v1.ItemRequestDTO;

public class ItemRequestDTOBuilder {

    private ItemRequestDTO dto;

    private ItemRequestDTOBuilder() {}

    public static ItemRequestDTOBuilder create() {
        return new ItemRequestDTOBuilder();
    }

    public ItemRequestDTO withPlatform(String platform) {
        dto.setPlatform(platform);
        return dto;
    }
}
