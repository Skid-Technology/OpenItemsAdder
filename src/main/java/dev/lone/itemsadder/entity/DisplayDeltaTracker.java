package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.util.DoubleVector3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

final class DisplayDeltaTracker extends DoubleVector3 {
   boolean aRG;
   boolean GD;
   long aQV = -1L;
   int aRH = -1;
   float aRI;
   boolean aRJ;
   boolean aRK;
   int aRL;
   int aRM;
   int aRN;
   int aRO;
   int aRP;
   int aRQ;
   int aRR;
   int aRS;
   int aRT;
   int aRU;
   int aRV;
   int aRW;
   int aRX;
   int aRY;

   int computeDelta(TransformDecomposition var1, float var2) {
      if (!this.aRJ) {
         return 127;
      }

      Vector3f var3 = var1.getTranslation();
      Quaternionf var4 = var1.getRotation();
      Vector3f var5 = var1.getScale();
      Quaternionf var6 = var1.mA();
      byte var7 = 7;
      if (this.aRL != quantize(var3.x()) || this.aRM != quantize(var2 == 0.0F ? var3.y() : var3.y() - var2) || this.aRN != quantize(var3.z())) {
         var7 |= 8;
      }

      if (this.aRO != quantize(var4.x()) || this.aRP != quantize(var4.y()) || this.aRQ != quantize(var4.z()) || this.aRR != quantize(var4.w())) {
         var7 |= 16;
      }

      if (this.aRS != quantize(var5.x()) || this.aRT != quantize(var5.y()) || this.aRU != quantize(var5.z())) {
         var7 |= 32;
      }

      if (this.aRV != quantize(var6.x()) || this.aRW != quantize(var6.y()) || this.aRX != quantize(var6.z()) || this.aRY != quantize(var6.w())) {
         var7 |= 64;
      }

      return var7 == 7 ? 127 : var7;
   }

   void applyDelta(TransformDecomposition var1, float var2) {
      Vector3f var3 = var1.getTranslation();
      Quaternionf var4 = var1.getRotation();
      Vector3f var5 = var1.getScale();
      Quaternionf var6 = var1.mA();
      this.aRL = quantize(var3.x());
      this.aRM = quantize(var2 == 0.0F ? var3.y() : var3.y() - var2);
      this.aRN = quantize(var3.z());
      this.aRO = quantize(var4.x());
      this.aRP = quantize(var4.y());
      this.aRQ = quantize(var4.z());
      this.aRR = quantize(var4.w());
      this.aRS = quantize(var5.x());
      this.aRT = quantize(var5.y());
      this.aRU = quantize(var5.z());
      this.aRV = quantize(var6.x());
      this.aRW = quantize(var6.y());
      this.aRX = quantize(var6.z());
      this.aRY = quantize(var6.w());
      this.aRJ = true;
   }

   private static int quantize(float var0) {
      return Float.floatToRawIntBits(var0);
   }
}
