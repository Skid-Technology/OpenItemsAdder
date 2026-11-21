package dev.lone.itemsadder.util;

import java.io.IOException;

public final class LoadFailedException extends IOException {
   private final boolean fatal;

   private LoadFailedException(String var1, boolean var2) {
      super(var1);
      this.fatal = var2;
   }

   private LoadFailedException(String var1, boolean var2, Throwable var3) {
      super(var1, var3);
      this.fatal = var2;
   }
}
