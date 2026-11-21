package dev.lone.itemsadder.nms;

import net.minecraft.world.entity.EntityLiving;
import org.bukkit.craftbukkit.v1_21_R7.CraftServer;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftEntityEquipment;
import org.bukkit.inventory.EntityEquipment;

class LivingEquipmentV1_21_R7 extends CraftLivingEntity {
   private final EntityEquipment aXd = new CraftEntityEquipment(this);

   LivingEquipmentV1_21_R7(ModelPartEntityV1_21_R7 var1, CraftServer var2, EntityLiving var3) {
      super(var2, var3);
   }

   public EntityEquipment getEquipment() {
      return this.aXd;
   }
}
