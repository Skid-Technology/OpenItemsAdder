package dev.lone.itemsadder.physics;

import com.github.stephengold.joltjni.TwoBodyConstraint;
import org.joml.Vector3f;

final class BoneConstraintLink {
   final int GK;
   final int GL;
   final Vector3f GM = new Vector3f();
   final Vector3f GN = new Vector3f();
   final TwoBodyConstraint GO;

   BoneConstraintLink(int var1, int var2, TwoBodyConstraint var3) {
      this.GK = var1;
      this.GL = var2;
      this.GO = var3;
   }
}
