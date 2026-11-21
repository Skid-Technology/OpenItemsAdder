package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ConnectionHandle;
import java.util.UUID;

public class ConnectionRecord {
   private final UUID blE;
   private final long blF;
   private final long blG;
   private final ConnectionHandle blH;

   public ConnectionRecord(UUID var1, long var2, long var4, ConnectionHandle var6) {
      this.blE = var1;
      this.blF = var2;
      this.blG = var4;
      this.blH = var6;
   }

   public UUID bk() {
      return this.blE;
   }

   public long bl() {
      return this.blF;
   }

   public long ai() {
      return this.blG;
   }

   public ConnectionHandle PC() {
      return this.blH;
   }
}
