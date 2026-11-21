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
import dev.lone.itemsadder.item.conversion.ItemConversionNodeC;
import dev.lone.itemsadder.item.conversion.ItemDataFixC;
import dev.lone.itemsadder.nms.ContainerClickAdapter;
import dev.lone.itemsadder.nms.ContainerClickAdapterV1_21_R6;
import dev.lone.itemsadder.util.AdventureText;
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
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.util.Unit;
import net.minecraft.util.datafix.DataConverterRegistry;
import net.minecraft.util.datafix.fixes.DataConverterTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.food.FoodInfo;
import net.minecraft.world.item.AdventureModePredicate;
import net.minecraft.world.item.EitherHolder;
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
import net.minecraft.world.item.component.ItemAttributeModifiers.c;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.PlaySoundConsumeEffect;
import net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.equipment.Equippable.a;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R6.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftEntityType;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R6.inventory.CraftContainer;
import org.bukkit.craftbukkit.v1_21_R6.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R6.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_21_R6.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_21_R6.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemRegistryBridgeV1_21_R6 implements ItemRegistryBridge {
   private static final ContainerClickAdapter aZi = new ContainerClickAdapterV1_21_R6();
   public static final Field aZj = FieldUtils.getField(CraftItemStack.class, "handle", true);
   private static final boolean aZk = Modifier.isPublic(aZj.getModifiers());
   static double aZf = EntityHuman.go().a().b(GenericAttributes.e);

   @Override
   public ContainerClickAdapter OH() {
      return aZi;
   }

   public static ItemStack method3448(CraftItemStack var0) {
      if (aZk) {
         return var0.handle;
      }

      try {
         return (ItemStack)aZj.get(var0);
      } catch (IllegalAccessException var2) {
         new RuntimeException("Error reading handle field of CraftItemStack.", var2).printStackTrace();
         return null;
      }
   }

   @Override
   public void method3426(Player player, Component component) {
      EntityPlayer var3 = ((CraftPlayer)player).getHandle();
      PacketPlayOutOpenWindow var4 = new PacketPlayOutOpenWindow(
         var3.cl.l, CraftContainer.getNotchInventoryType(player.getOpenInventory().getTopInventory()), (IChatBaseComponent)AdventureText.toNative(component)
      );
      var3.g.b(var4);
      player.updateInventory();
   }

   @Override
   public int cx(Player player) {
      EntityPlayer var2 = ((CraftPlayer)player).getHandle();
      return var2.cl.l;
   }

   @Override
   public String method3427(Material bukkitMaterial) {
      return CraftMagicNumbers.getItem(bukkitMaterial).j();
   }

   public static boolean method3444(ItemStack var0) {
      CustomData var1 = (CustomData)var0.a(DataComponents.b);
      return var1 != null && var1.contains("itemsadder");
   }

   @Override
   public void method3428(org.bukkit.inventory.ItemStack bukkitItem, String skinString) {
      ItemStack var3 = CraftItemStack.asNMSCopy(bukkitItem);
      HashMultimap var4 = HashMultimap.create();
      var4.put("textures", new Property("textures", skinString, null));
      bukkitItem.setItemMeta(CraftItemStack.asCraftMirror(var3).getItemMeta());
      PropertyMap var5 = new PropertyMap(var4);
      ResolvableProfile var6 = ResolvableProfile.a(new GameProfile(UUID.randomUUID(), "", var5));
      var3.b(DataComponents.ak, var6);
   }

   @Override
   public Double ah(org.bukkit.inventory.ItemStack bukkitItem) {
      return this.method3445(bukkitItem, Item.i);
   }

   public Double method3445(org.bukkit.inventory.ItemStack var1, MinecraftKey var2) {
      ItemStack var3 = CraftItemStack.asNMSCopy(var1);
      ItemAttributeModifiers var4 = (ItemAttributeModifiers)var3.h().f().a(DataComponents.o);
      if (var4 != null) {
         for (c var6 : var4.b()) {
            if (var6.b().a().equals(var2)) {
               return aZf + var6.b().b();
            }
         }
      }

      return null;
   }

   @Override
   public void method3429(NItem nItem, String nbt) {
      String var3 = CraftMagicNumbers.getItem(nItem.getItem().getType()).e().h().a().toString();
      NBTTagCompound var4 = MojangsonParser.a(nbt);
      if (var4.b("components")) {
         var4.a("id", var3);
         var4.a("count", nItem.getItem().getAmount());
         NCompound var5 = new NCompound(var4);
         nItem.merge(var5.toBukkitItem());
         nItem.save();
      } else {
         if (!var4.b("tag")) {
            NBTTagCompound var8 = new NBTTagCompound();

            for (String var7 : var4.e()) {
               var8.a(var7, var4.a(var7));
               var4.r(var7);
            }

            var4.a("tag", var8);
         }

         var4.a("id", var3);
         var4.a("Count", nItem.getItem().getAmount());
         Dynamic var9 = DataConverterRegistry.a().update(DataConverterTypes.v, new Dynamic(DynamicOpsNBT.a, var4), 3692, 3816);
         ItemConversionNodeC var11 = (ItemConversionNodeC)ItemConversionNodeC.method3760(var9).get();
         ItemDataFixC.method3710(var11, var9);
         var9 = var11.OR();
         NCompound var12 = new NCompound(var9.getValue());
         nItem.merge(var12.toBukkitItem());
         nItem.save();
      }
   }

   @Override
   public org.bukkit.inventory.ItemStack ai(org.bukkit.inventory.ItemStack bukkitItem) {
      ItemStack var2 = CraftItemStack.asNMSCopy(bukkitItem);
      var2.b(0);
      TooltipDisplay var3 = TooltipDisplay.c;
      DataComponentMap var4 = var2.a();
      ItemAttributeModifiers var5 = (ItemAttributeModifiers)var4.a(DataComponents.o);
      if (var5 != null) {
         var3 = var3.a(DataComponents.o, true);
      }

      DyedItemColor var6 = (DyedItemColor)var4.a(DataComponents.K);
      if (var6 != null) {
         var3 = var3.a(DataComponents.K, true);
      }

      ArmorTrim var7 = (ArmorTrim)var4.a(DataComponents.W);
      if (var7 != null) {
         var3 = var3.a(DataComponents.W, true);
      }

      Unit var8 = (Unit)var4.a(DataComponents.f);
      if (var8 != null) {
         var3 = var3.a(DataComponents.f, true);
      }

      AdventureModePredicate var9 = (AdventureModePredicate)var4.a(DataComponents.n);
      if (var9 != null) {
         var3 = var3.a(DataComponents.n, true);
      }

      var2.b(DataComponents.j, null);
      var2.b(DataComponents.q, var3);
      bukkitItem.setItemMeta(CraftItemStack.asCraftMirror(var2).getItemMeta());
      return bukkitItem;
   }

   public static boolean method3446(ItemStack var0, ItemStack var1) {
      CustomData var2 = (CustomData)var0.a(DataComponents.b);
      CustomData var3 = (CustomData)var1.a(DataComponents.b);
      if (var2 == null && var3 == null) {
         return true;
      } else if (var2 != null && var3 != null) {
         return var2.getUnsafe().a("itemsadder") instanceof NBTTagCompound var4 && var3.getUnsafe().a("itemsadder") instanceof NBTTagCompound var5
            ? var4.i("id").equals(var5.i("id")) && var4.i("namespace").equals(var5.i("namespace"))
            : false;
      } else {
         return false;
      }
   }

   @Override
   public void method3430(@NotNull ItemStack from, @NotNull ItemStack to) {
      CraftItemStack var3 = !(to instanceof CraftItemStack) ? CraftItemStack.asCraftCopy(to) : (CraftItemStack)to;
      CraftItemStack var4 = !(from instanceof CraftItemStack) ? CraftItemStack.asCraftCopy(from) : (CraftItemStack)from;
      ItemStack var5 = method3448(var3);
      ItemStack var6 = method3448(var4);
      if (var5 == null) {
         throw new NullPointerException("ItemStack toNms is null.");
      }

      if (var6 == null) {
         throw new NullPointerException("ItemStack fromNms is null.");
      }

      if (var6.c(DataComponents.b)) {
         ((CustomData)var5.a(DataComponents.b)).getUnsafe().a(((CustomData)var6.a(DataComponents.b)).getUnsafe());
      }

      to.setItemMeta(CraftItemStack.asCraftMirror(var5).getItemMeta());
   }

   @Override
   public void method3431(org.bukkit.inventory.ItemStack bukkitItemStack, float seconds, String group) {
      if (group != null && !group.contains(":")) {
         group = "itemsadder:" + group;
      }

      Optional var4 = group == null ? Optional.empty() : Optional.of(MinecraftKey.a(group));
      ItemStack var5 = CraftItemStack.asNMSCopy(bukkitItemStack);
      var5.b(DataComponents.y, new UseCooldown(seconds, var4));
      bukkitItemStack.setItemMeta(CraftItemStack.asCraftMirror(var5).getItemMeta());
   }

   @Override
   public boolean aj(org.bukkit.inventory.ItemStack bukkitItemStack) {
      ItemStack var2 = CraftItemStack.asNMSCopy(bukkitItemStack);
      var2.e(DataComponents.x);
      bukkitItemStack.setItemMeta(CraftItemStack.asCraftMirror(var2).getItemMeta());
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
         var11 = Holder.a(SoundEffect.a(MinecraftKey.a(sound)));
      } else {
         var11 = SoundEffects.lv;
      }

      ItemUseAnimation var12 = ItemUseAnimation.b;
      if (animation != null) {
         for (ItemUseAnimation var16 : ItemUseAnimation.values()) {
            if (var16.c().equals(animation)) {
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
                     var0 -> new MobEffect(
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
               var21.add(new RemoveStatusEffectsConsumeEffect(HolderSet.a(var25)));
            } else if (var23 instanceof ParsedFloat var18) {
               var21.add(new TeleportRandomlyConsumeEffect(var18.aZa));
            } else if (var23 instanceof ParsedValueEmpty) {
               var21.add(new ClearAllStatusEffectsConsumeEffect());
            } else if (var23 instanceof ParsedString var19) {
               var21.add(new PlaySoundConsumeEffect(Holder.a(SoundEffect.a(MinecraftKey.a(var19.aYY)))));
            }
         }
      }

      var10.b(DataComponents.w, new Consumable(consumeSeconds, var12, (Holder)var11, hasConsumeParticles, var21));
      var10.b(DataComponents.v, new FoodInfo(nutrition, saturation, canAlwaysEat));
      bukkitItemStack.setItemMeta(CraftItemStack.asCraftMirror(var10).getItemMeta());
   }

   @Override
   public boolean ak(org.bukkit.inventory.ItemStack itemStack) {
      return CraftItemStack.asNMSCopy(itemStack).c(DataComponents.w);
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
      EnumItemSlot var11 = CraftEquipmentSlot.getNMS(bukkitEquipmentSlot);
      Holder var12 = null;
      if (equipSoundName != null) {
         var12 = Holder.a(SoundEffect.a(MinecraftKey.a(equipSoundName)));
      }

      a var13 = Equippable.a(var11);
      if (var12 != null) {
         var13.a(var12);
      }

      if (bukkitAssetId != null) {
         var13.a(ResourceKey.a(EquipmentAssets.a, CraftNamespacedKey.toMinecraft(bukkitAssetId)));
      }

      if (bukkitCameraOverlay != null) {
         var13.a(CraftNamespacedKey.toMinecraft(bukkitCameraOverlay));
      }

      if (allowedEntitiesBukkit != null && !allowedEntitiesBukkit.isEmpty()) {
         var13.a(HolderSet.a(allowedEntitiesBukkit.stream().filter(Objects::nonNull).map(CraftEntityType::bukkitToMinecraftHolder).toList()));
      }

      if (dispensable != null) {
         var13.a(dispensable);
      }

      if (swappable != null) {
         var13.b(swappable);
      }

      if (damageOnHurt != null) {
         var13.c(damageOnHurt);
      }

      ItemStack var14 = CraftItemStack.asNMSCopy(bukkitItemStack);
      var14.b(DataComponents.D, var13.a());
      if (glider != null && glider) {
         var14.b(DataComponents.F, Unit.a);
      }

      bukkitItemStack.setItemMeta(CraftItemStack.asCraftMirror(var14).getItemMeta());
   }

   @Override
   public void method3434(org.bukkit.inventory.ItemStack bukkitItemStack, @Nullable NamespacedKey bukkitTooltipStyleId) {
      ItemStack var3 = CraftItemStack.asNMSCopy(bukkitItemStack);
      if (bukkitTooltipStyleId != null) {
         var3.b(DataComponents.G, CraftNamespacedKey.toMinecraft(bukkitTooltipStyleId));
      } else {
         var3.e(DataComponents.G);
      }

      bukkitItemStack.setItemMeta(CraftItemStack.asCraftMirror(var3).getItemMeta());
   }

   @Override
   public void al(org.bukkit.inventory.ItemStack bukkitItem) {
      ItemStack var2 = CraftItemStack.asNMSCopy(bukkitItem);
      var2.b(DataComponents.q, TooltipDisplay.c.a(DataComponents.o, true));
      bukkitItem.setItemMeta(CraftItemStack.asCraftMirror(var2).getItemMeta());
   }

   @Override
   public void method3435(org.bukkit.inventory.ItemStack bukkitItemStack, @Nullable NamespacedKey jukeboxSongId) {
      ItemStack var3 = CraftItemStack.asNMSCopy(bukkitItemStack);
      if (jukeboxSongId != null) {
         CraftNamespacedKey.toMinecraft(jukeboxSongId);
         JukeboxPlayable var4 = new JukeboxPlayable(new EitherHolder(ResourceKey.a(Registries.bd, MinecraftKey.a(jukeboxSongId.asString()))));
         var3.b(DataComponents.ae, var4);
      } else {
         var3.e(DataComponents.ae);
      }

      bukkitItemStack.setItemMeta(CraftItemStack.asCraftMirror(var3).getItemMeta());
   }

   @Override
   public boolean method3436(org.bukkit.inventory.ItemStack bukkitItemStackA, org.bukkit.inventory.ItemStack bukkitItemStackB) {
      ItemStack var3 = CraftItemStack.asNMSCopy(bukkitItemStackA);
      ItemStack var4 = CraftItemStack.asNMSCopy(bukkitItemStackB);
      if (method3444(var3) && method3444(var4)) {
         method3449(var3);
         method3449(var4);
         CustomData var5 = (CustomData)var3.a(DataComponents.b);
         CustomData var6 = (CustomData)var4.a(DataComponents.b);
         if (var5 == null && var6 == null) {
            return true;
         } else {
            return var5 != null && var6 != null ? var3.d().equals(var4.d()) : false;
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
      DyedItemColor var2 = (DyedItemColor)CraftItemStack.asNMSCopy(bukkitItemStack).a(DataComponents.K);
      return var2 == null ? null : var2.a();
   }

   @Nullable
   @Override
   public String method3441(org.bukkit.inventory.ItemStack bukkitItemStack, EquipmentSlot bukkitEquipmentSlot) {
      Equippable var3 = (Equippable)CraftItemStack.asNMSCopy(bukkitItemStack).a(DataComponents.D);
      return var3 != null && var3.b() == CraftEquipmentSlot.getNMS(bukkitEquipmentSlot) ? var3.d().map(var0 -> var0.a().toString()).orElse(null) : null;
   }

   @Override
   public void method3442(org.bukkit.inventory.ItemStack bukkitItemStack, List hasLayer2) {
      ItemMeta var3 = bukkitItemStack.getItemMeta();
      CustomModelDataComponent var4 = var3.getCustomModelDataComponent();
      var4.setFlags(hasLayer2);
      var3.setCustomModelDataComponent(var4);
      bukkitItemStack.setItemMeta(var3);
   }

   private static void method3449(ItemStack var0) {
      var0.e(DataComponents.g);
      var0.e(DataComponents.h);
      var0.e(DataComponents.j);
      var0.e(DataComponents.e);
      var0.e(DataComponents.C);
      var0.e(DataComponents.l);
      var0.e(DataComponents.t);
      var0.e(DataComponents.J);
      var0.e(DataComponents.au);
   }
}
