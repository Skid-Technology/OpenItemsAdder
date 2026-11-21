package dev.lone.itemsadder.Core.OtherPlugins.CustomCrafting;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.function.Consumer;
import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.listeners.AnvilListener;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.InventoryView;

public class CustomCraftingPetuWrapper {
   public static CustomCraftingPetuWrapper instance;
   private static Consumer msg;
   private static Field field_preCraftedRecipes;

   public static void init(Consumer msg) {
      CustomCraftingPetuWrapper.msg = msg;
   }

   public static CustomCraftingPetuWrapper inst() {
      if (instance == null) {
         instance = new CustomCraftingPetuWrapper();
      }

      return instance;
   }

   public boolean isCustomCraftingRecipe(InventoryView view) {
      return CustomCrafting.inst().getCraftManager().has(view.getPlayer().getUniqueId());
   }

   public boolean isCustomCraftingRecipe(FurnaceSmeltEvent e) {
      return CustomCrafting.inst().getCookingManager().hasCustomRecipe(e);
   }

   public boolean isCustomCraftingRecipe(PrepareAnvilEvent e) {
      if (field_preCraftedRecipes == null) {
         return false;
      }

      try {
         HashMap var2 = (HashMap)field_preCraftedRecipes.get(null);
         return var2.containsKey(e.getView().getPlayer().getUniqueId());
      } catch (IllegalAccessException var3) {
         return false;
      }
   }

   static {
      try {
         field_preCraftedRecipes = FieldUtils.getField(AnvilListener.class, "preCraftedRecipes", true);
      } catch (Exception var1) {
         msg.accept("Failed to register Anvil recipes compatibility for CustomCrafting plugin: " + var1.getMessage());
      }
   }
}
