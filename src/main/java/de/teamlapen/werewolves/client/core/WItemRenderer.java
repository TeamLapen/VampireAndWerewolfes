package de.teamlapen.werewolves.client.core;

import de.teamlapen.werewolves.core.WItems;
import de.teamlapen.werewolves.items.CrossbowArrowItem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WItemRenderer {

    public static void registerColors() {
        Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
            if(tintIndex == 1) {
                return ((CrossbowArrowItem)stack.getItem()).getType().color;
            }
            return 0xFFFFFF;
        }, WItems.crossbow_arrow_silver_bolt);
    }
}
