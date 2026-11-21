package dev.lone.itemsadder.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import dev.lone.itemsadder.anim.AnimationBinaryCodec;
import dev.lone.itemsadder.anim.AnimationBinaryPayload;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.FloatPair;
import dev.lone.itemsadder.util.Log;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.PiglinAbstract;
import org.bukkit.entity.Raider;
import org.bukkit.entity.Zoglin;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.jetbrains.annotations.Nullable;

public class EntityConfigLoader {
   private static final Map arU = Map.of(
      "attack_damage",
      2048.0,
      "armor",
      30.0,
      "knockback_resistance",
      1.0,
      "follow_range",
      2048.0,
      "safe_fall_distance",
      1024.0,
      "fall_damage_multiplier",
      100.0
   );

   public void load(ContentManager var1) {
      HashSet var2 = new HashSet();

      for (YamlConfig var4 : var1.method8092()) {
         String var5 = var4.getString("info.namespace");
         var1.as.add(var5);
         List var6 = var4.getStringList("entities");
         if (var6 != null) {
            for (String var8 : var6) {
               ConfigSectionWrapper var9 = var4.kc("entities." + var8);
               assert var9 != null;
               String var10 = var9.bc("model_folder", null);
               if (var10 == null) {
                  if (!var9.ke("ragdoll_on_death")
                     && !var9.ke("damageable_parts")
                     && !var9.ke("vanilla_death_animation")
                     && !var9.ke("render_distance")
                     && !var9.ke("float_on_water")
                     && !var9.ke("waterline_offset")
                     && !var9.ke("display_name")
                     && !var9.ke("name_tag")
                     && !var9.ke("boss_bar")
                     && !var9.ke("goals")
                     && !var9.ke("events")
                     && !var9.ke("vanilla_defaults")
                     && !var9.ke("silent")
                     && !var9.ke("sun_burn")
                     && !var9.ke("can_sun_burn")
                     && !var9.ke("drop_vanilla_loot")
                     && !var9.ke("can_drop_loot")
                     && !var9.ke("speed")
                     && !var9.ke("max_health")
                     && !var9.ke("ai")
                     && !var9.ke("gravity")
                     && !var9.ke("invulnerable")
                     && !var9.ke("age_visual_scale")
                     && !var9.ke("age_stages")
                     && !var9.ke("initial_age_ticks")
                     && !var9.ke("initial_baby")
                     && !var9.ke("behaviors")
                     && !var9.ke("attributes")) {
                     Log.error("Missing 'model_folder' for entity '" + var8 + "'. File: " + var4.DJ());
                  } else {
                     var2.add(var5 + ":" + var8);
                  }
               } else {
                  if (var9.ke("ragdoll_on_death")) {
                     Log.warn("'ragdoll_on_death' applies only to V2 entity bundles: " + var5 + ":" + var8);
                  }

                  if (var9.ke("vanilla_death_animation")) {
                     Log.warn("'vanilla_death_animation' applies only to V2 entity bundles: " + var5 + ":" + var8);
                  }

                  if (var9.ke("age_stages")) {
                     Log.error("'age_stages' is supported only for V2 entity bundles: " + var5 + ":" + var8 + ". File: " + var4.DJ());
                  }

                  Double var11;
                  try {
                     var11 = method10095(
                        Settings.get().customEntityRenderDistance, var9.kf("render_distance"), "entities." + var8 + ".render_distance in " + var4.DJ()
                     );
                  } catch (IllegalArgumentException var35) {
                     Log.error(var35.getMessage());
                     continue;
                  }

                  EntityType var12 = (EntityType)BukkitRegistryLookup.enumOrNull(var9.jO("type"), EntityType.class);
                  if (var12 == null) {
                     Log.error("Missing 'type' for entity '" + var8 + "'. File: " + var4.DJ());
                  } else {
                     CustomEntityModel var13 = new CustomEntityModel(var5, var8);
                     if (!var13.bc("/assets/" + var5 + "/models/" + var10 + "/.metadata")) {
                        Log.error("Failed to load model data for entity '" + var8 + "'. File: " + var4.DJ());
                     } else {
                        BarStyle var14 = null;
                        if (var9.bc("boss_bar.style", null) instanceof String var15) {
                           var14 = BukkitRegistryLookup.barStyle(var15);
                           if (var14 == null) {
                              Log.error("Invalid 'boss_bar.style' for entity '" + var8 + "'. File: " + var4.DJ());
                              continue;
                           }
                        }

                        BarColor var38 = null;
                        if (var9.bc("boss_bar.color", null) instanceof String var39) {
                           var38 = BukkitRegistryLookup.barColor(var39);
                           if (var38 == null) {
                              Log.error("Invalid 'boss_bar.color' for entity '" + var8 + "'. File: " + var4.DJ());
                              continue;
                           }
                        }

                        MobAttributeEntry var40 = null;
                        if (var9.bc("sounds.ambient.sound", null) instanceof String var41) {
                           var40 = new MobAttributeEntry(
                              var41, var9.method7080("sounds.ambient.chance_per_tick", 0.1), var9.method7076("sounds.ambient.cooldown_ticks", 100)
                           );
                        }

                        Double var42 = var9.method7103("name_tag.y_offset", null);
                        if (var42 != null && !Double.isFinite(var42)) {
                           Log.error("Invalid 'name_tag.y_offset' for entity '" + var8 + "'. File: " + var4.DJ());
                        } else {
                           Object var43 = var9.kf("waterline_offset");
                           double var19 = var43 == null ? 0.0 : (var43 instanceof Number var21 ? var21.doubleValue() : Double.NaN);
                           if (!Double.isFinite(var19)) {
                              Log.error("Invalid 'waterline_offset' for entity '" + var8 + "'. File: " + var4.DJ());
                           } else {
                              Double var44;
                              try {
                                 var44 = method10095(null, var9.kf("boss_bar.distance"), "entities." + var8 + ".boss_bar.distance in " + var4.DJ());
                              } catch (IllegalArgumentException var34) {
                                 Log.error(var34.getMessage());
                                 continue;
                              }

                              boolean var22;
                              Boolean var23;
                              Boolean var24;
                              Map var25;
                              Map var26;
                              boolean var27;
                              SpawnLimitEntry var28;
                              ControlScheme var29;
                              try {
                                 var22 = method10084(false, var9.kf("vanilla_defaults"), "entities." + var8 + ".vanilla_defaults");
                                 var23 = method10088(var9, "behaviors.item_pickup", var12, Mob.class, "entities." + var8);
                                 var24 = method10088(var9, "behaviors.raid_participation", var12, Raider.class, "entities." + var8);
                                 var25 = method10089(var9, "entities." + var8);
                                 var26 = method10090(var9, "entities." + var8);
                                 var27 = method10084(false, var9.kf("age_visual_scale"), "entities." + var8 + ".age_visual_scale");
                                 var28 = parseSpawnLimitEntry(var9, var12, "entities." + var8);
                                 var29 = parseControlScheme(var9, "entities." + var8);
                              } catch (IllegalArgumentException var33) {
                                 Log.error(var33.getMessage() + ". File: " + var4.DJ());
                                 continue;
                              }

                              Double var30 = var22 ? null : 0.23;
                              if (var9.ke("speed.movement")) {
                                 var30 = var9.kl("speed.movement");
                              }

                              Float var31 = var22 ? null : 0.23F;
                              if (var9.ke("speed.flying")) {
                                 var31 = var9.jQ("speed.flying");
                              }

                              CustomMobSettingsBuilder var32 = CustomMobSettings.toBuilder()
                                 .withModel(var13)
                                 .withConfigFile(var4)
                                 .withVanillaDefaults(var22)
                                 .withItemPickup(var23)
                                 .withRaidParticipation(var24)
                                 .withDamageModifiers(var25)
                                 .withNativeAttributes(var26)
                                 .withAgeVisualScale(var27)
                                 .withInitialAgeTicks(var28.limit())
                                 .withInitialBaby(var28.enabled())
                                 .withSilentConfigured(var9.ke("silent"))
                                 .withGravityConfigured(var9.ke("gravity"))
                                 .withInvulnerableConfigured(var9.ke("invulnerable"))
                                 .withAiConfigured(var9.ke("ai"))
                                 .withDisplayName(var9.bc("display_name", var8))
                                 .withShowNameTag(var9.method7078("name_tag.enabled", false))
                                 .withNameTagYOffset(var42)
                                 .withEntityType(var12)
                                 .withHasBossbar(var9.method7078("boss_bar.enabled", false))
                                 .withBossBarDistance(var44)
                                 .withSilent(var9.method7078("silent", !var22))
                                 .withCanSunBurn(var9.method7078("sun_burn", var9.method7078("can_sun_burn", var22)))
                                 .withCanDropLoot(var9.method7078("drop_vanilla_loot", var9.method7078("can_drop_loot", var22)))
                                 .withMountOnInteract(var9.method7078("mount_on_interact", true))
                                 .withShadow(var9.method7078("shadow", true))
                                 .withMaxHealth(var9.method7103("max_health", null))
                                 .withRenderDistance(var11)
                                 .withBossBarStyle(var14 == null ? BarStyle.SEGMENTED_6 : var14)
                                 .withBossBarColor(var38 == null ? BarColor.PURPLE : var38)
                                 .withMovementSpeed(var30)
                                 .withFlyingSpeed(var31)
                                 .withInvulnerable(var9.kj("invulnerable"))
                                 .withGravity(var9.method7078("gravity", true))
                                 .withFloatOnWater(var9.method7078("float_on_water", false))
                                 .withWaterlineOffset(var19)
                                 .withVehicleControls(var29)
                                 .withAi(var9.method7078("ai", true))
                                 .withEyesHeight(var9.method7080("eyes_height", 0.25))
                                 .withSoundAmbient(var40)
                                 .withSoundAttack(var9.bc("sounds.attack", null))
                                 .withSoundHurt(var9.bc("sounds.hurt", null))
                                 .withSoundDeath(var9.bc("sounds.death", null))
                                 .withAllActionsPermission(var9.bc("all_actions_permission", null));
                              CustomEntityManager.getInstance().registerSettingsBuilder(var32);
                           }
                        }
                     }
                  }
               }
            }
         } else if (Settings.get().isLoggingEnabled("logging.entities.loading")) {
            Log.log(ChatColor.YELLOW + "    No entity found in file " + var4.GQ());
         }
      }

      this.method10080(var1);

      for (String var37 : var2) {
         if (CustomEntityManager.getInstance().getSettings(var37) == null || !CustomEntityManager.getInstance().getSettings(var37).vn.of()) {
            Log.warn("Ignored entity override for " + var37 + ": no matching V2 .iaentity bundle");
         }
      }

      this.FT();
   }

   private void method10080(ContentManager var1) {
      for (File var3 : var1.field84) {
         try (Stream var4 = listFiles(var3.toPath())) {
            var4.forEach(var3x -> this.method10082(var1, var3, var3x));
         } catch (IOException var9) {
            Log.error("Failed to scan entity bundles in " + var3.getAbsolutePath(), var9);
         }
      }
   }

   static Stream listFiles(Path var0) {
      return Files.walk(var0)
         .filter(var0x -> Files.isRegularFile(var0x))
         .filter(var0x -> !var0x.getFileName().toString().startsWith("._"))
         .filter(var0x -> var0x.getFileName().toString().endsWith(".iaentity") || var0x.getFileName().toString().endsWith(".iaemotes"))
         .sorted(Comparator.comparing(Path::toString));
   }

   private void method10082(ContentManager var1, File var2, Path var3) {
      try {
         AnimationBinaryPayload var4 = AnimationBinaryCodec.method9161(Files.readAllBytes(var3));
         JSONObject var5 = var4.vH().getJSONObject("entity");
         boolean var6 = var5 != null && var5.getBooleanValue("playerEmote");
         if (var3.getFileName().toString().endsWith(".iaemotes") && !var6) {
            throw new IllegalArgumentException(".iaemotes requires a V2 player emote bundle");
         }

         if (var5 == null || !var6 && var4.vH().getJSONObject("geometryModels") == null) {
            throw new IllegalArgumentException("Standalone .iaentity requires entity and geometryModels");
         }

         if (var6 && (!Settings.get().emotesEnabled || Settings.get().emotesUseArmorStands)) {
            return;
         }

         String var7 = var5.getString("namespace");
         String var8 = var5.getString("id");
         if (!var2.getName().equals(var7)) {
            throw new IllegalArgumentException("Bundle namespace '" + var7 + "' does not match contents folder '" + var2.getName() + "'");
         }

         var1.as.add(var7);
         String var9 = var7 + ":" + var8;
         if (CustomEntityManager.getInstance().getSettings(var9) != null) {
            throw new IllegalArgumentException("Duplicate custom entity '" + var9 + "'");
         }

         EntityType var10 = (EntityType)BukkitRegistryLookup.enumOrNull(var5.getString("type"), EntityType.class);
         if (var10 == null) {
            throw new IllegalArgumentException("Unknown base entity type '" + var5.getString("type") + "'");
         }

         CustomEntityModel var11 = new CustomEntityModel(var7, var8);
         if (!var11.method449(var3.toFile(), var4)) {
            return;
         }

         boolean var12 = !var6 && var5.getBooleanValue("ragdollOnDeath");
         BoneStateList var13 = var11.nU();
         boolean var14 = false;
         boolean var15 = true;
         boolean var16 = false;
         double var17 = 0.0;
         Double var19 = Settings.get().customEntityRenderDistance;
         String var20 = var8;
         boolean var21 = false;
         Double var22 = null;
         boolean var23 = false;
         Double var24 = null;
         BarStyle var25 = BarStyle.SEGMENTED_6;
         BarColor var26 = BarColor.PURPLE;
         boolean var27 = false;
         boolean var28 = false;
         boolean var29 = false;
         boolean var30 = false;
         ControlScheme var31 = ControlScheme.yc;
         boolean var32 = false;
         boolean var33 = false;
         boolean var34 = false;
         boolean var35 = false;
         List var36 = List.of();
         boolean var37 = false;
         boolean var38 = false;
         SpawnLimits var39 = SpawnLimits.wv;
         JSONObject var40 = var5.getJSONObject("clientInterpolation");
         if (var40 != null) {
            var39 = new SpawnLimits(true, 4, var40.getBooleanValue("forceAll"), var40.getIntValue("intervalTicks"), var40.getIntValue("idleIntervalTicks"));
         }

         boolean var41 = false;
         ConfigSectionWrapper var42 = null;
         YamlConfig var43 = null;
         YamlConfig var44 = null;

         for (YamlConfig var46 : var1.method8092()) {
            if (var7.equals(var46.getString("info.namespace"))) {
               ConfigSectionWrapper var47 = var46.kc("entities." + var8);
               if (var47 != null && !var47.ke("model_folder") && !var6) {
                  if (var47.ke("damageable_parts")) {
                     if (var13 == null && !var14) {
                        var13 = var11.nV();
                     }

                     if (var14 || var13 == null) {
                        throw new IllegalArgumentException("Duplicate damageable_parts override or bundle has no parts: " + var9);
                     }

                     var14 = true;
                     ConfigSectionWrapper var48 = var47.kc("damageable_parts");
                     if (var48 == null) {
                        throw new IllegalArgumentException("damageable_parts must be a UUID-keyed section");
                     }

                     JSONObject var49 = new JSONObject();

                     for (String var51 : var48.bJ(false)) {
                        ConfigSectionWrapper var52 = var48.kc(var51);
                        if (var52 == null) {
                           throw new IllegalArgumentException("Part override must be a section: " + var51);
                        }

                        JSONObject var53 = new JSONObject();

                        for (String var55 : var52.bJ(false)) {
                           var53.put(var55, var52.kf(var55));
                        }

                        var49.put(var51, var53);
                     }

                     var13 = var13.parseList(JSONObject.parseObject(var49.toJSONString(new Feature[0])));
                  }

                  if (var47.ke("goals")) {
                     if (var43 != null) {
                        throw new IllegalArgumentException("Duplicate goals override for " + var9);
                     }

                     var43 = var46;
                  }

                  if (var47.ke("events")) {
                     if (var44 != null) {
                        throw new IllegalArgumentException("Duplicate events override for " + var9);
                     }

                     var44 = var46;
                  }

                  if (var47.ke("vanilla_defaults")
                     || var47.ke("silent")
                     || var47.ke("sun_burn")
                     || var47.ke("can_sun_burn")
                     || var47.ke("drop_vanilla_loot")
                     || var47.ke("can_drop_loot")
                     || var47.ke("speed")
                     || var47.ke("max_health")
                     || var47.ke("ai")
                     || var47.ke("gravity")
                     || var47.ke("invulnerable")
                     || var47.ke("behaviors")
                     || var47.ke("attributes")
                     || var47.ke("initial_age_ticks")
                     || var47.ke("initial_baby")) {
                     if (var42 != null) {
                        throw new IllegalArgumentException("Duplicate vanilla gameplay override for " + var9);
                     }

                     var42 = var47;
                  }

                  if (var47.ke("controls")) {
                     if (var30) {
                        throw new IllegalArgumentException("Duplicate controls override for " + var9);
                     }

                     var30 = true;
                     var31 = parseControlScheme(var47, "entities." + var8);
                  }

                  if (var47.ke("client_interpolation")) {
                     if (var41) {
                        throw new IllegalArgumentException("Duplicate client_interpolation override for " + var9);
                     }

                     var41 = true;
                     ConfigSectionWrapper var61 = var47.kc("client_interpolation");
                     if (var61 == null) {
                        throw new IllegalArgumentException("client_interpolation must be a section for " + var9);
                     }

                     var39 = mergeSpawnLimits(var61, "entities." + var8 + ".client_interpolation", var39);
                  }

                  if (var47.ke("ragdoll_on_death")) {
                     if (var27) {
                        throw new IllegalArgumentException("Duplicate ragdoll_on_death override for " + var9);
                     }

                     var27 = true;
                     var12 = method10084(var12, var47.kf("ragdoll_on_death"), "entities." + var8 + ".ragdoll_on_death in " + var46.DJ());
                  }

                  if (var47.ke("vanilla_death_animation") && var11.method473(AnimationState.DEATH) != null) {
                     if (var28) {
                        throw new IllegalArgumentException("Duplicate vanilla_death_animation override for " + var9);
                     }

                     var28 = true;
                     var15 = method10084(true, var47.kf("vanilla_death_animation"), "entities." + var8 + ".vanilla_death_animation in " + var46.DJ());
                  }

                  if (var47.ke("float_on_water") || var47.ke("waterline_offset")) {
                     if (var29) {
                        throw new IllegalArgumentException("Duplicate water float override for " + var9);
                     }

                     var29 = true;
                     var16 = method10084(false, var47.kf("float_on_water"), "entities." + var8 + ".float_on_water in " + var46.DJ());
                     Object var62 = var47.kf("waterline_offset");
                     var17 = var62 == null ? 0.0 : (var62 instanceof Number var65 ? var65.doubleValue() : Double.NaN);
                     if (!Double.isFinite(var17)) {
                        throw new IllegalArgumentException("Invalid waterline_offset for " + var9);
                     }
                  }

                  if (var47.ke("render_distance")) {
                     if (var32) {
                        throw new IllegalArgumentException("Duplicate render_distance override for " + var9);
                     }

                     var32 = true;
                     var19 = method10095(
                        Settings.get().customEntityRenderDistance, var47.kf("render_distance"), "entities." + var8 + ".render_distance in " + var46.DJ()
                     );
                  }

                  if (var47.ke("age_visual_scale")) {
                     if (var34) {
                        throw new IllegalArgumentException("Duplicate age_visual_scale override for " + var9);
                     }

                     var34 = true;
                     var35 = method10084(false, var47.kf("age_visual_scale"), "entities." + var8 + ".age_visual_scale in " + var46.DJ());
                  }

                  if (var47.ke("age_stages")) {
                     if (var38) {
                        throw new IllegalArgumentException("Duplicate age_stages override for " + var9);
                     }

                     var38 = true;
                     ControlSchemeSettings var63 = parseControlSchemes(var47.kf("age_stages"), "entities." + var8 + ".age_stages in " + var46.DJ());
                     var36 = var63.schemes();
                     var37 = var63.enabled();
                  }

                  if (var47.ke("display_name") || var47.ke("name_tag") || var47.ke("boss_bar")) {
                     if (var33) {
                        throw new IllegalArgumentException("Duplicate presentation override for " + var9);
                     }

                     var33 = true;
                     var20 = var47.bc("display_name", var8);
                     var21 = var47.method7078("name_tag.enabled", false);
                     var22 = var47.method7103("name_tag.y_offset", null);
                     if (var22 != null && !Double.isFinite(var22)) {
                        throw new IllegalArgumentException("Invalid name_tag.y_offset for " + var9);
                     }

                     var23 = var47.method7078("boss_bar.enabled", false);
                     var24 = method10095(null, var47.kf("boss_bar.distance"), "entities." + var8 + ".boss_bar.distance in " + var46.DJ());
                     if (var47.ke("boss_bar.style")) {
                        var25 = BukkitRegistryLookup.barStyle(var47.jO("boss_bar.style"));
                        if (var25 == null) {
                           throw new IllegalArgumentException("Invalid boss_bar.style for " + var9);
                        }
                     }

                     if (var47.ke("boss_bar.color")) {
                        var26 = BukkitRegistryLookup.barColor(var47.jO("boss_bar.color"));
                        if (var26 == null) {
                           throw new IllegalArgumentException("Invalid boss_bar.color for " + var9);
                        }
                     }
                  }
               }
            }
         }

         if (var35 && !var36.isEmpty()) {
            throw new IllegalArgumentException("age_visual_scale cannot be enabled with age_stages for " + var9);
         }

         boolean var58 = var42 != null && method10084(false, var42.kf("vanilla_defaults"), "entities." + var8 + ".vanilla_defaults");
         Boolean var59 = method10088(var42, "behaviors.item_pickup", var10, Mob.class, "entities." + var8);
         Boolean var60 = method10088(var42, "behaviors.raid_participation", var10, Raider.class, "entities." + var8);
         Map var64 = method10089(var42, "entities." + var8);
         Map var66 = method10090(var42, "entities." + var8);
         SpawnLimitEntry var67 = parseSpawnLimitEntry(var42, var10, "entities." + var8);
         boolean var68 = var42 == null ? true : method10084(!var58, var42.kf("silent"), "entities." + var8 + ".silent");
         boolean var69 = var42 == null
            ? var5.getBooleanValue("canSunBurn")
            : method10084(
               var58 || var5.getBooleanValue("canSunBurn"),
               var42.ke("sun_burn") ? var42.kf("sun_burn") : var42.kf("can_sun_burn"),
               "entities." + var8 + ".sun_burn"
            );
         boolean var70 = var42 != null
            && method10084(
               var58, var42.ke("drop_vanilla_loot") ? var42.kf("drop_vanilla_loot") : var42.kf("can_drop_loot"), "entities." + var8 + ".drop_vanilla_loot"
            );
         Double var71 = method10091(var42 == null ? null : var42.kf("speed.movement"), var58 ? null : 0.23, "entities." + var8 + ".speed.movement");
         Double var72 = method10091(var42 == null ? null : var42.kf("max_health"), null, "entities." + var8 + ".max_health");
         if (var72 != null && var72 <= 0.0) {
            throw new IllegalArgumentException("entities." + var8 + ".max_health must be a positive finite number");
         }

         Double var56 = method10091(var42 == null ? null : var42.kf("speed.flying"), var58 ? null : 0.23, "entities." + var8 + ".speed.flying");
         if (var12 && var11.nY() == null) {
            Log.warn("ragdoll_on_death for " + var9 + " has no authored V2 physics profile; normal death animation will play");
         }

         CustomEntityManager.getInstance()
            .registerSettingsBuilder(
               CustomMobSettings.toBuilder()
                  .withModel(var11)
                  .withConfigFile(var44)
                  .withGoalsConfigFile(var43)
                  .withVanillaDefaults(var58)
                  .withItemPickup(var59)
                  .withRaidParticipation(var60)
                  .withDamageModifiers(var64)
                  .withNativeAttributes(var66)
                  .withAgeVisualScale(var35)
                  .withClientInterpolation(var39)
                  .withInitialAgeTicks(var67.limit())
                  .withInitialBaby(var67.enabled())
                  .withAgeStages(var36)
                  .withBinaryAgeStages(var37)
                  .withSilentConfigured(var42 != null && var42.ke("silent"))
                  .withGravityConfigured(var42 != null && var42.ke("gravity"))
                  .withInvulnerableConfigured(var42 != null && var42.ke("invulnerable"))
                  .withAiConfigured(var42 != null && var42.ke("ai"))
                  .withDisplayName(var20)
                  .withShowNameTag(var21)
                  .withNameTagYOffset(var22)
                  .withEntityType(var10)
                  .withHasBossbar(var23)
                  .withBossBarDistance(var24)
                  .withSilent(var68)
                  .withCanSunBurn(var69)
                  .withCanDropLoot(var70)
                  .withMountOnInteract(true)
                  .withShadow(true)
                  .withMaxHealth(var72)
                  .withBossBarStyle(var25)
                  .withBossBarColor(var26)
                  .withMovementSpeed(var71)
                  .withFlyingSpeed(var56 == null ? null : var56.floatValue())
                  .withInvulnerable(var42 != null && method10084(false, var42.kf("invulnerable"), "entities." + var8 + ".invulnerable"))
                  .withGravity(var42 == null || method10084(true, var42.kf("gravity"), "entities." + var8 + ".gravity"))
                  .withFloatOnWater(var16)
                  .withWaterlineOffset(var17)
                  .withVehicleControls(var31)
                  .withAi(var42 == null || method10084(true, var42.kf("ai"), "entities." + var8 + ".ai"))
                  .withRagdollOnDeath(var12)
                  .withDamageableParts(var13)
                  .withVanillaDeathAnimation(var15)
                  .withRenderDistance(var19)
                  .withEyesHeight(0.25)
            );
         Log.log("Loaded entity bundle " + var9);
      } catch (IOException | IllegalArgumentException var57) {
         Log.error("Failed to load entity bundle: " + var3);
         Log.error(var57.getMessage());
      }
   }

   public static ControlScheme parseControlScheme(ConfigSectionWrapper var0, String var1) {
      if (!var0.ke("controls")) {
         return ControlScheme.yc;
      }

      ConfigSectionWrapper var2 = var0.kc("controls");
      if (var2 == null) {
         throw new IllegalArgumentException(var1 + ".controls must be a section");
      }

      if (var2.bJ(false).stream().anyMatch(var0x -> !var0x.equals("steering") && !var0x.equals("strafe") && !var0x.equals("collision"))) {
         throw new IllegalArgumentException(var1 + ".controls contains an unknown key");
      }

      Object var3 = var2.kf("steering");
      ControlInputMode var4 = var3 == null
         ? ControlInputMode.MOUSE
         : (var3 instanceof String var5 ? (ControlInputMode)BukkitRegistryLookup.enumOrNull(var5.toUpperCase(Locale.ROOT), ControlInputMode.class) : null);
      if (var4 == null) {
         throw new IllegalArgumentException(var1 + ".controls.steering must be MOUSE or WASD");
      }

      FloatPair var11 = null;
      if (var2.ke("collision")) {
         ConfigSectionWrapper var6 = var2.kc("collision");
         if (var6 == null
            || var6.bJ(false).stream().anyMatch(var0x -> !var0x.equals("width") && !var0x.equals("height"))
            || !(var6.kf("width") instanceof Number var7)
            || !(var6.kf("height") instanceof Number var8)) {
            throw new IllegalArgumentException(var1 + ".controls.collision requires numeric width and height only");
         }

         try {
            var11 = new FloatPair(var7.floatValue(), var8.floatValue());
         } catch (IllegalArgumentException var10) {
            throw new IllegalArgumentException(var1 + "." + var10.getMessage());
         }
      }

      return new ControlScheme(var4, method10084(false, var2.kf("strafe"), var1 + ".controls.strafe"), var11);
   }

   public static boolean method10084(boolean var0, @Nullable Object var1, String var2) {
      if (var1 == null) {
         return var0;
      } else if (var1 instanceof Boolean var3) {
         return var3;
      } else {
         throw new IllegalArgumentException(var2 + " must be true or false");
      }
   }

   public static ControlSchemeSettings parseControlSchemes(@Nullable Object var0, String var1) {
      List var2 = ConfigSectionWrapper.method7083(var0, var1);
      ArrayList var3 = new ArrayList();
      Boolean var4 = null;
      HashSet var5 = new HashSet();

      for (Object var7 : var2) {
         Map var8 = (Map)var7;
         if (var8.keySet().stream().anyMatch(var0x -> !var0x.equals("from_age_ticks") && !var0x.equals("state") && !var0x.equals("entity"))) {
            throw new IllegalArgumentException(var1 + " entry contains an unknown key");
         }

         boolean var9 = var8.containsKey("from_age_ticks");
         boolean var10 = var8.containsKey("state");
         if (var9 == var10) {
            throw new IllegalArgumentException(var1 + " entries require exactly one of from_age_ticks or state");
         }

         boolean var11 = var10;
         if (var4 != null && var4 != var11) {
            throw new IllegalArgumentException(var1 + " cannot mix numeric and binary entries");
         }

         var4 = var11;
         if (!(var8.get("entity") instanceof String var13 && var13.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))) {
            throw new IllegalArgumentException(var1 + " entity must be a namespaced ID");
         }

         int var14;
         if (var9) {
            if (!(var8.get("from_age_ticks") instanceof Number var16)
               || var16.doubleValue() != Math.rint(var16.doubleValue())
               || var16.longValue() < -2147483648L
               || var16.longValue() > 0L) {
               throw new IllegalArgumentException(var1 + " from_age_ticks must be a nonpositive integer");
            }

            var14 = var16.intValue();
            if (!var5.add("n:" + var14)) {
               throw new IllegalArgumentException(var1 + " numeric thresholds must be unique");
            }
         } else {
            if (!(var8.get("state") instanceof String var18) || !var18.equals("baby") && !var18.equals("adult")) {
               throw new IllegalArgumentException(var1 + " state must be baby or adult");
            }

            var14 = var18.equals("baby") ? -1 : 0;
            if (!var5.add("b:" + var18)) {
               throw new IllegalArgumentException(var1 + " binary states must be unique");
            }
         }

         var3.add(new CustomEntityRef(var14, var13));
      }

      var3.sort(Comparator.comparingInt(CustomEntityRef::index));
      return new ControlSchemeSettings(List.copyOf(var3), Boolean.TRUE.equals(var4));
   }

   public static SpawnLimitEntry parseSpawnLimitEntry(@Nullable ConfigSectionWrapper var0, EntityType var1, String var2) {
      if (var0 == null) {
         return new SpawnLimitEntry(null, null);
      }

      boolean var3 = var0.ke("initial_age_ticks");
      boolean var4 = var0.ke("initial_baby");
      if (var3 && var4) {
         throw new IllegalArgumentException(var2 + " cannot set both initial_age_ticks and initial_baby");
      }

      if (!var3 && !var4) {
         return new SpawnLimitEntry(null, null);
      }

      Class var5 = var1.getEntityClass();
      if (var5 == null || !Ageable.class.isAssignableFrom(var5)) {
         throw new IllegalArgumentException(var2 + " initial age requires an Ageable base type");
      }

      if (var4) {
         if (var0.kf("initial_baby") instanceof Boolean var9) {
            return new SpawnLimitEntry(null, var9);
         } else {
            throw new IllegalArgumentException(var2 + ".initial_baby must be true or false");
         }
      } else if (!isSupported(var1)) {
         throw new IllegalArgumentException(var2 + ".initial_age_ticks requires a numeric-age base; use initial_baby");
      } else if (var0.kf("initial_age_ticks") instanceof Number var7
         && Double.isFinite(var7.doubleValue())
         && var7.doubleValue() == Math.rint(var7.doubleValue())
         && var7.longValue() >= -2147483648L
         && var7.longValue() <= 0L) {
         return new SpawnLimitEntry(var7.intValue(), null);
      } else {
         throw new IllegalArgumentException(var2 + ".initial_age_ticks must be a nonpositive integer");
      }
   }

   private static boolean isSupported(EntityType var0) {
      Class var1 = var0.getEntityClass();
      return var1 != null
         && Ageable.class.isAssignableFrom(var1)
         && !Zombie.class.isAssignableFrom(var1)
         && !PiglinAbstract.class.isAssignableFrom(var1)
         && !Zoglin.class.isAssignableFrom(var1);
   }

   private void FT() {
      for (YamlConfig var2 : ContentManager.method8084().method8092()) {
         for (String var4 : var2.getStringList("entities") == null ? List.of() : var2.getStringList("entities")) {
            String var5 = var2.getString("info.namespace") + ":" + var4;
            CustomMobSettings var6 = CustomEntityManager.getInstance().getSettings(var5);
            if (var6 != null && !var6.vX.isEmpty()) {
               if (var6.vw.getEntityClass() == null || !Mob.class.isAssignableFrom(var6.vw.getEntityClass())) {
                  Log.error("age_stages for " + var6.getNamespacedId() + " requires a Mob base type");
               }

               if (!var6.vY && !isSupported(var6.vw)) {
                  Log.error("Numeric age_stages for " + var6.getNamespacedId() + " requires a numeric-age base type");
               }

               for (int var7 = 0; var7 < var6.vX.size(); var7++) {
                  CustomEntityRef var8 = (CustomEntityRef)var6.vX.get(var7);
                  CustomMobSettings var9 = CustomEntityManager.getInstance().getSettings(var8.namespacedId());
                  if (var9 != null && var9.vn.of()) {
                     Class var10 = var9.vw.getEntityClass();
                     if (var10 == null || !Mob.class.isAssignableFrom(var10)) {
                        Log.error("age_stages target '" + var8.namespacedId() + "' for " + var6.getNamespacedId() + " must use a Mob base type");
                     }

                     if (!var6.vY && var7 < var6.vX.size() - 1 && !isSupported(var9.vw)) {
                        Log.error(
                           "Nonterminal numeric age_stages target '" + var8.namespacedId() + "' for " + var6.getNamespacedId() + " must have numeric age"
                        );
                     }
                  } else {
                     Log.error("age_stages target '" + var8.namespacedId() + "' for " + var6.getNamespacedId() + " must be a registered V2 model");
                  }
               }
            }
         }
      }
   }

   @Nullable
   private static Boolean method10088(@Nullable ConfigSectionWrapper var0, String var1, EntityType var2, Class var3, String var4) {
      if (var0 != null && var0.ke(var1)) {
         if (var0.kf(var1) instanceof Boolean var6) {
            Class var7 = var2.getEntityClass();
            if (var7 != null && var3.isAssignableFrom(var7)) {
               return var6;
            } else {
               throw new IllegalArgumentException(var4 + "." + var1 + " requires a " + var3.getSimpleName() + " base");
            }
         } else {
            throw new IllegalArgumentException(var4 + "." + var1 + " must be true or false");
         }
      } else {
         return null;
      }
   }

   public static Map method10089(@Nullable ConfigSectionWrapper var0, String var1) {
      if (var0 != null && var0.ke("behaviors.damage_modifiers")) {
         ConfigSectionWrapper var2 = var0.kc("behaviors.damage_modifiers");
         if (var2 == null) {
            throw new IllegalArgumentException(var1 + ".behaviors.damage_modifiers must be a map");
         }

         EnumMap var3 = new EnumMap(DamageCause.class);

         for (String var5 : var2.bJ(false)) {
            DamageCause var6;
            try {
               var6 = DamageCause.valueOf(var5.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException var9) {
               throw new IllegalArgumentException(var1 + ".behaviors.damage_modifiers." + var5 + " has an unknown damage cause");
            }

            if (!(var2.kf(var5) instanceof Number var8 && Double.isFinite(var8.doubleValue()) && !(var8.doubleValue() < 0.0))) {
               throw new IllegalArgumentException(var1 + ".behaviors.damage_modifiers." + var5 + " must be a finite nonnegative number");
            }

            if (var3.putIfAbsent(var6, var8.doubleValue()) != null) {
               throw new IllegalArgumentException(var1 + ".behaviors.damage_modifiers has duplicate cause " + var6);
            }
         }

         return Map.copyOf(var3);
      } else {
         return Map.of();
      }
   }

   public static Map method10090(@Nullable ConfigSectionWrapper var0, String var1) {
      if (var0 != null && var0.ke("attributes")) {
         ConfigSectionWrapper var2 = var0.kc("attributes");
         if (var2 == null) {
            throw new IllegalArgumentException(var1 + ".attributes must be a map");
         }

         HashMap var3 = new HashMap();

         for (String var5 : var2.bJ(false)) {
            Double var6 = (Double)arU.get(var5);
            if (var6 == null) {
               throw new IllegalArgumentException(var1 + ".attributes." + var5 + " is not a supported native attribute");
            }

            if (!(var2.kf(var5) instanceof Number var8) || !Double.isFinite(var8.doubleValue()) || var8.doubleValue() < 0.0 || var8.doubleValue() > var6) {
               throw new IllegalArgumentException(var1 + ".attributes." + var5 + " must be a finite number from 0 to " + var6);
            }

            var3.put(var5, var8.doubleValue());
         }

         return Map.copyOf(var3);
      } else {
         return Map.of();
      }
   }

   @Nullable
   private static Double method10091(@Nullable Object var0, @Nullable Double var1, String var2) {
      if (var0 == null) {
         return var1;
      } else if (var0 instanceof Number var3 && Double.isFinite(var3.doubleValue())) {
         return var3.doubleValue();
      } else {
         throw new IllegalArgumentException(var2 + " must be a finite number");
      }
   }

   public static SpawnLimits parseSpawnLimits(@Nullable ConfigSectionWrapper var0, String var1) {
      return mergeSpawnLimits(var0, var1, SpawnLimits.wv);
   }

   public static SpawnLimits mergeSpawnLimits(@Nullable ConfigSectionWrapper var0, String var1, SpawnLimits var2) {
      if (var0 == null) {
         return var2;
      }

      for (String var4 : var0.bJ(false)) {
         if (!Set.of("enabled", "settle_ticks", "force_all", "interval_ticks", "idle_interval_ticks").contains(var4)) {
            throw new IllegalArgumentException(var1 + " has unknown key " + var4);
         }
      }

      boolean var7 = method10084(var2.enabled(), var0.kf("enabled"), var1 + ".enabled");
      Object var8 = var0.kf("settle_ticks");
      int var5 = var2.perChunk();
      if (var8 != null) {
         if (!(var8 instanceof Number var6)
            || !Double.isFinite(var6.doubleValue())
            || var6.doubleValue() != Math.rint(var6.doubleValue())
            || var6.doubleValue() < 2.0
            || var6.doubleValue() > 40.0) {
            throw new IllegalArgumentException(var1 + ".settle_ticks must be an integer from 2 to 40");
         }

         var5 = var6.intValue();
      }

      boolean var9 = method10084(var2.lL(), var0.kf("force_all"), var1 + ".force_all");
      return new SpawnLimits(
         var7,
         var5,
         var9,
         method10094(var0.kf("interval_ticks"), var2.lM(), var1 + ".interval_ticks"),
         method10094(var0.kf("idle_interval_ticks"), var2.lN(), var1 + ".idle_interval_ticks")
      );
   }

   private static int method10094(Object var0, int var1, String var2) {
      if (var0 == null) {
         return var1;
      } else if (var0 instanceof Number var3
         && Double.isFinite(var3.doubleValue())
         && var3.doubleValue() == Math.rint(var3.doubleValue())
         && !(var3.doubleValue() < 1.0)
         && !(var3.doubleValue() > 8.0)) {
         return var3.intValue();
      } else {
         throw new IllegalArgumentException(var2 + " must be an integer from 1 to 8");
      }
   }

   @Nullable
   public static Double method10095(@Nullable Double var0, @Nullable Object var1, String var2) {
      if (var1 == null) {
         return var0;
      } else if (var1 instanceof Number var3 && Double.isFinite(var3.doubleValue()) && !(var3.doubleValue() <= 0.0)) {
         return var3.doubleValue();
      } else {
         throw new IllegalArgumentException(var2 + " must be a positive finite number");
      }
   }
}
