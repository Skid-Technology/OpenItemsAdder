package dev.lone.itemsadder.config;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.block.BlockRealizationMode;
import dev.lone.itemsadder.internal.SwitchMap37;
import dev.lone.itemsadder.item.UpdateMode;
import dev.lone.itemsadder.resourcepack.DownloadPolicy;
import dev.lone.itemsadder.resourcepack.PackFormat;
import dev.lone.itemsadder.resourcepack.PackHostingMode;
import dev.lone.itemsadder.world.WorldActivationTracker;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public class Settings {
   private static volatile Settings INSTANCE = new Settings();
   private Set enabledLoggingKeys = Set.of();
   public boolean guiScrollable = true;
   public boolean guiScrollableInventoryTitle = false;
   public boolean logPerformanceBenchmark;
   public String packDeliveryUuid;
   public boolean perClientVersionDelivery;
   public boolean showFallbackArmorOnPackNotLoaded;
   public boolean soundPacksEnabled = true;
   public boolean shaderPacksEnabled = true;
   public Set enabledClientVersions = Set.of();
   public int packCommandCooldownSeconds;
   public boolean simpleSelfHost;
   public String simpleSelfHostUrl;
   public DownloadPolicy simpleSelfHostDef;
   @Nullable
   public String selfHostPackUrl;
   public DownloadPolicy selfHostDef;
   @Deprecated
   public boolean ek = true;
   public boolean obfuscateZipEntries;
   public boolean obfuscateZipArchive;
   public boolean obfuscatePng;
   public boolean obfuscateShortTextureRefs;
   public String dictionariesLang;
   public boolean customBlocksMushroom;
   public boolean customBlocksChorus;
   public boolean customBlocksNote;
   public boolean customBlocksEnable120States350;
   public boolean customBlocksEnable261States200;
   public boolean customBlocksWire;
   public boolean customBlocksFire;
   public boolean customBlocksEntity;
   public int blockEntityRenderRadius;
   public int blockEntityMaxDisplaysPerPlayer;
   public int blockEntityMaxBlocksPerChunk;
   public Material blockEntityFallbackMaterial;
   public int blockEntityUpdateIntervalTicks;
   public int blockEntityWarmSlotTimeoutTicks;
   public boolean liquidsEnabled;
   public boolean fixLiquidsFlow;
   public boolean fixFloatingBlocks;
   public boolean fontImagesInventoryTitle;
   public boolean autoApplyForced;
   public boolean autoApplyPack;
   public int autoApplyDelayTicks;
   public boolean kickOnDecline;
   public boolean notifyOtherPluginsResourcepacks;
   public PackHostingMode packHostingMode;
   public boolean compressPng;
   public boolean hideSlotHighlight;
   @Nullable
   public String inventoryTitle;
   public boolean compressJson;
   public boolean compressShaders;
   public boolean validateShaders;
   public boolean compressSounds;
   public int soundsQualityPercent;
   public boolean fixMissingParticle;
   public boolean fixDarkInInventory;
   public boolean logNonStandardRotations;
   public boolean fixAtlasItemModels;
   public Set forceFixAtlasModels = Set.of();
   public boolean armorShadersEnabled;
   public boolean armorGenOptifineCit;
   public boolean armorOverwriteLeatherLayers;
   public boolean armorGenShaderPre1214;
   public boolean armorGenShader1215Plus;
   public boolean emoteShaders1215To1219;
   public boolean emoteModernMethod;
   public boolean logArmorsLoading;
   public boolean logFixPacketTooLarge;
   public boolean logConvertVanillaBlocks;
   public boolean logFixBuggedBlocks;
   public boolean logUnusualBlockBreak;
   public boolean logUnknownBlockBreak;
   public boolean logBlockSaveLoad;
   public int logBlockSaveLoadMinMs;
   public boolean skipUrlFileTypeCheck;
   public boolean logConfigLoading;
   public boolean logConfigReloading;
   public boolean fixPacketTooLargeKick;
   public boolean customEntitiesEnabled;
   public boolean emotesEnabled;
   public boolean emotesUseArmorStands;
   public boolean emotesThirdPersonView;
   public boolean emotesMatchPlayerScale;
   @Nullable
   public Double customEntityRenderDistance;
   public boolean emotesRagdollOnDeath;
   public int emotesDeathScreenDelaySeconds;
   public boolean emotesAutoRespawn;
   public boolean customEntitiesDelayPackets;
   public boolean customEntitiesBundlePackets;
   public boolean customEntitiesHumanoidEquipment;
   public int customEntityTerrainFeetViewDistance = 10;
   public int customEntityRagdollNearDetailDistance = 30;
   public boolean emoteLegacyShadersPre1214;
   public boolean disguisesLagCompensation;
   public int customEntitiesMaxPerChunk;
   public int cropAvgSecondsPerStage;
   public boolean cropFovRenderLimit;
   public boolean cropCheckerboardPattern;
   public int cropCheckerboardDistance;
   public int cropMaxShownPerPlayer;
   public boolean cropBillboard;
   public int cropBillboardDistance;
   public int cropTickInterval;
   public int cropRenderRadiusBlocks;
   public int cropMaxInArea;
   public int cropAreaLimitRadius;
   public boolean cropPreciseHitbox;
   public boolean cropContinuousHarvest;
   public boolean allowSpawnerLootSilkTouch;
   public boolean fixAttackDamageLore;
   public boolean logSurfaceDecoratorGen;
   public boolean logCaveDecoratorGen;
   public boolean logBlocksPopulatorGen;
   public boolean logTreesPopulatorGen;
   public boolean unregBlockstatesRegisterAsCustom;
   public boolean unregBlockstatesOnlyNewChunks;
   public boolean field36;
   public boolean recoverLegacyBlockPositions;
   public boolean unregBlockstatesOnlyNewChunksAlias;
   public boolean unregBlockstatesRestoreVanilla;
   public List contentsFoldersPriority = List.of();
   public List mergedResourcepackFolders = List.of();
   public boolean extractInternalUtilityAssets;
   public boolean protectLockUsingEntity;
   public boolean protectCancelCommands;
   public boolean protectCancelTeleport;
   public boolean protectCancelMovementEvents;
   public boolean fontImagesWithForceUnicode;
   public boolean allowOtherPluginsResourcepacks;
   public boolean recipeNoPermMsgRecipebook;
   public boolean recipeNoPermMsgChat;
   public boolean cacheFontImageReplacements;
   public boolean fontImagesChat;
   public boolean fontImagesChatHoverSuggestion;
   public boolean fontImagesSign;
   public boolean fontImagesBook;
   public boolean fontImagesAnvil;
   public boolean fontImagesCommandBlocks;
   public boolean fontImagesChatTabComplete;
   public List fontImagesCommandExcluded = List.of();
   public boolean fontImagesScoreboardTeams;
   public boolean textEffectsChat;
   public boolean textEffectsSign;
   public boolean textEffectsBook;
   public boolean textEffectsAnvil;
   public boolean removeUnusedPalettesOnSave;
   public boolean useBlockMaterialInInventory;
   public WorldActivationTracker blockWorlds;
   public int iaImageBookMaxLineLength;
   public String iaImageBookPlaceholderFont;
   public boolean aeCustomEnchantsInItemConfigs;
   public boolean unlockAllRecipesOnJoin;
   public boolean unlockRecipesAutomatically;
   public boolean recipesEnabled;
   public boolean recipesCrafting;
   public boolean recipesCooking;
   public boolean recipesAnvil;
   public boolean recipesSmithing;
   public Integer hudForceColorRgb;
   public boolean cooldownBars;
   public int cooldownBarsRefreshTicks;
   public BarColor cooldownBarColor;
   public BarStyle cooldownBarStyle;
   public boolean fontImagesItemNameLore;
   public boolean textEffectsItemNameLore;
   public boolean customBlockSounds;
   public boolean customBlockSoundsStep;
   public boolean customBlockSoundsNonStep;
   public boolean autoFixPre1212Shaders;
   public boolean field37;
   public boolean cancelBreakWrongToolActionbar;
   public boolean textEffectsEnabled;
   public boolean textEffectsLegacyColors;
   public boolean hideScoreboardNumbersOldClients;
   public boolean hideScoreboardNumbers;
   public boolean playerStatsUsePlayerDat;
   public boolean fixTradeGuiItemsAdderMachines;
   public boolean fixTradeGuiVanillaVillagers;
   public boolean fixTradeGuiOtherPlugins;
   public boolean logPlayerPackInstall;
   public boolean convertFurnitureToItemDisplay;
   public boolean autoUpdateTransformations;
   public boolean kickOnQuickbarSpam;
   public int quickbarSpamWindowMs;
   public int quickbarSpamClicks;
   public boolean migrateLegacyV3Durability;
   public boolean itemAutoUpdate;
   public UpdateMode itemAutoUpdateEnchantmentMode;
   public boolean fixUnstackableItemsOnClick;
   public boolean legacyArmorAppendEquipment;
   public boolean legacyArmorFullConversion;
   public String legacyArmorFullConversionMaterial;
   public boolean convertResourceToGraphics;
   public boolean logCompressionFiles;
   public boolean logCompressionOverlays;
   public boolean logCompressionDuplicateTextures;
   public boolean logScriptCompilation;
   public boolean logScriptExecution;
   public boolean logBonesSpawning;
   public boolean ffmpegEnabled;
   public boolean ffmpegDownload;
   public boolean registryChangeRejoin;
   public boolean registryChangeKick;
   public String registryRejoinAddress;
   public int registryRejoinPort;

   private Settings() {
   }

   public static Settings get() {
      return INSTANCE;
   }

   public static void set(Settings var0) {
      INSTANCE = Objects.requireNonNull(var0, "settings");
   }

   public boolean isLoggingEnabled(String var1) {
      return this.enabledLoggingKeys.contains(var1);
   }

   static Set loadEnabledLoggingKeys(YamlConfig var0) {
      ConfigurationSection var1 = var0.abM().getConfigurationSection("logging");
      if (var1 == null) {
         throw method2943("logging", "expected a section");
      }

      HashSet var2 = new HashSet();
      var1.getValues(true).forEach((var2x, var3) -> {
         if (Boolean.TRUE.equals(var3)) {
            String var4 = "logging." + var2x;
            String var5 = var4;

            while (var5.contains(".")) {
               var5 = var5.substring(0, var5.lastIndexOf(46));
               String var6 = var5 + ".enabled";
               if (var0.contains(var6) && !var0.getBoolean(var6)) {
                  return;
               }
            }

            var2.add(var4);
         }
      });
      return Set.copyOf(var2);
   }

   public static Settings load(YamlConfig var0) {
      Objects.requireNonNull(var0, "config");
      Settings var1 = new Settings();
      var1.enabledLoggingKeys = loadEnabledLoggingKeys(var0);
      var1.guiScrollable = var0.getBoolean("ia_gui_scrollable", true);
      var1.guiScrollableInventoryTitle = var0.getBoolean("ia_gui_scrollable_inventory_title", true);
      var1.logPerformanceBenchmark = var1.isLoggingEnabled("logging.performance.benchmark");
      var1.obfuscateZipEntries = var0.getBoolean("resource_pack.obfuscation.zip_entries");
      var1.obfuscateZipArchive = var0.getBoolean("resource_pack.obfuscation.zip_archive");
      var1.obfuscatePng = var0.getBoolean("resource_pack.obfuscation.png");
      var1.obfuscateShortTextureRefs = var0.getBoolean("resource_pack.obfuscation.short_texture_references");
      var1.dictionariesLang = method2932(var0, "config_files.dictionaries_lang");
      var1.customBlocksMushroom = var0.getBoolean("resource_pack.custom_blocks.mushroom");
      var1.customBlocksChorus = var0.getBoolean("resource_pack.custom_blocks.chorus");
      var1.customBlocksNote = var0.getBoolean("resource_pack.custom_blocks.note");
      var1.customBlocksEnable120States350 = var0.getBoolean("resource_pack.custom_blocks.enable_1_20_states_350");
      var1.customBlocksEnable261States200 = var0.getBoolean("resource_pack.custom_blocks.enable_26_1_states_200");
      var1.customBlocksWire = var0.getBoolean("resource_pack.custom_blocks.wire");
      var1.customBlocksFire = var0.getBoolean("resource_pack.custom_blocks.fire");
      var1.customBlocksEntity = var0.getBoolean("resource_pack.custom_blocks.entity");
      var1.blockEntityRenderRadius = method2933(var0, "blocks.entity.render_radius_blocks", 1);
      var1.blockEntityMaxDisplaysPerPlayer = method2933(var0, "blocks.entity.max_displays_per_player", 9);
      var1.blockEntityMaxBlocksPerChunk = method2933(var0, "blocks.entity.max_blocks_per_chunk", 0);
      var1.blockEntityFallbackMaterial = var0.getMaterial("blocks.entity.fallback_vanilla_block");
      if (!BlockRealizationMode.isRealizableMaterial(var1.blockEntityFallbackMaterial)) {
         throw method2943("blocks.entity.fallback_vanilla_block", "must be a solid, occluding, non-interactable vanilla block without gravity");
      }

      var1.blockEntityUpdateIntervalTicks = method2933(var0, "blocks.entity.update_interval_ticks", 1);
      var1.blockEntityWarmSlotTimeoutTicks = method2933(var0, "blocks.entity.warm_slot_timeout_ticks", 0);
      var1.liquidsEnabled = var0.getBoolean("liquids.enabled");
      var1.fixLiquidsFlow = var0.getBoolean("blocks.fix_liquids_flow");
      var1.fixFloatingBlocks = var0.getBoolean("blocks.fix_floating_blocks");
      var1.compressPng = var0.getBoolean("resource_pack.png.compress");
      var1.hideSlotHighlight = var0.getBoolean("resource_pack.hide_slot_highlight", true);
      var1.inventoryTitle = var0.getString("resource_pack.inventory_title");
      if (var1.guiScrollable || var1.guiScrollableInventoryTitle) {
         var1.inventoryTitle = "";
      }

      var1.compressJson = var0.getBoolean("resource_pack.json.compress");
      var1.compressShaders = var0.getBoolean("resource_pack.shaders.compress");
      var1.validateShaders = var0.getBoolean("resource_pack.shaders.validate");
      var1.compressSounds = var0.getBoolean("resource_pack.sounds.compress");
      var1.soundsQualityPercent = method2934(var0, "resource_pack.sounds.quality_percent", 1, 100);
      method2936(var0, "resource_pack.font.custom.size");
      method2936(var0, "resource_pack.font.custom.oversample");
      method2938(var0, "resource_pack.font.mode", "vanilla", "thin", "custom");
      method2939(var0);
      var1.fixMissingParticle = var0.getBoolean("resource_pack.models.fix_missing_particle");
      var1.fixDarkInInventory = var0.getBoolean("resource_pack.models.fix_dark_in_inventory");
      var1.logNonStandardRotations = var1.isLoggingEnabled("logging.resource_packs.non_standard_rotations");
      var1.fixAtlasItemModels = var0.getBoolean("resource_pack.models.fix_1_21_11_plus_atlas_items_models.enabled");
      var1.forceFixAtlasModels = var0.ka("resource_pack.models.fix_1_21_11_plus_atlas_items_models.force_fix_models")
         .stream()
         .filter(var0x -> !var0x.endsWith(".json"))
         .map(var0x -> var0x.toLowerCase(Locale.ROOT))
         .collect(Collectors.toUnmodifiableSet());
      String var2 = method2932(var0, "pack_delivery.delivery_mode");
      if (!var2.equals("universal") && !var2.equals("per_client_version")) {
         throw method2943("pack_delivery.delivery_mode", "expected universal or per_client_version");
      }

      var1.perClientVersionDelivery = var2.equals("per_client_version");
      var1.soundPacksEnabled = var0.getBoolean("pack_delivery.sound_packs.enabled");
      var1.shaderPacksEnabled = var0.getBoolean("pack_delivery.shader_packs.enabled");
      var1.enabledClientVersions = Arrays.stream(PackFormat.values())
         .map(PackFormat::getKey)
         .filter(var1x -> var0.getBoolean("pack_delivery.per_client_version." + var1x))
         .collect(Collectors.toUnmodifiableSet());
      var1.autoApplyPack = var0.getBoolean("pack_delivery.auto_apply.enabled");
      var1.autoApplyForced = method2937(var0);
      var1.autoApplyDelayTicks = method2933(var0, "pack_delivery.auto_apply.delay_ticks", 0);
      var1.kickOnDecline = var0.getBoolean("pack_delivery.kick_player_on_decline");
      var1.packCommandCooldownSeconds = method2933(var0, "pack_delivery.command_cooldown_seconds", 0);
      loadPackHosting(var0, var1);
      var1.armorOverwriteLeatherLayers = var0.getBoolean("resource_pack.custom_armors_shaders_legacy.vanilla.overwrite_leather_layers");
      var1.armorGenShaderPre1214 = var0.getBoolean("resource_pack.custom_armors_shaders_legacy.vanilla.generate_shader.1_21_4_lower");
      var1.armorGenShader1215Plus = var0.getBoolean("resource_pack.custom_armors_shaders_legacy.vanilla.generate_shader.1_21_5_plus");
      var1.armorGenOptifineCit = var0.getBoolean("resource_pack.custom_armors_shaders_legacy.generate_optifine_cit_1_20_4_lower");
      var1.armorShadersEnabled = var1.armorGenShaderPre1214 || var1.armorGenShader1215Plus || var1.armorGenOptifineCit;
      var1.emoteLegacyShadersPre1214 = var0.getBoolean("resource_pack.custom_entities.emotes.1_21_4_lower_legacy_shaders");
      var1.emoteShaders1215To1219 = var0.getBoolean("resource_pack.custom_entities.emotes.1_21_5_to_1_21_9_shaders");
      var1.emoteModernMethod = var0.getBoolean("resource_pack.custom_entities.emotes.1_21_4_plus_modern_method");
      if (var1.emoteShaders1215To1219 && var1.emoteModernMethod) {
         var1.emoteShaders1215To1219 = false;
      }

      if (var1.emoteModernMethod && !Version.isAtLeast(Version.v1_21_4)) {
         var1.emoteModernMethod = false;
      }

      var1.logArmorsLoading = var1.isLoggingEnabled("logging.armors.loading");
      var1.logFixPacketTooLarge = var1.isLoggingEnabled("logging.recipes.fix_packet_too_large");
      var1.logConvertVanillaBlocks = var1.isLoggingEnabled("logging.blocks.convert_vanilla_blocks");
      var1.logFixBuggedBlocks = var1.isLoggingEnabled("logging.blocks.fix_bugged_blocks");
      var1.logUnusualBlockBreak = var1.isLoggingEnabled("logging.blocks.unusual_block_break");
      var1.logUnknownBlockBreak = var1.isLoggingEnabled("logging.blocks.unknown_block_break");
      var1.logBlockSaveLoad = var1.isLoggingEnabled("logging.blocks.save_load.enabled");
      var1.logBlockSaveLoadMinMs = method2933(var0, "logging.blocks.save_load.min_ms", 0);
      var1.skipUrlFileTypeCheck = var0.getBoolean("pack_hosting.external_host.skip_url_file_type_check");
      var1.logConfigLoading = var1.isLoggingEnabled("logging.config_files.loading");
      var1.logConfigReloading = var1.isLoggingEnabled("logging.config_files.reloading");
      var1.fixPacketTooLargeKick = var0.getBoolean("recipes.fix_packet_too_large_kick");
      var1.customEntitiesEnabled = var0.getBoolean("resource_pack.custom_entities.enabled");
      var1.emotesEnabled = var1.customEntitiesEnabled && var0.getBoolean("resource_pack.custom_entities.emotes.enabled");
      var1.emotesUseArmorStands = var0.getBoolean("resource_pack.custom_entities.emotes.use_armorstands", true) || !var1.emoteModernMethod;
      var1.emotesThirdPersonView = var1.customEntitiesEnabled && var0.getBoolean("custom_entities.emotes.third_person_view");
      var1.emotesMatchPlayerScale = var0.getBoolean("custom_entities.emotes.match_player_scale", true);
      double var3 = var0.getDouble("custom_entities.render_distance", -1.0);
      if (Double.isFinite(var3) && var3 > 0.0) {
         var1.customEntityRenderDistance = var3;
      }

      var1.emotesRagdollOnDeath = var1.emotesEnabled && var1.emoteModernMethod && var0.getBoolean("custom_entities.emotes.ragdoll_on_death");
      var1.emotesDeathScreenDelaySeconds = Math.max(0, Math.min(60, var0.getInt("custom_entities.emotes.death_screen_delay_seconds", 6)));
      var1.emotesAutoRespawn = var0.getBoolean("custom_entities.emotes.auto_respawn", false);
      var1.customEntitiesDelayPackets = var0.getBoolean("custom_entities.delay_packets");
      var1.customEntitiesBundlePackets = var0.getBoolean("custom_entities.bundle_packets", false);
      var1.customEntitiesHumanoidEquipment = var0.getBoolean("custom_entities.humanoid_equipment", true);
      var1.customEntityTerrainFeetViewDistance = method2935(var0, "custom_entities.terrain_feet_view_distance", 0, 128, 10);
      var1.customEntityRagdollNearDetailDistance = method2935(var0, "custom_entities.ragdoll_near_detail_distance", 1, 256, 30);
      var1.disguisesLagCompensation = var0.getBoolean("custom_entities.disguises_lag_compensation");
      var1.customEntitiesMaxPerChunk = method2933(var0, "custom_entities.max_per_chunk", 0);
      var1.cropAvgSecondsPerStage = method2933(var0, "crops.avg_seconds_per_stage", 1);
      var1.cropFovRenderLimit = var0.getBoolean("crops.rendering.fov_render_limit");
      var1.cropCheckerboardPattern = var0.getBoolean("crops.rendering.checkerboard_pattern.enabled");
      var1.cropCheckerboardDistance = method2933(var0, "crops.rendering.checkerboard_pattern.distance", 0);
      var1.cropMaxShownPerPlayer = method2934(var0, "crops.rendering.max_shown_per_player", 200, 1800);
      var1.cropBillboard = var0.getBoolean("crops.rendering.billboard.enabled");
      var1.cropBillboardDistance = method2934(var0, "crops.rendering.billboard.distance", 0, 10);
      var1.cropTickInterval = method2934(var0, "crops.rendering.tick_interval", 1, 10);
      var1.cropRenderRadiusBlocks = method2934(var0, "crops.rendering.radius_blocks", 16, 64);
      var1.cropMaxInArea = method2933(var0, "crops.max_crops_in_area", 0);
      var1.cropAreaLimitRadius = method2934(var0, "crops.area_limit_blocks_radius", 16, 64);
      var1.cropPreciseHitbox = var0.getBoolean("crops.interaction.precise_hitbox_targeting");
      var1.cropContinuousHarvest = var0.getBoolean("crops.interaction.continuous_harvesting_assistance");
      var1.allowSpawnerLootSilkTouch = var0.getBoolean("loots.allow_loots_drop_from_spawners_using_silk_touch");
      var1.fixAttackDamageLore = var0.getBoolean("items.fix_show_attack_and_damage_as_lore");
      var1.logSurfaceDecoratorGen = var1.isLoggingEnabled("logging.world_generation.surface_decorators.generation");
      var1.logCaveDecoratorGen = var1.isLoggingEnabled("logging.world_generation.cave_decorators.generation");
      var1.logBlocksPopulatorGen = var1.isLoggingEnabled("logging.world_generation.blocks_populators.generation");
      var1.logTreesPopulatorGen = var1.isLoggingEnabled("logging.world_generation.trees_populators.generation");
      String var5 = method2938(var0, "blocks.unregistered_blockstates.action", "restore_vanilla", "register_as_custom", "none");
      var1.unregBlockstatesRegisterAsCustom = var5.equals("register_as_custom");
      var1.unregBlockstatesRestoreVanilla = var5.equals("restore_vanilla");
      var1.unregBlockstatesOnlyNewChunks = var0.getBoolean("blocks.unregistered_blockstates.only_new_chunks");
      var1.unregBlockstatesOnlyNewChunksAlias = var1.unregBlockstatesOnlyNewChunks;
      var1.field36 = var0.getBoolean("blocks.fix_reverted_custom_blockstates.enabled");
      var1.recoverLegacyBlockPositions = var0.getBoolean("blocks.recover_legacy_block_positions.enabled");
      var1.contentsFoldersPriority = method2942(var0.jZ("resource_pack.contents_folders_priority"));
      var1.mergedResourcepackFolders = var0.getBoolean("resource_pack.merge_other_plugins_resourcepacks_folders.enabled")
         ? method2942(var0.jZ("resource_pack.merge_other_plugins_resourcepacks_folders.list"))
         : List.of();
      var1.packDeliveryUuid = method2932(var0, "pack_delivery.uuid");
      var1.showFallbackArmorOnPackNotLoaded = var0.getBoolean("pack_delivery.show_fallback_armor_on_pack_not_loaded");

      try {
         UUID.fromString(var1.packDeliveryUuid);
      } catch (IllegalArgumentException var11) {
         throw method2944("pack_delivery.uuid", "expected a UUID", var11);
      }

      var1.unlockAllRecipesOnJoin = var0.getBoolean("recipes.custom_recipes.unlock.all_on_join");
      var1.unlockRecipesAutomatically = var0.getBoolean("recipes.custom_recipes.unlock.automatically");
      var1.extractInternalUtilityAssets = var0.getBoolean("resource_pack.extract_internal_utility_assets");
      var1.protectLockUsingEntity = var0.getBoolean("pack_delivery.protect_player.movement.lock_using_entity");
      var1.protectCancelCommands = var0.getBoolean("pack_delivery.protect_player.cancel_commands");
      var1.protectCancelTeleport = var0.getBoolean("pack_delivery.protect_player.cancel_teleport");
      var1.protectCancelMovementEvents = var0.getBoolean("pack_delivery.protect_player.movement.cancel_events");
      String var6 = method2938(var0, "recipes.no_permission_message", "chat", "recipebook", "both", "none");
      var1.recipeNoPermMsgRecipebook = var6.equals("recipebook") || var6.equals("both");
      var1.recipeNoPermMsgChat = var6.equals("chat") || var6.equals("both");
      var1.cacheFontImageReplacements = var0.getBoolean("cache_font_images_and_effects_replacements") && !Main.isPluginEnabled("InteractiveChat");
      var1.fontImagesChat = var0.getBoolean("font_images.chat.enabled");
      var1.fontImagesChatHoverSuggestion = var0.getBoolean("font_images.chat.add_hover_suggestion");
      var1.fontImagesSign = var0.getBoolean("font_images.sign.enabled");
      var1.fontImagesBook = var0.getBoolean("font_images.book.enabled");
      var1.fontImagesAnvil = var0.getBoolean("font_images.anvil.enabled");
      var1.fontImagesInventoryTitle = var0.getBoolean("font_images.inventory_title.enabled");
      var1.fontImagesCommandBlocks = var0.getBoolean("font_images.command.commandblocks");
      var1.fontImagesCommandExcluded = List.copyOf(var0.jZ("font_images.command.excluded"));
      var1.fontImagesScoreboardTeams = var0.getBoolean("font_images.scoreboard_teams.enabled");
      method2933(var0, "font_images.scoreboard_teams.update_every_ticks", 1);
      var1.fontImagesChatTabComplete = var0.getBoolean("font_images.chat.tab_autocompletion");
      var1.textEffectsEnabled = var0.getBoolean("resource_pack.text_effects.enabled");
      var1.textEffectsLegacyColors = var0.getBoolean("resource_pack.text_effects.legacy_colors_compatibility");
      var1.textEffectsChat = var1.textEffectsEnabled && var0.getBoolean("text_effects.chat.enabled");
      var1.textEffectsSign = var1.textEffectsEnabled && var0.getBoolean("text_effects.sign.enabled");
      var1.textEffectsBook = var1.textEffectsEnabled && var0.getBoolean("text_effects.book.enabled");
      var1.textEffectsAnvil = var1.textEffectsEnabled && var0.getBoolean("text_effects.anvil.enabled");
      var1.textEffectsItemNameLore = var1.textEffectsEnabled && var0.getBoolean("text_effects.customitem_name_and_lore.enabled");
      var1.fontImagesItemNameLore = var0.getBoolean("font_images.customitem_name_and_lore.enabled");
      var1.hideScoreboardNumbersOldClients = var0.getBoolean("resource_pack.scoreboard.hide_numbers_old_clients");
      var1.hideScoreboardNumbers = var0.getBoolean("scoreboard.hide_numbers");
      var1.removeUnusedPalettesOnSave = var0.getBoolean("blocks.remove_unused_palettes_on_region_save");
      var1.useBlockMaterialInInventory = var0.getBoolean("resource_pack.custom_blocks.use_block_material_in_inventory");
      var1.blockWorlds = new WorldActivationTracker(List.copyOf(var0.jZ("blocks.worlds")));
      var1.iaImageBookMaxLineLength = method2933(var0, "font_images.iaimage_book.max_line_length", 1);
      var1.iaImageBookPlaceholderFont = method2932(var0, "font_images.iaimage_book.placeholder_font");
      var1.fontImagesWithForceUnicode = var0.getBoolean("resource_pack.enable_font_images_with_force_unicode");
      var1.allowOtherPluginsResourcepacks = var0.getBoolean("pack_delivery.allow_other_plugins_resourcepacks");
      var1.notifyOtherPluginsResourcepacks = var0.getBoolean("pack_delivery.notify_other_plugins_resourcepacks");
      var1.aeCustomEnchantsInItemConfigs = var0.getBoolean("compatibility.advanced_enchantments.enable_custom_enchants_in_items_configs");
      var1.recipesCrafting = var0.getBoolean("recipes.crafting.enabled");
      var1.recipesCooking = var0.getBoolean("recipes.cooking.enabled");
      var1.recipesAnvil = var0.getBoolean("recipes.anvil.enabled");
      var1.recipesSmithing = var0.getBoolean("recipes.smithing.enabled");
      var1.recipesEnabled = var1.recipesCrafting || var1.recipesCooking || var1.recipesAnvil || var1.recipesSmithing;
      if (var0.getBoolean("huds.enabled") && var0.getBoolean("huds.force_color.enabled")) {
         String var7 = method2932(var0, "huds.force_color.color");
         if (!var7.matches("#[0-9a-fA-F]{6}")) {
            throw method2943("huds.force_color.color", "expected #RRGGBB");
         }

         var1.hudForceColorRgb = Color.fromRGB(Integer.parseInt(var7.substring(1), 16)).asRGB();
      }

      var1.cooldownBars = var0.getBoolean("cooldown_bars.enabled");
      var1.cooldownBarsRefreshTicks = method2933(var0, "cooldown_bars.refresh_ticks", 1);
      var1.cooldownBarColor = (BarColor)method2941(var0, "cooldown_bars.bossbar.color", BarColor.class);
      var1.cooldownBarStyle = (BarStyle)method2941(var0, "cooldown_bars.bossbar.style", BarStyle.class);
      var1.customBlockSounds = var0.getBoolean("resource_pack.custom_block_sounds.enabled");
      var1.customBlockSoundsStep = var0.getBoolean("resource_pack.custom_block_sounds.step");
      var1.customBlockSoundsNonStep = var0.getBoolean("resource_pack.custom_block_sounds.non_step");
      var1.autoFixPre1212Shaders = var0.getBoolean("resource_pack.auto_fix_pre_1_21_2_shaders");
      var1.field37 = var0.getBoolean("blocks.cancel_break_wrong_tool.enabled");
      var1.cancelBreakWrongToolActionbar = var0.getBoolean("blocks.cancel_break_wrong_tool.send_actionbar");
      String var12 = method2932(var0, "player_stats.save_type");
      if (!var12.equals("CUSTOM_NBT") && !var12.equals("PLAYER_DAT")) {
         throw method2943("player_stats.save_type", "expected CUSTOM_NBT or PLAYER_DAT");
      }

      var1.playerStatsUsePlayerDat = var12.equals("PLAYER_DAT");
      var1.fixTradeGuiItemsAdderMachines = var0.getBoolean("items.fix_trades_guis_match_custom_items.itemsadder_trade_machines");
      var1.fixTradeGuiVanillaVillagers = var0.getBoolean("items.fix_trades_guis_match_custom_items.vanilla_villagers");
      var1.fixTradeGuiOtherPlugins = var0.getBoolean("items.fix_trades_guis_match_custom_items.other_plugins");
      var1.logPlayerPackInstall = var1.isLoggingEnabled("logging.resource_packs.player_install");
      var1.convertFurnitureToItemDisplay = var0.getBoolean("resource_pack.content_conversion.furniture_to_item_display");
      var1.autoUpdateTransformations = var0.getBoolean("resource_pack.content_conversion.auto_update_transformations");
      var1.kickOnQuickbarSpam = var0.getBoolean("items.optimizations.kick_on_quickbar_spam.enabled");
      var1.quickbarSpamWindowMs = method2933(var0, "items.optimizations.kick_on_quickbar_spam.window_ms", 1);
      var1.quickbarSpamClicks = method2933(var0, "items.optimizations.kick_on_quickbar_spam.clicks", 1);
      var1.migrateLegacyV3Durability = var0.getBoolean("items.migrate_legacy_v3_durability");
      var1.itemAutoUpdate = var0.getBoolean("items.auto_update.enabled");
      var1.itemAutoUpdateEnchantmentMode = (UpdateMode)method2941(var0, "items.auto_update.enchantment_mode", UpdateMode.class);
      var1.fixUnstackableItemsOnClick = var0.getBoolean("items.auto_update.fix_unstackable_items_on_click");
      var1.legacyArmorAppendEquipment = var0.getBoolean("resource_pack.content_conversion.legacy_armor.append_equipment");
      var1.legacyArmorFullConversion = var0.getBoolean("resource_pack.content_conversion.legacy_armor.full_conversion.enabled");
      var1.legacyArmorFullConversionMaterial = method2932(var0, "resource_pack.content_conversion.legacy_armor.full_conversion.material");
      if (var1.legacyArmorFullConversion && Material.matchMaterial(var1.legacyArmorFullConversionMaterial + "_HELMET") == null) {
         throw method2943("resource_pack.content_conversion.legacy_armor.full_conversion.material", "expected an armor material such as IRON or DIAMOND");
      }

      if (var1.legacyArmorAppendEquipment && var1.legacyArmorFullConversion) {
         throw method2943("resource_pack.content_conversion.legacy_armor", "append_equipment and full_conversion.enabled cannot both be enabled");
      }

      var1.convertResourceToGraphics = var0.getBoolean("resource_pack.content_conversion.resource_to_graphics");
      var1.logCompressionFiles = var1.isLoggingEnabled("logging.resource_packs.compression.files");
      var1.logCompressionOverlays = var1.isLoggingEnabled("logging.resource_packs.compression.overlays");
      var1.logCompressionDuplicateTextures = var1.isLoggingEnabled("logging.resource_packs.compression.duplicate_textures");
      var1.logScriptCompilation = var1.isLoggingEnabled("logging.scripts.compilation");
      var1.logScriptExecution = var1.isLoggingEnabled("logging.scripts.execution");
      var1.logBonesSpawning = var1.isLoggingEnabled("logging.entities.bones_spawning");
      var1.ffmpegEnabled = var0.getBoolean("resource_pack.ffmpeg.enabled");
      var1.ffmpegDownload = var0.getBoolean("resource_pack.ffmpeg.download");
      String var8 = method2938(var0, "registry_changes.action", "rejoin", "kick", "none");
      var1.registryChangeRejoin = var8.equals("rejoin");
      var1.registryChangeKick = var8.equals("kick");
      String var9 = Bukkit.getIp();
      if (var9.isEmpty()) {
         var9 = "127.0.1.1";
      }

      var1.registryRejoinAddress = method2932(var0, "registry_changes.rejoin.address");
      if (var1.registryRejoinAddress.equals("auto")) {
         var1.registryRejoinAddress = var9;
      }

      var1.registryRejoinAddress = var1.registryRejoinAddress.replaceFirst("^https?://", "");
      if (var1.registryRejoinAddress.isBlank()) {
         throw method2943("registry_changes.rejoin.address", "must not be blank");
      }

      String var10 = method2932(var0, "registry_changes.rejoin.port");
      var1.registryRejoinPort = var10.equals("auto") ? Bukkit.getPort() : method2940("registry_changes.rejoin.port", var10);
      return var1;
   }

   private static void loadPackHosting(YamlConfig var0, Settings var1) {
      String var2 = method2932(var0, "pack_hosting.method").toLowerCase(Locale.ROOT);

      var1.packHostingMode = switch (var2) {
         case "none" -> PackHostingMode.NO_HOST;
         case "simple_self_host" -> PackHostingMode.SIMPLE_SELF_HOST;
         case "self_host" -> PackHostingMode.SELF_HOST;
         case "external_host" -> PackHostingMode.EXTERNAL;
         case "lobfile" -> Version.isAtLeast(Version.v1_17_R1) ? PackHostingMode.LOBFILE : PackHostingMode.NO_HOST;
         case "auto_external_legacy" -> PackHostingMode.AUTO_EXTERNAL_LEGACY;
         default -> throw method2943("pack_hosting.method", "expected none, simple_self_host, self_host, external_host, lobfile, or auto_external_legacy");
      };
      var1.simpleSelfHost = var1.packHostingMode == PackHostingMode.SIMPLE_SELF_HOST;
      var1.simpleSelfHostDef = method2928(var0, "simple_self_host");
      var1.selfHostDef = method2928(var0, "self_host");
      String var8 = Bukkit.getIp();
      if (var8.isEmpty()) {
         var8 = "127.0.1.1";
      }

      String var9 = method2932(var0, "pack_hosting.simple_self_host.server_address");
      if (var9.equals("auto")) {
         var9 = var8 + ":" + Bukkit.getPort();
      }

      var9 = var9.replaceFirst("^https?://", "").replaceFirst("/+$", "");
      if (var9.isBlank()) {
         throw method2943("pack_hosting.simple_self_host.server_address", "must not be blank");
      }

      if (!var9.endsWith("/resourcepack")) {
         var9 = var9 + "/resourcepack";
      }

      var1.simpleSelfHostUrl = "http://" + var9;
      if (var1.packHostingMode == PackHostingMode.EXTERNAL) {
         String var5 = method2932(var0, "pack_hosting.external_host.url");

         try {
            URI var6 = new URI(var5);
            if (var6.getHost() == null || !"http".equalsIgnoreCase(var6.getScheme()) && !"https".equalsIgnoreCase(var6.getScheme())) {
               throw method2943("pack_hosting.external_host.url", "expected an absolute HTTP(S) URL");
            }
         } catch (URISyntaxException var7) {
            throw method2944("pack_hosting.external_host.url", "expected an absolute HTTP(S) URL", var7);
         }
      }

      int var11 = method2934(var0, "pack_hosting.self_host.pack_port", 1, 65535);
      if (var1.packHostingMode == PackHostingMode.SELF_HOST) {
         String var12 = method2932(var0, "pack_hosting.self_host.server_ip");
         if (var12.isBlank()) {
            throw method2943("pack_hosting.self_host.server_ip", "must not be blank");
         }

         if (var0.getBoolean("pack_hosting.self_host.append_port")) {
            var12 = var12 + ":" + var11;
         }

         if (!var12.startsWith("http://") && !var12.startsWith("https://")) {
            var12 = "http://" + var12;
         }

         var1.selfHostPackUrl = var12.replaceFirst("/+$", "") + "/generated.zip";
      }
   }

   static DownloadPolicy method2928(YamlConfig var0, String var1) {
      String var2 = "pack_hosting." + var1 + ".protection";

      for (String var4 : List.of(var2, var2 + ".rate_limit", var2 + ".rate_limit.cooldown")) {
         if (var0.contains(var4) && !var0.abM().isConfigurationSection(var4)) {
            throw method2943(var4, "expected a section");
         }
      }

      boolean var5 = method2930(var0, var1, "rate_limit.cooldown.enabled");
      return new DownloadPolicy(
         method2930(var0, var1, "require_minecraft_headers"),
         method2930(var0, var1, "rate_limit.enabled"),
         method2931(var0, var1, "rate_limit.max_requests", 1),
         method2931(var0, var1, "rate_limit.period_seconds", 1),
         var5,
         method2931(var0, var1, "rate_limit.cooldown.after_rejections", 1),
         method2931(var0, var1, "rate_limit.cooldown.duration_minutes", var5 ? 1 : 0),
         method2931(var0, var1, "max_concurrent_downloads_per_ip", 1),
         method2931(var0, var1, "max_speed_mib_per_second", 1)
      );
   }

   private static String method2929(YamlConfig var0, String var1, String var2) {
      String var3 = "pack_hosting." + var1 + ".protection." + var2;
      return var0.contains(var3) ? var3 : "pack_hosting.protection." + var2;
   }

   private static boolean method2930(YamlConfig var0, String var1, String var2) {
      String var3 = method2929(var0, var1, var2);
      if (var0.abM().get(var3) instanceof Boolean var5) {
         return var5;
      } else {
         throw method2943(var3, "expected a boolean");
      }
   }

   private static int method2931(YamlConfig var0, String var1, String var2, int var3) {
      String var4 = method2929(var0, var1, var2);
      if (var0.abM().get(var4) instanceof Integer var6) {
         if (var6 < var3) {
            throw method2943(var4, "must be at least " + var3);
         } else {
            return var6;
         }
      } else {
         throw method2943(var4, "expected a 32-bit integer");
      }
   }

   private static String method2932(YamlConfig var0, String var1) {
      String var2 = var0.getString(var1);
      if (var2 == null) {
         throw method2943(var1, "is missing");
      } else {
         return var2;
      }
   }

   private static int method2933(YamlConfig var0, String var1, int var2) {
      int var3 = var0.getInt(var1);
      if (var3 < var2) {
         throw method2943(var1, "must be at least " + var2);
      } else {
         return var3;
      }
   }

   private static int method2934(YamlConfig var0, String var1, int var2, int var3) {
      int var4 = var0.getInt(var1);
      if (var4 >= var2 && var4 <= var3) {
         return var4;
      } else {
         throw method2943(var1, "must be between " + var2 + " and " + var3);
      }
   }

   private static int method2935(YamlConfig var0, String var1, int var2, int var3, int var4) {
      int var5 = var0.getInt(var1, var4);
      if (var5 >= var2 && var5 <= var3) {
         return var5;
      } else {
         throw method2943(var1, "must be between " + var2 + " and " + var3);
      }
   }

   private static double method2936(YamlConfig var0, String var1) {
      double var2 = var0.getDouble(var1, Double.NaN);
      if (Double.isFinite(var2) && !(var2 <= 0.0)) {
         return var2;
      } else {
         throw method2943(var1, "must be a finite number greater than zero");
      }
   }

   static boolean method2937(YamlConfig var0) {
      return var0.getBoolean("pack_delivery.auto_apply.enabled") && var0.getBoolean("pack_delivery.auto_apply.before_join");
   }

   private static String method2938(YamlConfig var0, String var1, String... var2) {
      String var3 = method2932(var0, var1);
      if (!Arrays.asList(var2).contains(var3)) {
         throw method2943(var1, "expected one of " + String.join(", ", var2));
      } else {
         return var3;
      }
   }

   private static void method2939(YamlConfig var0) {
      if (var0.method7053("resource_pack.font.custom.shift", null) instanceof List var2 && var2.size() == 2) {
         Iterator var4 = var2.iterator();

         while (var4.hasNext()) {
            if (!(var4.next() instanceof Number var3) || !Double.isFinite(var3.doubleValue())) {
               throw method2943("resource_pack.font.custom.shift", "expected a YAML list of two finite numbers");
            }
         }
      } else {
         throw method2943("resource_pack.font.custom.shift", "expected a YAML list of two finite numbers");
      }
   }

   private static int method2940(String var0, String var1) {
      try {
         int var2 = Integer.parseInt(var1);
         if (var2 >= 1 && var2 <= 65535) {
            return var2;
         } else {
            throw method2943(var0, "must be between 1 and 65535");
         }
      } catch (NumberFormatException var3) {
         throw method2944(var0, "expected auto or a port between 1 and 65535", var3);
      }
   }

   private static Enum method2941(YamlConfig var0, String var1, Class var2) {
      String var3 = method2932(var0, var1);

      try {
         return Enum.valueOf(var2, var3.toUpperCase(Locale.ROOT));
      } catch (IllegalArgumentException var5) {
         throw method2944(var1, "expected one of " + Arrays.toString(var2.getEnumConstants()), var5);
      }
   }

   private static List method2942(List var0) {
      ArrayList var1 = new ArrayList(var0);
      Collections.reverse(var1);
      return List.copyOf(var1);
   }

   private static IllegalArgumentException method2943(String var0, String var1) {
      return new IllegalArgumentException("Invalid config.yml value at '" + var0 + "': " + var1);
   }

   private static IllegalArgumentException method2944(String var0, String var1, Exception var2) {
      return new IllegalArgumentException("Invalid config.yml value at '" + var0 + "': " + var1, var2);
   }

   public static boolean bI() {
      Settings var0 = get();
      return var0.textEffectsEnabled || var0.hideScoreboardNumbersOldClients;
   }

   public static boolean bJ() {
      Settings var0 = get();
      return var0.emoteLegacyShadersPre1214 && var0.emotesEnabled;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static boolean method2945(BlockRealizationMode var0) {
      Settings var1 = get();

      return switch (SwitchMap37.hR[var0.ordinal()]) {
         case 1 -> var1.customBlocksMushroom;
         case 2 -> var1.customBlocksChorus;
         case 3 -> var1.customBlocksNote;
         case 4 -> var1.customBlocksWire;
         case 5 -> var1.customBlocksFire;
         case 6 -> true;
         case 7 -> var1.customBlocksEntity;
         default -> throw new MatchException(null, null);
      };
   }

   public static boolean bK() {
      Settings var0 = get();
      return var0.customBlocksMushroom
         || var0.customBlocksChorus
         || var0.customBlocksNote
         || var0.customBlocksWire
         || var0.customBlocksFire
         || var0.customBlocksEntity;
   }
}
