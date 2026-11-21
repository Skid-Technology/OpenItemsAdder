package dev.lone.itemsadder.anim;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public class AnimationFrame {
   private final int field9;
   private final List tu;
   private final long tv;
   @Nullable
   private final List tw;

   public AnimationFrame(int var1, List var2, long var3, @Nullable List var5) {
      this.field9 = var1;
      this.tu = var2;
      this.tv = var3;
      this.tw = var5;
   }

   public int jI() {
      return this.field9;
   }

   public List jJ() {
      return this.tu;
   }

   public long jK() {
      return this.tv;
   }

   @Nullable
   public List jL() {
      return this.tw;
   }
}
