package dev.lone.itemsadder.core;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public abstract class NamespacedElement extends NamespacedId {
   public final List abo;
   int abp;
   int abq;
   @Nullable
   Integer abr;
   boolean visible;
   public boolean abs;
   boolean abt;
   final boolean abu;
   final boolean abv;
   final boolean abw;
   final boolean abx;
   private final boolean aby;
   private List abz;
   private List abA;
   String abB;
   @Nullable
   PlayerEffectState abC;
   int abD;
   boolean abE = true;

   public NamespacedElement(PlayerEffectState var1, String var2, String var3, int var4, boolean var5, boolean var6, boolean var7, boolean var8, List var9) {
      super(var2, var3);
      this.abC = var1;
      this.abp = var4;
      this.abu = var5;
      this.abv = var6;
      this.abw = var7;
      this.abx = var8;
      this.abq = var4;
      this.abo = var9;
      this.aby = var9.contains("*") || var9.isEmpty();
      if (!this.aby) {
         this.abz = new ArrayList();
         this.abA = new ArrayList();

         for (World var11 : Bukkit.getWorlds()) {
            for (String var13 : var9) {
               if (var13.endsWith("*")) {
                  var13 = var13.substring(0, var13.length() - 1);
                  if (var13.startsWith("!")) {
                     var13 = var13.substring(1);
                     if (var11.getName().startsWith(var13)) {
                        this.abA.add(var11);
                     }
                  } else if (var11.getName().startsWith(var13)) {
                     this.abz.add(var11);
                  }
               } else if (var13.startsWith("!")) {
                  var13 = var13.substring(1);
                  if (var11.getName().equals(var13)) {
                     this.abA.add(var11);
                  }
               } else if (var11.getName().equals(var13)) {
                  this.abz.add(var11);
               }
            }
         }
      }

      this.abs = var5;
      this.setVisible(this.abs);
   }

   @Deprecated
   public abstract NamespacedElement method491(PlayerEffectState var1);

   public boolean method492(World var1) {
      if (this.aby) {
         return true;
      } else if (this.abA.contains(var1)) {
         return false;
      } else {
         return this.abz.contains(var1) ? true : this.abz.isEmpty() && !this.abA.contains(var1);
      }
   }

   public void setVisible(boolean visible) {
      if (this.visible != visible) {
         this.visible = visible;
         if (visible) {
            this.abs = true;
         }

         this.zF();
      }
   }

   public void bn(int var1) {
      this.abp = var1;
   }

   public int zA() {
      return this.abq;
   }

   public int zB() {
      return this.abp;
   }

   public abstract int zC();

   public abstract String zD();

   public boolean zE() {
      if (!this.abt) {
         return false;
      }

      if (!this.visible) {
         return false;
      }

      if (this.abC == null) {
         return false;
      }

      Player var1 = this.abC.acI;
      return var1.getGameMode() != GameMode.SPECTATOR
         && (this.abv || var1.getGameMode() != GameMode.CREATIVE)
         && (this.abw || var1.getRemainingAir() >= var1.getMaximumAir())
         && (this.abx || !var1.isInsideVehicle());
   }

   public void bo(int var1) {
      if (this.abr != null) {
         if (this.abr == var1) {
            return;
         }

         this.abD = this.abr;
      }

      this.abr = var1;
      this.zF();
   }

   public void zF() {
      if (this.abC != null) {
         this.abC.recalculateOffsets();
      }

      this.abE = true;
      this.abB = null;
   }

   @Generated
   public boolean isVisible() {
      return this.visible;
   }

   @Generated
   public boolean zG() {
      return this.abt;
   }

   @Generated
   public void aE(boolean var1) {
      this.abt = var1;
   }

   @Generated
   public boolean zH() {
      return this.abu;
   }

   @Generated
   public boolean zI() {
      return this.abv;
   }

   @Generated
   public boolean zJ() {
      return this.abw;
   }

   @Generated
   public boolean zK() {
      return this.abx;
   }

   @Generated
   public boolean zL() {
      return this.abE;
   }
}
