package net.yiran.tetra_addition.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;
import net.yiran.modernkubeevent.core.IKubeEvent;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

public class WorkbenchTileCraftEvent extends Event implements IKubeEvent {
    public ItemStack targetStack;
    public ItemStack upgradedStack;
    public Player player;
    public Level level;
    public WorkbenchTile workbenchTile;
    public ItemStack[] materials;
    public ItemStack[] materialsAltered;
    public UpgradeSchematic currentSchematic;
    public String currentSlot;

    public WorkbenchTileCraftEvent(
            ItemStack targetStack,
            ItemStack upgradedStack,
            Player player,
            WorkbenchTile workbenchTile,
            ItemStack[] materials,
            ItemStack[] materialsAltered,
            UpgradeSchematic currentSchematic,
            String currentSlot
    ) {
        this.targetStack = targetStack;
        this.upgradedStack = upgradedStack;
        this.player = player;
        this.level = player.level();
        this.workbenchTile = workbenchTile;
        this.materials = materials;
        this.materialsAltered = materialsAltered;
        this.currentSchematic = currentSchematic;
        this.currentSlot = currentSlot;
    }
}
