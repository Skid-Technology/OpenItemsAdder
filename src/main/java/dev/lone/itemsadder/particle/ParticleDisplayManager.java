package dev.lone.itemsadder.particle;

import beer.devs.fastnbt.nms.Version;
import beer.devs.nativepackets.api.SchedulerAccess;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.DisplayPoseSnapshot;
import dev.lone.itemsadder.entity.TrackedDisplayEntity;
import dev.lone.itemsadder.network.ConnectionRecord;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.network.ViaVersionIntegration;
import dev.lone.itemsadder.resourcepack.AssetBundle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public final class ParticleDisplayManager {
   public static final int axQ = 128;
   public static final int axR = 64;
   public static final int axS = 2048;
   public static final int axT = 8192;
   private final ParticleCapacity axU = new ParticleCapacity(8192);
   private final Set axV = ConcurrentHashMap.newKeySet();
   private final Map axW = new HashMap();
   private final Map axX = new HashMap();
   private final LongAdder axY = new LongAdder();
   private final LongAdder axZ = new LongAdder();
   private final LongAdder aya = new LongAdder();
   private final LongAdder ayb = new LongAdder();
   private final LongAdder ayc = new LongAdder();
   private final LongAdder ayd = new LongAdder();
   private final LongAdder aye = new LongAdder();
   private final LongAdder ayf = new LongAdder();
   private final LongAdder ayg = new LongAdder();

   public synchronized ParticlePerfStats IN() {
      return new ParticlePerfStats(
         this.axV.size(),
         this.axV.stream().mapToInt(var0 -> var0.ayP).sum(),
         this.axW.values().stream().mapToInt(Integer::intValue).sum(),
         this.axY.sum(),
         this.ayc.sum(),
         this.ayd.sum(),
         this.aye.sum(),
         this.ayf.sum(),
         this.axZ.sum(),
         this.ayg.sum(),
         this.aya.sum(),
         this.ayb.sum()
      );
   }

   public synchronized ParticleEmitter method2151(String var1, Location var2, Collection var3, int var4) {
      return this.method2154(var1, var2, var3, var4, null, null);
   }

   public synchronized ParticleEmitter method2152(String var1, Entity var2, Collection var3, int var4) {
      if (!IQ()) {
         return new ParticleEmitter(this, null);
      } else if (!IP().ownsEntity(var2)) {
         throw new IllegalStateException("Start attached particles on their parent entity scheduler");
      } else if (!var2.isValid()) {
         throw new IllegalArgumentException("Invalid particle parent");
      } else {
         return this.method2154(var1, var2.getLocation(), var3, var4, var2, null);
      }
   }

   synchronized ParticleEmitter method2153(String var1, TrackedDisplayEntity var2, Collection var3, int var4) {
      if (!IQ()) {
         return new ParticleEmitter(this, var2);
      } else {
         Entity var5 = var2.getEntity();
         if (!IP().ownsEntity(var5)) {
            throw new IllegalStateException("Start following particles on their owner entity scheduler");
         } else {
            DisplayPoseSnapshot var6 = var2.getSnapshot();
            if (var6 != null && var5.isValid() && !var5.isDead() && var5.getWorld().getUID().equals(var6.entityId())) {
               return this.method2154(var1, new Location(var5.getWorld(), var6.ka(), var6.kb(), var6.kc()), var3, var4, var5, var2);
            } else {
               throw new IllegalArgumentException(
                  "Invalid particle bone anchor (pose="
                     + (var6 != null)
                     + ", ownerValid="
                     + var5.isValid()
                     + ", ownerDead="
                     + var5.isDead()
                     + ", worldMatches="
                     + (var6 != null && var5.getWorld().getUID().equals(var6.entityId()))
                     + ")"
               );
            }
         }
      }
   }

   private ParticleEmitter method2154(String var1, Location var2, Collection var3, int var4, Entity var5, TrackedDisplayEntity var6) {
      if (!IQ()) {
         return new ParticleEmitter(this, var6);
      }

      if (var2.getWorld() != null && Double.isFinite(var2.getX()) && Double.isFinite(var2.getY()) && Double.isFinite(var2.getZ())) {
         if (var5 == null && !IP().ownsRegion(var2)) {
            throw new IllegalStateException("Start particles on their origin region");
         }

         if (var3.size() > 64) {
            throw new IllegalArgumentException("Too many particle viewers");
         }

         AssetBundle var7 = (AssetBundle)ContentManager.method8084().field85.getAssets().bundles().get(var1);
         if (var7 == null) {
            throw new IllegalArgumentException("Unknown particle effect " + var1);
         }

         ArrayList var8 = new ArrayList();

         for (UUID var10 : new LinkedHashSet(var3)) {
            ConnectionRecord var11 = NativePacketsHolder.PE().method1870(var10);
            if (var11 != null && bR(ViaVersionIntegration.method4866(var10))) {
               var8.add(var11);
            }
         }

         if (var8.isEmpty()) {
            return new ParticleEmitter(this, var6);
         }

         if (this.axV.size() >= 128) {
            throw new IllegalStateException("Active particle effect budget exceeded");
         }

         ParticleEmitter var15 = new ParticleEmitter(this, var7, var2.clone(), var8, var4, var5, var6);
         this.axV.add(var15);

         try {
            synchronized (var15.ayF) {
               if (!var15.ayI.get()) {
                  var15.ayF.start();
                  var15.Je();
               }
            }

            var15.Jg();
            var15.Jf();
            var15.dH();
            return var15;
         } catch (RuntimeException var14) {
            var15.close();
            throw var14;
         }
      } else {
         throw new IllegalArgumentException("Invalid particle origin");
      }
   }

   public void method2155() {
      for (ParticleEmitter var2 : List.copyOf(this.axV)) {
         var2.close();
      }
   }

   private static SchedulerAccess IP() {
      return NativePacketsHolder.PE().IP();
   }

   private static boolean IQ() {
      return NativePacketsHolder.aN() && NativePacketsHolder.PD().isSupported();
   }

   static boolean bR(int var0) {
      return var0 >= Version.v1_21_4.getProtocol();
   }

   private synchronized ViewerBudgetLease method2156(ConnectionRecord var1) {
      return this.axX.computeIfAbsent(var1, var0 -> new ViewerBudget(16384, 4096)).JP();
   }

   private synchronized void method2157(ConnectionRecord var1, ViewerBudgetLease var2) {
      var2.close();
      this.axX.computeIfPresent(var1, (var0, var1x) -> (ViewerBudget)(var1x.JQ() == 0 ? null : var1x));
   }

   private synchronized boolean method2158(ConnectionRecord var1) {
      int var2 = this.axW.getOrDefault(var1, 0);
      if (var2 >= 2048) {
         return false;
      }

      this.axW.put(var1, var2 + 1);
      return true;
   }

   private synchronized void method2159(ConnectionRecord var1, int var2) {
      if (var2 != 0) {
         this.axW.computeIfPresent(var1, (var1x, var2x) -> var2x == var2 ? null : var2x - var2);
      }
   }

   private static double method2160(double var0) {
      return var0 * var0;
   }
}
