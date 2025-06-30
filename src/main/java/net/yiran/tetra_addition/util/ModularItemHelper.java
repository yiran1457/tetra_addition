package net.yiran.tetra_addition.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.module.ItemModule;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ModularItemHelper {
    public static ModularItemHelper INSTANCE = new ModularItemHelper();

    private ModularItem is(ItemStack item) {
        if (item.getItem() instanceof ModularItem modularItem) {
            return modularItem;
        }
        throw new IllegalArgumentException("item is not a ModularItem");
    }

    public Collection<ItemModule> getAllModules(ItemStack item) {
        return is(item).getAllModules(item);
    }

    public int getMagicCapacity(ItemModule module, ItemStack item) {
        return module.getMagicCapacity(item);
    }

    public Map<String, Integer> getAllMagicCapacity(ItemStack item) {
        Map<String, Integer> map = new HashMap<>();
        for (ItemModule module : getAllModules(item)) {
            map.put(module.getSlot(), getMagicCapacity(module, item));
        }
        return map;
    }

    public Map<ToolAction, Integer> getToolLevels(ItemStack item) {
        return is(item).getToolLevels(item);
    }

    public int getToolLevel(ItemStack item, ToolAction toolAction) {
        return getToolLevels(item).getOrDefault(toolAction, 0);
    }

    public Map<ItemEffect, Integer> getArmorEffectsLevel(LivingEntity entity) {
        Map<ItemEffect, Integer> map = new HashMap<>();
        for (ItemStack itemStack : entity.getArmorSlots()) {
            if (itemStack.getItem() instanceof ModularItem modularItem) {
                for (ItemEffect effect : modularItem.getEffects(itemStack)) {
                    map.merge(effect, modularItem.getEffectLevel(itemStack, effect), ArmorEffectLevelMath.getMath(effect));
                }
            }
        }
        return map;
    }

    public static class ArmorEffectLevelMath {
        public static Map<ItemEffect, BiFunction<Integer, Integer, Integer>> MATHS = new HashMap<>();
        public static BiFunction<Integer, Integer, Integer> DEFAULT = Integer::sum;

        public static BiFunction<Integer, Integer, Integer> getMath(ItemEffect effect) {
            return MATHS.getOrDefault(effect, DEFAULT);
        }

        public static void registerMath(ItemEffect effect, BiFunction<Integer, Integer, Integer> function) {
            MATHS.put(effect, function);
        }
    }

}
