package com.studio;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public final class ModBlocks {
    private ModBlocks() {}

    /** Piso xadrez da dimensao. */
    public static final Block REALM_TILE = new Block(AbstractBlock.Settings.create()
            .mapColor(MapColor.PURPLE)
            .strength(-1.0F, 3600000.0F)
            .dropsNothing()
            .sounds(BlockSoundGroup.DEEPSLATE_TILES));

    public static void register() {
        add("realm_tile", REALM_TILE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(REALM_TILE);
        });
    }

    private static void add(String name, Block block) {
        Identifier id = new Identifier(Studio.MOD_ID, name);
        Registry.register(Registries.BLOCK, id, block);
        Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
    }
}
