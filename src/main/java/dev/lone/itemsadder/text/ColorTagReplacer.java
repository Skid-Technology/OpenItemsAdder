package dev.lone.itemsadder.text;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.util.Log;
import java.util.regex.Matcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.Nullable;

public class ColorTagReplacer {
   public static Component method2366(Component var0) {
      return method2367(null, var0);
   }

   public static Component method2367(@Nullable Permissible var0, Component var1) {
      return var1.replaceText((TextReplacementConfig)TextReplacementConfig.builder().match(ColorTagParser.aHe).replacement(var1x -> {
         Matcher var2 = ColorTagParser.aHe.matcher(var1x.content());
         if (var2.matches()) {
            String var3 = var2.group(1);
            int[] var4 = ColorTagParser.eC(var3);
            if (var4 == null) {
               return var1x;
            }

            if (var0 != null && !var0.hasPermission("ia.user.text_effect.use." + var3)) {
               if (var0 instanceof Player) {
                  Log.sendPrefixed((Player)var0, Main.aFi.lj("no-permission-detail").replace("{permission}", "ia.user.text_effect.use." + var3));
               }

               return var1x.content(var2.group(2));
            } else {
               return var1x.content(var2.group(2)).color(TextColor.color(var4[0], var4[1], var4[2]));
            }
         } else {
            return var1x;
         }
      }).build());
   }
}
