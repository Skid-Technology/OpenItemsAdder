package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemEventHandler;
import dev.lone.itemsadder.item.ItemEventType;
import dev.lone.itemsadder.item.ItemHandlerBinding;
import dev.lone.itemsadder.util.IntTableConstantsN;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class FishingEffectListener implements IaListener {
   public FishingEffectListener(Plugin var1, ContentManager var2) {
      var2.aK.method933(new ItemHandlerBinding(PlayerFishEvent.class, this::onFish));
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @ItemEventHandler
   void onFish(PlayerFishEvent var1, CustomItemDefinition var2, ItemStack var3) {
      ItemEventType var4 = null;
      switch (IntTableConstantsN.SWITCH_MAP[var1.getState().ordinal()]) {
         case 1:
            var4 = ItemEventType.FISHING_START;
            break;
         case 2:
         case 3:
            var4 = ItemEventType.FISHING_CAUGHT;
            break;
         case 4:
            var4 = ItemEventType.FISHING_FAILED;
            break;
         case 5:
            var4 = ItemEventType.FISHING_CANCEL;
            break;
         case 6:
            var4 = ItemEventType.FISHING_BITE;
            break;
         case 7:
            var4 = ItemEventType.FISHING_IN_GROUND;
      }

      if (var4 != null) {
         var2.method8761(var1.getPlayer(), var4, var3x -> var3x.applyOnFish(var1, var2, var3));
      }
   }
}
