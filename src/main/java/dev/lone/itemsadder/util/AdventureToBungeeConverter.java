package dev.lone.itemsadder.util;

import dev.lone.itemsadder.internal.SwitchMap82;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.event.ClickEvent.Action;
import net.kyori.adventure.text.event.ClickEvent.Payload;
import net.kyori.adventure.text.event.ClickEvent.Payload.Int;
import net.kyori.adventure.text.event.ClickEvent.Payload.Text;
import net.kyori.adventure.text.event.HoverEvent.ShowEntity;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Entity;

public final class AdventureToBungeeConverter {
   private static final BaseComponent[] EMPTY_ARRAY = new BaseComponent[0];

   private AdventureToBungeeConverter() {
   }

   public static BaseComponent convert(Component var0) {
      return (BaseComponent)(var0 == null ? new TextComponent("") : convertComponent(var0));
   }

   public static BaseComponent[] convertArray(Component var0) {
      return var0 == null ? EMPTY_ARRAY : new BaseComponent[]{convertComponent(var0)};
   }

   public static BaseComponent[] convertCollection(Collection var0) {
      if (var0 != null && !var0.isEmpty()) {
         ArrayList var1 = new ArrayList(var0.size());

         for (Component var3 : var0) {
            if (var3 != null) {
               var1.add(convertComponent(var3));
            }
         }

         return var1.isEmpty() ? EMPTY_ARRAY : var1.toArray(new BaseComponent[var1.size()]);
      } else {
         return EMPTY_ARRAY;
      }
   }

   private static BaseComponent convertComponent(Component var0) {
      BaseComponent var1 = convertContent(var0);
      applyStyle(var1, var0.style());
      applyExtras(var1, var0.children());
      return var1;
   }

   private static BaseComponent convertContent(Component var0) {
      if (var0 instanceof net.kyori.adventure.text.TextComponent var7) {
         return new TextComponent(nullToEmpty(var7.content()));
      } else if (var0 instanceof TranslatableComponent var6) {
         return convertTranslatable(var6);
      } else if (var0 instanceof KeybindComponent var5) {
         return new net.md_5.bungee.api.chat.KeybindComponent(nullToEmpty(var5.keybind()));
      } else if (var0 instanceof ScoreComponent var4) {
         net.md_5.bungee.api.chat.ScoreComponent var8 = new net.md_5.bungee.api.chat.ScoreComponent(nullToEmpty(var4.name()), nullToEmpty(var4.objective()));
         String var9 = var4.value();
         if (var9 != null && !var9.isEmpty()) {
            var8.setValue(var9);
         }

         return var8;
      } else if (var0 instanceof SelectorComponent var1) {
         net.md_5.bungee.api.chat.SelectorComponent var2 = new net.md_5.bungee.api.chat.SelectorComponent(nullToEmpty(var1.pattern()));
         Component var3 = var1.separator();
         if (var3 != null) {
            var2.setSeparator(convertComponent(var3));
         }

         return var2;
      } else {
         return new TextComponent("");
      }
   }

   private static BaseComponent convertTranslatable(TranslatableComponent var0) {
      net.md_5.bungee.api.chat.TranslatableComponent var1 = new net.md_5.bungee.api.chat.TranslatableComponent(nullToEmpty(var0.key()), new Object[0]);
      String var2 = var0.fallback();
      if (var2 != null) {
         var1.setFallback(var2);
      }

      List var3 = var0.arguments();
      if (var3 != null && !var3.isEmpty()) {
         ArrayList var4 = new ArrayList(var3.size());

         for (TranslationArgument var6 : var3) {
            var4.add(convertArgument(var6));
         }

         var1.setWith(var4);
      }

      return var1;
   }

   private static BaseComponent convertArgument(TranslationArgument var0) {
      if (var0 != null && var0.value() != null) {
         Object var1 = var0.value();
         return (BaseComponent)(var1 instanceof ComponentLike var2 ? convertComponent(var2.asComponent()) : new TextComponent(String.valueOf(var1)));
      } else {
         return new TextComponent("");
      }
   }

   private static void applyStyle(BaseComponent var0, Style var1) {
      if (var1 != null && !var1.isEmpty()) {
         TextColor var2 = var1.color();
         if (var2 != null) {
            ChatColor var3 = toBungeeColor(var2);
            if (var3 != null) {
               var0.setColor(var3);
            }
         }

         applyDecoration(var0, TextDecoration.BOLD, var1.decoration(TextDecoration.BOLD));
         applyDecoration(var0, TextDecoration.ITALIC, var1.decoration(TextDecoration.ITALIC));
         applyDecoration(var0, TextDecoration.UNDERLINED, var1.decoration(TextDecoration.UNDERLINED));
         applyDecoration(var0, TextDecoration.STRIKETHROUGH, var1.decoration(TextDecoration.STRIKETHROUGH));
         applyDecoration(var0, TextDecoration.OBFUSCATED, var1.decoration(TextDecoration.OBFUSCATED));
         Key var8 = var1.font();
         if (var8 != null) {
            var0.setFont(var8.asString());
         }

         ShadowColor var4 = var1.shadowColor();
         String var5 = var1.insertion();
         if (var5 != null) {
            var0.setInsertion(var5);
         }

         ClickEvent var6 = convertClickEvent(var1.clickEvent());
         if (var6 != null) {
            var0.setClickEvent(var6);
         }

         HoverEvent var7 = convertHoverEvent(var1.hoverEvent());
         if (var7 != null) {
            var0.setHoverEvent(var7);
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static void applyDecoration(BaseComponent var0, TextDecoration var1, State var2) {
      if (var2 != null && var2 != State.NOT_SET) {
         boolean var3 = var2 == State.TRUE;
         switch (SwitchMap82.bLp[var1.ordinal()]) {
            case 1:
               var0.setBold(var3);
               break;
            case 2:
               var0.setItalic(var3);
               break;
            case 3:
               var0.setUnderlined(var3);
               break;
            case 4:
               var0.setStrikethrough(var3);
               break;
            case 5:
               var0.setObfuscated(var3);
         }
      }
   }

   private static void applyExtras(BaseComponent var0, List var1) {
      if (var1 != null && !var1.isEmpty()) {
         for (Component var3 : var1) {
            if (var3 != null) {
               var0.addExtra(convertComponent(var3));
            }
         }
      }
   }

   private static ChatColor toBungeeColor(TextColor var0) {
      int var1 = var0.value() & 16777215;
      ChatColor var2 = legacyColorOf(var1);
      if (var2 != null) {
         return var2;
      }

      try {
         return ChatColor.of(toHexColor(var1));
      } catch (IllegalArgumentException var4) {
         return null;
      }
   }

   private static ChatColor legacyColorOf(int var0) {
      switch (var0) {
         case 0:
            return ChatColor.BLACK;
         case 170:
            return ChatColor.DARK_BLUE;
         case 43520:
            return ChatColor.DARK_GREEN;
         case 43690:
            return ChatColor.DARK_AQUA;
         case 5592405:
            return ChatColor.DARK_GRAY;
         case 5592575:
            return ChatColor.BLUE;
         case 5635925:
            return ChatColor.GREEN;
         case 5636095:
            return ChatColor.AQUA;
         case 11141120:
            return ChatColor.DARK_RED;
         case 11141290:
            return ChatColor.DARK_PURPLE;
         case 11184810:
            return ChatColor.GRAY;
         case 16733525:
            return ChatColor.RED;
         case 16733695:
            return ChatColor.LIGHT_PURPLE;
         case 16755200:
            return ChatColor.GOLD;
         case 16777045:
            return ChatColor.YELLOW;
         case 16777215:
            return ChatColor.WHITE;
         default:
            return null;
      }
   }

   private static ClickEvent convertClickEvent(net.kyori.adventure.text.event.ClickEvent var0) {
      if (var0 == null) {
         return null;
      } else {
         Action var1 = var0.action();
         Payload var2 = var0.payload();
         if (var1 == Action.OPEN_URL) {
            return convertTextClick(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, var2);
         } else if (var1 == Action.OPEN_FILE) {
            return convertTextClick(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_FILE, var2);
         } else if (var1 == Action.RUN_COMMAND) {
            return convertTextClick(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, var2);
         } else if (var1 == Action.SUGGEST_COMMAND) {
            return convertTextClick(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, var2);
         } else if (var1 == Action.COPY_TO_CLIPBOARD) {
            return convertTextClick(net.md_5.bungee.api.chat.ClickEvent.Action.COPY_TO_CLIPBOARD, var2);
         } else if (var1 == Action.CHANGE_PAGE) {
            return var2 instanceof Int var3 ? new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.CHANGE_PAGE, Integer.toString(var3.integer())) : null;
         } else {
            return null;
         }
      }
   }

   private static ClickEvent convertTextClick(net.md_5.bungee.api.chat.ClickEvent.Action var0, Payload var1) {
      return var1 instanceof Text var2 ? new ClickEvent(var0, var2.value()) : null;
   }

   private static HoverEvent convertHoverEvent(net.kyori.adventure.text.event.HoverEvent var0) {
      if (var0 == null) {
         return null;
      } else {
         net.kyori.adventure.text.event.HoverEvent.Action var1 = var0.action();
         Object var2 = var0.value();
         if (var1 == net.kyori.adventure.text.event.HoverEvent.Action.SHOW_TEXT) {
            return convertShowText(var2);
         } else {
            return var1 == net.kyori.adventure.text.event.HoverEvent.Action.SHOW_ENTITY && var2 instanceof ShowEntity var3 ? convertShowEntity(var3) : null;
         }
      }
   }

   private static HoverEvent convertShowText(Object var0) {
      Object var1;
      if (var0 instanceof ComponentLike var2) {
         var1 = convertComponent(var2.asComponent());
      } else {
         var1 = new TextComponent(var0 == null ? "" : String.valueOf(var0));
      }

      return new HoverEvent(
         net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
         new Content[]{new net.md_5.bungee.api.chat.hover.content.Text(new BaseComponent[]{(BaseComponent)var1})}
      );
   }

   private static HoverEvent convertShowEntity(ShowEntity var0) {
      Component var1 = var0.name();
      return new HoverEvent(
         net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_ENTITY,
         new Content[]{new Entity(var0.type().asString(), var0.id().toString(), var1 == null ? null : convertComponent(var1))}
      );
   }

   private static String toHexColor(int var0) {
      String var1 = Integer.toHexString(var0 & 16777215);
      return "#000000".substring(0, 7 - var1.length()) + var1;
   }

   private static String nullToEmpty(String var0) {
      return var0 == null ? "" : var0;
   }
}
