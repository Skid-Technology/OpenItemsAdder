package dev.lone.itemsadder.item;

import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.util.Log;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class PlacedItemInstance {
   private final CustomEntityImpl adx;
   private final CustomItemDefinition ady;
   private final PlaceableItemSpec adz;

   public PlacedItemInstance(CustomEntityImpl var1, CustomItemDefinition var2, PlaceableItemSpec var3) {
      this.adx = var1;
      this.ady = var2;
      this.adz = var3;
   }

   public void aG(Player var1) {
      RayTraceResult var2 = var1.getWorld()
         .rayTraceEntities(
            var1.getEyeLocation(),
            var1.getEyeLocation().getDirection(),
            5.0,
            0.1,
            var2x -> var2x.getEntityId() != var1.getEntityId() && var2x.getEntityId() == this.adx.getBukkitEntity().getEntityId()
         );
      Vector var3 = var2 != null ? var2.getHitPosition() : new Vector(0, 0, 0);
      Log.devLog("Position: " + var3);
      PlayerInteractAtEntityEvent var4 = new PlayerInteractAtEntityEvent(var1, this.adx.getBukkitEntity(), var3);
      this.ady.method8761(var1, ItemEventType.PLACED_FURNITURE_INTERACT, var2x -> {
         ItemStack var3x = this.ady.Ds();
         return var2x.applyOnInteractEntity(var4, this.ady, var3x);
      });
   }

   public void remove() {
      BoundingBox var1 = this.adx.getBukkitEntity().getBoundingBox();
      PlaceableItemSpec.findPlacedInBounds(this.adx.getBukkitEntity().getWorld(), var1, false).forEach(var0 -> {
         Block var1x = var0.getBlock();
         if (var1x.getType() == Material.BARRIER) {
            var1x.setType(Material.AIR);
         }

         BlockUtils.clearBlockState(var1x);
      });
   }

   public CustomEntityImpl entity() {
      return this.adx;
   }

   public CustomItemDefinition definition() {
      return this.ady;
   }

   public PlaceableItemSpec spec() {
      return this.adz;
   }
}
