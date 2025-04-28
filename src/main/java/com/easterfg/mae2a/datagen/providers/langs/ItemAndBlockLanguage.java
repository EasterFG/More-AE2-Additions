package com.easterfg.mae2a.datagen.providers.langs;

import static com.easterfg.mae2a.datagen.providers.LocalizationProvider.add;

import com.easterfg.mae2a.common.definition.MAE2ABlocks;
import com.easterfg.mae2a.common.definition.MAE2AItems;

/**
 * @author EasterFG on 2025/4/1
 */
public class ItemAndBlockLanguage {
    public static void init() {
        for (var item : MAE2AItems.getItems()) {
            add("item.mae2a." + item.id().getPath(), item.getChineseName(), item.getEnglishName());
        }

        for (var block : MAE2ABlocks.getBlocks()) {
            add("block.mae2a." + block.id().getPath(), block.getChineseName(), block.getEnglishName());
        }
    }
}
