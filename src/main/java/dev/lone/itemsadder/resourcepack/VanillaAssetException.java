package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.util.IaRuntimeException;
import java.util.concurrent.FutureTask;

public final class VanillaAssetException extends IaRuntimeException {
   private final FutureTask iG;
   private final boolean iH;
   private final String iI;

   private VanillaAssetException(String var1, Throwable var2, FutureTask var3) {
      this(var1, var2, var3, false, null);
   }

   private VanillaAssetException(String var1, Throwable var2, FutureTask var3, boolean var4, String var5) {
      super(var1, var2);
      this.iG = var3;
      this.iH = var4;
      this.iI = var5;
   }

   public boolean isRepairable() {
      return this.iH;
   }

   public String getAssetKey() {
      return this.iI;
   }
}
