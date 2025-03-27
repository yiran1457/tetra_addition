package net.yiran.tetra_addition;

import net.minecraft.resources.ResourceLocation;
import net.yiran.tetra_addition.util.ISchematicRegistry;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

import java.util.Map;

public class MyTetraUtil {
    public MyTetraUtil() {}
    public static MyTetraUtil instance = new MyTetraUtil();
    public static Map<ResourceLocation, UpgradeSchematic> getSchematic() {
        return ((ISchematicRegistry)SchematicRegistry.instance).getSchematicMap();
    }
}
