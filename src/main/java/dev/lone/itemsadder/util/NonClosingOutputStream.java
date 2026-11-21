package dev.lone.itemsadder.util;

import java.io.FilterOutputStream;
import java.io.OutputStream;

public final class NonClosingOutputStream extends FilterOutputStream {
   public NonClosingOutputStream(OutputStream var1) {
      super(var1);
   }

   @Override
   public void close() {
      this.flush();
   }
}
