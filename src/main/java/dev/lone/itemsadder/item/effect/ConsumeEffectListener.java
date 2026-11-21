package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.block.CustomBlockListener;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemEventHandler;
import dev.lone.itemsadder.item.ItemEventType;
import dev.lone.itemsadder.item.ItemHandlerBinding;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.util.EnvironmentDetector;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ConsumeEffectListener implements IaListener {
   private final Plugin alK;
   private final ContentManager alL;

   public ConsumeEffectListener(Plugin var1, ContentManager var2) {
      this.alK = var1;
      this.alL = var2;
      var2.aK.method933(new ItemHandlerBinding(PlayerItemConsumeEvent.class, this::onItemConsume));
      var2.aK.method933(new ItemHandlerBinding(FoodLevelChangeEvent.class, this::onFoodLevelChange));
   }

   @ItemEventHandler
   void onItemConsume(PlayerItemConsumeEvent var1, CustomItemDefinition var2, ItemStack var3) {
      boolean var4 = var1.getItem().getType() == Material.POTION;
      if (var2.aog.getEffects(ItemEventType.DRINK).stream().anyMatch(ScriptEffect.class::isInstance)
         || !var4 && var2.aog.getEffects(ItemEventType.EAT).stream().anyMatch(ScriptEffect.class::isInstance)) {
         var1.setCancelled(true);
      }

      if (var4) {
         var2.method8761(var1.getPlayer(), ItemEventType.DRINK, var3x -> var3x.applyOnConsume(var1, var2, var3));
         if (!var1.isCancelled() && EnvironmentDetector.paperAdventure && var1.getReplacement() == null && !ItemOperationsService.getRegistryBridge().aj(var3)) {
            onItemConsumeStatic(var1);
         }
      } else {
         var2.method8761(var1.getPlayer(), ItemEventType.EAT, var3x -> var3x.applyOnConsume(var1, var2, var3));
         var2.method8761(var1.getPlayer(), ItemEventType.DRINK, var3x -> var3x.applyOnConsume(var1, var2, var3));
      }
   }

   private static void onItemConsumeStatic(PlayerItemConsumeEvent var0) {
      ItemStack var1 = var0.getItem();
      ItemStack var2 = var1.clone();
      var1.setAmount(1);
      var0.setItem(var1);
      if (var0.getPlayer().getGameMode() != GameMode.CREATIVE) {
         if (var2.getAmount() == 1) {
            var2 = new ItemStack(Material.AIR);
         } else {
            var2.setAmount(var2.getAmount() - 1);
         }
      }

      var0.setReplacement(var2);
   }

   @ItemEventHandler
   void onFoodLevelChange(FoodLevelChangeEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var3.getType().isEdible() && !ItemOperationsService.getRegistryBridge().ak(var3)) {
         var1.setCancelled(true);
         if (!(var1.getEntity() instanceof Player) || !CustomBlockListener.dn.method729((Player)var1.getEntity())) {
            var2.method8761(var1.getEntity(), ItemEventType.EAT, var3x -> var3x.applyOnFoodLevel(var1, var2, var3));
         }
      }
   }
}
