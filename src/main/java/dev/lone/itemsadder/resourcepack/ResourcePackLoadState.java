package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.core.ContentManager;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class ResourcePackLoadState {
   private final CompletableFuture future;
   private boolean finished;
   private final Set repairedKeys;
   private final Set pendingKeys;

   boolean handleAssetFailure(VanillaAssetException var1) {
      return !var1.isRepairable() ? this.tryMarkFinished() : this.isActive() && this.repairedKeys.add(var1.getAssetKey());
   }

   boolean tryMarkFinished() {
      if (this.isActive() && !this.finished) {
         this.finished = true;
         return true;
      } else {
         return false;
      }
   }

   private ResourcePackLoadState(ContentManager var1) {
      this.owner = var1;
      this.future = new CompletableFuture();
      this.repairedKeys = ConcurrentHashMap.newKeySet();
      this.pendingKeys = new HashSet();
   }

   public CompletableFuture getFuture() {
      return this.future.copy();
   }

   public boolean isActive() {
      return !this.owner.field57 && this.owner.field52.get() == this;
   }

   public boolean complete() {
      if (!this.owner.field52.compareAndSet(this, null)) {
         return false;
      }

      this.future.complete(null);
      return true;
   }
}
