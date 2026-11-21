package dev.lone.itemsadder.particle;

import beer.devs.nativepackets.api.SchedulerAccess;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.TrackedDisplayEntity;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.BlockFaceUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Location;

public final class ParticleEmitterManager implements AutoCloseable {
   public static final int axu = 60;
   public static final int axv = 1200;
   private final Set axw = new HashSet();
   private final Set axx = new HashSet();
   private boolean cz;

   public static boolean dP(String var0) {
      if (var0 != null && var0.contains(":") && !var0.regionMatches(true, 0, "minecraft:", 0, 10)) {
         if (var0.matches("[a-z0-9_-]{1,64}:[a-z0-9_/-]{1,128}")
            && !Arrays.stream(var0.substring(var0.indexOf(58) + 1).split("/", -1)).anyMatch(String::isEmpty)) {
            return true;
         } else {
            throw new IllegalArgumentException("Invalid custom particle ID " + var0);
         }
      } else {
         return false;
      }
   }

   public static int method1713(double var0) {
      if (Double.isFinite(var0) && var0 == Math.rint(var0) && !(var0 < 1.0) && !(var0 > 1200.0)) {
         return (int)var0;
      } else {
         throw new IllegalArgumentException("Particle duration_ticks must be an integer in 1..1200");
      }
   }

   public void method1714(String var1, Location var2, Collection var3, int var4) {
      if (!dP(var1)) {
         throw new IllegalArgumentException("Expected a custom particle ID");
      }

      method1713(var4);
      synchronized (this) {
         if (this.cz) {
            return;
         }
      }

      Location var5 = var2.clone();
      if (var5.getWorld() != null && Double.isFinite(var5.getX()) && Double.isFinite(var5.getY()) && Double.isFinite(var5.getZ())) {
         List var6 = var3 == null ? null : var3.stream().distinct().limit(64L).toList();
         Runnable var7 = () -> {
            synchronized (this) {
               if (this.cz) {
                  return;
               }
            }

            List var5x = var6 == null ? BlockFaceUtils.playersWithin(var5, 32).stream().limit(64L).map(var0 -> var0.getUniqueId()).toList() : var6;
            if (!var5x.isEmpty()) {
               try {
                  this.method1716(ContentManager.method8084().field86.method2151(var1, var5, var5x, ThreadLocalRandom.current().nextInt()).bS(var4));
               } catch (RuntimeException var7x) {
                  Main.getInstance().getLogger().warning("Custom particle " + var1 + " was not started: " + var7x.getMessage());
               }
            }
         };
         SchedulerAccess var8 = NativePacketsHolder.PE().IP();
         if (var8.ownsRegion(var5)) {
            var7.run();
         } else {
            var8.region(Main.getInstance(), var5, var7);
         }
      } else {
         throw new IllegalArgumentException("Invalid particle origin");
      }
   }

   public void method1715(String var1, TrackedDisplayEntity var2, Collection var3, int var4) {
      if (!dP(var1)) {
         throw new IllegalArgumentException("Expected a custom particle ID");
      }

      method1713(var4);
      List var5 = var3.stream().distinct().limit(64L).toList();
      synchronized (this) {
         if (this.cz || var5.isEmpty()) {
            var2.close();
            return;
         }

         this.axx.removeIf(TrackedDisplayEntity::isActive);
         this.axx.add(var2);
      }

      Runnable var6 = () -> {
         synchronized (this) {
            if (this.cz || var2.isActive()) {
               return;
            }
         }

         try {
            this.method1716(ContentManager.method8084().field86.method2153(var1, var2, var5, ThreadLocalRandom.current().nextInt()).bS(var4));
         } catch (RuntimeException var9) {
            RuntimeException var5x = var9;
            var2.close();
            synchronized (this) {
               if (!this.cz) {
                  Main.getInstance().getLogger().warning("Custom particle " + var1 + " was not started: " + var5x.getMessage());
               }
            }
         }
      };
      SchedulerAccess var7 = NativePacketsHolder.PE().IP();
      if (var7.ownsEntity(var2.getEntity()) && var2.getEntity().isValid()) {
         var6.run();
      } else {
         var7.entity(Main.getInstance(), var2.getEntity(), var6, var2::close);
      }
   }

   synchronized void method1716(ParticleEmitter var1) {
      if (this.cz) {
         var1.close();
      } else {
         this.axw.removeIf(ParticleEmitter::Jd);
         this.axw.add(var1);
      }
   }

   @Override
   public synchronized void close() {
      this.cz = true;
      this.axx.forEach(TrackedDisplayEntity::close);
      this.axx.clear();

      for (ParticleEmitter var2 : this.axw) {
         var2.close();
      }

      this.axw.clear();
   }
}
