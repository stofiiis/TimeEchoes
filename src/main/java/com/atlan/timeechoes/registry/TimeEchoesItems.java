package com.atlan.timeechoes.registry;

import com.atlan.timeechoes.TimeEchoesMod;
import com.atlan.timeechoes.item.LoreItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TimeEchoesItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TimeEchoesMod.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TimeEchoesMod.MODID);

    public static final DeferredItem<Item> CHRONO_SHARD = ITEMS.registerItem("chrono_shard",
            props -> new LoreItem(props.stacksTo(1).rarity(Rarity.RARE), 2));
    public static final DeferredItem<Item> ENTROPY_CORE = ITEMS.registerItem("entropy_core",
            props -> new LoreItem(props.stacksTo(1).rarity(Rarity.EPIC), 2));

    public static final DeferredItem<Item> BRONZE_HOURGLASS = ITEMS.registerItem("bronze_hourglass",
            props -> new LoreItem(props.stacksTo(1).rarity(Rarity.UNCOMMON), 2));
    public static final DeferredItem<Item> OBSIDIAN_SUNDIAL = ITEMS.registerItem("obsidian_sundial",
            props -> new LoreItem(props.stacksTo(1).rarity(Rarity.UNCOMMON), 2));
    public static final DeferredItem<Item> FRACTURED_TABLET = ITEMS.registerItem("fractured_tablet",
            props -> new LoreItem(props.stacksTo(1).rarity(Rarity.UNCOMMON), 2));
    public static final DeferredItem<Item> ECHO_COMPASS = ITEMS.registerItem("echo_compass",
            props -> new LoreItem(props.stacksTo(1).rarity(Rarity.RARE), 2));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ECHO_TAB = CREATIVE_MODE_TABS.register("echo_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.timeechoes"))
                    .icon(() -> CHRONO_SHARD.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(CHRONO_SHARD.get());
                        output.accept(ENTROPY_CORE.get());
                        output.accept(BRONZE_HOURGLASS.get());
                        output.accept(OBSIDIAN_SUNDIAL.get());
                        output.accept(FRACTURED_TABLET.get());
                        output.accept(ECHO_COMPASS.get());
                    })
                    .build());

    private TimeEchoesItems() {
    }
}
