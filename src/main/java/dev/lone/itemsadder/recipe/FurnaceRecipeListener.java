package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.CustomCrafting.CustomCraftingWrapper;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.internal.SwitchMap6;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.plugin.Plugin;

public class FurnaceRecipeListener implements RecipeType, IaListener {
   public final List aBP = new ArrayList();
   public final Map aBQ = new HashMap();
   private final ContentManager aBR;

   public FurnaceRecipeListener(Plugin var1, ContentManager var2) {
      this.aBR = var2;
      EventRegistrar.registerIfAbsent(this, var1);
   }

   @Override
   public void unregister() {
      IaListener.super.unregister();
      this.aBP.clear();
      this.aBQ.clear();
   }

   public String Kc() {
      return "Cooking";
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public void method989(CookingRecipe var1, String var2, String var3) {
      for (CookingRecipe var5 : this.aBP) {
         if (var5.aBN == var1.aBN && var5.method1589(var1.aBK.aBI)) {
            Log.error(
               "Cooking recipe '"
                  + var1.getNamespacedKey()
                  + "' not registered. A recipe '"
                  + var5.getNamespacedKey()
                  + "' already exists with the same ingredient. Ingredients must be unique. File: "
                  + var5.DJ()
            );
            return;
         }
      }

      NamespacedKey var12 = new NamespacedKey(var2, var1.Kj().toString().toLowerCase() + "_" + var3.toLowerCase());

      try {
         var12 = this.method1432(this.aBQ, var1, var12, var2, var3);
      } catch (RecipeException var11) {
      }

      var1.method1582(var12);

      Object var13 = switch (SwitchMap6.aBS[var1.Kj().ordinal()]) {
         case 1 -> new FurnaceRecipe(var12, var1.JR(), var1.Kg().Kf(), var1.Kh(), var1.Ki());
         case 2 -> new BlastingRecipe(var12, var1.JR(), var1.Kg().Kf(), var1.Kh(), var1.Ki());
         case 3 -> new SmokingRecipe(var12, var1.JR(), var1.Kg().Kf(), var1.Kh(), var1.Ki());
         default -> null;
      };
      if (var13 != null) {
         CustomItemDefinition var6 = ContentManager.method8084().method8149(var1.Kg().aBI);
         if (var6 != null && !RecipeManagerHolder.Qb().PZ()) {
            NamespacedKey var7 = new NamespacedKey(AbstractRecipeManager.boe + var12.getNamespace(), var12.getKey());
            ItemStack var8 = new ItemStack(Material.STICK);
            MaterialChoice var9 = new MaterialChoice(var6.getType());

            Object var10 = switch (SwitchMap6.aBS[var1.Kj().ordinal()]) {
               case 1 -> new FurnaceRecipe(var7, var8, var9, var1.Kh(), var1.Ki());
               case 2 -> new BlastingRecipe(var7, var8, var9, var1.Kh(), var1.Ki());
               case 3 -> new SmokingRecipe(var7, var8, var9, var1.Kh(), var1.Ki());
               default -> null;
            };
            if (var10 != null) {
               var10.setCookingTime(600);
               var10.setExperience(0.0F);
               RecipeRegistry.method2236((Recipe)var10);
            } else {
               Log.error("Failed to register fake recipe '" + var7 + "' for custom cooking. Invalid furance type '" + var1.Kj() + "'. This is a bug.");
            }
         }

         var1.method1588((org.bukkit.inventory.CookingRecipe)var13);
         var1.method1583((Recipe)var13);
         this.aBP.add(var1);
         this.aBQ.put(var1.getNamespacedKey().toString(), var1);
         this.aBR.bp.method2222(var1);
         this.aBR.bp.method2223(var1);
      }

      if (Settings.get().isLoggingEnabled("logging.recipes.cooking.loading")) {
         if (var1.JS() != null) {
            Log.log("Registered cooking recipe " + var1.getNamespacedKey() + " for: " + var1.JS().getNamespacedID());
         } else {
            Log.log("Registered cooking recipe " + var1.getNamespacedKey() + " for: " + var1.JR().getType());
         }
      }
   }

   public List method745(CustomItemDefinition var1) {
      return this.aBR.bp.method2229(CookingRecipe.class, var1);
   }

   public List method746(CustomItemDefinition var1) {
      return this.aBR.bp.method2230(CookingRecipe.class, var1);
   }

   public Set method747(CustomItemDefinition var1) {
      LinkedHashSet var2 = new LinkedHashSet(this.method745(var1));
      var2.addAll(this.method746(var1));
      return var2;
   }

   public CookingRecipe method990(ItemStack var1, Material var2) {
      CustomItemDefinition var3 = ContentManager.method8084().method8149(var1);
      if (var3 == null) {
         for (CookingRecipe var9 : this.aBP) {
            if (!var9.Kg().aBJ && var9.Kj() == var2 && var9.method1589(var1)) {
               return var9;
            }
         }

         return null;
      } else {
         LinkedList var4 = ContentManager.method8084().bp.method2235(var3);
         if (var4 != null) {
            for (AbstractCustomRecipe var6 : var4) {
               if (var6 instanceof CookingRecipe var7 && var7.Kj() == var2 && var7.method1589(var1)) {
                  return var7;
               }
            }
         }

         return null;
      }
   }

   public boolean method991(Recipe var1) {
      return !(var1 instanceof Keyed var2)
         ? false
         : var2.getKey().getNamespace().startsWith(AbstractRecipeManager.boe) || var2.getKey().getNamespace().equals("minecraft");
   }

   public boolean method992(Recipe var1) {
      return var1 instanceof Keyed var2 ? var2.getKey().getNamespace().startsWith(AbstractRecipeManager.boe) : false;
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void method993(FurnaceBurnEvent var1) {
      if (var1.getBlock().getState() instanceof Furnace var2) {
         ItemStack var4 = var2.getInventory().getSmelting();
         if (CustomItemDefinition.isCustomItem(var4) && this.method990(var4, var1.getBlock().getType()) == null) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   private void method994(PlayerInteractEvent var1) {
      if (var1.getAction() == Action.RIGHT_CLICK_BLOCK
         && var1.getClickedBlock() != null
         && var1.getItem() != null
         && (var1.getClickedBlock().getType() == Material.CAMPFIRE || var1.getClickedBlock().getType() == Material.SOUL_CAMPFIRE)
         && var1.getItem().getType().isEdible()
         && CustomItemDefinition.isCustomItem(var1.getItem())) {
         var1.setUseInteractedBlock(Result.DENY);
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void method995(BlockCookEvent var1) {
      if ((var1.getBlock().getType() == Material.CAMPFIRE || var1.getBlock().getType() == Material.SOUL_CAMPFIRE)
         && CustomItemDefinition.isCustomItem(var1.getSource())) {
         var1.setCancelled(true);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method996(FurnaceStartSmeltEvent var1) {
      org.bukkit.inventory.CookingRecipe var2 = var1.getRecipe();
      ItemStack var3 = var1.getSource();
      if (var1.getBlock().getState() instanceof Furnace var4) {
         boolean var7 = CustomItemDefinition.isCustomItem(var3);
         if (!this.method992(var2) || var7) {
            CookingRecipe var6 = null;
            if (this.method991(var2)) {
               if (var7) {
                  var6 = this.method990(var3, var1.getBlock().getType());
               }
            } else {
               var6 = (CookingRecipe)this.aBR.bp.dV(var2.getKey().toString());
            }

            if (var6 != null && var6.aK(var1.getBlock())) {
               var1.setTotalCookTime(var6.aBM);
               RecipeManagerHolder.Qb().method601(var4, var6.getNamespacedKey());
            } else if (var7) {
               var1.setTotalCookTime(Integer.MAX_VALUE);
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method997(FurnaceSmeltEvent var1) {
      if (!Main.aEW || !CustomCraftingWrapper.inst().isCustomCraftingRecipe(var1)) {
         NamespacedKey var2 = RecipeManagerHolder.Qb().method600((Furnace)var1.getBlock().getState());
         if (var2 != null) {
            if (var2.getNamespace().startsWith(AbstractRecipeManager.boe)) {
               if (!CustomItemDefinition.isCustomItem(var1.getSource())) {
                  var1.setResult(ItemStackUtils.method7263(var1.getSource(), 1));
                  return;
               }

               CookingRecipe var3 = this.method990(var1.getSource(), var1.getBlock().getType());
               if (var3 == null || !var3.aK(var1.getBlock())) {
                  var1.setResult(ItemStackUtils.method7263(var1.getSource(), 1));
                  return;
               }

               if (var3.method1589(var1.getSource())) {
                  var1.setResult(var3.JR());
                  SchedulerUtils.runLaterSync(
                     () -> {
                        if (var1.getBlock().getState() instanceof Furnace var1x) {
                           ItemStack var5 = var1x.getInventory().getResult();
                           if (!ItemStackUtils.aE(var5)
                              && !var1x.getInventory().getViewers().isEmpty()
                              && var1x.getInventory().getViewers().getFirst() instanceof Player var3x) {
                              ItemStack var6 = var5.clone();
                              var1x.getInventory().setResult(null);
                              ItemStackUtils.method7243(var3x, var6);
                           }
                        }
                     },
                     3L
                  );
               } else {
                  Log.error("Recipe '" + var3.getNamespacedKey() + "' is not matching the item in the furnace. This is a bug.");
                  var1.setResult(ItemStackUtils.method7263(var1.getSource(), 1));
               }
            } else if (CustomItemDefinition.isCustomItem(var1.getSource()) && this.method990(var1.getSource(), var1.getBlock().getType()) == null) {
               var1.setCancelled(true);
            }
         }
      }
   }

   private static boolean method998(Inventory var0) {
      return var0 instanceof FurnaceInventory
         && (var0.getType() == InventoryType.BLAST_FURNACE || var0.getType() == InventoryType.FURNACE || var0.getType() == InventoryType.SMOKER);
   }
}
