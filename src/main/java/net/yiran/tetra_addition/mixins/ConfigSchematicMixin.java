package net.yiran.tetra_addition.mixins;

import net.minecraft.world.item.ItemStack;
import net.yiran.tetra_addition.util.IConfigSchematic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import se.mickelus.tetra.module.schematic.ConfigSchematic;
import se.mickelus.tetra.module.schematic.OutcomeDefinition;
import se.mickelus.tetra.module.schematic.SchematicDefinition;

import java.util.Optional;

@Mixin(value = ConfigSchematic.class,remap = false)
public abstract class ConfigSchematicMixin implements IConfigSchematic {
    @Shadow @Final private SchematicDefinition definition;

    @Shadow protected abstract Optional<OutcomeDefinition> getOutcomeFromMaterial(ItemStack materialStack, int slot);

    public Optional<OutcomeDefinition> myGetOutcomeFromMaterial(ItemStack materialStack, int slot) {
        return getOutcomeFromMaterial(materialStack, slot);
    }

    public SchematicDefinition getDefinition(){
        return definition;
    }
}
