package dev.lone.itemsadder.api.FontImages;

import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.lang.TextFormatCache;
import dev.lone.itemsadder.text.PendingMessageStore;
import dev.lone.itemsadder.util.AdventureText;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TexturedInventoryWrapper {
   private final Inventory internal;
   Component component;

   public TexturedInventoryWrapper(@Nullable InventoryHolder owner, int size, String title, int titleOffset, int textureOffset, FontImageWrapper... textures) {
      this(owner, null, size, title, titleOffset, textureOffset, textures);
   }

   public TexturedInventoryWrapper(
      @Nullable InventoryHolder owner, @NotNull InventoryType type, String title, int titleOffset, int textureOffset, FontImageWrapper... textures
   ) {
      this(owner, type, 0, title, titleOffset, textureOffset, textures);
   }

   public TexturedInventoryWrapper(@Nullable InventoryHolder owner, int size, int textureOffset, FontImageWrapper... textures) {
      this(owner, size, null, 0, textureOffset, textures);
   }

   public TexturedInventoryWrapper(@Nullable InventoryHolder owner, @NotNull InventoryType type, int textureOffset, FontImageWrapper... textures) {
      this(owner, type, 0, null, 0, textureOffset, textures);
   }

   private TexturedInventoryWrapper(
      @Nullable InventoryHolder owner, InventoryType type, int size, String title, int titleOffset, int textureOffset, FontImageWrapper... textures
   ) {
      if (type != null) {
         this.internal = Bukkit.createInventory(owner, type, "IA_CUST_GUI");
      } else if (size > 0) {
         this.internal = Bukkit.createInventory(owner, size, "IA_CUST_GUI");
      } else {
         this.internal = Bukkit.createInventory(owner, 54, "IA_CUST_GUI");
      }

      this.component = Component.text("");
      this.component = this.component.style(builder -> {
         builder.decoration(TextDecoration.ITALIC, false);
         builder.color(AdventureText.defaultColor);
      });
      int var8 = 0;

      for (FontImageWrapper var12 : textures) {
         var8 += var12.getInternal().getWidth();
         this.component = this.component.append(Component.text(var12.internalGetUnicode()).color(var12.getColor()));
      }

      this.component = TextFormatCache.method9635(this.component, textureOffset);
      if (title != null && !title.isEmpty()) {
         this.component = this.component.append(TextFormatCache.method9635(AdventureText.fromLegacy(title), -var8 + textureOffset + titleOffset));
      }
   }

   public TexturedInventoryWrapper(
      @Nullable InventoryHolder owner, @NotNull InventoryType type, String title, FontImageWrapper texture, int titleOffset, int textureOffset
   ) {
      this.internal = Bukkit.createInventory(owner, type, "IA_CUST_GUI");
      this.component = AdventureText.fromLegacy(ChatColor.WHITE + TextFormatCache.method9634(texture.getInternal().getFormattedText(), textureOffset));
      if (title != null && !title.isEmpty()) {
         this.component = this.component
            .append(TextFormatCache.method9635(AdventureText.fromLegacy(title), -texture.getInternal().getWidth() + textureOffset + titleOffset));
      }
   }

   public TexturedInventoryWrapper(@Nullable InventoryHolder owner, int size, String title, FontImageWrapper texture, int titleOffset, int textureOffset) {
      this.internal = Bukkit.createInventory(owner, size, "IA_CUST_GUI");
      this.component = AdventureText.fromLegacy(ChatColor.WHITE + TextFormatCache.method9634(texture.getInternal().getFormattedText(), textureOffset));
      if (title != null && !title.isEmpty()) {
         this.component = this.component
            .append(TextFormatCache.method9635(AdventureText.fromLegacy(title), -texture.getInternal().getWidth() + textureOffset + titleOffset));
      }
   }

   public TexturedInventoryWrapper(@Nullable InventoryHolder owner, int size, FontImageWrapper texture, int textureOffset) {
      this(owner, size, null, texture, 0, textureOffset);
   }

   @Deprecated
   public TexturedInventoryWrapper(@Nullable InventoryHolder owner, @NotNull InventoryType type, String title, FontImageWrapper texture) {
      this(owner, type, title, texture, 16, -16);
   }

   @Deprecated
   public TexturedInventoryWrapper(@Nullable InventoryHolder owner, @NotNull InventoryType type, String title, FontImageWrapper texture, int titleOffset) {
      this(owner, type, title, texture, titleOffset, -16);
   }

   @Deprecated
   public TexturedInventoryWrapper(@Nullable InventoryHolder owner, int size, String title, FontImageWrapper texture) {
      this(owner, size, title, texture, 16, -16);
   }

   @Deprecated
   public TexturedInventoryWrapper(@Nullable InventoryHolder owner, int size, String title, FontImageWrapper texture, int titleOffset) {
      this(owner, size, title, texture, titleOffset, -16);
   }

   public static void setPlayerInventoryTexture(Player player, FontImageWrapper texture, String title) {
      setPlayerInventoryTexture(player, texture, title, 16, -16);
   }

   public static void setPlayerInventoryTexture(Player player, FontImageWrapper texture) {
      setPlayerInventoryTexture(player, texture, null, 16, -16);
   }

   public static void setPlayerInventoryTexture(Player player, FontImageWrapper texture, int titleOffset) {
      setPlayerInventoryTexture(player, texture, null, titleOffset, -16);
   }

   public static void setPlayerInventoryTexture(Player player, FontImageWrapper texture, String title, int titleOffset, int textureOffset) {
      if (title == null) {
         title = player.getOpenInventory().getTitle();
      }

      TextComponent var5 = Component.text(ChatColor.WHITE + TextFormatCache.method9634(texture.getInternal().getFormattedText(), textureOffset));
      Component var6 = var5.append(TextFormatCache.method9635(AdventureText.fromLegacy(title), -texture.getInternal().getWidth() + textureOffset + titleOffset));
      ItemOperationsService.getRegistryBridge().method3426(player, var6);
   }

   @Deprecated
   public Inventory getInternal() {
      return this.internal;
   }

   public void showInventory(Player player) {
      PendingMessageStore.method2753(player, this.component);
      player.openInventory(this.internal);
   }
}
