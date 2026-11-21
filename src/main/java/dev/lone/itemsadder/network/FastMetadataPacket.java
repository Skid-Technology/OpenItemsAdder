package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ConnectionHandle;
import beer.devs.nativepackets.api.EntityMetadataField;
import beer.devs.nativepackets.api.FastMetadataWriter;
import beer.devs.nativepackets.api.FastPacketBatch;
import beer.devs.nativepackets.api.FastPacketCapability;
import beer.devs.nativepackets.api.MinecraftVersion;
import beer.devs.nativepackets.api.NativeItem;
import beer.devs.nativepackets.api.NativePacketFactory;
import beer.devs.nativepackets.api.NativeValue;
import beer.devs.nativepackets.api.EntityMetadataFields.Display;
import beer.devs.nativepackets.api.EntityMetadataFields.Display_ItemDisplay;
import beer.devs.nativepackets.api.EntityMetadataFields.Entity;
import dev.lone.itemsadder.entity.DisplayEntity;
import dev.lone.itemsadder.entity.TransformDecomposition;
import java.util.function.Consumer;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class FastMetadataPacket extends FastPacketAction implements Consumer {
   private final int aSa;
   private final int aSb;
   private final int aSc;
   private final int aSd;
   private final int aSe;
   private final float aSf;
   private final float aSg;
   private final float aSh;
   private final float aSi;
   private final float aSj;
   private final float aSk;
   private final float aSl;
   private final float aSm;
   private final float aSn;
   private final float aSo;
   private final float aSp;
   private final float aSq;
   private final float aSr;
   private final float aSs;
   private final boolean aSt;
   private final boolean aSu;
   private final NativeItem aSv;
   private final byte aSw;
   private final byte aSx;
   private final float aSy;

   public FastMetadataPacket(
      int var1,
      int var2,
      int var3,
      int var4,
      TransformDecomposition var5,
      float var6,
      boolean var7,
      NativeValue var8,
      byte var9,
      float var10,
      byte var11,
      MinecraftVersion var12
   ) {
      this(var1, var2, var3, 0, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   public FastMetadataPacket(
      int var1,
      int var2,
      int var3,
      int var4,
      int var5,
      TransformDecomposition var6,
      float var7,
      boolean var8,
      NativeValue var9,
      byte var10,
      float var11,
      byte var12,
      MinecraftVersion var13
   ) {
      this.aSa = var1;
      this.aSb = var2;
      this.aSd = var4;
      this.aSc = var3;
      this.aSe = var5;
      this.aSt = var8;
      this.aSv = var9 == null ? null : (var9 instanceof NativeItem var14 ? var14 : var9::nativeHandle);
      this.aSw = var10;
      this.aSy = var11;
      this.aSx = var12;
      Vector3f var21 = var6.getTranslation();
      Quaternionf var15 = var6.getRotation();
      Vector3f var16 = var6.getScale();
      Quaternionf var17 = var6.mA();
      this.aSf = var21.x;
      this.aSg = var7 == 0.0F ? var21.y : var21.y - var7;
      this.aSh = var21.z;
      this.aSi = var15.x;
      this.aSj = var15.y;
      this.aSk = var15.z;
      this.aSl = var15.w;
      this.aSm = var16.x;
      this.aSn = var16.y;
      this.aSo = var16.z;
      this.aSp = var17.x;
      this.aSq = var17.y;
      this.aSr = var17.z;
      this.aSs = var17.w;
      boolean var18 = !var8 || this.aSv != null;
      EntityMetadataField[] var19 = var8 ? DisplayEntity.aQI : DisplayEntity.aQH;

      for (int var20 = 0; var20 < var19.length; var20++) {
         if (var8 || (var5 & 1 << var20) != 0) {
            var18 &= FastMetadataWriter.supports(var19[var20], var13);
         }
      }

      this.aSu = var18;
   }

   @Override
   public boolean method3162(ConnectionHandle var1) {
      return this.aSu && var1.supportsFast(FastPacketCapability.METADATA);
   }

   @Override
   public void method3163(FastPacketBatch var1) {
      var1.entityMetadata(this.aSa, this);
   }

   public void method3169(FastMetadataWriter var1) {
      if (this.aSt) {
         var1.byteValue(Entity.SHARED_FLAGS, this.aSw);
         var1.floatValue(Display.VIEW_RANGE, this.aSy);
      }

      if ((this.aSe & 1) != 0) {
         var1.intValue(Display.TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS, this.aSd);
      }

      if ((this.aSe & 2) != 0) {
         var1.intValue(Display.TRANSFORMATION_INTERPOLATION_DURATION, this.aSb);
      }

      if ((this.aSe & 4) != 0) {
         var1.intValue(Display.POS_ROT_INTERPOLATION_DURATION, this.aSc);
      }

      if ((this.aSe & 8) != 0) {
         var1.vector(Display.TRANSLATION, this.aSf, this.aSg, this.aSh);
      }

      if ((this.aSe & 16) != 0) {
         var1.quaternion(Display.LEFT_ROTATION, this.aSi, this.aSj, this.aSk, this.aSl);
      }

      if ((this.aSe & 32) != 0) {
         var1.vector(Display.SCALE, this.aSm, this.aSn, this.aSo);
      }

      if ((this.aSe & 64) != 0) {
         var1.quaternion(Display.RIGHT_ROTATION, this.aSp, this.aSq, this.aSr, this.aSs);
      }

      if (this.aSt) {
         var1.floatValue(Display.SHADOW_RADIUS, 0.0F);
         var1.floatValue(Display.SHADOW_STRENGTH, 0.0F);
         var1.item(Display_ItemDisplay.ITEM_STACK, this.aSv);
         var1.byteValue(Display_ItemDisplay.ITEM_DISPLAY, this.aSx);
      }
   }

   @Override
   protected Object method3164(NativePacketFactory var1) {
      NativeValue var2 = () -> new Vector3f(this.aSf, this.aSg, this.aSh);
      NativeValue var3 = () -> new Quaternionf(this.aSi, this.aSj, this.aSk, this.aSl);
      NativeValue var4 = () -> new Vector3f(this.aSm, this.aSn, this.aSo);
      NativeValue var5 = () -> new Quaternionf(this.aSp, this.aSq, this.aSr, this.aSs);
      if (this.aSt) {
         return var1.metadata(
            this.aSa, DisplayEntity.aQI, new Object[]{this.aSw, this.aSy, this.aSd, this.aSb, this.aSc, var2, var3, var4, var5, 0.0F, 0.0F, this.aSv, this.aSx}
         );
      }

      EntityMetadataField[] var6 = new EntityMetadataField[Integer.bitCount(this.aSe)];
      Object[] var7 = new Object[var6.length];
      int var8 = 0;

      for (int var9 = 0; var9 < DisplayEntity.aQH.length; var9++) {
         if ((this.aSe & 1 << var9) != 0) {
            var6[var8] = DisplayEntity.aQH[var9];

            var7[var8++] = switch (var9) {
               case 0 -> this.aSd;
               case 1 -> this.aSb;
               case 2 -> this.aSc;
               case 3 -> var2;
               case 4 -> var3;
               case 5 -> var4;
               case 6 -> var5;
               default -> throw new AssertionError(var9);
            };
         }
      }

      return var1.metadata(this.aSa, var6, var7);
   }
}
