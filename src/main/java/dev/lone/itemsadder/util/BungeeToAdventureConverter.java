package dev.lone.itemsadder.util;

import dev.lone.itemsadder.internal.SwitchMap83;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.KeybindComponent;
import net.md_5.bungee.api.chat.ScoreComponent;
import net.md_5.bungee.api.chat.SelectorComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Entity;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;

public final class BungeeToAdventureConverter {
   private static final Key AIR_KEY = Key.key("minecraft:air");
   private static final Key PIG_KEY = Key.key("minecraft:pig");

   private BungeeToAdventureConverter() {
   }

   public static Component convert(BaseComponent var0) {
      return (Component)(var0 == null ? Component.empty() : convertComponent(var0));
   }

   public static Component convertArray(BaseComponent... var0) {
      if (var0 != null && var0.length != 0) {
         if (var0.length == 1) {
            return convert(var0[0]);
         }

         Builder var1 = Component.text();

         for (BaseComponent var5 : var0) {
            if (var5 != null) {
               var1.append(convertComponent(var5));
            }
         }

         return var1.build();
      } else {
         return Component.empty();
      }
   }

   public static Component convertCollection(Collection var0) {
      if (var0 != null && !var0.isEmpty()) {
         if (var0.size() == 1) {
            return convert((BaseComponent)var0.iterator().next());
         }

         Builder var1 = Component.text();

         for (BaseComponent var3 : var0) {
            if (var3 != null) {
               var1.append(convertComponent(var3));
            }
         }

         return var1.build();
      } else {
         return Component.empty();
      }
   }

   private static Component convertComponent(BaseComponent var0) {
      Component var1 = convertContent(var0);
      var1 = applyStyleFrom(var1, var0);
      List var2 = var0.getExtra();
      if (var2 != null && !var2.isEmpty()) {
         ArrayList var3 = new ArrayList(var2.size());

         for (BaseComponent var5 : var2) {
            if (var5 != null) {
               var3.add(convertComponent(var5));
            }
         }

         return var3.isEmpty() ? var1 : var1.children(var3);
      } else {
         return var1;
      }
   }

   private static Component convertContent(BaseComponent var0) {
      if (var0 instanceof TextComponent var6) {
         return Component.text(nullToEmpty(var6.getText()));
      } else if (var0 instanceof TranslatableComponent var5) {
         return convertTranslatable(var5);
      } else if (var0 instanceof KeybindComponent var4) {
         return Component.keybind(nullToEmpty(var4.getKeybind()));
      } else if (var0 instanceof ScoreComponent var3) {
         String var7 = var3.getValue();
         return var7 != null && !var7.isEmpty()
            ? Component.score(nullToEmpty(var3.getName()), nullToEmpty(var3.getObjective()), var7)
            : Component.score(nullToEmpty(var3.getName()), nullToEmpty(var3.getObjective()));
      } else if (var0 instanceof SelectorComponent var1) {
         BaseComponent var2 = var1.getSeparator();
         return var2 == null
            ? Component.selector(nullToEmpty(var1.getSelector()))
            : Component.selector(nullToEmpty(var1.getSelector()), convertComponent(var2));
      } else {
         return Component.text(var0.toPlainText());
      }
   }

   private static Component convertTranslatable(TranslatableComponent var0) {
      List var1 = var0.getWith();
      if (var1 != null && !var1.isEmpty()) {
         ArrayList var2 = new ArrayList(var1.size());

         for (BaseComponent var4 : var1) {
            if (var4 != null) {
               var2.add(convertComponent(var4));
            }
         }

         return Component.translatable(nullToEmpty(var0.getTranslate()), var0.getFallback(), var2, new StyleBuilderApplicable[0]);
      } else {
         return Component.translatable(nullToEmpty(var0.getTranslate()), var0.getFallback());
      }
   }

   private static Component applyStyleFrom(Component var0, BaseComponent var1) {
      net.kyori.adventure.text.format.Style.Builder var2 = Style.style();
      boolean var3 = false;
      boolean var4 = var1.isReset();
      TextColor var5 = toAdventureColor(var1.getColorRaw());
      if (var5 != null) {
         var2.color(var5);
         var3 = true;
      } else if (var4) {
         var2.color(NamedTextColor.WHITE);
         var3 = true;
      }

      String var6 = var1.getFontRaw();
      if (var6 != null && !var6.isEmpty()) {
         Key var7 = parseKey(var6);
         if (var7 != null) {
            var2.font(var7);
            var3 = true;
         }
      }

      var3 |= applyDecorationFrom(var2, TextDecoration.BOLD, var1.isBoldRaw(), var4);
      var3 |= applyDecorationFrom(var2, TextDecoration.ITALIC, var1.isItalicRaw(), var4);
      var3 |= applyDecorationFrom(var2, TextDecoration.UNDERLINED, var1.isUnderlinedRaw(), var4);
      var3 |= applyDecorationFrom(var2, TextDecoration.STRIKETHROUGH, var1.isStrikethroughRaw(), var4);
      var3 |= applyDecorationFrom(var2, TextDecoration.OBFUSCATED, var1.isObfuscatedRaw(), var4);
      String var15 = var1.getInsertion();
      if (var15 != null) {
         var2.insertion(var15);
         var3 = true;
      }

      ClickEvent var8 = convertClickEvent(var1.getClickEvent());
      if (var8 != null) {
         var2.clickEvent(var8);
         var3 = true;
      }

      HoverEvent var9 = convertHoverEvent(var1.getHoverEvent());
      if (var9 != null) {
         var2.hoverEvent(var9);
         var3 = true;
      }

      return var3 ? var0.style(var2.build()) : var0;
   }

   private static boolean applyDecorationFrom(net.kyori.adventure.text.format.Style.Builder var0, TextDecoration var1, Boolean var2, boolean var3) {
      if (var2 == null && !var3) {
         return false;
      }

      var0.decoration(var1, Boolean.TRUE.equals(var2) ? State.TRUE : State.FALSE);
      return true;
   }

   private static TextColor toAdventureColor(ChatColor var0) {
      if (var0 == null || var0.getColor() == null) {
         return null;
      } else if (var0 == ChatColor.BLACK) {
         return NamedTextColor.BLACK;
      } else if (var0 == ChatColor.DARK_BLUE) {
         return NamedTextColor.DARK_BLUE;
      } else if (var0 == ChatColor.DARK_GREEN) {
         return NamedTextColor.DARK_GREEN;
      } else if (var0 == ChatColor.DARK_AQUA) {
         return NamedTextColor.DARK_AQUA;
      } else if (var0 == ChatColor.DARK_RED) {
         return NamedTextColor.DARK_RED;
      } else if (var0 == ChatColor.DARK_PURPLE) {
         return NamedTextColor.DARK_PURPLE;
      } else if (var0 == ChatColor.GOLD) {
         return NamedTextColor.GOLD;
      } else if (var0 == ChatColor.GRAY) {
         return NamedTextColor.GRAY;
      } else if (var0 == ChatColor.DARK_GRAY) {
         return NamedTextColor.DARK_GRAY;
      } else if (var0 == ChatColor.BLUE) {
         return NamedTextColor.BLUE;
      } else if (var0 == ChatColor.GREEN) {
         return NamedTextColor.GREEN;
      } else if (var0 == ChatColor.AQUA) {
         return NamedTextColor.AQUA;
      } else if (var0 == ChatColor.RED) {
         return NamedTextColor.RED;
      } else if (var0 == ChatColor.LIGHT_PURPLE) {
         return NamedTextColor.LIGHT_PURPLE;
      } else if (var0 == ChatColor.YELLOW) {
         return NamedTextColor.YELLOW;
      } else {
         return (TextColor)(var0 == ChatColor.WHITE ? NamedTextColor.WHITE : TextColor.color(var0.getColor().getRGB() & 16777215));
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static ClickEvent convertClickEvent(net.md_5.bungee.api.chat.ClickEvent var0) {
      if (var0 == null) {
         return null;
      }

      String var1 = var0.getValue();

      try {
         switch (SwitchMap83.bLt[var0.getAction().ordinal()]) {
            case 1:
               return var1 == null ? null : ClickEvent.openUrl(var1);
            case 2:
               return var1 == null ? null : ClickEvent.openFile(var1);
            case 3:
               return var1 == null ? null : ClickEvent.runCommand(var1);
            case 4:
               return var1 == null ? null : ClickEvent.suggestCommand(var1);
            case 5:
               Integer var2 = parsePageNumber(var1);
               return var2 == null ? null : ClickEvent.changePage(var2);
            case 6:
               return var1 == null ? null : ClickEvent.copyToClipboard(var1);
            default:
               return null;
         }
      } catch (IllegalArgumentException var3) {
         return null;
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static HoverEvent convertHoverEvent(net.md_5.bungee.api.chat.HoverEvent var0) {
      if (var0 != null && var0.getContents() != null && !var0.getContents().isEmpty()) {
         try {
            switch (SwitchMap83.bLu[var0.getAction().ordinal()]) {
               case 1:
                  return HoverEvent.showText(buildHoverText(var0.getContents()));
               case 2:
                  Item var1 = (Item)findContent(var0.getContents(), Item.class);
                  return var1 == null ? null : convertItemHover(var1);
               case 3:
                  Entity var2 = (Entity)findContent(var0.getContents(), Entity.class);
                  return var2 == null ? null : convertEntityHover(var2);
               case 4:
                  String var3 = plainHoverText(var0.getContents());
                  return var3 == null ? null : HoverEvent.showAchievement(var3);
               default:
                  return null;
            }
         } catch (IllegalArgumentException var4) {
            return null;
         }
      } else {
         return null;
      }
   }

   private static Component buildHoverText(List var0) {
      ArrayList var1 = new ArrayList(var0.size());

      for (Content var3 : var0) {
         if (var3 instanceof Text var4) {
            Component var5 = toComponentValue(var4.getValue());
            if (!var5.equals(Component.empty())) {
               var1.add(var5);
            }
         }
      }

      if (var1.isEmpty()) {
         return Component.empty();
      }

      if (var1.size() == 1) {
         return (Component)var1.get(0);
      }

      Builder var6 = Component.text();

      for (Component var8 : var1) {
         var6.append(var8);
      }

      return var6.build();
   }

   private static Component toComponentValue(Object var0) {
      if (var0 == null) {
         return Component.empty();
      } else if (var0 instanceof BaseComponent[] var2) {
         return convertArray(var2);
      } else {
         return (Component)(var0 instanceof BaseComponent var1 ? convert(var1) : Component.text(String.valueOf(var0)));
      }
   }

   private static HoverEvent convertItemHover(Item var0) {
      Key var1 = withDefaultKey(var0.getId(), AIR_KEY);
      int var2 = var0.getCount() > 0 ? var0.getCount() : 1;
      BinaryTagHolder var3 = null;
      if (var0.getTag() != null && var0.getTag().getNbt() != null && !var0.getTag().getNbt().isEmpty()) {
         var3 = BinaryTagHolder.binaryTagHolder(var0.getTag().getNbt());
      }

      return HoverEvent.showItem(var1, var2, var3);
   }

   private static HoverEvent convertEntityHover(Entity var0) {
      if (var0.getId() != null && !var0.getId().isEmpty()) {
         UUID var1;
         try {
            var1 = UUID.fromString(var0.getId());
         } catch (IllegalArgumentException var4) {
            return null;
         }

         Key var2 = withDefaultKey(var0.getType(), PIG_KEY);
         Component var3 = var0.getName() == null ? null : convert(var0.getName());
         return HoverEvent.showEntity(var2, var1, var3);
      } else {
         return null;
      }
   }

   private static Content findContent(List var0, Class var1) {
      for (Content var3 : var0) {
         if (var1.isInstance(var3)) {
            return (Content)var1.cast(var3);
         }
      }

      return null;
   }

   private static String plainHoverText(List var0) {
      for (Content var2 : var0) {
         if (var2 instanceof Text var3) {
            Object var4 = var3.getValue();
            if (var4 instanceof BaseComponent[] var6) {
               return BaseComponent.toPlainText(var6);
            }

            if (var4 instanceof BaseComponent var5) {
               return var5.toPlainText();
            }

            if (var4 != null) {
               return String.valueOf(var4);
            }
         }
      }

      return null;
   }

   private static Integer parsePageNumber(String var0) {
      if (var0 == null) {
         return null;
      }

      try {
         int var1 = Integer.parseInt(var0);
         return var1 > 0 ? var1 : null;
      } catch (NumberFormatException var2) {
         return null;
      }
   }

   private static Key withDefaultKey(String var0, Key var1) {
      Key var2 = parseKey(var0);
      return var2 == null ? var1 : var2;
   }

   private static Key parseKey(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         try {
            return Key.key(var0);
         } catch (IllegalArgumentException var2) {
            return null;
         }
      } else {
         return null;
      }
   }

   private static String nullToEmpty(String var0) {
      return var0 == null ? "" : var0;
   }
}
