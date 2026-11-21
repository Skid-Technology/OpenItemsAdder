package dev.lone.itemsadder.util;

import dev.lone.itemsadder.world.BlockEffectsService;
import org.bukkit.Location;

public class LocationSinkImplA implements LocationSink {
   @Override
   public void placeLight(Location var1, int var2) {
      BlockEffectsService.getAdapter().setLightBlocks(var1.getBlock(), var2);
   }

   @Override
   public void removeLight(Location var1) {
      BlockEffectsService.getAdapter().clearLightBlocks(var1.getBlock());
   }
}
