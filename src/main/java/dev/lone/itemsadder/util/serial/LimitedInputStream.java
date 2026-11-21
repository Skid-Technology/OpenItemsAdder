package dev.lone.itemsadder.util.serial;

import java.io.FilterInputStream;
import java.io.InputStream;

public final class LimitedInputStream extends FilterInputStream {
   private int limit;

   public LimitedInputStream(InputStream var1) {
      super(var1);
   }

   public LimitedInputStream(InputStream var1, int var2) {
      super(var1);
      this.limit = var2;
   }

   LimitedInputStream withLimit(int var1) {
      this.limit = var1;
      return this;
   }

   @Override
   public int available() {
      return Math.min(super.available(), this.limit);
   }

   @Override
   public int read() {
      if (this.limit <= 0) {
         return -1;
      }

      int var1 = super.read();
      if (var1 >= 0) {
         this.limit--;
      }

      return var1;
   }

   @Override
   public int read(byte[] b, int off, int len) {
      if (this.limit <= 0) {
         return -1;
      }

      len = Math.min(len, this.limit);
      int var4 = super.read(b, off, len);
      if (var4 >= 0) {
         this.limit -= var4;
      }

      return var4;
   }

   @Override
   public long skip(long n) {
      long var3 = super.skip(Math.min(n, this.limit));
      if (var3 >= 0L) {
         this.limit = (int)(this.limit - var3);
      }

      return var3;
   }
}
