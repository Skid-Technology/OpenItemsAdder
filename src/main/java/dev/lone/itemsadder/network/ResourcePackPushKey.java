package dev.lone.itemsadder.network;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

class ResourcePackPushKey {
   private final long bzp;
   private final UUID bzq;
   private final String bzr;
   @Nullable
   private final String bzs;

   private ResourcePackPushKey(long var1, UUID var3, String var4, @Nullable String var5) {
      this.bzp = var1;
      this.bzq = var3;
      this.bzr = var4;
      this.bzs = var5;
   }

   boolean method124(long var1, UUID var3, String var4) {
      return this.bzp == var1 && this.bzq.equals(var3) && this.bzr.equals(var4);
   }

   public long bl() {
      return this.bzp;
   }

   public UUID bk() {
      return this.bzq;
   }

   public String method125() {
      return this.bzr;
   }

   @Nullable
   public String VW() {
      return this.bzs;
   }
}
