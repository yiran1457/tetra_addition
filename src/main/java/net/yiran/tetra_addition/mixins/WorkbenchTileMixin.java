package net.yiran.tetra_addition.mixins;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolAction;
import net.yiran.tetra_addition.WorkbenchTileCraftEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.items.modular.IModularItem;

import java.util.Map;

@Mixin(WorkbenchTile.class)
public class WorkbenchTileMixin {
    @Inject(at = @At(value = "RETURN"),method = "craft",remap = false,locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void craft(
            Player player,
                      CallbackInfo ci,
                      ItemStack targetStack,
                      ItemStack upgradedStack, // 这里捕获最终的 upgradedStack
                      IModularItem item,
                      BlockState blockState,
                      Map<ToolAction, Integer> availableTools,
                      ItemStack[] materials,
                      ItemStack[] materialsAltered
    ) {
        MinecraftForge.EVENT_BUS.post(new WorkbenchTileCraftEvent(upgradedStack));
        if(!player.level().isClientSide) {
            player.sendSystemMessage(Component.literal(upgradedStack.toString()));
        }
    }
}
