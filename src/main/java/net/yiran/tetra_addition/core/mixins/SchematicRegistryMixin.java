package net.yiran.tetra_addition.core.mixins;

import net.minecraft.resources.ResourceLocation;
import net.yiran.tetra_addition.core.ISchematicRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

import java.util.Map;

@Mixin(SchematicRegistry.class)
public class SchematicRegistryMixin implements ISchematicRegistry {


    @Shadow
    private Map<ResourceLocation, UpgradeSchematic> schematicMap;

    @Shadow public static SchematicRegistry instance;

    public Map<ResourceLocation, UpgradeSchematic> getSchematicMap() {
        return schematicMap;
    }
}
