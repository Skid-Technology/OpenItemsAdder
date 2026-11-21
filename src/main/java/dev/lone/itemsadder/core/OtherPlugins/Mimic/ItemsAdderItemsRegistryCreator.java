package dev.lone.itemsadder.Core.OtherPlugins.Mimic;

import dev.lone.itemsadder.api.ItemsAdder;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import java.util.Collection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.endlesscode.mimic.items.BukkitItemsRegistry;

public class ItemsAdderItemsRegistryCreator implements BukkitItemsRegistry {
   public boolean isEnabled() {
      return ItemsAdder.areItemsLoaded();
   }

   @NotNull
   public String getId() {
      return "ia";
   }

   @NotNull
   public Collection getKnownIds() {
      return ContentManager.method8084().field92.keySet();
   }

   @Nullable
   public ItemStack getItem(@NotNull String namespacedId_itemsadder, @Nullable Object payload) {
      return this.getItem(namespacedId_itemsadder, 1);
   }

   @Nullable
   public ItemStack getItem(@NotNull String namespacedId_itemsadder, @Nullable Object payload, int amount) {
      return this.getItem(namespacedId_itemsadder, amount);
   }

   @Nullable
   public ItemStack getItem(@NotNull String namespacedId_itemsadder, int amount) {
      ItemStack var3 = ContentManager.method8084().method8179(namespacedId_itemsadder);
      if (var3 == null) {
         return null;
      }

      var3.setAmount(amount);
      return var3;
   }

   @Nullable
   public String getItemId(@NotNull ItemStack item) {
      return CustomItemDefinition.isCustomItem(item) ? CustomItemDefinition.method8750(item) : null;
   }

   public boolean isItemExists(@NotNull String namespacedId_itemsadder) {
      return ContentManager.method8084().method8155(namespacedId_itemsadder);
   }

   @Nullable
   public ItemStack getItem(@NotNull String namespacedId_itemsadder) {
      return ContentManager.method8084().method8179(namespacedId_itemsadder);
   }

   public boolean isSameItem(@NotNull ItemStack item, @NotNull String itemId) {
      return CustomItemDefinition.method8783(item, itemId);
   }
}
