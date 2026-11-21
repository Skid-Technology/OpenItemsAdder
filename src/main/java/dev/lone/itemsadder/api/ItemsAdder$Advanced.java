package dev.lone.itemsadder.api;

import dev.lone.itemsadder.api.ItemsAdder.Advanced.InjectedItemModifier;
import dev.lone.itemsadder.api.ItemsAdder.Advanced.ModifierHandler;
import dev.lone.itemsadder.block.BlockRealizationMode;
import dev.lone.itemsadder.block.CustomBlockVariant;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.internal.SwitchMap91;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.ServerCompat;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class ItemsAdder$Advanced {
   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Deprecated
   @Nullable
   public static BlockData getBlockDataByInternalId(int id) {
      BlockRealizationMode var1 = CustomBlockVariant.getRealizationModeById(id);
      switch (SwitchMap91.$SwitchMap$dev$lone$itemsadder$Core$item$types$data$PlacedModelType[var1.ordinal()]) {
         case 1:
         case 2:
            Material var2 = CustomBlockVariant.getMaterialById(id);
            return CustomBlockVariant.method10023(id, null, var2);
         case 3:
            return CustomBlockVariant.bI(id);
         case 4:
            return CustomBlockVariant.bH(id);
         default:
            return null;
      }
   }

   public static void injectItemModifier(Plugin plugin, String namespacedId, ModifierHandler modifier) {
      if (!ServerCompat.calledFromPluginLifecycle(plugin)) {
         throw new RuntimeException("This method can be called only from onEnable or onLoad methods!");
      }

      ContentManager.aB.computeIfAbsent(namespacedId, k -> new ArrayList<>()).add(new InjectedItemModifier(plugin.getName(), null, modifier));
   }

   public static void injectItemModifier(Plugin plugin, ModifierHandler modifier) {
      if (!ServerCompat.calledFromPluginLifecycle(plugin)) {
         throw new RuntimeException("This method can be called only from onEnable or onLoad methods!");
      }

      ContentManager.aC.add(new InjectedItemModifier(plugin.getName(), null, modifier));
   }

   @Nullable
   public static String getItemModelResourceLocation(String namespacedId) {
      CustomItemDefinition var1 = ContentManager.method8084().method8151(namespacedId, false);
      return var1 == null ? null : var1.method8734(true, true);
   }
}
