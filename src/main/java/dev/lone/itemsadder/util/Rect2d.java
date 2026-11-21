package dev.lone.itemsadder.util;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;

public class Rect2d {
   private final Vector2d min;
   private final Vector2d max;

   public Rect2d(Vector2d var1, Vector2d var2) {
      this.min = var1;
      this.max = var2;
   }

   @NotNull
   public static Rect2d of(Vector2d var0, Vector2d var1) {
      return new Rect2d(var0, var1);
   }

   public Vector2d getMin() {
      return this.min;
   }

   public Vector2d getMax() {
      return this.max;
   }
}
