package com.gildedrose;

class GildedRose {
    static final String BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT = "Backstage passes to a TAFKAL80ETC concert";
    static final String SULFURAS_HAND_OF_RAGNAROS = "Sulfuras, Hand of Ragnaros";
    static final String CONJURED = "Conjured";
    static final String AGED_BRIE = "Aged Brie";

    static final int MAX_QUALITY = 50;
    static final int MIN_QUALITY = 0;

    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (Item item : items) {
            processQuality(item);
            processSellIn(item);
            processSellInExpired(item);
        }
    }

    private void processSellInExpired(Item item) {
        if (item.sellIn < 0) {
            if (item.name.equals(AGED_BRIE)) {
                if (item.quality < MAX_QUALITY) {
                    item.quality++;
                }
            } else {
                if (item.quality > MIN_QUALITY && !item.name.equals(SULFURAS_HAND_OF_RAGNAROS)) {
                    item.quality--;
                    processSellInExpiredForConcertTickets(item);
                }
            }
        }
    }

    private void processSellInExpiredForConcertTickets(Item item) {
        if (item.name.equals(BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT) && item.quality > MIN_QUALITY) { // I'm not sure which is more expensive: comparing to 0 or assigning 0
            item.quality = MIN_QUALITY;
        }
    }

    private void processSellIn(Item item) {
        if (!item.name.equals(SULFURAS_HAND_OF_RAGNAROS)) {
            item.sellIn = item.sellIn - 1;
        }
    }

    private void processQuality(Item item) {
        if (item.name.equals(AGED_BRIE) || item.name.equals(BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT)) {
            if (item.quality < MAX_QUALITY) {
                item.quality = item.quality + 1;
                processQualityForConcertTickets(item);
            }
        } else {
            if (item.quality > MIN_QUALITY && !item.name.equals(SULFURAS_HAND_OF_RAGNAROS)) {
                item.quality--;
                processQualityForConjuredItems(item);
            }
        }
    }

    private void processQualityForConjuredItems(Item item) {
        if (item.name.startsWith(CONJURED)) { // Decrease twice
            item.quality--;
        }
    }

    private void processQualityForConcertTickets(Item item) {
        if (item.name.equals(BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT)) {
            if (item.sellIn < 11 && item.quality < MAX_QUALITY) { // Increase again (2x)
                item.quality++;
            }
            if (item.sellIn < 6 && item.quality < MAX_QUALITY) { // Increase again (3x)
                item.quality++;
            }
        }
    }
}