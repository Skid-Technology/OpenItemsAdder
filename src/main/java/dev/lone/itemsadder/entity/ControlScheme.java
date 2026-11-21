package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.FloatPair;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class ControlScheme {
   private final ControlInputMode xZ;
   private final boolean ya;
   @Nullable
   private final FloatPair yb;
   public static final ControlScheme yc = new ControlScheme(ControlInputMode.MOUSE, false);

   public ControlScheme(ControlInputMode var1, boolean var2) {
      this(var1, var2, null);
   }

   public ControlScheme(ControlInputMode var1, boolean var2, @Nullable FloatPair var3) {
      this.xZ = var1;
      this.ya = var2;
      this.yb = var3;
   }

   public float method67(float var1, float var2, float var3, boolean var4) {
      if (this.xZ == ControlInputMode.WASD) {
         return var1 - Math.signum(var3) * 3.0F;
      } else if (var4) {
         return BlockFaceUtils.lerpYaw(var1, var2, 0.6F);
      } else {
         return Math.abs(BlockFaceUtils.normalizeYaw(var1 - var2)) > 60.0F ? BlockFaceUtils.lerpYaw(var1, var2, 0.1F) : var1;
      }
   }

   public Vector method68(float var1, double var2, float var4, float var5) {
      double var6 = this.ya ? var5 : 0.0;
      double var8 = Math.hypot(var4, var6);
      if (var8 == 0.0) {
         return new Vector();
      }

      double var10 = Math.toRadians(var1);
      return new Vector(-Math.sin(var10) * var4 + Math.cos(var10) * var6, 0.0, Math.cos(var10) * var4 + Math.sin(var10) * var6)
         .multiply(var2 / Math.max(1.0, var8));
   }

   public ControlInputMode inputMode() {
      return this.xZ;
   }

   public boolean mt() {
      return this.ya;
   }

   @Nullable
   public FloatPair sensitivity() {
      return this.yb;
   }
}
