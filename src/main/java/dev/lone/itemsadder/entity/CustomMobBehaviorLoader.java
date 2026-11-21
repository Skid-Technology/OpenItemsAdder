package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.ai.AbstractMobGoal;
import dev.lone.itemsadder.ai.MobGoalEventListener;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.ItemMatcher;
import dev.lone.itemsadder.item.MaterialItemMatcher;
import dev.lone.itemsadder.item.NamespacedItemMatcher;
import dev.lone.itemsadder.nms.MobGoalService;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.world.BlockDataMatcher;
import dev.lone.itemsadder.world.BlockStateMatcher;
import dev.lone.itemsadder.world.NamespacedBlockMatcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public class CustomMobBehaviorLoader {
   private static final NamespacedKey Zo = new NamespacedKey("itemsadder", "entity_spawn_home_position");
   private static final NamespacedKey Zp = new NamespacedKey("itemsadder", "entity_spawn_home_world");
   private static final Map Zq = new HashMap();
   private static final Map Zr = Map.of(
      "wanderer",
      Map.of("random_wander_around", Map.of("priority", 8), "random_look_around", Map.of("priority", 9)),
      "observer",
      Map.of("look_at_entity", Map.of("priority", 7, "entity", "PLAYER"), "random_look_around", Map.of("priority", 9)),
      "timid",
      Map.of("avoid_entity", Map.of("priority", 2, "entity", "PLAYER"), "random_wander_around", Map.of("priority", 8)),
      "retaliator",
      Map.of("hurt_by_target", Map.of("priority", 1), "melee_attack", Map.of("priority", 2)),
      "melee_hunter",
      Map.of("attack_near", Map.of("priority", 2, "entity", "PLAYER"), "melee_attack", Map.of("priority", 2)),
      "bow_hunter",
      Map.of("attack_near", Map.of("priority", 2, "entity", "PLAYER"), "ranged_bow_attack", Map.of("priority", 4))
   );
   private final CustomMobSettings Zs;
   private final List Zt = new ArrayList();
   private final Map Zu = new IdentityHashMap();
   private final List Zv = new ArrayList();
   private boolean Zw = false;
   @Nullable
   private Integer Zx;

   private static void registerMatcher(String var0, MatcherLoader var1) {
      Zq.put(var0, var1);
   }

   public CustomMobBehaviorLoader(CustomMobSettings var1) {
      this.Zs = var1;
   }

   public void load() {
      this.Zt.clear();
      this.Zu.clear();
      this.Zv.clear();
      this.Zw = false;
      this.Zx = null;
      String var1 = this.Zs.vn.getNamespacedID();
      YamlConfig var2 = this.Zs.vq != null ? this.Zs.vq : this.Zs.getConfig();
      if (var2 != null) {
         ConfigSectionWrapper var3 = var2.kc("entities." + this.Zs.vn.dl + ".goals");
         if (var3 != null) {
            String var4 = var3.jO("preset");
            if (!var3.ke("preset") || var4 != null && Zr.containsKey(var4)) {
               if ("bow_hunter".equals(var4) && !EntityMatcherRootO.matchesType(this.Zs.vw)) {
                  Log.error("Goal preset 'bow_hunter' is incompatible with base type '" + this.Zs.vw + "' for entity '" + var1 + "'. File: " + var2.DJ());
               } else {
                  this.Zw = var3.method7078("clear_all", false);
                  ConfigSectionWrapper var5 = var3.kc("vanilla");
                  if (var5 != null) {
                     for (String var7 : List.of("goals", "targets")) {
                        ConfigSectionWrapper var8 = var5.kc(var7);
                        if (var8 != null) {
                           for (String var10 : var8.abO()) {
                              ConfigSectionWrapper var11 = var8.kc(var10);
                              if (var11 != null && !var10.isBlank()) {
                                 Integer var12 = var11.ke("priority") ? var11.jP("priority") : null;
                                 if (var12 != null && var12 < 0) {
                                    Log.error("Invalid priority for vanilla goal '" + var10 + "' in entity '" + var1 + "'. File: " + var2.DJ());
                                 } else {
                                    this.Zv.add(new MobGoalConfig(var7.equals("targets"), var10, var12, var11.method7078("enabled", true)));
                                 }
                              } else {
                                 Log.error("Invalid vanilla goal override '" + var10 + "' for entity '" + var1 + "'. File: " + var2.DJ());
                              }
                           }
                        }
                     }
                  }

                  for (Entry var15 : parseSection(var3).entrySet()) {
                     String var16 = (String)var15.getKey();
                     ConfigSectionWrapper var17 = (ConfigSectionWrapper)var15.getValue();
                     if (var17 != null && var17.method7078("enabled", true)) {
                        Optional var18 = Zq.entrySet().stream().filter(var1x -> var16.startsWith((String)var1x.getKey())).findFirst();
                        if (var18.isEmpty()) {
                           Log.warn("Unknown goal '" + var16 + "' in entity '" + var1 + "'. File: " + var2.DJ());
                        } else {
                           EntityMatcherBase var19 = ((MatcherLoader)((Entry)var18.get()).getValue()).load(var1, this.Zs, var17, var16, var2);
                           if (var19 != null) {
                              if (var17.ke("conditions")) {
                                 try {
                                    this.Zu.put(var19, MobFilterPredicate.fromSection(var17.bLU));
                                 } catch (IllegalArgumentException var13) {
                                    Log.error("Invalid conditions in '" + var16 + "' for entity '" + var1 + "': " + var13.getMessage() + ". File: " + var2.DJ());
                                    continue;
                                 }
                              }

                              this.Zt.add(var19);
                              if (var19 instanceof EntityMatcherRootP var20 && var20.ZY.equals("move_towards_restriction") && var20.aaa > 0.0F) {
                                 this.Zx = (int)var20.aaa;
                              }
                           }
                        }
                     }
                  }
               }
            } else {
               Log.error("Unknown goal preset '" + var3.kf("preset") + "' for entity '" + var1 + "'. File: " + var2.DJ());
            }
         }
      }
   }

   static Map parseSection(ConfigSectionWrapper var0) {
      String var1 = var0.jO("preset");
      LinkedHashMap var2 = new LinkedHashMap();
      if (var1 != null) {
         new TreeMap((Map)Zr.get(var1)).forEach(var2::put);
      }

      for (String var4 : var0.abO()) {
         if (!var4.equals("preset") && !var4.equals("clear_all") && !var4.equals("vanilla")) {
            var2.putIfAbsent(var4, Map.of());
         }
      }

      LinkedHashMap var5 = new LinkedHashMap();
      var2.forEach((var2x, var3) -> {
         ConfigSectionWrapper var4x = var0.kc(var2x);
         var5.put(var2x, var3.isEmpty() ? var4x : method9536(var3, var4x));
      });
      return var5;
   }

   private static ConfigSectionWrapper method9536(Map var0, @Nullable ConfigSectionWrapper var1) {
      ConfigurationSection var2 = new MemoryConfiguration().createSection("goal", var0);
      if (var1 != null) {
         method9537(var2, var1.bLU);
      }

      return new ConfigSectionWrapper(var2);
   }

   private static void method9537(ConfigurationSection var0, ConfigurationSection var1) {
      for (String var3 : var1.getKeys(false)) {
         Object var4 = var1.get(var3);
         if (var4 instanceof ConfigurationSection var5) {
            ConfigurationSection var6 = var0.getConfigurationSection(var3);
            method9537(var6 == null ? var0.createSection(var3) : var6, var5);
         } else {
            var0.set(var3, var4);
         }
      }
   }

   public void applyBehaviors(Mob var1) {
      if (this.Zx != null) {
         PersistentDataContainer var2 = var1.getPersistentDataContainer();
         int[] var3 = (int[])var2.get(Zo, PersistentDataType.INTEGER_ARRAY);
         String var4 = (String)var2.get(Zp, PersistentDataType.STRING);
         if (var3 == null || var3.length != 3 || var4 == null) {
            Location var5 = var1.getLocation();
            var3 = new int[]{var5.getBlockX(), var5.getBlockY(), var5.getBlockZ()};
            var4 = var5.getWorld().getUID().toString();
            var2.set(Zo, PersistentDataType.INTEGER_ARRAY, var3);
            var2.set(Zp, PersistentDataType.STRING, var4);
         }

         if (!var1.getWorld().getUID().toString().equals(var4)) {
            ProjectileTypeHelper.getAdapter().method3249(var1);
         } else {
            if (!var1.isLeashed()) {
               ProjectileTypeHelper.getAdapter().method3248(var1, new Location(var1.getWorld(), var3[0], var3[1], var3[2]), this.Zx);
            }
         }
      }
   }

   public void applyBehaviorsDup2(Mob var1) {
      if (var1 == null) {
         Log.error("Failed to apply goals to a null mob for entity '" + this.Zs.vn.getNamespacedID() + "'.");
      } else {
         MobGoalService.OV().method4595(var1);
         MobPersistentKeys.method9622(var1);
         if (!var1.getPersistentDataContainer().has(MobPersistentKeys.aaJ, PersistentDataType.STRING)
            || !var1.getPersistentDataContainer().has(MobPersistentKeys.aaF, PersistentDataType.STRING)) {
            var1.getPersistentDataContainer().set(MobPersistentKeys.aaF, PersistentDataType.STRING, this.Zs.vn.getNamespacedID());
         }

         if (this.Zw) {
            MobGoalService.OV().method4593(var1);
         }

         for (MobGoalConfig var3 : this.Zv) {
            if (MobGoalService.OV().method4605(var1, var3.Zz, var3.ZA, var3.ZB, var3.ZC) == 0) {
               Log.warn("Vanilla goal '" + var3.ZA + "' was not found on base mob '" + this.Zs.vn.getNamespacedID() + "'.");
            }
         }

         for (EntityMatcherBase var6 : this.Zt) {
            MobFilterPredicate var4 = (MobFilterPredicate)this.Zu.get(var6);
            if (var4 == null) {
               this.method9540(var1, var6);
            } else {
               MobGoalService.OV().method4596(var1, var4, () -> this.method9540(var1, var6));
            }
         }

         MobGoalEventListener.method9606(var1);
      }
   }

   private void method9540(Mob var1, EntityMatcherBase var2) {
      switch (var2) {
         case EntityMatcherRootC var5:
            MobGoalService.OV().method4594(var1, var2.YQ, var2.YP, var5.Zb.zq(), var2x -> AbstractMobGoal.method4568(var1, var5.Zb, var2x));
            break;
         case SizedEntityTypeMatcherB var6:
            MobGoalService.OV()
               .method4597(var1, var2.YQ, var2.YP, var6.YR, var6.ZF, var6.ZG, var6.ZH, var6.YS == null ? null : var2x -> var6.YS.test(var1, var2x));
            break;
         case EntityMatcherRootF var7:
            MobGoalService.OV().method4604(var1, var2.YQ, var2.YP, var7.Ze, var7.Zf, var1x -> {
               for (ItemMatcher var3 : var7.Zd) {
                  if (var3.matches(var1x)) {
                     return true;
                  }
               }

               return false;
            });
            break;
         case EntityMatcherRootP var8:
            MobGoalService.OV().method4606(var1, var2.YQ, var2.YP, var8.ZY, var8.field135, var8.aaa, var8.aab, var8.aac);
            break;
         case EntityTypeMatcherB var9:
            MobGoalService.OV()
               .method4600(var1, var2.YQ, var2.YP, var9.YR, var9.YV, var9.YW, var9.YX, var9.YS == null ? null : var2x -> var9.YS.test(var1, var2x));
            break;
         case EntityMatcherRootK var10:
            MobGoalService.OV().method4598(var1, var10.YQ, var10.YP);
            break;
         case EntityMatcherRootN var11:
            MobGoalService.OV().method4599(var1, var11.YQ, var11.YP, var11.ZS, var11.ZT);
            break;
         case EntityMatcherRootM var12:
            MobGoalService.OV().method4614(var1, var12.YP, var12.ZQ, var12.ZR);
            break;
         case EntityMatcherRootL var13:
            MobGoalService.OV().method4615(var1, var13.YP, var13.ZO, var13.ZP);
            break;
         case EntityMatcherRootH var14:
            MobGoalService.OV().method4601(var1, var14.YQ, var14.YP, var14.ZI, var14.ZJ);
            break;
         case EntityMatcherRootO var15:
            MobGoalService.OV().method4602(var1, var15.YQ, var15.YP, var15.ZV, var15.ZW, var15.ZX);
            break;
         case SizedEntityTypeMatcher var16:
            MobGoalService.OV()
               .method4603(
                  var1, var16.YQ, var16.YP, var16.YR, var16.field136, var16.YZ, var16.Za, var16.YS == null ? null : var2x -> var16.YS.test(var1, var2x)
               );
            break;
         case EntityMatcherRootG var17:
            MobGoalService.OV().method4607(var1, var17.YQ, var17.YP, var17.ZD, var17.ZE);
            break;
         case EntityMatcherRoot var18:
            MobGoalService.OV().method4608(var1, var18.YQ, var18.YP);
            break;
         case EntityMatcherRootE var19:
            MobGoalService.OV().method4609(var1, var19.YQ, var19.YP);
            break;
         case EntityMatcherRootI var20:
            MobGoalService.OV().method4610(var1, var20.YQ, var20.YP, var20.ZK, var20.ZL, var20.ZM);
            break;
         case EntityMatcherRootJ var21:
            MobGoalService.OV().method4611(var1, var21.YQ, var21.YP, var21.ZN);
            break;
         case EntityMatcherRootB var22:
            MobGoalService.OV().method4612(var1, var22.YQ, var22.YP);
            break;
         case EntityMatcherRootD var23:
            MobGoalService.OV().method4613(var1, var23.YQ, var23.YP, var23.Zc);
            break;
         default:
            Log.error("Unknown goal type '" + var2.getClass().getSimpleName() + "' for entity '" + this.Zs.vn.getNamespacedID() + "'.");
      }
   }

   @Nullable
   private static Predicate method9541(List var0, String var1, String var2, YamlConfig var3) {
      if (var0.isEmpty()) {
         Log.error("Failed to register action '" + var1 + "' for entity '" + var2 + "'. No blocks specified. File: " + var3.DJ());
         return null;
      }

      ArrayList var4 = new ArrayList();

      for (String var6 : var0) {
         var6 = var6.toLowerCase();
         if (ContentManager.method8084().method8158(var6)) {
            var4.add(new NamespacedBlockMatcher(var6));
         } else {
            try {
               BlockData var7 = Bukkit.createBlockData(var6);
               var4.add(new BlockDataMatcher(var7));
            } catch (IllegalArgumentException var8) {
               Log.error("Unknown block '" + var6 + "' for action '" + var1 + "' for entity '" + var2 + "'. File: " + var3.DJ());
            }
         }
      }

      if (var4.isEmpty()) {
         Log.error("Failed to register action '" + var1 + "' for entity '" + var2 + "'. No valid blocks specified. File: " + var3.DJ());
         return null;
      } else {
         return var1x -> {
            if (var1x == null) {
               return false;
            }

            for (BlockStateMatcher var3x : var4) {
               if (var3x.matches(var1x)) {
                  return true;
               }
            }

            return false;
         };
      }
   }

   private static @Nullable LivingEntityPredicate method9542(
      ConfigSectionWrapper var0, String var1, String var2, YamlConfig var3, @Nullable CustomMobSettings var4, @Nullable Boolean var5
   ) {
      float var6 = var0.method7102("max_health", Float.MAX_VALUE);
      float var7 = var0.method7102("min_health", 0.0F);
      Float var8 = var0.method7101("health_below", (Float)null);
      Float var9 = var0.method7101("health_above", (Float)null);
      if (Float.isFinite(var7) && Float.isFinite(var6) && (var8 == null || Float.isFinite(var8)) && (var9 == null || Float.isFinite(var9)) && !(var7 > var6)) {
         boolean var10 = var0.method7078("equipment.match_all", true);
         HashMap var11 = new HashMap();

         for (String var13 : var0.kN("equipment")) {
            if (!var13.equals("match_all")) {
               String var14 = var0.jO("equipment." + var13);
               if (var14 != null && !var14.isEmpty()) {
                  EquipmentSlot var15 = BukkitRegistryLookup.equipmentSlot(var13);
                  if (var15 == null) {
                     Log.error("Failed to register action '" + var1 + "' for entity '" + var2 + "'. Invalid equipment slot '" + var13 + "'. File: " + var3.DJ());
                     return null;
                  }

                  if (ContentManager.method8084().method8155(var14)) {
                     var11.put(var15, new NamespacedItemMatcher(var14));
                  } else {
                     Material var16 = BukkitRegistryLookup.material(var14);
                     if (var16 == null) {
                        Log.error(
                           "Failed to register action '"
                              + var1
                              + "' for entity '"
                              + var2
                              + "'. Invalid item '"
                              + var14
                              + "' for equipment slot '"
                              + var13
                              + "'. File: "
                              + var3.DJ()
                        );
                        return null;
                     }

                     var11.put(var15, new MaterialItemMatcher(var16));
                  }
               }
            }
         }

         return (var8x, var9x) -> {
            if (var4 != null) {
               CustomEntityImpl var10x = CustomEntityManager.getInstance().getByEntity(var9x);
               if (var10x == null || var10x.qK != var4) {
                  return false;
               }
            }

            double var16x = var9x.getHealth();
            if (var16x < var7 || var16x > var6 || var8 != null && var16x >= var8.floatValue() || var9 != null && var16x <= var9.floatValue()) {
               return false;
            }

            if (var5 != null && var5 && !ProjectileTypeHelper.getAdapter().method3278(var8x, var9x)) {
               return false;
            }

            if (!var11.isEmpty()) {
               for (Entry var13x : var11.entrySet()) {
                  ItemMatcher var14x = (ItemMatcher)var13x.getValue();
                  boolean var15x = var9x.getEquipment() != null && var14x.matches(var9x.getEquipment().getItem((EquipmentSlot)var13x.getKey()));
                  if (var10) {
                     if (!var15x) {
                        return false;
                     }
                  } else if (var15x) {
                     return true;
                  }
               }
            }

            return var10;
         };
      } else {
         Log.error("Invalid health range in '" + var1 + "' for entity '" + var2 + "'. File: " + var3.DJ());
         return null;
      }
   }

   @Nullable
   static EntityPredicateRule method9543(ConfigSectionWrapper var0, String var1, String var2, YamlConfig var3) {
      ConfigSectionWrapper var4 = var0.kc("selector");
      if (var4 != null && var4.hA("groups")) {
         ConfigSectionWrapper var19 = var4.kc("groups");
         if (var19 != null && !var19.abO().isEmpty()) {
            ArrayList var20 = new ArrayList();

            for (String var22 : var19.abO()) {
               ConfigSectionWrapper var23 = var19.kc(var22);
               if (var23 == null || var23.jZ("entities").isEmpty()) {
                  return null;
               }

               for (String var11 : var23.abO()) {
                  if (!var11.equals("entities") && !var11.equals("rules")) {
                     Log.error("Unknown selector group field '" + var11 + "' in '" + var22 + "' for entity '" + var2 + "'. File: " + var3.DJ());
                     return null;
                  }
               }

               ConfigSectionWrapper var24 = var23.kc("rules");
               if (var24 == null) {
                  var24 = new ConfigSectionWrapper(new MemoryConfiguration().createSection("rules"));
               }

               for (String var12 : var24.abO()) {
                  if (!Set.of("min_health", "max_health", "health_below", "health_above", "equipment", "must_be_angry_at").contains(var12)) {
                     Log.error("Unknown selector rule '" + var12 + "' in group '" + var22 + "' for entity '" + var2 + "'. File: " + var3.DJ());
                     return null;
                  }
               }

               ArrayList var26 = new ArrayList();

               for (String var13 : var23.jZ("entities")) {
                  EntityType var14 = var13 == null ? null : BukkitRegistryLookup.entityType(var13);
                  CustomMobSettings var15 = var14 == null ? (CustomMobSettings)CustomEntityManager.getInstance().oN.get(var13) : null;
                  if (var14 == null && var15 == null) {
                     Log.error("Invalid entity '" + var13 + "' in selector group '" + var22 + "' for entity '" + var2 + "'. File: " + var3.DJ());
                     return null;
                  }

                  EntityType var16 = var14 == null ? var15.vw : var14;
                  Boolean var17 = var1.startsWith("attack_near") ? var24.method7104("must_be_angry_at", null) : null;
                  LivingEntityPredicate var18 = method9542(var24, var1, var2, var3, var15, var17);
                  if (var18 == null) {
                     return null;
                  }

                  var26.add((var2x, var3x) -> var3x.getType() == var16 && var18.test(var2x, var3x));
               }

               var20.add((var1x, var2x) -> {
                  for (LivingEntityPredicate var4x : var26) {
                     if (var4x.test(var1x, var2x)) {
                        return true;
                     }
                  }

                  return false;
               });
            }

            return new EntityPredicateRule(null, (var1x, var2x) -> {
               for (LivingEntityPredicate var4x : var20) {
                  if (var4x.test(var1x, var2x)) {
                     return true;
                  }
               }

               return false;
            });
         } else {
            return null;
         }
      } else {
         String var5 = var0.jO("entity");
         EntityType var6 = var5 == null ? null : BukkitRegistryLookup.entityType(var5);
         CustomMobSettings var7;
         if (var6 == null) {
            if (var5 == null || !CustomEntityManager.getInstance().oN.containsKey(var5)) {
               return null;
            }

            var7 = (CustomMobSettings)CustomEntityManager.getInstance().oN.get(var5);
            var6 = var7.vw;
         } else {
            var7 = null;
         }

         Boolean var8 = null;
         if (var1.equals("attack_near")) {
            var8 = var0.method7104("must_be_angry_at", null);
         }

         LivingEntityPredicate var9 = null;
         if (!var0.hA("selector") && var7 != null) {
            var9 = (var1x, var2x) -> {
               CustomEntityImpl var3x = CustomEntityManager.getInstance().getByEntity(var2x);
               return var3x != null && var3x.qK == var7;
            };
         } else {
            if (var4 != null) {
               var9 = method9542(var4, var1, var2, var3, var7, var8);
            }

            if (var4 != null && var9 == null) {
               return null;
            }
         }

         return new EntityPredicateRule(var6, var9);
      }
   }

   static EntityTypePairRule[] method9544(ConfigSectionWrapper var0, String var1, String var2, YamlConfig var3, String var4) {
      ArrayList var5 = new ArrayList();
      ConfigSectionWrapper var6 = var0.kc(var4);
      if (var6 != null) {
         for (String var8 : var6.abO()) {
            ConfigSectionWrapper var9 = var6.kc(var8);
            assert var9 != null;
            EntityPredicateRule var10 = method9543(var9, var1, var2, var3);
            if (var10 == null) {
               Log.error("Invalid selector in '" + var1 + "' for entity '" + var2 + "'. File: " + var3.DJ());
            } else {
               var5.add(EntityTypePairRule.yc().method9578(var10.entityType()).method9579(var10.predicate()).yd());
            }
         }
      }

      return var5.toArray(new EntityTypePairRule[0]);
   }

   static {
      registerMatcher(
         "look_at_entity",
         (var0, var1x, var2, var3x, var4) -> {
            EntityPredicateRule var5 = method9543(var2, var3x, var0, var4);
            if (var5 == null) {
               Log.error("Failed to register action '" + var3x + "' for entity '" + var0 + "'. Invalid entity type or selector. File: " + var4.DJ());
               return null;
            } else {
               return ((SizedEntityMatcherBuilderB)((SizedEntityMatcherBuilderB)((SizedEntityMatcherBuilderB)((SizedEntityMatcherBuilderB)SizedEntityTypeMatcherB.yh()
                              .withFlag(var2.method7078("replace_vanilla", true)))
                           .withPriority(var2.method7076("priority", 10)))
                        .method9511(var5.entityType()))
                     .method9521(var2.method7102("range", 8.0F))
                     .method9522(var2.method7102("chance", 20.0F) / 100.0F)
                     .ay(var2.method7078("look_forward", false))
                     .method9512(var5.predicate()))
                  .yj();
            }
         }
      );
      registerMatcher(
         "random_look_around",
         (var0, var1x, var2, var3x, var4) -> ((MatcherNodeK)((MatcherNodeK)EntityMatcherRootK.getNode().withFlag(var2.method7078("replace_vanilla", true)))
               .withPriority(var2.method7076("priority", 10)))
            .getRoot()
      );
      registerMatcher(
         "random_wander_around",
         (var0, var1x, var2, var3x, var4) -> ((MatcherNodeN)((MatcherNodeN)EntityMatcherRootN.getNode().withFlag(var2.method7078("replace_vanilla", true)))
               .withPriority(var2.method7076("priority", 10)))
            .withDistance(var2.method7102("speed_modifier", 1.0F))
            .withCount(var2.method7076("interval", 10))
            .withEnabled(var2.method7078("check_no_action_time", true))
            .getRoot()
      );
      registerMatcher(
         "follow_held_item",
         (var0, var1x, var2, var3x, var4) -> {
            ArrayList var5 = new ArrayList();

            for (String var7 : var2.jZ("items")) {
               if (!ContentManager.method8084().method8155(var7)) {
                  Material var8 = BukkitRegistryLookup.material(var7);
                  if (var8 == null || var8.isAir()) {
                     Log.error("Invalid item '" + var7 + "' in '" + var3x + "' for entity '" + var0 + "'. File: " + var4.DJ());
                     return null;
                  }

                  var5.add(new MaterialItemMatcher(var8));
               } else {
                  var5.add(new NamespacedItemMatcher(var7));
               }
            }

            if (var5.isEmpty()) {
               Log.error("No items in '" + var3x + "' for entity '" + var0 + "'. File: " + var4.DJ());
               return null;
            }

            double var9 = var2.method7080("speed_modifier", 1.0);
            if (Double.isFinite(var9) && !(var9 <= 0.0)) {
               return ((MatcherNodeF)((MatcherNodeF)EntityMatcherRootF.getNode().withFlag(var2.method7078("replace_vanilla", true)))
                     .withPriority(var2.method7076("priority", 10)))
                  .withItems(var5)
                  .withDistance(var9)
                  .withEnabled(var2.method7078("can_scare", false))
                  .getRoot();
            }

            Log.error("Invalid speed_modifier in '" + var3x + "' for entity '" + var0 + "'. File: " + var4.DJ());
            return null;
         }
      );

      for (String var1 : List.of(
         "panic",
         "flee_sun",
         "water_avoiding_wander",
         "random_swim",
         "move_towards_restriction",
         "move_towards_target",
         "leap_at_target",
         "follow_mob",
         "ranged_attack",
         "breed",
         "follow_parent",
         "follow_owner",
         "seek_water",
         "seek_air",
         "sit_when_ordered"
      )) {
         registerMatcher(
            var1,
            (var1x, var2, var3x, var4, var5) -> {
               double var6 = var3x.method7080("speed_modifier", 1.0);

               float var8 = switch (var1) {
                  case "water_avoiding_wander" -> var3x.method7102("water_probability", 0.001F);
                  case "move_towards_target" -> var3x.method7102("range", 32.0F);
                  case "ranged_attack" -> var3x.method7102("attack_radius", 15.0F);
                  case "leap_at_target" -> var3x.method7102("strength", 0.4F);
                  case "follow_mob" -> var3x.method7102("stop_distance", 3.0F);
                  case "follow_owner" -> var3x.method7102("start_distance", 10.0F);
                  case "move_towards_restriction" -> var3x.method7102("home_radius", 0.0F);
                  default -> 0.0F;
               };

               float var12 = switch (var1) {
                  case "follow_mob" -> var3x.method7102("area_size", 7.0F);
                  case "follow_owner" -> var3x.method7102("stop_distance", 2.0F);
                  default -> 0.0F;
               };
               int var14 = var3x.method7076("interval", var1.equals("ranged_attack") ? 20 : 80);
               if (Double.isFinite(var6)
                  && !(var6 <= 0.0)
                  && Float.isFinite(var8)
                  && !(var8 < 0.0F)
                  && Float.isFinite(var12)
                  && !(var12 < 0.0F)
                  && var14 > 0
                  && (!var1.equals("water_avoiding_wander") || !(var8 > 1.0F))
                  && (!var1.equals("move_towards_restriction") || !var3x.ke("home_radius") || !(var8 <= 0.0F) && var8 == (int)var8)
                  && (!var1.equals("follow_owner") || !(var8 <= var12))) {
                  return ((MatcherNodeP)((MatcherNodeP)EntityMatcherRootP.getNode().withFlag(var3x.method7078("replace_vanilla", true)))
                        .withPriority(var3x.method7076("priority", 10)))
                     .withName(var1)
                     .withDistance(var6)
                     .withMinScale(var8)
                     .withMaxScale(var12)
                     .withCount(var14)
                     .getRoot();
               }

               Log.error("Invalid parameters in '" + var4 + "' for entity '" + var1x + "'. File: " + var5.DJ());
               return null;
            }
         );
      }

      for (String var3 : MobGoalSettings.zl()) {
         registerMatcher(
            var3,
            (var1x, var2, var3x, var4, var5) -> {
               try {
                  if (var3x.method7073("priority", 10) instanceof Number var7
                     && Double.isFinite(var7.doubleValue())
                     && var7.doubleValue() == Math.rint(var7.doubleValue())
                     && !(var7.doubleValue() < 0.0)
                     && !(var7.doubleValue() > 2.147483647E9)) {
                     for (String var9 : List.of("enabled", "replace_vanilla")) {
                        if (var3x.ke(var9) && !(var3x.kf(var9) instanceof Boolean)) {
                           throw new IllegalArgumentException(var9 + " must be true or false");
                        }
                     }

                     return ((MatcherNodeC)((MatcherNodeC)EntityMatcherRootC.getNode().withPriority(var7.intValue()))
                           .withFlag(var3x.method7078("replace_vanilla", true)))
                        .withSettings(MobGoalSettings.parse(var3, var3x.bLU, var1x))
                        .getRoot();
                  } else {
                     throw new IllegalArgumentException("priority must be a non-negative integer");
                  }
               } catch (IllegalArgumentException var10) {
                  Log.error("Invalid behaviour '" + var4 + "' for entity '" + var1x + "': " + var10.getMessage() + ". File: " + var5.DJ());
                  return null;
               }
            }
         );
      }

      registerMatcher(
         "random_teleport_timed",
         (var0, var1x, var2, var3x, var4) -> ((MatcherNodeM)EntityMatcherRootM.getNode().withPriority(var2.method7076("priority", 10)))
            .withMin(var2.method7076("min_delay", 10))
            .withMax(var2.method7076("max_delay", 20))
            .getRoot()
      );
      registerMatcher(
         "random_teleport_on_hit",
         (var0, var1x, var2, var3x, var4) -> ((MatcherNodeL)EntityMatcherRootL.getNode().withPriority(var2.method7076("priority", 10)))
            .withEnabled(var2.method7078("check_visibility", true))
            .withScale(var2.method7102("chance", 100.0F) / 100.0F)
            .getRoot()
      );
      registerMatcher(
         "attack_near",
         (var0, var1x, var2, var3x, var4) -> {
            EntityPredicateRule var5 = method9543(var2, var3x, var0, var4);
            if (var5 == null) {
               Log.error("Failed to register action '" + var3x + "' for entity '" + var0 + "'. Invalid entity type or selector. File: " + var4.DJ());
               return null;
            } else {
               return ((EntityMatcherBuilderWithFlags)((EntityMatcherBuilderWithFlags)((EntityMatcherBuilderWithFlags)((EntityMatcherBuilderWithFlags)EntityTypeMatcherB.getBuilder()
                              .withFlag(var2.method7078("replace_vanilla", true)))
                           .withPriority(var2.method7076("priority", 10)))
                        .method9511(var5.entityType()))
                     .be(var2.method7076("interval", 10))
                     .av(var2.method7078("check_visibility", true))
                     .aw(var2.method7078("check_can_navigate", true))
                     .method9512(var5.predicate()))
                  .xr();
            }
         }
      );
      registerMatcher(
         "melee_attack",
         (var0, var1x, var2, var3x, var4) -> ((MatcherNodeH)((MatcherNodeH)EntityMatcherRootH.getNode().withFlag(var2.method7078("replace_vanilla", true)))
               .withPriority(var2.method7076("priority", 10)))
            .withRange(var2.method7076("speed_modifier", 1))
            .withEnabled(var2.method7078("pause_when_idle", false))
            .getRoot()
      );
      registerMatcher(
         "ranged_bow_attack",
         (var0, var1x, var2, var3x, var4) -> {
            if (!EntityMatcherRootO.matchesType(var1x.vw)) {
               Log.warn(
                  "Custom entity entity '" + var0 + "' is not compatible with RangedBowAttackGoal. This goal will not work as expected. File: " + var4.DJ()
               );
               Log.warn("Compatible types: " + Arrays.toString(EntityMatcherRootO.getTypes()));
               return null;
            } else {
               return ((MatcherNodeO)((MatcherNodeO)EntityMatcherRootO.getNode().withFlag(var2.method7078("replace_vanilla", true)))
                     .withPriority(var2.method7076("priority", 10)))
                  .withRange(var2.method7102("speed_modifier", 1.0F))
                  .withMin(var2.method7076("attack_interval_min", 20))
                  .withScale(var2.method7102("attack_radius", 15.0F))
                  .getRoot();
            }
         }
      );
      registerMatcher(
         "avoid_entity",
         (var0, var1x, var2, var3x, var4) -> {
            EntityPredicateRule var5 = method9543(var2, var3x, var0, var4);
            if (var5 == null) {
               Log.error("Failed to register action '" + var3x + "' for entity '" + var0 + "'. Invalid entity type or selector. File: " + var4.DJ());
               return null;
            } else {
               return ((SizedEntityMatcherBuilder)((SizedEntityMatcherBuilder)((SizedEntityMatcherBuilder)((SizedEntityMatcherBuilder)SizedEntityTypeMatcher.getBuilder()
                              .withFlag(var2.method7078("replace_vanilla", true)))
                           .withPriority(var2.method7076("priority", 10)))
                        .method9511(var5.entityType()))
                     .withScale(var2.method7102("max_distance", 12.0F))
                     .withMinWidth(var2.method7102("walk_speed_modifier", 1.0F))
                     .withMinHeight(var2.method7102("sprint_speed_modifier", 1.2F))
                     .method9512(var5.predicate()))
                  .buildTypeMatcher();
            }
         }
      );
      registerMatcher(
         "hurt_by_target",
         (var0, var1x, var2, var3x, var4) -> {
            EntityTypePairRule[] var5 = method9544(var2, var3x, var0, var4, "ignore_damage_from");
            EntityTypePairRule[] var6 = method9544(var2, var3x, var0, var4, "alert_others");
            return ((MatcherNodeG)((MatcherNodeG)EntityMatcherRootG.getNode().withFlag(var2.method7078("replace_vanilla", true)))
                  .withPriority(var2.method7076("priority", 10)))
               .withPairs(var5)
               .withPairsDup2(var6)
               .getRoot();
         }
      );
      registerMatcher(
         "avoid_sun",
         (var0, var1x, var2, var3x, var4) -> ((MatcherNode)((MatcherNode)EntityMatcherRoot.method9500().withFlag(var2.method7078("replace_vanilla", true)))
               .withPriority(var2.method7076("priority", 10)))
            .xz()
      );
      registerMatcher(
         "float",
         (var0, var1x, var2, var3x, var4) -> ((MatcherNodeE)((MatcherNodeE)EntityMatcherRootE.getNode().withFlag(var2.method7078("replace_vanilla", true)))
               .withPriority(var2.method7076("priority", 10)))
            .getRoot()
      );
      registerMatcher(
         "move_to_block",
         (var0, var1x, var2, var3x, var4) -> {
            List var5 = var2.jZ("blocks");
            Predicate var6 = method9541(var5, var3x, var0, var4);
            return var6 == null
               ? null
               : ((MatcherNodeI)((MatcherNodeI)EntityMatcherRootI.getNode().withFlag(var2.method7078("replace_vanilla", true)))
                     .withPriority(var2.method7076("priority", 10)))
                  .withPredicate(var6)
                  .withDistance(var2.method7102("speed_modifier", 1.0F))
                  .withCount(var2.method7076("search_range", 16))
                  .getRoot();
         }
      );
      registerMatcher(
         "open_door",
         (var0, var1x, var2, var3x, var4) -> ((MatcherNodeJ)((MatcherNodeJ)EntityMatcherRootJ.getNode().withFlag(var2.method7078("replace_vanilla", true)))
               .withPriority(var2.method7076("priority", 10)))
            .withEnabled(var2.method7078("close", true))
            .getRoot()
      );
      registerMatcher(
         "break_door",
         (var0, var1x, var2, var3x, var4) -> ((MatcherNodeB)((MatcherNodeB)EntityMatcherRootB.xB().withFlag(var2.method7078("replace_vanilla", true)))
               .withPriority(var2.method7076("priority", 10)))
            .xD()
      );
      registerMatcher(
         "eat_block",
         (var0, var1x, var2, var3x, var4) -> {
            List var5 = var2.jZ("blocks");
            Predicate var6 = method9541(var5, var3x, var0, var4);
            return var6 == null
               ? null
               : ((MatcherNodeD)((MatcherNodeD)EntityMatcherRootD.getNode().withFlag(var2.method7078("replace_vanilla", true)))
                     .withPriority(var2.method7076("priority", 10)))
                  .withPredicate(var6)
                  .getRoot();
         }
      );
   }
}
