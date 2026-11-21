package dev.lone.itemsadder.core;

import dev.lone.itemsadder.event.PlayerEventEffect;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public abstract class NamespacedElementWithEffects extends NamespacedElement {
   public final String abF;
   String abG;
   float abH;
   final float abI;
   final float abJ;
   final float abK;
   final List abL = new ArrayList();
   final List abM = new ArrayList();
   final List abN = new ArrayList();

   public NamespacedElementWithEffects(
      PlayerEffectState var1,
      String var2,
      String var3,
      String var4,
      int var5,
      boolean var6,
      boolean var7,
      boolean var8,
      boolean var9,
      float var10,
      float var11,
      float var12,
      List var13
   ) {
      super(var1, var2, var3, var5, var6, var7, var8, var9, var13);
      this.abF = var4;
      this.abI = var10;
      float var14 = var11;
      float var15 = var12;
      if (var11 > var12) {
         var14 = var12;
         var15 = var11;
      }

      this.abJ = var14;
      this.abK = var15;
      if (var1 != null) {
         Float var16 = this.method500(null);
         if (var16 == null) {
            this.method499(var10);
         }
      }
   }

   public void method494(ProgressEffectAction var1) {
      this.abL.add(var1);
   }

   public List zM() {
      return this.abL;
   }

   public void method495(Player var1, int var2) {
      for (ProgressEffectAction var4 : this.abL) {
         var4.method9642(this, var1, var2);
      }
   }

   public void method496(ConditionAction var1) {
      this.abM.add(var1);
   }

   public List zN() {
      return this.abM;
   }

   public void method497(Player var1, int var2) {
      for (ConditionAction var4 : this.abM) {
         var4.method9640(this, var1, var2);
      }
   }

   public void method498(PlayerEventEffect var1) {
      this.abN.add(var1);
   }

   public List zO() {
      return this.abN;
   }

   public float method499(float var1) {
      this.abH = this.zP();
      if (var1 == this.abH) {
         return var1;
      }

      if (var1 > this.zS()) {
         var1 = this.zS();
      }

      if (var1 < this.zR()) {
         var1 = this.zR();
      }

      assert this.abC != null : "Holder must not be null when getting value";
      ContentManager.method8084().aG.method704(this.abC.acI, this.abF, var1);
      if ((int)var1 != (int)this.abH) {
         this.zF();
      }

      return var1;
   }

   public float zP() {
      Float var1 = this.method500(null);
      if (var1 == null) {
         var1 = this.zR();
      }

      return var1;
   }

   @Nullable
   public Float method500(@Nullable Float var1) {
      assert this.abC != null : "Holder must not be null when getting value";
      return ContentManager.method8084().aG.method705(this.abC.acI, this.abF, var1);
   }

   protected void method501(NamespacedElementWithEffects var1) {
      for (ProgressEffectAction var3 : var1.zM()) {
         this.abL.add(var3.Ag());
      }

      for (ConditionAction var6 : var1.zN()) {
         this.abM.add(var6.Ab());
      }

      for (PlayerEventEffect var7 : var1.zO()) {
         this.method498(var7);
      }
   }

   @Generated
   public String zQ() {
      return this.abF;
   }

   @Generated
   public float zR() {
      return this.abJ;
   }

   @Generated
   public float zS() {
      return this.abK;
   }
}
