package dev.lone.itemsadder.api;

import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.FurnitureItemEntity;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.Nullable;

public class CustomFurniture extends CustomStack {
   private static final Set EMPTY_SET = new HashSet();
   Entity entity;
   private FurnitureItemEntity behaviour;

   public static Set getNamespacedIdsInRegistry() {
      Set var0 = ContentManager.method8084().method8146("furniture");
      return var0 == null ? EMPTY_SET : var0;
   }

   CustomFurniture(CustomItemDefinition customItem, Entity entity) {
      super(customItem);
      this.entity = entity;
      this.initItemStack(entity);
   }

   CustomFurniture(CustomItemDefinition furniture, ItemStack clone, Entity entity, FurnitureItemEntity behaviour) {
      super(furniture, clone);
      this.entity = entity;
      this.behaviour = behaviour;
      this.initItemStack(entity);
   }

   private void initItemStack(Entity entity) {
      this.itemStack = FurnitureItemEntity.A(entity);
   }

   @Nullable
   public static CustomFurniture byAlreadySpawned(Entity entity) {
      ItemStack var1 = FurnitureItemEntity.A(entity);
      if (!CustomItemDefinition.isCustomItem(var1)) {
         return null;
      }

      CustomItemDefinition var2 = ContentManager.method8084().method8149(var1);
      if (var2 == null) {
         return null;
      }

      FurnitureItemEntity var3 = (FurnitureItemEntity)var2.aoh.getAction("furniture");
      return var3 == null ? null : new CustomFurniture(var2, entity);
   }

   @Nullable
   public static CustomFurniture byAlreadySpawned(Block block) {
      Optional var1 = block.getLocation()
         .getWorld()
         .getNearbyEntities(block.getLocation().add(0.5, 0.5, 0.5), 0.1, 0.1, 0.1)
         .stream()
         .filter(FurnitureItemEntity::method409)
         .findFirst();
      return var1.<CustomFurniture>map(CustomFurniture::byAlreadySpawned).orElse(null);
   }

   @Nullable
   public static CustomFurniture spawnPreciseNonSolid(String namespacedId, Location location) {
      CustomItemDefinition var2 = ContentManager.method8084().method8154(namespacedId);
      if (var2 == null) {
         return null;
      }

      if (!var2.aoh.hasAction("furniture")) {
         return null;
      }

      ItemStack var3 = var2.Ds();
      FurnitureItemEntity var4 = (FurnitureItemEntity)var2.aoh.getAction("furniture");
      Entity var5 = var4.method411(var3, location);
      return new CustomFurniture(var2, var3, var5, var4);
   }

   @Nullable
   public static CustomFurniture spawn(String namespacedId, Block blockLocation) {
      CustomItemDefinition var2 = ContentManager.method8084().method8154(namespacedId);
      if (var2 == null) {
         return null;
      }

      if (!var2.aoh.hasAction("furniture")) {
         return null;
      }

      ItemStack var3 = var2.Ds();
      FurnitureItemEntity var4 = (FurnitureItemEntity)var2.aoh.getAction("furniture");
      Entity var5 = var4.method410(var3, blockLocation);
      return new CustomFurniture(var2, var3, var5, var4);
   }

   public void remove(boolean dropItem) {
      if (this.entity != null) {
         remove(this.entity, dropItem);
      }
   }

   public static void remove(Entity entity, boolean dropItem) {
      ContentManager.method8084().aV.updateAttachment(entity, dropItem);
   }

   public void teleport(Entity destinationEntity) {
      this.teleport(destinationEntity.getLocation());
   }

   public void teleport(Location location) {
      if (this.entity != null) {
         if (this.behaviour.method406()) {
            this.behaviour.method370(this.entity);
         }

         this.entity.teleport(location);
      }
   }

   public void replaceFurniture(String newFurnitureNamespacedId) {
      this.replaceFurniture(newFurnitureNamespacedId, null);
   }

   public void replaceFurniture(String newFurnitureNamespacedId, Color color) {
      CustomItemDefinition var3 = ContentManager.method8084().method8154(newFurnitureNamespacedId);
      if (var3 == null) {
         throw new NoSuchElementException("No custom item with that namespacedId: " + newFurnitureNamespacedId);
      }

      if (!var3.aoh.hasAction("furniture")) {
         throw new NoSuchElementException("Custom item has no furniture behaviour with that namespacedId: " + newFurnitureNamespacedId);
      }

      if (FurnitureItemEntity.method401(var3, this.internalItem, this.entity)) {
         this.replace(this.itemStack, var3);
         this.itemStack = var3.Ds();
         this.itemStack = FurnitureItemEntity.method416(this.itemStack);
         if (color != null) {
            this.setColor(color);
         } else if (this.entity instanceof ArmorStand) {
            ((ArmorStand)this.entity).setHelmet(this.itemStack);
         } else if (this.entity instanceof ItemFrame) {
            ((ItemFrame)this.entity).setItem(this.itemStack, false);
         } else if (this.entity instanceof ItemDisplay) {
            ((ItemDisplay)this.entity).setItemStack(this.itemStack);
         }
      }
   }

   public void setColor(Color color) {
      ItemMeta var2 = this.itemStack.getItemMeta();
      if (var2 instanceof PotionMeta) {
         ((PotionMeta)var2).setColor(color);
         this.itemStack.setItemMeta(var2);
      } else if (var2 instanceof LeatherArmorMeta) {
         ((LeatherArmorMeta)var2).setColor(color);
         this.itemStack.setItemMeta(var2);
      }

      if (this.entity instanceof ArmorStand) {
         ((ArmorStand)this.entity).setHelmet(this.itemStack);
      } else if (this.entity instanceof ItemFrame) {
         ((ItemFrame)this.entity).setItem(this.itemStack, false);
      } else if (this.entity instanceof ItemDisplay) {
         ((ItemDisplay)this.entity).setItemStack(this.itemStack);
      }
   }

   public int getOriginalLightLevel() {
      return this.behaviour.Az();
   }

   public void setCurrentLightLevel(int level) {
      BlockUtils.clearBlockState(this.entity.getLocation().getBlock());
      BlockUtils.markBlockState(this.entity.getLocation().getBlock(), level);
   }

   @Deprecated
   @Nullable
   public Entity getArmorstand() {
      return this.entity;
   }

   @Nullable
   public Entity getEntity() {
      return this.entity;
   }
}
