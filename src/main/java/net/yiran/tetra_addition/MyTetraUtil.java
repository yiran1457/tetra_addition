package net.yiran.tetra_addition;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.yiran.tetra_addition.util.IConfigSchematic;
import net.yiran.tetra_addition.util.ISchematicRegistry;
import org.jetbrains.annotations.NotNull;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.ConfigSchematic;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class MyTetraUtil {
    public static MyTetraUtil instance = new MyTetraUtil();
    @Nullable
    public Map<String, ConfigSchematic> moduleKey2Schematic;

    public MyTetraUtil() {
    }

    public Map<ResourceLocation, UpgradeSchematic> getSchematicMap() {
        return ((ISchematicRegistry) SchematicRegistry.instance).getSchematicMap();
    }

    public String getModuleKey(@NotNull ItemStack itemStack, String slot) {
        return itemStack.getOrCreateTag().getString(slot);
    }

    public String getModuleMaterial(@NotNull ItemStack itemStack, String slot) {
        return itemStack.getOrCreateTag().getString(getModuleKey(itemStack, slot) + "_material").split("/")[1];
    }

    public int getMaterialCount(@NotNull ItemStack itemStack, String slot) {
        ConfigSchematic schematic = getModuleKey2Schematic().get(getModuleKey(itemStack, slot));
        if (schematic == null) return 1;
        return ((IConfigSchematic) schematic).getDefinition().outcomes[0].material.count;
    }

    public Map<String, ConfigSchematic> getModuleKey2Schematic() {
        if (moduleKey2Schematic == null) {
            return getAndUpdateModuleKey2Schematic();
        }
        return moduleKey2Schematic;
    }

    public Map<String, ConfigSchematic> getAndUpdateModuleKey2Schematic() {
        HashMap<String, ConfigSchematic> moduleKey2Schematic = new HashMap<>();
        getSchematicMap().forEach((k, v) -> {
            if (v instanceof ConfigSchematic && !v.isHoning()) {
                String moduleKey = ((IConfigSchematic) v).getDefinition().outcomes[0].moduleKey;
                if (moduleKey != null) {
                    moduleKey2Schematic.put(moduleKey, (ConfigSchematic) v);
                }
            }
        });
        this.moduleKey2Schematic = moduleKey2Schematic;
        return moduleKey2Schematic;
    }

    public boolean isSame(ItemStack itemStack, String slot, ConfigSchematic schematic) {
        return getModuleKey2Schematic().get(getModuleKey(itemStack, slot)) == schematic;
    }
}
