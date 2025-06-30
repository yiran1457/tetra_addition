package net.yiran.tetra_addition.kubejs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.yiran.tetra_addition.events.WorkbenchTileCraftEvent;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

public class WorkbenchTileCraftEventJS extends EventJS {
    public ItemStack targetStack;
    public ItemStack upgradedStack;
    public Player player;
    public Level level;
    public WorkbenchTile workbenchTile;
    public ItemStack[] materials;
    public ItemStack[] materialsAltered;
    public UpgradeSchematic currentSchematic;
    public String currentSlot;

    public WorkbenchTileCraftEventJS(WorkbenchTileCraftEvent event) {
        targetStack = event.targetStack;
        upgradedStack = event.upgradedStack;
        player = event.player;
        level = event.level;
        workbenchTile = event.workbenchTile;
        materials = event.materials;
        materialsAltered = event.materialsAltered;
        currentSchematic = event.currentSchematic;
        currentSlot = event.currentSlot;
    }
    public static void handle(WorkbenchTileCraftEvent event) {
        TetraAdditionEvents.CRAFT.post(event);
    }
}
