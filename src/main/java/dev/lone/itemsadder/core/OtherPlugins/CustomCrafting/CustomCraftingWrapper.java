package dev.lone.itemsadder.Core.OtherPlugins.CustomCrafting;

import java.util.function.Consumer;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.InventoryView;

public class CustomCraftingWrapper {
   public static CustomCraftingWrapper instance;
   static boolean isPetuFork = false;

   public static void init(Consumer msg) {
      try {
         Class.forName("me.wolfyscript.customcrafting.listeners.cooking.CookingManager");
         CustomCraftingPetuWrapper.init(msg);
         isPetuFork = true;
      } catch (ClassNotFoundException var2) {
      }
   }

   public static CustomCraftingWrapper inst() {
      if (instance == null) {
         instance = new CustomCraftingWrapper();
      }

      return instance;
   }

   public boolean isCustomCraftingRecipe(InventoryView view) {
      return isPetuFork ? CustomCraftingPetuWrapper.inst().isCustomCraftingRecipe(view) : CustomCraftingWolfyWrapper.inst().isCustomCraftingRecipe(view);
   }

   public boolean isCustomCraftingRecipe(FurnaceSmeltEvent e) {
      return isPetuFork ? CustomCraftingPetuWrapper.inst().isCustomCraftingRecipe(e) : CustomCraftingWolfyWrapper.inst().isCustomCraftingRecipe(e);
   }

   public boolean isCustomCraftingRecipe(PrepareAnvilEvent e) {
      return isPetuFork ? CustomCraftingPetuWrapper.inst().isCustomCraftingRecipe(e) : CustomCraftingWolfyWrapper.inst().isCustomCraftingRecipe(e);
   }
}
