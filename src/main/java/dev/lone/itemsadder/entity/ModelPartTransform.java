package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.util.FloatVector3;
import dev.lone.itemsadder.util.Rotation3D;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.bukkit.Location;
import org.joml.Matrix4fc;

public class ModelPartTransform {
   public final Matrix4fc Da;
   public final TransformDecomposition Db;
   public ModelDefinitionData Dc;
   public final FloatVector3 Dd;
   public final FloatVector3 De;
   public final Rotation3D Df;
   public final FloatVector3 Dg;
   public final Integer Dh;

   public ModelPartTransform(ModelDefinitionData var1, FloatVector3 var2, FloatVector3 var3, FloatVector3 var4, String var5) {
      this(var1, var2, var3, var4, var5, null);
   }

   public ModelPartTransform(ModelDefinitionData var1, FloatVector3 var2, FloatVector3 var3, FloatVector3 var4, String var5, Matrix4fc var6) {
      this(var1, var2, var3, var4, var5, var6, var6 == null ? null : TransformDecomposition.decompose(var6));
   }

   public ModelPartTransform(
      ModelDefinitionData var1, FloatVector3 var2, FloatVector3 var3, FloatVector3 var4, String var5, Matrix4fc var6, TransformDecomposition var7
   ) {
      this.Da = var6;
      this.Db = var7;
      this.Dc = var1;
      this.Dd = var2;
      this.De = var3;
      this.Dg = var4;
      this.Dh = var5 == null ? null : var5.hashCode();
      double var8 = Location.normalizeYaw(var3.x);
      double var10 = Location.normalizeYaw(var3.y);
      double var12 = Location.normalizeYaw(var3.z);
      this.Df = new Rotation3D(RotationOrder.YXZ, RotationConvention.VECTOR_OPERATOR, Math.toRadians(var8), Math.toRadians(var10), Math.toRadians(var12));
   }
}
