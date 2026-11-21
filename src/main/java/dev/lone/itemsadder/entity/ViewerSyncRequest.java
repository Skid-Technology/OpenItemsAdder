package dev.lone.itemsadder.entity;

import org.jetbrains.annotations.Nullable;

class ViewerSyncRequest {
   private final long vk;
   private final boolean vl;
   @Nullable
   private final ViewerSyncTask vm;

   private ViewerSyncRequest(long var1, boolean var3, @Nullable ViewerSyncTask var4) {
      this.vk = var1;
      this.vl = var3;
      this.vm = var4;
   }

   public long timestamp() {
      return this.vk;
   }

   public boolean ls() {
      return this.vl;
   }

   @Nullable
   public ViewerSyncTask task() {
      return this.vm;
   }
}
