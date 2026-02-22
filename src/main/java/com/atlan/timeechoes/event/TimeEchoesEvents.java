package com.atlan.timeechoes.event;

import com.atlan.timeechoes.TimeEchoesConfig;
import com.atlan.timeechoes.registry.TimeEchoesItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.registries.DeferredItem;

public final class TimeEchoesEvents {
    private TimeEchoesEvents() {
    }

    @SubscribeEvent
    public static void onCropGrow(CropGrowEvent.Pre event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }

        Vec3 cropCenter = Vec3.atCenterOf(event.getPos());
        double radius = TimeEchoesConfig.AURA_RADIUS.getAsInt();
        double radiusSq = radius * radius;

        int chronoBearers = 0;
        int entropyBearers = 0;
        for (ServerPlayer player : serverLevel.players()) {
            if (player.isSpectator() || player.distanceToSqr(cropCenter) > radiusSq) {
                continue;
            }

            if (hasItem(player, TimeEchoesItems.CHRONO_SHARD)) {
                chronoBearers++;
            }
            if (hasItem(player, TimeEchoesItems.ENTROPY_CORE)) {
                entropyBearers++;
            }
        }

        if (chronoBearers == 0 && entropyBearers == 0) {
            return;
        }

        double growChance = Math.min(0.95D, chronoBearers * TimeEchoesConfig.CHRONO_GROW_CHANCE.get());
        double blockChance = Math.min(0.95D, entropyBearers * TimeEchoesConfig.ENTROPY_BLOCK_CHANCE.get());

        if (entropyBearers == 0) {
            if (serverLevel.getRandom().nextDouble() < growChance) {
                event.setResult(CropGrowEvent.Pre.Result.GROW);
            }
            return;
        }

        if (chronoBearers == 0) {
            if (serverLevel.getRandom().nextDouble() < blockChance) {
                event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
            }
            return;
        }

        double combinedChance = Math.min(0.95D, growChance + blockChance);
        if (serverLevel.getRandom().nextDouble() > combinedChance) {
            return;
        }

        double normalizedGrowChance = growChance / (growChance + blockChance);
        if (serverLevel.getRandom().nextDouble() < normalizedGrowChance) {
            event.setResult(CropGrowEvent.Pre.Result.GROW);
        } else {
            event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
        }
    }

    @SubscribeEvent
    public static void onWandererTrades(WandererTradesEvent event) {
        event.getGenericTrades().add(new BasicItemListing(6, new ItemStack(TimeEchoesItems.BRONZE_HOURGLASS.get()), 4, 2, 0.05F));
        event.getGenericTrades().add(new BasicItemListing(6, new ItemStack(TimeEchoesItems.OBSIDIAN_SUNDIAL.get()), 4, 2, 0.05F));
        event.getGenericTrades().add(new BasicItemListing(8, new ItemStack(TimeEchoesItems.FRACTURED_TABLET.get()), 3, 3, 0.05F));
        event.getGenericTrades().add(new BasicItemListing(10, new ItemStack(TimeEchoesItems.ECHO_COMPASS.get()), 2, 4, 0.02F));
        event.getRareTrades().add(new BasicItemListing(12, new ItemStack(TimeEchoesItems.CHRONO_SHARD.get()), 1, 6, 0.02F));
        event.getRareTrades().add(new BasicItemListing(16, new ItemStack(TimeEchoesItems.ENTROPY_CORE.get()), 1, 8, 0.01F));

        event.setGenericAmount(Math.max(event.getGenericAmount(), 6));
        event.setRareAmount(Math.max(event.getRareAmount(), 3));
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || event.loadedFromDisk()) {
            return;
        }

        if (!(event.getEntity() instanceof WanderingTrader trader)) {
            return;
        }

        if (trader.hasCustomName() || trader.getRandom().nextDouble() > 0.35D) {
            return;
        }

        trader.setCustomName(Component.translatable("entity.timeechoes.echo_archivist"));
        trader.setDespawnDelay(Math.max(trader.getDespawnDelay(), 96000));
    }

    private static boolean hasItem(ServerPlayer player, DeferredItem<Item> item) {
        return player.getInventory().contains(stack -> stack.is(item.get()));
    }
}
