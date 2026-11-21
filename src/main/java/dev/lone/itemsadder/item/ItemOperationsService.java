package dev.lone.itemsadder.item;

import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.nms.NmsRegistry;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class ItemOperationsService {
   private final ItemRegistryBridge aZb = (ItemRegistryBridge)NmsRegistry.method2777(ItemRegistryBridge.class);
   public static ItemOperationsService aZc;

   ItemOperationsService() {
   }

   private static ItemOperationsService getInstance() {
      if (aZc == null) {
         aZc = new ItemOperationsService();
      }

      return aZc;
   }

   public static ItemRegistryBridge getRegistryBridge() {
      return getInstance().aZb;
   }

   public static void giveItemToPlayer(Player var0, ItemStack var1, int var2) {
      giveItemToPlayerInternal(var0, var1, var2, getRegistryBridge().cx(var0));
   }

   public static void giveItemToPlayerInternal(Player var0, ItemStack var1, int var2, int var3) {
      PlayerInteractionFacade.giveItem(var0, var1, var2, var3);
   }

   public static Material parseMaterial(String var0) {
      try {
         return Material.getMaterial(var0.toUpperCase(Locale.ROOT));
      } catch (Throwable var2) {
         return null;
      }
   }
}
