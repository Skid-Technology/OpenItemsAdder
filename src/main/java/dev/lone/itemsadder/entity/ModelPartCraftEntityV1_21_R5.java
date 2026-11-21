package dev.lone.itemsadder.entity;

import net.minecraft.world.entity.EntityLiving;
import org.bukkit.craftbukkit.v1_21_R5.CraftServer;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R5.inventory.CraftEntityEquipment;
import org.bukkit.inventory.EntityEquipment;

class ModelPartCraftEntityV1_21_R5 extends CraftLivingEntity {
   private final EntityEquipment aXI = new CraftEntityEquipment(this);

   ModelPartCraftEntityV1_21_R5(ModelPartEntityV1_21_R5 var1, CraftServer var2, EntityLiving var3) {
      super(var2, var3);
   }

   public EntityEquipment getEquipment() {
      return this.aXI;
   }
}
