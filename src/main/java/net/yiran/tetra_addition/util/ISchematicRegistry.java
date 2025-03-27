package net.yiran.tetra_addition.util;

import net.minecraft.resources.ResourceLocation;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

import java.util.Map;

public interface ISchematicRegistry {
    Map<ResourceLocation, UpgradeSchematic> getSchematicMap();
}
