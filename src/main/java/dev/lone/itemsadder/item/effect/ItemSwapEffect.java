package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.FurnitureItemEntity;
import dev.lone.itemsadder.item.ItemEntityAction;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemSwapEffect extends ItemEffect {
   public final CustomItemDefinition akd;
   public final boolean ake;
   public final CustomItemDefinition akf;
   public final boolean akg;

   public ItemSwapEffect(CustomItemDefinition var1, boolean var2, CustomItemDefinition var3, boolean var4) {
      this.akd = var1;
      this.ake = var2;
      this.akf = var3;
      this.akg = var4;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      if (var3 != null && var3.getType() != Material.AIR) {
         boolean var5 = false;
         if (this.akd != null && CustomItemDefinition.method8776(var3) != this.akd.nz()) {
            CustomItemDefinition.method8775(var3, this.akd.nz());
            var5 = true;
         } else if (this.akd != null && this.ake) {
            CustomItemDefinition.method8775(var3, var2.nz());
            var5 = true;
         }

         if (this.akf != null) {
            var5 |= isSameSwapItem(var3, this.akf, var2, this.akg);
         }

         return var5 ? EffectOutcome.failureWith(var3) : EffectOutcome.successWith(var3);
      } else {
         return EffectOutcome.successWith(var3);
      }
   }

   static boolean isSameSwapItem(ItemStack var0, ItemStack var1, ItemStack var2, boolean var3) {
      NamespacedKey var4 = getSwapKey(var0);
      NamespacedKey var5 = getSwapKey(var1);
      if (!Objects.equals(var4, var5)) {
         setSwapKey(var0, var5);
         return true;
      }

      if (var3) {
         NamespacedKey var6 = getSwapKey(var2);
         if (!Objects.equals(var4, var6)) {
            setSwapKey(var0, var6);
            return true;
         }
      }

      return false;
   }

   private static NamespacedKey getSwapKey(ItemStack var0) {
      try {
         ItemMeta var1 = var0.getItemMeta();
         return (NamespacedKey)var1.getClass().getMethod("getItemModel").invoke(var1);
      } catch (ReflectiveOperationException var2) {
         throw new IllegalStateException("Cannot read item model", var2);
      }
   }

   private static void setSwapKey(ItemStack var0, NamespacedKey var1) {
      try {
         ItemMeta var2 = var0.getItemMeta();
         var2.getClass().getMethod("setItemModel", NamespacedKey.class).invoke(var2, var1);
         var0.setItemMeta(var2);
      } catch (ReflectiveOperationException var3) {
         throw new IllegalStateException("Cannot set item model", var3);
      }
   }

   @Override
   public EffectOutcome applyOnInteract(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      Entity var4 = ItemEntityAction.aC(var1.getPlayer());
      if (var4 == null) {
         return this.applyEffect(var1, var2, var3, var1.getPlayer());
      }

      if (this.akd == null) {
         return this.applyEffect(var1, var2, var3, var1.getPlayer());
      }

      boolean var5 = FurnitureItemEntity.method401(this.akd, var2, var4);
      return var5 ? EffectOutcome.failure() : EffectOutcome.success();
   }
}
