package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.nbt.NCompound;
import beer.devs.fastnbt.nms.nbt.NItem;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.serialization.Dynamic;
import dev.lone.itemsadder.config.ParsedValue;
import dev.lone.itemsadder.item.conversion.ItemConversionNodeB;
import dev.lone.itemsadder.item.conversion.ItemDataFixB;
import dev.lone.itemsadder.nms.InventoryAdapter;
import dev.lone.itemsadder.nms.InventoryAdapterV1_21_R1;
import dev.lone.itemsadder.util.AdventureText;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.text.Component;
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
import net.minecraft.util.datafix.DataConverterRegistry;
import net.minecraft.util.datafix.fixes.DataConverterTypes;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.food.FoodInfo;
import net.minecraft.world.item.AdventureModePredicate;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.component.ItemAttributeModifiers.b;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftContainer;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R1.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_21_R1.util.CraftNamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemRegistryBridgeV1_21_R1 implements ItemRegistryBridge {
   public static final Field aZg = FieldUtils.getField(CraftItemStack.class, "handle", true);
   private static final boolean aZh = Modifier.isPublic(aZg.getModifiers());
   static double aZf = EntityHuman.fM().a().b(GenericAttributes.e);

   public static ItemStack method3443(CraftItemStack var0) {
      if (aZh) {
         return var0.handle;
      }

      try {
         return (ItemStack)aZg.get(var0);
      } catch (IllegalAccessException var2) {
         new RuntimeException("Error reading handle field of CraftItemStack.", var2).printStackTrace();
         return null;
      }
   }

   @Override
   public InventoryAdapter OG() {
      return new InventoryAdapterV1_21_R1();
   }

   @Override
   public void method3426(Player player, Component component) {
      EntityPlayer var3 = ((CraftPlayer)player).getHandle();
      PacketPlayOutOpenWindow var4 = new PacketPlayOutOpenWindow(
         var3.cd.j, CraftContainer.getNotchInventoryType(player.getOpenInventory().getTopInventory()), (IChatBaseComponent)AdventureText.toNative(component)
      );
      var3.c.b(var4);
      player.updateInventory();
   }

   @Override
   public int cx(Player player) {
      EntityPlayer var2 = ((CraftPlayer)player).getHandle();
      return var2.cd.j;
   }

   @Override
   public String method3427(Material bukkitMaterial) {
      return CraftMagicNumbers.getItem(bukkitMaterial).a();
   }

   public static boolean method3444(ItemStack var0) {
      CustomData var1 = (CustomData)var0.a(DataComponents.b);
      return var1 != null && var1.a("itemsadder");
   }

   @Override
   public void method3428(org.bukkit.inventory.ItemStack bukkitItem, String skinString) {
      ItemStack var3 = CraftItemStack.asNMSCopy(bukkitItem);
      PropertyMap var4 = new PropertyMap();
      var4.put("textures", new Property("textures", skinString, null));
      var3.b(DataComponents.W, new ResolvableProfile(Optional.empty(), Optional.of(UUID.randomUUID()), var4));
      bukkitItem.setItemMeta(CraftItemStack.asCraftMirror(var3).getItemMeta());
   }

   @Override
   public Double ah(org.bukkit.inventory.ItemStack bukkitItem) {
      return this.method3445(bukkitItem, Item.f);
   }

   public Double method3445(org.bukkit.inventory.ItemStack var1, MinecraftKey var2) {
      ItemStack var3 = CraftItemStack.asNMSCopy(var1);
      ItemAttributeModifiers var4 = (ItemAttributeModifiers)var3.g().p().a(DataComponents.n);
      if (var4 != null) {
         for (b var6 : var4.b()) {
            if (var6.b().b().equals(var2)) {
               return aZf + var6.b().c();
            }
         }
      }

      return null;
   }

   @Override
   public void method3429(NItem nItem, String nbt) {
      String var3 = CraftMagicNumbers.getItem(nItem.getItem().getType()).o().h().a().toString();
      NBTTagCompound var4 = MojangsonParser.a(nbt);
      if (var4.e("components")) {
         var4.a("id", var3);
         var4.a("count", nItem.getItem().getAmount());
         NCompound var5 = new NCompound(var4);
         nItem.merge(var5.toBukkitItem());
         nItem.save();
      } else {
         if (!var4.e("tag")) {
            NBTTagCompound var8 = new NBTTagCompound();

            for (String var7 : var4.e()) {
               var8.a(var7, var4.c(var7));
               var4.r(var7);
            }

            var4.a("tag", var8);
         }

         var4.a("id", var3);
         var4.a("Count", nItem.getItem().getAmount());
         Dynamic var9 = DataConverterRegistry.a().update(DataConverterTypes.t, new Dynamic(DynamicOpsNBT.a, var4), 3692, 3816);
         ItemConversionNodeB var11 = (ItemConversionNodeB)ItemConversionNodeB.method3690(var9).get();
         ItemDataFixB.method3640(var11, var9);
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
      DataComponentMap var3 = var2.a();
      ItemAttributeModifiers var4 = (ItemAttributeModifiers)var3.a(DataComponents.n);
      if (var4 != null) {
         var2.b(DataComponents.n, var4.a(false));
      }

      DyedItemColor var5 = (DyedItemColor)var3.a(DataComponents.z);
      if (var5 != null) {
         var2.b(DataComponents.z, var5.a(false));
      }

      ArmorTrim var6 = (ArmorTrim)var3.a(DataComponents.K);
      if (var6 != null) {
         var2.b(DataComponents.K, var6.a(false));
      }

      Unbreakable var7 = (Unbreakable)var3.a(DataComponents.f);
      if (var7 != null) {
         var2.b(DataComponents.f, var7.a(false));
      }

      AdventureModePredicate var8 = (AdventureModePredicate)var3.a(DataComponents.m);
      if (var8 != null) {
         var2.b(DataComponents.m, var8.a(false));
      }

      var2.b(DataComponents.i, null);
      bukkitItem.setItemMeta(CraftItemStack.asCraftMirror(var2).getItemMeta());
      return bukkitItem;
   }

   @Override
   public void method3430(@NotNull ItemStack from, @NotNull ItemStack to) {
      CraftItemStack var3 = !(to instanceof CraftItemStack) ? CraftItemStack.asCraftCopy(to) : (CraftItemStack)to;
      CraftItemStack var4 = !(from instanceof CraftItemStack) ? CraftItemStack.asCraftCopy(from) : (CraftItemStack)from;
      ItemStack var5 = method3443(var3);
      ItemStack var6 = method3443(var4);
      if (var5 == null) {
         throw new NullPointerException("ItemStack toNms is null.");
      }

      if (var6 == null) {
         throw new NullPointerException("ItemStack fromNms is null.");
      }

      if (var6.b(DataComponents.b)) {
         ((CustomData)var5.a(DataComponents.b)).d().a(((CustomData)var6.a(DataComponents.b)).d());
      }

      to.setItemMeta(CraftItemStack.asCraftMirror(var5).getItemMeta());
   }

   public static boolean method3446(ItemStack var0, ItemStack var1) {
      CustomData var2 = (CustomData)var0.a(DataComponents.b);
      CustomData var3 = (CustomData)var1.a(DataComponents.b);
      if (var2 == null && var3 == null) {
         return true;
      } else if (var2 != null && var3 != null) {
         return var2.d().c("itemsadder") instanceof NBTTagCompound var4 && var3.d().c("itemsadder") instanceof NBTTagCompound var5
            ? var4.l("id").equals(var5.l("id")) && var4.l("namespace").equals(var5.l("namespace"))
            : false;
      } else {
         return false;
      }
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
      @Nullable List wrappedRemoveStatusEffects
   ) {
      ItemStack var10 = CraftItemStack.asNMSCopy(bukkitItemStack);
      var10.b(DataComponents.v, new FoodInfo(nutrition, saturation, canAlwaysEat, consumeSeconds, Optional.empty(), List.of()));
      bukkitItemStack.setItemMeta(CraftItemStack.asCraftMirror(var10).getItemMeta());
   }

   @Override
   public void al(org.bukkit.inventory.ItemStack bukkitItem) {
      ItemStack var2 = CraftItemStack.asNMSCopy(bukkitItem);
      ItemAttributeModifiers var3 = (ItemAttributeModifiers)var2.a(DataComponents.n);
      if (var3 != null) {
         var2.b(DataComponents.n, var3.a(false));
      } else {
         var2.b(DataComponents.n, ItemAttributeModifiers.a.a(false));
      }

      bukkitItem.setItemMeta(CraftItemStack.asCraftMirror(var2).getItemMeta());
   }

   @Override
   public void method3435(org.bukkit.inventory.ItemStack bukkitItemStack, @Nullable NamespacedKey jukeboxSongId) {
      ItemStack var3 = CraftItemStack.asNMSCopy(bukkitItemStack);
      if (jukeboxSongId != null) {
         CraftNamespacedKey.toMinecraft(jukeboxSongId);
         JukeboxPlayable var4 = new JukeboxPlayable(new EitherHolder(ResourceKey.a(Registries.L, MinecraftKey.a(jukeboxSongId.asString()))), true);
         var3.b(DataComponents.R, var4);
      } else {
         var3.c(DataComponents.R);
      }

      bukkitItemStack.setItemMeta(CraftItemStack.asCraftMirror(var3).getItemMeta());
   }

   @Override
   public boolean method3436(org.bukkit.inventory.ItemStack bukkitItemStackA, org.bukkit.inventory.ItemStack bukkitItemStackB) {
      ItemStack var3 = CraftItemStack.asNMSCopy(bukkitItemStackA);
      ItemStack var4 = CraftItemStack.asNMSCopy(bukkitItemStackB);
      if (method3444(var3) && method3444(var4)) {
         method3447(var3);
         method3447(var4);
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

   private static void method3447(ItemStack var0) {
      var0.c(DataComponents.g);
      var0.c(DataComponents.h);
      var0.c(DataComponents.i);
      var0.c(DataComponents.e);
      var0.c(DataComponents.k);
      var0.c(DataComponents.t);
      var0.c(DataComponents.y);
   }
}
