package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ComparisonConditionFloat;
import dev.lone.itemsadder.core.ComparisonOperator;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.ElementAlignment;
import dev.lone.itemsadder.core.NamespacedAlignedElement;
import dev.lone.itemsadder.core.NamespacedElementWithEffects;
import dev.lone.itemsadder.core.NamespacedKeyedEffect;
import dev.lone.itemsadder.core.NamespacedLabelElement;
import dev.lone.itemsadder.core.NamespacedTextElement;
import dev.lone.itemsadder.core.PotionComparisonCondition;
import dev.lone.itemsadder.core.ProgressEffect;
import dev.lone.itemsadder.event.FoodLevelEffect;
import dev.lone.itemsadder.event.FoodLevelEffectDup2;
import dev.lone.itemsadder.event.RespawnEffect;
import dev.lone.itemsadder.item.effect.PotionGrantEffect;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.world.ChunkBlockHookHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

public class MobGoalLoader {
   final Plugin abk;
   final ContentManager abl;
   public final HashMap abm = new HashMap();
   private boolean wq = true;
   public final List abn = new ArrayList();

   public MobGoalLoader(Plugin var1, ContentManager var2) {
      this.abk = var1;
      this.abl = var2;
   }

   public void setEnabled(boolean var1) {
      this.wq = var1;
   }

   public boolean isEnabled() {
      return this.wq;
   }

   public void loadGoals() {
      for (YamlConfig var2 : this.abl.method8092()) {
         List var3 = var2.getStringList("huds");
         if (!var3.isEmpty()) {
            String var4 = var2.getString("info.namespace");
            int var5 = 0;

            for (String var7 : var3) {
               if (Settings.get().isLoggingEnabled("logging.huds.loading")) {
                  Log.log("    Detected HUD: " + var7);
               }

               if (!var2.getBoolean("huds." + var7 + ".enabled", true)) {
                  if (Settings.get().isLoggingEnabled("logging.huds.loading_disabled")) {
                     Log.log("    Skip disabled HUD");
                  }
               } else {
                  NamespacedElementWithEffects var8 = null;
                  int var9 = var2.getInt("huds." + var7 + ".value.min");
                  int var10 = var2.getInt("huds." + var7 + ".value.max");
                  String var11 = var2.getString("huds." + var7 + ".value.player_stat_name");
                  switch (var2.getString("huds." + var7 + ".type")) {
                     case "STATUS":
                        CustomTexture var23 = (CustomTexture)this.abl.aE.Yp.get(var2.getString("huds." + var7 + ".images.positive"));
                        CustomTexture var24 = (CustomTexture)this.abl.aE.Yp.get(var2.getString("huds." + var7 + ".images.half"));
                        CustomTexture var26 = (CustomTexture)this.abl.aE.Yp.get(var2.getString("huds." + var7 + ".images.negative"));
                        if (var23 == null) {
                           Log.error("Failed to load positive font_image for hud " + var7 + ". font_image doesn't exists. " + var2.DJ());
                        }

                        if (var24 == null) {
                           Log.error("Failed to load half font_image for hud " + var7 + ". font_image doesn't exists. " + var2.DJ());
                        }

                        if (var26 == null) {
                           Log.error("Failed to load negative font_image for hud " + var7 + ". font_image doesn't exists. " + var2.DJ());
                        }

                        if (var23 != null && var24 != null && var26 != null) {
                           NamespacedAlignedElement var27 = new NamespacedAlignedElement(
                              null,
                              var4,
                              var7,
                              var11,
                              ElementAlignment.valueOf(var2.getString("huds." + var7 + ".direction", "RIGHT")),
                              var23,
                              var24,
                              var26,
                              var2.getInt("huds." + var7 + ".x_position_pixels"),
                              var10,
                              var2.getBoolean("huds." + var7 + ".show.auto", true),
                              var2.getBoolean("huds." + var7 + ".show.creative", true),
                              var2.getBoolean("huds." + var7 + ".show.underwater", true),
                              var2.getBoolean("huds." + var7 + ".show.riding", true),
                              var2.getInt("huds." + var7 + ".value.start"),
                              var9,
                              var10,
                              var2.jZ("huds." + var7 + ".worlds")
                           );
                           this.loadGoal(var2, var7, var27);
                           this.abm.put(var27.getNamespacedID(), var27);
                           var8 = var27;
                        }
                        break;
                     case "FRAMES":
                        List var22 = var2.jZ("huds." + var7 + ".images.frames");
                        int var15 = 0;

                        for (int var16 = var9; var16 <= var10; var16++) {
                           var15++;
                        }

                        if (var22.size() != var15) {
                           Log.error(
                              "Frames amount "
                                 + var22.size()
                                 + " is different from total amount of possible values "
                                 + var15
                                 + " ("
                                 + var9
                                 + " to "
                                 + var10
                                 + ") of hud '"
                                 + var7
                                 + "'. File: "
                                 + var2.DJ()
                           );
                        } else {
                           ArrayList var25 = new ArrayList();
                           int var17 = var9;

                           for (String var19 : var22) {
                              CustomTexture var20 = (CustomTexture)this.abl.aE.Yp.get(var19);
                              if (var20 == null) {
                                 Log.error("font_image '" + var19 + "' not found for frame '" + var17 + "' of hud '" + var7 + "'. File: " + var2.DJ());
                              } else {
                                 var25.add(var20);
                              }

                              var17++;
                           }

                           if (var25.isEmpty()) {
                              Log.error("No frame specified for hud '" + var7 + "'. File: " + var2.DJ());
                           } else {
                              NamespacedLabelElement var28 = new NamespacedLabelElement(
                                 null,
                                 var4,
                                 var7,
                                 var11,
                                 var25,
                                 var2.getInt("huds." + var7 + ".x_position_pixels"),
                                 var2.getBoolean("huds." + var7 + ".show.auto", true),
                                 var2.getBoolean("huds." + var7 + ".show.creative", true),
                                 var2.getBoolean("huds." + var7 + ".show.underwater", true),
                                 var2.getBoolean("huds." + var7 + ".show.riding", true),
                                 var2.getInt("huds." + var7 + ".value.start"),
                                 var9,
                                 var10,
                                 var2.jZ("huds." + var7 + ".worlds")
                              );
                              this.loadGoal(var2, var7, var28);
                              this.abm.put(var28.getNamespacedID(), var28);
                              var8 = var28;
                           }
                        }
                        break;
                     case "CUSTOM":
                        NamespacedTextElement var14 = new NamespacedTextElement(
                           null,
                           var4,
                           var7,
                           var2.getString("huds." + var7 + ".value.player_stat_name", "no_stat"),
                           var2.getInt("huds." + var7 + ".x_position_pixels"),
                           var2.getBoolean("huds." + var7 + ".show.auto", true),
                           var2.getBoolean("huds." + var7 + ".show.creative", true),
                           var2.getBoolean("huds." + var7 + ".show.underwater", true),
                           var2.getBoolean("huds." + var7 + ".show.riding", true),
                           var2.getInt("huds." + var7 + ".value.start"),
                           var9,
                           var10,
                           var2.jZ("huds." + var7 + ".worlds")
                        );
                        this.loadGoal(var2, var7, var14);
                        this.abm.put(var14.getNamespacedID(), var14);
                        var8 = var14;
                  }

                  if (var8 != null) {
                     PermissionNodes.er(var8.getNamespacedID());
                     if (var11 != null) {
                        this.abn.add(var11);
                     }
                  }
               }
            }

            var5++;
         } else if (Settings.get().isLoggingEnabled("logging.huds.loading_empty")) {
            Log.log(ChatColor.YELLOW + "    No huds found in file " + var2.GQ());
         }
      }
   }

   void loadGoal(YamlConfig var1, String var2, NamespacedElementWithEffects var3) {
      if (var1.contains("huds." + var2 + ".value.triggers")) {
         if (var1.contains("huds." + var2 + ".value.triggers.decrement")) {
            ConfigurationSection var4 = var1.kb("huds." + var2 + ".value.triggers.decrement");

            for (String var6 : var4.getKeys(false)) {
               if (var6.startsWith("generic_over_time")) {
                  ProgressEffect var7 = new ProgressEffect(
                     var4.getInt(var6 + ".every_ticks"),
                     -((float)var4.getDouble(var6 + ".amount")),
                     var4.getInt(var6 + ".min_sky_light", -1),
                     var4.getInt(var6 + ".max_sky_light", -1)
                  );
                  var3.method494(var7);
               } else if (var6.startsWith("biome_over_time")) {
                  NamespacedKeyedEffect var15 = new NamespacedKeyedEffect(
                     ChunkBlockHookHolder.getInstance().hook.parseBiomeKey(var4.getString(var6 + ".biome")),
                     var4.getInt(var6 + ".every_ticks"),
                     -((float)var4.getDouble(var6 + ".amount")),
                     var4.getInt(var6 + ".min_sky_light", -1),
                     var4.getInt(var6 + ".max_sky_light", -1)
                  );
                  var3.method494(var15);
               } else if (var6.startsWith("food_level_change")) {
                  FoodLevelEffect var16 = new FoodLevelEffect((float)var4.getDouble(var6 + ".amount"));
                  var3.method498(var16);
               }
            }
         }

         if (var1.contains("huds." + var2 + ".value.triggers.increment")) {
            ConfigurationSection var8 = var1.kb("huds." + var2 + ".value.triggers.increment");

            for (String var12 : var8.getKeys(false)) {
               if (var12.startsWith("food_level_change")) {
                  FoodLevelEffectDup2 var17 = new FoodLevelEffectDup2((float)var8.getDouble(var12 + ".amount"));
                  var3.method498(var17);
               } else if (var12.startsWith("player_respawn")) {
                  RespawnEffect var18 = new RespawnEffect((float)var8.getDouble(var12 + ".amount"));
                  var3.method498(var18);
               } else if (var12.startsWith("generic_over_time")) {
                  ProgressEffect var19 = new ProgressEffect(
                     var8.getInt(var12 + ".every_ticks"),
                     (float)var8.getDouble(var12 + ".amount"),
                     var8.getInt(var12 + ".min_sky_light", -1),
                     var8.getInt(var12 + ".max_sky_light", -1)
                  );
                  var3.method494(var19);
               } else if (var12.startsWith("biome_over_time")) {
                  NamespacedKeyedEffect var20 = new NamespacedKeyedEffect(
                     ChunkBlockHookHolder.getInstance().hook.parseBiomeKey(var8.getString(var12 + ".biome")),
                     var8.getInt(var12 + ".every_ticks"),
                     (float)var8.getDouble(var12 + ".amount"),
                     var8.getInt(var12 + ".min_sky_light", -1),
                     var8.getInt(var12 + ".max_sky_light", -1)
                  );
                  var3.method494(var20);
               }
            }
         }

         if (var1.contains("huds." + var2 + ".value.triggers.on_min_value")) {
            for (String var11 : var1.getStringList("huds." + var2 + ".value.triggers.on_min_value")) {
               if (var11.startsWith("damage_player")) {
                  ComparisonConditionFloat var13 = new ComparisonConditionFloat(
                     var1.getInt("huds." + var2 + ".value.triggers.on_min_value." + var11 + ".every_ticks"),
                     var1.getFloat("huds." + var2 + ".value.triggers.on_min_value." + var11 + ".damage"),
                     var3.zR(),
                     ComparisonOperator.LESS_EQUAL
                  );
                  var3.method496(var13);
               } else if (var11.startsWith("potion_effect")) {
                  PotionEffectType var14 = var1.jT("huds." + var2 + ".value.triggers.on_min_value." + var11 + ".type");
                  if (var14 == null) {
                     Log.error("Invalid 'type' for 'potion_effect' of  HUD '" + var2 + "'. File: " + var1.DJ());
                  } else {
                     PotionComparisonCondition var21 = new PotionComparisonCondition(
                        var1.getInt("huds." + var2 + ".value.triggers.on_min_value." + var11 + ".every_ticks"),
                        var1.getFloat("huds." + var2 + ".value.triggers.on_min_value." + var11 + ".damage"),
                        new PotionGrantEffect(
                           var14,
                           var1.getInt("huds." + var2 + ".value.triggers.on_min_value." + var11 + ".amplifier"),
                           var1.getInt("huds." + var2 + ".value.triggers.on_min_value." + var11 + ".duration"),
                           var1.getBoolean("huds." + var2 + ".value.triggers.on_min_value." + var11 + ".ambient"),
                           var1.getBoolean("huds." + var2 + ".value.triggers.on_min_value." + var11 + ".particles", true),
                           var1.getBoolean("huds." + var2 + ".value.triggers.on_min_value." + var11 + ".icon", true)
                        ),
                        ComparisonOperator.LESS_EQUAL
                     );
                     var3.method496(var21);
                  }
               }
            }
         }
      }
   }
}
