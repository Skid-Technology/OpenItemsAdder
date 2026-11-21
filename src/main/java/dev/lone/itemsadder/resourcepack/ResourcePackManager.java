package dev.lone.itemsadder.resourcepack;

import beer.devs.nativepackets.api.ConnectionHandle;
import beer.devs.nativepackets.api.ConnectionIdentity;
import beer.devs.nativepackets.api.NativePacketFactory;
import beer.devs.nativepackets.api.NativeText;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.ProtocolPhase;
import beer.devs.nativepackets.api.ResourcePackPushPacketView;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader.Feature;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.network.ViaVersionIntegration;
import dev.lone.itemsadder.util.BossBarManager;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NamedValue;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class ResourcePackManager implements AutoCloseable {
   private static final Map btn = new ConcurrentHashMap();
   private volatile ShaderFingerprint bto = ShaderFingerprint.bFR;
   private final ResourcePackService btp;
   private final ResourcePackRepository btq;
   private final Path btr;
   private final boolean bts;
   private final Map btt = new ConcurrentHashMap();
   private static final Map btu = new ConcurrentHashMap();
   private static final Set btv = ConcurrentHashMap.newKeySet();
   private final AtomicReference btw = new AtomicReference<>(List.of());
   private String btx;
   private volatile boolean cV = true;
   private volatile boolean bty;
   private volatile HumanoidEquipmentPack btz;
   private volatile String btA;
   private volatile Set btB = Set.of();
   private volatile Map btC = Map.of();
   private final Set btD = ConcurrentHashMap.newKeySet();

   ResourcePackManager(ResourcePackService var1, Path var2) {
      this.btp = var1;
      this.btr = var2;
      this.bts = detectProxyDelivery();
      this.btq = new ResourcePackRepository(var2);
   }

   public boolean isOwnPackUrl(String var1) {
      return Objects.equals(var1, this.btp.delivery.getUrl()) || btv.contains(var1);
   }

   public File getClientPackFile(String var1) {
      String var2 = var1.startsWith("/resourcepack/") ? "/resourcepack/client_packs/" : "client_packs/";
      if (!var1.startsWith(var2)) {
         return null;
      }

      String var3 = var1.substring(var2.length());
      return var3.matches("[a-f0-9]{40}\\.zip") ? (File)btu.get(var3) : null;
   }

   public void updateSoundPackFingerprint(Collection var1) {
      if (!Settings.get().soundPacksEnabled) {
         this.bto = ShaderFingerprint.bFR;
      } else {
         try {
            this.bto = ShaderFingerprint.fromSoundPackFiles(var1);
         } catch (IllegalArgumentException var3) {
            this.bto = ShaderFingerprint.bFR;
            Log.error("[Pack] " + var3.getMessage() + "; sound packs disabled, using complete pack.");
         }
      }

      if (!this.bto.isEmpty() && (!this.btp.delivery.supportsPerClientDelivery() || this.bts)) {
         Log.warn("[Pack] sound_packs unavailable with this hosting/proxy configuration; using complete pack.");
      }

      if (Settings.get().shaderPacksEnabled && (!this.btp.delivery.supportsPerClientDelivery() || this.bts)) {
         Log.warn("[Pack] shader_packs unavailable with this hosting/proxy configuration; using complete pack.");
      }
   }

   public boolean isPerClientDeliveryActive() {
      return this.cV && !this.bts && this.btp.delivery.supportsPerClientDelivery();
   }

   public HumanoidEquipmentPack getHumanoidEquipmentPack() {
      return this.isPerClientDeliveryActive() && Settings.get().customEntitiesHumanoidEquipment ? this.btz : null;
   }

   public void warnHumanoidEquipment(String var1, String var2) {
      if (this.btD.add(var1)) {
         Log.warn("[Pack] Humanoid equipment: " + var2);
      }
   }

   public PackBinding getPackBinding(UUID var1, long var2) {
      PackSession var4 = (PackSession)btn.get(var1);
      return this.isPerClientDeliveryActive()
            && Settings.get().customEntitiesHumanoidEquipment
            && var4 != null
            && var4.connectionId == var2
            && var4.equipment != null
            && var4.equipment.btJ == this.btz
            && Objects.equals(this.btz.hash(), this.btp.delivery.getHash())
            && var4.state.isInstalledWith(var4.base, var4.baseHash)
         ? var4.equipment
         : null;
   }

   private CompletableFuture preparePackFor(String var1, int var2) {
      Settings var3 = Settings.get();
      PackFormat var4 = PackFormat.fromProtocol(var2);
      PackFormat var5 = var4 != null && var4.isEnabledFor(var3) ? var4 : null;
      ShaderFingerprint var6 = var4 == null ? ShaderFingerprint.bFR : this.bto;
      if (!this.bty) {
         return this.buildPackFor(var1, var5, var6);
      }

      if (var4 != null && var5 == null) {
         return this.buildPackFor(var1, null, var6);
      }

      PackVersionSpec var7 = HumanoidEquipmentPack.findVersionSpec(var2);
      return var7 != null && this.btz != null && this.btz.catalogs().containsKey(var7.version())
         ? this.buildDerivedPack(var1, "equipment_" + var2, List.of(var7.toPackVersion()), var6)
         : this.buildDerivedPack(
            var1,
            var4 != null && var7 == null ? "equipment_" + var4.getKey() : "equipment_base",
            var4 != null && var7 == null ? var4.getFormats() : List.of(),
            var6
         );
   }

   boolean canDeliverVersion(int var1) {
      Settings var2 = Settings.get();
      if (!this.cV || this.bts || !this.btp.delivery.supportsPerClientDelivery() || !var2.perClientVersionDelivery) {
         return true;
      }

      if (var1 < 0) {
         return false;
      }

      PackFormat var3 = PackFormat.fromProtocol(var1);
      if (var3 != null && var3.isEnabledFor(var2)) {
         String var4 = this.btp.delivery.getHash();
         if (var4 != null && this.btp.generator.generated && !this.btp.delivery.bIS && !this.btp.delivery.bIT) {
            try {
               CompletableFuture var5 = this.preparePackFor(var4, var1);
               return var2.autoApplyForced && !var5.isDone() || var5.getNow(null) != null;
            } catch (CompletionException | CancellationException | RejectedExecutionException var6) {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   public void runAfterPacksReady(Runnable var1) {
      CompletableFuture var2 = CompletableFuture.allOf(((List)this.btw.get()).toArray(CompletableFuture[]::new));
      if (var2.isDone()) {
         if (this.cV) {
            var1.run();
         }
      } else {
         var2.thenRun(() -> SchedulerUtils.runOnMainThread(() -> {
            if (this.cV) {
               var1.run();
            }
         }));
      }
   }

   public boolean hasPendingPacks() {
      return this.cV && ((List)this.btw.get()).stream().anyMatch(var0 -> !var0.isDone());
   }

   public void runWhenPackReady(Player var1, Runnable var2) {
      if (this.cV) {
         Settings var3 = Settings.get();
         if (!this.bts
            && this.btp.delivery.supportsPerClientDelivery()
            && (this.bty || var3.perClientVersionDelivery || !this.bto.isEmpty() || var3.shaderPacksEnabled)) {
            String var4 = this.btp.delivery.getHash();
            CompletableFuture var5 = this.preparePackFor(var4, ViaVersionIntegration.method4866(var1.getUniqueId()));
            if (var5.isDone()) {
               var2.run();
            } else {
               var5.whenComplete((var4x, var5x) -> {
                  if (this.cV) {
                     NativePacketsHolder.PE().IP().entity(Main.getInstance(), var1, () -> {
                        if (this.cV && var1.isOnline()) {
                           if (Objects.equals(var4, this.btp.delivery.getHash())) {
                              var2.run();
                           } else {
                              this.runWhenPackReady(var1, var2);
                           }
                        }
                     }, () -> {});
                  }
               });
            }
         } else {
            var2.run();
         }
      }
   }

   void onResourcePackPush(PacketEvent var1) {
      Settings var2 = Settings.get();
      if (this.cV && !var1.isCancelled()) {
         ResourcePackPushPacketView var3 = (ResourcePackPushPacketView)var1.view(ResourcePackPushPacketView.class);
         ConnectionHandle var4 = var1.connection();
         ConnectionIdentity var5 = var4.identity();
         if (var5 != null && Objects.equals(var3.url(), this.btp.delivery.getUrl()) && Objects.equals(var3.hash(), this.btp.delivery.getHash())) {
            UUID var6 = var3.packId();
            int var7 = ViaVersionIntegration.method4866(var5.uniqueId());
            String var8 = var3.hash();
            PackSendResult var9 = new PackSendResult(new PackIdentity(var3.url(), var3.hash()), List.of(), null);
            if (!this.bts && this.btp.delivery.supportsPerClientDelivery()) {
               PackFormat var10 = PackFormat.fromProtocol(var7);
               PackFormat var11 = var10 != null && var10.isEnabledFor(var2) ? var10 : null;
               ShaderFingerprint var12 = var10 == null ? ShaderFingerprint.bFR : this.bto;
               if (this.bty || var11 != null || !var12.isEmpty() || var10 != null && var2.shaderPacksEnabled) {
                  CompletableFuture var13 = this.preparePackFor(var3.hash(), var7);

                  PackSendResult var14;
                  try {
                     var14 = (PackSendResult)var13.getNow(null);
                  } catch (CompletionException | CancellationException var20) {
                     var14 = null;
                  }

                  if ((this.bty || var11 != null) && var14 == null) {
                     String var26 = var3.url();
                     boolean var27 = var3.required();
                     NativeText var17 = copyNativeText(var3.prompt());
                     NativePacketFactory var18 = NativePacketsHolder.PE().Pv();
                     Object var19 = var1.phase() == ProtocolPhase.CONFIGURATION
                        ? var18.disconnect(NativePacketsHolder.PE().Px().literal(Main.aFi.lj("resourcepack-not-ready")))
                        : null;
                     var1.cancel();
                     var13.whenComplete((var9x, var10x) -> var4.eventLoop().execute(() -> {
                        if (var4.isOpen()) {
                           if (var9x != null && var10x == null && this.cV && Objects.equals(var8, this.btp.delivery.getHash())) {
                              var4.send(var18.resourcePackPush(var6, var26, var8, var27, var17));
                           } else {
                              if (var19 != null) {
                                 var4.send(var19);
                              }
                           }
                        }
                     }));
                     return;
                  }

                  if (var14 != null && Objects.equals(var3.hash(), this.btp.delivery.getHash())) {
                     var9 = var14;
                  }
               }
            }

            PackSendResult var21 = var9;
            UUID var22 = this.bty ? humanoidEquipmentUuid(var6, var21.main.btF) : var6;
            UUID var23 = var5.uniqueId();
            boolean var24 = var3.required();
            HumanoidEquipmentPack var25 = this.btz;
            PackVersionSpec var15 = HumanoidEquipmentPack.findVersionSpec(var7);
            PackBinding var16 = var25 != null && var15 != null && var25.hash().equals(var8) && var25.catalogs().containsKey(var15.version())
               ? new PackBinding(var25, var15)
               : null;
            var1.afterWrite(
               var8x -> {
                  if (var8x.successful() && var4.isOpen()) {
                     btn.entrySet().removeIf(var0x -> {
                        ConnectionHandle var1xx = (ConnectionHandle)((PackSession)var0x.getValue()).connection.get();
                        return var1xx == null || !var1xx.isOpen();
                     });
                     PackSession var9x = btn.compute(
                        var23,
                        (var6xx, var7xx) -> new PackSession(
                           var4.id(),
                           new WeakReference<>(var4),
                           var7xx != null && var7xx.connectionId == var4.id() ? var7xx.state : new PlayerPackState(),
                           var22,
                           var6,
                           var21.main.btF,
                           var8,
                           var16,
                           new AtomicReference()
                        )
                     );
                     LinkedHashMap var10x = new LinkedHashMap();
                     var10x.put(var22, var21.main.btF);
                     ArrayList var11x = new ArrayList();

                     for (PackRecipient var13x : var21.sounds) {
                        var10x.put(var13x.id, var13x.delivery.btF);
                        if (!var9x.state.isInstalledWith(var13x.id, var13x.delivery.btF)) {
                           var11x.add(var13x);
                        }
                     }

                     PackRecipient var18x = var21.shaders;
                     if (var18x != null) {
                        var10x.put(var18x.id, var18x.delivery.btF);
                     }

                     boolean var19x = var18x != null && var9x.state.isInstalledWith(var18x.id, var18x.delivery.btF);
                     Set var14x = var9x.state.applyPackState(var22, var10x, var18x == null ? Set.of() : Set.of(var18x.id));
                     var9x.shaders.set(var19x ? null : var18x);
                     NativePacketFactory var15x = NativePacketsHolder.PE().Pv();

                     for (UUID var17x : var14x) {
                        var4.send(var15x.resourcePackPop(var17x));
                     }

                     for (PackRecipient var21x : var11x) {
                        var4.send(Main.getInstance(), var15x.resourcePackPush(var21x.id, var21x.delivery.btE, var21x.delivery.btF, var24, null), var4xx -> {
                           if (!var4xx.successful() && var4.isOpen() && btn.get(var23) == var9x) {
                              var9x.state.updateStatus(var21x.id, "FAILED_DOWNLOAD");
                              NativePacketsHolder.PE().IP().global(Main.getInstance(), () -> {
                                 if (var4.isOpen() && btn.get(var23) == var9x) {
                                    ResourcePackService var4xxx = ContentManager.method8084().aL;
                                    var4xxx.setPlayerStatusById(var23, var9x.state.aggregateStatus());
                                    Player var5xx = NativePacketsHolder.PE().method1885(var23, var4.id());
                                    if (var5xx != null) {
                                       NativePacketsHolder.PE().IP().entity(Main.getInstance(), var5xx, () -> {
                                          if (btn.get(var23) == var9x) {
                                             var4xxx.downloadGate.releasePlayer(var5xx);
                                          }
                                       }, () -> {});
                                    }

                                    Log.warn("[Pack] Failed to send sound pack " + var21x.id + " to " + var23);
                                 }
                              });
                           }
                        });
                     }
                  }
               }
            );
            var3.url(var21.main.btE);
            var3.hash(var21.main.btF);
            var3.packId(var22);
         }
      }
   }

   boolean hasRecipient(UUID var1, UUID var2) {
      PackSession var3 = (PackSession)btn.get(var1);
      return var3 != null && var3.state.hasPack(var2);
   }

   boolean hasSession(UUID var1) {
      return btn.containsKey(var1);
   }

   boolean isRecipientSent(UUID var1, UUID var2) {
      PackSession var3 = (PackSession)btn.get(var1);
      return var3 != null && var3.state.isRequiredPresent(var2);
   }

   boolean isRecipientAcked(UUID var1, UUID var2) {
      PackSession var3 = (PackSession)btn.get(var1);
      return var3 != null && var3.state.markInstalled(var2);
   }

   boolean isFullyAccepted(UUID var1) {
      PackSession var2 = (PackSession)btn.get(var1);
      return var2 != null && var2.state.consumeInstallCompletion();
   }

   void sendShaderPackTo(UUID var1) {
      PackSession var2 = (PackSession)btn.get(var1);
      if (this.cV && var2 != null) {
         PackRecipient var3 = (PackRecipient)var2.shaders.get();
         ConnectionHandle var4 = (ConnectionHandle)var2.connection.get();
         if (var3 != null
            && var4 != null
            && var4.isOpen()
            && NativePacketsHolder.PE().method1885(var1, var2.connectionId) != null
            && var2.state.tryStartDownload(var3.id, var3.delivery.btF)) {
            NativePacketFactory var5 = NativePacketsHolder.PE().Pv();
            var4.send(Main.getInstance(), var5.resourcePackPush(var3.id, var3.delivery.btE, var3.delivery.btF, false, null), var4x -> {
               if (!var4x.successful() && var4.isOpen() && btn.get(var1) == var2) {
                  var2.state.updateStatus(var3.id, "FAILED_DOWNLOAD");
                  Log.warn("[Pack] Failed to send shader pack to " + var1);
               }
            });
         }
      }
   }

   UUID resolveRecipientUuid(UUID var1, long var2, UUID var4) {
      PackSession var5 = (PackSession)btn.get(var1);
      return var5 != null && var5.connectionId == var2 && var5.base.equals(var4) ? var5.requestedBase : var4;
   }

   PackPushStatus updatePackStatus(UUID var1, long var2, UUID var4, String var5) {
      PackSession var6 = (PackSession)btn.get(var1);
      return var6 != null && (var2 < 0L || var6.connectionId == var2) ? var6.state.updateStatus(var4, var5) : null;
   }

   void removeStaleSession(Player var1) {
      btn.computeIfPresent(var1.getUniqueId(), (var1x, var2) -> {
         ConnectionHandle var3 = (ConnectionHandle)var2.connection.get();
         return (PackSession)(var3 != null && var3.isOpen() && NativePacketsHolder.PE().method1885(var1x, var2.connectionId) != var1 ? var2 : null);
      });
   }

   private CompletableFuture buildPackFor(String var1, PackFormat var2, ShaderFingerprint var3) {
      String var4 = var2 == null ? "universal" : var2.getKey();
      return this.buildDerivedPack(var1, var4, var2 == null ? null : var2.getFormats(), var3);
   }

   private CompletableFuture buildDerivedPack(String var1, String var2, List var3, ShaderFingerprint var4) {
      boolean var5 = Settings.get().shaderPacksEnabled;
      UUID var6 = getPackDeliveryUuid();
      String var7 = var1 + "_" + var2 + "_" + var4.getHash() + "_" + var5 + "_" + var6;
      CompletableFuture var8 = var3 != null && var5 ? this.buildDerivedPack(var1, "universal", null, var4) : null;
      return this.btt
         .computeIfAbsent(
            var7,
            var8x -> {
               Path var9 = this.btp.generator.getPackFile().toPath();
               CompletableFuture var10 = var3 == null
                  ? CompletableFuture.completedFuture(new PackSource(var9, var1))
                  : this.btq.deriveClientPackAsync(var9, var1, var2, var3);
               CompletableFuture var11 = var10.thenApplyAsync(var6xx -> {
                  try {
                     if (!ResourcePackCompressor.digestFile(var6xx.file(), "SHA-1").equals(var6xx.sha1())) {
                        throw new IOException("Source pack changed before sound-pack derivation");
                     }

                     Map var7xx = ShaderPackBuilder.buildSoundSplit(var6xx.file(), this.btr, var4, var5, var3 == null, var1, Log::warn);
                     if (!ResourcePackCompressor.digestFile(var6xx.file(), "SHA-1").equals(var6xx.sha1())) {
                        throw new IOException("Source pack changed during sound-pack derivation");
                     }

                     PackIdentity var8xx = null;
                     ArrayList var9x = new ArrayList();
                     PackRecipient var10x = null;

                     for (Entry var12 : var7xx.entrySet()) {
                        Path var13 = (Path)var12.getValue();
                        PackSource var14 = new PackSource(var13, ResourcePackCompressor.digestFile(var13, "SHA-1"));
                        this.btq.writeArtifactManifest(var14, var1);
                        PackIdentity var15 = this.resolvePackIdentity(var14, var1);
                        if (var15 == null) {
                           return null;
                        }

                        if (((String)var12.getKey()).isEmpty()) {
                           var8xx = var15;
                        } else if (((String)var12.getKey()).equals("@shaders")) {
                           var10x = new PackRecipient(shaderPackUuid(var6), var15);
                        } else {
                           var9x.add(new PackRecipient(soundPackUuid(var6, (String)var12.getKey()), var15));
                        }
                     }

                     if (var8xx == null) {
                        throw new IOException("Missing base pack");
                     } else {
                        return new PackSendResult(var8xx, List.copyOf(var9x), var10x);
                     }
                  } catch (IOException var16) {
                     throw new CompletionException(var16);
                  }
               }, this.btq.getExecutor());
               if (var8 != null) {
                  var11 = var11.thenCombine(
                     var8, (var0, var1xx) -> var0 != null && var1xx != null ? new PackSendResult(var0.main, var0.sounds, var1xx.shaders) : null
                  );
               }

               return var11.exceptionally(
                  var3xx -> {
                     if (this.cV) {
                        Log.warn(
                           "[Pack] derived packs "
                              + var2
                              + " failed; "
                              + (!this.bty && var3 == null ? "using complete pack: " : "client pack delivery withheld: ")
                              + (var3xx.getCause() == null ? var3xx : var3xx.getCause()).getMessage()
                        );
                     }

                     return null;
                  }
               );
            }
         );
   }

   private static UUID getPackDeliveryUuid() {
      return UUID.fromString(Settings.get().packDeliveryUuid);
   }

   public String publishUniversalPackUrl(String var1) {
      PackSource var2 = this.btq.publishUniversalPack(this.btp.generator.getPackFile().toPath(), var1);
      String var3 = this.btp.delivery.publishPackFile(var2.file().toFile(), var1);
      if (var3 != null && (var3.startsWith("http://") || var3.startsWith("https://"))) {
         btu.put(var1 + ".zip", var2.file().toFile());
         btv.add(var3);
         return var3;
      } else {
         throw new IOException("Self-host did not publish a universal pack URL");
      }
   }

   static UUID soundPackUuid(UUID var0, String var1) {
      return UUID.nameUUIDFromBytes((var0 + ":sound_packs:" + var1).getBytes(StandardCharsets.UTF_8));
   }

   static UUID shaderPackUuid(UUID var0) {
      return UUID.nameUUIDFromBytes((var0 + ":shaders").getBytes(StandardCharsets.UTF_8));
   }

   static UUID humanoidEquipmentUuid(UUID var0, String var1) {
      return UUID.nameUUIDFromBytes((var0 + ":humanoid_equipment:" + var1).getBytes(StandardCharsets.UTF_8));
   }

   static NativeText copyNativeText(NativeText var0) {
      if (var0 == null) {
         return null;
      }

      Object var1 = var0.nativeHandle();
      return () -> var1;
   }

   private PackIdentity resolvePackIdentity(PackSource var1, String var2) {
      if (this.cV && Objects.equals(var2, this.btp.delivery.getHash())) {
         String var3 = null;
         Path var4 = this.btr.resolve(var1.sha1() + "_" + this.btp.delivery.getName().replace('-', '_') + ".url.json");
         if (this.btp.delivery instanceof HostedPackDelivery && Files.isRegularFile(var4)) {
            try {
               JSONObject var5 = JSON.parseObject(Files.readString(var4));
               if (var1.sha1().equals(var5.getString("sha1"))) {
                  var3 = var5.getString("url");
               }
            } catch (RuntimeException var6) {
            }
         }

         if (var3 == null) {
            var3 = this.btp.delivery.publishPackFile(var1.file().toFile(), var1.sha1());
         }

         if (var3 != null && (var3.startsWith("http://") || var3.startsWith("https://"))) {
            if (this.cV && Objects.equals(var2, this.btp.delivery.getHash())) {
               if (this.btp.delivery instanceof HostedPackDelivery) {
                  JSONObject var7 = new JSONObject();
                  var7.put("url", var3);
                  var7.put("sha1", var1.sha1());
                  VanillaAssetCache.writeCacheFile(var4, JSON.toJSONBytes(var7));
               }

               btu.put(var1.sha1() + ".zip", var1.file().toFile());
               btv.add(var3);
               return new PackIdentity(var3, var1.sha1());
            } else {
               return null;
            }
         } else {
            throw new IOException("Hosting did not publish a derived pack URL");
         }
      } else {
         return null;
      }
   }

   public void clearRecipient(UUID var1, long var2, UUID var4) {
      PackSession var5 = (PackSession)btn.get(var1);
      if (var5 != null && var5.connectionId == var2) {
         var5.state.reset(var4);
      }
   }

   public boolean isPackActive(UUID var1, long var2) {
      PackSession var4 = (PackSession)btn.get(var1);
      String var5 = this.btA;
      return this.cV
         && !this.bts
         && this.btp.delivery.supportsPerClientDelivery()
         && var5 != null
         && var5.equals(this.btp.delivery.getHash())
         && var4 != null
         && var4.connectionId == var2
         && var5.equals(var4.universalHash)
         && var4.state.isInstalledWith(var4.base, var4.baseHash);
   }

   public boolean isCustomModelSupported(UUID var1, long var2, String var4) {
      return this.isPackActive(var1, var2) && this.btB.contains(var4);
   }

   public int getScrollableModelVersion(UUID var1, long var2, String var4) {
      return this.isPackActive(var1, var2) ? this.btC.getOrDefault(var4, -1) : -1;
   }

   public void reloadPackFor(String var1) {
      this.btA = null;
      this.btB = Set.of();
      this.btC = Map.of();
      if (var1 != null) {
         try (ZipFile var2 = new ZipFile(this.btp.generator.getPackFile())) {
            ZipEntry var3 = var2.getEntry("assets/_iainternal/scrollable_gui_legacy_v1.json");
            if (var3 != null) {
               try (InputStream var4 = var2.getInputStream(var3)) {
                  JSONObject var5 = JSON.parseObject(var4.readAllBytes());
                  JSONObject var6 = var5.getJSONObject("models");
                  if (var5.getIntValue("version") == 1 && var6 != null) {
                     HashMap var7 = new HashMap();

                     for (String var9 : var6.keySet()) {
                        int var10 = var6.getIntValue(var9);
                        if (var10 > 0) {
                           var7.put(var9, var10);
                        }
                     }

                     this.btC = Map.copyOf(var7);
                  }
               }
            }

            ZipEntry var22 = var2.getEntry("ia_overlay_1_21_4_plus/assets/_iainternal/scrollable_gui_v1.json");
            if (var22 != null) {
               try (InputStream var23 = var2.getInputStream(var22)) {
                  JSONObject var25 = JSON.parseObject(var23.readAllBytes());
                  if (var25.getIntValue("version") == 2) {
                     JSONArray var27 = var25.getJSONArray("custom_models");
                     if (var27 != null) {
                        this.btB = Set.copyOf(var27.toJavaList(String.class, new Feature[0]));
                     }

                     this.btA = var1;
                  }
               }
            }
         } catch (IOException | JSONException var19) {
         }
      }

      Settings var20 = Settings.get();
      this.btz = null;
      this.bty = false;
      this.btD.clear();
      if (var1 != null && this.isPerClientDeliveryActive()) {
         try {
            this.btz = HumanoidEquipmentPack.loadFromZip(this.btp.generator.getPackFile().toPath(), var1);
            this.bty = this.btz != null;
         } catch (IOException | RuntimeException var11) {
            this.bty = true;
            Log.warn("[Pack] Equipment generation unavailable; serving packs without equipment: " + var11.getMessage());
         }
      }

      if (var1 != null
         && this.cV
         && !this.bts
         && (this.bty || var20.perClientVersionDelivery || !this.bto.isEmpty() || var20.shaderPacksEnabled)
         && this.btp.delivery.supportsPerClientDelivery()) {
         this.btt
            .entrySet()
            .removeIf(
               var1x -> ((CompletableFuture)var1x.getValue()).isDone()
                  && (!((String)var1x.getKey()).startsWith(var1 + "_") || ((CompletableFuture)var1x.getValue()).getNow(null) == null)
            );
         this.btq.setCurrentUniversalHash(var1);

         try {
            long var21 = System.nanoTime();
            Map var24 = this.collectPackVariants(var1);
            List var26 = List.copyOf(var24.values());
            if (var26.isEmpty() || var26.equals(this.btw.get())) {
               return;
            }

            this.btw.set(var26);
            Log.log("[Pack] per_client_version: preparing " + var26.size() + " packs...");
            this.updateProgressBar(var1, var24, var26, "-", var21);
            var24.forEach((var6x, var7x) -> var7x.whenComplete((var7xx, var8) -> this.updateProgressBar(var1, var24, var26, var6x, var21)));
            CompletableFuture.allOf(var26.toArray(CompletableFuture[]::new))
               .whenComplete(
                  (var5x, var6x) -> {
                     synchronized (this.btw) {
                        if (!this.cV || this.btw.get() != var26 || !Objects.equals(var1, this.btp.delivery.getHash())) {
                           return;
                        }

                        this.clearProgressBar();
                     }

                     long var10x = var26.stream().filter(var0 -> !var0.isCompletedExceptionally() && var0.getNow(null) != null).count();
                     Log.log(
                        "[Pack] per_client_version: "
                           + var10x
                           + "/"
                           + var26.size()
                           + " packs ready in "
                           + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - var21)
                           + " ms."
                           + (var10x < var26.size() ? " Unavailable client packs are withheld." : "")
                     );
                  }
               );
         } catch (RejectedExecutionException var15) {
         }
      }
   }

   Map collectPackVariants(String var1) {
      Settings var2 = Settings.get();
      LinkedHashMap var3 = new LinkedHashMap();
      if (this.bty) {
         var3.put("base", this.preparePackFor(var1, -1));
         HumanoidEquipmentPack.buU.stream().filter(var1x -> {
            PackFormat var2x = PackFormat.fromProtocol(var1x.protocol());
            return var2x != null && var2x.isEnabledFor(var2);
         }).forEach(var3x -> var3.put(var3x.version(), this.preparePackFor(var1, var3x.protocol())));

         for (PackFormat var7 : PackFormat.values()) {
            if (var7.isEnabledFor(var2) && HumanoidEquipmentPack.buU.stream().noneMatch(var1x -> PackFormat.fromProtocol(var1x.protocol()) == var7)) {
               var3.put(
                  var7.getKey().replace("_to_", " - ").replace('_', '.'),
                  this.buildDerivedPack(var1, "equipment_" + var7.getKey(), var7.getFormats(), this.bto)
               );
            }
         }
      } else if (!this.bto.isEmpty() || var2.shaderPacksEnabled) {
         var3.put("universal/sounds", this.buildPackFor(var1, null, this.bto));
      }

      if (!this.bty) {
         Arrays.stream(PackFormat.values())
            .filter(var1x -> var1x.isEnabledFor(var2))
            .forEach(var3x -> var3.put(var3x.getKey().replace("_to_", " - ").replace('_', '.'), this.buildPackFor(var1, var3x, this.bto)));
      }

      return var3;
   }

   static NamedValue buildProgressValue(Map var0, String var1, long var2) {
      int var4 = 0;
      int var5 = 0;

      for (CompletableFuture var7 : var0.values()) {
         if (var7.isDone()) {
            var4++;
            if (!var7.isCompletedExceptionally() && var7.getNow(null) != null) {
               var5++;
            }
         }
      }

      double var8 = var0.isEmpty() ? 1.0 : (double)var4 / var0.size();
      return new NamedValue(
         "[Pack] per_client_version: "
            + var4
            + "/"
            + var0.size()
            + " completed ("
            + Math.round(var8 * 100.0)
            + "%) | ready: "
            + var5
            + " | failed: "
            + (var4 - var5)
            + " | last: "
            + var1
            + " | "
            + var2
            + " ms",
         var8
      );
   }

   private void updateProgressBar(String var1, Map var2, List var3, String var4, long var5) {
      synchronized (this.btw) {
         if (this.cV && this.btw.get() == var3 && Objects.equals(var1, this.btp.delivery.getHash())) {
            NamedValue var8 = buildProgressValue(var2, var4, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - var5));
            if (var8.value != 1.0) {
               this.btx = var8.name;
               BossBarManager.setTitleAndProgress(var8.name, var8.value);
            }
         }
      }
   }

   private void clearProgressBar() {
      synchronized (this.btw) {
         if (this.btx != null) {
            BossBarManager.clearIfTitle(this.btx);
         }

         this.btx = null;
      }
   }

   @Override
   public void close() {
      this.cV = false;
      this.clearProgressBar();
      this.btt.values().forEach(var0 -> var0.cancel(false));
      this.btq.shutdownExecutor();
      this.btp.delivery.onShutdown();
      this.btq.close();
      this.btt.clear();
   }

   private static boolean detectProxyDelivery() {
      try {
         if (Bukkit.spigot().getConfig().getBoolean("settings.bungeecord")) {
            return true;
         }

         Class var0;
         try {
            var0 = Class.forName("io.papermc.paper.configuration.GlobalConfiguration", false, Bukkit.getServer().getClass().getClassLoader());
         } catch (ClassNotFoundException var4) {
            return false;
         }

         Object var1 = var0.getMethod("get").invoke(null);
         Object var2 = var0.getField("proxies").get(var1);
         Object var3 = var2.getClass().getField("velocity").get(var2);
         return var3.getClass().getField("enabled").getBoolean(var3);
      } catch (ReflectiveOperationException | RuntimeException | LinkageError var5) {
         return true;
      }
   }
}
