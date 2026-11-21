package dev.lone.itemsadder.particle;

import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

final class ParticleRotation {
   static Quaternionf method2133(String var0, double var1, double var3, double var5, double var7, double var9, double var11, ParentPose var13) {
      if (var13 != null && var0.equals("lookat_direction")) {
         Vector3d var14 = new Quaterniond(var13.HG(), var13.method131(), var13.HI(), var13.HJ()).conjugate().transform(new Vector3d(var7, var9, var11));
         var7 = var14.x;
         var9 = var14.y;
         var11 = var14.z;
      }

      Quaternionf var15 = method2134(var0, var1, var3, var5, var7, var9, var11);
      if (var13 != null && (var0.startsWith("direction") || var0.startsWith("emitter_transform") || var0.equals("lookat_direction"))) {
         var15.premul(new Quaternionf((float)var13.HG(), (float)var13.method131(), (float)var13.HI(), (float)var13.HJ()));
      }

      return var15;
   }

   static Quaternionf method2134(String var0, double var1, double var3, double var5, double var7, double var9, double var11) {
      Quaternionf var13 = new Quaternionf();
      if (var0.startsWith("direction")) {
         if (var3 == 1.0) {
            var3 = -1.0;
         } else if (var3 == -1.0) {
            var3 = 1.0;
            var5 = -1.0E-5;
         }

         float var18 = (float)Math.atan2(var1, var5);
         float var19 = (float)Math.atan2(var3, Math.hypot(var1, var5));

         return switch (var0) {
            case "direction_x" -> var13.rotationYXZ(var18 - (float) (Math.PI / 2), 0.0F, var19);
            case "direction_y" -> var13.rotationYXZ(var18 - (float) Math.PI, var19 - (float) (Math.PI / 2), 0.0F);
            case "direction_z" -> var13.rotationYXZ(var18, -var19, 0.0F);
            default -> throw new IllegalArgumentException("Unknown particle facing " + var0);
         };
      } else {
         return switch (var0) {
            case "rotate_xyz", "rotate_y", "emitter_transform_xy" -> var13;
            case "emitter_transform_xz" -> var13.rotationX((float) (-Math.PI / 2));
            case "emitter_transform_yz" -> var13.rotationY((float) (Math.PI / 2));
            case "lookat_xyz", "lookat_y" -> {
               if (var0.equals("lookat_y")) {
                  var9 = 0.0;
               }

               if (var7 == 0.0 && var11 == 0.0) {
                  if (var9 == 0.0) {
                     var11 = 1.0;
                  } else {
                     var11 = 1.0E-4;
                  }
               }

               yield var13.rotationYXZ((float)Math.atan2(var7, var11), -((float)Math.atan2(var9, Math.hypot(var7, var11))), 0.0F);
            }
            case "lookat_direction" -> {
               Vector3f var16 = new Vector3f((float)var1, (float)var3, (float)var5);
               if (var16.lengthSquared() > 0.0F) {
                  var13.rotationTo(new Vector3f(1.0F, 0.0F, 0.0F), var16.normalize());
               }

               Vector3f var17 = new Quaternionf(var13).conjugate().transform(new Vector3f((float)var7, (float)var9, (float)var11));
               yield var13.rotateX((float)Math.atan2(-var17.y, var17.z));
            }
            default -> throw new IllegalArgumentException("Unknown particle facing " + var0);
         };
      }
   }
}
