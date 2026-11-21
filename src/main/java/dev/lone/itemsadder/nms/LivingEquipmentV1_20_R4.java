package dev.lone.itemsadder.nms;

import net.minecraft.world.entity.EntityLiving;
import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftEntityEquipment;
import org.bukkit.inventory.EntityEquipment;

class LivingEquipmentV1_20_R4 extends CraftLivingEntity {
   private final EntityEquipment aWK = new CraftEntityEquipment(this);

   LivingEquipmentV1_20_R4(ModelPartEntityV1_20_R4 var1, CraftServer var2, EntityLiving var3) {
      super(var2, var3);
   }

   public EntityEquipment getEquipment() {
      return this.aWK;
   }
}
