package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ConnectionHandle;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class ViewerSession {
   private final UUID Uy;
   private final ConnectionHandle Uz;
   private final int UA;
   private final boolean UB;
   private final CustomEntityImpl UC;
   private final AtomicBoolean UD = new AtomicBoolean();
   private final AtomicReference UE = new AtomicReference();
   private final AtomicReference UF = new AtomicReference();
   private final ConcurrentLinkedQueue UG = new ConcurrentLinkedQueue();
   private final ConcurrentLinkedQueue UH = new ConcurrentLinkedQueue();

   private ViewerSession(UUID var1, ConnectionHandle var2, int var3, boolean var4, CustomEntityImpl var5) {
      this.Uy = var1;
      this.Uz = var2;
      this.UA = var3;
      this.UB = var4;
      this.UC = var5;
   }
}
