package dev.lone.itemsadder.api;

import dev.lone.itemsadder.api.ItemsAdder.Advanced.ModifierHandler;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemsAdder$Advanced$InjectedItemModifier {
   public final String owner;
   @Nullable
   public final String item;
   public final ModifierHandler handler;

   public ItemsAdder$Advanced$InjectedItemModifier(String owner, @Nullable String item, ModifierHandler handler) {
      this.owner = owner;
      this.item = item;
      this.handler = handler;
   }

   public void call(String namespacedId, ItemStack item) {
      ItemStack var3 = this.handler.call(namespacedId, item);
      if (var3 != null) {
         item.setType(var3.getType());
         item.setItemMeta(var3.getItemMeta());
      }
   }
}
