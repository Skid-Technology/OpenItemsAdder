package dev.lone.itemsadder.fontimage;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.lang.MessageFormatter;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import dev.lone.itemsadder.text.ColorTagParser;
import dev.lone.itemsadder.util.NullableBox;
import java.awt.Color;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.permissions.Permissible;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

public class ChatComponentFormatter {
   public static @NonNull Component method2348(Component var0) {
      return method2352(var0, false, null);
   }

   public static @NonNull Component method2349(@Nullable MessageFormatter var0, Component var1) {
      return method2354(var0, null, var1, false, null);
   }

   public static @NonNull Component method2350(@Nullable Permissible var0, Component var1) {
      return method2353(var0, var1, false, null);
   }

   public static @NonNull Component method2351(@Nullable MessageFormatter var0, @Nullable Permissible var1, Component var2) {
      return method2354(var0, var1, var2, false, null);
   }

   public static @NonNull Component method2352(Component var0, boolean var1, String var2) {
      return method2353(null, var0, var1, var2);
   }

   public static @NonNull Component method2353(@Nullable Permissible var0, Component var1, boolean var2, String var3) {
      ContentManager var4 = ContentManager.method8084();
      return method2354(var4 == null ? null : var4.method8109(), var0, var1, var2, var3);
   }

   public static @NonNull Component method2354(@Nullable MessageFormatter var0, @Nullable Permissible var1, Component var2, boolean var3, String var4) {
      if (var0 == null) {
         return var2;
      }

      NullableBox var5 = NullableBox.of(var4);
      return var2.replaceText(
         (TextReplacementConfig)TextReplacementConfig.builder()
            .match(EmojiReplacer.aHc)
            .replacement(
               var6 -> {
                  CustomTexture var7 = var0.bB(var6.content());
                  if (var7 != null && var1 != null && !var7.hasPermission(var1)) {
                     return var6;
                  }

                  if (var7 == null) {
                     String var9 = var0.bE(var6.content());
                     return var9 != null ? var6.content(var9) : var6;
                  }

                  if (var3 && var4 != null && var7.YG) {
                     if (var7.getCustomText() != null) {
                        var5.set(var7.getCustomText());
                     }

                     var6.hoverEvent(
                        HoverEvent.showText(
                           Component.text(((String)var5.value).replace("{id}", var7.dl).replace("{placeholder}", var7.getLegacyId()))
                              .replaceText((TextReplacementConfig)TextReplacementConfig.builder().match("\\{font_image\\}").replacement(var1xx -> {
                                 Builder var2xx = var1xx.content(var7.getDisplayText(false));
                                 return var7.applyShadow(var2xx);
                              }).build())
                        )
                     );
                  }

                  Builder var8;
                  if (Settings.get().textEffectsEnabled && ColorTagParser.method2357(var2)) {
                     var8 = var6.content(var7.getDisplayText(false));
                  } else {
                     var8 = (Builder)var6.content(var7.getDisplayText(false)).color(TextColor.color(Color.WHITE.getRGB()));
                  }

                  return var7.applyShadow(var8);
               }
            )
            .build()
      );
   }
}
