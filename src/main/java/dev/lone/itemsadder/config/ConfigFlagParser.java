package dev.lone.itemsadder.config;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.PotionEffectSpec;
import dev.lone.itemsadder.util.Log;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.bukkit.potion.PotionEffectType;

public class ConfigFlagParser {
   public static boolean method10054(ConfigSectionWrapper var0) {
      return var0.jI("food") || var0.jI("consumable");
   }

   public static void method10055(CustomItemDefinition var0, ConfigSectionWrapper var1, YamlConfig var2, String var3, String var4) {
      ConfigSectionWrapper var5 = var1.kc("food");
      if (var5 == null) {
         var5 = var1.kc("consumable");
      }

      assert var5 != null;
      boolean var6 = Version.isNewerThan(Version.v1_21_1);
      String var7 = var5.jO("animation");
      String var8 = var5.bc("sound", null);
      Boolean var9 = var5.method7104("particles", null);
      ArrayList var10 = null;
      ConfigSectionWrapper var11 = var5.kc("effects");
      if (var6) {
         if (var7 != null) {
            var7 = var7.toLowerCase();
            switch (var7) {
               case "none":
               case "eat":
               case "drink":
               case "block":
               case "bow":
               case "spear":
               case "crossbow":
               case "spyglass":
               case "toot_horn":
               case "brush":
                  break;
               default:
                  Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Unknown 'animation' value '" + var7 + "'. File: " + var2.DJ());
                  var7 = null;
            }
         }

         if (var8 == null && var7 != null && var7.equalsIgnoreCase("drink")) {
            var8 = "entity.generic.drink";
            if (var9 == null) {
               var9 = false;
            }
         }

         if (var11 != null) {
            var10 = new ArrayList();

            for (String var21 : var5.jK("effects")) {
               ConfigSectionWrapper var14 = var11.kc(var21);
               assert var14 != null;
               if (var21.startsWith("apply_status_effects")) {
                  if (var14.jK("effects").isEmpty()) {
                     Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: 'apply_status_effects' is missing 'effects' property. File: " + var2.DJ());
                  }

                  ArrayList var22 = new ArrayList();

                  for (String var24 : var14.jK("effects")) {
                     ConfigSectionWrapper var25 = var14.kc("effects." + var24);
                     PotionEffectType var26 = YamlConfig.jU(var25.jO("potion"));
                     if (var26 == null) {
                        Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Unknown 'potion' effect type '" + var25.jO("potion") + "'. File: " + var2.DJ());
                     } else {
                        var22.add(
                           new PotionEffectSpec(
                              var26,
                              var25.jP("duration"),
                              var25.jP("amplifier"),
                              var25.kj("ambient"),
                              var25.method7078("particles", true),
                              var25.method7078("icon", var25.method7078("show_icon", true))
                           )
                        );
                     }
                  }

                  var10.add(new ParsedList(var22, var14.method7102("probability", 1.0F)));
               } else if (!var21.startsWith("remove_status_effects")) {
                  if (var21.startsWith("play_sound")) {
                     if (var14.jI("sound")) {
                        var10.add(new ParsedString(var14.bc("sound", null)));
                     } else {
                        Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: 'play_sound' is missing 'sound' property. File: " + var2.DJ());
                     }
                  } else if (var21.startsWith("teleport_randomly")) {
                     if (var10.stream().noneMatch(var0x -> var0x instanceof ParsedFloat)) {
                        var10.add(new ParsedFloat(var14.method7102("diameter", 5.0F)));
                     } else {
                        Log.warn("[Items] '" + var3 + ":" + var4 + "'. Warning: 'teleport_randomly' is already set. File: " + var2.DJ());
                     }
                  } else if (var21.startsWith("clear_all_status_effects")) {
                     if (var10.stream().noneMatch(var0x -> var0x instanceof ParsedValueEmpty)) {
                        var10.add(new ParsedValueEmpty());
                     } else {
                        Log.warn("[Items] '" + var3 + ":" + var4 + "'. Warning: 'clear_all_status_effects' is already set. File: " + var2.DJ());
                     }
                  } else {
                     Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Unknown effect type '" + var21 + "'. File: " + var2.DJ());
                  }
               } else {
                  List var15 = var14.jZ("effects");
                  if (var15.isEmpty()) {
                     Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: 'remove_status_effects' is missing 'effects' property. File: " + var2.DJ());
                     HashSet var16 = new HashSet();

                     for (String var18 : var15) {
                        PotionEffectType var19 = YamlConfig.jU(var18);
                        if (var19 == null) {
                           Log.error(
                              "[Items] '" + var3 + ":" + var4 + "'. Error: Unknown 'potion' effect type '" + var14.jO("potion") + "'. File: " + var2.DJ()
                           );
                        } else {
                           var16.add(var19);
                        }
                     }

                     var10.add(new ParsedSet(var16));
                  }
               }
            }
         }
      } else {
         if (var7 != null) {
            Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: 'animation' is only available in 1.21.2+. File: " + var2.DJ());
         }

         if (var8 != null) {
            Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: 'sound' is only available in 1.21.2+. File: " + var2.DJ());
         }

         if (var9 != null && var9) {
            Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: 'particles' is only available in 1.21.2+. File: " + var2.DJ());
         }

         if (var11 != null
            && var5.jK("effects")
               .stream()
               .anyMatch(
                  var0x -> var0x.startsWith("remove_status_effect") || var0x.startsWith("teleport_randomly") || var0x.startsWith("clear_all_status_effects")
               )) {
            Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: some consumable 'effects' are only available in 1.21.2+. File: " + var2.DJ());
         }
      }

      var0.method8800(
         var0,
         var5.jP("nutrition"),
         var5.method7102("saturation", var5.jQ("saturation_modifier")),
         var5.kj("can_always_eat"),
         var5.method7076("consume_seconds", var5.method7076("eat_seconds", 2)),
         var7,
         var8,
         var9,
         var10
      );
   }
}
