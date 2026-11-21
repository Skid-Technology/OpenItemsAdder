package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.NamespacedIdentityImpl;
import dev.lone.itemsadder.entity.BoneHierarchy;
import dev.lone.itemsadder.entity.CustomEntityDefinition;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.RotationPolicy;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemSpecWithTexture;
import dev.lone.itemsadder.item.TexturedItemBase;
import dev.lone.itemsadder.item.TexturedItemDefinition;
import dev.lone.itemsadder.lang.MinecraftLocales;
import dev.lone.itemsadder.loot.LootTableLoader;
import dev.lone.itemsadder.util.BossBarManager;
import dev.lone.itemsadder.util.DoubleCallback;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public final class ResourcePackGenerationTask {
   private final Plugin plugin;
   private final ResourcePackService service;

   ResourcePackGenerationTask(Plugin var1, ResourcePackService var2) {
      this.plugin = var1;
      this.service = var2;
   }

   void finishAndAutoApply(boolean var1) {
      this.service.generator.generated = true;
      if (!var1 && Settings.get().autoApplyForced) {
         for (Player var3 : Bukkit.getOnlinePlayers()) {
            if (var3.isOnline() && this.service.getPlayerStatus(var3) != PackPushStatus.INSTALLED) {
               this.service.delivery.sendPackTo(var3);
            }
         }
      }
   }

   boolean startGeneration(
      HashMap var1,
      HashMap var2,
      boolean var3,
      HashMap var4,
      MinecraftLocales var5,
      boolean var6,
      @Nullable Runnable var7,
      @Nullable Consumer var8,
      @Nullable DoubleCallback var9,
      @Nullable Player var10,
      @Nullable ResourcePackLoadState var11
   ) {
      boolean var12 = var11 != null;
      ResourcePackLoadState var13 = var11;
      if (var13 != null) {
         if (!var13.isActive()) {
            throw new IllegalStateException("Resource-pack operation was not reserved");
         }
      } else {
         var13 = ContentManager.method8084().method8137();
         if (var13 == null) {
            return false;
         }
      }

      GenerationSuspension var14 = new GenerationSuspension(this, var13, var8);
      var14.dR = () -> {
         ContentManager var6x = ContentManager.method8084();
         var6x.method8103(false, true, var14.dO, null).whenComplete((var7x, var8x) -> {
            if (!var14.dQ.get() && var14.dO.isActive()) {
               if (var8x != null) {
                  Log.error("Could not rebuild resource-pack inputs after vanilla repair.", var8x);
                  var14.completeWith(false);
               } else {
                  var14.dQ.set(true);

                  try {
                     var6x.method8135(var6, var7, var8, var9, var10, var14.dO);
                  } catch (RuntimeException | Error var10x) {
                     var14.dQ.set(false);
                     Log.error("Resource-pack retry failed. The previous pack was preserved.", var10x);
                     var14.completeWith(false);
                  }
               }
            }
         });
      };

      try {
         if (!this.service.delivery.canRunCommand(var10)) {
            BossBarManager.clear();
            var14.scheduleCompletion(false);
            return false;
         } else {
            boolean var15 = this.isModelEnginePostServerEnabled();
            if (shouldSkipStartup(var12, var15)) {
               Log.error("[Pack] Automatic startup generation skipped because ModelEngine is configured to regenerate its pack after server startup.");
               Log.error("[Pack] Wait for ModelEngine to finish and run /iazip, or disable its three 'Post-Server' flags and restart.");
               this.service.generator.generated = false;
               BossBarManager.clear();
               var14.scheduleCompletion(false);
               return false;
            } else {
               VanillaAssetManager var16 = ContentManager.method8084().field91;
               Runnable var17 = () -> {
                  var16.rethrowFailure();
                  this.runGenerationPipeline(var1, var2, var3, var4, var5, var6, var7, var14, var9, var10);
               };
               LinkedHashSet var18 = new LinkedHashSet(var1.keySet());
               var18.add(Material.PAPER);
               CustomEntityManager.getInstance().oN.values().forEach(var1x -> var1x.vn.AL.values().forEach(var1xx -> {
                  if (var1xx.Ar) {
                     var18.add(var1xx.field115 ? Material.PLAYER_HEAD : Material.POTION);
                  }
               }));
               ArrayList var19 = new ArrayList();
               boolean var20 = var1.getOrDefault(Material.TRIDENT, List.of()).stream().anyMatch(var0 -> !var0.DI() && var0.nz() >= 0);
               var2.values().forEach(var1x -> {
                  if (var1x.anZ == null && var1x.nB()) {
                     var19.add(new TexturedItemBase(var1x.getType(), var1x.aoi, var1x.aoj, var1x));
                  }
               });
               SchedulerUtils.runSync(() -> {
                  try {
                     var16.getCommonAssets();

                     for (Material var8x : var18) {
                        ModelJsonAsset.resolveForMaterial(var8x, VanillaAssetManager.io, true);
                        if (var8x == Material.TRIDENT && var20) {
                           ModelJsonAsset.forNameVersion("trident_in_hand", VanillaAssetManager.io);
                        }
                     }

                     for (TexturedItemBase var11x : var19) {
                        var11x.DO();
                        if (var11x.apf.isBlock() && !(var11x.ape instanceof CustomBlockDefinition) && !var11x.getTextures().isEmpty()) {
                           ModelJsonAsset.forMaterial(var11x.apf);
                        }
                     }
                  } catch (RuntimeException var9x) {
                     Log.error("Could not prepare vanilla assets for resource-pack generation. The previous pack was preserved.", var9x);
                     var14.scheduleCompletion(false);
                     return;
                  }

                  SchedulerUtils.runOnMainThread(() -> {
                     if (!var14.dQ.get() && var14.dO.isActive()) {
                        try {
                           var17.run();
                        } catch (RuntimeException | Error var4xx) {
                           if (!this.tryRecoverFromFailure(var14, var4xx)) {
                              Log.error("Resource-pack generation failed. The previous pack was preserved.", var4xx);
                           }
                        }
                     }
                  });
               });
               return true;
            }
         }
      } catch (RuntimeException | Error var21) {
         if (this.tryRecoverFromFailure(var14, var21)) {
            return true;
         } else {
            throw var21;
         }
      }
   }

   private boolean tryRecoverFromFailure(GenerationSuspension var1, Throwable var2) {
      try {
         this.service.resetAfterFailure();
      } catch (Throwable var4) {
         var2.addSuppressed(var4);
      }

      BossBarManager.clear();
      if (var2 instanceof VanillaAssetException && var1.tryRepairVanillaAssets()) {
         return true;
      }

      var1.scheduleCompletion(false);
      return false;
   }

   private void runGenerationPipeline(
      HashMap var1,
      HashMap var2,
      boolean var3,
      HashMap var4,
      MinecraftLocales var5,
      boolean var6,
      @Nullable Runnable var7,
      GenerationSuspension var8,
      @Nullable DoubleCallback var9,
      @Nullable Player var10
   ) {
      if (var7 != null) {
         var7.run();
      }

      Log.log(" ");
      Log.log(" ");
      Log.logToSender(var10, ChatColor.GREEN + "[Pack] Started generation");
      BossBarManager.setTitleKeepProgress("Handling entities bones...");
      HashMap var11 = new HashMap();
      var1.forEach(
         (var2x, var3x) -> {
            var11.put(var2x, new ArrayList(var3x));

            for (CustomItemDefinition var5x : var3x) {
               Map var6x = ModelTransformUtils.buildFurnitureModels(var5x);
               if (!var6x.isEmpty()) {
                  String[] var7x = var5x.getNamespacedID().split(":", 2);

                  for (Entry var9x : var6x.entrySet()) {
                     BlockFace var10x = (BlockFace)var9x.getKey();
                     ModelDefinition var11x = (ModelDefinition)var9x.getValue();
                     String var12x = var7x[0] + ":ia_auto/furniture/" + var7x[1] + (var10x == BlockFace.UP ? "" : "_" + var10x.name().toLowerCase(Locale.ROOT));
                     if (var11x.bHo.equals(var12x)) {
                        this.service.generator.manifestJsons.put(var11x.bHp, var11x.bHq);
                     }

                     String var13 = "assets/"
                        + var7x[0]
                        + "/items/ia_auto/furniture/"
                        + var7x[1]
                        + (var10x == BlockFace.UP ? "" : "_" + var10x.name().toLowerCase(Locale.ROOT))
                        + ".json";
                     JSONObject var14x = new JSONObject()
                        .fluentPut("model", new JSONObject().fluentPut("type", "minecraft:model").fluentPut("model", var11x.bHo));
                     this.service.generator.furnitureItemJsons.put(var13, var14x);
                  }
               }
            }
         }
      );
      CustomEntityManager.getInstance()
         .oN
         .forEach(
            (var1x, var2x) -> var2x.vn
               .AL
               .forEach(
                  (var1xx, var2xx) -> {
                     if (var2xx.Ar) {
                        Material var3x = var2xx.field115 ? Material.PLAYER_HEAD : Material.POTION;
                        if (!var11.containsKey(var3x)) {
                           var11.put(var3x, new ArrayList());
                        }

                        List var4x = (List)var11.get(var3x);
                        if (!var2xx.nJ()) {
                           if (var2xx.field115) {
                              var4x.add(new NamespacedIdentityImpl(var2xx.nz(), CustomEntityDefinition.aX(var2xx.getId()), "_iainternal", var2xx.getId()));
                           } else {
                              var4x.add(var2xx);
                           }

                           for (int var14x = 1; var14x < var2xx.nK(); var14x++) {
                              String var15 = var2xx.method8743(var14x);
                              if (var15 != null) {
                                 var4x.add(
                                    new NamespacedIdentityImpl(
                                       var2xx.method8742(var14x), var15, var2xx.getNamespace(), var2xx.getId() + "_texture_variant_" + var14x
                                    )
                                 );
                              }
                           }

                           var2xx.nA()
                              .forEach(
                                 (var2xxx, var3xx) -> {
                                    if (!var2xxx.equals("1-1-1")) {
                                       if (var2xx.field115 || !var2xxx.equals("0-0-0")) {
                                          var4x.add(
                                             new NamespacedIdentityImpl(
                                                var3xx, var2xx.nC() + "_" + var2xxx, var2xx.getNamespace(), var2xx.getId() + "_" + var2xxx.replace(".", ",")
                                             )
                                          );
                                       }
                                    }
                                 }
                              );
                        } else {
                           for (RotationPolicy var8x : RotationPolicy.values()) {
                              BoneHierarchy[] var9x = var2xx.method8741(var8x);

                              for (int var10x = 0; var10x < var9x.length; var10x++) {
                                 BoneHierarchy var11x = var9x[var10x];

                                 for (int var12x = 0; var12x < var11x.getBoneCount(); var12x++) {
                                    String var13 = var11x.getBoneName(var12x);
                                    if (var13 != null) {
                                       var4x.add(
                                          new NamespacedIdentityImpl(
                                             var11x.getParentOf(var12x),
                                             var13,
                                             var2xx.getNamespace(),
                                             var2xx.getId() + "_" + var8x.getKey() + "_" + var10x + "_" + var12x
                                          )
                                       );
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               )
         );
      BossBarManager.setTitleKeepProgress("Generating predicates...");
      this.service.generator.registerScrollGuiItems(var11);
      this.service.generator.registerMaterialNames(var11);
      BossBarManager.setTitleKeepProgress("Generating models...");
      ArrayList var12 = new ArrayList();
      var2.forEach((var1x, var2x) -> {
         if (var2x.anZ == null) {
            if (var2x.Du() != null) {
               TexturedItemDefinition var3x = new TexturedItemDefinition(Material.PAPER, var2x.aoi, var2x.aoj + "_icon", var2x, var2x.Du());
               var2x.cM(var3x.buildTexturePath(false, true));
               var12.add(var3x);
            }

            var12.add(new TexturedItemBase(var2x.getType(), var2x.aoi, var2x.aoj, var2x));
         }
      });
      this.service.generator.buildItemManifests(var12);
      BossBarManager.setTitleKeepProgress("Generating modern models...");
      var2.forEach((var1x, var2x) -> {
         try {
            this.service.generator.generateEntityBlockModels(var2x);
         } catch (Exception var4x) {
            if (!(var4x instanceof VanillaAssetException)) {
               Log.error("Error", var4x);
            }
         }
      });
      ContentManager.method8084().field91.rethrowFailure();
      if (var3) {
         for (ItemSpecWithTexture var14 : LootTableLoader.FS()) {
            this.service.generator.registerItemModels(var14);
         }
      }

      BossBarManager.setTitleKeepProgress("Generating block models...");
      this.service.generator.buildBlockstateOverlays(var2);
      BossBarManager.setTitleKeepProgress("Generating font images...");
      this.service.generator.buildPackArrays(var4);
      SchedulerUtils.runLaterSync(() -> {
         if (!var8.dO.isActive()) {
            var8.abortIfNotCompleted();
         } else {
            try {
               this.runLegacyArmorStage(var5, var6, var8, var9, var10);
            } catch (RuntimeException | Error var7x) {
               this.handleGenerationFailure(var8, var7x);
            }
         }
      }, 1L);
   }

   private void runLegacyArmorStage(MinecraftLocales var1, boolean var2, GenerationSuspension var3, @Nullable DoubleCallback var4, @Nullable Player var5) {
      BossBarManager.setTitleKeepProgress("Generating legacy armors...");
      this.service.generator.generateArmorTextures();
      this.service.generator.bAo = var1.HC();
      if (Settings.get().compressPng) {
         BossBarManager.setTitleKeepProgress("Compressing images...");
         SchedulerUtils.runSync(() -> {
            try {
               this.service.generator.warnDuplicateImages(var2);
               var3.runOnServerThread(() -> {
                  if (!var3.dO.isActive()) {
                     var3.abortIfNotCompleted();
                  } else {
                     try {
                        this.runPackagingStage(var3, var4, var5);
                     } catch (RuntimeException | Error var5x) {
                        this.handleGenerationFailure(var3, var5x);
                     }
                  }
               });
            } catch (RuntimeException | Error var6) {
               var3.runOnServerThread(() -> this.handleGenerationFailure(var3, var6));
            }
         });
      } else {
         this.runPackagingStage(var3, var4, var5);
      }
   }

   private void runPackagingStage(GenerationSuspension var1, @Nullable DoubleCallback var2, @Nullable Player var3) {
      BossBarManager.setTitleKeepProgress("Generating pack.mcmeta...");
      this.service.generator.writePackMetadata();
      BossBarManager.setTitleKeepProgress("Processing packs...");
      this.service.generator.mergeItemOverrides();
      Log.log(" ");
      Log.logToSender(var3, ChatColor.GREEN + "[Pack] Generated resourcepack assets.");
      long var4 = System.currentTimeMillis();
      BossBarManager.setTitleKeepProgress("Compressing pack...");
      this.service.generator.startGeneration(var4x -> var1.handleCompressionResult(var4x, var3, var4), var2, var1::abortIfNotCompleted);
   }

   private void handleGenerationFailure(GenerationSuspension var1, Throwable var2) {
      try {
         this.service.resetAfterFailure();
      } catch (Throwable var4) {
         var2.addSuppressed(var4);
      }

      BossBarManager.clear();
      if (!(var2 instanceof VanillaAssetException) || !var1.tryRepairVanillaAssets()) {
         Log.error("Resource-pack generation failed.", var2);
         var1.scheduleCompletion(false);
      }
   }

   private boolean isModelEnginePostServerEnabled() {
      if (!Main.aFe) {
         return false;
      }

      File var1 = new File(this.plugin.getDataFolder().getParent(), "ModelEngine" + File.separator + "config.yml");
      if (!var1.exists()) {
         return false;
      }

      YamlConfiguration var2 = YamlConfiguration.loadConfiguration(var1);
      boolean var3 = var2.getBoolean("Model-Generator.Register-Post-Server");
      boolean var4 = var2.getBoolean("Model-Generator.Assets-Post-Server");
      boolean var5 = var2.getBoolean("Model-Generator.Compile-Post-Server");
      if (!anyPostServerFlag(var3, var4, var5)) {
         return false;
      }

      Log.warn(" ");
      Log.warn(" ");
      Log.warn("ModelEngine 'Post-Server' flags are enabled. This can make its resource pack change while ItemsAdder is reading it.");
      if (var3) {
         Log.warn("- 'Register-Post-Server' is enabled.");
      }

      if (var4) {
         Log.warn("- 'Assets-Post-Server' is enabled.");
      }

      if (var5) {
         Log.warn("- 'Compile-Post-Server' is enabled.");
      }

      Log.warn(" ");
      Log.warn(" ");
      return true;
   }

   static boolean anyPostServerFlag(boolean var0, boolean var1, boolean var2) {
      return var0 || var1 || var2;
   }

   static boolean shouldSkipStartup(boolean var0, boolean var1) {
      return !var0 && var1;
   }
}
