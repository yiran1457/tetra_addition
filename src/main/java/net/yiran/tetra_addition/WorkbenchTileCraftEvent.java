package net.yiran.tetra_addition;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;

public class WorkbenchTileCraftEvent extends Event {
    public ItemStack targetStack;
    public ItemStack upgradedStack;
    public Player player;
    public Level level;
    public WorkbenchTile workbenchTile;
    public ItemStack[] materials;
    public ItemStack[] materialsAltered;

    public WorkbenchTileCraftEvent(
            ItemStack targetStack,
            ItemStack upgradedStack,
            Player player,
            WorkbenchTile workbenchTile,
            ItemStack[] materials,
            ItemStack[] materialsAltered
    ) {
        this.targetStack = targetStack;
        this.upgradedStack = upgradedStack;
        this.player = player;
        this.level = player.level();
        this.workbenchTile = workbenchTile;
        this.materials = materials;
        this.materialsAltered = materialsAltered;
    }
}
