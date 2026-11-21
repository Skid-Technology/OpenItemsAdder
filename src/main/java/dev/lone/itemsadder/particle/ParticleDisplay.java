package dev.lone.itemsadder.particle;

import dev.lone.itemsadder.anim.DisplayTransform;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.DisplayEntityKey;
import dev.lone.itemsadder.entity.DisplayUpdateRecord;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.network.ConnectionRecord;
import dev.lone.itemsadder.network.NativePacketsHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;

final class ParticleDisplay {
   final ConnectionRecord ayS;
   final ViewerBudgetLease ayT;
   final AtomicBoolean ayU;
   final AtomicBoolean ayV;
   final Map ayW;
   final Map ayX;
   final Map ayY;
   volatile CurveFrame ayZ;
   long aza;
   boolean azb;
   int azc;
   int aaR;

   ParticleDisplay(ParticleEmitter var1, ConnectionRecord var2) {
      this.azd = var1;
      this.ayU = new AtomicBoolean();
      this.ayV = new AtomicBoolean();
      this.ayW = new HashMap();
      this.ayX = new HashMap();
      this.ayY = new HashMap();
      this.ayS = var2;
      this.ayT = var1.ayR.method2156(var2);
   }

   synchronized boolean method2165(CurveFrame var1) {
      if (this.azd.ayI.get()) {
         return false;
      }

      this.ayZ = var1;
      return this.ayU.compareAndSet(false, true);
   }

   synchronized void bB(Player var1) {
      long var2 = System.nanoTime();

      try {
         this.bC(var1);
      } finally {
         this.azd.ayR.ayd.add(System.nanoTime() - var2);
      }
   }

   void bC(Player var1) {
      CurveFrame var2 = this.ayZ;
      if (this.azd.ayI.get() || !NativePacketsHolder.PE().method1873(this.ayS)) {
         this.bD(var1);
      } else if (var2 != null && var2.aze > this.aza) {
         this.aza = var2.aze;
         Location var3 = var1.getEyeLocation();
         boolean var4 = this.azd.method1797(this.ayS.bk())
            && !var1.isDead()
            && var1.getWorld() == this.azd.ayw.getWorld()
            && ContentManager.method8084().aL != null
            && ContentManager.method8084().aL.isInstalled(var1);
         double var5 = this.azb ? 132.0 : 128.0;
         ArrayList var7 = new ArrayList();
         HashSet var8 = new HashSet();
         long var9 = var1.getTicksLived() / 2;
         if (var4) {
            for (int var11 = 0; var11 < var2.azf.size(); var11++) {
               ParticleState var12 = (ParticleState)var2.azf.get((this.aaR + var11) % var2.azf.size());
               double var13 = this.azd.ayw.getX() + var12.azk;
               double var15 = this.azd.ayw.getY() + var12.azl;
               double var17 = this.azd.ayw.getZ() + var12.azm;
               if (!(
                  ParticleDisplayManager.method2160(var3.getX() - var13)
                        + ParticleDisplayManager.method2160(var3.getY() - var15)
                        + ParticleDisplayManager.method2160(var3.getZ() - var17)
                     > ParticleDisplayManager.method2160(var5 + var12.azw)
               )) {
                  ParticleInstance var19 = (ParticleInstance)this.ayW.get(var12.azi);
                  if (var19 != null) {
                     var8.add(var12.azi);
                  }

                  if (!this.ayT.method1796(var9, 1, 0)) {
                     this.azd.ayR.ayb.increment();
                  } else {
                     DisplayTransform var20 = (DisplayTransform)this.azd.ayG.get(var12.azj);
                     String var21 = ((ParticleEffectDefinition)this.azd.ayE.definitions().get(var12.azj)).avo;
                     Quaternionf var22 = ParticleRotation.method2133(
                        var21, var12.azn, var12.azo, var12.azp, var3.getX() - var13, var3.getY() - var15, var3.getZ() - var17, var12.azx
                     );
                     boolean var23 = var19 == null || var19.azz.azk != var12.azk || var19.azz.azl != var12.azl || var19.azz.azm != var12.azm;
                     boolean var24 = var19 == null || !var19.azz.method7(var12) || !var19.azA.equals(var22);
                     if (var23 || var24) {
                        if (!this.ayT.method1796(var9, 0, (var23 ? 1 : 0) + (var24 ? 1 : 0))) {
                           this.azd.ayR.ayb.increment();
                        } else if (var19 == null && !this.azd.ayR.method2158(this.ayS)) {
                           this.azd.ayR.ayb.increment();
                        } else {
                           if (var19 == null) {
                              this.azc++;
                           }

                           var8.add(var12.azi);
                           Object var25 = null;
                           if (var24) {
                              ItemDisplay var26 = this.ayX.computeIfAbsent(var12.azj, var2x -> {
                                 ItemDisplay var3x = NativePacketsHolder.PD().ac(var3);
                                 var20.method2080(var3x);
                                 return var3x;
                              });
                              List var27 = (List)((List)this.azd.ayE.frames().get(var12.azj)).get(var12.azu);
                              String var28 = (String)var27.get(Math.min(Math.max(0, var12.azt), var27.size() - 1));
                              ItemStack var29 = this.ayY.computeIfAbsent(var28, var0 -> {
                                 ItemStack var1x = new ItemStack(Material.PAPER);
                                 ItemOperationsService.getRegistryBridge().method3437(var1x, var0);
                                 return var1x;
                              });
                              if (var12.azv != 16777215) {
                                 var29 = var29.clone();
                                 ItemOperationsService.getRegistryBridge().method3438(var29, Color.fromRGB(var12.azv));
                              }

                              var26.setItemStack(var29);
                              var26.setTransformation(var20.method2081(var12.azq, var12.azr, var22, var12.azs));
                              var26.setInterpolationDelay(var20.awT);
                              var25 = NativePacketsHolder.PD().getNmsHandle(var26);
                              this.azd.ayR.aye.increment();
                           }

                           DisplayEntityKey var31 = var19 == null ? NativePacketsHolder.PD().allocateKey(this.azd.ayw.getWorld()) : var19.azy;
                           var7.add(new DisplayUpdateRecord(var31, var13, var15, var17, var25, var19 == null, var23));
                           this.ayW.put(var12.azi, new ParticleInstance(var31, var12, var22));
                        }
                     }
                  }
               }
            }
         }

         this.aaR = var2.azf.isEmpty() ? 0 : (this.aaR + Math.max(1, var7.size())) % var2.azf.size();
         int[] var30 = this.ayW
            .entrySet()
            .stream()
            .filter(var1x -> !var8.contains(var1x.getKey()))
            .mapToInt(var0 -> ((ParticleInstance)var0.getValue()).azy.entityId())
            .toArray();
         this.ayW.keySet().retainAll(var8);
         this.azb = !var8.isEmpty();
         this.azd.ayR.method2159(this.ayS, var30.length);
         this.azc -= var30.length;
         if (NativePacketsHolder.PE().method1873(this.ayS) && (!var7.isEmpty() || var30.length > 0)) {
            NativePacketsHolder.PD().sendPassengerUpdate(var1, var7, var30, var30.length);
            this.azd.ayR.axZ.add(var7.size());
            this.azd.ayR.aya.add(var30.length);
            this.azd.ayR.ayf.add(var7.stream().filter(DisplayUpdateRecord::Os).count());
            this.azd
               .ayR
               .ayg
               .add(var7.stream().mapToInt(var0 -> (!var0.Os() && !var0.Pt() ? 0 : 1) + (var0.value() != null ? 1 : 0)).sum() + (var30.length > 0 ? 1 : 0));
         }
      }
   }

   synchronized void bD(Player var1) {
      if (var1 != null && !this.ayW.isEmpty() && NativePacketsHolder.PE().method1873(this.ayS)) {
         int[] var2 = this.ayW.values().stream().mapToInt(var0 -> var0.azy.entityId()).toArray();
         NativePacketsHolder.PD().sendPassengerUpdate(var1, List.of(), var2, var2.length);
         this.azd.ayR.aya.add(var2.length);
         this.azd.ayR.ayg.increment();
      }

      this.azd.ayR.method2159(this.ayS, this.azc);
      this.azc = 0;
      this.azd.ayR.method2157(this.ayS, this.ayT);
      this.ayW.clear();
      this.ayX.clear();
      this.ayY.clear();
      this.ayZ = null;
   }
}
