package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GildedRoseTest {

    @Test
    public void foo() {
        Item[] items = new Item[] { new Item("foo", 0, 0) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        // This doesn't really test anything (apart that the name isn't changed), but the goblin in the corner probably
        // had a good reason for putting it here.
        assertEquals("foo", app.items[0].name);
    }

    @Test
    public void validateQualityRules() {
        Item[] items = new Item[] {
                new Item("+5 Dexterity Vest", 10, 20), //
                new Item("Amazing Guacamole", 0, 10), //
                new Item(GildedRose.AGED_BRIE, 2, 0), //
                new Item("Elixir of the Mongoose", 5, 7), //
                new Item(GildedRose.SULFURAS_HAND_OF_RAGNAROS, 0, 80), //
                new Item(GildedRose.SULFURAS_HAND_OF_RAGNAROS, -1, 80),
                new Item(GildedRose.BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT, 15, 20),
                new Item(GildedRose.BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT, 10, 49),
                new Item(GildedRose.BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT, 5, 49),
                new Item("Conjured Mana Cake", 3, 6) };

        GildedRose app = new GildedRose(items);

        app.updateQuality();
        // Degrades normally for a standard item before sell-by date
        assertEquals(19, items[0].quality, "Expecting the quality for item 0 to be reduced by 1");

        // Degrades 2x faster for a standard item past sell-by date
        assertEquals(8, items[1].quality, "Expecting the quality of an item past sell-by date to drop 2x faster");

        // Increases in quality for aged brie and concert tickets BEFORE sell-by date -10
        assertEquals(1, items[2].quality, "Aged brie should increase in quality");
        assertEquals(21, items[6].quality, "Concert tickets increase quality");

        // Quality never goes above 50
        // Increases in quality x2 for concert tickets before sell-by date - 10
        assertEquals(50, items[7].quality, "Concert tickets quality should increase 2x when sell-by date is less or equals to 10, but remain below 50");

        // Increases in quality 3x for concert tickets after sell-by date - 10
        assertEquals(50, items[8].quality, "Concert tickets quality should increase 1x when sell-by date is less or equals to 5, but remain below 50");

        // Conjured items decrease in quality 2x faster
        assertEquals(4, items[9].quality, "Conjured items decrease in quality 2x faster");

        // Sulfuras quality doesn't change. Its sell-by date doesn't change either
        assertEquals(80, items[4].quality);
        assertEquals(0, items[4].sellIn);
        assertEquals(80, items[5].quality);
        assertEquals(-1, items[5].sellIn);

        // Quality becomes 0 for concert tickets after sell by date, check quality increase too
        app.updateQuality();
        app.updateQuality();
        app.updateQuality();
        app.updateQuality();
        assertEquals(25, items[6].quality, "Concert tickets increase quality");
        app.updateQuality();
        assertEquals(0, items[8].quality, "Concert tickets drop quality to 0 after sell-by date");
        assertEquals(27, items[6].quality, "Concert tickets quality should increase 2x when sell-by date is less or equals to 10");

        // Quality never goes below 0
        assertEquals(0, items[9].quality, "Conjured items decrease in quality 2x faster but never below 0");
        app.updateQuality();
        app.updateQuality();
        app.updateQuality();
        app.updateQuality();
        assertEquals(0, items[1].quality, "Quality never goes below 0");

    }
}
