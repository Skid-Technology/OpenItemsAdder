package dev.lone.itemsadder.worldgen;

import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.Log;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class SurfaceFeature extends AbstractDecorator {
   public static Map aDT = new HashMap();

   protected SurfaceFeature(
      String var1, String var2, @NotNull List var3, CustomBlockDefinition var4, CustomItemDefinition var5, int var6, int var7, int var8, double var9
   ) {
      super(var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   @Override
   public void method2315(World var1) {
      SurfacePopulator var2 = this.method2317(var1);
      if (var2 != null) {
         var2.start();
      }
   }

   @Override
   public void method2316(World var1) {
      this.method2317(var1);
   }

   private SurfacePopulator method2317(World var1) {
      if (!this.method2313(var1)) {
         return null;
      }

      SurfacePopulator var2 = SurfacePopulator.qj.stream().filter(var1x -> var1.equals(var1x.ez())).findFirst().orElseGet(() -> {
         SurfacePopulator var1x = new SurfacePopulator(var1);
         SurfacePopulator.qj.add(var1x);
         return var1x;
      });
      var2.method1575(this);
      return var2;
   }

   public static void method2318(YamlConfig var0) {
      ContentManager var1 = ContentManager.method8084();
      String var2 = var0.getString("info.namespace");
      List var3 = var0.getStringList("surface_decorators");
      if (!var3.isEmpty()) {
         for (String var5 : var3) {
            ConfigurationSection var6 = var0.kb("surface_decorators." + var5);
            assert var6 != null;
            if (var6.getBoolean("enabled", true)) {
               String var7 = var6.getString("block");
               String var8 = var6.getString("furniture");
               if (var7 == null == (var8 == null)) {
                  Log.error("Surface decorator '" + var5 + "' must specify exactly one of 'block' or 'furniture'. File: " + var0.DJ());
               } else {
                  CustomBlockDefinition var9 = var7 == null ? null : var1.method8160(var7);
                  if (var7 != null && var9 == null) {
                     Log.error("[surface_decorator} &6'" + var5 + "'. &cError: Unknown block &6'" + var7 + "'&c. &6File: " + var0.DJ());
                  } else {
                     CustomItemDefinition var10 = var8 == null ? null : var1.method8154(var8);
                     if (var8 == null || var10 != null && (var10.aoh.hasAction("furniture") || var10.aoh.hasAction("complex_furniture"))) {
                        SurfaceFeature var11;
                        try {
                           var11 = new SurfaceFeature(
                              var5,
                              var0.DJ(),
                              var6.getStringList("worlds"),
                              var9,
                              var10,
                              var6.getInt("amount"),
                              var6.getInt("max_height"),
                              var6.getInt("min_height"),
                              var6.getDouble("chance")
                           );
                        } catch (IllegalArgumentException var15) {
                           Log.error("[surface_decorator} &6'" + var5 + "'. &cError: " + var15.getMessage() + "&c. &6File: " + var0.DJ());
                           continue;
                        }

                        if (var6.contains("biomes")) {
                           boolean var12 = true;

                           for (String var14 : var6.getStringList("biomes")) {
                              if (!var11.ei(var14)) {
                                 Log.error("[surface_decorator} &6'" + var5 + "'. &cError: Unknown biome &6'" + var14 + "'&c. &6File: " + var0.DJ());
                                 var12 = false;
                              }
                           }

                           if (!var12) {
                              continue;
                           }
                        }

                        if (var6.contains("bottom_blocks")) {
                           for (String var17 : var6.getStringList("bottom_blocks")) {
                              Material var18 = (Material)BukkitRegistryLookup.enumOrNull(var17.toUpperCase(), Material.class);
                              if (var18 != null) {
                                 var11.method2311(var18);
                              }
                           }
                        }

                        aDT.put(var5, var11);
                        Bukkit.getWorlds().forEach(var11::method2316);
                     } else {
                        Log.error("Surface decorator '" + var5 + "' has unknown furniture '" + var8 + "'. File: " + var0.DJ());
                     }
                  }
               }
            } else if (Settings.get().isLoggingEnabled("logging.world_generation.surface_decorators.loading_disabled")) {
               Log.log("    Ignored disabled surface decorator: " + var5 + ", namespace: " + var2);
            }
         }
      } else if (Settings.get().isLoggingEnabled("logging.world_generation.surface_decorators.loading_no_decorators")) {
         Log.log(ChatColor.YELLOW + "    No surface_decorators found in file " + var0.GQ());
      }
   }
}
