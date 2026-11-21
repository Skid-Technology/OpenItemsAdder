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
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class AttachedDecorator extends AbstractDecorator {
   public static HashMap aDN = new HashMap();
   public final AttachFace aDR;

   public AttachedDecorator(
      String var1,
      String var2,
      @NotNull List var3,
      CustomBlockDefinition var4,
      CustomItemDefinition var5,
      int var6,
      int var7,
      int var8,
      double var9,
      AttachFace var11
   ) {
      super(var1, var2, var3, var4, var5, var6, var7, var8, var9);
      this.aDR = var11;
   }

   public static void method2318(YamlConfig var0) {
      String var1 = var0.getString("info.namespace");
      List var2 = var0.getStringList("cave_decorators");
      if (var2.isEmpty()) {
         if (Settings.get().isLoggingEnabled("logging.world_generation.cave_decorators.loading_no_decorators")) {
            Log.log(ChatColor.YELLOW + "    No cave_decorators found in file " + var0.GQ());
         }
      } else {
         for (String var4 : var2) {
            ConfigurationSection var5 = var0.kb("cave_decorators." + var4);
            assert var5 != null;
            if (var5.getBoolean("enabled", true)) {
               String var6 = var5.getString("block");
               String var7 = var5.getString("furniture");
               if (var6 == null == (var7 == null)) {
                  Log.error("Cave decorator '" + var4 + "' must specify exactly one of 'block' or 'furniture'. File: " + var0.DJ());
               } else {
                  CustomBlockDefinition var8 = var6 == null ? null : ContentManager.method8084().method8160(var6);
                  if (var6 != null && var8 == null) {
                     Log.error("&cCave decorator &6" + var4 + " &cfailed to load. Unknown block &6" + var6 + "&c. &6File: " + var0.DJ());
                  } else {
                     CustomItemDefinition var9 = var7 == null ? null : ContentManager.method8084().method8154(var7);
                     if (var7 == null || var9 != null && (var9.aoh.hasAction("furniture") || var9.aoh.hasAction("complex_furniture"))) {
                        AttachFace var10;
                        if (var5.get("position") instanceof String var11) {
                           var10 = (AttachFace)BukkitRegistryLookup.enumOrNull(var11, AttachFace.class);
                           if (var10 == null) {
                              Log.error("&cCave decorator &6" + var4 + " &cfailed to load. Unknown 'position' &6" + var11 + "&c. &6File: " + var0.DJ());
                              continue;
                           }
                        } else {
                           var10 = AttachFace.SURFACE;
                        }

                        AttachedDecorator var15 = new AttachedDecorator(
                           var4,
                           var0.DJ(),
                           var5.getStringList("worlds"),
                           var8,
                           var9,
                           var5.getInt("amount"),
                           var5.getInt("max_height"),
                           var5.getInt("min_height"),
                           var5.getDouble("chance"),
                           var10
                        );
                        if (var5.contains("biomes")) {
                           boolean var16 = true;

                           for (String var14 : var5.getStringList("biomes")) {
                              if (!var15.ei(var14)) {
                                 Log.error("[cave_decorator} &6'" + var4 + "'&c. Error: Unknown biome &6'" + var14 + "'&c. &6File: " + var0.DJ());
                                 var16 = false;
                              }
                           }

                           if (!var16) {
                              continue;
                           }
                        }

                        if (var5.contains("bottom_blocks")) {
                           for (String var18 : var5.getStringList("bottom_blocks")) {
                              Material var19 = (Material)BukkitRegistryLookup.enumOrNull(var18.toUpperCase(), Material.class);
                              if (var19 != null) {
                                 var15.method2311(var19);
                              }
                           }
                        }

                        aDN.put(var4, var15);
                        Bukkit.getWorlds().forEach(var15::method2316);
                     } else {
                        Log.error("Cave decorator '" + var4 + "' has unknown furniture '" + var7 + "'. File: " + var0.DJ());
                     }
                  }
               }
            } else if (Settings.get().isLoggingEnabled("logging.world_generation.cave_decorators.loading_disabled")) {
               Log.log("    Ignored disabled cave decorator: " + var4 + ", namespace: " + var1);
            }
         }
      }
   }

   @Override
   public void method2315(World var1) {
      CavePopulator var2 = this.method2326(var1);
      if (var2 != null) {
         var2.start();
      }
   }

   @Override
   public void method2316(World var1) {
      this.method2326(var1);
   }

   private CavePopulator method2326(World var1) {
      if (!this.method2313(var1)) {
         return null;
      }

      CavePopulator var2 = CavePopulator.qj.stream().filter(var1x -> var1.equals(var1x.ez())).findFirst().orElseGet(() -> {
         CavePopulator var1x = new CavePopulator(var1);
         CavePopulator.qj.add(var1x);
         CavePopulator.aEe.put(var1.getUID(), var1x);
         return var1x;
      });
      var2.method1361(this);
      return var2;
   }
}
