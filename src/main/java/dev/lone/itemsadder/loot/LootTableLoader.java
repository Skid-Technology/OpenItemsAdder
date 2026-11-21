package dev.lone.itemsadder.loot;

import beer.devs.fastnbt.nms.Version;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import dev.lone.itemsadder.block.BlockInteractionKind;
import dev.lone.itemsadder.block.BlockSoundProfile;
import dev.lone.itemsadder.block.BlockSoundProfileBuilder;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.NamedFlags;
import dev.lone.itemsadder.config.PatternMatcher;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.EffectPlaybackSet;
import dev.lone.itemsadder.item.ItemConfigResolver;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.item.ItemSpecWithTexture;
import dev.lone.itemsadder.item.ItemStackPairRecord;
import dev.lone.itemsadder.item.ItemVariantHolder;
import dev.lone.itemsadder.item.TimedStep;
import dev.lone.itemsadder.util.FloatVector3Base;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NullableFloatVector3;
import dev.lone.itemsadder.util.StringListMatcher;
import dev.lone.itemsadder.world.ChunkBlockHookHolder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class LootTableLoader {
   public static final String arL = "minecraft:block/crop";
   static List arM = new ArrayList();
   static List arN = new ArrayList();

   public static void cD() {
      for (YamlConfig var1 : ContentManager.method8084().method8092()) {
         method10058(var1);
      }
   }

   public static void FR() {
      arM.clear();
      arN.clear();
   }

   public static List FS() {
      ArrayList var0 = new ArrayList(arM);
      arM.clear();
      return var0;
   }

   private static void method10058(YamlConfig var0) {
      ConfigSectionWrapper var1 = var0.kc("crops");
      if (var1 != null) {
         String var2 = var0.getString("info.namespace");
         if (var2 == null) {
            throw new IllegalArgumentException("[Items] Missing 'info.namespace'. File: " + var0.DJ());
         }

         Set var3 = var1.bJ(false);
         if (!var3.isEmpty() && !Version.isAtLeast(Version.v1_21_4)) {
            Log.error("[Items] Crops are only supported on Minecraft 1.21.4 or higher! Skipping loading crops. File: " + var0.DJ());
         } else {
            for (String var5 : var3) {
               try {
                  ConfigSectionWrapper var6 = var1.kc(var5);
                  assert var6 != null;
                  BlockSoundProfileBuilder var7 = BlockSoundProfile.builder();
                  var7.withNamespacedId(var2 + ":" + var5);
                  CustomItemDefinition var8 = ContentManager.method8084().method8154(var6.jO("seed"));
                  if (var8 == null) {
                     throw new IllegalArgumentException("'" + var2 + ":" + var5 + "'. Error: Missing or invalid 'seed' entry. File: " + var0.DJ());
                  }

                  var7.withSeed(var8)
                     .withDropSeedIfImmature(var6.method7078("drop_seed_if_immature", true))
                     .withAvgSecondsPerStage(var6.method7080("avg_seconds_per_stage", Settings.get().cropAvgSecondsPerStage))
                     .withMinLight(var6.method7076("min_light", 9))
                     .withRequiredSpacing(var6.hA("required_spacing") ? var6.method7076("required_spacing", 1) : null)
                     .withBonemealGrowthMultiplier((float)var6.method7080("multipliers.bonemeal", 1.5))
                     .withMaxGrowthMoistureMultiplier(var6.method7080("multipliers.max_moisture", 0.5))
                     .withRainGrowthMultiplier((float)var6.method7080("multipliers.rain", 1.5))
                     .withThunderGrowthMultiplier((float)var6.method7080("multipliers.thunder", -1.0))
                     .withSnowGrowthMultiplier((float)var6.method7080("multipliers.snow", -1.5));
                  ConfigSectionWrapper var9 = var6.kc("default_hitbox");
                  if (var9 != null) {
                     float var10 = (float)var9.method7080("width", 1.0);
                     float var11 = (float)var9.method7080("length", 1.0);
                     float var12 = (float)var9.method7080("height", 1.0);
                     var7.withHitbox(new FloatVector3Base(var10, var12, var11));
                  } else {
                     var7.withHitbox(new FloatVector3Base(1.0F, 1.0F, 1.0F));
                  }

                  HashMap var30 = new HashMap();
                  int var31 = var6.method7076("max_age", 7);
                  HashMap var32 = new HashMap();
                  HashMap var13 = new HashMap();
                  if (var6.hA("models_by_items") && var6.hA("models_by_textures")) {
                     throw new IllegalArgumentException(
                        "Crop '"
                           + var2
                           + ":"
                           + var5
                           + "'. Warning: Contains both 'models_by_items' and 'models_by_textures' sections. Only one is allowed. File: "
                           + var0.DJ()
                     );
                  }

                  if (var6.hA("models_by_items")) {
                     String var14 = var6.jO("models_by_items.items_id_prefix");
                     if (var14 == null) {
                        throw new IllegalArgumentException(
                           "Crop '" + var2 + ":" + var5 + "'. Error: Missing 'models_by_items.items_id_prefix' entry. File: " + var0.DJ()
                        );
                     }

                     for (byte var15 = 0; var15 <= var31; var15++) {
                        String var16 = var14 + var15;
                        CustomItemDefinition var17 = ContentManager.method8084().method8154(var16);
                        if (var17 == null) {
                           throw new IllegalArgumentException(
                              "Crop '"
                                 + var2
                                 + ":"
                                 + var5
                                 + "'. Error: Unknown item '"
                                 + var16
                                 + "' generated from 'models_by_items.items_id_prefix'. File: "
                                 + var0.DJ()
                           );
                        }

                        var32.put(var15, var17.Ds());
                        arN.add(var17);
                        if (Settings.get().cropBillboard) {
                           String var18 = var14 + var15 + "_billboard";
                           CustomItemDefinition var19 = ContentManager.method8084().method8154(var18);
                           if (var19 != null) {
                              var13.put(var15, var19.Ds());
                              arN.add(var19);
                           }
                        }
                     }
                  } else {
                     if (!var6.hA("models_by_textures")) {
                        throw new IllegalArgumentException("'" + var2 + ":" + var5 + "'. Error: Missing 'stages.id_prefix' entry. File: " + var0.DJ());
                     }

                     String var33 = var6.bf("models_by_textures.parent", "minecraft:block/crop");

                     try {
                        var33 = ItemConfigResolver.method10110(var33, var2);
                     } catch (IllegalArgumentException var28) {
                        throw new IllegalArgumentException(
                           "[Crops] '" + var2 + ":" + var5 + "'. Error: Invalid 'parent' '" + var33 + "'." + var28.getMessage() + ". File: " + var0.DJ()
                        );
                     }

                     if (var33.equalsIgnoreCase("minecraft:block/crop")) {
                        var7.withUsesGameCropModel(true);
                     }

                     String var35 = var6.bf("models_by_textures.model_texture_key", "crop");
                     String var37 = var6.jO("models_by_textures.textures_prefix");
                     if (var37 == null) {
                        throw new IllegalArgumentException(
                           "'" + var2 + ":" + var5 + "'. Error: Missing 'models_by_textures.textures_prefix' entry. File: " + var0.DJ()
                        );
                     }

                     if (!var37.contains(":")) {
                        var37 = var2 + ":" + var37;
                     }

                     if (var37.endsWith(".png")) {
                        var37 = var37.substring(0, var37.length() - 4);
                     }

                     String var39 = var6.bf("models_by_textures.billboard_parent", "minecraft:block/crop_billboard");
                     String var43 = var6.bf("models_by_textures.billboard_model_texture_key", "crop");
                     String var48 = var6.jO("models_by_textures.billboard_textures_prefix");
                     if (var48 == null) {
                        var48 = var37;
                     } else {
                        if (!var48.contains(":")) {
                           var48 = var2 + ":" + var48;
                        }

                        if (var48.endsWith(".png")) {
                           var48 = var48.substring(0, var48.length() - 4);
                        }
                     }

                     for (byte var20 = 0; var20 <= var31; var20++) {
                        String var21 = var2 + ":" + var5 + "_" + var20;
                        String var22 = var2 + ":" + var5 + "_" + var20 + "_billboard";
                        ItemSpecWithTexture var23 = new ItemSpecWithTexture(
                           var21, Material.PAPER, var33, Map.of(var35, var37 + var20), null, null, null, null, null
                        );
                        arM.add(var23);
                        ItemSpecWithTexture var24 = new ItemSpecWithTexture(
                           var22, Material.PAPER, var39, Map.of(var43, var48 + var20), null, null, null, null, null
                        );
                        arM.add(var24);
                        ItemStack var25 = new ItemStack(Material.PAPER);
                        ItemOperationsService.getRegistryBridge().method3437(var25, var23.Ei());
                        var32.put(var20, var25);
                        ItemStack var26 = new ItemStack(Material.PAPER);
                        ItemOperationsService.getRegistryBridge().method3437(var26, var24.Ei());
                        var13.put(var20, var26);
                     }
                  }

                  float var34 = (float)var6.method7080("hitbox_auto_height_step", 0.0);
                  HashMap var36 = new HashMap();
                  ConfigSectionWrapper var38 = var6.kc("hitboxes");
                  if (var38 != null) {
                     for (String var44 : var38.abO()) {
                        try {
                           byte var49 = Byte.parseByte(var44);
                           ConfigSectionWrapper var52 = var38.kc(var44);
                           Float var54 = null;
                           Float var56 = null;
                           Float var58 = null;
                           if (var52 != null) {
                              if (var52.hA("width")) {
                                 var54 = (float)var52.kl("width");
                              }

                              if (var52.hA("length")) {
                                 var56 = (float)var52.kl("length");
                              }

                              if (var52.hA("height")) {
                                 var58 = (float)var52.kl("height");
                              }
                           }

                           var36.put(var49, new NullableFloatVector3(var54, var56, var58));
                        } catch (NumberFormatException var27) {
                           Log.warn("[Items] Invalid hitbox stage '" + var44 + "' in crop '" + var2 + ":" + var5 + "'");
                        }
                     }
                  }

                  if (var31 > 127) {
                     throw new IllegalArgumentException(
                        "'" + var2 + ":" + var5 + "'. Error: 'max_age' cannot be higher than 127 (current: " + var31 + "). File: " + var0.DJ()
                     );
                  }

                  for (byte var41 = 0; var41 <= var31; var41++) {
                     NullableFloatVector3 var45 = (NullableFloatVector3)var36.get(var41);
                     Float var50 = null;
                     Float var53 = null;
                     Float var55 = null;
                     if (var45 != null) {
                        var50 = var45.x;
                        var53 = var45.y;
                        var55 = var45.z;
                     } else if (var34 != 0.0F) {
                        var55 = var34 * var41;
                        if (var55 < 0.1F) {
                           var55 = 0.1F;
                        }
                     }

                     ItemStackPairRecord var57 = new ItemStackPairRecord((ItemStack)var32.get(var41), (ItemStack)var13.get(var41), var50, var53, var55);
                     var30.put(var41, var57);
                  }

                  var7.withMaxAge(var31);
                  var7.withAgeStatuses(ImmutableMap.copyOf(var30));
                  String var42 = var6.jO("bottom_block");
                  if (var42 != null) {
                     CustomBlockDefinition var46 = ContentManager.method8084().method8160(var42);
                     if (var46 != null) {
                        var7.withRequiredCustomBottomBlockId(var46.getNamespacedID());
                     } else {
                        Material var51 = ConfigSectionWrapper.method7106(var42, null);
                        if (var51 == null) {
                           throw new IllegalArgumentException("'" + var2 + ":" + var5 + "'. Error: Invalid 'bottom_block' '" + var42 + "'. File: " + var0.DJ());
                        }

                        var7.withRequireBottomBlockMaterial(var51);
                        var7.withRequiresFarmlandBottom(var51 == Material.FARMLAND);
                     }
                  }

                  var7.withPlaceSound(var6.jO("place_sound"));
                  var7.withBreakSound(var6.jO("break_sound"));
                  var7.withInteractSound(var6.jO("interact_sound"));
                  var7.withLoots(method10059(var6, var2, var5, var0));
                  BlockSoundProfile var47 = var7.build();
                  ContentManager.method8084().au.put(var47.jP, var47);
                  ContentManager.method8084().av.put(var8, var47);
               } catch (IllegalArgumentException var29) {
                  Log.warn("[Items] " + var29.getMessage());
               }
            }
         }
      }
   }

   static ImmutableMap method10059(ConfigSectionWrapper var0, String var1, String var2, YamlConfig var3) {
      HashMap var4 = new HashMap();
      ConfigSectionWrapper var5 = var0.kc("loots");
      if (var5 != null) {
         for (String var7 : var5.abO()) {
            ConfigSectionWrapper var8 = var5.kc(var7);
            assert var8 != null;
            LootDropRulesBuilder var9 = LootDropRulesImplA.dx();
            if (var8.kf("age") instanceof Integer) {
               var9.method2032(var8.method7077("age", null));
               var9.method2033(var8.method7077("age", null));
            } else {
               var9.method2032(var8.method7077("age_range.min", null));
               var9.method2033(var8.method7077("age_range.max", null));
            }

            var9.method2034(var8.method7077("decrease_age", null));
            HashSet var10 = new HashSet();

            for (String var12 : var8.jZ("biomes")) {
               NamespacedKey var13 = ChunkBlockHookHolder.getInstance().hook.parseBiomeKey(var12.toUpperCase());
               if (var13 != null) {
                  var10.add(var13);
               } else {
                  Log.warn("[Items] '" + var1 + ":" + var2 + "' has unknown biome '" + var12 + "' in loot.biomes. File: " + var3.DJ());
               }
            }

            var9.method2022(var10);
            ArrayList var26 = new ArrayList();
            if (var8.hA("tools")) {
               for (String var14 : var8.jZ("tools")) {
                  var26.add(PatternMatcher.method17(var14));
               }
            }

            if (!var26.isEmpty()) {
               var9.method2035(new StringListMatcher(var26));
            }

            ArrayList var28 = new ArrayList();
            if (var8.hA("triggers")) {
               for (String var33 : var8.jZ("triggers")) {
                  try {
                     BlockInteractionKind var15 = BlockInteractionKind.valueOf(var33.toUpperCase(Locale.ROOT));
                     var28.add(var15);
                  } catch (IllegalArgumentException var22) {
                     Log.warn("[Items] '" + var1 + ":" + var2 + "' has unknown 'trigger' '" + var33 + "'. File: " + var3.DJ());
                  }
               }
            } else {
               BlockInteractionKind var30 = (BlockInteractionKind)var8.method7097("trigger", BlockInteractionKind.class, BlockInteractionKind.BREAK);
               var28.add(var30);
            }

            var9.method2031(ImmutableList.copyOf(var28));
            LootDropRulesImplA var32 = var9.dC();
            var32.bg(var8.method7078("stop_on_first_success", var8.method7078("drop_only_first", false)));

            for (BlockInteractionKind var38 : var28) {
               var4.computeIfAbsent(var38, var0x -> new ArrayList<>()).add(var32);
            }

            var8.jZ("worlds").forEach(var32::dz);
            var32.method2011(var8);
            if (var8.jI("commands")) {
               ConfigSectionWrapper var35 = var8.kc("commands");
               assert var35 != null;

               for (String var16 : var35.abO()) {
                  ConfigSectionWrapper var17 = var35.kc(var16);
                  assert var17 != null;
                  ArrayList var18 = new ArrayList();

                  for (String var20 : var17.abO()) {
                     if (!var20.equals("chance")) {
                        ConfigSectionWrapper var21 = var17.kc(var20);
                        assert var21 != null;
                        var18.add(new NamedFlags(var21.jO("command"), var21.method7078("as_console", true), var21.method7078("silent", false)));
                     }
                  }

                  var32.method2001(new EffectPlaybackSet(var18, var17.jP("chance")));
               }
            }

            if (var8.jI("exp_drop")) {
               method10061(var8, "exp_drop", var32, var3);
            }

            if (var8.jI("exp_drops")) {
               ConfigSectionWrapper var36 = var8.kc("exp_drops");
               assert var36 != null;

               for (String var42 : var36.abO()) {
                  method10061(var36, var42, var32, var3);
               }
            }

            if (var8.jI("item_drop")) {
               method10060(var8, "item_drop", var32, var3);
            }

            if (var8.jI("item_drops")) {
               ConfigSectionWrapper var37 = var8.kc("item_drops");
               assert var37 != null;

               for (String var43 : var8.jK("item_drops")) {
                  method10060(var37, var43, var32, var3);
               }
            }
         }
      }

      Builder var23 = ImmutableMap.builder();

      for (Entry var25 : var4.entrySet()) {
         var23.put((BlockInteractionKind)var25.getKey(), ImmutableList.copyOf((Collection)var25.getValue()));
      }

      return var23.build();
   }

   static void method10060(ConfigSectionWrapper var0, String var1, LootDropRulesImplA var2, YamlConfig var3) {
      ConfigSectionWrapper var4 = var0.kc(var1);
      assert var4 != null;
      String var5 = var4.jO("item");
      ItemStack var6 = ContentManager.method8084().method8178(var5);
      if (var6 == null) {
         Log.error("Item '" + var5 + "' not found for loot '" + var1 + "'. File: " + var3.DJ());
      } else {
         ItemVariantHolder var7 = new ItemVariantHolder(
            var6,
            var4.method7076("min_amount", 1),
            var4.method7076("max_amount", 1),
            var4.method7102("chance", 100.0F),
            var4.method7078("ignore_fortune", false)
         );
         if (var7.Fr() > var7.Fs()) {
            Log.error(
               "Invalid loot. 'min_amount' "
                  + var7.Fr()
                  + " greater than 'max_amount' "
                  + var7.Fs()
                  + "! Must be lower. Loot '"
                  + var1
                  + "'. File: "
                  + var3.DJ()
            );
         } else {
            var2.method1999(var7);
         }
      }
   }

   static void method10061(ConfigSectionWrapper var0, String var1, LootDropRulesImplA var2, YamlConfig var3) {
      if (var0.kf(var1) instanceof Integer var4) {
         var2.method2000(new TimedStep(var4));
      } else {
         ConfigSectionWrapper var7 = var0.kc(var1);
         assert var7 != null;
         TimedStep var6 = new TimedStep(var7.jP("min_amount"), var7.jP("max_amount"), var7.jQ("chance"));
         if (var6.Fn() > var6.Fo()) {
            Log.error(
               "Invalid loot. 'min_amount' "
                  + var6.Fn()
                  + " greater than 'max_amount' "
                  + var6.Fo()
                  + "! Must be lower. Loot '"
                  + var1
                  + "'. File: "
                  + var3.DJ()
            );
            return;
         }

         var2.method2000(var6);
      }
   }
}
