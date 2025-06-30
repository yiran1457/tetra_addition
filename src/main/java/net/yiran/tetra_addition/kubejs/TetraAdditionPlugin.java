package net.yiran.tetra_addition.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraftforge.common.ToolAction;
import net.yiran.tetra_addition.kubejs.events.TetraAdditionEvents;
import net.yiran.tetra_addition.kubejs.item.BasicTetraBuilder;
import net.yiran.tetra_addition.util.ModularItemHelper;
import net.yiran.tetra_addition.util.MyTetraUtil;
import net.yiran.tetra_addition.events.WorkbenchTileCraftEvent;
import se.mickelus.tetra.effect.ItemEffect;

public class TetraAdditionPlugin extends KubeJSPlugin {
    public void registerEvents() {
        TetraAdditionEvents.GROUP.register();
    }

    public void init() {
        RegistryInfo.ITEM.addType("basic_tetra", BasicTetraBuilder.class,BasicTetraBuilder::new);
    }

    public void registerBindings(BindingsEvent event) {
        event.add("MyTetraUtil", MyTetraUtil.instance);
        event.add("ItemEffect", ItemEffect.class);
        event.add("$WorkbenchTileCraftEvent", WorkbenchTileCraftEvent.class);
        event.add("ModularItemHelper", ModularItemHelper.INSTANCE);
        event.add("ArmorEffectLevelMath", ModularItemHelper.ArmorEffectLevelMath.class);
    }

    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.registerSimple(ItemEffect.class, TetraAdditionWrappers::toItemEffect);
        typeWrappers.registerSimple(ToolAction.class, TetraAdditionWrappers::toToolAction);
    }
}
