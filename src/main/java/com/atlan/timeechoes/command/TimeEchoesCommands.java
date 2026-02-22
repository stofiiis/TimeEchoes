package com.atlan.timeechoes.command;

import com.atlan.timeechoes.TimeEchoesMod;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.datafixers.util.Pair;
import java.util.Set;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class TimeEchoesCommands {
    private static final int DEFAULT_RADIUS = 256;
    private static final int MAX_RADIUS = 4096;

    private static final ResourceKey<Structure> ECHO_RUIN_KEY = ResourceKey.create(
            Registries.STRUCTURE,
            Identifier.fromNamespaceAndPath(TimeEchoesMod.MODID, "echo_ruined_village")
    );

    private static final DynamicCommandExceptionType ERROR_STRUCTURE_NOT_FOUND = new DynamicCommandExceptionType(
            structure -> Component.translatable("commands.timeechoes.tp_structure.not_found", structure)
    );
    private static final DynamicCommandExceptionType ERROR_STRUCTURE_INVALID = new DynamicCommandExceptionType(
            structure -> Component.translatable("commands.timeechoes.tp_structure.invalid", structure)
    );
    private static final SimpleCommandExceptionType ERROR_TELEPORT_FAILED = new SimpleCommandExceptionType(
            Component.translatable("commands.timeechoes.tp_structure.teleport_failed")
    );

    private TimeEchoesCommands() {
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("timeechoes")
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(Commands.literal("tp_echo_ruin")
                                .executes(context -> teleportToEchoRuin(context.getSource(), DEFAULT_RADIUS))
                                .then(Commands.argument("radius", IntegerArgumentType.integer(1, MAX_RADIUS))
                                        .executes(context -> teleportToEchoRuin(context.getSource(), IntegerArgumentType.getInteger(context, "radius")))))
                        .then(Commands.literal("tp_structure")
                                .then(Commands.argument("structure", ResourceKeyArgument.key(Registries.STRUCTURE))
                                        .executes(context -> teleportToStructure(
                                                context.getSource(),
                                                ResourceKeyArgument.getStructure(context, "structure"),
                                                DEFAULT_RADIUS
                                        ))
                                        .then(Commands.argument("radius", IntegerArgumentType.integer(1, MAX_RADIUS))
                                                .executes(context -> teleportToStructure(
                                                        context.getSource(),
                                                        ResourceKeyArgument.getStructure(context, "structure"),
                                                        IntegerArgumentType.getInteger(context, "radius")
                                                )))))
        );
    }

    private static int teleportToEchoRuin(CommandSourceStack source, int radius) throws CommandSyntaxException {
        Holder.Reference<Structure> structure = source.getServer().registryAccess()
                .lookupOrThrow(Registries.STRUCTURE)
                .get(ECHO_RUIN_KEY)
                .orElseThrow(() -> ERROR_STRUCTURE_INVALID.create(ECHO_RUIN_KEY.identifier()));

        return teleportToStructure(source, structure, radius);
    }

    private static int teleportToStructure(
            CommandSourceStack source,
            Holder.Reference<Structure> structure,
            int radius
    ) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        ServerLevel level = source.getLevel();

        BlockPos origin = BlockPos.containing(source.getPosition());
        Pair<BlockPos, Holder<Structure>> located = level.getChunkSource()
                .getGenerator()
                .findNearestMapStructure(level, HolderSet.direct(structure), origin, radius, false);

        if (located == null) {
            throw ERROR_STRUCTURE_NOT_FOUND.create(structure.getRegisteredName());
        }

        BlockPos structurePos = located.getFirst();
        BlockPos surfacePos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, structurePos).above();

        boolean teleported = player.teleportTo(
                level,
                surfacePos.getX() + 0.5D,
                surfacePos.getY(),
                surfacePos.getZ() + 0.5D,
                Set.<Relative>of(),
                player.getYRot(),
                player.getXRot(),
                true
        );
        if (!teleported) {
            throw ERROR_TELEPORT_FAILED.create();
        }

        source.sendSuccess(
                () -> Component.translatable(
                        "commands.timeechoes.tp_structure.success",
                        located.getSecond().getRegisteredName(),
                        surfacePos.getX(),
                        surfacePos.getY(),
                        surfacePos.getZ(),
                        radius
                ),
                true
        );
        return 1;
    }
}
