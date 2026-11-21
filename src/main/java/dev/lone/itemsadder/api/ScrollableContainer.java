package dev.lone.itemsadder.api;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.gui.GuiSessionManager;
import dev.lone.itemsadder.gui.ScrollableGuiTitle;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ScrollableContainer {
   public static final int COLUMNS = 8;
   private final ScrollableGuiTitle title;
   private final GuiSessionManager container;

   public ScrollableContainer(@NotNull Plugin owner, @NotNull String title, int rows) {
      this.title = new ScrollableGuiTitle(title);
      this.container = new GuiSessionManager(owner, this.title, rows);
   }

   public ScrollableContainer(@NotNull Plugin owner, @NotNull Component title, int rows) {
      this.title = new ScrollableGuiTitle(title);
      this.container = new GuiSessionManager(owner, this.title, rows);
   }

   public ScrollableContainer setTitle(@NotNull Component title) {
      this.title.method2718(title);
      this.container.refresh();
      return this;
   }

   public ScrollableContainer setTitleMiniMessage(@NotNull String title) {
      this.title.eI(title);
      this.container.refresh();
      return this;
   }

   public ScrollableContainer setBackground(@Nullable FontImageWrapper background) {
      this.title.method2722(background);
      this.container.refresh();
      return this;
   }

   public ScrollableContainer setBackground(@Nullable FontImageWrapper background, int titleOffset, int backgroundOffset) {
      this.title.method2719(background, titleOffset, backgroundOffset);
      this.container.refresh();
      return this;
   }

   public ScrollableContainer setScrollbarTextures(@NotNull NamespacedKey track, @NotNull NamespacedKey thumb) {
      return this.setScrollbarTextures(track, thumb, null);
   }

   public ScrollableContainer setScrollbarTextures(@NotNull NamespacedKey track, @NotNull NamespacedKey thumb, @Nullable NamespacedKey fastThumb) {
      this.container.method2413(track, thumb, fastThumb);
      return this;
   }

   public void refresh() {
      this.container.refresh();
   }

   public ScrollableContainer setScrollButtons(@Nullable ItemStack up, @Nullable ItemStack down) {
      this.container.aIa.method2593(up, down);
      this.container.refresh();
      return this;
   }

   public ScrollableContainer setScrollbarModel(@Nullable NamespacedKey model) {
      this.container.aIa.method2594(model);
      this.container.refresh();
      return this;
   }

   public ScrollableContainer onChange(@Nullable Consumer handler) {
      this.container.method2404(handler);
      return this;
   }

   public ScrollableContainer onDrag(@Nullable BiConsumer handler) {
      this.container.method2407(handler);
      return this;
   }

   public ScrollableContainer onClose(@Nullable Consumer handler) {
      this.container.method2405(handler);
      return this;
   }

   public ScrollableContainer onClick(@Nullable BiConsumer handler) {
      this.container.method2406(handler);
      return this;
   }

   public int getRows() {
      return this.container.getRows();
   }

   public int getSize() {
      return this.container.getSize();
   }

   @NotNull
   public List getViewers() {
      return this.container.getViewers();
   }

   public int getLogicalSlot(@NotNull Player player, int rawSlot) {
      return this.container.getLogicalSlot(player, rawSlot);
   }

   public void setRows(int rows) {
      this.container.setRows(rows);
   }

   @Nullable
   public ItemStack getItem(int slot) {
      return this.container.getItem(slot);
   }

   public void setItem(int slot, @Nullable ItemStack item) {
      this.container.setItem(slot, item);
   }

   @NotNull
   public Map getContents() {
      return this.container.getContents();
   }

   public void open(@NotNull Player player) {
      this.container.open(player);
   }

   public void close() {
      this.container.close();
   }

   public void close(@NotNull Player player) {
      this.container.close(player);
   }
}
