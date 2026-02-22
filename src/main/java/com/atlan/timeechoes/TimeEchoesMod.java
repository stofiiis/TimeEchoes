package com.atlan.timeechoes;

import com.atlan.timeechoes.command.TimeEchoesCommands;
import com.atlan.timeechoes.event.TimeEchoesEvents;
import com.atlan.timeechoes.registry.TimeEchoesItems;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(TimeEchoesMod.MODID)
public class TimeEchoesMod {
    public static final String MODID = "timeechoes";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TimeEchoesMod(IEventBus modEventBus, ModContainer modContainer) {
        TimeEchoesItems.ITEMS.register(modEventBus);
        TimeEchoesItems.CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(TimeEchoesEvents.class);
        NeoForge.EVENT_BUS.register(TimeEchoesCommands.class);

        modContainer.registerConfig(ModConfig.Type.COMMON, TimeEchoesConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Time Echoes initialized");
    }
}
