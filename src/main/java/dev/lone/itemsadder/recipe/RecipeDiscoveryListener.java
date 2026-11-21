package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemEventHandler;
import dev.lone.itemsadder.item.ItemHandlerBinding;
import dev.lone.itemsadder.util.EventRegistrar;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class RecipeDiscoveryListener implements Listener {
   private final AbstractRecipeManager aBi;

   public RecipeDiscoveryListener(Plugin var1, AbstractRecipeManager var2) {
      this.aBi = var2;
      ContentManager.method8084().aK.method933(new ItemHandlerBinding(PlayerPickupItemEvent.class, this::method2216));
      ContentManager.method8084().aK.method933(new ItemHandlerBinding(InventoryClickEvent.class, this::method2217));
      EventRegistrar.registerIfAbsent(this, var1);
   }

   boolean method2215(Player var1, CustomItemDefinition var2) {
      if (this.aBi.bol.containsKey(var1)) {
         List var3 = (List)this.aBi.bol.get(var1);
         if (var3 != null && var3.contains(var2)) {
            return true;
         }
      }

      this.aBi.bol.computeIfAbsent(var1, var0 -> new ArrayList<>()).add(var2);
      return false;
   }

   @ItemEventHandler
   private void method2216(PlayerPickupItemEvent var1, CustomItemDefinition var2, ItemStack var3) {
      Player var4 = var1.getPlayer();
      if (!this.method2215(var4, var2)) {
         RecipeManagerHolder.method5354(var4, ContentManager.method8084().bp.method2234(var2));
         RecipeManagerHolder.method5354(var4, ContentManager.method8084().bp.method2235(var2));
      }
   }

   @ItemEventHandler
   private void method2217(InventoryClickEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (!var1.isCancelled()) {
         if (var1.getWhoClicked() instanceof Player var4) {
            if (!this.method2215(var4, var2)) {
               RecipeManagerHolder.method5354(var4, ContentManager.method8084().bp.method2234(var2));
               RecipeManagerHolder.method5354(var4, ContentManager.method8084().bp.method2235(var2));
            }
         }
      }
   }

   @EventHandler
   private void method2218(PlayerQuitEvent var1) {
      this.aBi.bol.remove(var1.getPlayer());
   }
}
