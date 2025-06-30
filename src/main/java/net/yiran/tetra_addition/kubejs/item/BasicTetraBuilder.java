package net.yiran.tetra_addition.kubejs.item;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.yiran.tetra_addition.item.BasicTetraModular;
import net.yiran.tetra_addition.item.IBasicTetraBuilder;

import java.util.ArrayList;
import java.util.List;

public class BasicTetraBuilder extends ItemBuilder implements IBasicTetraBuilder<BasicTetraBuilder> {
    public String identifier;
    public int honeBase = 200;
    public int honeMultiplier = 400;
    public List<String> majorModuleKeys = new ArrayList<>();
    public List<String> minorModuleKeys = new ArrayList<>();
    public List<String> requiredModules = new ArrayList<>();
    public List<Integer> majorOffsets = new ArrayList<>();
    public List<Integer> minorOffsets = new ArrayList<>();
    public EquipmentSlot slotType = EquipmentSlot.MAINHAND;
    public BasicTetraModular.AttributeMode mode = BasicTetraModular.AttributeMode.TETRA;

    public BasicTetraBuilder(ResourceLocation i) {
        super(i);
        identifier = i.getPath();
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

    public BasicTetraModular.AttributeMode getAttributeMode() {
        return mode;
    }

    public BasicTetraBuilder honeBase(int honeBase) {
        this.honeBase = honeBase;
        return this;
    }

    public BasicTetraBuilder setSlotType(EquipmentSlot slotType) {
        this.slotType = slotType;
        return this;
    }

    public BasicTetraBuilder setAttributeMode(BasicTetraModular.AttributeMode mode) {
        this.mode = mode;
        return this;
    }

    public BasicTetraBuilder honeMultiplier(int honeMultiplier) {
        this.honeMultiplier = honeMultiplier;
        return this;
    }

    public Item createObject() {
        return new BasicTetraModular(this);
    }
}
