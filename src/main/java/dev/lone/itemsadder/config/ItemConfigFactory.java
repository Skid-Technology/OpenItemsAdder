package dev.lone.itemsadder.config;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.block.AgeableBlockDefinition;
import dev.lone.itemsadder.block.BlockRealizationMode;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.block.CustomBlockStateSettings;
import dev.lone.itemsadder.block.CustomBlockVariant;
import dev.lone.itemsadder.block.ExplosiveBlockSpec;
import dev.lone.itemsadder.block.ExplosiveBlockSpecBuilder;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemKind;
import dev.lone.itemsadder.item.ToolDurabilitySpec;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.StringListMatcher;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public class ItemConfigFactory {
   public static boolean method10049(ConfigSectionWrapper var0) {
      return var0.jI("specific_properties.block.placed_model") || var0.jI("behaviours.block.placed_model");
   }

   public static CustomItemDefinition method10050(ConfigSectionWrapper var0, YamlConfig var1, String var2, String var3, Material var4, boolean var5) {
      ConfigSectionWrapper var6 = var0.kc("specific_properties.block");
      if (var6 == null) {
         var6 = var0.kc("behaviours.block");
      }

      if (var6 == null) {
         throw new IllegalArgumentException(
            "[Items] '" + var2 + ":" + var3 + "'. Error: Missing 'specific_properties.block' or 'behaviours.block' section. File: " + var1.DJ()
         );
      }

      BlockRealizationMode var7 = BlockRealizationMode.valueOf(var6.jO("placed_model.type"));
      if (var7 == BlockRealizationMode.ENTITY && !Version.isAtLeast(Version.v1_21_4)) {
         throw new IllegalStateException(
            "[Items] '"
               + var2
               + ":"
               + var3
               + "'. Error: placed_model.type ENTITY requires Minecraft server and clients 1.21.4+. No legacy Custom Model Data fallback is available. File: "
               + var1.DJ()
         );
      }

      var5 &= var4 == Material.PAPER && var7.isFullyRealized();
      int var8;
      if (!var7.isBlockBacked()) {
         var8 = -1;
      } else if (var5) {
         var8 = CustomBlockStateSettings.getInstance().resolveStateIdForRealization(var7, var2, var3);
         if (var8 >= 0) {
            var4 = var7.resolveMaterial(CustomBlockVariant.getMaterialById(var8));
         }
      } else {
         var8 = CustomBlockStateSettings.getInstance().resolveStateId(var4, var7, var2, var3);
      }

      if (var7 == BlockRealizationMode.FIRE) {
         if (var8 == -1 && var7.isBlockBacked()) {
            throw new IllegalArgumentException("[Items] '" + var2 + ":" + var3 + "'. Error: This fire block would have a block id > than 15.");
         } else {
            return new AgeableBlockDefinition(var1, var2, var3, var4, var8);
         }
      } else {
         if (var8 == -1 && var7.isBlockBacked()) {
            throw new IllegalStateException(
               "[Items] '"
                  + var2
                  + ":"
                  + var3
                  + "'. Error: Block would have a block id > than the max limit for blocks of type '"
                  + var7
                  + "'. In order to add more blocks please use another type of block (REAL, REAL_NOTE, REAL_TRANSPARENT or TILE)."
            );
         }

         ArrayList var9 = new ArrayList();
         ConfigurationSection var10 = var6.kb("placed_model.custom_variants");
         if (var10 != null) {
            for (String var12 : var10.getKeys(false)) {
               ConfigurationSection var13 = var10.getConfigurationSection(var12);
               String var14 = var13.getString("model", null);
               if (var14 == null) {
                  Log.error("[Items] '" + var2 + ":" + var3 + "'. Error: Missing 'model' for 'custom_variant' '" + var12 + "'. File: " + var1.DJ());
               } else {
                  Integer var15 = (Integer)var13.get("x", null);
                  Integer var16 = (Integer)var13.get("y", null);
                  if (var15 != null && var15 % 90 != 0) {
                     Log.error(
                        "[Items] '" + var2 + ":" + var3 + "'. Invalid 'x' for '" + var14 + "' block model. Allowed values: 0,90,180,270. File: " + var1.DJ()
                     );
                  } else if (var16 != null && var16 % 90 != 0) {
                     Log.error(
                        "[Items] '" + var2 + ":" + var3 + "'. Invalid 'y' for '" + var14 + "' block model. Allowed values: 0,90,180,270. File: " + var1.DJ()
                     );
                  } else {
                     if (!var14.contains(":")) {
                        var14 = var2 + ":" + var14;
                     }

                     var9.add(new ToolDurabilitySpec(var14, (Integer)var13.get("weight", null), var15, var16, (Boolean)var13.get("uvlock", null)));
                  }
               }
            }
         } else {
            var9 = null;
         }

         Material var22 = method10051(
            var6,
            var7,
            Settings.get().blockEntityFallbackMaterial,
            var3x -> Log.warn(
               "[Items] '"
                  + var2
                  + ":"
                  + var3
                  + "'. Invalid fallback_vanilla_block '"
                  + var3x
                  + "': it must be a solid, occluding, non-interactable vanilla block without gravity. Using "
                  + Settings.get().blockEntityFallbackMaterial
                  + ". File: "
                  + var1.DJ()
            )
         );
         CustomBlockDefinition var23 = new CustomBlockDefinition(
            var1,
            var2,
            var3,
            var4,
            var8,
            var7,
            ItemKind.valueOf(var6.bc("placed_model.break_particles", "VANILLA")),
            var6.method7078("drop_when_mined", !var6.method7078("cancel_drop", false)),
            var6.method7076("light_level", -1),
            var6.jZ("break_tools_blacklist"),
            var6.jZ("break_tools_whitelist"),
            var6.jZ("events_tools_blacklist"),
            var6.jZ("events_tools_whitelist"),
            var6.method7102("hardness", 0.5F) + 0.2F,
            var6.method7078("placed_model.shift_up", false),
            var6.method7078("placed_model.placeable_on_other_real_wire", false),
            var6.method7078("placed_model.placeable_on_water", false),
            var6.method7078("placed_model.placeable_on_lava", false),
            var6.method7076("placed_model.rotx", 0),
            var6.method7076("placed_model.roty", 0),
            var9
         );
         var23.anC = var22;
         var23.applyInventoryBlockState(var5);
         var23.method8847(var6.bc("sound.break.name", null), var6.method7102("sound.break.volume", 1.0F), var6.method7102("sound.break.pitch", 0.8F));
         var23.method8848(var6.bc("sound.place.name", null), var6.method7102("sound.place.volume", 1.0F), var6.method7102("sound.place.pitch", 0.8F));
         var23.method8849(var6.bc("sound.hit.name", null), var6.method7102("sound.hit.volume", 0.25F), var6.method7102("sound.hit.pitch", 0.5F));
         var23.method8850(var6.bc("sound.step.name", null), var6.method7102("sound.step.volume", 0.15F), var6.method7102("sound.step.pitch", 1.0F));
         var23.method8851(var6.bc("sound.fall.name", null), var6.method7102("sound.fall.volume", 0.5F), var6.method7102("sound.fall.pitch", 0.75F));
         var23.aN(var6.method7078("no_explosion", false));
         var23.setBreakTime(var6.method7102("blast_resistance", -1.0F));
         if (var6.jI("permission_suffix.break")) {
            var23.setBreakPermissionSuffix(var6.bc("permission_suffix.break", null));
         }

         if (var6.jI("permission_suffix.place")) {
            var23.setPlacePermissionSuffix(var6.bc("permission_suffix.place", null));
         }

         if (var6.jI("drop_on_silk_touch")) {
            var23.aO(var6.method7078("drop_on_silk_touch", true));
         }

         if (var6.jI("drop_on_shears")) {
            var23.aP(var6.method7078("drop_on_shears", true));
         }

         ConfigurationSection var24 = var6.kb("primeable");
         if (var24 != null) {
            ExplosiveBlockSpecBuilder var25 = ExplosiveBlockSpec.builder();
            var25.withRadius(var24.getInt("radius", 3));
            var25.withFuseTicks(var24.getInt("fuse_ticks", 80));
            var25.withDamageMultiplier(var24.getDouble("damage_multiplier", 1.0));
            var25.withWaterproof(var24.getBoolean("waterproof", true));
            var25.withIncendiary(var24.getBoolean("incendiary", false));
            ConfigurationSection var26 = var24.getConfigurationSection("triggers");
            if (var26 != null) {
               int var27 = 0;
               if (var26.getBoolean("explosion", false)) {
                  var27 = ExplosiveBlockSpec.combineFlags(var27, 8);
               }

               if (var26.getBoolean("fire", false)) {
                  var27 = ExplosiveBlockSpec.combineFlags(var27, 4);
               }

               if (var26.getBoolean("flaming_projectile", false)) {
                  var27 = ExplosiveBlockSpec.combineFlags(var27, 1);
               }

               if (var26.getBoolean("redstone", false)) {
                  var27 = ExplosiveBlockSpec.combineFlags(var27, 2);
               }

               var25.withTriggers(var27);
               ArrayList var17 = new ArrayList();
               if (var26.contains("tools")) {
                  for (String var20 : var26.getStringList("tools")) {
                     var17.add(PatternMatcher.method17(var20));
                  }
               }

               if (!var17.isEmpty()) {
                  var25.withItemsMatcher(new StringListMatcher(var17));
               }
            }

            var23.setExplosiveSpec(var25.build());
         }

         ContentManager.method8084().al.add(var23);
         ContentManager.method8084().am.put(var23.getNamespacedID(), var23);
         if (var7.isBlockBacked()) {
            ContentManager.method8084().an.put(var8, var23);
         }

         return var23;
      }
   }

   static Material method10051(ConfigSectionWrapper var0, BlockRealizationMode var1, Material var2, @Nullable Consumer var3) {
      return method10052(var0, var1, var2, var3, BlockRealizationMode::isRealizableMaterial);
   }

   static Material method10052(ConfigSectionWrapper var0, BlockRealizationMode var1, Material var2, @Nullable Consumer var3, Predicate var4) {
      if (var1 == BlockRealizationMode.ENTITY && var0.jI("fallback_vanilla_block")) {
         Material var5 = var0.method7105("fallback_vanilla_block", null);
         if (var5 != null && var4.test(var5)) {
            return var5;
         }

         if (var3 != null) {
            var3.accept(var0.bc("fallback_vanilla_block", ""));
         }

         return var2;
      } else {
         return var2;
      }
   }
}
