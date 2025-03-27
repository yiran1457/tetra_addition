package net.yiran.tetra_addition.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.yiran.tetra_addition.MyTetraUtil;
import net.yiran.tetra_addition.WorkbenchTileCraftEvent;
import se.mickelus.tetra.effect.ItemEffect;

public class TetraAdditionPlugin extends KubeJSPlugin {
    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("MyTetraUtil", MyTetraUtil.instance);
        event.add("ItemEffect", ItemEffect.class);
        event.add("$WorkbenchTileCraftEvent", WorkbenchTileCraftEvent.class);
    }
}
