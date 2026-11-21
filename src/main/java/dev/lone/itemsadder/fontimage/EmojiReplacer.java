package dev.lone.itemsadder.fontimage;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.lang.MessageFormatter;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import dev.lone.itemsadder.text.ColorTagParser;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.ComponentResult;
import dev.lone.itemsadder.util.NullableBox;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmojiReplacer {
   public static final Pattern aHc = Pattern.compile(":([a-z_\\-0-9]+):", 2);
   private static final Pattern aHd = Pattern.compile("%img_([a-z_\\-0-9]+)%", 2);

   @NotNull
   public static Component method2333(@NotNull Component var0) {
      return method2337(null, var0, false, null, NullableBox.empty());
   }

   @NotNull
   public static Component method2334(@Nullable Permissible var0, @NotNull Component var1) {
      return method2337(var0, var1, false, null, NullableBox.empty());
   }

   @NotNull
   public static Component method2335(@NotNull Component var0, NullableBox var1) {
      return method2337(null, var0, false, null, var1);
   }

   public static boolean eB(String var0) {
      if (var0 == null) {
         return false;
      }

      int var1 = var0.indexOf(58);
      return var1 >= 0 && var0.indexOf(58, var1 + 1) >= 0;
   }

   @NotNull
   public static Component method2336(@NotNull Component var0, boolean var1, @Nullable String var2) {
      return method2337(null, var0, var1, var2, NullableBox.empty());
   }

   @NotNull
   public static Component method2337(@Nullable Permissible var0, @NotNull Component var1, boolean var2, @Nullable String var3, NullableBox var4) {
      ContentManager var5 = ContentManager.method8084();
      return method2338(var5 == null ? null : var5.method8109(), var0, var1, var2, var3, var4);
   }

   @NotNull
   public static Component method2338(
      @Nullable MessageFormatter var0, @Nullable Permissible var1, @NotNull Component var2, boolean var3, @Nullable String var4, NullableBox var5
   ) {
      if (var0 == null) {
         return var2;
      }

      NullableBox var6 = NullableBox.of(var4);
      return var2.replaceText(
         (TextReplacementConfig)TextReplacementConfig.builder()
            .match(aHc)
            .replacement(
               var6x -> {
                  var5.value = true;
                  CustomTexture var7 = var0.bB(var6x.content());
                  if (var7 != null && var1 != null && !var7.hasPermission(var1)) {
                     return var6x;
                  }

                  if (var7 == null) {
                     String var9 = var0.bE(var6x.content());
                     return var9 != null ? var6x.content(var9) : var6x;
                  }

                  if (var3 && var6.isPresent() && var7.YG) {
                     if (var7.getCustomText() != null) {
                        var6.set(var7.getCustomText());
                     }

                     var6x.hoverEvent(
                        HoverEvent.showText(
                           AdventureText.parseRichText(((String)var6.value).replace("{id}", var7.dl).replace("{placeholder}", var7.getLegacyId()))
                              .replaceText((TextReplacementConfig)TextReplacementConfig.builder().match("\\{font_image\\}").replacement(var1xx -> {
                                 Builder var2xx = var1xx.content(var7.getDisplayText(false));
                                 return var7.applyShadow(var2xx);
                              }).build())
                        )
                     );
                  }

                  Builder var8;
                  if (Settings.get().textEffectsEnabled && ColorTagParser.method2357(var2)) {
                     var8 = var6x.content(var7.getDisplayText(false));
                  } else {
                     var8 = (Builder)var6x.content(var7.getDisplayText(false)).color(TextColor.color(Color.WHITE.getRGB()));
                  }

                  return var7.applyShadow(var8);
               }
            )
            .build()
      );
   }

   public static String replaceFontImages(Permissible p, String message) {
      return method2339(p, message, true);
   }

   public static String method2339(Permissible var0, String var1, boolean var2) {
      ContentManager var3 = ContentManager.method8084();
      return method2340(var3 == null ? null : var3.method8109(), var0, var1, var2);
   }

   public static String method2340(@Nullable MessageFormatter var0, Permissible var1, String var2, boolean var3) {
      Matcher var4 = aHc.matcher(var2);
      if (var0 == null) {
         return var2;
      }

      while (var4.find()) {
         String var5 = var4.group();
         CustomTexture var6 = var0.bB(var5);
         if (var6 == null) {
            String var7 = var0.bE(var5);
            if (var7 != null) {
               var2 = StringUtils.replace(var2, var5, var7);
            }
         } else if (var6.hasPermission(var1)) {
            if (var3) {
               var2 = var2.replace(var5, ChatColor.WHITE + var6.getDisplayText(false) + ChatColor.RESET);
            } else {
               var2 = StringUtils.replace(var2, var5, var6.getDisplayText(false));
            }
         }
      }

      return var2;
   }

   public static String replaceFontImages(String string) {
      ContentManager var1 = ContentManager.method8084();
      return method2341(var1 == null ? null : var1.method8109(), string);
   }

   public static String method2341(@Nullable MessageFormatter var0, String var1) {
      Matcher var2 = aHc.matcher(var1);
      if (var0 == null) {
         return var1;
      }

      while (var2.find()) {
         String var3 = var2.group();
         CustomTexture var4 = var0.bB(var3);
         if (var4 == null) {
            String var5 = var0.bE(var3);
            if (var5 != null) {
               var1 = StringUtils.replace(var1, var3, var5);
            }
         } else {
            var1 = StringUtils.replace(var1, var3, ChatColor.WHITE + var4.getDisplayText(false) + ChatColor.RESET);
         }
      }

      return var1;
   }

   public static void method2342(CustomItemDefinition var0) {
      ContentManager var1 = ContentManager.method8084();
      MessageFormatter var2 = var1 == null ? null : var1.method8109();
      CustomItemDefinition.method8807(var0, var1x -> {
         NullableBox var2x = NullableBox.of(false);
         var1x = method2343(var2, var1x, var2x);
         return new ComponentResult(var1x, (Boolean)var2x.value);
      });
   }

   static Component method2343(@Nullable MessageFormatter var0, Component var1, NullableBox var2) {
      if (var0 == null) {
         return var1;
      }

      var1 = var1.replaceText((TextReplacementConfig)TextReplacementConfig.builder().match(aHd).replacement(var2x -> {
         String var3x = var2x.content().substring(5, var2x.content().length() - 1);
         if (var0.bB(var3x) == null && var0.bE(var3x) == null) {
            return var2x;
         }

         var2.value = true;
         return var2x.content(":" + var3x + ":");
      }).build());
      return method2338(var0, null, var1, false, null, var2);
   }
}
