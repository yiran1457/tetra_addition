package net.yiran.tetra_addition.mixins;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.yiran.tetra_addition.WorkbenchTileCraftEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

import java.util.Map;

@Mixin(value = WorkbenchTile.class,remap = false)
public class WorkbenchTileMixin {
    @Shadow
    private String currentSlot;

    @Shadow
    private UpgradeSchematic currentSchematic;

    @Shadow @Final private LazyOptional<ItemStackHandler> handler;

    @Inject(
            at = @At(value = "INVOKE", target = "Lse/mickelus/tetra/blocks/workbench/WorkbenchTile;clearSchematic()V"),
            method = "craft",
            remap = false,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void craft(
            Player player,
            CallbackInfo ci,
            ItemStack targetStack,
            ItemStack upgradedStack,
            IModularItem item,
            BlockState blockState,
            Map<ToolAction, Integer> availableTools,
            ItemStack[] materials,
            ItemStack[] materialsAltered
    ) {
        var event = new WorkbenchTileCraftEvent(
                targetStack,
                upgradedStack,
                player,
                (WorkbenchTile) (Object) this,
                materials,
                materialsAltered,
                currentSchematic,
                currentSlot
        );
        MinecraftForge.EVENT_BUS.post(event);

        this.handler.ifPresent((handler) -> {
            handler.setStackInSlot(0, event.getUpgradedStack());
        });
    }
}
