package dev.lone.itemsadder.particle;

import beer.devs.nativepackets.api.SchedulerAccess.ScheduledTask;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.anim.DisplayTransform;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.DisplayPoseSnapshot;
import dev.lone.itemsadder.entity.TrackedDisplayEntity;
import dev.lone.itemsadder.network.ConnectionRecord;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.resourcepack.AssetBundle;
import dev.lone.itemsadder.util.Vec3i;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public final class ParticleEmitter implements AutoCloseable {
   private final Location ayw;
   private final Entity ayx;
   private final TrackedDisplayEntity ayy;
   private final boolean ayz;
   private final boolean ayA;
   private final Map ayB;
   private final Map ayC;
   private int ayD;
   private final AssetBundle ayE;
   private final ParticleSimulation ayF;
   private final Map ayG;
   private final List ayH;
   private final AtomicBoolean ayI;
   private final AtomicBoolean ayJ;
   private volatile ScheduledTask ayK;
   private int ayL;
   private int ayM;
   private long ayN;
   private long ayO;
   private volatile int ayP;
   private volatile int ayQ;

   private ParticleEmitter(ParticleDisplayManager var1, TrackedDisplayEntity var2) {
      this.ayR = var1;
      this.ayB = new HashMap();
      this.ayC = new HashMap();
      this.ayG = new HashMap();
      this.ayH = new ArrayList();
      this.ayI = new AtomicBoolean();
      this.ayJ = new AtomicBoolean();
      this.ayw = null;
      this.ayx = null;
      this.ayy = null;
      this.ayE = null;
      this.ayF = null;
      this.ayz = false;
      this.ayA = false;
      this.ayI.set(true);
      if (var2 != null) {
         var2.close();
      }
   }

   private ParticleEmitter(ParticleDisplayManager var1, AssetBundle var2, Location var3, Collection var4, int var5, Entity var6, TrackedDisplayEntity var7) {
      this.ayR = var1;
      this.ayB = new HashMap();
      this.ayC = new HashMap();
      this.ayG = new HashMap();
      this.ayH = new ArrayList();
      this.ayI = new AtomicBoolean();
      this.ayJ = new AtomicBoolean();
      this.ayE = var2;
      this.ayw = var3;
      this.ayx = var6;
      this.ayy = var7;
      Map var8 = var2.definitions();
      this.ayA = var8.values().stream().anyMatch(var0 -> var0.awd);
      this.ayz = var8.values().stream().anyMatch(var0 -> var0.avk.equals("entity_aabb"));
      this.ayG.putAll(var2.displays());
      this.ayF = new ParticleSimulation((ParticleEffectDefinition)var8.get(var2.root()), var8, var5, 2048, var1.axU);
      if (var8.values().stream().anyMatch(var0 -> !var0.awm.isEmpty() || !var0.awn.isEmpty())) {
         this.ayF.method2174(this::method1799);
      }

      if (var8.values().stream().anyMatch(var0 -> var0.awo)) {
         this.ayF.method2175(this::method1800);
      }

      if (var6 != null) {
         this.method1798(var3, var7 == null ? null : var7.getSnapshot());
      }

      for (ConnectionRecord var10 : var4) {
         this.ayH.add(new ParticleDisplay(this, var10));
      }
   }

   public boolean Jd() {
      return this.ayI.get();
   }

   public ParticleEmitter bS(int var1) {
      this.ayQ = ParticleEmitterManager.method1713(var1);
      return this;
   }

   private void dH() {
      if (!this.ayI.get()) {
         this.ayK = ParticleDisplayManager.IP().globalLater(Main.getInstance(), () -> {
            if (!this.ayI.get()) {
               if (this.ayJ.compareAndSet(false, true)) {
                  Runnable var1 = () -> {
                     try {
                        this.tick();
                     } catch (RuntimeException var5) {
                        Main.getInstance().getLogger().warning("Particle effect stopped: " + var5.getMessage());
                        this.close();
                     } finally {
                        this.ayJ.set(false);
                     }
                  };
                  if (this.ayx != null) {
                     if (ParticleDisplayManager.IP().ownsEntity(this.ayx)) {
                        var1.run();
                     } else {
                        ParticleDisplayManager.IP().entity(Main.getInstance(), this.ayx, var1, () -> {
                           this.ayJ.set(false);
                           this.close();
                        });
                     }
                  } else if (ParticleDisplayManager.IP().ownsRegion(this.ayw)) {
                     var1.run();
                  } else {
                     ParticleDisplayManager.IP().region(Main.getInstance(), this.ayw, var1);
                  }
               } else {
                  this.ayR.ayb.increment();
               }

               this.dH();
            }
         }, 1L);
      }
   }

   private void tick() {
      if (!this.ayI.get()) {
         if (this.ayQ > 0 && --this.ayQ == 0) {
            this.close();
         } else if (this.ayx == null || this.ayx.isValid() && !this.ayx.isDead() && this.ayx.getWorld() == this.ayw.getWorld()) {
            DisplayPoseSnapshot var1 = this.ayy == null ? null : this.ayy.getSnapshot();
            if (this.ayy == null || var1 != null && this.ayw.getWorld().getUID().equals(var1.entityId())) {
               boolean var2;
               synchronized (this.ayF) {
                  if (this.ayI.get()) {
                     return;
                  }

                  if (this.ayx != null) {
                     this.method1798(this.ayx.getLocation(), var1);
                  }

                  this.ayB.clear();
                  this.ayC.clear();
                  this.ayD = 0;
                  this.ayL += 3;
                  long var4 = System.nanoTime();

                  try {
                     while (this.ayL >= 2) {
                        this.ayF.tick();
                        this.ayR.axY.increment();
                        this.ayL -= 2;
                     }
                  } finally {
                     this.ayR.ayc.add(System.nanoTime() - var4);
                  }

                  this.Je();
                  var2 = this.ayF.JD();
               }

               this.Jg();
               if (++this.ayM % 2 == 0) {
                  this.Jf();
               }

               if (var2) {
                  this.close();
               }
            } else {
               this.close();
            }
         } else {
            this.close();
         }
      }
   }

   private boolean method1797(UUID var1) {
      DisplayPoseSnapshot var2 = this.ayy == null ? null : this.ayy.getSnapshot();
      return this.ayy == null || var2 != null && var2.HK().contains(var1);
   }

   private void method1798(Location var1, DisplayPoseSnapshot var2) {
      if (this.ayy != null) {
         if (var2 == null) {
            throw new IllegalStateException("Particle bone anchor was removed");
         }

         this.ayF.azx = var2.toParentPose(this.ayw.getX(), this.ayw.getY(), this.ayw.getZ());
      } else {
         Quaterniond var3 = new Quaterniond().rotationY(Math.toRadians(-var1.getYaw())).rotateX(Math.toRadians(var1.getPitch()));
         this.ayF.azx = new ParentPose(
            var1.getX() - this.ayw.getX(), var1.getY() - this.ayw.getY(), var1.getZ() - this.ayw.getZ(), var3.x, var3.y, var3.z, var3.w
         );
      }

      if (this.ayA) {
         Vector var4 = this.ayx.getVelocity();
         this.ayF.method2172(var4.getX() * 20.0, var4.getY() * 20.0, var4.getZ() * 20.0);
      }

      if (this.ayz) {
         BoundingBox var5 = this.ayx.getBoundingBox();
         this.ayF
            .method2173(
               new ParticleBounds(
                  var5.getMinX() - this.ayw.getX(),
                  var5.getMinY() - this.ayw.getY(),
                  var5.getMinZ() - this.ayw.getZ(),
                  var5.getMaxX() - this.ayw.getX(),
                  var5.getMaxY() - this.ayw.getY(),
                  var5.getMaxZ() - this.ayw.getZ()
               )
            );
      }
   }

   private IllegalStateException dR(String var1) {
      this.ayR.ayb.increment();
      return new IllegalStateException(var1);
   }

   private String method1799(double var1, double var3, double var5) {
      if (++this.ayD > 16384) {
         throw this.dR("Particle world query work budget exceeded");
      }

      var1 += this.ayw.getX();
      var3 += this.ayw.getY();
      var5 += this.ayw.getZ();
      if (Double.isFinite(var1) && Double.isFinite(var3) && Double.isFinite(var5) && !(Math.abs(var1) > 3.0E7) && !(Math.abs(var5) > 3.0E7)) {
         World var7 = this.ayw.getWorld();
         if (!(var3 < var7.getMinHeight()) && !(var3 >= var7.getMaxHeight())) {
            Vec3i var8 = new Vec3i((int)Math.floor(var1), (int)Math.floor(var3), (int)Math.floor(var5));
            return this.ayB.computeIfAbsent(var8, var2 -> {
               if (this.ayB.size() >= 4096) {
                  throw this.dR("Particle block snapshot budget exceeded");
               } else {
                  Location var3x = new Location(var7, var2.x, var2.y, var2.z);
                  if (ParticleDisplayManager.IP().ownsRegion(var3x) && var7.isChunkLoaded(var2.x >> 4, var2.z >> 4)) {
                     return var7.getBlockAt(var2.x, var2.y, var2.z).getType().getKey().toString();
                  } else {
                     throw new IllegalStateException("Particle block query requires a loaded, owned region");
                  }
               }
            });
         } else {
            return "minecraft:void_air";
         }
      } else {
         throw new IllegalStateException("Particle block query outside world bounds");
      }
   }

   private List method1800(ParticleBounds var1) {
      World var2 = this.ayw.getWorld();
      double var3 = var1.HL() + this.ayw.getX();
      double var5 = var1.HO() + this.ayw.getX();
      double var7 = var1.HN() + this.ayw.getZ();
      double var9 = var1.HQ() + this.ayw.getZ();
      if (!(Math.abs(var3) > 3.0E7) && !(Math.abs(var5) > 3.0E7) && !(Math.abs(var7) > 3.0E7) && !(Math.abs(var9) > 3.0E7)) {
         int var11 = (int)Math.floor(var3) - 1;
         int var12 = (int)Math.floor(var5) + 1;
         int var13 = (int)Math.floor(var7) - 1;
         int var14 = (int)Math.floor(var9) + 1;
         double var15 = var1.HM() + this.ayw.getY() - 1.0;
         double var17 = var1.HP() + this.ayw.getY() + 1.0;
         if (!(var17 < var2.getMinHeight()) && !(var15 >= var2.getMaxHeight())) {
            int var19 = (int)Math.floor(Math.max(var2.getMinHeight(), var15));
            int var20 = (int)Math.floor(Math.min(var2.getMaxHeight() - 1, var17));
            long var21 = var12 - var11 + 1L;
            long var23 = var20 - var19 + 1L;
            long var25 = var14 - var13 + 1L;
            if (var21 <= 4096L && var23 <= 4096L && var25 <= 4096L && var21 * var23 * var25 <= 4096L) {
               this.ayD += (int)(var21 * var23 * var25);
               if (this.ayD > 16384) {
                  throw this.dR("Particle world query work budget exceeded");
               }

               ArrayList var27 = new ArrayList();

               for (int var28 = var11; var28 <= var12; var28++) {
                  for (int var29 = var19; var29 <= var20; var29++) {
                     for (int var30 = var13; var30 <= var14; var30++) {
                        Vec3i var31 = new Vec3i(var28, var29, var30);
                        List var32 = this.ayC
                           .computeIfAbsent(
                              var31,
                              var2x -> {
                                 if (this.ayC.size() >= 4096) {
                                    throw this.dR("Particle collision snapshot budget exceeded");
                                 }

                                 for (int var3x = var2x.x - 1 >> 4; var3x <= var2x.x + 1 >> 4; var3x++) {
                                    for (int var4 = var2x.z - 1 >> 4; var4 <= var2x.z + 1 >> 4; var4++) {
                                       if (!var2.isChunkLoaded(var3x, var4)
                                          || !ParticleDisplayManager.IP().ownsRegion(new Location(var2, var3x * 16, var2x.y, var4 * 16))) {
                                          throw new IllegalStateException("Particle collision requires loaded, owned neighboring regions");
                                       }
                                    }
                                 }

                                 ArrayList var6 = new ArrayList();

                                 for (BoundingBox var5x : var2.getBlockAt(var2x.x, var2x.y, var2x.z).getCollisionShape().getBoundingBoxes()) {
                                    var6.add(
                                       new ParticleBounds(
                                          var5x.getMinX() + var2x.x - this.ayw.getX(),
                                          var5x.getMinY() + var2x.y - this.ayw.getY(),
                                          var5x.getMinZ() + var2x.z - this.ayw.getZ(),
                                          var5x.getMaxX() + var2x.x - this.ayw.getX(),
                                          var5x.getMaxY() + var2x.y - this.ayw.getY(),
                                          var5x.getMaxZ() + var2x.z - this.ayw.getZ()
                                       )
                                    );
                                 }

                                 return List.copyOf(var6);
                              }
                           );
                        if (var27.size() + var32.size() > 4096) {
                           throw this.dR("Particle collision shape budget exceeded");
                        }

                        var27.addAll(var32);
                     }
                  }
               }

               return var27;
            } else {
               throw this.dR("Particle collision sweep exceeds 4096 block cells");
            }
         } else {
            return List.of();
         }
      } else {
         throw new IllegalStateException("Particle collision outside world bounds");
      }
   }

   private void Je() {
      this.ayP = this.ayF.JC();
      long var1 = this.ayF.Jc();
      this.ayR.ayb.add(var1 - this.ayO);
      this.ayO = var1;
   }

   private void Jf() {
      ArrayList var1 = new ArrayList();
      synchronized (this.ayF) {
         if (this.ayI.get()) {
            return;
         }

         this.method1801(this.ayF, var1);
      }

      CurveFrame var9 = new CurveFrame(++this.ayN, List.copyOf(var1));
      boolean var3 = false;

      for (ParticleDisplay var5 : this.ayH) {
         if (!NativePacketsHolder.PE().method1873(var5.ayS)) {
            var5.bD(null);
         } else {
            var3 = true;
            Player var6 = NativePacketsHolder.PE().method1885(var5.ayS.bk(), var5.ayS.bl());
            if (var6 != null && var5.method2165(var9)) {
               Runnable var7 = () -> {
                  try {
                     var5.bB(var6);
                  } catch (RuntimeException var7x) {
                     Main.getInstance().getLogger().warning("Particle rendering stopped: " + var7x.getMessage());
                     this.close();
                  } finally {
                     var5.ayU.set(false);
                  }
               };
               if (ParticleDisplayManager.IP().ownsEntity(var6)) {
                  var7.run();
               } else {
                  ParticleDisplayManager.IP().entity(Main.getInstance(), var6, var7, () -> var5.ayU.set(false));
               }
            }
         }
      }

      if (!var3) {
         this.close();
      }
   }

   private void Jg() {
      List var1;
      synchronized (this.ayF) {
         var1 = this.ayF.JE();
      }

      if (!var1.isEmpty()) {
         for (ParticleDisplay var3 : this.ayH) {
            Player var4 = NativePacketsHolder.PE().method1885(var3.ayS.bk(), var3.ayS.bl());
            if (var4 != null) {
               if (!var3.ayV.compareAndSet(false, true)) {
                  this.ayR.ayb.add(var1.size());
               } else {
                  Runnable var5 = () -> {
                     try {
                        if (this.ayI.get()
                           || !this.method1797(var3.ayS.bk())
                           || !NativePacketsHolder.PE().method1873(var3.ayS)
                           || var4.isDead()
                           || var4.getWorld() != this.ayw.getWorld()
                           || ContentManager.method8084().aL == null
                           || !ContentManager.method8084().aL.isInstalled(var4)) {
                           return;
                        }

                        Location var4x = var4.getEyeLocation();

                        for (KeyedVector3 var6 : var1) {
                           Location var7 = this.ayw.clone().add(var6.ka(), var6.kb(), var6.kc());
                           if (!(var4x.distanceSquared(var7) > 256.0)) {
                              if (!var3.ayT.method1796(var4.getTicksLived() / 2, 1, 1)) {
                                 this.ayR.ayb.increment();
                              } else {
                                 var4.playSound(var7, var6.lS(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                                 this.ayR.ayg.increment();
                              }
                           }
                        }
                     } finally {
                        var3.ayV.set(false);
                     }
                  };
                  if (ParticleDisplayManager.IP().ownsEntity(var4)) {
                     var5.run();
                  } else {
                     ParticleDisplayManager.IP().entity(Main.getInstance(), var4, var5, () -> var3.ayV.set(false));
                  }
               }
            }
         }
      }
   }

   private void method1801(ParticleSimulation var1, List var2) {
      Vector3d var3 = new Vector3d();

      for (ParticleData var5 : var1.Jy()) {
         if (var2.size() >= 2048) {
            this.ayR.ayb.increment();
            break;
         }

         double var6 = ((DisplayTransform)this.ayG.get(var1.azC.avh)).method2082(var5.azq, var5.azr);
         var1.method2171(var5, var3);
         var2.add(
            new ParticleState(
               new ParticleId(var1.azG, var5.azh),
               var1.azC.avh,
               var3.x,
               var3.y,
               var3.z,
               var5.Jc,
               var5.ave,
               var5.Jd,
               var5.azq,
               var5.azr,
               var5.azs,
               var5.DL,
               var5.Mw,
               var5.azv,
               var6,
               var1.azC.awb ? var1.azx : null
            )
         );
      }

      for (ParticleSimulation var9 : var1.Jz()) {
         this.method1801(var9, var2);
      }
   }

   @Override
   public void close() {
      if (this.ayI.compareAndSet(false, true)) {
         if (this.ayy != null) {
            this.ayy.close();
         }

         if (this.ayK != null) {
            this.ayK.cancel();
         }

         this.ayR.axV.remove(this);
         synchronized (this.ayF) {
            this.ayF.stop();
            this.ayP = 0;
            this.ayB.clear();
            this.ayC.clear();
         }

         for (ParticleDisplay var2 : this.ayH) {
            Player var3 = NativePacketsHolder.PE().method1885(var2.ayS.bk(), var2.ayS.bl());
            if (var3 == null) {
               var2.bD(null);
            } else {
               var2.bD(var3);
            }
         }
      }
   }
}
