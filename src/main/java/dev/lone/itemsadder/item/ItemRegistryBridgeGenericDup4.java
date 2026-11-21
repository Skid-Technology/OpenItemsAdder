package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.nbt.NCompound;
import beer.devs.fastnbt.nms.nbt.NItem;
import com.google.common.collect.HashMultimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.serialization.Dynamic;
import dev.lone.itemsadder.config.ParsedFloat;
import dev.lone.itemsadder.config.ParsedList;
import dev.lone.itemsadder.config.ParsedSet;
import dev.lone.itemsadder.config.ParsedString;
import dev.lone.itemsadder.config.ParsedValue;
import dev.lone.itemsadder.config.ParsedValueEmpty;
import dev.lone.itemsadder.item.conversion.ItemConversionNodeN;
import dev.lone.itemsadder.item.conversion.ItemDataFixN;
import dev.lone.itemsadder.nms.ContainerClickAdapter;
import dev.lone.itemsadder.nms.ContainerClickAdapterImplF;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AdventureModePredicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.item.component.ItemAttributeModifiers.Entry;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.PlaySoundConsumeEffect;
import net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.equipment.Equippable.Builder;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftEntityType;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftContainer;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R7.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_21_R7.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_21_R7.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemRegistryBridgeGenericDup4 implements ItemRegistryBridge {
   private static final ContainerClickAdapter aZO = new ContainerClickAdapterImplF();
   public static final Field aZP = FieldUtils.getField(CraftItemStack.class, "handle", true);
   private static final boolean aZQ = Modifier.isPublic(aZP.getModifiers());
   static double aZf = Player.createAttributes().build().getBaseValue(Attributes.ATTACK_SPEED);

   @Override
   public ContainerClickAdapter OH() {
      return aZO;
   }

   public static ItemStack method3474(CraftItemStack var0) {
      if (aZQ) {
         return var0.handle;
      }

      try {
         return (ItemStack)aZP.get(var0);
      } catch (IllegalAccessException var2) {
         new RuntimeException("Error reading handle field of CraftItemStack.", var2).printStackTrace();
         return null;
      }
   }

   @Override
   public void method3426(org.bukkit.entity.Player player, Component component) {
      ServerPlayer var3 = ((CraftPlayer)player).getHandle();
      ClientboundOpenScreenPacket var4 = new ClientboundOpenScreenPacket(
         var3.containerMenu.containerId,
         CraftContainer.getNotchInventoryType(player.getOpenInventory().getTopInventory()),
         (net.minecraft.network.chat.Component)AdventureText.toNative(component)
      );
      var3.connection.send(var4);
      player.updateInventory();
   }

   @Override
   public int cx(org.bukkit.entity.Player player) {
      ServerPlayer var2 = ((CraftPlayer)player).getHandle();
      return var2.containerMenu.containerId;
   }

   @Override
   public String method3427(Material bukkitMaterial) {
      return CraftMagicNumbers.getItem(bukkitMaterial).getDescriptionId();
   }

   public static boolean method3444(ItemStack var0) {
      CustomData var1 = (CustomData)var0.get(DataComponents.CUSTOM_DATA);
      return var1 != null && var1.contains("itemsadder");
   }

   @Override
   public void method3428(org.bukkit.inventory.ItemStack bukkitItem, String skinString) {
      ItemStack var3 = CraftItemStack.asNMSCopy(bukkitItem);
      HashMultimap var4 = HashMultimap.create();
      var4.put("textures", new Property("textures", skinString, null));
      PropertyMap var5 = new PropertyMap(var4);
      ResolvableProfile var6 = ResolvableProfile.createResolved(new GameProfile(UUID.randomUUID(), "", var5));
      var3.set(DataComponents.PROFILE, var6);
      bukkitItem.setItemMeta(CraftItemStack.getItemMeta(var3));
   }

   @Override
   public Double ah(org.bukkit.inventory.ItemStack bukkitItem) {
      return this.method3475(bukkitItem, Item.BASE_ATTACK_SPEED_ID);
   }

   public Double method3475(org.bukkit.inventory.ItemStack var1, Identifier var2) {
      ItemStack var3 = CraftItemStack.asNMSCopy(var1);
      ItemAttributeModifiers var4 = (ItemAttributeModifiers)var3.getItem().components().get(DataComponents.ATTRIBUTE_MODIFIERS);
      if (var4 != null) {
         for (Entry var6 : var4.modifiers()) {
            if (var6.modifier().id().equals(var2)) {
               return aZf + var6.modifier().amount();
            }
         }
      }

      return null;
   }

   @Override
   public void method3429(NItem nItem, String nbt) {
      String var3 = CraftMagicNumbers.getItem(nItem.getItem().getType()).builtInRegistryHolder().key().identifier().toString();
      CompoundTag var4 = TagParser.parseCompoundFully(nbt);
      if (var4.contains("components")) {
         var4.putString("id", var3);
         var4.putInt("count", nItem.getItem().getAmount());
         NCompound var5 = new NCompound(var4);
         nItem.merge(var5.toBukkitItem());
         nItem.save();
      } else {
         if (!var4.contains("tag")) {
            CompoundTag var8 = new CompoundTag();

            for (String var7 : var4.keySet()) {
               var8.put(var7, var4.get(var7));
               var4.remove(var7);
            }

            var4.put("tag", var8);
         }

         var4.putString("id", var3);
         var4.putInt("Count", nItem.getItem().getAmount());
         Dynamic var9 = DataFixers.getDataFixer().update(References.ITEM_STACK, new Dynamic(NbtOps.INSTANCE, var4), 3692, 3816);
         ItemConversionNodeN var11 = (ItemConversionNodeN)ItemConversionNodeN.method4464(var9).get();
         ItemDataFixN.method4414(var11, var9);
         var9 = var11.OR();
         NCompound var12 = new NCompound(var9.getValue());
         nItem.merge(var12.toBukkitItem());
         nItem.save();
      }
   }

   @Override
   public org.bukkit.inventory.ItemStack ai(org.bukkit.inventory.ItemStack bukkitItem) {
      ItemStack var2 = CraftItemStack.asNMSCopy(bukkitItem);
      var2.setDamageValue(0);
      TooltipDisplay var3 = TooltipDisplay.DEFAULT;
      DataComponentMap var4 = var2.getComponents();
      ItemAttributeModifiers var5 = (ItemAttributeModifiers)var4.get(DataComponents.ATTRIBUTE_MODIFIERS);
      if (var5 != null) {
         var3 = var3.withHidden(DataComponents.ATTRIBUTE_MODIFIERS, true);
      }

      DyedItemColor var6 = (DyedItemColor)var4.get(DataComponents.DYED_COLOR);
      if (var6 != null) {
         var3 = var3.withHidden(DataComponents.DYED_COLOR, true);
      }

      ArmorTrim var7 = (ArmorTrim)var4.get(DataComponents.TRIM);
      if (var7 != null) {
         var3 = var3.withHidden(DataComponents.TRIM, true);
      }

      Unit var8 = (Unit)var4.get(DataComponents.UNBREAKABLE);
      if (var8 != null) {
         var3 = var3.withHidden(DataComponents.UNBREAKABLE, true);
      }

      AdventureModePredicate var9 = (AdventureModePredicate)var4.get(DataComponents.CAN_BREAK);
      if (var9 != null) {
         var3 = var3.withHidden(DataComponents.CAN_BREAK, true);
      }

      var2.set(DataComponents.LORE, null);
      var2.set(DataComponents.TOOLTIP_DISPLAY, var3);
      bukkitItem.setItemMeta(CraftItemStack.getItemMeta(var2));
      return bukkitItem;
   }

   public static boolean method3446(ItemStack var0, ItemStack var1) {
      CustomData var2 = (CustomData)var0.get(DataComponents.CUSTOM_DATA);
      CustomData var3 = (CustomData)var1.get(DataComponents.CUSTOM_DATA);
      if (var2 == null && var3 == null) {
         return true;
      } else if (var2 != null && var3 != null) {
         return var2.copyTag().get("itemsadder") instanceof CompoundTag var4 && var3.copyTag().get("itemsadder") instanceof CompoundTag var5
            ? var4.getString("id").equals(var5.getString("id")) && var4.getString("namespace").equals(var5.getString("namespace"))
            : false;
      } else {
         return false;
      }
   }

   @Override
   public void method3430(@NotNull ItemStack from, @NotNull ItemStack to) {
      ItemStack var3 = CraftItemStack.asNMSCopy(to);
      ItemStack var4 = CraftItemStack.asNMSCopy(from);
      if (var3 == null) {
         throw new NullPointerException("ItemStack toNms is null.");
      }

      if (var4 == null) {
         throw new NullPointerException("ItemStack fromNms is null.");
      }

      CustomData var5 = (CustomData)var4.get(DataComponents.CUSTOM_DATA);
      if (var5 != null) {
         CustomData.update(DataComponents.CUSTOM_DATA, var3, var1x -> var1x.merge(var5.copyTag()));
      }

      to.setItemMeta(CraftItemStack.getItemMeta(var3));
   }

   @Override
   public void method3431(org.bukkit.inventory.ItemStack bukkitItemStack, float seconds, String group) {
      if (group != null && !group.contains(":")) {
         group = "itemsadder:" + group;
      }

      Optional var4 = group == null ? Optional.empty() : Optional.of(Identifier.parse(group));
      ItemStack var5 = CraftItemStack.asNMSCopy(bukkitItemStack);
      var5.set(DataComponents.USE_COOLDOWN, new UseCooldown(seconds, var4));
      bukkitItemStack.setItemMeta(CraftItemStack.getItemMeta(var5));
   }

   @Override
   public boolean aj(org.bukkit.inventory.ItemStack bukkitItemStack) {
      ItemStack var2 = CraftItemStack.asNMSCopy(bukkitItemStack);
      var2.remove(DataComponents.USE_REMAINDER);
      bukkitItemStack.setItemMeta(CraftItemStack.getItemMeta(var2));
      return true;
   }

   @Override
   public void method3432(
      org.bukkit.inventory.ItemStack bukkitItemStack,
      int nutrition,
      float saturation,
      boolean canAlwaysEat,
      int consumeSeconds,
      @Nullable String animation,
      @Nullable String sound,
      @Nullable Boolean hasConsumeParticles,
      @Nullable List wrappedConsumeEffects
   ) {
      ItemStack var10 = CraftItemStack.asNMSCopy(bukkitItemStack);
      Object var11;
      if (sound != null) {
         var11 = Holder.direct(SoundEvent.createVariableRangeEvent(Identifier.parse(sound)));
      } else {
         var11 = SoundEvents.GENERIC_EAT;
      }

      ItemUseAnimation var12 = ItemUseAnimation.EAT;
      if (animation != null) {
         for (ItemUseAnimation var16 : ItemUseAnimation.values()) {
            if (var16.getSerializedName().equals(animation)) {
               var12 = var16;
               break;
            }
         }
      }

      if (hasConsumeParticles == null) {
         hasConsumeParticles = true;
      }

      ArrayList var21 = new ArrayList();
      if (wrappedConsumeEffects != null) {
         for (ParsedValue var23 : wrappedConsumeEffects) {
            if (var23 instanceof ParsedList var24) {
               List var20 = var24.aYQ
                  .stream()
                  .map(
                     var0 -> new MobEffectInstance(
                        CraftPotionEffectType.bukkitToMinecraftHolder(var0.type()),
                        var0.duration(),
                        var0.amplifier(),
                        var0.ambient(),
                        var0.showParticles(),
                        var0.showIcon()
                     )
                  )
                  .toList();
               var21.add(new ApplyStatusEffectsConsumeEffect(var20, var24.aYR));
            } else if (var23 instanceof ParsedSet var17) {
               List var25 = var17.aYZ.stream().map(CraftPotionEffectType::bukkitToMinecraftHolder).collect(Collectors.toList());
               var21.add(new RemoveStatusEffectsConsumeEffect(HolderSet.direct(var25)));
            } else if (var23 instanceof ParsedFloat var18) {
               var21.add(new TeleportRandomlyConsumeEffect(var18.aZa, true));
            } else if (var23 instanceof ParsedValueEmpty) {
               var21.add(new ClearAllStatusEffectsConsumeEffect());
            } else if (var23 instanceof ParsedString var19) {
               var21.add(new PlaySoundConsumeEffect(Holder.direct(SoundEvent.createVariableRangeEvent(Identifier.parse(var19.aYY)))));
            }
         }
      }

      var10.set(DataComponents.CONSUMABLE, new Consumable(consumeSeconds, var12, (Holder)var11, hasConsumeParticles, var21));
      var10.set(DataComponents.FOOD, new FoodProperties(nutrition, saturation, canAlwaysEat));
      bukkitItemStack.setItemMeta(CraftItemStack.getItemMeta(var10));
   }

   @Override
   public boolean ak(org.bukkit.inventory.ItemStack itemStack) {
      return CraftItemStack.asNMSCopy(itemStack).has(DataComponents.CONSUMABLE);
   }

   @Override
   public void method3433(
      org.bukkit.inventory.ItemStack bukkitItemStack,
      EquipmentSlot bukkitEquipmentSlot,
      @Nullable NamespacedKey bukkitAssetId,
      @Nullable String equipSoundName,
      @Nullable NamespacedKey bukkitCameraOverlay,
      @Nullable List allowedEntitiesBukkit,
      @Nullable Boolean dispensable,
      @Nullable Boolean swappable,
      @Nullable Boolean damageOnHurt,
      @Nullable Boolean glider
   ) {
      net.minecraft.world.entity.EquipmentSlot var11 = CraftEquipmentSlot.getNMS(bukkitEquipmentSlot);
      Holder var12 = null;
      if (equipSoundName != null) {
         var12 = Holder.direct(SoundEvent.createVariableRangeEvent(Identifier.parse(equipSoundName)));
      }

      Builder var13 = Equippable.builder(var11);
      if (var12 != null) {
         var13.setEquipSound(var12);
      }

      if (bukkitAssetId != null) {
         var13.setAsset(ResourceKey.create(EquipmentAssets.ROOT_ID, CraftNamespacedKey.toMinecraft(bukkitAssetId)));
      }

      if (bukkitCameraOverlay != null) {
         var13.setCameraOverlay(CraftNamespacedKey.toMinecraft(bukkitCameraOverlay));
      }

      if (allowedEntitiesBukkit != null && !allowedEntitiesBukkit.isEmpty()) {
         var13.setAllowedEntities(
            HolderSet.direct(allowedEntitiesBukkit.stream().filter(Objects::nonNull).map(CraftEntityType::bukkitToMinecraftHolder).toList())
         );
      }

      if (dispensable != null) {
         var13.setDispensable(dispensable);
      }

      if (swappable != null) {
         var13.setSwappable(swappable);
      }

      if (damageOnHurt != null) {
         var13.setDamageOnHurt(damageOnHurt);
      }

      ItemStack var14 = CraftItemStack.asNMSCopy(bukkitItemStack);
      var14.set(DataComponents.EQUIPPABLE, var13.build());
      if (glider != null && glider) {
         var14.set(DataComponents.GLIDER, Unit.INSTANCE);
      }

      bukkitItemStack.setItemMeta(CraftItemStack.getItemMeta(var14));
   }

   @Override
   public void method3434(org.bukkit.inventory.ItemStack bukkitItemStack, @Nullable NamespacedKey bukkitTooltipStyleId) {
      ItemStack var3 = CraftItemStack.asNMSCopy(bukkitItemStack);
      if (bukkitTooltipStyleId != null) {
         var3.set(DataComponents.TOOLTIP_STYLE, CraftNamespacedKey.toMinecraft(bukkitTooltipStyleId));
      } else {
         var3.remove(DataComponents.TOOLTIP_STYLE);
      }

      bukkitItemStack.setItemMeta(CraftItemStack.getItemMeta(var3));
   }

   @Override
   public void al(org.bukkit.inventory.ItemStack bukkitItem) {
      ItemStack var2 = CraftItemStack.asNMSCopy(bukkitItem);
      var2.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.ATTRIBUTE_MODIFIERS, true));
      bukkitItem.setItemMeta(CraftItemStack.getItemMeta(var2));
   }

   @Override
   public void method3435(org.bukkit.inventory.ItemStack bukkitItemStack, @Nullable NamespacedKey jukeboxSongId) {
      ItemStack var3 = CraftItemStack.asNMSCopy(bukkitItemStack);
      if (jukeboxSongId != null) {
         Registry var4 = CraftRegistry.getMinecraftRegistry(Registries.JUKEBOX_SONG);
         ResourceKey var5 = ResourceKey.create(Registries.JUKEBOX_SONG, CraftNamespacedKey.toMinecraft(jukeboxSongId));
         Optional var6 = var4.get(var5);
         if (var6.isEmpty()) {
            Log.warn("Jukebox song with id '" + jukeboxSongId + "' not found.");
            return;
         }

         JukeboxPlayable var7 = new JukeboxPlayable((Holder)var6.get());
         var3.set(DataComponents.JUKEBOX_PLAYABLE, var7);
      } else {
         var3.remove(DataComponents.JUKEBOX_PLAYABLE);
      }

      bukkitItemStack.setItemMeta(CraftItemStack.getItemMeta(var3));
   }

   @Override
   public boolean method3436(org.bukkit.inventory.ItemStack bukkitItemStackA, org.bukkit.inventory.ItemStack bukkitItemStackB) {
      ItemStack var3 = CraftItemStack.asNMSCopy(bukkitItemStackA);
      ItemStack var4 = CraftItemStack.asNMSCopy(bukkitItemStackB);
      if (method3444(var3) && method3444(var4)) {
         method3485(var3);
         method3485(var4);
         CustomData var5 = (CustomData)var3.get(DataComponents.CUSTOM_DATA);
         CustomData var6 = (CustomData)var4.get(DataComponents.CUSTOM_DATA);
         if (var5 == null && var6 == null) {
            return true;
         } else {
            return var5 != null && var6 != null ? var3.getComponentsPatch().equals(var4.getComponentsPatch()) : false;
         }
      } else {
         return false;
      }
   }

   @Override
   public void method3437(org.bukkit.inventory.ItemStack customItem, String modernItemModel) {
      ItemMeta var3 = customItem.getItemMeta();
      var3.setItemModel(NamespacedKey.fromString(modernItemModel));
      customItem.setItemMeta(var3);
   }

   @Override
   public void method3438(org.bukkit.inventory.ItemStack bukkitItemStack, Color color) {
      ItemMeta var3 = bukkitItemStack.getItemMeta();
      CustomModelDataComponent var4 = var3.getCustomModelDataComponent();
      var4.setColors(List.of(color));
      var3.setCustomModelDataComponent(var4);
      bukkitItemStack.setItemMeta(var3);
   }

   @Override
   public void method3439(org.bukkit.inventory.ItemStack bukkitItemStack, List colors) {
      ItemMeta var3 = bukkitItemStack.getItemMeta();
      CustomModelDataComponent var4 = var3.getCustomModelDataComponent();
      var4.setColors(colors);
      var3.setCustomModelDataComponent(var4);
      bukkitItemStack.setItemMeta(var3);
   }

   @Override
   public void method3440(org.bukkit.inventory.ItemStack bukkitItemStack, List strings) {
      ItemMeta var3 = bukkitItemStack.getItemMeta();
      CustomModelDataComponent var4 = var3.getCustomModelDataComponent();
      var4.setStrings(strings);
      var3.setCustomModelDataComponent(var4);
      bukkitItemStack.setItemMeta(var3);
   }

   @Nullable
   @Override
   public Integer am(org.bukkit.inventory.ItemStack bukkitItemStack) {
      DyedItemColor var2 = (DyedItemColor)CraftItemStack.asNMSCopy(bukkitItemStack).get(DataComponents.DYED_COLOR);
      return var2 == null ? null : var2.rgb();
   }

   @Nullable
   @Override
   public String method3441(org.bukkit.inventory.ItemStack bukkitItemStack, EquipmentSlot bukkitEquipmentSlot) {
      Equippable var3 = (Equippable)CraftItemStack.asNMSCopy(bukkitItemStack).get(DataComponents.EQUIPPABLE);
      return var3 != null && var3.slot() == CraftEquipmentSlot.getNMS(bukkitEquipmentSlot)
         ? var3.assetId().map(var0 -> var0.identifier().toString()).orElse(null)
         : null;
   }

   @Override
   public void method3442(org.bukkit.inventory.ItemStack bukkitItemStack, List hasLayer2) {
      ItemMeta var3 = bukkitItemStack.getItemMeta();
      CustomModelDataComponent var4 = var3.getCustomModelDataComponent();
      var4.setFlags(hasLayer2);
      var3.setCustomModelDataComponent(var4);
      bukkitItemStack.setItemMeta(var3);
   }

   private static void method3485(ItemStack var0) {
      var0.remove(DataComponents.CUSTOM_NAME);
      var0.remove(DataComponents.ITEM_NAME);
      var0.remove(DataComponents.LORE);
      var0.remove(DataComponents.DAMAGE);
      var0.remove(DataComponents.ENCHANTABLE);
      var0.remove(DataComponents.ENCHANTMENTS);
      var0.remove(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
      var0.remove(DataComponents.STORED_ENCHANTMENTS);
      var0.remove(DataComponents.BREAK_SOUND);
   }
}
