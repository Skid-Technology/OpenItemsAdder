package dev.lone.itemsadder.anim;

import dev.lone.itemsadder.entity.BoneHierarchy;
import dev.lone.itemsadder.entity.DisplayEntity;
import org.joml.Matrix4f;

public class ComposedDisplayState {
   private final BoneHierarchy[] aTd;
   private final DisplayEntity[] aTe;
   private final Matrix4f[] aTf;
   private final InterpolationCache aTg;

   private ComposedDisplayState(BoneHierarchy[] var1, DisplayEntity[] var2, Matrix4f[] var3, InterpolationCache var4) {
      this.aTd = var1;
      this.aTe = var2;
      this.aTf = var3;
      this.aTg = var4;
   }

   public BoneHierarchy[] Of() {
      return this.aTd;
   }

   public DisplayEntity[] Og() {
      return this.aTe;
   }

   public Matrix4f[] Oh() {
      return this.aTf;
   }

   public InterpolationCache Oi() {
      return this.aTg;
   }
}
