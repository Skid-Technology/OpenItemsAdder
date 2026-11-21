package dev.lone.itemsadder.recipe;

import beer.devs.fastnbt.nms.nbt.NItem;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.CustomCrafting.CustomCraftingWrapper;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.plugin.Plugin;

public class AnvilRecipeListener implements RecipeType, IaListener {
   public final ArrayList aBG = new ArrayList();

   public AnvilRecipeListener(Plugin var1) {
      EventRegistrar.registerIfAbsent(this, var1);
   }

   @Override
   public void unregister() {
      IaListener.super.unregister();
      this.aBG.clear();
   }

   public String Kc() {
      return "Anvil";
   }

   public void method907(AnvilRecipe var1, String var2, String var3) {
      this.aBG.add(var1);
      if (Settings.get().isLoggingEnabled("logging.recipes.anvil.loading")) {
         if (var1.JS() != null) {
            Log.log("Registered anvil recipe " + var1.getNamespacedKey() + " for: " + var1.JS().getNamespacedID());
         } else {
            Log.log("Registered anvil recipe " + var1.getNamespacedKey() + " for: " + var1.JR().getType());
         }
      }
   }

   public AnvilRecipe method908(ItemStack var1) {
      boolean var2 = CustomItemDefinition.isCustomItem(var1);
      CustomItemDefinition var3 = var2 ? ContentManager.method8084().method8149(var1) : null;

      for (AnvilRecipe var5 : this.aBG) {
         CustomItemDefinition var6 = var5.JS();
         if (var6 != null) {
            if (var3 != null && var3.aol == var6.aol) {
               return var5;
            }
         } else if (!var2 && var1.getType() == var5.Kd().getType()) {
            return var5;
         }
      }

      return null;
   }

   public List method745(CustomItemDefinition var1) {
      ArrayList var2 = new ArrayList();

      for (AnvilRecipe var4 : this.aBG) {
         if (var1.method8778(var4.JS())) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public List method746(CustomItemDefinition var1) {
      ArrayList var2 = new ArrayList();

      for (AnvilRecipe var4 : this.aBG) {
         if (var1.method8778(var4.JS())) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public Set method747(CustomItemDefinition var1) {
      LinkedHashSet var2 = new LinkedHashSet(this.method745(var1));
      var2.addAll(this.method746(var1));
      return var2;
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method909(PrepareAnvilEvent var1) {
      AnvilInventory var2 = var1.getInventory();
      ItemStack var3 = var2.getItem(0);
      ItemStack var4 = var2.getItem(1);
      if (var3 != null && var3.getType() != Material.AIR) {
         if (var4 != null && var4.getType() != Material.AIR) {
            if (var4.getType() != Material.ENCHANTED_BOOK) {
               if (!Main.aEW || !CustomCraftingWrapper.inst().isCustomCraftingRecipe(var1)) {
                  Player var5 = (Player)var1.getView().getPlayer();
                  boolean var6 = CustomItemDefinition.method8780(var3, var4);
                  if (!var6 && var3.getType() == var4.getType()) {
                     if (CustomItemDefinition.isCustomItem(var3) || CustomItemDefinition.isCustomItem(var4)) {
                        this.method912(var1);
                     }
                  } else if (var6) {
                     SchedulerUtils.runLaterSync(() -> {
                        ItemStack var2x = ItemStackUtils.method7265(var2);
                        if (CustomItemDefinition.method8780(var2.getItem(0), var2x)) {
                           var2.setItem(2, var2x);
                           var1.setResult(var2x);
                        } else {
                           var1.setResult(null);
                        }
                     }, 1L);
                  } else {
                     AnvilRecipe var7 = this.method908(var3);
                     if (var7 == null) {
                        if (!CustomItemDefinition.method8780(var3, var1.getResult())
                           || !var3.getItemMeta().getAsComponentString().contains("minecraft:repairable=")) {
                           if (CustomItemDefinition.isCustomItem(var3) || CustomItemDefinition.isCustomItem(var4)) {
                              this.method912(var1);
                           }
                        }
                     } else if (!var7.method1584(var4)) {
                        if (CustomItemDefinition.method8780(var3, var1.getResult())) {
                           this.method912(var1);
                        }
                     } else if (!PermissionNodes.method1620(var1.getView().getPlayer(), var7)) {
                        this.method912(var1);
                        AbstractRecipeManager.method1418(var5, var7);
                     } else {
                        ItemStack var8 = var3.clone();
                        if (!ItemStackUtils.aG(var8)) {
                           this.method912(var1);
                        } else {
                           int var9 = CustomItemDefinition.method8814(var8);
                           double var10 = var9 / 4.0;
                           int var12 = var9 - ItemStackUtils.aw(var8);
                           int var13 = Math.min(var4.getAmount(), (int)Math.ceil(var12 / var10));
                           int var14 = Math.min(var12, (int)(var10 * var13));
                           var2.setRepairCostAmount(var13);
                           ItemStackUtils.method7236(var8, var14 + 1);
                           var1.setResult(var8);
                           int var15 = var13;
                           SchedulerUtils.runLaterSync(
                              () -> {
                                 ItemStack var5x = var2.getItem(0);
                                 if (var5x == null
                                    || (!CustomItemDefinition.isCustomItem(var7.Kd()) || !CustomItemDefinition.method8780(var5x, var8))
                                       && var5x.getType() != var8.getType()) {
                                    var1.setResult(null);
                                 } else {
                                    Repairable var6x = (Repairable)var8.getItemMeta();
                                    var2.setRepairCost(var6x.getRepairCost() + var15);
                                    var6x.setRepairCost(var6x.getRepairCost() * 2 + 1);
                                    var8.setItemMeta(var6x);
                                    var2.setItem(2, var8);
                                    var1.setResult(var8);
                                 }
                              },
                              1L
                           );
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void method910(InventoryClickEvent var1) {
      if (var1.getInventory().getType() == InventoryType.ANVIL) {
         if (var1.getRawSlot() == 2) {
            if (var1.getInventory() instanceof AnvilInventory var2) {
               this.method911(var1, var2);
            }
         }
      }
   }

   void method911(InventoryClickEvent var1, AnvilInventory var2) {
      ItemStack var3 = var2.getItem(0);
      ItemStack var4 = var1.getCursor();
      if (var3 != null) {
         if (var4 != null && var4.getType() != Material.AIR) {
            if (var4.getType() != Material.ENCHANTED_BOOK) {
               if (ItemStackUtils.method7217(var1)) {
                  if (var3.getType() == var4.getType() && !CustomItemDefinition.method8780(var3, var4)) {
                     var1.setCancelled(true);
                     SchedulerUtils.runLaterSync(() -> {
                        var2.setItem(2, null);
                        ((Player)var1.getWhoClicked()).updateInventory();
                     }, 0L);
                  }
               }
            }
         }
      }
   }

   void method912(PrepareAnvilEvent var1) {
      if (Main.aEV) {
         ItemStack var2 = var1.getInventory().getItem(1);
         if (var2 != null && var2.getType() != Material.AIR && var2.hasItemMeta() && new NItem(var2).hasKey("isHyperStone")) {
            return;
         }
      }

      Player var3 = (Player)var1.getView().getPlayer();
      var1.setResult(null);
      var1.getInventory().setItem(2, new ItemStack(Material.AIR));
      var1.setResult(new ItemStack(Material.AIR));
      SchedulerUtils.runLaterSync(() -> {
         var1.setResult(null);
         var1.getInventory().setItem(2, new ItemStack(Material.AIR));
         var1.setResult(new ItemStack(Material.AIR));
         var3.updateInventory();
      }, 1L);
   }
}
