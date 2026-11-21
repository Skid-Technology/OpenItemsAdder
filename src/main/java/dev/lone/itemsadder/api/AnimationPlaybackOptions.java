package dev.lone.itemsadder.api;

public class AnimationPlaybackOptions {
   private final double speed;
   private final boolean reversed;
   private final double start;
   private final double end;
   public static final AnimationPlaybackOptions DEFAULT = new AnimationPlaybackOptions(1.0, false, 0.0, -1.0);

   public AnimationPlaybackOptions(double speed, boolean reversed, double start, double end) {
      if (Double.isFinite(speed) && !(speed <= 0.0) && !(speed > 16.0)) {
         if (Double.isFinite(start) && !(start < 0.0) && Double.isFinite(end) && (end == -1.0 || !(end <= start))) {
            this.speed = speed;
            this.reversed = reversed;
            this.start = start;
            this.end = end;
         } else {
            throw new IllegalArgumentException("Playback range must have 0 <= start < end, or end=-1");
         }
      } else {
         throw new IllegalArgumentException("Playback speed must be greater than zero and at most 16");
      }
   }
}
