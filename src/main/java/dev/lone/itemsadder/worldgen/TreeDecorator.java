package dev.lone.itemsadder.worldgen;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.BlockDataSetter;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.world.TreeFeatureHookHolder;
import java.util.HashMap;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class TreeDecorator extends AbstractDecorator {
   public static HashMap aDN = new HashMap();
   public final int aDV;
   public final CustomBlockDefinition aDW;
   public final CustomBlockDefinition aDX;
   public final TreeType aDY;
   private final BlockDataSetter aDZ;

   public TreeDecorator(
      String var1,
      String var2,
      @NotNull List var3,
      int var4,
      int var5,
      int var6,
      int var7,
      double var8,
      CustomBlockDefinition var10,
      CustomBlockDefinition var11,
      TreeType var12
   ) {
      super(var1, var2, var3, var10, var4, var5, var6, var8);
      this.aDV = var7;
      this.aDW = var10;
      this.aDX = var11;
      this.aDY = var12;
      if (var6 >= var5) {
         throw new IllegalArgumentException("min_height must be less than max_height");
      }

      this.aDZ = (var2x, var3x) -> {
         try {
            if (ItemStackUtils.aj(var3x.getMaterial())) {
               var11.placeBlockFull(null, (Block)var2x, true, false, true, false);
            } else if (ItemStackUtils.ai(var3x.getMaterial())) {
               var10.placeBlockFull(null, (Block)var2x, true, false, true, false);
            } else {
               var2x.setBlockData(var3x, false);
            }
         } catch (Exception var5x) {
            Log.warn("Error while setting block " + (var2x == null ? "" : var2x));
            var5x.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(Main.getInstance());
         }

         return true;
      };
   }

   public boolean aO(Block var1) {
      return this.method2321(var1, true);
   }

   public boolean method2321(Block var1, boolean var2) {
      if (this.method2312(var1.getRelative(BlockFace.DOWN).getType())) {
         try {
            return TreeFeatureHookHolder.getHook().growTree(var1.getLocation(), this.aDY, this.aDZ, var2);
         } catch (Throwable var4) {
            Log.error("Custom tree '" + this.yK + "' failed to spawn at " + BlockFaceUtils.locationCoordsString(var1.getLocation()), var4);
            return true;
         }
      } else {
         return false;
      }
   }

   public static void method2318(YamlConfig var0) {
      List var1 = var0.getStringList("trees_populators");
      if (var1 == null) {
         if (Settings.get().isLoggingEnabled("logging.world_generation.trees_populators.loading_no_populators")) {
            Log.log(ChatColor.YELLOW + "    No trees_populators found in file " + var0.GQ());
         }
      } else {
         ContentManager var2 = ContentManager.method8084();
         String var3 = var0.getString("info.namespace");

         for (String var5 : var1) {
            ConfigurationSection var6 = var0.kb("trees_populators." + var5);
            assert var6 != null;
            if (var6.getBoolean("enabled", true)) {
               String var7 = var6.getString("log");
               CustomItemDefinition var8 = var2.method8154(var7);
               if (var8 == null) {
                  Log.error("&cTree populator &6" + var5 + " &cfailed to load. Missing 'log'&c. &6File: " + var0.DJ());
               } else if (!CustomBlockDefinition.isCustomBlockDefinition(var8)) {
                  Log.error("&cTree populator &6" + var5 + " &cfailed to load. Attribute 'log' is not a custom block '" + var7 + "'&c. &6File: " + var0.DJ());
               } else {
                  String var9 = var6.getString("leaves");
                  CustomItemDefinition var10 = var2.method8154(var9);
                  if (var10 == null) {
                     Log.error("&cTree populator &6" + var5 + " &cfailed to load. Missing 'leaves'&c. &6File: " + var0.DJ());
                  } else if (!CustomBlockDefinition.isCustomBlockDefinition(var10)) {
                     Log.error(
                        "&cTree populator &6" + var5 + " &cfailed to load. Attribute 'leaves' is not a custom block '" + var9 + "'&c. &6File: " + var0.DJ()
                     );
                  } else {
                     String var11 = var6.getString("tree_type", "TREE");
                     TreeType var12 = (TreeType)BukkitRegistryLookup.enumOrDefault(var11, TreeType.class, TreeType.TREE);

                     TreeDecorator var13;
                     try {
                        var13 = new TreeDecorator(
                           var5,
                           var0.DJ(),
                           var6.getStringList("worlds"),
                           var6.getInt("amount"),
                           var6.getInt("max_height", 100),
                           var6.getInt("min_height", 5),
                           var6.getInt("iterations"),
                           var6.getDouble("chance"),
                           (CustomBlockDefinition)var8,
                           (CustomBlockDefinition)var10,
                           var12
                        );
                     } catch (IllegalArgumentException var17) {
                        Log.error("&cTree populator &6" + var5 + " &cfailed to load. " + var17.getMessage() + "&c. &6File: " + var0.DJ());
                        continue;
                     }

                     ContentManager.method8084().at.add((CustomBlockDefinition)var10);
                     if (var6.contains("biomes")) {
                        boolean var14 = true;

                        for (String var16 : var6.getStringList("biomes")) {
                           if (!var13.ei(var16)) {
                              Log.error("[tree_populator} &6'" + var5 + "'&c. Error: Unknown biome &6'" + var16 + "'&c. &6File: " + var0.DJ());
                              var14 = false;
                           }
                        }

                        if (!var14) {
                           continue;
                        }
                     }

                     if (var6.contains("bottom_blocks")) {
                        for (String var19 : var6.getStringList("bottom_blocks")) {
                           Material var20 = (Material)BukkitRegistryLookup.enumOrNull(var19.toUpperCase(), Material.class);
                           if (var20 != null) {
                              var13.method2311(var20);
                           }
                        }
                     }

                     aDN.put(var5, var13);
                     Bukkit.getWorlds().forEach(var13::method2316);
                  }
               }
            } else if (Settings.get().isLoggingEnabled("logging.world_generation.trees_populators.loading_disabled")) {
               Log.log("    Ignored disabled trees populator: " + var5 + ", namespace: " + var3);
            }
         }
      }
   }

   @Override
   public void method2315(World var1) {
      TreePopulator var2 = this.method2322(var1);
      if (var2 != null) {
         var2.start();
      }
   }

   @Override
   public void method2316(World var1) {
      this.method2322(var1);
   }

   private TreePopulator method2322(World var1) {
      if (!this.method2313(var1)) {
         return null;
      }

      TreePopulator var2 = TreePopulator.qj.stream().filter(var1x -> var1.equals(var1x.ez())).findFirst().orElseGet(() -> {
         TreePopulator var1x = new TreePopulator(var1);
         TreePopulator.qj.add(var1x);
         return var1x;
      });
      var2.method1364(this);
      return var2;
   }
}
