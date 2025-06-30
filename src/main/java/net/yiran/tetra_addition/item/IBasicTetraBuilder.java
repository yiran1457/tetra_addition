package net.yiran.tetra_addition.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

import java.util.List;

public interface IBasicTetraBuilder<T> {

    default T addMajorModuleKey(String majorModuleKey, int x, int y, boolean required) {
        if (required) {
            getRequiredModules().add(majorModuleKey);
        }
        getMajorModuleKeys().add(majorModuleKey);
        getMajorOffsets().add(x);
        getMajorOffsets().add(y);
        return (T) this;
    }

    default T addMajorModuleKey(String majorModuleKey, int x, int y) {
        return addMajorModuleKey(majorModuleKey, x, y, false);
    }

    default T addMinorModuleKey(String minorModuleKey, int x, int y) {
        getMinorModuleKeys().add(minorModuleKey);
        getMinorOffsets().add(x);
        getMinorOffsets().add(y);
        return (T) this;
    }

    T honeBase(int honeBase);

    T setSlotType(EquipmentSlot slotType);

    T setAttributeMode(BasicTetraModular.AttributeMode mode);

    T honeMultiplier(int honeMultiplier);

    List<String> getMajorModuleKeys();

    List<String> getMinorModuleKeys();

    List<String> getRequiredModules();

    List<Integer> getMajorOffsets();

    List<Integer> getMinorOffsets();

    String getIdentifier();

    int getHoneMultiplier();
    int getHoneBase();
    BasicTetraModular.AttributeMode getAttributeMode();
    EquipmentSlot getSlotType();

    Item createObject();
}
