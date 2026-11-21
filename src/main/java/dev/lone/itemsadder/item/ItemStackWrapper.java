package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.nbt.NItem;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.core.ContentManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemStackWrapper {
   NItem anT;
   final ItemStack apj;
   Boolean apk;
   Boolean apl;
   Boolean apm;

   public ItemStackWrapper(ItemStack var1) {
      this.apj = var1;
      if (this.Ed()) {
         this.anT = new NItem(var1);
      }
   }

   @Nullable
   private String cS(String var1) {
      return this.anT.hasKey("itemsadder") ? this.anT.getCompound("itemsadder").getString(var1) : null;
   }

   public boolean Eb() {
      if (this.anT == null) {
         return false;
      }

      if (this.apl == null && this.Ed()) {
         this.apl = this.anT.hasKey("itemsadder");
      }

      return this.apl;
   }

   public boolean Ec() {
      if (this.anT == null) {
         return false;
      }

      if (this.apl != null && !this.apl) {
         return false;
      }

      if (this.apm == null) {
         this.apm = ContentManager.method8084().method8154(this.getNamespacedID()) instanceof CustomBlockDefinition;
      }

      return this.apm;
   }

   public static boolean method9990(ItemStack var0) {
      return var0 != null && var0.getType() != Material.AIR ? NItem.hasNBT(var0) : false;
   }

   public boolean Ed() {
      if (this.apk == null) {
         this.apk = method9990(this.apj);
      }

      return this.apk;
   }

   public boolean method9991(ItemStack var1) {
      return this.Eb() && CustomItemDefinition.isCustomItem(var1)
         ? CustomItemDefinition.method8750(this.apj).equals(CustomItemDefinition.method8750(var1))
         : false;
   }

   public boolean cT(String var1) {
      return !this.Eb() ? false : this.getNamespacedID().equals(var1);
   }

   public String getNamespacedID() {
      return this.cS("namespace") + ":" + this.cS("id");
   }

   public String getNamespace() {
      return this.cS("namespace");
   }

   public String Ee() {
      return this.cS("id");
   }
}
