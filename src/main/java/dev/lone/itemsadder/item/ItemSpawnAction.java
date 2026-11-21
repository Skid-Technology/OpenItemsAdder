package dev.lone.itemsadder.item;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.Log;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemSpawnAction extends ItemActionBase {
   public static final String adA = "entity_summoner";
   @Nullable
   public String iZ;
   @Nullable
   public EntityType ja;
   @Nullable
   public Integer jb;
   @Nullable
   public Integer adB;
   @Nullable
   public Integer adC;

   public ItemSpawnAction(CustomItemDefinition var1, String var2, @Nullable Integer var3, @Nullable Integer var4, @Nullable Integer var5) {
      super(var1);
      this.jb = var3;
      this.adB = var4;
      this.adC = var5;
      EntityType var6 = (EntityType)BukkitRegistryLookup.enumOrNull(var2.toUpperCase(), EntityType.class);
      if (var6 != null) {
         this.ja = var6;
      } else {
         if (!var2.contains(":")) {
            var2 = var1.getNamespace() + ":" + var2;
         }

         if (Settings.get().customEntitiesEnabled && CustomEntityManager.getInstance().oN.containsKey(var2)) {
            this.iZ = var2;
         }
      }

      if (this.ja == null && this.iZ == null) {
         throw new IllegalArgumentException("Invalid entity id: " + var2);
      }
   }

   public void method336(Player var1, ItemStack var2, Location var3) {
      if (this.adB != null && this.adB > 0 && !ItemStackUtils.aA(var2)) {
         Log.warn("Item '" + this.aeT.getNamespacedID() + "' has no durability. Failed to execute 'entity_summoner' behaviour. File: " + this.aeT.DJ());
      } else {
         boolean var4 = false;
         int var5 = this.jb != null ? this.jb : Settings.get().customEntitiesMaxPerChunk;
         int var6 = 0;
         if (this.ja != null) {
            for (Entity var10 : var3.getChunk().getEntities()) {
               if (var10.getType() == this.ja) {
                  if (++var6 >= var5) {
                     return;
                  }
               }
            }

            var3.getWorld().spawnEntity(var3, this.ja);
            var4 = true;
         } else {
            for (Entity var15 : var3.getChunk().getEntities()) {
               if (CustomEntityManager.getInstance().isCustomEntityLoaded(var15)) {
                  if (++var6 >= var5) {
                     return;
                  }
               }
            }

            CustomEntityImpl var12 = CustomEntityManager.getInstance().spawnCustomEntitySimple(this.iZ, var3, true);
            if (var12 != null) {
               var4 = true;
            }
         }

         if (var4) {
            if (var1.getGameMode() == GameMode.CREATIVE) {
               return;
            }

            if (this.adC != null && this.adC > 0) {
               ItemStackUtils.au(var2);
               return;
            }

            if (this.adB != null && this.adB > 0) {
               ItemStackUtils.method7239(var2, this.adB, false, true);
            }
         }
      }
   }
}
