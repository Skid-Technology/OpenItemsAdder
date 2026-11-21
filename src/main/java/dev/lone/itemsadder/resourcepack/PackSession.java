package dev.lone.itemsadder.resourcepack;

import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

class PackSession {
   private final long connectionId;
   private final WeakReference connection;
   private final PlayerPackState state;
   private final UUID base;
   private final UUID requestedBase;
   private final String baseHash;
   private final String universalHash;
   private final PackBinding equipment;
   private final AtomicReference shaders;

   private PackSession(
      long var1, WeakReference var3, PlayerPackState var4, UUID var5, UUID var6, String var7, String var8, PackBinding var9, AtomicReference var10
   ) {
      this.connectionId = var1;
      this.connection = var3;
      this.state = var4;
      this.base = var5;
      this.requestedBase = var6;
      this.baseHash = var7;
      this.universalHash = var8;
      this.equipment = var9;
      this.shaders = var10;
   }
}
