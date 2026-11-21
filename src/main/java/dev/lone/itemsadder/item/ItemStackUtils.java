package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.Version;
import beer.devs.fastnbt.nms.nbt.NBT;
import beer.devs.fastnbt.nms.nbt.NItem;
import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import dev.lone.itemsadder.internal.SwitchMap84;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.EnvironmentDetector;
import dev.lone.itemsadder.util.Utils;
import java.util.List;
import java.util.Map.Entry;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemStackUtils {
   public static ItemStack bMp = new ItemStack(Material.AIR);
   public static EquipmentSlot[] bMq = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
   public static EquipmentSlot[] bMr = new EquipmentSlot[]{
      EquipmentSlot.HAND, EquipmentSlot.OFF_HAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
   };

   public static boolean method7217(InventoryClickEvent var0) {
      return var0.getRawSlot() < var0.getView().getTopInventory().getSize();
   }

   public static boolean dk(Player var0) {
      return var0.getInventory().firstEmpty() == -1;
   }

   public static boolean ap(ItemStack var0) {
      return var0 == null ? false : ab(var0.getType());
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static boolean ab(Material var0) {
      if (var0 != null && var0 != Material.AIR) {
         switch (SwitchMap84.bMs[var0.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
               return true;
            default:
               if (Version.isAtLeast(Version.v1_21)) {
                  if (var0 == Material.MACE) {
                     return true;
                  }

                  if (Version.isAtLeast(Version.v1_21_10)) {
                     String var1 = var0.toString();
                     return var1.equals("COPPER_SWORD")
                        || var1.equals("COPPER_PICKAXE")
                        || var1.equals("COPPER_AXE")
                        || var1.equals("COPPER_HOE")
                        || var1.equals("COPPER_SHOVEL")
                        || var1.endsWith("_SPEAR");
                  }
               }

               return false;
         }
      } else {
         return false;
      }
   }

   public static boolean ac(Material var0) {
      return EnchantmentTarget.ARMOR.includes(var0);
   }

   public static boolean ad(Material var0) {
      return var0.toString().endsWith("_HEAD");
   }

   public static boolean aq(ItemStack var0) {
      return var0.hasItemMeta() && var0.getItemMeta().hasEnchants() ? var0.getEnchantments().containsKey(Enchantment.SILK_TOUCH) : false;
   }

   public static boolean method7218(ItemStack var0, Enchantment var1) {
      return var0.hasItemMeta() && var0.getItemMeta().hasEnchants() ? var0.getItemMeta().hasEnchant(var1) : false;
   }

   public static int method7219(ItemStack var0, Enchantment var1) {
      return var0.getItemMeta().getEnchantLevel(var1);
   }

   public static boolean method7220(ItemStack var0, Enchantment var1) {
      ItemMeta var2 = var0.getItemMeta();
      boolean var3 = var2.removeEnchant(var1);
      var0.setItemMeta(var2);
      return var3;
   }

   public static void method7221(ItemStack var0, ItemStack var1) {
      NItem var2 = new NItem(var1);
      NItem var3 = new NItem(var0);
      var3.merge(var2);
   }

   public static void method7222(ItemStack var0, String var1) {
      ItemMeta var2 = var0.getItemMeta();
      var2.setDisplayName(var1);
      var0.setItemMeta(var2);
   }

   public static void method7223(ItemStack var0, Component var1) {
      NItem var2 = new NItem(var0);
      method7224(var2, var1);
   }

   public static void method7224(NItem var0, Component var1) {
      var0.setCustomName(AdventureText.serializePlain(var1));
   }

   @Nullable
   public static String ar(ItemStack var0) {
      NItem var1 = new NItem(var0);
      return var1.getCustomNameJson();
   }

   @Nullable
   public static Component as(ItemStack var0) {
      NItem var1 = new NItem(var0);
      Object var2 = var1.getCustomName();
      if (var2 == null) {
         var2 = var1.getItemName();
      }

      return var2 == null ? null : AdventureText.fromNative(var2);
   }

   public static void method7225(CustomItemDefinition var0, CustomItemDefinition var1) {
      ItemStack var2 = NBT.item.convertToCraft(var0);
      NBT.dataComponents.copyLore(NBT.item.convertToCraft(var1), var2);
      var0.method8769();
   }

   public static void method7226(NItem var0, List var1) {
      NBT.dataComponents.appendLoreJson(var0.getItem(), var1);
      var0.save();
   }

   public static void method7227(ItemStack var0, @NotNull List var1) {
      ItemMeta var2 = var0.getItemMeta();
      var2.setLore(var1);
      var0.setItemMeta(var2);
   }

   public static void method7228(ItemStack var0, List var1) {
      ItemMeta var2 = var0.getItemMeta();
      var2.setLoreComponents(var1);
      var0.setItemMeta(var2);
   }

   public static ItemStack method7229(ItemStack var0, Color var1) {
      PotionMeta var2 = (PotionMeta)var0.getItemMeta();
      var2.setColor(var1);
      var0.setItemMeta(var2);
      return var0;
   }

   public static ItemStack at(ItemStack var0) {
      return method7229(var0, null);
   }

   public static void dl(Player var0) {
      ItemStack var1 = var0.getInventory().getItemInMainHand();
      if (var1.getType() != Material.AIR && var1.getAmount() > 0 && var1.getAmount() - 1 >= 0) {
         var1.setAmount(var1.getAmount() - 1);
      }
   }

   public static void dm(Player var0) {
      ItemStack var1 = var0.getInventory().getItemInOffHand();
      if (var1.getType() != Material.AIR && var1.getAmount() > 0 && var1.getAmount() - 1 >= 0) {
         var1.setAmount(var1.getAmount() - 1);
      }
   }

   public static ItemStack method7230(ItemStack var0, int var1) {
      if (var0 != null && var0.getType() != Material.AIR) {
         var0.setAmount(var0.getAmount() - var1);
         return var0;
      } else {
         return var0;
      }
   }

   public static int au(ItemStack var0) {
      return method7231(var0, 1);
   }

   public static int method7231(ItemStack var0, int var1) {
      if (var0 != null && var0.getType() != Material.AIR) {
         var0.setAmount(var0.getAmount() - var1);
         return var0.getAmount();
      } else {
         return 0;
      }
   }

   public static void method7232(Player var0, EquipmentSlot var1) {
      if (var1 == EquipmentSlot.OFF_HAND) {
         dm(var0);
      } else {
         dl(var0);
      }
   }

   public static void av(ItemStack var0) {
      if (var0 != null && var0.getType() != Material.AIR && var0.getAmount() > 0 && var0.getAmount() + 1 <= var0.getType().getMaxStackSize()) {
         var0.setAmount(var0.getAmount() + 1);
      }
   }

   public static int aw(ItemStack var0) {
      return var0 != null && var0.getItemMeta() instanceof Damageable var1 ? method7233(var0) - var1.getDamage() : 0;
   }

   public static int method7233(ItemStack var0) {
      return var0 != null && var0.getItemMeta() instanceof Damageable var1 ? method7234(var1, var0.getType()) : 0;
   }

   public static int method7234(Damageable var0, Material var1) {
      return var0.hasMaxDamage() ? var0.getMaxDamage() : var1.getMaxDurability();
   }

   public static int ax(ItemStack var0) {
      if (var0 == null) {
         return 0;
      }

      ItemMeta var1 = var0.getItemMeta();
      return var1.hasMaxStackSize() ? var1.getMaxStackSize() : var0.getType().getMaxStackSize();
   }

   public static void method7235(ItemStack var0, int var1) {
      if (var0 != null && var0.getItemMeta() instanceof Damageable var2) {
         if (var1 > method7233(var0)) {
            var1 = method7233(var0);
         }

         var2.setDamage(method7233(var0) - var1);
         var0.setItemMeta(var2);
      }
   }

   public static void method7236(ItemStack var0, int var1) {
      if (var0 != null && var0.getItemMeta() instanceof Damageable var2) {
         if (var2.getDamage() - var1 < 0) {
            var2.setDamage(0);
            var0.setItemMeta(var2);
         } else {
            var2.setDamage(var2.getDamage() - var1);
            var0.setItemMeta(var2);
         }
      }
   }

   public static void method7237(ItemStack var0, int var1) {
      method7238(var0, var1, false);
   }

   public static void method7238(ItemStack var0, int var1, boolean var2) {
      method7239(var0, var1, var2, false);
   }

   public static void method7239(ItemStack var0, int var1, boolean var2, boolean var3) {
      if (var0 != null && var0.getItemMeta() instanceof Damageable var4) {
         if (!var2 || az(var0)) {
            if (var4.getDamage() + var1 > method7234(var4, var0.getType())) {
               var4.setDamage(method7234(var4, var0.getType()));
            } else {
               var4.setDamage(var4.getDamage() + var1);
               if (var3 && var4.getDamage() >= method7234(var4, var0.getType())) {
                  var0.setAmount(var0.getAmount() - 1);
                  var4.setDamage(0);
               }

               var0.setItemMeta(var4);
            }
         }
      }
   }

   public static void method7240(ItemStack var0, int var1) {
      if (var0 != null && var0.getItemMeta() instanceof Damageable var2) {
         var2.setMaxDamage(var1);
         var2.setMaxStackSize(1);
         var0.setItemMeta(var2);
      }
   }

   public static void method7241(CustomItemDefinition var0, boolean var1) {
      if (var0 != null && var0.getItemMeta() instanceof Damageable var2) {
         var2.setUnbreakable(var1);
         var0.setItemMeta(var2);
      }
   }

   public static ItemStack ay(ItemStack var0) {
      if (var0 != null && var0.getItemMeta() instanceof Damageable var1) {
         var1.setDamage(0);
         var0.setItemMeta(var1);
         return var0;
      } else {
         return var0;
      }
   }

   public static boolean method7242(ItemStack var0) {
      if (var0.getItemMeta() instanceof Damageable var1) {
         int var3 = method7234(var1, var0.getType());
         return var3 <= 0 ? false : var1.getDamage() > var3;
      } else {
         return false;
      }
   }

   public static boolean az(ItemStack var0) {
      return var0.getItemMeta().hasEnchant(Enchantment.UNBREAKING) ? dM(var0.getItemMeta().getEnchantLevel(Enchantment.UNBREAKING)) : true;
   }

   public static boolean dM(int var0) {
      return Utils.randomInt(1, 1 + var0) == 1;
   }

   public static ItemStack dn(Player var0) {
      return var0.getInventory().getItemInMainHand();
   }

   public static void method7243(Player var0, ItemStack var1) {
      method7246(var0, var1);
   }

   public static void method7244(Player var0, ItemStack... var1) {
      for (ItemStack var5 : var1) {
         if (var5 != null) {
            method7243(var0, var5);
         }
      }
   }

   public static void method7245(Player var0, ItemStack var1) {
      if (var1 != null) {
         method7243(var0, var1);
      }
   }

   public static void method7246(Player var0, ItemStack... var1) {
      var0.getInventory().addItem(var1).forEach((var1x, var2) -> {
         if (var2 != null && var2.getType() != Material.AIR) {
            Item var3 = var0.getWorld().dropItem(var0.getLocation(), var2);

            try {
               var3.setOwner(var0.getUniqueId());
            } catch (NoSuchMethodError var5) {
            }

            var3.setPickupDelay(0);
         }
      });
   }

   public static boolean ae(Material var0) {
      return var0.getMaxDurability() > 0;
   }

   public static boolean aA(ItemStack var0) {
      if (var0.getType().getMaxDurability() > 0) {
         return true;
      } else {
         return var0.getItemMeta() instanceof Damageable var1 ? var1.hasMaxDamage() : false;
      }
   }

   public static String method7247(ItemStack var0, ChatColor var1) {
      if (var0 == null) {
         return null;
      }

      String var2 = "";
      if (var0.hasItemMeta() && var0.getItemMeta().hasDisplayName()) {
         var2 = var0.getItemMeta().getDisplayName();
      } else {
         try {
            var2 = var0.getI18NDisplayName();
         } catch (NoSuchMethodError var4) {
            var2 = WordUtils.capitalizeFully(var0.getType().toString()).replace("_", " ");
         }
      }

      return var2.startsWith(org.bukkit.ChatColor.WHITE.toString()) ? org.bukkit.ChatColor.stripColor(var2) : var2;
   }

   public static String method7248(ItemStack var0, org.bukkit.ChatColor var1) {
      return method7249(var0, var1.toString());
   }

   public static String method7249(ItemStack var0, String var1) {
      String var2 = "";
      if (var0.hasItemMeta() && var0.getItemMeta().hasDisplayName()) {
         var2 = var1 + var0.getItemMeta().getDisplayName();
      } else {
         try {
            var2 = var1 + var0.getI18NDisplayName();
         } catch (NoSuchMethodError var4) {
            var2 = var1 + WordUtils.capitalizeFully(var0.getType().toString()).replace("_", " ");
         }
      }

      return var2;
   }

   public static String af(Material var0) {
      String var1 = "";

      try {
         var1 = new ItemStack(var0).getI18NDisplayName();
      } catch (NoSuchMethodError var3) {
         var1 = org.bukkit.ChatColor.GRAY + WordUtils.capitalizeFully(var0.toString()).replace("_", " ");
      }

      return var1;
   }

   public static String ag(Material var0) {
      return String.format(String.format("%s.minecraft.%%s", var0.isBlock() ? "block" : "item"), var0.toString().toLowerCase());
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static double ah(Material var0) {
      if (Version.isAtLeast(Version.v1_21)) {
         if (var0 == Material.MACE) {
            return 6.0;
         }

         if (Version.isAtLeast(Version.v1_21_10)) {
            String var1 = var0.toString();
            switch (var1) {
               case "COPPER_SWORD":
                  return 5.0;
               case "COPPER_PICKAXE":
                  return 3.0;
               case "COPPER_AXE":
                  return 9.0;
               case "COPPER_HOE":
                  return 1.0;
               case "COPPER_SHOVEL":
                  return 3.5;
            }
         }
      }
      return switch (SwitchMap84.bMs[var0.ordinal()]) {
         case 1 -> 8.0;
         case 2 -> 7.0;
         case 3 -> 6.0;
         case 4 -> 6.0;
         case 5 -> 6.0;
         case 6 -> 7.0;
         case 7 -> 6.0;
         case 8 -> 4.0;
         case 9 -> 5.0;
         case 10 -> 4.0;
         case 11 -> 9.0;
         case 12 -> 9.0;
         case 13 -> 7.0;
         case 14 -> 9.0;
         case 15 -> 7.0;
         case 16, 17, 18, 19, 20 -> 1.0;
         case 21 -> 5.5;
         case 22 -> 4.5;
         case 23 -> 2.5;
         case 24 -> 3.5;
         case 25 -> 2.5;
         case 26 -> 5.0;
         case 27 -> 4.0;
         case 28 -> 2.0;
         case 29 -> 3.0;
         case 30 -> 2.0;
         case 31 -> 9.0;
         default -> 1.0;
      };
   }

   public static boolean lh(String var0) {
      try {
         Material.valueOf(var0);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static boolean method7250(PlayerInteractEvent var0) {
      return var0.getHand() == EquipmentSlot.OFF_HAND;
   }

   public static boolean method7251(PlayerInteractEntityEvent var0) {
      return var0.getHand() == EquipmentSlot.OFF_HAND;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static boolean ai(Material var0) {
      switch (SwitchMap84.bMs[var0.ordinal()]) {
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
            return true;
         default:
            return false;
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static boolean aj(Material var0) {
      switch (SwitchMap84.bMs[var0.ordinal()]) {
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
            return true;
         default:
            return false;
      }
   }

   public static void method7252(BlockPlaceEvent var0) {
      if (var0.getHand() == EquipmentSlot.OFF_HAND) {
         dm(var0.getPlayer());
      } else {
         dl(var0.getPlayer());
      }
   }

   @Nullable
   public static ItemStack method7253(Projectile var0) {
      return ProjectileTypeHelper.getProjectileItem(var0);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static boolean aB(ItemStack var0) {
      return switch (SwitchMap84.bMs[var0.getType().ordinal()]) {
         case 31, 48, 49, 50, 51, 52 -> true;
         default -> false;
      };
   }

   public static void method7254(@Nullable Player var0, ItemStack var1) {
      if (var0 != null) {
         var0.playSound(var0.getLocation(), Sound.ENTITY_ITEM_BREAK, 10.0F, 1.0F);
         var0.getWorld()
            .spawnParticle(Particle.ITEM, var0.getLocation().add(var0.getLocation().getDirection()).add(0.0, 0.5, 0.0), 10, 0.3, 0.5, 0.3, 0.1, var1);
      }
   }

   public static void method7255(Location var0, ItemStack var1) {
      var0 = var0.clone();
      var0.getWorld().playSound(var0, Sound.ENTITY_ITEM_BREAK, 10.0F, 1.0F);
      var0.getWorld().spawnParticle(Particle.ITEM, var0.add(var0.getDirection()).add(0.0, 0.5, 0.0), 10, 0.3, 0.5, 0.3, 0.1, var1);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static float aC(ItemStack var0) {
      if (var0.getType() == Material.AIR) {
         return 1.0F;
      }

      return switch (SwitchMap84.bMs[var0.getType().ordinal()]) {
         case 1, 2, 3, 4, 5 -> 9.0F;
         case 6, 11, 16, 21, 26 -> 8.0F;
         case 7, 12, 17, 22, 27 -> 6.0F;
         case 8, 13, 18, 23, 28 -> 12.0F;
         case 9, 14, 19, 24, 29 -> 4.0F;
         case 10, 15, 20, 25, 30 -> 2.0F;
         default -> 1.0F;
      };
   }

   public static void method7256(ItemStack var0, ItemStack var1) {
      ItemMeta var2 = var0.getItemMeta();
      ItemMeta var3 = var1.getItemMeta();
      if (var2.hasEnchants()) {
         for (Entry var5 : var2.getEnchants().entrySet()) {
            if (var3.getEnchantLevel((Enchantment)var5.getKey()) < (Integer)var5.getValue()) {
               var3.addEnchant((Enchantment)var5.getKey(), (Integer)var5.getValue(), true);
            }
         }
      }

      var1.setItemMeta(var3);
   }

   public static void method7257(ItemStack var0, ItemStack var1) {
      ItemMeta var2 = var0.getItemMeta();
      ItemMeta var3 = var1.getItemMeta();
      if (var2.hasCustomModelData()) {
         var3.setCustomModelData(var2.getCustomModelData());
      } else {
         var3.setCustomModelData(0);
      }

      var1.setItemMeta(var3);
   }

   public static boolean aD(ItemStack var0) {
      boolean var1 = false;
      ItemMeta var2 = var0.getItemMeta();
      if (var2.hasEnchants()) {
         for (Entry var4 : var2.getEnchants().entrySet()) {
            if (var2.removeEnchant((Enchantment)var4.getKey())) {
               var1 = true;
            }
         }
      }

      var0.setItemMeta(var2);
      return var1;
   }

   public static int method7258(ItemStack var0) {
      ItemMeta var1 = var0.getItemMeta();
      return !var1.hasCustomModelData() ? 0 : var1.getCustomModelData();
   }

   public static void method7259(ItemStack var0, int var1) {
      ItemMeta var2 = var0.getItemMeta();
      var2.setCustomModelData(var1);
      var0.setItemMeta(var2);
   }

   public static boolean aE(@Nullable ItemStack var0) {
      return var0 == null || var0.getType() == Material.AIR;
   }

   public static ItemStack acf() {
      return new ItemStack(Material.AIR);
   }

   public static boolean method7260(PlayerPickupItemEvent var0) {
      Integer var1 = method7261(var0);
      return var1 == null ? false : var1 == var0.getPlayer().getInventory().getHeldItemSlot();
   }

   public static Integer method7261(PlayerPickupItemEvent var0) {
      return method7262(var0.getPlayer(), var0.getItem().getItemStack());
   }

   public static Integer method7262(Player var0, ItemStack var1) {
      PlayerInventory var2 = var0.getInventory();
      int var3 = var2.first(var1);
      if (var3 != -1) {
         ItemStack var4 = var2.getItem(var3);
         if (var4 == null) {
            var3 = -1;
         } else if (var4.getAmount() >= var4.getMaxStackSize()) {
            var3 = -1;
         }
      }

      if (var3 == -1) {
         var3 = var2.firstEmpty();
      }

      return var3 == -1 ? null : var3;
   }

   public static ItemStack method7263(ItemStack var0, int var1) {
      ItemStack var2 = var0.clone();
      var2.setAmount(var1);
      return var2;
   }

   public static boolean method7264(EquipmentSlot var0) {
      return var0 == EquipmentSlot.HEAD || var0 == EquipmentSlot.CHEST || var0 == EquipmentSlot.LEGS || var0 == EquipmentSlot.FEET;
   }

   public static ItemStack aF(ItemStack var0) {
      ItemStack var1 = new ItemStack(var0.getType());
      var1.setAmount(var0.getAmount());
      var1.setItemMeta(var0.getItemMeta());
      return var1;
   }

   public static boolean aG(ItemStack var0) {
      return aw(var0) < method7233(var0);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static EquipmentSlot ak(Material var0) {
      if (var0.toString().endsWith("_HELMET") || var0.toString().endsWith("_SCUTE")) {
         return EquipmentSlot.HEAD;
      }

      if (var0.toString().endsWith("_CHESTPLATE")) {
         return EquipmentSlot.CHEST;
      }

      if (var0.toString().endsWith("_LEGGINGS")) {
         return EquipmentSlot.LEGS;
      }

      if (var0.toString().endsWith("_BOOTS")) {
         return EquipmentSlot.FEET;
      }

      return switch (SwitchMap84.bMs[var0.ordinal()]) {
         case 53 -> EquipmentSlot.CHEST;
         case 54 -> EquipmentSlot.HEAD;
         case 55 -> EquipmentSlot.OFF_HAND;
         default -> null;
      };
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static boolean al(Material var0) {
      return switch (SwitchMap84.bMs[var0.ordinal()]) {
         case 56, 57, 58, 59 -> true;
         default -> false;
      };
   }

   public static ItemStack method7265(AnvilInventory var0) {
      return EnvironmentDetector.paperAdventure ? var0.getResult() : var0.getItem(2);
   }

   public static boolean am(Material var0) {
      return var0 == Material.POTION || var0 == Material.SPLASH_POTION || var0 == Material.LINGERING_POTION;
   }

   public static ItemMeta method7266(ItemStack var0, Color var1) {
      ItemMeta var2 = var0.getItemMeta();
      method7267(var2, var1);
      return var2;
   }

   public static void method7267(ItemMeta var0, Color var1) {
      switch (var0) {
         case PotionMeta var4:
            var4.setColor(var1);
            var4.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ADDITIONAL_TOOLTIP});
            break;
         case LeatherArmorMeta var5:
            var5.setColor(var1);
            var5.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_DYE});
            break;
         default:
      }
   }

   public static void method7268(ItemMeta var0) {
      switch (var0) {
         case PotionMeta var3:
            var3.setColor(null);
            var3.removeItemFlags(new ItemFlag[]{ItemFlag.HIDE_ADDITIONAL_TOOLTIP});
            break;
         case LeatherArmorMeta var4:
            var4.setColor(null);
            var4.removeItemFlags(new ItemFlag[]{ItemFlag.HIDE_DYE});
            break;
         default:
      }
   }

   @Nullable
   public static Integer aH(ItemStack var0) {
      Color var1 = aI(var0);
      return var1 == null ? null : var1.asRGB();
   }

   @Nullable
   public static Color aI(ItemStack var0) {
      if (var0 == null) {
         return null;
      }

      return switch (var0.getItemMeta()) {
         case PotionMeta var3 -> var3.getColor();
         case LeatherArmorMeta var4 -> var4.getColor();
         case null, default -> null;
      };
   }

   public static boolean aJ(ItemStack var0) {
      return var0 == null ? false : var0.getItemMeta() instanceof PotionMeta || var0.getItemMeta() instanceof LeatherArmorMeta;
   }

   public static boolean aK(@Nullable ItemStack var0) {
      return var0 != null && var0.getItemMeta() != null && var0.getItemMeta().hasEnchant(Enchantment.LOYALTY);
   }

   public static int aL(@Nullable ItemStack var0) {
      return var0 != null && var0.getItemMeta() != null ? var0.getItemMeta().getEnchantLevel(Enchantment.LOYALTY) : 0;
   }

   public static void ah(Location var0) {
      var0.getWorld().playSound(var0, Sound.ENTITY_ITEM_PICKUP, 0.2F, (Utils.random.nextFloat() - Utils.random.nextFloat()) * 1.4F + 2.0F);
   }
}
