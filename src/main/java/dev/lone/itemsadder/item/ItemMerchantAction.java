package dev.lone.itemsadder.item;

import dev.lone.itemsadder.api.Events.TradeMachineOpenEvent;
import dev.lone.itemsadder.api.Events.TradeMachinePreOpenEvent;
import dev.lone.itemsadder.lang.TextFormatCache;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import dev.lone.itemsadder.util.EventRegistrar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.Nullable;

public class ItemMerchantAction extends ItemActionBase {
   public final List merchantRecipes = new ArrayList();
   String aeA;
   CustomTexture aeB;
   CustomTexture aeC;
   int aeD;
   String iL;

   public ItemMerchantAction(CustomItemDefinition var1, String var2) {
      super(var1);
      this.aeA = var2;
   }

   @Nullable
   public String getPermission() {
      return this.iL;
   }

   public void cb(String var1) {
      this.iL = var1;
   }

   public void method376(CustomTexture var1) {
      this.aeB = var1;
   }

   public void method377(CustomTexture var1) {
      this.aeC = var1;
   }

   public void bq(int var1) {
      this.aeD = var1;
   }

   public void AQ() {
      if (this.aeB != null && this.aeC != null) {
         this.aeA = ChatColor.WHITE
            + TextFormatCache.method9634(this.aeB.getFormattedText(), -117 + this.aeC.getWidth() / 2)
            + TextFormatCache.method9634(this.aeC.getFormattedText(), -49);
      }
   }

   public void method378(ItemStack var1, ItemStack var2, ItemStack var3) {
      MerchantRecipe var4 = new MerchantRecipe(var1, 10000);
      var4.setExperienceReward(true);
      var4.setVillagerExperience(1000);
      var4.addIngredient(var2);
      if (var3 != null) {
         var4.addIngredient(var3);
      }

      this.merchantRecipes.add(var4);
   }

   public void method379(String var1, ItemStack var2, ItemStack var3) {
      this.method378(var2, var3, null);
   }

   public void aI(Player var1) {
      List var2 = this.merchantRecipes;
      TradeMachinePreOpenEvent var3 = new TradeMachinePreOpenEvent(var1, this, this.aeT, var2);
      if (EventRegistrar.callEvent(var3)) {
         Merchant var4 = Bukkit.createMerchant(this.aeA);
         var4.setRecipes(Collections.unmodifiableList(var2));
         if (var1.openMerchant(var4, true) != null) {
            EventRegistrar.callEvent(new TradeMachineOpenEvent(var1, this, this.aeT, var2));
         }
      }
   }

   @Generated
   public String getTitle() {
      return this.aeA;
   }
}
