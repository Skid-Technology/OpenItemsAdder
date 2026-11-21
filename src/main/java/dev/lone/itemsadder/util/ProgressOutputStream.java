package dev.lone.itemsadder.util;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import org.jetbrains.annotations.NotNull;

class ProgressOutputStream extends FilterOutputStream {
   private final ProgressListener listener;
   private long written;

   ProgressOutputStream(OutputStream var1, ProgressListener var2) {
      super(var1);
      this.listener = var2;
      this.written = 0L;
   }

   @Override
   public void write(byte @NotNull [] bytes, int offset, int length) {
      this.out.write(bytes, offset, length);
      this.written += length;
      this.listener.step(this.written);
   }

   @Override
   public void write(int b) {
      this.out.write(b);
      this.written++;
      this.listener.step(this.written);
   }
}
