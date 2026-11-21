package dev.lone.itemsadder.util;

import java.util.Objects;

public class BinaryPackResult {
   private final ImageOptimizeStatus status;
   private final byte[] bytes;
   private final String message;

   BinaryPackResult(ImageOptimizeStatus var1, byte[] var2, String var3) {
      Objects.requireNonNull(var1, "status");
      Objects.requireNonNull(var2, "bytes");
      Objects.requireNonNull(var3, "reason");
      this.status = var1;
      this.bytes = var2;
      this.message = var3;
   }

   public ImageOptimizeStatus VS() {
      return this.status;
   }

   public byte[] Ud() {
      return this.bytes;
   }

   public String Ue() {
      return this.message;
   }
}
