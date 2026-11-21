package dev.lone.itemsadder.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;
import org.joml.Matrix4f;

final class EquipmentPoseState {
   EquipmentEntry xa;
   final DisplayEntity xb;
   final Matrix4f xc;
   final Matrix4f xd = new Matrix4f();
   final List xe = new ArrayList();

   EquipmentPoseState(HumanoidEquipmentRenderer var1, EquipmentEntry var2) {
      this.xa = var2;
      this.xc = TransformJsonCodec.computeMatrix(var2.xf, var2.xi);
      Location var3 = var1.wU.getBaseLocation().clone();
      var3.setWorld(var1.wU.rt);
      this.xb = new DisplayEntity(var3, false, var1.wU.getModel(), var1.wU);
      this.xb.method3072(List.of(), var2.xj);
   }

   void applyPose(EquipmentEntry var1, List var2, Set var3) {
      Iterator var4 = this.xe.iterator();

      while (var4.hasNext()) {
         EntityViewer var5 = (EntityViewer)var4.next();
         if (!var2.contains(var5)) {
            this.xb.despawnFor(var5);
            var3.add(var5);
            var4.remove();
         }
      }

      if (!this.xa.xg.equals(var1.xg)) {
         this.xb.method3072(this.xe, var1.xj);
      }

      this.xa = var1;

      for (EntityViewer var7 : var2) {
         if (!this.xe.contains(var7)) {
            this.xb.spawnFor(var7);
            this.xe.add(var7);
            var3.add(var7);
         }
      }
   }

   void method8629(Set var1) {
      this.xe.forEach(this.xb::despawnFor);
      var1.addAll(this.xe);
      this.xe.clear();
   }
}
