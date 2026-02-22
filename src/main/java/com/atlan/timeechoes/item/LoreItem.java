package com.atlan.timeechoes.item;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class LoreItem extends Item {
    private final int loreLineCount;

    public LoreItem(Properties properties, int loreLineCount) {
        super(properties);
        this.loreLineCount = loreLineCount;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            TooltipDisplay tooltipDisplay,
            Consumer<Component> tooltip,
            TooltipFlag flag
    ) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltip, flag);

        String lorePrefix = this.getDescriptionId() + ".lore.";
        for (int line = 1; line <= this.loreLineCount; line++) {
            tooltip.accept(Component.translatable(lorePrefix + line).withStyle(ChatFormatting.GRAY));
        }
    }
}
