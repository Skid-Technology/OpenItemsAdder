package dev.lone.itemsadder.api.FontImages;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.fontimage.EmojiReplacer;
import dev.lone.itemsadder.lang.MessageFormatter;
import dev.lone.itemsadder.lang.TextFormatCache;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

public class FontImageWrapper {
   private final CustomTexture internal;
   private TextColor color;
   private int offset;

   public FontImageWrapper(String namespacedID) {
      if (namespacedID != null && !namespacedID.isEmpty()) {
         ContentManager var2 = ContentManager.method8084();
         MessageFormatter var3 = var2 == null ? null : var2.method8109();
         this.internal = var3 == null ? null : (CustomTexture)var3.Yp.get(namespacedID);
         if (this.internal == null) {
            throw new IllegalArgumentException("FontImageWrapper: FontImage with namespacedID '" + namespacedID + "' does not exist");
         }

         this.color = TextColor.fromHexString("#FFFFFF");
         this.offset = 0;
      } else {
         throw new IllegalArgumentException("FontImageWrapper: namespacedID cannot be null or empty");
      }
   }

   public static FontImageWrapper instance(String namespacedID) {
      try {
         return new FontImageWrapper(namespacedID);
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }

   public static Set getNamespacedIdsInRegistry() {
      ContentManager var0 = ContentManager.method8084();
      MessageFormatter var1 = var0 == null ? null : var0.method8109();
      return var1 == null ? Collections.emptySet() : var1.Yp.keySet();
   }

   public static Map getNamespacedIdsAndValueInRegistry() {
      ContentManager var0 = ContentManager.method8084();
      MessageFormatter var1 = var0 == null ? null : var0.method8109();
      return var1 == null
         ? Collections.emptyMap()
         : var1.Yp.entrySet().stream().collect(Collectors.toMap(Entry::getKey, e -> ((CustomTexture)e.getValue()).getDisplayText(false)));
   }

   public static String replaceFontImages(String string) {
      return EmojiReplacer.replaceFontImages(string);
   }

   @NotNull
   public static Component replaceFontImages(Component adventureComponent) {
      return EmojiReplacer.method2333(adventureComponent);
   }

   public static String replaceFontImages(Permissible player, String string) {
      return EmojiReplacer.replaceFontImages(player, string);
   }

   @NotNull
   public static Component replaceFontImages(Permissible player, Component adventureComponent) {
      return EmojiReplacer.method2334(player, adventureComponent);
   }

   public static String applyPixelsOffsetToString(String str, int offset) {
      return TextFormatCache.method9634(str, offset);
   }

   public CustomTexture getInternal() {
      return this.internal;
   }

   public String getNamespacedID() {
      return this.internal.getNamespacedID();
   }

   public boolean exists() {
      return this.internal != null;
   }

   public String getString() {
      return TextFormatCache.method9634(this.internal.getFormattedText(), this.offset);
   }

   public int getWidth() {
      return this.internal.getWidth();
   }

   public int getHeight() {
      return this.internal.getHeight();
   }

   @Deprecated
   public String applyPixelsOffset(int offset) {
      this.offset = offset;
      return TextFormatCache.method9634(this.internal.getFormattedText(), offset);
   }

   public TextColor getColor() {
      return this.color;
   }

   public FontImageWrapper setColor(TextColor color) {
      this.color = color;
      return this;
   }

   public FontImageWrapper setColor(ChatColor bukkitChatColor) {
      if (bukkitChatColor == null) {
         this.color = TextColor.fromHexString("#FFFFFF");
      } else if (bukkitChatColor == ChatColor.WHITE) {
         this.color = TextColor.fromHexString("#FFFFFF");
      } else if (bukkitChatColor == ChatColor.BLACK) {
         this.color = TextColor.fromHexString("#000000");
      } else if (bukkitChatColor == ChatColor.DARK_BLUE) {
         this.color = TextColor.fromHexString("#0000AA");
      } else if (bukkitChatColor == ChatColor.DARK_GREEN) {
         this.color = TextColor.fromHexString("#00AA00");
      } else if (bukkitChatColor == ChatColor.DARK_AQUA) {
         this.color = TextColor.fromHexString("#00AAAA");
      } else if (bukkitChatColor == ChatColor.DARK_RED) {
         this.color = TextColor.fromHexString("#AA0000");
      } else if (bukkitChatColor == ChatColor.DARK_PURPLE) {
         this.color = TextColor.fromHexString("#AA00AA");
      } else if (bukkitChatColor == ChatColor.GOLD) {
         this.color = TextColor.fromHexString("#FFAA00");
      } else if (bukkitChatColor == ChatColor.GRAY) {
         this.color = TextColor.fromHexString("#AAAAAA");
      } else if (bukkitChatColor == ChatColor.DARK_GRAY) {
         this.color = TextColor.fromHexString("#555555");
      } else if (bukkitChatColor == ChatColor.BLUE) {
         this.color = TextColor.fromHexString("#5555FF");
      } else if (bukkitChatColor == ChatColor.GREEN) {
         this.color = TextColor.fromHexString("#55FF55");
      } else if (bukkitChatColor == ChatColor.AQUA) {
         this.color = TextColor.fromHexString("#55FFFF");
      } else if (bukkitChatColor == ChatColor.RED) {
         this.color = TextColor.fromHexString("#FF5555");
      } else if (bukkitChatColor == ChatColor.LIGHT_PURPLE) {
         this.color = TextColor.fromHexString("#FF55FF");
      } else if (bukkitChatColor == ChatColor.YELLOW) {
         this.color = TextColor.fromHexString("#FFFF55");
      }

      return this;
   }

   public FontImageWrapper setOffset(int offset) {
      this.offset = offset;
      return this;
   }

   public int getOffset() {
      return this.offset;
   }

   public String internalGetUnicode() {
      return TextFormatCache.method9634(this.internal.getFormattedText(), this.offset);
   }
}
