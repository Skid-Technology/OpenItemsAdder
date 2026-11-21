package dev.lone.itemsadder.resourcepack;

import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

final class PackRequestReader {
   private final InputStream bKj;
   private final Socket bKk;
   private final long bKl;
   private final byte[] bKm = new byte[1024];
   private int axt;
   private int ava;

   private PackRequestReader(InputStream var1, Socket var2, long var3) {
      this.bKj = var1;
      this.bKk = var2;
      this.bKl = var3;
   }

   private int readByte() {
      long var1 = this.bKl - System.nanoTime();
      if (var1 <= 0L) {
         throw new SocketTimeoutException("HTTP header deadline exceeded.");
      }

      if (this.axt == this.ava) {
         long var3 = Math.max(1L, TimeUnit.NANOSECONDS.toMillis(var1));
         this.bKk.setSoTimeout((int)Math.min(5000L, var3));
         this.ava = this.bKj.read(this.bKm);
         this.axt = 0;
         if (this.ava < 0) {
            return -1;
         }
      }

      return this.bKm[this.axt++] & 0xFF;
   }
}
