package net.yiran.tetra_addition;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class WorkbenchTileCraftEvent extends Event {
    public ItemStack targetStack;
    public ItemStack upgradedStack;

    public WorkbenchTileCraftEvent(ItemStack upgradedStack) {
        this.upgradedStack = upgradedStack;
    }
}
