package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemEventHandler;
import dev.lone.itemsadder.item.ItemEventType;
import dev.lone.itemsadder.item.ItemHandlerBinding;
import dev.lone.itemsadder.item.SimpleItemDefinition;
import dev.lone.itemsadder.util.EventRegistrar;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class BowEffectListener implements IaListener {
   public BowEffectListener(Plugin var1, ContentManager var2) {
      EventRegistrar.registerIfAbsent(this, var1);
      var2.aK.method933(new ItemHandlerBinding(EntityShootBowEvent.class, this::onShootBow));
   }

   @EventHandler(ignoreCancelled = true)
   public void onBowDamage(EntityDamageByEntityEvent var1) {
      if (!EventRegistrar.isCustomEntityDamageEvent(var1)) {
         if (var1.getCause() == DamageCause.PROJECTILE) {
            if (var1.getDamager() instanceof Arrow) {
               if (!var1.getDamager().hasMetadata("IAArrowDamage")) {
                  return;
               }

               int var2 = ((MetadataValue)var1.getDamager().getMetadata("IAArrowDamage").get(0)).asInt();
               var1.setDamage(var1.getFinalDamage() * var2 / 2.0);
            }
         }
      }
   }

   @ItemEventHandler
   void onShootBow(EntityShootBowEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var2 instanceof SimpleItemDefinition var4) {
         if (var4.anq != -1.0) {
            var1.getProjectile().setMetadata("IAArrowDamage", new FixedMetadataValue(Main.getInstance(), var4.anq));
         }

         if (var4.anp) {
            var1.getProjectile().setFireTicks(100);
         }

         var1.getProjectile().setGlowing(var4.ano);
         applyArrowForce(var1.getProjectile(), var4.anr);
         var2.method8761(var1.getEntity(), ItemEventType.BOW_SHOT, var3x -> var3x.applyOnShootBow(var1, var2, var3));
      }
   }

   static void applyArrowForce(Entity var0, double var1) {
      if (var0 instanceof AbstractArrow && var1 != 1.0 && var1 >= 0.0 && Double.isFinite(var1)) {
         var0.setVelocity(var0.getVelocity().multiply(var1));
      }
   }
}
