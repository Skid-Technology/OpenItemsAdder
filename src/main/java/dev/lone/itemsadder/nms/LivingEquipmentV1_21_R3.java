package dev.lone.itemsadder.nms;

import net.minecraft.world.entity.EntityLiving;
import org.bukkit.craftbukkit.v1_21_R3.CraftServer;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R3.inventory.CraftEntityEquipment;
import org.bukkit.inventory.EntityEquipment;

class LivingEquipmentV1_21_R3 extends CraftLivingEntity {
   private final EntityEquipment aXr = new CraftEntityEquipment(this);

   LivingEquipmentV1_21_R3(ModelPartEntityV1_21_R3 var1, CraftServer var2, EntityLiving var3) {
      super(var2, var3);
   }

   public EntityEquipment getEquipment() {
      return this.aXr;
   }
}
