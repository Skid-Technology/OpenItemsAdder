package dev.lone.itemsadder.anim;

import java.util.List;
import java.util.Map;

public class AnimationTrack {
   private final long tx;
   private final long ty;
   private final PlaybackRequest tz;
   private final List tA;
   private final Map tB;
   private final Map tC;

   public AnimationTrack(long var1, long var3, PlaybackRequest var5, List var6, Map var7, Map var8) {
      this.tx = var1;
      this.ty = var3;
      this.tz = var5;
      this.tA = var6;
      this.tB = var7;
      this.tC = var8;
   }

   public long jM() {
      return this.tx;
   }

   public long jN() {
      return this.ty;
   }

   public PlaybackRequest jO() {
      return this.tz;
   }

   public List jP() {
      return this.tA;
   }

   public Map jQ() {
      return this.tB;
   }

   public Map jR() {
      return this.tC;
   }
}
