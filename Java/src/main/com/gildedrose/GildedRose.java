package com.gildedrose;

import java.util.ArrayList;
import java.util.List;

class GildedRose {
    static final String BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT = "Backstage passes to a TAFKAL80ETC concert";
    static final String SULFURAS_HAND_OF_RAGNAROS = "Sulfuras, Hand of Ragnaros";
    static final String CONJURED = "Conjured";
    static final String AGED_BRIE = "Aged Brie";

    static final int MAX_QUALITY = 50;
    static final int MIN_QUALITY = 0;

    List<Item> qualityIncreases;
    List<Item> qualityIncreasesVariableThenDrops;
    List<Item> qualityDoesntChange;

    Item[] items;

    public GildedRose(Item[] items) {
        this.qualityIncreases = new ArrayList<>();
        this.qualityIncreasesVariableThenDrops = new ArrayList<>();
        this.qualityDoesntChange = new ArrayList<>();

        // Using Lists in case new items are added with similar abilities, or if we load the list from a file
        this.qualityIncreases.add(new Item(GildedRose.AGED_BRIE, 0, 0));
        this.qualityDoesntChange.add(new Item(GildedRose.SULFURAS_HAND_OF_RAGNAROS, 0, 80));
        this.qualityIncreasesVariableThenDrops.add(new Item(GildedRose.BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT, 15, 20));

        this.items = items;
    }

    public void updateQuality() {
        for (Item item : items) {
            if (item.name.equals(AGED_BRIE) || item.name.equals(BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT)) {
                if (item.quality < MAX_QUALITY) {
                    item.quality = item.quality + 1;

                    if (item.name.equals(BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT)) {
                        if (item.sellIn < 11 && item.quality < MAX_QUALITY) {
                            item.quality++;
                        }
                        if (item.sellIn < 6 && item.quality < MAX_QUALITY) {
                            item.quality++;
                        }
                    }
                }
            } else {
                if (item.quality > MIN_QUALITY && !item.name.equals(SULFURAS_HAND_OF_RAGNAROS)) {
                    item.quality--;
                    if (item.name.startsWith(CONJURED)) {
                        item.quality--;
                    }
                }
            }

            if (!item.name.equals(SULFURAS_HAND_OF_RAGNAROS)) {
                item.sellIn = item.sellIn - 1;
            }

            if (item.sellIn < 0) {
                if (item.name.equals(AGED_BRIE)) {
                    if (item.quality < MAX_QUALITY) {
                        item.quality++;
                    }
                } else {
                    if (item.name.equals(BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT)) {
                        item.quality = MIN_QUALITY;
                    } else {
                        if (item.quality > MIN_QUALITY && !item.name.equals(SULFURAS_HAND_OF_RAGNAROS)) {
                            item.quality--;
                        }
                    }
                }
            }
        }
    }
}