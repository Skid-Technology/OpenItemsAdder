package dev.lone.itemsadder.entity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class EntityAnchor {
   @Nullable
   public LivingEntity xo;
   @Nullable
   Location field111;

   public EntityAnchor(LivingEntity var1) {
      this.xo = var1;
   }

   public EntityAnchor(Location var1) {
      this.field111 = var1;
   }

   public Location resolveLocation() {
      return this.xo == null ? this.field111 : this.xo.getLocation();
   }

   public void setLocation(Location var1) {
      if (this.xo != null) {
         this.xo.teleport(var1);
      } else {
         this.field111 = var1;
      }
   }

   public World getWorld() {
      return this.resolveLocation().getWorld();
   }

   public boolean isAttached() {
      return this.xo != null;
   }
}
