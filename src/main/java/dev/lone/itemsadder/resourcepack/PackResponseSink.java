package dev.lone.itemsadder.resourcepack;

import java.nio.charset.StandardCharsets;

public interface PackResponseSink {
   default void sendStatus(int var1, String var2, String var3) {
      this.send(var1, var2 != null ? var2.getBytes(StandardCharsets.UTF_8) : null, var3);
   }

   void send(int var1, byte[] var2, String var3);
}
