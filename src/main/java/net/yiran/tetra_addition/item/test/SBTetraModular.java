package net.yiran.tetra_addition.item.test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.*;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.item.ReachModifier;
import mods.flammpfeil.slashblade.item.SwordType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.Tooltips;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.event.ModularItemDamageEvent;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.InitializableItem;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.data.EffectData;
import se.mickelus.tetra.module.data.ItemProperties;
import se.mickelus.tetra.module.data.SynergyData;
import se.mickelus.tetra.module.data.ToolData;
import se.mickelus.tetra.module.schematic.RepairSchematic;
import se.mickelus.tetra.properties.AttributeHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class SBTetraModular extends ItemSlashBlade implements IModularItem, InitializableItem {

    public static final UUID attackDamageModifier;
    public static final UUID attackSpeedModifier;
    private static final Logger logger;
    private final Cache<String, Multimap<Attribute, AttributeModifier>> attributeCache;
    private final Cache<String, ToolData> toolCache;
    private final Cache<String, EffectData> effectCache;
    private final Cache<String, ItemProperties> propertyCache;
    protected int honeBase;
    protected int honeIntegrityMultiplier;
    protected boolean canHone;
    protected String[] majorModuleKeys;
    protected String[] minorModuleKeys;
    protected String[] requiredModules;
    protected int baseDurability;
    protected int baseIntegrity;
    protected SynergyData[] synergies;


    public SBTetraModular(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, 0, 0, new Properties().stacksTo(1));
        this.attributeCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
        this.toolCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
        this.effectCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
        this.propertyCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
        this.honeBase = 450;
        this.honeIntegrityMultiplier = 200;
        this.canHone = true;
        this.requiredModules = new String[0];
        this.baseDurability = 0;
        this.baseIntegrity = 0;
        this.synergies = new SynergyData[0];
        DataManager.instance.moduleData.onReload(this::clearCaches);
        this.majorModuleKeys = new String[]{"sb/handle", "sb/blade"};//刀柄 刀条
        this.minorModuleKeys = new String[]{"sb/tsuba","sb/scabbard"};//刀镡 刀鞘
        this.requiredModules = new String[]{"sb/handle", "sb/blade","sb/tsuba","sb/scabbard"};
        this.updateConfig( ConfigHandler.honeBowBase.get(),ConfigHandler.honeBowIntegrityMultiplier.get());
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, "sb_test"));
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiersCached(ItemStack itemStack) {
        try {
             Multimap<Attribute, AttributeModifier> SB = super.getAttributeModifiers(EquipmentSlot.MAINHAND, itemStack);
            return  this.getAttributeModifierCache().get(this.getDataCacheKey(itemStack), () -> Optional.ofNullable(this.getAttributeModifiersCollapsed(itemStack)).orElseGet(ArrayListMultimap::create));
        } catch (ExecutionException e) {
            e.printStackTrace();
            return this.getAttributeModifiersCollapsed(itemStack);
        }
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {/*
        Multimap<Attribute, AttributeModifier> multimap;
        if (this.isBroken(itemStack)) {
            multimap = ArrayListMultimap.create();
        } else if (slot == EquipmentSlot.MAINHAND) {
            multimap = this.getAttributeModifiersCached(itemStack);
        } else {
            multimap = slot == EquipmentSlot.OFFHAND ?
                    this.getAttributeModifiersCached(itemStack)
                            .entries()
                            .stream()
                            .filter((entry) -> entry.getKey().equals(Attributes.ARMOR) || entry.getKey().equals(Attributes.ARMOR_TOUGHNESS))
                            .collect(Multimaps.toMultimap(Map.Entry::getKey, Map.Entry::getValue, ArrayListMultimap::create))
                    :
                    ArrayListMultimap.create();
        }
        Multimap<Attribute, AttributeModifier> result =ArrayListMultimap.create();
        result.putAll(multimap);
        Multimap<Attribute, AttributeModifier> def = super.getAttributeModifiers(slot, itemStack);
        result.putAll(Attributes.ATTACK_DAMAGE, def.get(Attributes.ATTACK_DAMAGE));
        result.putAll(Attributes.ATTACK_SPEED, def.get(Attributes.ATTACK_SPEED));*/
        Multimap<Attribute, AttributeModifier> result = ArrayListMultimap.create();

        if (this.isBroken(itemStack)) {
        } else if (slot == EquipmentSlot.MAINHAND) {
            //Multimap<Attribute, AttributeModifier> SB = super.getAttributeModifiers(EquipmentSlot.MAINHAND, itemStack);
            Multimap<Attribute, AttributeModifier> Tetra = this.getAttributeModifiersCached(itemStack);
            //result = merge(SB, Tetra);
            result.putAll(Tetra);
            LazyOptional<ISlashBladeState> state = itemStack.getCapability(BLADESTATE);
            state.ifPresent((s) -> {
                EnumSet<SwordType> swordType = SwordType.from(itemStack);
                float baseAttackModifier = s.getBaseAttackModifier();
                int refine = s.getRefine();
                float attackAmplifier = s.getAttackAmplifier();
                if (s.isBroken()) {
                    attackAmplifier = -0.5F - baseAttackModifier;
                } else {
                    float refineFactor = swordType.contains(SwordType.FIERCEREDGE) ? 0.1F : 0.05F;
                    attackAmplifier = (1.0F - 1.0F / (1.0F + refineFactor * (float) refine)) * baseAttackModifier;
                }

                double damage = (double) baseAttackModifier + (double) attackAmplifier - (double) 4.0F;
                SlashBladeEvent.UpdateAttackEvent event = new SlashBladeEvent.UpdateAttackEvent(itemStack, s, damage);
                MinecraftForge.EVENT_BUS.post(event);
                AttributeModifier attack = new AttributeModifier(UUID.nameUUIDFromBytes(new byte[]{127, 0, 0, 11, 45, 14}), "Weapon modifier", event.getNewDamage(), AttributeModifier.Operation.ADDITION);
                //result.remove(Attributes.ATTACK_DAMAGE, attack);
                result.put(Attributes.ATTACK_DAMAGE, attack);
                result.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(UUID.nameUUIDFromBytes(new byte[]{127, 1, 0, 11, 45, 14}), "Reach amplifer", s.isBroken() ? ReachModifier.BrokendReach() : ReachModifier.BladeReach(), AttributeModifier.Operation.ADDITION));
            });
        }
        return result;
    }

    public static @NotNull Multimap<Attribute, AttributeModifier> merge(
            Multimap<Attribute, AttributeModifier> map1,
            @NotNull Multimap<Attribute, AttributeModifier> map2) {

        Multimap<Attribute, AttributeModifier> merged = ArrayListMultimap.create();
        merged.putAll(map1);

        // 遍历 map2 中的每个 Attribute 键
        for (Map.Entry<Attribute, Collection<AttributeModifier>> entry : map2.asMap().entrySet()) {
            Attribute attribute = entry.getKey();
            Collection<AttributeModifier> modifiersToAdd = entry.getValue();

            // 收集当前 Attribute 对应的所有 AttributeModifier
            Map<UUID, AttributeModifier> existingMap = new HashMap<>();
            for (AttributeModifier mod : merged.get(attribute)) {
                existingMap.put(mod.getId(), mod);
            }

            // 处理 map2 中的每个 AttributeModifier
            for (AttributeModifier modToAdd : modifiersToAdd) {
                UUID uuid = modToAdd.getId();
                if (existingMap.containsKey(uuid)) {
                    // UUID 相同，合并 amount
                    AttributeModifier existing = existingMap.get(uuid);
                    double newAmount = existing.getAmount() + modToAdd.getAmount();
                    AttributeModifier newMod = new AttributeModifier(
                            uuid,
                            existing.getName(),
                            newAmount,
                            existing.getOperation()
                    );
                    existingMap.put(uuid, newMod);
                } else {
                    // UUID 不同，直接添加
                    existingMap.put(uuid, modToAdd);
                }
            }

            // 替换 merged 中该 Attribute 的所有 Modifier
            merged.get(attribute).clear();
            merged.get(attribute).addAll(existingMap.values());
        }

        return merged;
    }

    @Override
    public GuiModuleOffsets getMajorGuiOffsets(ItemStack itemStack) {
        return new GuiModuleOffsets(4, 0-4, 4, 18+2);
    }

    @Override
    public GuiModuleOffsets getMinorGuiOffsets(ItemStack itemStack) {
        return new GuiModuleOffsets(-18, 0, -18, 18+6);
    }
/*
    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        //super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {

    }
*/
    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }

    public void clearCaches() {
        logger.debug("Clearing item data caches for {}...", this.toString());
        this.attributeCache.invalidateAll();
        this.toolCache.invalidateAll();
        this.effectCache.invalidateAll();
        this.propertyCache.invalidateAll();
    }

    public String[] getMajorModuleKeys(ItemStack itemStack) {
        return this.majorModuleKeys;
    }

    public String[] getMinorModuleKeys(ItemStack itemStack) {
        return this.minorModuleKeys;
    }

    public String[] getRequiredModules(ItemStack itemStack) {
        return this.requiredModules;
    }

    public int getHoneBase(ItemStack itemStack) {
        return this.honeBase;
    }

    public int getHoneIntegrityMultiplier(ItemStack itemStack) {
        return this.honeIntegrityMultiplier;
    }

    public boolean canGainHoneProgress(ItemStack itemStack) {
        return this.canHone;
    }

    public Cache<String, Multimap<Attribute, AttributeModifier>> getAttributeModifierCache() {
        return this.attributeCache;
    }

    public Cache<String, EffectData> getEffectDataCache() {
        return this.effectCache;
    }

    public Cache<String, ItemProperties> getPropertyCache() {
        return this.propertyCache;
    }

    public Cache<String, ToolData> getToolDataCache() {
        return this.toolCache;
    }

    public Item getItem() {
        return this;
    }
    public Component getName(ItemStack stack) {
        return Component.literal(this.getItemName(stack));
    }


    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.addAll(this.getTooltip(stack, world, flag));
    }
    public @NotNull Rarity getRarity(@NotNull ItemStack itemStack) {
        return Optional.ofNullable(this.getPropertiesCached(itemStack)).map((props) -> props.rarity).orElse(super.getRarity(itemStack));
    }

    public int getDamage(ItemStack stack) {
        //return stack.getOrCreateTagElement("bladeState").getInt("Damage");
        return stack.getCapability(BLADESTATE).map((s) -> s.getDamage()).orElse(0);
    }

    public int getMaxDamage(ItemStack stack) {
        return stack.getCapability(BLADESTATE).map((s) -> {
            s.setMaxDamage(Optional.of(this.getPropertiesCached(stack)).map((properties) -> (float) (properties.durability + this.baseDurability) * properties.durabilityMultiplier).map(Math::round).orElse(0));
            return s.getMaxDamage();
        }).orElse(super.getMaxDamage(stack));
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        ModularItemDamageEvent event = new ModularItemDamageEvent(entity, stack, amount);
        MinecraftForge.EVENT_BUS.post(event);
        amount = event.getAmount();
        amount = super.damageItem(stack, amount, entity, onBroken);
        this.applyUsageEffects(entity,stack,amount);
        return amount;
    }

    public void onCraftedBy(ItemStack itemStack, Level world, Player player) {
        IModularItem.updateIdentifier(itemStack);
    }

    public boolean isFoil(@Nonnull ItemStack itemStack) {
        return ConfigHandler.enableGlint.get() ? super.isFoil(itemStack) : false;
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    public SynergyData[] getAllSynergyData(ItemStack itemStack) {
        return this.synergies;
    }

    static {
        attackDamageModifier = Item.BASE_ATTACK_DAMAGE_UUID;
        attackSpeedModifier = Item.BASE_ATTACK_SPEED_UUID;
        logger = LogManager.getLogger();
    }

    public boolean isBookEnchantable(ItemStack itemStack, ItemStack bookStack) {
        return false;
    }

    public boolean canApplyAtEnchantingTable(ItemStack itemStack, Enchantment enchantment) {
        return this.acceptsEnchantment(itemStack, enchantment, true);
    }

    public int getEnchantmentValue(ItemStack itemStack) {
        return this.getEnchantability(itemStack);
    }

    @Override
    public boolean isBroken(ItemStack itemStack) {
        return itemStack.getOrCreateTagElement("bladeState").getBoolean("isBroken");
    }

    public boolean isBroken(int damage, int maxDamage) {
        return damage == maxDamage;
    }


    public boolean canProvideTools(ItemStack itemStack) {
        return !this.isBroken(itemStack);
    }
/*
    public ToolData getToolData(ItemStack itemStack) {
        try {
            return (ToolData) this.getToolDataCache().get(this.getDataCacheKey(itemStack), () -> (ToolData) Optional.ofNullable(this.getToolDataRaw(itemStack)).orElseGet(ToolData::new));
        } catch (ExecutionException e) {
            e.printStackTrace();
            return (ToolData) Optional.ofNullable(this.getToolDataRaw(itemStack)).orElseGet(ToolData::new);
        }
    }
*/
    /*
    protected ToolData getToolDataRaw(ItemStack itemStack) {
        logger.debug("Gathering tool data for {} ({})", this.getName(itemStack).getString(), this.getDataCacheKey(itemStack));
        return (ToolData) Stream.concat(this.getAllModules(itemStack).stream().map((module) -> module.getToolData(itemStack)), Arrays.stream(this.getSynergyData(itemStack)).map((synergy) -> synergy.tools)).filter(Objects::nonNull).reduce((ToolData) null, ToolData::merge);
    }
*/

}
