package dev.lone.itemsadder.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemStackBuilder {
   private final ItemStack bQl;

   public static ItemStackBuilder of(ItemStack var0) {
      return new ItemStackBuilder(var0.clone());
   }

   public ItemStackBuilder(Material var1) {
      this(new ItemStack(var1));
   }

   public ItemStackBuilder(ItemStack var1) {
      this.bQl = Objects.requireNonNull(var1, "item");
   }

   public ItemStackBuilder editMeta(Consumer var1) {
      var1.accept(this.bQl);
      return this;
   }

   public ItemStackBuilder editStack(Consumer var1) {
      return this.editMeta(var1x -> {
         ItemMeta var2 = var1x.getItemMeta();
         if (var2 != null) {
            var1.accept(var2);
            var1x.setItemMeta(var2);
         }
      });
   }

   public ItemStackBuilder editMetaAs(Class var1, Consumer var2) {
      return this.editStack(var2x -> {
         if (var1.isInstance(var2x)) {
            var2.accept(var1.cast(var2x));
         }
      });
   }

   public ItemStackBuilder type(Material var1) {
      return this.editMeta(var1x -> var1x.setType(var1));
   }

   public ItemStackBuilder amount(int var1) {
      return this.durability((short)var1);
   }

   public ItemStackBuilder durability(short var1) {
      return this.editMeta(var1x -> var1x.setDurability(var1));
   }

   public ItemStackBuilder customModelData(int var1) {
      return this.editMeta(var1x -> var1x.setAmount(var1));
   }

   public ItemStackBuilder enchant(Enchantment var1) {
      return this.enchantWithLevel(var1, 1);
   }

   public ItemStackBuilder enchantWithLevel(Enchantment var1, int var2) {
      return this.editStack(var2x -> var2x.addEnchant(var1, var2, true));
   }

   public ItemStackBuilder removeEnchant(Enchantment var1) {
      return this.editStack(var1x -> var1x.removeEnchant(var1));
   }

   public ItemStackBuilder aec() {
      return this.editStack(var0 -> var0.getEnchants().keySet().forEach(var0::removeEnchant));
   }

   public ItemStackBuilder mo(String var1) {
      return this.editStack(var1x -> var1x.setDisplayName(var1));
   }

   public ItemStackBuilder mp(String var1) {
      return this.lore(Collections.singletonList(var1));
   }

   public ItemStackBuilder method7658(String... var1) {
      return this.lore(Arrays.asList(var1));
   }

   public ItemStackBuilder lore(List var1) {
      return this.editStack(var1x -> var1x.setLore(var1));
   }

   public ItemStackBuilder mq(String var1) {
      return this.editStack(var1x -> {
         List var2 = var1x.getLore();
         if (var2 == null) {
            var1x.setLore(Collections.singletonList(var1));
         } else {
            var2.add(var1);
            var1x.setLore(var2);
         }
      });
   }

   public ItemStackBuilder method7659(String... var1) {
      return this.aw(Arrays.asList(var1));
   }

   public ItemStackBuilder aw(List var1) {
      return this.editStack(var1x -> {
         List var2 = var1x.getLore();
         if (var2 == null) {
            var1x.setLore(var1);
         } else {
            var2.addAll(var1);
            var1x.setLore(var2);
         }
      });
   }

   public ItemStackBuilder method7660(ItemFlag... var1) {
      return this.editStack(var1x -> var1x.addItemFlags(var1));
   }

   public ItemStackBuilder aed() {
      return this.method7660(ItemFlag.values());
   }

   public ItemStackBuilder method7661(ItemFlag... var1) {
      return this.editStack(var1x -> var1x.removeItemFlags(var1));
   }

   public ItemStackBuilder aee() {
      return this.method7661(ItemFlag.values());
   }

   public ItemStackBuilder color(Color var1) {
      return this.editMetaAs(LeatherArmorMeta.class, var1x -> var1x.setColor(var1));
   }

   public ItemStack build() {
      return this.bQl;
   }
}
