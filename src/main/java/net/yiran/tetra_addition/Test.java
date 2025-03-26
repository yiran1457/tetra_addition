package net.yiran.tetra_addition;

import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TetraAddition.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Test {
    @SubscribeEvent
    public static void craft(WorkbenchTileCraftEvent event) {
        event.player.sendSystemMessage(Component.literal(event.targetStack.serializeNBT().toString()));
        event.player.sendSystemMessage(Component.literal(event.upgradedStack.serializeNBT().toString()));
    }
}
