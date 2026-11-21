package dev.lone.itemsadder.util;

final class BinaryPackException extends Exception {
   private final ImageOptimizeStatus status;

   private BinaryPackException(ImageOptimizeStatus var1, String var2) {
      super(var2);
      this.status = var1;
   }
}
