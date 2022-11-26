package com.kovospace.paster.item.mappings.dtoConversions;

import com.kovospace.paster.item.models.Item;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ItemToFileNameConversion implements Function<Item, String> {

    @Override
    public String apply(Item item) {
        if (item == null) return null;
        return String.format("%s-%s-%s", item.getUser().getId(), item.getId(), item.getFile().getId());
    }
}
