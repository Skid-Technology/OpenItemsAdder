package dev.lone.itemsadder.entity;

import beer.devs.nativepackets.api.ConnectionHandle;
import beer.devs.nativepackets.api.FastPacketCapability;
import dev.lone.itemsadder.anim.EntityLifecycleState;
import dev.lone.itemsadder.block.CustomBlockListener;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.network.ConnectionRecord;
import dev.lone.itemsadder.network.FastPacketAction;
import dev.lone.itemsadder.network.NativePacketsBridge;
import dev.lone.itemsadder.network.NativePacketsHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class EntityViewer {
   public Player li;
   public final UUID uz;
   @Nullable
   private final ConnectionRecord uA;
   private final long uB;
   private final CustomEntityImpl uC;
   private final RotationPolicy uD;
   private volatile boolean uE;
   private volatile boolean uF;
   private volatile boolean uG;
   private volatile boolean uH;
   private volatile boolean uI;
   private volatile boolean uJ;
   private boolean uK;
   private boolean uL = true;
   private volatile int uM = 1;
   private final int uN;
   @Nullable
   private Location uO;
   private boolean uP;
   double uQ;
   double uR;
   private boolean uS;
   private boolean uT;
   private volatile double uU = 64.0;
   private volatile ViewerVisibility uV;
   boolean uW;
   private boolean uX;
   private boolean uY;
   private List uZ;
   private List va;
   private boolean vb;

   public EntityViewer(Player var1, CustomEntityImpl var2) {
      this(var1, var2, var1.getUniqueId().hashCode());
   }

   EntityViewer(Player var1, CustomEntityImpl var2, int var3) {
      this(
         var1,
         var1.getUniqueId(),
         var2,
         var3,
         NativePacketsHolder.aN() ? NativePacketsHolder.PE().cB(var1) : null,
         NativePacketsHolder.aN() ? NativePacketsHolder.PE().method1886(var1.getUniqueId()) : RotationPolicy.COMPATIBLE
      );
   }

   private EntityViewer(Player var1, UUID var2, CustomEntityImpl var3, int var4, @Nullable ConnectionRecord var5, RotationPolicy var6) {
      this.li = var1;
      this.uz = var2;
      this.uC = var3;
      this.uA = var5;
      this.uB = var3.ro.jY();
      this.uN = var4;
      this.uD = var6;
      this.uE = true;
   }

   @Nullable
   private EntityViewer kP() {
      if (!NativePacketsHolder.aN()) {
         return this;
      } else {
         Player var1 = NativePacketsHolder.PE().method1884(this.uz);
         ConnectionRecord var2 = NativePacketsHolder.PE().method1870(this.uz);
         if (var1 != null && var2 != null) {
            RotationPolicy var3 = NativePacketsHolder.PE().method1886(this.uz);
            return this.li == var1 && this.uA == var2 && this.uD == var3 ? this : new EntityViewer(var1, this.uz, this.uC, this.uN, var2, var3);
         } else {
            return null;
         }
      }
   }

   private boolean method8550(EntityViewer var1) {
      return this.li == var1.li && this.uA == var1.uA && this.uD == var1.uD && this.uB == var1.uB;
   }

   @Nullable
   public ConnectionRecord getConnection() {
      return NativePacketsHolder.aN() ? this.method8553(NativePacketsHolder.PE()) : null;
   }

   public void kR() {
      this.vb = true;
   }

   public void kS() {
      if (this.uY) {
         throw new IllegalStateException("Nested display frame");
      }

      if (this.uI) {
         synchronized (this) {
            this.uH = true;
            this.uI = false;
         }
      }

      this.uY = true;
   }

   public void method8551(Object var1, Runnable var2) {
      if (this.uZ == null) {
         if (!this.uY) {
            ConnectionRecord var3 = this.getConnection();
            if (var3 != null && FastPacketAction.method3167(NativePacketsHolder.PE(), var3, var1, this::lb) && this.getConnection() == var3) {
               var2.run();
            }

            return;
         }

         this.uZ = new ArrayList();
         this.va = new ArrayList();
      }

      this.uZ.add(var1);
      this.va.add(var2);
   }

   public void method8552(boolean var1) {
      if (this.uY) {
         List var2 = this.uZ;
         List var3 = this.va;
         this.uY = false;
         this.uZ = null;
         this.va = null;
         if (var1 && (this.vb || var2 != null && !var2.isEmpty())) {
            ConnectionRecord var4 = this.getConnection();
            if (var4 == null) {
               if (this.uH) {
                  this.lb();
               }
            } else if (this.vb) {
               this.uC.updateViewer(this);
               this.vb = false;
            } else {
               boolean var5 = var2.size() > 1
                  && (Settings.get().customEntitiesBundlePackets || this.uC.countMountedRiders() > 0 || this.uC.rW != null && this.uC.rW.method8615(this));
               NativePacketsBridge var6 = NativePacketsHolder.PE();
               ConnectionHandle var7 = var4.PC();
               boolean var8 = false;
               boolean var9 = var7.supportsFast(FastPacketCapability.MIXED_BATCH) && (!var5 || var7.supportsFast(FastPacketCapability.BUNDLE));

               for (Object var11 : var2) {
                  if (var11 instanceof FastPacketAction var12) {
                     var8 = true;
                     if (!var12.method3162(var7)) {
                        var9 = false;
                     }
                  }
               }

               if (var9 && var8) {
                  List var16 = var2;

                  try {
                     if (var7.fastBatch(var5, true, var2x -> {
                        try {
                           for (Object var4x : var16) {
                              if (var4x instanceof FastPacketAction var5x) {
                                 var5x.method3163(var2x);
                              } else {
                                 var2x.nativePacket(var4x);
                              }
                           }
                        } catch (RuntimeException | Error var6x) {
                           this.lb();
                           throw var6x;
                        }
                     }) && this.getConnection() == var4) {
                        var3.forEach(Runnable::run);
                     } else if (this.uH) {
                        this.lb();
                     }
                  } catch (RuntimeException | Error var13) {
                     this.lb();
                     throw var13;
                  }
               } else {
                  if (var8) {
                     ArrayList var14 = new ArrayList(var2.size());

                     for (Object var18 : var2) {
                        var14.add(FastPacketAction.method3166(var18, var6.Pv()));
                     }

                     var2 = var14;
                  }

                  boolean var15 = var5 ? var6.method1871(var4, var6.Pv().bundle(var2)) : var6.method1872(var4, var2);
                  if (var15 && this.getConnection() == var4) {
                     var3.forEach(Runnable::run);
                  } else if (this.uH) {
                     this.lb();
                  }
               }
            }
         } else {
            if (!var1 && this.uH) {
               this.lb();
            }
         }
      }
   }

   @Nullable
   private ConnectionRecord method8553(NativePacketsBridge var1) {
      if (this.uC.isViewerAllowed(this.uz)
         && this.uC.hasActiveViewerRequest(this.uz)
         && !this.uC.removed
         && this.uC.ro.jY() == this.uB
         && this.uC.ro.jX() != EntityLifecycleState.DISPOSING
         && this.uC.ro.jX() != EntityLifecycleState.DISPOSED) {
         return this.uA != null && var1.method1873(this.uA) ? this.uA : null;
      } else {
         return null;
      }
   }

   @Nullable
   ConnectionRecord kT() {
      return NativePacketsHolder.aN() ? this.method8554(NativePacketsHolder.PE()) : null;
   }

   @Nullable
   ConnectionRecord method8554(NativePacketsBridge var1) {
      if (this.uC.ro.jX() == EntityLifecycleState.DISPOSING && this.uC.ro.jY() == this.uB + 1L) {
         return this.uA != null && var1.method1873(this.uA) ? this.uA : null;
      } else {
         return null;
      }
   }

   @Nullable
   public ConnectionRecord kU() {
      ConnectionRecord var1 = this.getConnection();
      if (var1 != null) {
         return var1;
      } else {
         return this.uC.ro.jY() == this.uB
               && this.uC.ro.jX() != EntityLifecycleState.DISPOSED
               && this.uA != null
               && NativePacketsHolder.PE().method1873(this.uA)
            ? this.uA
            : this.kT();
      }
   }

   public RotationPolicy getRotationPolicy() {
      return this.uD;
   }

   public boolean kW() {
      return this.uC.isViewerAllowed(this.uz) && (this.uE || this.uS) && (this.uC.rq == null || this.uC.rq.isVisibleTo(this));
   }

   public boolean kX() {
      return this.uF;
   }

   public boolean kY() {
      return this.uF && this.uQ >= 30.0;
   }

   public boolean kZ() {
      return this.uG;
   }

   public boolean la() {
      return this.uH || this.uI;
   }

   public synchronized void lb() {
      this.uI = true;
   }

   public void lc() {
      if (this.uE && !this.uW) {
         this.uF = true;
         this.uG = true;
      }
   }

   public boolean ld() {
      return this.uJ;
   }

   public void le() {
      this.uJ = true;
   }

   public void lf() {
      this.teleport(this.li.getLocation());
   }

   void teleport(Location var1) {
      EntityStateSnapshot var2 = this.uC.sQ;
      double var3 = -1.0;
      double var5 = -1.0;
      boolean var7 = false;
      World var8 = var1.getWorld();
      if (var2 != null && var8 != null && Objects.equals(var8.getUID(), var2.uuid())) {
         double var9 = var1.getX() - var2.x();
         double var11 = var1.getY() - var2.y();
         double var13 = var1.getZ() - var2.z();
         double var15 = Math.sqrt(var9 * var9 + var11 * var11 + var13 * var13);
         if (this.uC.getModel().Bk != null) {
            var3 = Math.max(var15 - this.uC.getModel().Bk.x, 0.0);
         } else {
            var3 = var15;
         }

         var5 = var9 * var9 + var13 * var13;
         var7 = var3 <= 2.0 || var3 < this.uC.tc && this.uC.isInViewRange(this.li);
      }

      if (var3 >= 30.0) {
         AttributeInstance var17 = CustomEntityImpl.qJ == null ? null : this.li.getAttribute(CustomEntityImpl.qJ);
         this.uU = var17 == null ? 64.0 : var17.getValue();
      }

      this.method8557(
         var3, var5, this.uC.getEntityManager().isRiderOf(this.li, this.uC), var7, CustomBlockListener.dn != null && CustomBlockListener.dn.method712(this.li)
      );
   }

   void method8556(double var1, double var3, boolean var5, boolean var6) {
      this.method8557(var1, var3, var5, var6, false);
   }

   void method8557(double var1, double var3, boolean var5, boolean var6, boolean var7) {
      this.uV = new ViewerVisibility(var1, var3, var5, var6, var7);
   }

   public void lg() {
      this.method8558(CustomEntityManager.op);
   }

   void method8558(long var1) {
      ViewerVisibility var3 = this.uV;
      if (var3 != null) {
         this.uQ = var3.fm();
         this.uR = var3.lm();
         this.uS = var3.ln();
         this.uT = var3.method24();
      }

      if (this.uH && this.uF && this.uG) {
         this.uH = false;
      }

      if (this.uJ && this.uF && this.uG) {
         this.uJ = false;
      }

      boolean var4 = this.uF && this.uG;
      boolean var5 = this.uE;
      if (this.uQ != -1.0 && this.uC.isViewerAllowed(this.uz)) {
         this.uE = this.uS || this.method8560(this.uQ);
         if (this.uE) {
            int var6 = this.li();
            int var7 = this.uC.getModel().AN && var6 > this.uM ? this.uM : var6;
            boolean var8 = this.uC.getModel().AN && var6 < this.uM || Math.floorMod(var1 + this.uN, var7) == 0;
            this.uF = var8;
            this.uG = var8;
         } else {
            this.uF = false;
            this.uG = false;
         }
      } else {
         this.uE = false;
         this.uF = false;
         this.uG = false;
      }

      int var9 = this.li();
      boolean var10 = this.uF && this.uG && this.uM != var9;
      if (var10) {
         this.uM = var9;
      }

      boolean var11 = this.uC.getModel().AN ? !var5 && this.uE : !var4 && this.uF && this.uG;
      if (var11) {
         if (this.uC.getModel().AN) {
            this.uH = true;
         } else {
            this.uC.rQ.values().forEach(var0 -> var0.Hn.Nz());
         }
      }

      if (var10 && this.uC.getModel().AN) {
         this.uJ = true;
      }
   }

   boolean lh() {
      return this.method8559(CustomEntityManager.op);
   }

   boolean method8559(long var1) {
      return Math.floorMod(var1 + this.uN, this.li()) != 0;
   }

   public int li() {
      byte var1;
      if (this.uC.ry != null) {
         if (this.uQ >= 70.0) {
            var1 = 16;
         } else if (this.uQ >= 60.0) {
            var1 = 12;
         } else if (this.uQ >= 50.0) {
            var1 = 10;
         } else if (this.uQ >= 30.0) {
            var1 = 4;
         } else {
            var1 = 1;
         }
      } else if (this.uQ >= 70.0) {
         var1 = 9;
      } else if (this.uQ >= 60.0) {
         var1 = 8;
      } else if (this.uQ >= 50.0) {
         var1 = 6;
      } else if (this.uQ >= 30.0) {
         var1 = 2;
      } else {
         var1 = 1;
      }

      return this.uT && !this.uS ? Math.max(10, var1 * 2) : var1;
   }

   public int lj() {
      int var1 = this.li();
      return var1 + 1;
   }

   boolean method8560(double var1) {
      ViewerVisibility var3 = this.uV;
      return var3 == null || var3.visible();
   }

   public boolean lk() {
      return this.uQ >= 0.0 && this.uQ <= 30.0;
   }

   public boolean method8561() {
      return this.uW;
   }
}
