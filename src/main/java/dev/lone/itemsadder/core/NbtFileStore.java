package dev.lone.itemsadder.core;

import beer.devs.fastnbt.nms.nbt.NFile;
import org.jetbrains.annotations.Nullable;

class NbtFileStore {
   public final NFile dK;
   boolean dL;

   public NbtFileStore(NFile var1) {
      this.dK = var1;
   }

   public synchronized boolean method2388(String var1) {
      return this.dK.hasKey(var1);
   }

   public synchronized void method2389(String var1, float var2) {
      if (!this.dK.hasKey(var1) || this.dK.getFloat(var1) != var2) {
         this.dK.setFloat(var1, var2);
         this.dL = true;
      }
   }

   @Nullable
   public synchronized Float method2390(String var1, @Nullable Float var2) {
      return this.dK.hasKey(var1) ? this.dK.getFloat(var1) : var2;
   }

   public synchronized boolean bD() {
      if (!this.dL) {
         return false;
      }

      this.dK.save();
      this.dL = false;
      return true;
   }
}
