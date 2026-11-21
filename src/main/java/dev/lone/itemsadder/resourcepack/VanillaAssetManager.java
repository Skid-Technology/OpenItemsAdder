package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.lone.itemsadder.api.VanillaModelResource;
import dev.lone.itemsadder.core.NamespacedId;
import dev.lone.itemsadder.util.IaRuntimeException;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class VanillaAssetManager {
   public static final Version io = Version.v1_20_6;
   public static final Set ip = Collections.unmodifiableSet(
      new LinkedHashSet<>(
         List.of(
            Version.v1_20_6,
            Version.v1_21_4,
            Version.v1_21_5,
            Version.v1_21_6,
            Version.v1_21_7,
            Version.v1_21_8,
            Version.v1_21_10,
            Version.v1_21_11,
            Version.v26_1_1,
            Version.v26_1_2,
            Version.v26_2,
            Version.v26_3
         )
      )
   );
   private final Path iq;
   private final PackCacheStore ir;
   @Nullable
   private final Consumer is;
   private FutureTask it;
   private final ReentrantLock iu = new ReentrantLock();
   private final Set iv = ConcurrentHashMap.newKeySet();
   private volatile Thread iw;
   private volatile boolean cz;
   private volatile boolean ix;
   private volatile VanillaAssetException iy;
   private final Map iz = new ConcurrentHashMap();
   private final Set iA = ConcurrentHashMap.newKeySet();
   Map iB = new ConcurrentHashMap();
   List iC;
   @Nullable
   static Version iD;
   private static final Gson iE = new GsonBuilder().serializeNulls().create();

   public VanillaAssetManager(Path var1, @Nullable Consumer var2) {
      this.iq = var1;
      this.ir = new PackCacheStore(var1, var0 -> Log.warn(var0));
      this.is = var2;
   }

   public static boolean isBuiltinPath(String var0) {
      return var0.contains("builtin/") || var0.endsWith("block/crop_cross") || var0.endsWith("block/crop_billboard");
   }

   public void prepareAsync() {
      this.prepare(false);
   }

   public void prepare(boolean var1) {
      VanillaAssetTask var2;
      synchronized (this) {
         if (this.cz || this.it != null && !this.it.isDone()) {
            return;
         }

         this.ix = false;
         var2 = new VanillaAssetTask(this, () -> {
            long var2x = System.nanoTime();
            Log.log("Preparing vanilla assets...");
            this.iu.lockInterruptibly();

            try {
               Version var4x = getTargetVersion();
               if (this.cz || Thread.currentThread().isInterrupted() || !this.prepareCatalog(var4x)) {
                  if (var1) {
                     throw new IaRuntimeException("Failed to prepare vanilla asset catalog for " + var4x.getName());
                  }

                  return null;
               }

               this.ix = !this.cz;
            } finally {
               this.iu.unlock();
            }

            if (this.ix) {
               Log.log("Vanilla assets ready in " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - var2x) + " ms.");
            }

            if (!this.cz && this.is != null) {
               if (var1) {
                  this.is.accept(this);
               } else {
                  SchedulerUtils.runOnMainThread(() -> {
                     if (!this.cz) {
                        this.is.accept(this);
                     }
                  });
               }
            }

            return null;
         }, var1);
         this.it = var2;
      }

      if (!var1) {
         SchedulerUtils.runSync(var2);
      } else {
         try {
            this.iw = Thread.currentThread();
            var2.run();
            var2.get();
         } catch (InterruptedException var11) {
            Thread.currentThread().interrupt();
            throw new IaRuntimeException("Interrupted preparing vanilla assets", var11);
         } catch (ExecutionException var12) {
            Throwable var4 = var12.getCause();
            if (var4 instanceof IaRuntimeException var5) {
               throw var5;
            }

            throw new IaRuntimeException("Failed to prepare vanilla assets", var4);
         } finally {
            this.iw = null;
         }
      }
   }

   private boolean prepareCatalog(Version var1) {
      this.iA.remove(var1);
      boolean var2 = this.ensureCatalog(var1);
      if (var2) {
         this.iv.add(var1);
      }

      return var2;
   }

   public synchronized void close() {
      this.cz = true;
      this.ix = false;
      this.iz.values().forEach(var0 -> var0.cancel(true));
      if (this.it != null) {
         this.it.cancel(Thread.currentThread() != this.iw);
      }
   }

   public static Version getTargetVersion() {
      if (iD != null) {
         return iD;
      }

      iD = resolveTargetVersion();
      return iD;
   }

   private static Version resolveTargetVersion() {
      Version var0 = Version.get();
      if (var0 != null && var0.ordinal() >= io.ordinal()) {
         if (ip.contains(var0)) {
            return var0;
         }

         Version[] var1 = Version.values();

         for (int var2 = var0.ordinal() - 1; var2 >= 0; var2--) {
            Version var3 = var1[var2];
            if (ip.contains(var3)) {
               return var3;
            }
         }

         return io;
      } else {
         return io;
      }
   }

   private boolean ensureCatalog(Version var1) {
      try {
         boolean var2 = this.ir.ensureVersion(var1.getName(), () -> this.hasValidCatalog(var1), var2x -> {
            Log.log("Downloading vanilla asset catalog for Minecraft " + var1.getName() + "...");
            VanillaAssetCache.fetchCatalog(this.iq.resolve("mojang"), var1.getName(), var2x);
         });
         if (var2 && this.loadAssetList(var1)) {
            return true;
         }

         Log.error("Downloaded Minecraft asset catalog is incomplete for version " + var1.getName() + ".");
         return false;
      } catch (UnsupportedPackVersionException var3) {
         this.iA.add(var1);
         Log.warn("Official Minecraft assets for " + var1.getName() + " are not published by Mojang yet; that exact client target remains unavailable.");
         return true;
      } catch (Exception var4) {
         if (var4 instanceof AssetDownloadException
            && !this.cz
            && !Thread.currentThread().isInterrupted()
            && !this.hasValidCatalog(var1)
            && this.loadOfflineAssetList(var1)) {
            Log.warn("Cannot refresh the official Minecraft catalog for " + var1.getName() + "; using the existing complete legacy assets offline.");
            return true;
         }

         if (!this.cz) {
            Log.error("Failed to prepare official Minecraft assets for version " + var1.getName(), var4);
         }

         return false;
      }
   }

   private boolean hasValidCatalog(Version var1) {
      try {
         byte[] var2 = this.ir.readAsset(var1.getName(), "assets-list.txt");
         byte[] var3 = this.ir.readAsset(var1.getName(), "assets-list.mojang");
         if (var2 != null && var3 != null) {
            String[] var4 = new String(var3, StandardCharsets.UTF_8).split("\n");
            return var4.length == 3
               && var4[0].equals(var1.getName())
               && var4[1].matches("[a-f0-9]{40}")
               && var4[2].equals(HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var2)));
         } else {
            return false;
         }
      } catch (IOException | NoSuchAlgorithmException | RuntimeException var5) {
         return false;
      }
   }

   private boolean loadAssetList(Version var1) {
      if (!this.hasValidCatalog(var1)) {
         return false;
      }

      try {
         byte[] var2 = this.ir.readAsset(var1.getName(), "assets-list.txt");
         if (var2 == null) {
            return false;
         }

         this.iB
            .put(
               var1,
               new String(var2, StandardCharsets.UTF_8)
                  .lines()
                  .map(String::trim)
                  .filter(var0 -> !var0.isEmpty() && !var0.startsWith("#") && !var0.startsWith("/") && !var0.contains("\\") && !var0.contains(".."))
                  .collect(Collectors.toList())
            );
         return true;
      } catch (IOException var3) {
         return false;
      }
   }

   private boolean loadOfflineAssetList(Version var1) {
      try {
         byte[] var2 = this.ir.readAsset(var1.getName(), "assets-list.txt");
         if (var2 == null) {
            return false;
         }

         List var3 = new String(var2, StandardCharsets.UTF_8)
            .lines()
            .map(String::trim)
            .filter(var0 -> !var0.isEmpty() && !var0.startsWith("#") && !var0.startsWith("/") && !var0.contains("\\") && !var0.contains(".."))
            .collect(Collectors.toList());
         if (!hasRequiredFamilies(var3, var1.ordinal() >= Version.v1_21_4.ordinal(), var2x -> this.ir.getHash(var1.getName(), var2x) != null)) {
            return false;
         }

         this.iB.put(var1, var3);
         return true;
      } catch (IOException var4) {
         return false;
      }
   }

   static boolean isCompleteOffline(Path var0, boolean var1) {
      Path var2 = var0.resolve("assets/minecraft").normalize();

      try {
         return hasRequiredFamilies(Files.readAllLines(var0.resolve("assets-list.txt")), var1, var2x -> {
            Path var3 = var0.resolve(var2x).normalize();
            return var3.startsWith(var2) && Files.isRegularFile(var3);
         });
      } catch (IOException | RuntimeException var4) {
         return false;
      }
   }

   private static boolean hasRequiredFamilies(List var0, boolean var1, Predicate var2) {
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = !var1;
      boolean var6 = false;

      for (String var8 : var0) {
         String var9 = var8.trim();
         if (var9.startsWith("models/")) {
            var3 = true;
         } else if (var9.startsWith("blockstates/")) {
            var4 = true;
         } else if (var1 && var9.startsWith("items/")) {
            var5 = true;
         } else {
            if (!var9.startsWith("shaders/")) {
               continue;
            }

            var6 = true;
         }

         if (!var2.test("assets/minecraft/" + var9)) {
            return false;
         }
      }

      return var3 && var4 && var5 && var6;
   }

   public List getAssetList(Version var1) {
      this.ensureAssetList(var1);
      this.checkNotUnsupported(var1);
      return this.iB.getOrDefault(var1, Collections.emptyList());
   }

   public List getAllAssets() {
      for (Version var2 : ip) {
         if (!this.iA.contains(var2)) {
            this.ensureAssetList(var2);
         }
      }

      return this.iB.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
   }

   public List getCommonAssets() {
      if (this.iC != null) {
         return this.iC;
      }

      for (Version var2 : ip) {
         if (!this.iA.contains(var2)) {
            this.ensureAssetList(var2);
         }
      }

      if (this.iB.isEmpty()) {
         return Collections.emptyList();
      }

      Iterator var3 = ip.stream().filter(var1 -> !this.iA.contains(var1)).map(this.iB::get).filter(Objects::nonNull).iterator();
      if (!var3.hasNext()) {
         return Collections.emptyList();
      }

      HashSet var4 = new HashSet((Collection)var3.next());

      while (var3.hasNext()) {
         var4.retainAll((Collection<?>)var3.next());
         if (var4.isEmpty()) {
            break;
         }
      }

      this.iC = new ArrayList(var4);
      return this.iC;
   }

   public boolean isReady() {
      return this.ix;
   }

   public InputStream openStream(String var1, Version var2) {
      this.checkNotUnsupported(var2);
      return this.openStreamInternal(var1, var2);
   }

   @Nullable
   private InputStream openStreamInternal(String var1, @Nullable Version var2) {
      return var2 == null ? null : (InputStream)this.readWithRepair(var1, var2, var1x -> {
         if (var1.endsWith(".json")) {
            parseValidated(var1, var1x);
         }

         return new ByteArrayInputStream(var1x);
      });
   }

   @Nullable
   public String readText(String var1, Version var2) {
      return (String)this.readWithRepair(var1, var2, var1x -> {
         if (var1.endsWith(".json")) {
            parseValidated(var1, var1x);
         }

         return new String(var1x, StandardCharsets.UTF_8);
      });
   }

   @Nullable
   public JSONObject readJson(String var1, Version var2) {
      return (JSONObject)this.readWithRepair(var1, var2, var1x -> parseValidated(var1, var1x));
   }

   private static JSONObject parseValidated(String var0, byte[] var1) {
      JSONObject var2 = parseJsonObject(var1);
      if (var0.startsWith("assets/minecraft/models/")) {
         String var3 = PackConfigProcessor.validateStructure(var2);
         if (var3 != null) {
            throw new IllegalArgumentException(var3);
         }
      } else if (var0.startsWith("assets/minecraft/items/")) {
         OverlayPackBuilder.validateVanillaModel(var2);
      }

      return var2;
   }

   private static JSONObject parseJsonObject(byte[] var0) {
      JSONObject var1 = JSON.parseObject(var0);
      if (var1 == null) {
         throw new IllegalArgumentException("Expected a vanilla JSON object");
      } else {
         return var1;
      }
   }

   private Object readWithRepair(String var1, Version var2, Function var3) {
      this.ensureAssetList(var2);
      this.checkNotUnsupported(var2);
      if (this.cz) {
         throw new IllegalStateException("Vanilla assets provider is closed");
      }

      if (var1.startsWith("assets/minecraft/") && !this.iB.getOrDefault(var2, Collections.emptyList()).contains(var1.substring("assets/minecraft/".length()))) {
         return null;
      }

      try {
         byte[] var4 = this.ir.readAsset(var2.getName(), var1);
         if (var4 == null) {
            this.scheduleRepair(var2, var1, var3, true);
            var4 = this.ir.readAsset(var2.getName(), var1);
            if (var4 == null) {
               throw new IOException("Fetched vanilla asset mapping is missing");
            } else {
               return var3.apply(var4);
            }
         } else {
            return var3.apply(var4);
         }
      } catch (IOException | IllegalArgumentException | JSONException var7) {
         this.scheduleRepair(var2, var1, var3, false);

         try {
            byte[] var5 = this.ir.readAsset(var2.getName(), var1);
            if (var5 == null) {
               throw new IOException("Repaired mapping disappeared");
            } else {
               return var3.apply(var5);
            }
         } catch (IOException | RuntimeException var6) {
            throw this.recordFailure(var2, var1, var6, false, (FutureTask)this.iz.get(var2.getName() + "/" + var1));
         }
      }
   }

   private void ensureAssetList(Version var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Minecraft version is required");
      }

      this.checkNotUnsupported(var1);
      if (!this.iv.contains(var1) || !this.iB.containsKey(var1)) {
         this.ir.resolveAssetPath(var1.getName(), "assets-list.txt");
         if (this.loadAssetList(var1)) {
            this.iv.add(var1);
         } else {
            String var2 = var1.getName() + "/catalog";
            FutureTask var3 = this.iz.computeIfAbsent(var2, var2x -> new FutureTask<>(() -> {
               this.iu.lockInterruptibly();

               try {
                  if (this.cz) {
                     throw new CancellationException("Vanilla assets provider closed");
                  } else if (!this.prepareCatalog(var1)) {
                     throw new IOException("Could not prepare vanilla asset catalog for " + var1.getName());
                  } else {
                     return null;
                  }
               } finally {
                  this.iu.unlock();
               }
            }));
            this.runRepair(var1, var2, var3, true);
         }
      }
   }

   private void scheduleRepair(Version var1, String var2, Function var3, boolean var4) {
      String var5 = var1.getName() + "/" + var2;
      FutureTask var6 = this.iz
         .computeIfAbsent(
            var5,
            var6x -> new FutureTask<>(
               () -> {
                  this.iu.lockInterruptibly();

                  try {
                     if (this.cz) {
                        throw new CancellationException("Vanilla assets provider closed");
                     }

                     if (!var4) {
                        Log.warn("Repairing vanilla asset: " + var5);
                     }

                     this.ir
                        .repairAsset(
                           var1.getName(),
                           var2,
                           var3xxx -> VanillaAssetCache.fetchVanillaAsset(this.iq.resolve("mojang"), var1.getName(), var2, var3xxx),
                           var2xxx -> {
                              try {
                                 var3.apply(var2xxx);
                              } catch (RuntimeException var4xxx) {
                                 throw new IOException("Invalid vanilla asset: " + var2, var4xxx);
                              }
                           }
                        );
                     return null;
                  } finally {
                     this.iu.unlock();
                  }
               }
            )
         );
      this.runRepair(var1, var5, var6, var4);
   }

   private void runRepair(Version var1, String var2, FutureTask var3, boolean var4) {
      if (Bukkit.isPrimaryThread() && Thread.currentThread() != this.iw) {
         SchedulerUtils.runSync(var3);
         throw this.recordFailure(var1, var2.substring(var1.getName().length() + 1), null, var4, var3);
      }

      var3.run();

      try {
         var3.get();
         this.iz.remove(var2, var3);
      } catch (InterruptedException var6) {
         Thread.currentThread().interrupt();
         throw this.recordFailure(var1, var2.substring(var1.getName().length() + 1), var6, var4, var3);
      } catch (ExecutionException | CancellationException var7) {
         throw this.recordFailure(var1, var2.substring(var1.getName().length() + 1), var7, var4, var3);
      }
   }

   private VanillaAssetException recordFailure(Version var1, String var2, Throwable var3, boolean var4, FutureTask var5) {
      if (var5 == null) {
         var5 = failedTask(var3 == null ? new IOException("Vanilla asset repair task is unavailable") : var3);
      }

      VanillaAssetException var6 = new VanillaAssetException(
         "Vanilla asset unavailable: " + var1.getName() + "/" + var2 + ". Selective repair is required.", var3, var5, var4, var1.getName() + "/" + var2
      );
      this.iy = var6;
      return var6;
   }

   private static FutureTask failedTask(Throwable var0) {
      FutureTask var1 = new FutureTask<>(() -> {
         if (var0 instanceof Exception var2) {
            throw var2;
         } else if (var0 instanceof Error var1x) {
            throw var1x;
         } else {
            throw new RuntimeException(var0);
         }
      });
      var1.run();
      return var1;
   }

   public void rethrowFailure() {
      if (this.iy != null) {
         throw this.iy;
      }
   }

   VanillaAssetException getFailure() {
      return this.iy;
   }

   void repairAll(VanillaAssetException var1, Consumer var2) {
      HashSet var3 = new HashSet(this.iz.values());
      var3.add(var1.iG);
      SchedulerUtils.runSync(() -> {
         Exception var3x = null;

         try {
            for (FutureTask var5 : var3) {
               if (this.cz) {
                  return;
               }

               var5.run();
               var5.get();
            }
         } catch (InterruptedException var6) {
            Thread.currentThread().interrupt();
            var3x = var6;
         } catch (ExecutionException | CancellationException var7) {
            var3x = var7;
         }

         Exception var8 = var3x;
         if (!this.cz) {
            SchedulerUtils.runOnMainThread(() -> {
               if (!this.cz) {
                  var2.accept(var8);
               }
            });
         }
      });
   }

   public void clearFailure() {
      if (this.cz) {
         throw new IllegalStateException("Vanilla assets provider closed");
      }

      for (FutureTask var2 : this.iz.values()) {
         if (!var2.isDone()) {
            throw new VanillaAssetException("Vanilla asset repair is still running.", null, var2);
         }
      }

      this.iz.clear();
      this.iy = null;
   }

   public boolean hasAsset(String var1) {
      return this.getCommonAssets().contains(var1);
   }

   public boolean hasVanillaModel(String var1) {
      NamespacedId var2 = NamespacedId.method386(var1, "minecraft");
      if (var2 == null) {
         return false;
      }

      String var3 = "models/" + var2.dl + ".json";
      return this.getAssetList(Version.get()).contains(var3);
   }

   public boolean hasVanillaTexture(String var1) {
      NamespacedId var2 = NamespacedId.method386(var1, "minecraft");
      if (var2 == null) {
         return false;
      }

      String var3 = "textures/" + var2.dl + ".png";
      return this.getAssetList(Version.get()).contains(var3);
   }

   @Nullable
   public VanillaModelResource getModelResource(String var1) {
      return this.getModelResourceFor(getTargetVersion(), var1);
   }

   @Nullable
   public VanillaModelResource getModelResourceFor(Version var1, String var2) {
      this.checkNotUnsupported(var1);
      NamespacedId var3 = NamespacedId.method386(var2, "minecraft");
      if (var3 != null && "minecraft".equals(var3.dk)) {
         String var4 = "assets/minecraft/models/" + var3.dl + ".json";
         JSONObject var5 = this.readJson(var4, var1);
         return var5 == null ? null : new VanillaModelResource(var4, iE.toJsonTree(var5).getAsJsonObject());
      } else {
         return null;
      }
   }

   @Nullable
   @Internal
   public JSONObject getModelJsonFor(Version var1, String var2) {
      this.checkNotUnsupported(var1);
      NamespacedId var3 = NamespacedId.method386(var2, "minecraft");
      return var3 != null && "minecraft".equals(var3.dk) ? this.readJson("assets/minecraft/models/" + var3.dl + ".json", var1) : null;
   }

   @Nullable
   @Internal
   public JSONObject getModelJson(String var1) {
      return this.getModelJsonFor(getTargetVersion(), var1);
   }

   @Nullable
   @Internal
   public JSONObject getItemModelJson(Material var1) {
      return this.getModelJson("minecraft:item/" + var1.name().toLowerCase(Locale.ROOT));
   }

   private void checkNotUnsupported(@Nullable Version var1) {
      if (var1 != null && this.iA.contains(var1)) {
         throw new IllegalStateException(
            "Official Minecraft assets for " + var1.getName() + " are not published by Mojang; disable that exact client target or retry after its release."
         );
      }
   }

   @Nullable
   public VanillaModelResource getItemModelResource(Material var1) {
      return this.getModelResource("minecraft:item/" + var1.name().toLowerCase(Locale.ROOT));
   }
}
