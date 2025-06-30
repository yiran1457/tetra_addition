package net.yiran.tetra_addition.item.test;

import mods.flammpfeil.slashblade.client.renderer.model.BladeModel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.yiran.tetra_addition.TetraAddition;

import static mods.flammpfeil.slashblade.client.ClientHandler.bakeBlade;

@Mod.EventBusSubscriber(value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.MOD,modid = TetraAddition.MODID)
public class SBClientHandler {


    @SubscribeEvent
    public static void doClientStuff(FMLClientSetupEvent event) {
        ItemProperties.register(TetraAddition.SB_ITEM.get(),
                new ResourceLocation("slashblade:user"),
                (stack, clientLevel, livingEntity, i) -> {
                    BladeModel.user = livingEntity;
                    return 0;
                });
    }

    @SubscribeEvent
    public static void Baked(ModelEvent.ModifyBakingResult event) {
        bakeBlade(TetraAddition.SB_ITEM.get(), event);
    }
}
