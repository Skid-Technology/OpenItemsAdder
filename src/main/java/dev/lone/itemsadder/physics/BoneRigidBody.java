package dev.lone.itemsadder.physics;

import com.github.stephengold.joltjni.Body;
import org.joml.Quaternionf;
import org.joml.Vector3f;

final class BoneRigidBody {
   final int GP;
   final double GQ;
   double GR;
   final Vector3f GS = new Vector3f();
   final Vector3f GT = new Vector3f();
   final Quaternionf GU = new Quaternionf();
   final Quaternionf GV = new Quaternionf();
   final Body GW;
   int GX;
   boolean GY;
   final Vector3f GZ = new Vector3f();
   final Vector3f Ha = new Vector3f();

   BoneRigidBody(int var1, double var2, Body var4) {
      this.GP = var1;
      this.GQ = var2;
      this.GW = var4;
   }
}
