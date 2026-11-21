package dev.lone.itemsadder.particle;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.anim.DistanceEventTrigger;
import dev.lone.itemsadder.anim.SpeedEventTrigger;
import dev.lone.itemsadder.molang.MolangExpression;
import dev.lone.itemsadder.util.XorShiftRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public final class ParticleSimulation {
   public static final double azB = 0.03333333333333333;
   final ParticleEffectDefinition azC;
   final Map azD;
   final XorShiftRandom azE;
   final EmitterState azF;
   final long azG;
   final int azH;
   final ArrayList azI = new ArrayList();
   final ArrayList azJ = new ArrayList();
   final ArrayList azK = new ArrayList();
   final Map azL = new HashMap();
   final double[] azM = new double[4];
   final EmitterMolangContext azN = new EmitterMolangContext(this);
   double azO;
   double azP;
   double azQ;
   double azR;
   boolean wq;
   boolean azS;
   long azT;
   int azU;
   double Gf;
   double Gg;
   double Gh;
   double azV;
   double azW;
   double azX;
   boolean azY;
   ParentPose azx;
   MolangExpression azZ;
   final EmitterMolangContext aAa = new EmitterMolangContext(this);
   private boolean aAb;
   private double aAc;
   private double aAd;
   private double aAe;
   private double aAf;

   public ParticleSimulation(ParticleEffectDefinition var1, Map var2, int var3, int var4) {
      this(var1, var2, var3, var4, new ParticleCapacity(var4));
   }

   ParticleSimulation(ParticleEffectDefinition var1, Map var2, int var3, int var4, ParticleCapacity var5) {
      this(var1, var2, new XorShiftRandom(var3), var4, new EmitterState(var5));
   }

   private ParticleSimulation(ParticleEffectDefinition var1, Map var2, XorShiftRandom var3, int var4, EmitterState var5) {
      if (var4 >= 1 && var4 <= 30000) {
         this.azC = var1;
         this.azD = var2;
         this.azE = var3;
         this.azH = var4;
         this.azF = var5;
         this.azG = ++var5.azh;
         var3.fillDoubles(this.azM);
      } else {
         throw new IllegalArgumentException("Particle limit must be 1..30000");
      }
   }

   public void start() {
      this.azO = this.azP = 0.0;
      this.wq = true;
      this.azL.clear();
      this.azN.method2091(null);
      this.azQ = this.azC.avs.method2089(this.azN);
      this.azR = this.azC.avt.method2089(this.azN);
      this.azE.fillDoubles(this.azM);
      this.azC.avG.method2089(this.azN);
      if (this.azZ != null) {
         this.azZ.method2089(this.azN);
      }

      this.method2184(this.azC.dG("emitter_lifetime_events").get("creation_event"), null);
      if (!this.aAb) {
         this.aAb = true;
         this.aAc = this.Gf + (this.azx == null ? 0.0 : this.azx.ka());
         this.aAd = this.Gg + (this.azx == null ? 0.0 : this.azx.kb());
         this.aAe = this.Gh + (this.azx == null ? 0.0 : this.azx.kc());
         this.method2176(-1.0, 0.0);
      }

      if (this.azC.avi.equals("instant")) {
         this.method2177(this.azC.avq.method2089(this.azN));
      } else if (this.azC.avi.equals("manual")) {
         this.method2177(1.0);
      }
   }

   Vector3d method2171(ParticleData var1, Vector3d var2) {
      var2.set(var1 == null ? 0.0 : var1.pW, var1 == null ? 0.0 : var1.pX, var1 == null ? 0.0 : var1.pY);
      if (this.azx != null && this.azC.awb) {
         this.azx.method165(var2);
      }

      return var2.add(this.Gf, this.Gg, this.Gh);
   }

   public void tick() {
      this.azN.method2091(null);
      this.azC.avH.method2089(this.azN);
      if (!this.azC.awl.isEmpty()) {
         double var1 = this.Gf + (this.azx == null ? 0.0 : this.azx.ka());
         double var3 = this.Gg + (this.azx == null ? 0.0 : this.azx.kb());
         double var5 = this.Gh + (this.azx == null ? 0.0 : this.azx.kc());
         double var7 = this.aAf + Math.hypot(Math.hypot(var1 - this.aAc, var3 - this.aAd), var5 - this.aAe);
         if (!Double.isFinite(var7)) {
            throw new IllegalStateException("Non-finite emitter travel distance");
         }

         if (this.wq) {
            this.method2176(this.aAf, var7);
         }

         this.aAf = var7;
         this.aAc = var1;
         this.aAd = var3;
         this.aAe = var5;
      }

      for (ParticleData var2 : this.azI) {
         this.azN.method2091(var2);
         this.azC.avI.method2089(this.azN);
      }

      int var10 = this.azI.size();

      for (int var11 = 0; var11 < var10 && var11 < this.azI.size(); var11++) {
         this.method2179((ParticleData)this.azI.get(var11));
      }

      double var12 = this.azO;
      this.azO += 0.03333333333333333;
      this.azP += 0.03333333333333333;
      this.azN.method2091(null);
      this.azN.aAo = var12;
      if (this.wq && this.azC.avi.equals("steady")) {
         double var4 = this.azC.avp.method2089(this.azN) / 30.0;
         double var6 = Math.floor(1.0 / var4 + 0.5);
         var4 = Math.floor(this.azO * 30.0 + 0.5) % var6 == 0.0 ? Math.ceil(var4) : Math.floor(var4);
         this.method2177(var4);
      }

      this.method2183(this.azC.dG("emitter_lifetime_events").getJSONObject("timeline"), var12, this.azO, null);
      int var14 = 0;

      for (int var15 = this.azK.size(); var14 < var15; var14++) {
         ((ParticleSimulation)this.azK.get(var14)).tick();
      }

      this.azK.removeIf(var1x -> {
         if (var1x.azI.isEmpty() && var1x.azO > var1x.azQ) {
            var1x.stop();
            this.azF.aAm--;
            return true;
         } else {
            return false;
         }
      });
      this.azN.method2091(null);
      this.azN.aAo = var12;
      if (this.azC.avj.equals("expression")) {
         if (this.wq && this.azC.avv.method2089(this.azN) != 0.0) {
            this.JF();
         }

         if (!this.wq && this.azC.avu.method2089(this.azN) != 0.0) {
            this.start();
         }
      } else if (this.azC.avj.equals("looping") && !this.azS) {
         if (this.wq && Math.floor(this.azO * 100000.0 + 0.5) / 100000.0 >= this.azQ) {
            this.JF();
         }

         if (!this.wq && Math.floor(this.azO * 100000.0 + 0.5) / 100000.0 >= this.azR) {
            this.start();
         }
      } else if (this.wq && Math.floor(this.azO * 100000.0 + 0.5) / 100000.0 >= this.azQ) {
         this.JF();
      }
   }

   public List Jy() {
      return Collections.unmodifiableList(this.azI);
   }

   void method2172(double var1, double var3, double var5) {
      if (Double.isFinite(var1) && Double.isFinite(var3) && Double.isFinite(var5)) {
         this.azF.Rv = var1;
         this.azF.uj = var3;
         this.azF.Rx = var5;
      } else {
         throw new IllegalArgumentException("Invalid parent velocity");
      }
   }

   void method2173(ParticleBounds var1) {
      this.azF.aAi = var1;
   }

   void method2174(BlockAtFunction var1) {
      this.azF.aAj = var1;
   }

   void method2175(ObstacleProvider var1) {
      this.azF.aAk = var1;
   }

   public List Jz() {
      return Collections.unmodifiableList(this.azK);
   }

   public double JA() {
      return this.azO;
   }

   public long JB() {
      return this.azE.state;
   }

   public int JC() {
      return this.azF.aAl;
   }

   public long Jc() {
      return this.azF.ayv;
   }

   public boolean JD() {
      return !this.wq && this.azI.isEmpty() && this.azK.stream().allMatch(ParticleSimulation::JD);
   }

   public void stop() {
      this.wq = false;
      this.azF.aAg.bP(this.azI.size());
      this.azF.aAl = this.azF.aAl - this.azI.size();
      this.azI.clear();
      this.azJ.clear();
      this.azK.forEach(var1 -> {
         var1.stop();
         this.azF.aAm--;
      });
      this.azK.clear();
      if (!this.azS) {
         this.azF.aAh.clear();
      }
   }

   public List JE() {
      if (this.azF.aAh.isEmpty()) {
         return List.of();
      }

      List var1 = List.copyOf(this.azF.aAh);
      this.azF.aAh.clear();
      return var1;
   }

   private void JF() {
      this.wq = false;
      this.azO = 0.0;
      this.method2184(this.azC.dG("emitter_lifetime_events").get("expiration_event"), null);
   }

   private void method2176(double var1, double var3) {
      int var5 = 256;

      for (DistanceEventTrigger var7 : this.azC.awl) {
         double var8 = var7.Ig()
            ? (var1 < 0.0 ? 0.0 : Math.floor((var3 - var1) / var7.fm() + var1 % var7.fm() / var7.fm()))
            : (var1 < var7.fm() && var3 >= var7.fm() ? 1 : 0);
         int var10 = (int)Math.min(var5, var8);

         for (int var11 = 0; var11 < var10; var11++) {
            this.method2184(var7.If(), null);
         }

         var5 -= var10;
         if (var8 > var10) {
            this.azF.ayv = (long)(this.azF.ayv + Math.min(2.147483647E9, var8 - var10));
         }
      }
   }

   private void method2177(double var1) {
      if (Double.isFinite(var1) && !(var1 <= 0.0)) {
         this.azN.method2091(null);
         int var3 = this.azC.avi.equals("steady") ? (int)Math.max(0.0, Math.min(this.azH, this.azC.avr.method2089(this.azN))) : this.azH;
         int var4 = (int)Math.min(Math.ceil(var1), Math.max(0, Math.min(var3 - this.azI.size(), this.azH - this.azF.aAl)));
         this.azF.ayv = this.azF.ayv + (long)Math.max(0.0, Math.ceil(var1) - var4);

         for (int var5 = 0; var5 < var4; var5++) {
            if (this.azF.aAl >= this.azH) {
               this.azF.ayv += var4 - var5;
               break;
            }

            ParticleData var6;
            if (this.azJ.isEmpty()) {
               var6 = new ParticleData();
               if (!this.method2178(var6)) {
                  this.azF.ayv += var4 - var5;
                  break;
               }
            } else {
               var6 = (ParticleData)this.azJ.remove(this.azJ.size() - 1);
            }

            if (!this.method2178(var6)) {
               this.azF.ayv += var4 - var5;
               break;
            }
         }
      }
   }

   private boolean method2178(ParticleData var1) {
      if (!this.azI.contains(var1)) {
         if (!this.azF.aAg.HV()) {
            return false;
         }

         this.azI.add(var1);
         this.azF.aAl++;
      }

      var1.azh = ++this.azT;
      var1.azO = var1.aAB = 0.0;
      var1.DL = 0;
      this.azE.fillDoubles(var1.aAC);
      var1.Mw = this.azC.method2071(var1.aAC[0]);
      var1.Jc = var1.ave = var1.Jd = 0.0;
      this.azN.method2091(var1);
      var1.aAv = this.azC.avw.method2089(this.azN);
      var1.aAz = this.azC.avB.method2089(this.azN);
      var1.aAA = this.azC.avC.method2089(this.azN);
      var1.azs = 0.0;
      var1.pW = var1.pX = var1.pY = 0.0;
      switch (this.azC.avk) {
         case "box":
         case "entity_aabb":
            ParticleBounds var21 = this.azF.aAi;
            double var5 = this.azC.avk.equals("box") ? this.azC.avN[0].method2089(this.azN) : (var21 == null ? 0.0 : (var21.HO() - var21.HL()) / 2.0);
            double var7 = this.azC.avk.equals("box") ? this.azC.avN[1].method2089(this.azN) : (var21 == null ? 0.0 : (var21.HP() - var21.HM()) / 2.0);
            double var9 = this.azC.avk.equals("box") ? this.azC.avN[2].method2089(this.azN) : (var21 == null ? 0.0 : (var21.HQ() - var21.HN()) / 2.0);
            var1.pW = (this.azE.nextDouble() * 2.0 - 1.0) * var5;
            var1.pX = (this.azE.nextDouble() * 2.0 - 1.0) * var7;
            var1.pY = (this.azE.nextDouble() * 2.0 - 1.0) * var9;
            if (this.azC.avY) {
               int var11 = (int)(this.azE.nextDouble() * 3.0);
               double var23 = this.azE.nextDouble() < 0.5 ? -1.0 : 1.0;
               if (var11 == 0) {
                  var1.pW = var5 * var23;
               } else if (var11 == 1) {
                  var1.pX = var7 * var23;
               } else {
                  var1.pY = var9 * var23;
               }
            }
            break;
         case "sphere":
            var1.pW = this.azC.avz.method2089(this.azN) * (this.azC.avY ? 1.0 : this.azE.nextDouble());
            this.method2185(var1, false);
            break;
         case "disc":
            double var4 = this.azC.avz.method2089(this.azN);
            double var6 = this.azE.nextDouble() * Math.PI * 2.0;
            double var8 = this.azC.avY ? var4 : var4 * Math.sqrt(this.azE.nextDouble());
            var1.pW = var8 * Math.cos(var6);
            var1.pY = var8 * Math.sin(var6);
            double var10 = this.azC.avO[0].method2089(this.azN);
            double var12 = this.azC.avO[1].method2089(this.azN);
            double var14 = this.azC.avO[2].method2089(this.azN);
            if (var10 != 0.0 || var12 != 0.0 || var14 != 0.0) {
               method2186(var1, var10, var12, var14);
            }
      }

      if (!this.azC.avl.equals("outwards") && !this.azC.avl.equals("inwards")) {
         var1.aAw = this.azC.avP[0].method2089(this.azN);
         var1.aAx = this.azC.avP[1].method2089(this.azN);
         var1.aAy = this.azC.avP[2].method2089(this.azN);
         double var17 = Math.sqrt(var1.aAw * var1.aAw + var1.aAx * var1.aAx + var1.aAy * var1.aAy);
         if (var17 != 0.0) {
            var1.aAw /= var17;
            var1.aAx /= var17;
            var1.aAy /= var17;
         }
      } else if (this.azY && this.azC.avl.equals("outwards")) {
         var1.aAw = this.azV;
         var1.aAx = this.azW;
         var1.aAy = this.azX;
      } else if (!this.azC.avk.equals("point") && !this.azC.avk.equals("custom")) {
         double var16 = Math.sqrt(var1.pW * var1.pW + var1.pX * var1.pX + var1.pY * var1.pY);
         double var22 = this.azC.avl.equals("inwards") ? -1.0 : 1.0;
         var1.aAw = var16 == 0.0 ? 0.0 : var1.pW / var16 * var22;
         var1.aAx = var16 == 0.0 ? 0.0 : var1.pX / var16 * var22;
         var1.aAy = var16 == 0.0 ? 0.0 : var1.pY / var16 * var22;
      } else {
         var1.aAw = 1.0;
         var1.aAx = var1.aAy = 0.0;
         this.method2185(var1, true);
      }

      if (!this.azY || !this.azC.avl.equals("outwards")) {
         double var18 = this.azC.avy.method2089(this.azN);
         var1.aAw *= var18;
         var1.aAx *= var18;
         var1.aAy *= var18;
      }

      var1.pW = var1.pW + this.azC.avM[0].method2089(this.azN);
      var1.pX = var1.pX + this.azC.avM[1].method2089(this.azN);
      var1.pY = var1.pY + this.azC.avM[2].method2089(this.azN);
      if (this.azC.avk.equals("entity_aabb") && this.azF.aAi != null) {
         ParticleBounds var19 = this.azF.aAi;
         var1.pW = var1.pW + ((var19.HL() + var19.HO()) / 2.0 - this.Gf);
         var1.pX = var1.pX + ((var19.HM() + var19.HP()) / 2.0 - this.Gg);
         var1.pY = var1.pY + ((var19.HN() + var19.HQ()) / 2.0 - this.Gh);
         if (this.azx != null && this.azC.awb) {
            var1.pW = var1.pW - this.azx.ka();
            var1.pX = var1.pX - this.azx.kb();
            var1.pY = var1.pY - this.azx.kc();
            method2187(var1, false, -this.azx.HG(), -this.azx.method131(), -this.azx.HI(), this.azx.HJ());
            method2187(var1, true, -this.azx.HG(), -this.azx.method131(), -this.azx.HI(), this.azx.HJ());
         }
      } else if (this.azx != null) {
         if (!this.azC.awc) {
            method2187(var1, false, this.azx.HG(), this.azx.method131(), this.azx.HI(), this.azx.HJ());
         }

         if (!this.azC.awb) {
            var1.pW = var1.pW + this.azx.ka();
            var1.pX = var1.pX + this.azx.kb();
            var1.pY = var1.pY + this.azx.kc();
         }
      }

      if (this.azC.awd) {
         Vector3d var20 = new Vector3d(this.azF.Rv, this.azF.uj, this.azF.Rx);
         if (this.azx != null && this.azC.awb) {
            new Quaterniond(-this.azx.HG(), -this.azx.method131(), -this.azx.HI(), this.azx.HJ()).transform(var20);
         }

         var1.aAw = var1.aAw + var20.x;
         var1.aAx = var1.aAx + var20.y;
         var1.aAy = var1.aAy + var20.z;
      }

      this.method2184(this.azC.dG("particle_lifetime_events").get("creation_event"), var1);
      this.method2179(var1);
      return true;
   }

   private void method2179(ParticleData var1) {
      this.azN.method2091(var1);
      this.azC.avJ.method2089(this.azN);
      double var2 = var1.azO;
      var1.azO += 0.03333333333333333;
      var1.aAB += 0.03333333333333333;
      if (var1.aAv != 0.0 && var1.azO > var1.aAv) {
         this.method2182(var1);
      }

      if (this.azC.avx.method2089(this.azN) != 0.0) {
         this.method2182(var1);
      }

      if (!this.azC.avm.equals("dynamic")) {
         if (this.azC.avm.equals("parametric")) {
            var1.pW = this.azC.avR[0].method2089(this.azN);
            var1.pX = this.azC.avR[1].method2089(this.azN);
            var1.pY = this.azC.avR[2].method2089(this.azN);
            var1.aAw = this.azC.avS[0].method2089(this.azN);
            var1.aAx = this.azC.avS[1].method2089(this.azN);
            var1.aAy = this.azC.avS[2].method2089(this.azN);
         }
      } else {
         Vector3d var4 = this.azC.awo && this.azC.awq.method2089(this.azN) != 0.0 ? this.method2171(var1, new Vector3d()) : null;
         double var5 = this.azC.avA.method2089(this.azN);
         var1.aAw = var1.aAw + (this.azC.avQ[0].method2089(this.azN) - var1.aAw * var5) * 0.03333333333333333;
         var1.aAx = var1.aAx + (this.azC.avQ[1].method2089(this.azN) - var1.aAx * var5) * 0.03333333333333333;
         var1.aAy = var1.aAy + (this.azC.avQ[2].method2089(this.azN) - var1.aAy * var5) * 0.03333333333333333;
         var1.pW = var1.pW + var1.aAw * 0.03333333333333333;
         var1.pX = var1.pX + var1.aAx * 0.03333333333333333;
         var1.pY = var1.pY + var1.aAy * 0.03333333333333333;
         if (this.azC.awk) {
            double var7 = this.azC.awj[0].method2089(this.azN);
            double var9 = this.azC.awj[1].method2089(this.azN);
            double var11 = this.azC.awj[2].method2089(this.azN);
            double var13 = this.azC.awj[3].method2089(this.azN);
            double var15 = var7 * var1.pW + var9 * var1.pX + var11 * var1.pY + var13;
            double var17 = var7 * (var1.pW - var1.aAw * 0.03333333333333333)
               + var9 * (var1.pX - var1.aAx * 0.03333333333333333)
               + var11 * (var1.pY - var1.aAy * 0.03333333333333333)
               + var13;
            if (var17 < 0.0 && var15 > 0.0 || var17 > 0.0 && var15 < 0.0) {
               this.method2182(var1);
               return;
            }
         }

         if (var4 != null && this.method2180(var1, var4)) {
            return;
         }
      }

      if (!this.azC.awm.isEmpty() || !this.azC.awn.isEmpty()) {
         if (this.azF.aAj == null) {
            throw new IllegalStateException("Particle block expiry requires a world input");
         }

         Vector3d var19 = this.method2171(var1, new Vector3d());
         String var22 = Objects.requireNonNull(this.azF.aAj.at(var19.x, var19.y, var19.z), "Missing particle block snapshot");
         if (this.azC.awm.contains(var22) || !this.azC.awn.isEmpty() && !this.azC.awn.contains(var22)) {
            this.method2182(var1);
            return;
         }
      }

      if (this.azC.avn.equals("dynamic")) {
         var1.aAA = var1.aAA + (this.azC.avD.method2089(this.azN) - this.azC.avE.method2089(this.azN) * var1.aAA) * 0.03333333333333333;
         var1.azs = (var1.aAz + var1.aAA * var1.azO) * Math.PI / 180.0;
      } else {
         var1.azs = this.azC.avF.method2089(this.azN) * Math.PI / 180.0;
      }

      if ((this.azC.avo.startsWith("direction") || this.azC.avo.equals("lookat_direction"))
         && (this.azC.avV || Math.sqrt(var1.aAw * var1.aAw + var1.aAx * var1.aAx + var1.aAy * var1.aAy) >= this.azC.avW)) {
         double var20 = this.azC.avV ? this.azC.avU[0].method2089(this.azN) : var1.aAw;
         double var6 = this.azC.avV ? this.azC.avU[1].method2089(this.azN) : var1.aAx;
         double var8 = this.azC.avV ? this.azC.avU[2].method2089(this.azN) : var1.aAy;
         double var10 = Math.sqrt(var20 * var20 + var6 * var6 + var8 * var8);
         var1.Jc = var10 == 0.0 ? 0.0 : var20 / var10;
         var1.ave = var10 == 0.0 ? 0.0 : var6 / var10;
         var1.Jd = var10 == 0.0 ? 0.0 : var8 / var10;
      }

      var1.azq = this.azC.avT[0].method2089(this.azN);
      var1.azr = this.azC.avT[1].method2089(this.azN);
      if (var1.azq == 0.0) {
         var1.azq = 1.0E-4;
      }

      if (var1.azr == 0.0) {
         var1.azr = 1.0E-4;
      }

      double var21 = this.azC.avK.method2089(this.azN);
      double var23 = this.azC.awa && var21 != 0.0 ? var21 / var1.aAv : this.azC.avL.method2089(this.azN);
      int var24 = (int)Math.floor(var1.aAB * var23);
      if (var24 > var1.DL) {
         var1.DL = var24;
         if (var21 != 0.0 && var24 >= var21 && this.azC.avZ) {
            var1.DL = 0;
            var1.aAB = 0.0;
         }
      }

      var1.azv = this.azC.awi.rgb(this.azN);
      this.method2183(this.azC.dG("particle_lifetime_events").getJSONObject("timeline"), var2, var1.azO, var1);
   }

   private boolean method2180(ParticleData var1, Vector3d var2) {
      if (this.azF.aAk == null) {
         throw new IllegalStateException("Particle collision requires a world input");
      }

      Vector3d var3 = this.method2171(var1, new Vector3d());
      double var4 = this.azC.awr;
      ParticleBounds var6 = new ParticleBounds(
         Math.min(var2.x, var3.x) - var4,
         Math.min(var2.y, var3.y) - var4,
         Math.min(var2.z, var3.z) - var4,
         Math.max(var2.x, var3.x) + var4,
         Math.max(var2.y, var3.y) + var4,
         Math.max(var2.z, var3.z) + var4
      );
      CollisionResult var7 = ParticleCollision.method2064(
         var2.x, var2.y, var2.z, var3.x - var2.x, var3.y - var2.y, var3.z - var2.z, var4, this.azF.aAk.at(var6)
      );
      if (var7.HY() == 0) {
         return false;
      }

      var3.set(var2.x + var7.sv(), var2.y + var7.HX(), var2.z + var7.sw()).sub(this.Gf, this.Gg, this.Gh);
      var1.pW = var3.x;
      var1.pX = var3.y;
      var1.pY = var3.z;
      if (this.azx != null && this.azC.awb) {
         var1.pW = var1.pW - this.azx.ka();
         var1.pX = var1.pX - this.azx.kb();
         var1.pY = var1.pY - this.azx.kc();
         method2187(var1, false, -this.azx.HG(), -this.azx.method131(), -this.azx.HI(), this.azx.HJ());
         method2187(var1, true, this.azx.HG(), this.azx.method131(), this.azx.HI(), this.azx.HJ());
      }

      double var8 = Math.sqrt(var1.aAw * var1.aAw + var1.aAx * var1.aAx + var1.aAy * var1.aAy);

      for (SpeedEventTrigger var11 : this.azC.awu) {
         if (var8 >= var11.Ia()) {
            this.method2184(var11.HZ(), var1);
         }
      }

      if (this.azC.awp) {
         this.method2182(var1);
         return true;
      }

      double var12 = this.azC.aws * 0.03333333333333333;
      var1.aAw = (var7.HY() & 1) != 0 ? -var1.aAw * this.azC.awt : method2181(var1.aAw, var12);
      var1.aAx = (var7.HY() & 2) != 0 ? -var1.aAx * this.azC.awt : method2181(var1.aAx, var12);
      var1.aAy = (var7.HY() & 4) != 0 ? -var1.aAy * this.azC.awt : method2181(var1.aAy, var12);
      if (this.azx != null && this.azC.awb) {
         method2187(var1, true, -this.azx.HG(), -this.azx.method131(), -this.azx.HI(), this.azx.HJ());
      }

      return false;
   }

   private static double method2181(double var0, double var2) {
      return Math.copySign(Math.max(0.0, Math.abs(var0) - var2), var0);
   }

   private void method2182(ParticleData var1) {
      this.method2184(this.azC.dG("particle_lifetime_events").get("expiration_event"), var1);
      if (this.azI.remove(var1)) {
         this.azJ.add(var1);
         this.azF.aAl--;
         this.azF.aAg.bP(1);
      }
   }

   private void method2183(JSONObject var1, double var2, double var4, ParticleData var6) {
      if (var1 != null) {
         for (String var8 : var1.keySet()) {
            double var9 = Double.parseDouble(var8);
            if (var9 >= var2 && var9 < var4) {
               this.method2184(var1.get(var8), var6);
            }
         }
      }
   }

   private void method2184(Object var1, ParticleData var2) {
      if (var1 != null) {
         if (++this.azU > 16) {
            this.azU--;
            throw new IllegalStateException("Particle event recursion limit");
         }

         try {
            if (var1 instanceof String var3) {
               JSONObject var22 = this.azC.awf.getJSONObject("particle_effect").getJSONObject("events");
               if (var22 != null && var22.containsKey(var3)) {
                  this.method2184(var22.get(var3), var2);
                  return;
               }

               throw new IllegalArgumentException("Missing particle event " + var3);
            }

            if (var1 instanceof JSONArray var4) {
               for (Object var24 : var4) {
                  this.method2184(var24, var2);
               }

               return;
            }

            if (!(var1 instanceof JSONObject var5)) {
               return;
            }

            this.method2184(var5.get("sequence"), var2);
            JSONArray var6 = var5.getJSONArray("randomize");
            if (var6 != null) {
               double var7 = 0.0;

               for (Object var10 : var6) {
                  JSONObject var11 = (JSONObject)var10;
                  var7 = var11.containsKey("weight") ? var7 + var11.getDoubleValue("weight") : Double.NaN;
                  if (var7 == 0.0 || Double.isNaN(var7)) {
                     var7 = 1.0;
                  }
               }

               double var26 = this.azE.nextDouble() * var7;
               double var29 = 0.0;

               for (Object var14 : var6) {
                  JSONObject var15 = (JSONObject)var14;
                  double var16 = var15.getDoubleValue("weight");
                  var29 += var16 == 0.0 ? 1.0 : var16;
                  if (var26 <= var29) {
                     this.method2184(var15, var2);
                     break;
                  }
               }
            }

            if (var5.containsKey("expression")) {
               this.aAa.method2091(null);
               ((MolangExpression)this.azC.awh.get(var5.getString("expression"))).method2089(this.aAa);
            }

            JSONObject var23 = var5.getJSONObject("sound_effect");
            if (var23 != null) {
               if (this.azF.aAh.size() >= 64) {
                  this.azF.ayv++;
               } else {
                  Vector3d var8 = this.method2171(var2, new Vector3d());
                  if (var2 == null && this.azx != null && !this.azC.awb) {
                     var8.add(this.azx.ka(), this.azx.kb(), this.azx.kc());
                  }

                  this.azF.aAh.add(new KeyedVector3(var23.getString("event_name"), var8.x, var8.y, var8.z));
               }
            }

            JSONObject var25 = var5.getJSONObject("particle_effect");
            if (var25 == null) {
               return;
            }

            ParticleEffectDefinition var27 = (ParticleEffectDefinition)this.azD.get(var25.getString("effect"));
            if (var27 == null) {
               throw new IllegalArgumentException("Missing particle effect " + var25.getString("effect"));
            }

            if (this.azF.aAm < 64) {
               this.azF.aAm++;
               ParticleSimulation var28 = new ParticleSimulation(var27, this.azD, this.azE, this.azH, this.azF);
               var28.azS = true;
               Vector3d var30 = this.method2171(var2, new Vector3d());
               if (this.azx != null && !this.azC.awb) {
                  var30.add(this.azx.ka(), this.azx.kb(), this.azx.kc());
               }

               var28.Gf = var30.x;
               var28.Gg = var30.y;
               var28.Gh = var30.z;
               var28.azZ = var25.containsKey("pre_effect_expression") ? (MolangExpression)this.azC.awh.get(var25.getString("pre_effect_expression")) : null;
               if (var2 != null && "particle_with_velocity".equals(var25.getString("type"))) {
                  var28.azY = true;
                  var28.azV = var2.aAw;
                  var28.azW = var2.aAx;
                  var28.azX = var2.aAy;
               }

               this.azK.add(var28);
               var28.start();
               return;
            }

            this.azF.ayv++;
         } finally {
            this.azU--;
         }
      }
   }

   private void method2185(ParticleData var1, boolean var2) {
      double var3 = (this.azE.nextDouble() * 2.0 - 1.0) * Math.PI / 2.0;
      double var5 = (this.azE.nextDouble() * 2.0 - 1.0) * Math.PI / 2.0;
      double var7 = (this.azE.nextDouble() * 2.0 - 1.0) * Math.PI / 2.0;
      double var9 = Math.sin(var3);
      double var11 = Math.sin(var5);
      double var13 = Math.sin(var7);
      double var15 = Math.cos(var3);
      double var17 = Math.cos(var5);
      double var19 = Math.cos(var7);
      method2187(
         var1,
         var2,
         var9 * var17 * var19 + var15 * var11 * var13,
         var15 * var11 * var19 - var9 * var17 * var13,
         var15 * var17 * var13 + var9 * var11 * var19,
         var15 * var17 * var19 - var9 * var11 * var13
      );
   }

   private static void method2186(ParticleData var0, double var1, double var3, double var5) {
      double var9 = 0.0;
      double var13 = var3 + 1.0;
      double var7;
      double var11;
      if (var13 < 1.0E-6) {
         var7 = 0.0;
         var11 = 1.0;
         var13 = 0.0;
      } else {
         var7 = var5;
         var11 = -var1;
      }

      double var15 = Math.sqrt(var7 * var7 + var9 * var9 + var11 * var11 + var13 * var13);
      method2187(var0, false, var7 / var15, var9 / var15, var11 / var15, var13 / var15);
   }

   private static void method2187(ParticleData var0, boolean var1, double var2, double var4, double var6, double var8) {
      double var10 = var1 ? var0.aAw : var0.pW;
      double var12 = var1 ? var0.aAx : var0.pX;
      double var14 = var1 ? var0.aAy : var0.pY;
      double var16 = var8 * var10 + var4 * var14 - var6 * var12;
      double var18 = var8 * var12 + var6 * var10 - var2 * var14;
      double var20 = var8 * var14 + var2 * var12 - var4 * var10;
      double var22 = -var2 * var10 - var4 * var12 - var6 * var14;
      var10 = var16 * var8 + var22 * -var2 + var18 * -var6 - var20 * -var4;
      var12 = var18 * var8 + var22 * -var4 + var20 * -var2 - var16 * -var6;
      var14 = var20 * var8 + var22 * -var6 + var16 * -var4 - var18 * -var2;
      if (var1) {
         var0.aAw = var10;
         var0.aAx = var12;
         var0.aAy = var14;
      } else {
         var0.pW = var10;
         var0.pX = var12;
         var0.pY = var14;
      }
   }
}
