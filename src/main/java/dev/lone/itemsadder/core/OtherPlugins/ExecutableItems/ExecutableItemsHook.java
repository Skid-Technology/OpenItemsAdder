package dev.lone.itemsadder.Core.OtherPlugins.ExecutableItems;

import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import com.ssomar.score.api.executableitems.config.ExecutableItemInterface;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;

public class ExecutableItemsHook {
   public static ItemStack getExecutableItem(String id) {
      ItemStack var1 = null;
      Optional var2 = ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(id);
      if (var2.isPresent()) {
         var1 = ((ExecutableItemInterface)var2.get()).buildItem(1, Optional.empty(), Optional.empty());
      }

      return var1;
   }
}
