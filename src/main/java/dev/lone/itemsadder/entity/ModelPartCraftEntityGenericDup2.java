package dev.lone.itemsadder.entity;

import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftEntityEquipment;
import org.bukkit.inventory.EntityEquipment;

class ModelPartCraftEntityGenericDup2 extends CraftLivingEntity {
   private final EntityEquipment aXY = new CraftEntityEquipment(this);

   ModelPartCraftEntityGenericDup2(ModelPartEntityGenericDup2 var1, CraftServer var2, LivingEntity var3) {
      super(var2, var3);
   }

   public EntityEquipment getEquipment() {
      return this.aXY;
   }
}
