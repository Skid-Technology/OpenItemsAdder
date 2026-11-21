package dev.lone.itemsadder.config;

import org.yaml.snakeyaml.nodes.MappingNode;

final class YamlNodeWriter {
   private YamlNodeWriter() {
   }

   static void method7524(YamlNodeDocument var0) {
      var0.jJ("config_files.legacy_pack");
      var0.jJ("config_files.extract-default-items");
      var0.jJ("config_files.extract-default-configs");
      var0.bo("resource-pack.no-host.enabled", "resource-pack.hosting.no-host.enabled");
      var0.bo("resource-pack.auto-external-host.enabled", "resource-pack.hosting.auto-external-host.enabled");
      var0.bo("resource-pack.self-host.enabled", "resource-pack.hosting.self-host.enabled");
      var0.bo("resource-pack.self-host.server-ip", "resource-pack.hosting.self-host.server-ip");
      var0.bo("resource-pack.self-host.pack-port", "resource-pack.hosting.self-host.pack-port");
      var0.bo("resource-pack.external-host.enabled", "resource-pack.hosting.external-host.enabled");
      var0.bo("resource-pack.external-host.url", "resource-pack.hosting.external-host.url");
      if (var0.method7501("resource-pack.zip.protect-file-from-unzip", null) instanceof Boolean) {
         var0.bo("resource-pack.zip.protect-file-from-unzip", "resource-pack.zip.protect-file-from-unzip.protection_1");
      }

      var0.bo("resource-pack.zip.protect-file-from-unzip__DISABLE_IF_ANY_PROBLEM", "resource-pack.zip.protect-file-from-unzip.enabled");
      var0.bo("resource-pack.zip.protect-file-from-unzip.enabled", "resource-pack.zip.protect-file-from-unzip.protection_1");
      var0.bo("resource-pack.zip.protect-file-from-unzip.extreme", "resource-pack.zip.protect-file-from-unzip.protection_2");
      var0.bo("resource-pack.zip.overwrite-vanilla-lang-json__DISABLE_AT_YOUR_OWN_RISK", "resource-pack.zip.overwrite-vanilla-lang-json");
      var0.jJ("resource-pack.zip.overwrite-vanilla-lang-json");
      var0.bo("font_images.chat.add-hover-suggestion_PAPER_ONLY", "font_images.chat.add-hover-suggestion");
      var0.bo("font_images.chat.replace-in-customitems-name-and-lore", "font_images.replace-in-customitems-name-and-lore");
      var0.bo("config_files.minify-json", "resource-pack.zip.compress-json-files");
      var0.jJ("font_images.chat.reset-color-to-white");
      var0.jJ("resource-pack.zip.fix_transparent_noteblocks__disable-for-more-performance");
      var0.jJ("resource-pack.zip.generate-custom-armors-textures.enabled");
      var0.jJ("resource-pack.zip.generate-custom-armors-textures.mode");
      var0.jJ("resource-pack.zip.generate-custom-armors-textures._");
      if (var0.jI("resource-pack.zip.generate-custom-armors-textures.vanilla_1_17") && !var0.jI("resource-pack.zip.generate-custom-armors-textures.vanilla")) {
         var0.bo("resource-pack.zip.generate-custom-armors-textures.vanilla_1_17", "resource-pack.zip.generate-custom-armors-textures.vanilla");
         var0.jJ("resource-pack.zip.generate-custom-armors-textures.vanilla_1_17");
      }

      var0.jJ("font_images.tab-header-footer");
      var0.jJ("effects.text-effects.tab-header-footer");
      var0.jJ("blocks.fix-glitched-blocks.search-also-in-these-biomes");
      var0.jJ("resource-pack.zip.overwrite-vanilla-models__DISABLE_AT_YOUR_OWN_RISK");
      var0.jJ("resource-pack.zip.overwrite-vanilla-models");
      var0.jJ("debug.blocks.log-save-on-unload");
      var0.bo("debug.blocks.log-save", "debug.blocks.log-save-load.enabled");
      var0.bo("blocks.campfire.unlit-on-place", "blocks.campfire_enhancements.unlit_on_place");
      var0.bo("blocks.campfire.allow-any-item", "blocks.campfire_enhancements.allow_any_item");
      var0.bo("blocks.campfire.drop_when_broken", "blocks.campfire_enhancements.drop_when_broken");
      var0.bo("effects.text-effects.replace-in-customitems-name-and-lore", "effects.text-effects.customitems-name-and-lore.enabled");
      if (var0.jI("items.fix_trades_guis_match_custom_items") && !(var0.method7501("items.fix_trades_guis_match_custom_items", null) instanceof MappingNode)) {
         var0.jJ("items.fix_trades_guis_match_custom_items");
      }

      if (var0.jI("items.fix_trades_guis_match_custom_items.itemsadder_trade_machines.itemsadder_trade_machines")) {
         var0.jJ("items.fix_trades_guis_match_custom_items.itemsadder_trade_machines.itemsadder_trade_machines");
      }

      var0.jJ("items.show_item_durability");
      var0.jJ("resource-pack.protect-player.black-screen");
      var0.jJ("resource-pack.protect-player.hide-hud");
      var0.jJ("resource-pack.protect-player.cancel-movement");
      if (var0.jI("disable-MOB-behaviour__DISABLE_AT_YOUR_OWN_RISK")) {
         var0.method7507("disable-MOB-behaviour__DISABLE_AT_YOUR_OWN_RISK", "mobs.enabled", true);
         var0.jJ("disable-MOB-behaviour__DISABLE_AT_YOUR_OWN_RISK");
      }

      method7525(var0, true, "blocks.custom.mushroom", "blocks.disable-REAL-behaviour__DISABLE_AT_YOUR_OWN_RISK", "blocks.disable-REAL");
      method7525(var0, true, "blocks.custom.chorus", "blocks.disable-REAL_TRANSPARENT-behaviour__DISABLE_AT_YOUR_OWN_RISK", "blocks.disable-REAL_TRANSPARENT");
      method7525(var0, true, "blocks.custom.note", "blocks.disable-REAL_NOTE-behaviour__DISABLE_AT_YOUR_OWN_RISK", "blocks.disable-REAL_NOTE");
      method7525(var0, true, "blocks.custom.wire", "blocks.disable-REAL_WIRE-behaviour__DISABLE_AT_YOUR_OWN_RISK", "blocks.disable-REAL_WIRE");
      method7525(var0, true, "blocks.custom.fire", "blocks.disable-CUSTOM_FIRE-behaviour", "blocks.disable-FIRE");
      var0.jJ("blocks.convert-legacy-ia-region-files");
      var0.jJ("disable-plugin-loading-messages_DONT_ASK_ME_FOR_SUPPORT_IF_YOU_SET_TRUE__THANKS");
      var0.jJ("items.handle-wear-held-actions-on-unexpected-inventory-changes");
      var0.jJ("ignore-server-compatibility-check");
      var0.jJ("entities.custom-entitites.expert_users.bundle_packets");
      var0.jJ("entities.custom-entitites.expert_users.fast_packets_creation");
      var0.jJ("entities.allow-removing-old-furnitures_2_3_11");
      var0.jJ("debug.worlds-populators.log-loading-no-world");
      var0.jJ("debug.trees-populators.log-loading-no-world");
      var0.jJ("debug.surface-decorators.log-loading-no-world");
      var0.jJ("debug.cave-decorators.log-loading-no-world");
      var0.bo("debug.worlds-populators.log-generation", "debug.blocks-populators.log-generation");
      if (var0.jI("recipes.custom-recipes.unlock.on-pickup.any") || var0.jI("recipes.custom-recipes.unlock.on-pickup.ingredients")) {
         var0.method7506(
            "recipes.custom-recipes.unlock.automatically",
            var0.kj("recipes.custom-recipes.unlock.on-pickup.any") || var0.kj("recipes.custom-recipes.unlock.on-pickup.ingredients")
         );
      }

      var0.jJ("recipes.custom-recipes.unlock.on-pickup.any");
      var0.jJ("recipes.custom-recipes.unlock.on-pickup.ingredients");
      var0.jJ("recipes.custom-recipes.unlock.on-item-click.any");
      var0.jJ("recipes.custom-recipes.unlock.on-item-click.ingredients");
      var0.jJ("blocks.light-api");
      if (var0.jI("resource-pack.apply-during-chunk-load-phase")) {
         var0.bo("resource-pack.apply-during-chunk-load-phase", "resource-pack.apply_during_chunk_load_phase");
         var0.jJ("resource-pack.apply-during-chunk-load-phase");
      }

      if (var0.jI("resource-pack.apply-on-join")) {
         var0.bo("resource-pack.apply-on-join", "resource-pack.apply_on_join.enabled");
         var0.jJ("resource-pack.apply-on-join");
      }

      if (var0.jI("resource-pack.delay-ticks")) {
         var0.bo("resource-pack.delay-ticks", "resource-pack.apply_on_join.delay_ticks");
         var0.jJ("resource-pack.delay-ticks");
      }

      if (var0.jI("resource-pack.notify-other-plugins-interfering")) {
         var0.bo("resource-pack.notify-other-plugins-interfering", "resource-pack.notify_other_plugins_resourcepacks");
         var0.jJ("resource-pack.notify-other-plugins-interfering");
      }

      if (var0.jI("resource-pack.expert_users.allow_other_plugins_resourcepacks")) {
         var0.bo("resource-pack.expert_users.allow_other_plugins_resourcepacks", "resource-pack.allow_other_plugins_resourcepacks");
         var0.jJ("resource-pack.expert_users.allow_other_plugins_resourcepacks");
      }

      var0.jJ("debug.resource-pack");
      if (var0.jI("resource-pack.pre_1_21_3_shaders_fix.enabled")) {
         var0.bo("resource-pack.pre_1_21_3_shaders_fix.enabled", "resource-pack.pre_1_21_2_shaders_fix.enabled");
         var0.jJ("resource-pack.pre_1_21_3_shaders_fix.enabled");
      }

      if (var0.jI("resource-pack.apply_on_join.enabled")) {
         var0.bo("resource-pack.apply_on_join.enabled", "resource-pack.auto_apply.enabled");
      }

      if (var0.jI("resource-pack.apply_during_chunk_load_phase")) {
         var0.bo("resource-pack.apply_during_chunk_load_phase", "resource-pack.auto_apply.before_join");
      }

      var0.jJ("resource-pack.apply_during_chunk_load_phase");
      var0.bo("resource-pack.apply_on_join.delay_ticks", "resource-pack.auto_apply.delay_ticks");
      var0.jJ("resource-pack.apply_on_join");
      var0.jJ("debug.recipes.log-recipes-unknown-item");
      if (var0.jI("effects.text-effects.hide-scoreboard-numbers")) {
         var0.bo("effects.text-effects.hide-scoreboard-numbers", "effects.text-effects.hide_scoreboard_numbers");
         var0.jJ("effects.text-effects.hide-scoreboard-numbers");
      }

      if (var0.jI("effects.text-effects.hide-scoreboard-numbers-old-clients")) {
         var0.bo("effects.text-effects.hide-scoreboard-numbers-old-clients", "effects.text-effects.hide_scoreboard_numbers_old_clients");
         var0.jJ("effects.text-effects.hide-scoreboard-numbers-old-clients");
      }

      if (var0.jI("effects.text-effects.customitem-name-and-lore")) {
         var0.bo("effects.text-effects.customitem-name-and-lore.enabled", "effects.text-effects.customitem_name_and_lore.enabled");
         var0.jJ("effects.text-effects.customitem-name-and-lore");
      }

      if (var0.jI("font_images.replace-in-customitems-name-and-lore")) {
         var0.bo("font_images.replace-in-customitems-name-and-lore", "font_images.customitem_name_and_lore.enabled");
         var0.jJ("font_images.replace-in-customitems-name-and-lore");
      }

      method7525(var0, false, "font_images.chat.add_hover_suggestion", "font_images.chat.add-hover-suggestion");
      method7525(var0, false, "font_images.chat.tab_autocompletion_1_19_1_plus", "font_images.chat.tab-autocompletion_1_19_1_plus");
      if (var0.jI("effects.text-effects")) {
         var0.bo("effects.text-effects", "text_effects");
         var0.jJ("effects.text-effects");
      }

      if (var0.jI("effects")) {
         var0.lY("effects");
      }

      var0.jJ("resource-pack.zip.fix_transparent_noteblocks");
      if (var0.jI("entities.custom-entitites")) {
         var0.bo("entities.custom-entitites", "entities.custom_entities");
         var0.jJ("entities.custom-entitites");
      }

      var0.jJ("blocks.campfire_enhancements");
      var0.jJ("font_images.player-display-name.enabled");
      var0.jJ("font_images.vault-prefix-suffix.enabled");
      var0.jJ("huds.hide_if_pack_not_accepted");
      var0.jJ("compatibility.worldguard.show_legacy_furniture_flags");
      if (var0.jI("resource-pack.zip.generate-custom-armors-textures.optifine_nbt")) {
         var0.bo(
            "resource-pack.zip.generate-custom-armors-textures.optifine_nbt",
            "resource-pack.zip.custom_armors_shaders_legacy.optifine.generate_cit.1_20_4_lower"
         );
         var0.jJ("resource-pack.zip.generate-custom-armors-textures.optifine_nbt");
      }

      if (var0.jI("resource-pack.zip.generate-custom-armors-textures.vanilla")) {
         var0.bo("resource-pack.zip.generate-custom-armors-textures.vanilla", "resource-pack.zip.custom_armors_shaders_legacy.vanilla.generate_textures");
         var0.jJ("resource-pack.zip.generate-custom-armors-textures.vanilla");
      }

      if (var0.jI("resource-pack.zip.generate-custom-armors-textures.overwrite_leather_armor_layers_textures")) {
         var0.bo(
            "resource-pack.zip.generate-custom-armors-textures.overwrite_leather_armor_layers_textures",
            "resource-pack.zip.custom_armors_shaders_legacy.vanilla.overwrite_leather_layers"
         );
         var0.jJ("resource-pack.zip.generate-custom-armors-textures.overwrite_leather_armor_layers_textures");
      }

      if (var0.jI("resource-pack.zip.generate-custom-armors-textures.1_21_5_plus_armors_shaders")) {
         var0.bo(
            "resource-pack.zip.generate-custom-armors-textures.1_21_5_plus_armors_shaders",
            "resource-pack.zip.custom_armors_shaders_legacy.vanilla.generate_shader.1_21_5_plus"
         );
         var0.jJ("resource-pack.zip.generate-custom-armors-textures.1_21_5_plus_armors_shaders");
      }

      var0.bo("items.fix_unstackable_items_on_click", "items.auto_update.fix_unstackable_items_on_click");
      String var1 = "items.auto_update.fix_unstackable_items_on_click";
      String var2 = "items.auto_update.on_stack_merge";
      String var3 = "items.auto_update.on_inventory_open";
      if (!var0.jI(var1) && var0.jI(var2) && var0.jI(var3) && var0.kj(var2) != var0.kj(var3)) {
         throw new IllegalArgumentException("Conflicting legacy " + var2 + " and " + var3 + "; set " + var1 + " explicitly");
      }

      var0.bo(var2, var1);
      var0.bo(var3, var1);
      if (var0.jI("resource-pack.zip.custom_armors_shaders_legacy.vanilla.generate_textures")) {
         var0.bo("resource-pack.zip.custom_armors_shaders_legacy.vanilla.generate_textures", "resource-pack.zip.custom_armors_shaders_legacy.enabled");
         var0.jJ("resource-pack.zip.custom_armors_shaders_legacy.vanilla.generate_textures");
      }

      if (var0.jI("resource-pack.zip.custom_armors_shaders_legacy.enabled")) {
         boolean var4 = var0.kj("resource-pack.zip.custom_armors_shaders_legacy.enabled");
         if (var4) {
            var0.method7506("resource-pack.zip.custom_armors_shaders_legacy.vanilla.overwrite_leather_layers", false);
         }

         var0.method7506("resource-pack.zip.custom_armors_shaders_legacy.vanilla.generate_shader.1_21_4_lower", var4);
         var0.method7506("resource-pack.zip.custom_armors_shaders_legacy.vanilla.generate_shader.1_21_5_plus", var4);
         var0.method7506("resource-pack.zip.custom_armors_shaders_legacy.optifine.generate_cit.1_20_4_lower", false);
      }

      var0.jJ("resource-pack.zip.custom_armors_shaders_legacy.enabled");
      if (var0.jI("resource-pack.custom_blocks_sounds_patch.step.enabled")) {
         var0.jJ("resource-pack.custom_blocks_sounds_patch.step.allow_non_player");
         var0.bo("resource-pack.custom_blocks_sounds_patch.step.enabled", "resource-pack.custom_blocks_sounds_patch.step");
         var0.jJ("resource-pack.custom_blocks_sounds_patch.step.enabled");
         var0.jJ("resource-pack.custom_blocks_sounds_patch.step.allow_non_player");
      }

      var0.jJ("resource-pack.atlas_support");
      var0.jJ("resource-pack.1_19_3_atlas_support");
      if (var0.jI("resource-pack.zip.emotes.1_21_5_plus_shaders")) {
         var0.bo("resource-pack.zip.emotes.1_21_5_plus_shaders", "resource-pack.zip.emotes.1_21_5_to_1_21_9_shaders");
         var0.jJ("resource-pack.zip.emotes.1_21_5_plus_shaders");
      }

      if (var0.jI("entities.custom_entities.expert_users.extract_shaders")) {
         var0.bo("entities.custom_entities.expert_users.extract_shaders", "resource-pack.zip.emotes.legacy_shaders");
         var0.jJ("entities.custom_entities.expert_users.extract_shaders");
      }

      if (var0.method7501("entities.custom_entities.emotes", null) instanceof Boolean var6) {
         var0.jJ("entities.custom_entities.emotes");
         var0.method7505("entities.custom_entities.emotes.enabled", var6);
      }

      if (var0.jI("resource-pack.hosting.external-host.skip-url-file-type-check___DONT_ASK_HELP_IF_SET_TRUE")) {
         var0.bo(
            "resource-pack.hosting.external-host.skip-url-file-type-check___DONT_ASK_HELP_IF_SET_TRUE",
            "resource-pack.hosting.external-host.skip_url_file_type_check"
         );
         var0.jJ("resource-pack.hosting.external-host.skip-url-file-type-check___DONT_ASK_HELP_IF_SET_TRUE");
      }

      method7525(
         var0,
         false,
         "resource-pack.zip.protect-file-from-unzip.short_texture_references",
         "resource-pack.zip.protect-file-from-unzip.obfuscate_texture_references"
      );
      var0.jJ("resource-pack.zip.protect-file-from-unzip.protection_4");
   }

   private static void method7525(YamlNodeDocument var0, boolean var1, String var2, String... var3) {
      if (!var0.jI(var2)) {
         for (int var4 = var3.length - 1; var4 >= 0; var4--) {
            if (var0.jI(var3[var4])) {
               var0.method7507(var3[var4], var2, var1);
               break;
            }
         }
      }

      for (String var7 : var3) {
         var0.jJ(var7);
      }
   }
}
