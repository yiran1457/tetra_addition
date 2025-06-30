package net.yiran.tetra_addition.kubejs;

import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.effect.ItemEffect;

public interface TetraAdditionWrappers {
    static ItemEffect toItemEffect(Object o) {
        if (o instanceof ItemEffect itemEffect)
            return itemEffect;
        if(o instanceof String s)
            return ItemEffect.get(s);
        return null;
    }
    static ToolAction toToolAction(Object o) {
        if (o instanceof ToolAction toolAction)
            return toolAction;
        if(o instanceof String s)
            return ToolAction.get(s);
        return null;
    }
}
