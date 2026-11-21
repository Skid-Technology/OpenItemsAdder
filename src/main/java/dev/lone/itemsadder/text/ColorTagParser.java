package dev.lone.itemsadder.text;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.ComponentResult;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NullableBox;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.Nullable;

public class ColorTagParser {
   public static final Pattern aHe = Pattern.compile("<([^\\s>]+)\\s([^>]+)>", 2);

   static int @Nullable [] eC(String var0) {
      return switch (var0) {
         case "r" -> new int[]{230, 255, 254};
         case "w" -> new int[]{230, 255, 250};
         case "rw" -> new int[]{230, 251, 254};
         case "j" -> new int[]{230, 251, 250};
         case "rj" -> new int[]{230, 247, 254};
         case "b" -> new int[]{230, 247, 250};
         case "noshadow" -> new int[]{78, 92, 36};
         default -> null;
      };
   }

   public static boolean method2357(Component var0) {
      for (Component var2 : var0.children()) {
         if (method2357(var2)) {
            return true;
         }
      }

      TextColor var3 = var0.color();
      if (var3 == null) {
         return false;
      } else if (var3.red() == 0 && var3.green() == 0 && var3.blue() == 0) {
         return false;
      } else if (var3.red() == 230 && var3.green() == 255 && var3.blue() == 254) {
         return true;
      } else if (var3.red() == 230 && var3.green() == 255 && var3.blue() == 250) {
         return true;
      } else if (var3.red() == 230 && var3.green() == 251 && var3.blue() == 254) {
         return true;
      } else if (var3.red() == 230 && var3.green() == 251 && var3.blue() == 250) {
         return true;
      } else if (var3.red() == 230 && var3.green() == 247 && var3.blue() == 254) {
         return true;
      } else {
         return var3.red() == 230 && var3.green() == 247 && var3.blue() == 250 ? true : var3.red() == 78 && var3.green() == 92 && var3.blue() == 36;
      }
   }

   public static Component method2358(Component var0) {
      return method2360(null, var0);
   }

   public static Component method2359(Component var0, NullableBox var1) {
      return method2361(null, var0, var1);
   }

   public static boolean eB(String var0) {
      return var0.contains("<") && var0.contains(">");
   }

   public static Component method2360(@Nullable Permissible var0, Component var1) {
      return method2361(var0, var1, NullableBox.empty());
   }

   private static Component method2361(@Nullable Permissible var0, Component var1, NullableBox var2) {
      return var1.replaceText((TextReplacementConfig)TextReplacementConfig.builder().match(aHe).replacement(var2x -> {
         Matcher var3 = aHe.matcher(var2x.content());
         if (var3.matches()) {
            String var4 = var3.group(1);
            int[] var5 = eC(var4);
            if (var5 == null) {
               return var2x;
            }

            var2.value = true;
            if (var0 != null && !var0.hasPermission("ia.user.text_effect.use." + var4)) {
               if (var0 instanceof Player) {
                  Log.sendPrefixed((Player)var0, Main.aFi.lj("no-permission-detail").replace("{permission}", "ia.user.text_effect.use." + var4));
               }

               return var2x.content(var3.group(2));
            } else {
               return var2x.content(var3.group(2)).color(TextColor.color(var5[0], var5[1], var5[2]));
            }
         } else {
            return var2x;
         }
      }).build());
   }

   public static String method2362(Permissible var0, String var1) {
      Matcher var2 = aHe.matcher(var1);

      while (var2.find()) {
         String var3 = var2.group();
         String var4 = var2.group(1);
         if (!var0.hasPermission("ia.user.text_effect.use." + var4)) {
            var1 = StringUtils.replace(var1, var3, var4.replace(var4, ""));
         }
      }

      return var1;
   }

   public static void method2363(CustomItemDefinition var0) {
      CustomItemDefinition.method8807(var0, var0x -> {
         NullableBox var1 = NullableBox.of(false);
         var0x = method2361(null, var0x, var1);
         return new ComponentResult(var0x, (Boolean)var1.value);
      });
   }
}
