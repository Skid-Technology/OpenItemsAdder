package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.util.DoubleVector3Tagged;
import java.util.Map;
import java.util.UUID;

public class EntityDescriptor {
   private final String RQ;
   private final UUID field21;
   private final String RS;
   private final String RT;
   private final String RU;
   private final String RV;
   private final String RW;
   private final Map RX;
   private final String RY;
   private final double RZ;
   private final DoubleVector3Tagged Sa;
   private final boolean Sb;
   private final int Sc;

   public EntityDescriptor(
      String var1,
      UUID var2,
      String var3,
      String var4,
      String var5,
      String var6,
      String var7,
      Map var8,
      String var9,
      double var10,
      DoubleVector3Tagged var12,
      boolean var13,
      int var14
   ) {
      this.RQ = var1;
      this.field21 = var2;
      this.RS = var3;
      this.RT = var4;
      this.RU = var5;
      this.RV = var6;
      this.RW = var7;
      this.RX = var8;
      this.RY = var9;
      this.RZ = var10;
      this.Sa = var12;
      this.Sb = var13;
      this.Sc = var14;
   }

   public String uI() {
      return this.RQ;
   }

   public UUID uuid() {
      return this.field21;
   }

   public String uK() {
      return this.RS;
   }

   public String uL() {
      return this.RT;
   }

   public String uM() {
      return this.RU;
   }

   public String uN() {
      return this.RV;
   }

   public String uO() {
      return this.RW;
   }

   public Map attributes() {
      return this.RX;
   }

   public String uQ() {
      return this.RY;
   }

   public double uR() {
      return this.RZ;
   }

   public DoubleVector3Tagged position() {
      return this.Sa;
   }

   public boolean uT() {
      return this.Sb;
   }

   public int uU() {
      return this.Sc;
   }
}
