package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.EntityEquipmentChangeEvent;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemEventHandler;
import dev.lone.itemsadder.item.ItemEventType;
import dev.lone.itemsadder.item.ItemHandlerBinding;
import dev.lone.itemsadder.util.EventRegistrar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class ProjectileEffectListener implements IaListener {
   private final Plugin alZ;
   private final ContentManager ama;

   public ProjectileEffectListener(Plugin var1, ContentManager var2) {
      this.alZ = var1;
      this.ama = var2;
      EventRegistrar.registerIfAbsent(this, var1);
      var2.aK.method933(new ItemHandlerBinding(ProjectileLaunchEvent.class, this::onProjectileLaunch));
      var2.aK.method933(new ItemHandlerBinding(EntityEquipmentChangeEvent.class, this::onEquipmentChange));
   }

   @ItemEventHandler
   public void onProjectileLaunch(ProjectileLaunchEvent var1, CustomItemDefinition var2, ItemStack var3) {
      ItemEventType var4 = var1.getEntity() instanceof Trident && var2.aog.hasEffects(ItemEventType.TRIDENT_THROW)
         ? ItemEventType.TRIDENT_THROW
         : ItemEventType.ITEM_THROW;
      var2.method8761((LivingEntity)var1.getEntity().getShooter(), var4, var3x -> var3x.applyOnProjectileLaunch(var1, var2, var3));
      var1.getEntity().setMetadata("IAProjectileNamespacedID", new FixedMetadataValue(this.alZ, var2.getNamespacedID()));
      var1.getEntity().setMetadata("IAProjectileItemStack", new FixedMetadataValue(this.alZ, var3));
   }

   @ItemEventHandler
   public void onEquipmentChange(EntityEquipmentChangeEvent var1, CustomItemDefinition var2, ItemStack var3) {
      var2.method8761(var1.getEntity(), ItemEventType.ITEM_THROW, var3x -> var3x.applyOnEquipmentChange(var1, var2, var3));
      var1.Lz().setMetadata("IAProjectileNamespacedID", new FixedMetadataValue(this.alZ, var2.getNamespacedID()));
      var1.Lz().setMetadata("IAProjectileItemStack", new FixedMetadataValue(this.alZ, var3));
   }

   @EventHandler
   public void onProjectileHit(ProjectileHitEvent var1) {
      if (var1.getEntity().hasMetadata("IAProjectileNamespacedID")) {
         if (var1.getHitEntity() != null || var1.getHitBlock() != null) {
            CustomItemDefinition var2 = this.ama.method8154(((MetadataValue)var1.getEntity().getMetadata("IAProjectileNamespacedID").get(0)).asString());
            ItemStack var3 = (ItemStack)((MetadataValue)var1.getEntity().getMetadata("IAProjectileItemStack").get(0)).value();
            if (var1.getHitEntity() == null) {
               ItemEventType var4 = var1.getEntity() instanceof Trident && var2.aog.hasEffects(ItemEventType.TRIDENT_HIT_BLOCK)
                  ? ItemEventType.TRIDENT_HIT_BLOCK
                  : ItemEventType.ITEM_HIT_GROUND;
               var2.method8761((LivingEntity)var1.getEntity().getShooter(), var4, var3x -> var3x.applyOnProjectileHit(var1, var2, var3));
            } else {
               ItemEventType var5 = var1.getEntity() instanceof Trident && var2.aog.hasEffects(ItemEventType.TRIDENT_HIT_ENTITY)
                  ? ItemEventType.TRIDENT_HIT_ENTITY
                  : ItemEventType.ITEM_HIT_ENTITY;
               var2.method8761((LivingEntity)var1.getEntity().getShooter(), var5, var3x -> var3x.applyOnProjectileHit(var1, var2, var3));
            }
         }
      }
   }
}
