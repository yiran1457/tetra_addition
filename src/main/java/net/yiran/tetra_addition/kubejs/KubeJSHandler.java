package net.yiran.tetra_addition.kubejs;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.yiran.tetra_addition.kubejs.events.WorkbenchTileCraftEventJS;

public class KubeJSHandler {
    public static void addEventListeners(){
        MinecraftForge.EVENT_BUS.addListener(WorkbenchTileCraftEventJS::handle);
    }
}
