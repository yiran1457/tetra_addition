package net.yiran.tetra_addition.util;

import net.minecraft.resources.ResourceLocation;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

import java.util.Map;

public interface ISchematicRegistry {
    private SchematicRegistry TD$self(){
        return (SchematicRegistry)this;
    }
    Map<ResourceLocation, UpgradeSchematic> getSchematicMap();
}
