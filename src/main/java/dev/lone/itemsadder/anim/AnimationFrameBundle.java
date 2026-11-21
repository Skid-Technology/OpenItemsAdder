package dev.lone.itemsadder.anim;

import java.util.List;

class AnimationFrameBundle {
   private final TimedSample PZ;
   private final List Qa;
   private final List Qb;
   private final boolean Qc;

   private AnimationFrameBundle(TimedSample var1, List var2, List var3, boolean var4) {
      this.PZ = var1;
      this.Qa = var2;
      this.Qb = var3;
      this.Qc = var4;
   }

   public TimedSample tY() {
      return this.PZ;
   }

   public List tZ() {
      return this.Qa;
   }

   public List ua() {
      return this.Qb;
   }

   public boolean ub() {
      return this.Qc;
   }
}
