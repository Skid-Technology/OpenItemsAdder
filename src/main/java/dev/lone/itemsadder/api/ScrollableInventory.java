package dev.lone.itemsadder.api;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.gui.GuiSessionRegistry;
import dev.lone.itemsadder.gui.ScrollableGuiTitle;
import java.util.List;
import java.util.function.BiConsumer;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ScrollableInventory {
   private final GuiSessionRegistry menu;
   private final ScrollableGuiTitle title;

   public ScrollableInventory(@NotNull Plugin owner, @NotNull String title, @NotNull List items, @NotNull BiConsumer onClick) {
      this.title = new ScrollableGuiTitle(title);
      this.menu = new GuiSessionRegistry(owner, this.title, items, onClick);
   }

   public ScrollableInventory(@NotNull Plugin owner, @NotNull Component title, @NotNull List items, @NotNull BiConsumer onClick) {
      this.title = new ScrollableGuiTitle(title);
      this.menu = new GuiSessionRegistry(owner, this.title, items, onClick);
   }

   public ScrollableInventory setTitle(@NotNull Component title) {
      this.title.method2718(title);
      this.menu.refresh();
      return this;
   }

   public ScrollableInventory setTitleMiniMessage(@NotNull String title) {
      this.title.eI(title);
      this.menu.refresh();
      return this;
   }

   public ScrollableInventory setBackground(@Nullable FontImageWrapper background) {
      return this.setBackground(background, 0, -8);
   }

   public ScrollableInventory setBackground(@Nullable FontImageWrapper background, int titleOffset, int backgroundOffset) {
      this.title.method2719(background, titleOffset, backgroundOffset);
      this.menu.refresh();
      return this;
   }

   public ScrollableInventory setScrollbarTextures(@NotNull NamespacedKey track, @NotNull NamespacedKey thumb) {
      return this.setScrollbarTextures(track, thumb, null);
   }

   public ScrollableInventory setScrollbarTextures(@NotNull NamespacedKey track, @NotNull NamespacedKey thumb, @Nullable NamespacedKey fastThumb) {
      this.menu.method2448(track, thumb, fastThumb);
      return this;
   }

   public void refresh() {
      this.menu.refresh();
   }

   public ScrollableInventory setScrollButtons(@Nullable ItemStack up, @Nullable ItemStack down) {
      this.menu.aIz.method2593(up, down);
      this.menu.refresh();
      return this;
   }

   public ScrollableInventory setScrollbarModel(@Nullable NamespacedKey model) {
      this.menu.aIz.method2594(model);
      this.menu.refresh();
      return this;
   }

   public void setItems(@NotNull List items) {
      this.menu.setItems(items);
   }

   public void setItem(int index, @NotNull ItemStack item) {
      this.menu.setItem(index, item);
   }

   public int getSize() {
      return this.menu.size();
   }

   @NotNull
   public ItemStack getItem(int index) {
      return this.menu.getItem(index);
   }

   public void open(@NotNull Player player) {
      this.menu.open(player);
   }

   public void close(@NotNull Player player) {
      this.menu.close(player);
   }
}
