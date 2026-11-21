package dev.lone.itemsadder.worldgen;

import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.Log;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class BlockDecorator extends AbstractDecorator {
   public static HashMap aDN = new HashMap();
   public final int aDO;
   public final HashSet aDP;

   public BlockDecorator(String var1, String var2, @NotNull List var3, CustomBlockDefinition var4, int var5, int var6, int var7, int var8, double var9) {
      super(var1, var2, var3, var4, var5, var6, var7, var9);
      this.aDO = var8;
      this.aDP = new HashSet();
      if (var7 >= var6) {
         throw new IllegalArgumentException("min_height must be less than max_height");
      }
   }

   public void method2329(Material var1) {
      this.aDP.add(var1);
   }

   public boolean aN(Block var1) {
      return this.aDP.isEmpty() ? true : this.aDP.contains(var1.getType());
   }

   public static void method2318(YamlConfig var0) {
      ContentManager var1 = ContentManager.method8084();
      String var2 = var0.getString("info.namespace");
      List var3 = var0.getStringList("blocks_populators");
      if (var3 == null || var3.isEmpty()) {
         var3 = var0.getStringList("worlds_populators");
      }

      if (var3 == null) {
         if (Settings.get().isLoggingEnabled("logging.world_generation.blocks_populators.loading_no_populators")) {
            Log.log(ChatColor.YELLOW + "    No blocks_populators found in file " + var0.GQ());
         }
      } else {
         for (String var5 : var3) {
            ConfigurationSection var6 = var0.kb("blocks_populators." + var5);
            if (var6 == null) {
               var6 = var0.kb("worlds_populators." + var5);
            }

            assert var6 != null;
            if (var6.getBoolean("enabled", true)) {
               CustomBlockDefinition var7 = var1.method8160(var6.getString("block"));
               if (var7 == null) {
                  Log.error("&cWorld populator &6" + var5 + " &cfailed to load. Unknown block &6" + var6.getString("block") + "&c. &6File: " + var0.DJ());
               } else {
                  BlockDecorator var8;
                  try {
                     var8 = new BlockDecorator(
                        var5,
                        var0.DJ(),
                        var6.getStringList("worlds"),
                        var7,
                        var6.getInt("vein_blocks", var6.getInt("amount")),
                        var6.getInt("max_height"),
                        var6.getInt("min_height"),
                        var6.getInt("chunk_veins", var6.getInt("iterations")),
                        var6.getDouble("chunk_chance", var6.getInt("chance"))
                     );
                  } catch (IllegalArgumentException var12) {
                     Log.error("&cWorld populator &6" + var5 + " &cfailed to load. " + var12.getMessage() + "&c. &6File: " + var0.DJ());
                     continue;
                  }

                  if (var6.contains("replaceable_blocks")) {
                     for (String var10 : var6.getStringList("replaceable_blocks")) {
                        if (ItemStackUtils.lh(var10)) {
                           var8.method2329(Material.valueOf(var10.toUpperCase()));
                        }
                     }
                  }

                  if (var6.contains("biomes")) {
                     boolean var13 = true;

                     for (String var11 : var6.getStringList("biomes")) {
                        if (!var8.ei(var11)) {
                           Log.error("[world_populator} &6'" + var5 + "'&c. Error: Unknown biome &6'" + var11 + "'&c. &6File: " + var0.DJ());
                           var13 = false;
                        }
                     }

                     if (!var13) {
                        continue;
                     }
                  }

                  aDN.put(var5, var8);
                  Bukkit.getWorlds().forEach(var8::method2316);
               }
            } else if (Settings.get().isLoggingEnabled("logging.world_generation.blocks_populators.loading_disabled")) {
               Log.log("    Ignored disabled world populator: " + var5 + ", namespace: " + var2);
            }
         }
      }
   }

   @Override
   public void method2315(World var1) {
      BlockPopulator var2 = this.method2330(var1);
      if (var2 != null) {
         var2.start();
      }
   }

   @Override
   public void method2316(World var1) {
      this.method2330(var1);
   }

   private BlockPopulator method2330(World var1) {
      if (!this.method2313(var1)) {
         return null;
      }

      BlockPopulator var2 = BlockPopulator.qj.stream().filter(var1x -> var1.equals(var1x.ez())).findFirst().orElseGet(() -> {
         BlockPopulator var1x = new BlockPopulator(var1);
         BlockPopulator.qj.add(var1x);
         return var1x;
      });
      var2.method1570(this);
      return var2;
   }
}
