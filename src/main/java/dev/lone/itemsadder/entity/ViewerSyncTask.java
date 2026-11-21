package dev.lone.itemsadder.entity;

import org.jetbrains.annotations.Nullable;

class ViewerSyncTask {
   private final long vg;
   private final boolean vh;
   @Nullable
   private final EntityViewer vi;
   private final boolean vj;

   private ViewerSyncTask(long var1, boolean var3, @Nullable EntityViewer var4, boolean var5) {
      this.vg = var1;
      this.vh = var3;
      this.vi = var4;
      this.vj = var5;
   }

   public long timestamp() {
      return this.vg;
   }

   public boolean lp() {
      return this.vh;
   }

   @Nullable
   public EntityViewer viewer() {
      return this.vi;
   }

   public boolean lr() {
      return this.vj;
   }
}
