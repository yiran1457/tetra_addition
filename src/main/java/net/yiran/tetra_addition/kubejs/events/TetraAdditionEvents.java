package net.yiran.tetra_addition.kubejs.events;

import net.yiran.modernkubeevent.core.IModernEventGroup;
import net.yiran.modernkubeevent.core.IModernEventHandler;
import net.yiran.tetra_addition.events.WorkbenchTileCraftEvent;

public interface TetraAdditionEvents {
    IModernEventGroup GROUP = IModernEventGroup.of("TetraAdditionEvents");
    IModernEventHandler CRAFT = GROUP.modernServer("craft",()-> WorkbenchTileCraftEvent.class);
}
