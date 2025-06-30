package net.yiran.tetra_addition;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.yiran.tetra_addition.item.test.SBTetraModular;
import net.yiran.tetra_addition.item.test.TestItem;
import net.yiran.tetra_addition.kubejs.KubeJSHandler;

@Mod(TetraAddition.MODID)
public class TetraAddition {
    public static final String MODID = "tetra_addition";
    public IEventBus modEventBus;

    public static DeferredRegister<Item> ITEM = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static RegistryObject<SBTetraModular> SB_ITEM = ITEM.register("sb_test", () -> new SBTetraModular(Tiers.IRON, 10, 2, new Item.Properties().stacksTo(1)));
    public static RegistryObject<Item> IIITEM = ITEM.register("ii_test", () -> new TestItem(new Item.Properties().stacksTo(1)));

    public TetraAddition() {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEM.register(modEventBus);
        if (ModList.get().isLoaded("kubejs")) {
            KubeJSHandler.addEventListeners();
        }
    }
}
