package dev.lone.itemsadder.entity;

import java.util.List;
import org.bukkit.Location;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public final class EntityPartRenderer {
   final ModelPartDisplay xp;
   final CustomEntityImpl xq;
   private final Matrix4f xr;
   private final Matrix4f xs = new Matrix4f();
   private List xt = List.of();
   volatile boolean xu;
   private boolean xv;

   EntityPartRenderer(ModelPartDisplay var1, CustomEntityImpl var2, Matrix4fc var3) {
      this.xp = var1;
      this.xq = var2;
      this.xr = new Matrix4f(var3);
   }

   void mh() {
      Location var1 = this.xp.Hm.getLocation().clone();
      var1.setYaw(0.0F);
      var1.setPitch(0.0F);
      this.xq.sl.setLocation(var1);
      List var2 = this.xp.Hm.getViewers();
      if (var2 != this.xt) {
         for (EntityViewer var4 : this.xt) {
            if (var2.stream().noneMatch(var1x -> var1x.uz.equals(var4.uz))) {
               this.xq.scheduleViewerRemoval(var4.li);
            }
         }

         for (EntityViewer var6 : var2) {
            this.xq.addViewer(var6.li, false);
         }

         this.xt = var2;
      }
   }

   boolean mi() {
      CustomEntityImpl var1 = this.xp.Hm;
      if (!var1.removed && var1.getModelPart(this.xp.Ho.getId()) == this.xp && !var1.isRagdolling()) {
         this.xp.getScaledPartMatrix(this.xs).mul(this.xr);
         this.xv = true;
         return true;
      } else {
         this.xu = true;
         this.xv = false;
         return false;
      }
   }

   void mj() {
      Location var1 = this.xp.Hm.getBaseLocation();
      this.xq.sB.setX(var1.getX());
      this.xq.sB.setY(var1.getY());
      this.xq.sB.setZ(var1.getZ());
   }

   public void setTransform(Matrix4f var1) {
      var1.mulLocal(this.xs);
   }

   boolean isVisibleTo(EntityViewer var1) {
      if (this.xv && !this.xu && !this.xp.Hm.removed && this.xp.isVisibleToViewers()) {
         EntityViewer var2 = (EntityViewer)this.xp.Hm.so.get(var1.uz);
         return var2 != null && !var2.uW && var2.kW();
      } else {
         return false;
      }
   }
}
