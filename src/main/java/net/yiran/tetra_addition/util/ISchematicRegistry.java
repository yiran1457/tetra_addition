package net.yiran.tetra_addition.util;

import net.minecraft.resources.ResourceLocation;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

import java.util.Map;

public interface ISchematicRegistry {
    public Map<ResourceLocation, UpgradeSchematic> getSchematicMap();
}
