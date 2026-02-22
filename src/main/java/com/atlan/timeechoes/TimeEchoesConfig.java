package com.atlan.timeechoes;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class TimeEchoesConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue AURA_RADIUS = BUILDER
            .comment("Radius around a player where temporal artifacts influence crop growth.")
            .defineInRange("auraRadius", 10, 1, 48);

    public static final ModConfigSpec.DoubleValue CHRONO_GROW_CHANCE = BUILDER
            .comment("Chance for Chrono Shard to force crop growth when influencing a random tick.")
            .defineInRange("chronoGrowChance", 0.35D, 0.0D, 1.0D);

    public static final ModConfigSpec.DoubleValue ENTROPY_BLOCK_CHANCE = BUILDER
            .comment("Chance for Entropy Core to block crop growth when influencing a random tick.")
            .defineInRange("entropyBlockChance", 0.45D, 0.0D, 1.0D);

    static final ModConfigSpec SPEC = BUILDER.build();

    private TimeEchoesConfig() {
    }
}
