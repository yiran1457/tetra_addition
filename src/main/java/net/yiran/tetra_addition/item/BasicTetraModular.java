package net.yiran.tetra_addition.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RepairSchematic;
import se.mickelus.tetra.properties.AttributeHelper;

import java.util.ArrayList;
import java.util.List;

public class BasicTetraModular extends ItemModularHandheld implements Equipable {
    private final GuiModuleOffsets majorOffsets;
    private final GuiModuleOffsets minorOffsets;
    public EquipmentSlot slotType;
    public AttributeMode mode;

    public BasicTetraModular(IBasicTetraBuilder<?>  builder) {
        super(new Properties().stacksTo(1));

        majorModuleKeys = builder.getMajorModuleKeys().toArray(new String[0]);
        majorOffsets = new GuiModuleOffsets(builder.getMajorOffsets().stream().mapToInt(i -> i).toArray());
        minorModuleKeys = builder.getMinorModuleKeys().toArray(new String[0]);
        minorOffsets = new GuiModuleOffsets(builder.getMinorOffsets().stream().mapToInt(i -> i).toArray());
        requiredModules = builder.getRequiredModules().toArray(new String[0]);
        slotType = builder.getSlotType();
        mode = builder.getAttributeMode();

        updateConfig(builder.getHoneBase(), builder.getHoneMultiplier());
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, builder.getIdentifier()));
    }

    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        if (this.isBroken(itemStack) || mode == AttributeMode.NONE) {
            return AttributeHelper.emptyMap;
        }
        if (slot == this.slotType) {
            if (mode == AttributeMode.TETRA)
                return this.getAttributeModifiersCached(itemStack);
            if (mode == AttributeMode.EFFECT_ARMOR) {
                Multimap<Attribute, AttributeModifier> modifierMultimap = HashMultimap.create();
                int armor = getEffectLevel(itemStack, Builder.ARMOR);
                int armor_toughness = getEffectLevel(itemStack, Builder.ARMOR_TOUGHNESS);
                if (armor != 0)
                    modifierMultimap.put(Attributes.ARMOR, new AttributeModifier(slot.getName() + "armor", armor, AttributeModifier.Operation.ADDITION));
                if (armor_toughness != 0)
                    modifierMultimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(slot.getName() + "armor_toughness", armor_toughness, AttributeModifier.Operation.ADDITION));
                return modifierMultimap;
            }
        }

        return AttributeHelper.emptyMap;
    }

    @OnlyIn(Dist.CLIENT)
    public GuiModuleOffsets getMajorGuiOffsets(ItemStack itemStack) {
        return majorOffsets;
    }

    @OnlyIn(Dist.CLIENT)
    public GuiModuleOffsets getMinorGuiOffsets(ItemStack itemStack) {
        return minorOffsets;
    }

    public @NotNull EquipmentSlot getEquipmentSlot() {
        return slotType;
    }

    public static class Builder implements IBasicTetraBuilder<Builder>  {
        public static ItemEffect ARMOR_TOUGHNESS = ItemEffect.get("armor_toughness");
        public static ItemEffect ARMOR = ItemEffect.get("armor");

        public String identifier;
        public int honeBase = 200;
        public int honeMultiplier = 400;
        public List<String> majorModuleKeys = new ArrayList<>();
        public List<String> minorModuleKeys = new ArrayList<>();
        public List<String> requiredModules = new ArrayList<>();
        public List<Integer> majorOffsets = new ArrayList<>();
        public List<Integer> minorOffsets = new ArrayList<>();
        public EquipmentSlot slotType = EquipmentSlot.MAINHAND;
        public AttributeMode mode = AttributeMode.TETRA;

        public Builder(String identifier) {
            this.identifier = identifier;
        }
        public String getIdentifier() {
            return identifier;
        }

        public int getHoneBase() {
            return honeBase;
        }

        public int getHoneMultiplier() {
            return honeMultiplier;
        }

        public List<String> getMajorModuleKeys() {
            return majorModuleKeys;
        }

        public List<String> getMinorModuleKeys() {
            return minorModuleKeys;
        }

        public List<String> getRequiredModules() {
            return requiredModules;
        }

        public List<Integer> getMajorOffsets() {
            return majorOffsets;
        }

        public List<Integer> getMinorOffsets() {
            return minorOffsets;
        }

        public EquipmentSlot getSlotType() {
            return slotType;
        }

        public AttributeMode getAttributeMode() {
            return mode;
        }

        public Builder honeBase(int honeBase) {
            this.honeBase = honeBase;
            return this;
        }

        public Builder setSlotType(EquipmentSlot slotType) {
            this.slotType = slotType;
            return this;
        }

        public Builder setAttributeMode(AttributeMode mode) {
            this.mode = mode;
            return this;
        }

        public Builder honeMultiplier(int honeMultiplier) {
            this.honeMultiplier = honeMultiplier;
            return this;
        }

        public BasicTetraModular build() {
            return new BasicTetraModular(this);
        }
        public Item createObject() {
            return new BasicTetraModular(this);
        }
    }

    public enum AttributeMode {
        TETRA, NONE, EFFECT_ARMOR
    }
}
