package dev.lone.itemsadder.entity;

import beer.devs.fastnbt.nms.Version;
import beer.devs.nativepackets.api.EntityMetadataField;
import beer.devs.nativepackets.api.NativeVector3;
import beer.devs.nativepackets.api.EntityMetadataFields.Display;
import dev.lone.itemsadder.network.ConnectionRecord;
import dev.lone.itemsadder.network.NativePacketsBridge;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.nms.NmsRegistry;
import dev.lone.itemsadder.util.DoubleVector3;
import dev.lone.itemsadder.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractItemDisplayEntity {
   public static final double bpu = 0.35;
   static Constructor aON;
   private static boolean aar = false;
   protected DoubleVector3 bpv;
   protected int bpw;
   @Nullable
   private Map bpx;
   @Nullable
   private Map bpy;
   @Nullable
   private DoubleVector3 bpz;

   @Nullable
   public static AbstractItemDisplayEntity create(Location var0) {
      if (aar) {
         return null;
      }

      if (aON == null) {
         aON = NmsRegistry.method2779(AbstractItemDisplayEntity.class, true, Location.class);
         if (aON == null) {
            aar = true;
            Log.error("Failed to get shadow constructor.");
            return null;
         }
      }

      try {
         return (AbstractItemDisplayEntity)aON.newInstance(var0);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException var2) {
         aar = true;
         Log.error("Error getting shadow constructor", var2);
         return null;
      }
   }

   public final void Qf() {
      if (this.bpx == null) {
         this.bpx = new WeakHashMap();
         this.bpy = new WeakHashMap();
      }
   }

   public abstract void appendSpawn(List var1);

   public abstract void appendUpdate(List var1);

   public abstract void appendMove(double var1, double var3, double var5, List var7);

   public final void appendStateUpdate(List var1, NativePacketsBridge var2) {
      this.appendEntityState(var2, this.bpw, var1);
   }

   protected final void appendEntityMove(int var1, double var2, double var4, double var6, List var8) {
      if (this.bpx != null) {
         if (this.bpz == null) {
            this.bpz = new DoubleVector3(var2, var4, var6);
         } else {
            this.bpz.method3161(var2, var4, var6);
         }

         this.appendRemove(var8);
      } else {
         NativePacketsBridge var9 = NativePacketsHolder.PE();
         Object var10 = var9.Pv().teleport(var1, var2, var4, var6, 0.0F, 0.0F, false);

         for (EntityViewer var12 : var8) {
            if (var12.kW() && var12.kZ()) {
               ConnectionRecord var13 = var12.getConnection();
               if (var13 != null) {
                  var9.method1871(var13, var10);
               }
            }
         }
      }
   }

   public final void appendRemove(List var1) {
      if (this.bpx != null) {
         NativePacketsBridge var2 = NativePacketsHolder.PE();
         boolean var3 = Version.isAtLeast(Version.v1_21_3);
         Object var4 = null;
         Object var5 = null;

         for (EntityViewer var7 : var1) {
            if (var7.kW()) {
               ConnectionRecord var8 = var7.getConnection();
               if (var8 != null) {
                  int var9 = var7.lj();
                  if (this.bpy.getOrDefault(var7, 1) != var9) {
                     Object var10 = var2.Pv().metadata(this.bpw, new EntityMetadataField[]{Display.POS_ROT_INTERPOLATION_DURATION}, new Object[]{var9});
                     if (var2.method1871(var8, var10)) {
                        this.bpy.put(var7, var9);
                     }
                  }

                  if (this.bpz != null) {
                     DoubleVector3 var24 = (DoubleVector3)this.bpx.get(var7);
                     double var11 = var24 == null ? 0.0 : this.bpz.pW - var24.pW;
                     double var13 = var24 == null ? 0.0 : this.bpz.pX - var24.pX;
                     double var15 = var24 == null ? 0.0 : this.bpz.pY - var24.pY;
                     if ((var24 == null || var11 != 0.0 || var13 != 0.0 || var15 != 0.0)
                        && (var24 == null || var7.kZ() || !(var11 * var11 + var13 * var13 + var15 * var15 <= 4.0))) {
                        Object var17 = null;
                        if (var3 && var24 != null && var11 * var11 + var13 * var13 + var15 * var15 <= 4.0) {
                           long var18 = Math.round(this.bpz.pW * 4096.0) - Math.round(var24.pW * 4096.0);
                           long var20 = Math.round(this.bpz.pX * 4096.0) - Math.round(var24.pX * 4096.0);
                           long var22 = Math.round(this.bpz.pY * 4096.0) - Math.round(var24.pY * 4096.0);
                           if (var18 >= -32768L && var18 <= 32767L && var20 >= -32768L && var20 <= 32767L && var22 >= -32768L && var22 <= 32767L) {
                              var17 = var2.Pv().relativeMove(this.bpw, var18 / 4096.0, var20 / 4096.0, var22 / 4096.0, false);
                           }
                        }

                        if (var17 == null) {
                           if (var3) {
                              if (var4 == null) {
                                 var4 = var2.Pv()
                                    .entityPositionSync(
                                       this.bpw, new NativeVector3(this.bpz.pW, this.bpz.pX, this.bpz.pY), new NativeVector3(0.0, 0.0, 0.0), 0.0F, 0.0F, false
                                    );
                              }

                              var17 = var4;
                           } else {
                              if (var5 == null) {
                                 var5 = var2.Pv().teleport(this.bpw, this.bpz.pW, this.bpz.pX, this.bpz.pY, 0.0F, 0.0F, false);
                              }

                              var17 = var5;
                           }
                        }

                        if (var2.method1871(var8, var17)) {
                           this.bpx.put(var7, this.bpz.acy());
                        }
                     }
                  }
               }
            }
         }
      }
   }

   protected final void appendEntityRemove(int var1, List var2) {
      this.appendEntityState(NativePacketsHolder.PE(), var1, var2);
   }

   private void appendEntityState(NativePacketsBridge var1, int var2, List var3) {
      Object var4 = var1.Pv().removeEntities(new int[]{var2});

      for (EntityViewer var6 : var3) {
         if (this.bpx != null) {
            this.bpx.remove(var6);
            this.bpy.remove(var6);
         }

         ConnectionRecord var7 = var6.kU();
         if (var7 != null) {
            var1.method1871(var7, var4);
         }
      }
   }
}
