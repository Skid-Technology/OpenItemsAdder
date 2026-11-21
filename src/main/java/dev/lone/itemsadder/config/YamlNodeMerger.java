package dev.lone.itemsadder.config;

import com.alibaba.fastjson2.JSON;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;

public final class YamlNodeMerger {
   private YamlNodeMerger() {
   }

   public static YamlFileSnapshot method7526(Path var0, String var1) {
      YamlNodeDocument var2 = new YamlNodeDocument(var1);
      if (var2.kf("config_version") instanceof Integer var4 && var4 == 1) {
         byte[] var5 = Files.exists(var0) ? Files.readAllBytes(var0) : null;
         if (var5 == null) {
            return new YamlFileSnapshot(var0, null, var1, true, List.of());
         }

         YamlNodeDocument var6 = new YamlNodeDocument(new String(var5, StandardCharsets.UTF_8));
         Object var7 = var6.method7501("config_version", 0);
         if (var7 instanceof Integer var8 && var8 >= 0 && var8 <= var4) {
            ArrayList var9 = new ArrayList();
            if (var8 == 0) {
               method7529(var6, var9);
               method7528(var6, var2, var9);
               method7527(var6, var2, var9);
            }

            method7530(var6, var2, var9);
            if (var8 < var4) {
               var6.method7505("config_version", var4);
            }

            var6.method7506("blocks.recover_legacy_block_positions.enabled", true);
            var6.method7509(var2);
            if (var6.bLS) {
               var6.method7511(var2);
            }

            String var10 = var6.bLS ? var6.acZ() : new String(var5, StandardCharsets.UTF_8);
            YamlNodeDocument var11 = new YamlNodeDocument(var10);
            var11.method7509(new YamlNodeDocument(var1));
            return new YamlFileSnapshot(var0, var5, var10, var6.bLS, var9);
         } else {
            throw new IllegalArgumentException("Unsupported config_version: " + var7 + ". File left unchanged.");
         }
      } else {
         throw new IllegalArgumentException("Unsupported bundled config_version");
      }
   }

   private static void method7527(YamlNodeDocument var0, YamlNodeDocument var1, List var2) {
      method7531(var0, var2);
      String var3 = "pack_hosting.protection.";
      String var4 = "pack_hosting.self_host.protection.";
      String var5 = "pack_hosting.simple_self_host.protection.";

      for (String var7 : List.of(var3, var4, var5)) {
         for (String var9 : List.of("", "rate_limit.", "rate_limit.cooldown.")) {
            String var10 = (var7 + var9).replaceAll("\\.$", "");
            if (var0.jI(var10) && !(var0.lX(var10) instanceof MappingNode)) {
               throw new IllegalArgumentException("Expected a section at " + var10);
            }
         }
      }

      String var18 = var4 + "rate_limit.cooldown.trigger_on_failed_times";
      String var19 = var4 + "rate_limit.cooldown.after_rejections";
      if (var0.jI(var18)) {
         int var20 = var0.jP(var18);
         if (var20 < 0) {
            throw new IllegalArgumentException("Expected non-negative integer: " + var18);
         }

         var0.method7505(var18, Math.addExact(var20, 2));
         method7537(var0, var18, var19, var2);
         var0.bo(var18, var19);
      }

      String var21 = (String)var0.method7501("pack_hosting.method", var1.kf("pack_hosting.method"));

      for (String var24 : List.of(
         "require_minecraft_headers",
         "rate_limit.enabled",
         "rate_limit.max_requests",
         "rate_limit.period_seconds",
         "rate_limit.cooldown.enabled",
         "rate_limit.cooldown.after_rejections",
         "rate_limit.cooldown.duration_minutes"
      )) {
         Object var11 = var0.method7501(var3 + var24, var1.kf(var3 + var24));
         Object var12 = var0.method7501(var4 + var24, var11);
         Object var13 = var24.equals("rate_limit.enabled") && !var0.jI(var3 + var24) ? false : (var24.equals("require_minecraft_headers") ? var11 : var12);
         Object var14 = var0.method7501(var5 + var24, var13);
         var0.method7506(var3 + var24, var21.equals("simple_self_host") ? var14 : var12);

         for (String var16 : List.of(var4, var5)) {
            Object var17 = var16.equals(var4) ? var12 : var14;
            if (Objects.equals(var17, var0.kf(var3 + var24))) {
               var0.bo(var16 + var24, var3 + var24);
            } else {
               var0.method7506(var16 + var24, var17);
            }
         }
      }

      for (String var25 : List.of(var4, var5)) {
         var0.lY(var25 + "rate_limit.cooldown");
         var0.lY(var25 + "rate_limit");
         var0.lY(var25.substring(0, var25.length() - 1));
      }
   }

   private static void method7528(YamlNodeDocument var0, YamlNodeDocument var1, List var2) {
      String var3 = "items.update_legacy_items";
      String var4 = "items.migrate_legacy_v3_durability";
      if (var0.jI(var3)) {
         if (!(var0.lX(var3) instanceof MappingNode)) {
            throw new IllegalArgumentException("Expected a section at " + var3);
         }

         if (var0.jI(var3 + ".enabled") || var0.jI(var3 + ".update_durability")) {
            boolean var5 = var0.jI(var3 + ".enabled") ? var0.kj(var3 + ".enabled") : var1.kj(var4);
            boolean var6 = var0.jI(var3 + ".update_durability") ? var0.kj(var3 + ".update_durability") : var1.kj(var4);
            boolean var7 = var5 && var6;
            if (var0.jI(var4) && var0.kj(var4) != var7) {
               var2.add("Kept explicit " + var4 + " instead of legacy " + var3);
            }

            var0.method7506(var4, var7);
            var0.bo(var3 + ".enabled", var4);
            var0.bo(var3 + ".update_durability", var4);
         }

         var0.lY(var3);
      }
   }

   private static void method7529(YamlNodeDocument var0, List var1) {
      method7536(var0, "resource-pack.apply_on_join.delay_ticks", "resource-pack.auto_apply.delay_ticks", var1);
      method7536(var0, "crops.area_limit_radius", "crops.area_limit_blocks_radius", var1);
      method7537(var0, "debug.blocks.log-save", "debug.blocks.log-save-load.enabled", var1);
      String var2 = "recipes.custom-recipes.unlock.on-pickup.";
      String var3 = "recipes.custom-recipes.unlock.automatically";
      if ((var0.jI(var2 + "any") || var0.jI(var2 + "ingredients"))
         && var0.jI(var3)
         && var0.kj(var3) != (var0.kj(var2 + "any") || var0.kj(var2 + "ingredients"))) {
         var1.add("Kept explicit " + var3 + " instead of legacy " + var2 + "any/ingredients");
      }

      YamlNodeWriter.method7524(var0);
      method7538(var0);
      method7535(var0, var1);
      var0.lY("advanced");
      var0.lY("compatibility.worldguard");
      var0.lY("debug.issues");
   }

   private static void method7530(YamlNodeDocument var0, YamlNodeDocument var1, List var2) {
      method7534(var0, "effects.hide-scoreboard-numbers-old-clients", "resource_pack.scoreboard.hide_numbers_old_clients", var2);
      String var3 = "resource_pack.models.merge_item_definitions";
      if (var0.jI(var3)) {
         if (Boolean.FALSE.equals(var0.kf(var3))) {
            var2.add(var3 + ": false is obsolete; item definitions are now always merged. Removed the old setting from config.yml.");
         }

         var0.jJ(var3);
      }

      for (String var5 : List.of("config_files.force_sync_loading", "config_files.force-sync-loading", "force_sync_loading", "force-sync-loading")) {
         var0.jJ(var5);
      }

      method7534(var0, "entities.custom_entities", "custom_entities", var2);

      for (String var12 : List.of("bundle_packets", "humanoid_equipment", "terrain_feet_view_distance", "ragdoll_near_detail_distance")) {
         method7537(var0, "custom_entities.v2." + var12, "custom_entities." + var12, var2);
      }

      var0.bo("custom_entities.v2", "custom_entities");
      var0.lY("custom_entities.v2");
      method7534(var0, "font_images.chat.tab_autocompletion_1_19_1_plus", "font_images.chat.tab_autocompletion", var2);
      method7534(var0, "entities.furniture.auto_update_transformations", "resource_pack.content_conversion.auto_update_transformations", var2);
      method7534(var0, "resource_pack.text_effects.hide_scoreboard_numbers_old_clients", "resource_pack.scoreboard.hide_numbers_old_clients", var2);
      method7534(var0, "text_effects.hide_scoreboard_numbers", "scoreboard.hide_numbers", var2);
      method7534(var0, "blocks.convert_paper_to_block_in_inventory", "resource_pack.custom_blocks.use_block_material_in_inventory", var2);

      for (String var13 : List.of("max_concurrent_downloads_per_ip", "max_speed_mib_per_second")) {
         method7534(var0, "pack_hosting.simple_self_host." + var13, "pack_hosting.protection." + var13, var2);
      }

      method7531(var0, var2);

      for (String[] var7 : new String[][]{
         {"resource_pack.models.fix_1_21_11_plus_atlas_items_models.models", "resource_pack.models.fix_1_21_11_plus_atlas_items_models.force_fix_models"},
         {"resource_pack.fix_force_unicode_font_images", "resource_pack.enable_font_images_with_force_unicode"},
         {"resource_pack.obfuscation.protection_1", "resource_pack.obfuscation.zip_entries"},
         {"resource_pack.obfuscation.protection_2", "resource_pack.obfuscation.zip_archive"},
         {"resource_pack.obfuscation.protection_3", "resource_pack.obfuscation.png"},
         {"resource_pack.custom_block_sounds.other", "resource_pack.custom_block_sounds.non_step"},
         {"pack_delivery.protect_player.force_cancel_movement", "pack_delivery.protect_player.movement.cancel_events"},
         {"pack_delivery.protect_player.lock_player", "pack_delivery.protect_player.movement.lock_using_entity"}
      }) {
         method7534(var0, var7[0], var7[1], var2);
      }

      method7534(
         var0,
         "resource_pack.custom_armors_shaders_legacy.optifine.generate_cit.1_20_4_lower",
         "resource_pack.custom_armors_shaders_legacy.generate_optifine_cit_1_20_4_lower",
         var2
      );
      var0.lY("resource_pack.custom_armors_shaders_legacy.optifine.generate_cit");
      var0.lY("resource_pack.custom_armors_shaders_legacy.optifine");
      String var11 = "resource_pack.merge_other_plugins_resourcepacks_folders";
      if (var0.kf(var11) instanceof List) {
         var0.bo(var11, var11 + ".list");
      }

      method7532(var0, var1, var2);
   }

   private static void method7531(YamlNodeDocument var0, List var1) {
      for (String var3 : List.of("", "self_host.", "simple_self_host.")) {
         method7534(var0, "pack_hosting." + var3 + "protection.block_non_game_requests", "pack_hosting." + var3 + "protection.require_minecraft_headers", var1);
      }
   }

   private static void method7532(YamlNodeDocument var0, YamlNodeDocument var1, List var2) {
      String var3 = "recipes.no_permission_message";
      String var4 = "recipes.show_no_permission_chat_message";
      String var5 = "recipes.show_no_permission_popup_in_recipebook";
      if (var0.jI(var4) || var0.jI(var5)) {
         Object var6 = var1.kf(var3);
         boolean var7 = var0.method7503(var4, "chat".equals(var6) || "both".equals(var6));
         boolean var8 = var0.method7503(var5, "recipebook".equals(var6) || "both".equals(var6));
         method7533(var0, var3, var7 ? (var8 ? "both" : "chat") : (var8 ? "recipebook" : "none"), var2, var4, var5);
      }

      String var16 = "registry_changes.action";
      if (var0.jI("registry_changes.rejoin.enabled") || var0.jI("registry_changes.kick")) {
         boolean var19 = var0.method7503("registry_changes.rejoin.enabled", "rejoin".equals(var1.kf(var16)));
         boolean var21 = var0.method7503("registry_changes.kick", "kick".equals(var1.kf(var16)));
         method7533(var0, var16, var19 ? "rejoin" : (var21 ? "kick" : "none"), var2, "registry_changes.rejoin.enabled", "registry_changes.kick");
      }

      String var20 = "blocks.convert_vanilla_blocks";
      String var22 = "blocks.fix_glitched_blocks";
      var16 = "blocks.unregistered_blockstates.action";
      if (var0.jI(var20) || var0.jI(var22)) {
         boolean var9 = var0.method7503(var20 + ".enabled", "register_as_custom".equals(var1.kf(var16)));
         boolean var10 = var0.method7503(var22 + ".enabled", "restore_vanilla".equals(var1.kf(var16)));
         if (var9 && var10 && !var0.jI(var16)) {
            throw new IllegalArgumentException("Cannot migrate mutually enabled " + var20 + " and " + var22);
         }

         method7533(var0, var16, var9 ? "register_as_custom" : (var10 ? "restore_vanilla" : "none"), var2, var20 + ".enabled", var22 + ".enabled");
         String var11 = "blocks.unregistered_blockstates.only_new_chunks";
         String var12 = var9 ? var20 : var22;
         boolean var13 = var0.method7503(var12 + ".only_new_chunks", var9 || var1.kj(var11));
         var0.method7506(var11, var13);
         method7534(var0, var12 + ".only_new_chunks", var11, var2);
         String var14 = var9 ? var22 : var20;
         if (var0.jI(var14 + ".only_new_chunks")) {
            var2.add("Retired inactive " + var14 + ".only_new_chunks=" + var0.kf(var14 + ".only_new_chunks"));
         }

         var0.jJ(var14 + ".only_new_chunks");
         var0.lY(var20);
         var0.lY(var22);
      }

      String var23 = "resource_pack.custom_font.enabled";
      String var24 = "resource_pack.thin_font";
      var16 = "resource_pack.font.mode";
      if (var0.jI(var23) || var0.jI(var24)) {
         boolean var25 = var0.method7503(var23, "custom".equals(var1.kf(var16)));
         boolean var27 = var0.method7503(var24, "thin".equals(var1.kf(var16)));
         String var29 = var25 == var27 ? "vanilla" : (var25 ? "custom" : "thin");
         method7533(var0, var16, var29, var2, var23, var24);
      }

      method7534(var0, "resource_pack.custom_font", "resource_pack.font.custom", var2);
      String var26 = "resource_pack.font.custom.shift";
      if (var0.kf(var26) instanceof String var28) {
         Object var31;
         try {
            var31 = JSON.parse(var28);
         } catch (RuntimeException var15) {
            throw new IllegalArgumentException("Invalid font shift at " + var26, var15);
         }

         if (!(var31 instanceof List var32)
            || var32.size() != 2
            || var32.stream().anyMatch(var0x -> !(var0x instanceof Number var1x && Double.isFinite(var1x.doubleValue())))) {
            throw new IllegalArgumentException("Expected two finite numbers at " + var26);
         }

         var0.method7505(var26, new ArrayList(var32));
      }
   }

   private static void method7533(YamlNodeDocument var0, String var1, String var2, List var3, String... var4) {
      if (var0.jI(var1) && !Objects.equals(var0.kf(var1), var2)) {
         var3.add("Kept explicit " + var1 + " instead of legacy flags");
      }

      var0.method7506(var1, var2);

      for (String var8 : var4) {
         var0.bo(var8, var1);
      }
   }

   private static void method7534(YamlNodeDocument var0, String var1, String var2, List var3) {
      method7537(var0, var1, var2, var3);
      var0.bo(var1, var2);
   }

   private static void method7535(YamlNodeDocument var0, List var1) {
      Properties var2 = new Properties();

      try (InputStream var3 = YamlNodeMerger.class.getResourceAsStream("/config_migrations/1.properties")) {
         if (var3 == null) {
            throw new IOException("Missing bundled config migration");
         }

         var2.load(new InputStreamReader(var3, StandardCharsets.UTF_8));
      }

      ArrayList var11 = new ArrayList<>(var2.stringPropertyNames());
      var11.sort(Comparator.comparingInt(YamlNodeMerger::ma).reversed().thenComparing(String::compareTo));

      for (String var5 : var11) {
         String[] var6 = var2.getProperty(var5).split(",", -1);
         String var7 = var6[0];
         if (var7.isEmpty()) {
            var0.jJ(var5);
         } else {
            for (int var8 = 1; var8 < var6.length && var0.jI(var5); var8++) {
               method7537(var0, var5, var6[var8], var1);
               var0.method7506(var6[var8], var0.kj(var5));
            }

            method7537(var0, var5, var7, var1);
            var0.bo(var5, var7);
         }
      }

      var0.lY("logging.other");
      var0.lY("logging.issues");
   }

   private static void method7536(YamlNodeDocument var0, String var1, String var2, List var3) {
      if (var0.jI(var1)) {
         Object var4 = var0.kf(var1);
         if (var0.jI(var2) && !Objects.equals(var4, var0.kf(var2))) {
            var3.add("Preserved the historically effective value from " + var1 + " for " + var2);
         }

         var0.bo(var1, var2);
         var0.method7505(var2, var4);
      }
   }

   private static void method7537(YamlNodeDocument var0, String var1, String var2, List var3) {
      if (!var1.equals(var2) && var0.jI(var1) && var0.jI(var2)) {
         Object var4 = var0.kf(var1);
         Object var5 = var0.kf(var2);
         if (!(var4 instanceof Node) && !(var5 instanceof Node)) {
            if (!Objects.equals(var4, var5)) {
               var3.add("Kept explicit " + var2 + " instead of legacy " + var1);
            }
         }
      }
   }

   private static int ma(String var0) {
      return var0.split("\\.").length;
   }

   private static void method7538(YamlNodeDocument var0) {
      LinkedHashMap var1 = new LinkedHashMap();
      var1.put("no-host", "none");
      var1.put("simple_self_host", "simple_self_host");
      var1.put("self-host", "self_host");
      var1.put("external-host", "external_host");
      var1.put("lobfile", "lobfile");
      var1.put("auto-external-host", "auto_external_legacy");
      ArrayList var2 = new ArrayList();
      boolean var3 = false;

      for (String var5 : var1.keySet()) {
         String var6 = "resource-pack.hosting." + var5 + ".enabled";
         if (var0.jI(var6)) {
            var3 = true;
            if (var0.kj(var6)) {
               var2.add(var5);
            }
         }
      }

      if (!var0.jI("pack_hosting.method")) {
         if (var2.size() > 1 || var3 && var2.isEmpty()) {
            throw new IllegalArgumentException("Select exactly one pack hosting method: " + var2 + ". File left unchanged.");
         }

         if (!var2.isEmpty()) {
            String var7 = "resource-pack.hosting." + (String)var2.getFirst() + ".enabled";
            var0.bo(var7, "pack_hosting.method");
            var0.method7505("pack_hosting.method", var1.get(var2.getFirst()));
         }
      }

      for (String var9 : var1.keySet()) {
         String var10 = "resource-pack.hosting." + var9;
         var0.jJ(var10 + ".enabled");
         var0.lY(var10);
      }
   }
}
