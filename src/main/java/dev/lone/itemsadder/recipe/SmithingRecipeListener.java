package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.inventory.SmithingTransformRecipe;
import org.bukkit.plugin.Plugin;

public class SmithingRecipeListener implements RecipeType, IaListener {
   public final List aCH = new ArrayList();
   public final Map aCI = new HashMap();
   private final ContentManager aCJ;

   public SmithingRecipeListener(Plugin var1, ContentManager var2) {
      this.aCJ = var2;
      EventRegistrar.registerIfAbsent(this, var1);
   }

   public String Kc() {
      return "Smithing";
   }

   @Override
   public void unregister() {
      IaListener.super.unregister();
      this.aCH.clear();
   }

   public void method1099(SmithingRecipe var1, String var2, String var3) {
      NamespacedKey var4 = new NamespacedKey(var2, var3.toLowerCase());

      try {
         var4 = this.method1432(this.aCI, var1, var4, var2, var3);
      } catch (RecipeException var6) {
      }

      var1.method1582(var4);
      SmithingTransformRecipe var5 = new SmithingTransformRecipe(var4, var1.JR(), var1.aCw.aCt, var1.aCx.aCt, var1.aCy.aCt);
      var1.method1583(var5);
      this.aCH.add(var1);
      this.aCJ.bp.method2222(var1);
      this.aCJ.bp.method2226(var1);
      if (Settings.get().isLoggingEnabled("logging.recipes.smithing.loading")) {
         if (var1.JS() != null) {
            Log.log("Registered smithing recipe " + var1.getNamespacedKey() + " for: " + var1.JS().getNamespacedID());
         } else {
            Log.log("Registered smithing recipe " + var1.getNamespacedKey() + " for: " + var1.JR().getType());
         }
      }
   }

   public List method745(CustomItemDefinition var1) {
      return this.aCJ.bp.method2229(SmithingRecipe.class, var1);
   }

   public List method746(CustomItemDefinition var1) {
      return this.aCJ.bp.method2230(SmithingRecipe.class, var1);
   }

   public Set method747(CustomItemDefinition var1) {
      LinkedHashSet var2 = new LinkedHashSet(this.method745(var1));
      var2.addAll(this.method746(var1));
      return var2;
   }

   public SmithingRecipe method1100(ItemStack var1, ItemStack var2, ItemStack var3) {
      String var4 = CustomItemDefinition.isCustomItem(var2) ? CustomItemDefinition.method8750(var2) : null;

      for (SmithingRecipe var6 : this.aCH) {
         if ((!var6.aCx.Kw() || var4 != null && var6.aCx.dW(var4)) && var6.method1586(var1, var2, var3)) {
            return var6;
         }
      }

      return null;
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method1101(PrepareSmithingEvent var1) {
      if (var1.getView().getPlayer() instanceof Player var2) {
         SmithingInventory var7 = var1.getInventory();
         if (var7.getType() == InventoryType.SMITHING) {
            SmithingRecipe var4 = this.method1100(method1102(var7), method1103(var7), method1105(var7));
            if (var4 == null
               && var7.getRecipe() instanceof Keyed var5
               && var5.getKey().getNamespace().equals("minecraft")
               && (
                  CustomItemDefinition.isCustomItem(method1102(var7))
                     || CustomItemDefinition.isCustomItem(method1103(var7))
                     || CustomItemDefinition.isCustomItem(method1105(var7))
               )) {
               var1.setResult(new ItemStack(Material.AIR));
               return;
            }

            if (var4 != null) {
               if (!PermissionNodes.method1620(var2, var4)) {
                  var1.setResult(new ItemStack(Material.AIR));
                  SchedulerUtils.runLaterSync(() -> AbstractRecipeManager.method1419(var2, var4), 5L);
               } else {
                  var1.setResult(var4.method1585(method1103(var7)));
               }
            }
         }
      }
   }

   private static ItemStack method1102(SmithingInventory var0) {
      return var0.getItem(0);
   }

   private static ItemStack method1103(SmithingInventory var0) {
      return var0.getItem(1);
   }

   private static void method1104(SmithingInventory var0, ItemStack var1) {
      var0.setItem(1, var1);
   }

   private static ItemStack method1105(SmithingInventory var0) {
      return var0.getItem(2);
   }

   private static void method1106(SmithingInventory var0, ItemStack var1) {
      var0.setItem(2, var1);
   }

   private boolean method1107(InventoryClickEvent var1) {
      return var1.getRawSlot() == 3;
   }
}
