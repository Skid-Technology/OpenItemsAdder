package dev.lone.itemsadder.util.serial;

import java.io.IOException;

public class SerialException extends IOException {
   private static final long serialVersionUID = 3969366848110070516L;

   public SerialException() {
   }

   public SerialException(String var1) {
      super(var1);
   }

   public SerialException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public SerialException(Throwable var1) {
      super(var1);
   }
}
