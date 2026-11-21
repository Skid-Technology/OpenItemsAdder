package dev.lone.itemsadder.config;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.CustomMobSettings;
import dev.lone.itemsadder.entity.EntitySpawnRule;
import dev.lone.itemsadder.entity.EntitySpawnRuleBuilder;
import dev.lone.itemsadder.entity.EntityWorldListener;
import dev.lone.itemsadder.entity.IntTriple;
import dev.lone.itemsadder.entity.SpawnBoundsPair;
import dev.lone.itemsadder.entity.SpawnGroupBuilder;
import dev.lone.itemsadder.entity.SpawnMode;
import dev.lone.itemsadder.entity.WeatherType;
import dev.lone.itemsadder.resourcepack.ResourcePackWriter;
import dev.lone.itemsadder.util.BossBarManager;
import dev.lone.itemsadder.util.IntRange;
import dev.lone.itemsadder.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigSectionLoader {
   public static void cD() {
      EntityWorldListener.getInstance().clear();
      ContentManager var0 = ContentManager.method8084();

      for (YamlConfig var2 : var0.method8092()) {
         String var3 = var2.getString("info.namespace");
         var0.as.add(var3);
         List var4 = var2.getStringList("entities_populators");
         if (var4.isEmpty()) {
            if (Settings.get().isLoggingEnabled("logging.entity_populators.loading_empty")) {
               Log.log(ChatColor.YELLOW + "    No entity populator found in file " + var2.GQ());
            }
         } else {
            BossBarManager.setTitleKeepProgress("Entities Populators '" + var3 + "'...");

            for (String var6 : var4) {
               ConfigSectionWrapper var7 = var2.kc("entities_populators." + var6);
               assert var7 != null;
               if (!var7.method7078("enabled", true)) {
                  if (Settings.get().isLoggingEnabled("logging.entity_populators.loading_disabled")) {
                     Log.log("    Ignored disabled entities_populators: " + var6 + ", namespace: " + var3);
                  }
               } else {
                  if (Settings.get().isLoggingEnabled("logging.entity_populators.loading")) {
                     Log.log("    Loading entities_populators: " + var6);
                  }

                  if (!ResourcePackWriter.isValidNamespace(var6)) {
                     Log.error("[Entities] Non [a-z0-9_.-] character in entities_populators '" + var3 + ":" + var6 + "' in yml file: " + var2.DJ());
                     ResourcePackWriter.printAllowedCharacters();
                  } else {
                     String var8 = var7.jO("entity");
                     if (var8 == null) {
                        Log.error("[Entities] Missing entity in entities_populators '" + var3 + ":" + var6 + "' in yml file: " + var2.DJ());
                     } else {
                        CustomMobSettings var9 = CustomEntityManager.getInstance().getSettings(var8);
                        if (var9 == null) {
                           Log.error("[Entities] Invalid entity '" + var8 + "' in entities_populators '" + var3 + ":" + var6 + "' in yml file: " + var2.DJ());
                        } else {
                           float var10 = var7.method7102("chance", 15.0F);
                           int var11 = var7.method7076("amount", 1);
                           List var12 = var7.jZ("worlds");
                           ArrayList var13 = new ArrayList();

                           for (String var15 : var7.jZ("biomes")) {
                              NamespacedKey var16 = YamlConfig.method7036(
                                 var15,
                                 "minecraft",
                                 true,
                                 var4x -> Log.error(
                                    "[Entities] Invalid biome namespaced ID '"
                                       + var15
                                       + "' in entities_populators '"
                                       + var3
                                       + ":"
                                       + var6
                                       + "' in yml file: "
                                       + var2.DJ()
                                 )
                              );
                              if (var16 != null) {
                                 var13.add(var16);
                              }
                           }

                           ArrayList var23 = new ArrayList();
                           List var24 = var7.method7111(WeatherType.class, "weather", var23);
                           if (!var23.isEmpty()) {
                              Log.error("[Entities] Invalid weather in entities_populators '" + var3 + ":" + var6 + "' in yml file: " + var2.DJ());

                              for (String var17 : var23) {
                                 Log.error("[Entities] - " + var17);
                              }
                           }

                           ArrayList var26 = new ArrayList();
                           if (var7.kf("time") instanceof ConfigurationSection) {
                              ConfigSectionWrapper var27 = var7.kc("time");
                              assert var27 != null;

                              for (String var19 : var27.bJ(false)) {
                                 int var20 = var27.method7076(var19 + ".min", 0);
                                 int var21 = var27.method7076(var19 + ".max", 0);
                                 if (var20 > var21) {
                                    Log.error("[Entities] Invalid time in entities_populators '" + var3 + ":" + var6 + "' in yml file: " + var2.DJ());
                                 } else {
                                    var26.add(new IntRange(var20, var21));
                                 }
                              }
                           }

                           IntRange var28 = null;
                           if (var7.kf("light_level") instanceof ConfigurationSection) {
                              ConfigSectionWrapper var29 = var7.kc("light_level");
                              assert var29 != null;
                              int var31 = var29.method7076("min", 0);
                              int var34 = var29.method7076("max", 0);
                              if (var31 > var34) {
                                 Log.error("[Entities] Invalid light level in entities_populators '" + var3 + ":" + var6 + "' in yml file: " + var2.DJ());
                                 continue;
                              }

                              var28 = new IntRange(var31, var34);
                           }

                           SpawnGroupBuilder var30 = new SpawnGroupBuilder();
                           if (var7.kf("spawn_logic") instanceof ConfigurationSection) {
                              ConfigSectionWrapper var32 = var7.kc("spawn_logic");
                              assert var32 != null;
                              if (!var32.jI("method")) {
                                 Log.error("[Entities] Missing method in entities_populators '" + var3 + ":" + var6 + "' in yml file: " + var2.DJ());
                              } else {
                                 var30.mode((SpawnMode)var32.method7097("method", SpawnMode.class, null));
                                 if (var30.getMode() == null) {
                                    Log.error("[Entities] Invalid method in entities_populators '" + var3 + ":" + var6 + "' in yml file: " + var2.DJ());
                                 } else {
                                    var30.members(var32.kP("types"));
                                    if (var30.getMembers().isEmpty()) {
                                       Log.error("[Entities] Missing types in entities_populators '" + var3 + ":" + var6 + "' in yml file: " + var2.DJ());
                                    } else {
                                       SpawnBoundsPair var33 = null;
                                       if (var7.kf("location_range") instanceof ConfigurationSection) {
                                          ConfigSectionWrapper var35 = var7.kc("location_range");
                                          assert var35 != null;
                                          var33 = new SpawnBoundsPair(
                                             new IntTriple(var35.method7077("x.min", null), var35.method7077("y.min", null), var35.method7077("z.min", null)),
                                             new IntTriple(var35.method7077("x.max", null), var35.method7077("y.max", null), var35.method7077("z.max", null))
                                          );
                                       }

                                       EntitySpawnRuleBuilder var36 = EntitySpawnRule.toBuilder()
                                          .settings(var9)
                                          .weight(var10)
                                          .maxPerChunk(var11)
                                          .method8383(var13)
                                          .method8384(var12)
                                          .group(var30.build())
                                          .bounds(var33)
                                          .method8387(var24)
                                          .method8388(var26)
                                          .range(var28);

                                       try {
                                          EntityWorldListener.getInstance().register(var36.build());
                                       } catch (IllegalArgumentException var22) {
                                          Log.error(
                                             "[Entities] Failed to register entities_populators '"
                                                + var3
                                                + ":"
                                                + var6
                                                + "'. "
                                                + var22.getMessage()
                                                + " File:"
                                                + var2.DJ()
                                          );
                                          continue;
                                       }

                                       Log.devLog("Loaded entities_populators: " + var6 + ". File: " + var2.DJ());
                                    }
                                 }
                              }
                           } else {
                              Log.warn("[Entities] Missing spawn_logic in entities_populators '" + var3 + ":" + var6 + "' in yml file: " + var2.DJ());
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
