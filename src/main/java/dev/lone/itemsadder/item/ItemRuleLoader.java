package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.NamespacedId;
import java.util.Map;
import java.util.function.Predicate;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public final class ItemRuleLoader {
   private ItemRuleLoader() {
   }

   public static boolean method10071(ConfigSectionWrapper var0) {
      return var0.hA("elytra");
   }

   public static void method10072(CustomItemDefinition var0, ConfigSectionWrapper var1, YamlConfig var2, String var3, String var4) {
      if (!Version.isAtLeast(Version.v1_21_3)) {
         throw new IllegalArgumentException("'elytra' is only available in 1.21.2+. File: " + var2.DJ());
      }

      ConfigurationSection var5 = var1.kb("elytra");
      if (var5 == null) {
         throw new IllegalArgumentException("'elytra' must be a section. File: " + var2.DJ());
      }

      NamespacedKey var6 = new NamespacedKey(var3, var4);
      ItemRuleSpec var7 = method10073(var5, var3, var6, method10078(var0), var0x -> ContentManager.method8199(var0x) != null);
      ItemAttributeLoader.method10104(var0, var1, var2, var3, var4, var6);
      var0.method8829(var7);
      if (var0.anZ != null) {
         var0.anZ.method9992(var7);
      }

      ContentManager.method8084().ab().put(var6, CustomItemDataEntry.method75(var6, var7.EP()));
   }

   static ItemRuleSpec method10073(ConfigurationSection var0, String var1, NamespacedKey var2, @Nullable String var3, Predicate var4) {
      String var5 = method10077(var0.get("wings_texture"), var1, "elytra.wings_texture", false);
      if (!var4.test(var5)) {
         throw new IllegalArgumentException("Can't find 'elytra.wings_texture' texture '" + var5 + "'.");
      }

      String var6 = method10077(var0.get("broken_item_texture"), var1, "elytra.broken_item_texture", true);
      String var7 = method10074(var3, var6, var4);
      if (var6 != null && var7 == null) {
         throw new IllegalArgumentException("Can't find 'elytra.broken_item_texture' texture '" + var6 + "'.");
      }

      int var8 = method10075(var0, "max_flight_ticks", 0);
      int var9 = method10075(var0, "firework.cooldown_ticks", 0);
      double var10 = method10076(var0, "durability_damage_multiplier", 1.0);
      double var12 = method10076(var0, "collision_damage_multiplier", 1.0);
      return new ItemRuleSpec(
         var2, var5, var7, var8, new ToggleSpec(var0.getBoolean("firework.enabled", true), var0.getBoolean("firework.consume", true), var9), var10, var12
      );
   }

   @Nullable
   static String method10074(@Nullable String var0, @Nullable String var1, Predicate var2) {
      if (var1 != null) {
         return var2.test(var1) ? var1 : null;
      }

      if (var0 == null) {
         return null;
      }

      String var3 = var0.replaceFirst("\\.png$", "") + "_broken";
      return var2.test(var3) ? var3 : null;
   }

   private static int method10075(ConfigurationSection var0, String var1, int var2) {
      Object var3 = var0.get(var1);
      if (var3 == null) {
         return var2;
      } else if (var3 instanceof Number var4
         && Double.isFinite(var4.doubleValue())
         && !(var4.doubleValue() < 0.0)
         && var4.doubleValue() == Math.rint(var4.doubleValue())
         && !(var4.doubleValue() > 2.147483647E9)) {
         return var4.intValue();
      } else {
         throw new IllegalArgumentException("'elytra." + var1 + "' must be a non-negative integer.");
      }
   }

   private static double method10076(ConfigurationSection var0, String var1, double var2) {
      Object var4 = var0.get(var1);
      if (var4 == null) {
         return var2;
      } else if (var4 instanceof Number var5 && Double.isFinite(var5.doubleValue()) && !(var5.doubleValue() < 0.0)) {
         return var5.doubleValue();
      } else {
         throw new IllegalArgumentException("'elytra." + var1 + "' must be a non-negative finite number.");
      }
   }

   @Nullable
   private static String method10077(@Nullable Object var0, String var1, String var2, boolean var3) {
      if (var0 == null && var3) {
         return null;
      }

      if (var0 instanceof String var4 && !var4.isBlank()) {
         String var5 = NamespacedId.method385(var4.replaceFirst("\\.png$", ""), var1);
         if (var5 == null) {
            throw new IllegalArgumentException("'" + var2 + "' has invalid resource path '" + var4 + "'.");
         } else {
            return var5;
         }
      } else {
         throw new IllegalArgumentException("'" + var2 + "' must be a resource path.");
      }
   }

   @Nullable
   private static String method10078(CustomItemDefinition var0) {
      if (var0.anZ instanceof ItemSpecWithTexture var1) {
         Map var4 = var1.Fd();
         String var3 = (String)var4.get("normal");
         if (var3 == null) {
            var3 = (String)var4.get("layer0");
         }

         if (var3 == null) {
            var3 = (String)var4.values().stream().findFirst().orElse(null);
         }

         return var3;
      } else {
         return var0.anZ == null && var0.getTextures() != null && !var0.getTextures().isEmpty() ? (String)var0.getTextures().getFirst() : null;
      }
   }
}
