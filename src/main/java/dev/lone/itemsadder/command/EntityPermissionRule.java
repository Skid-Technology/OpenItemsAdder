package dev.lone.itemsadder.command;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EntityPermissionRule extends AbstractCommandPermissionRule {
   @Nullable
   public final String iZ;
   @Nullable
   public final EntityType ja;
   @Nullable
   public final Integer jb;

   public static EntityPermissionRuleBuilder ai(String var0) {
      EntityType var1 = (EntityType)BukkitRegistryLookup.enumOrNull(var0.toUpperCase(), EntityType.class);
      if (var1 != null) {
         return new EntityPermissionRuleBuilderImpl().method5021(var1);
      } else if (CustomEntityManager.getInstance().oN.containsKey(var0)) {
         return new EntityPermissionRuleBuilderImpl().aj(var0);
      } else {
         throw new IllegalArgumentException("Invalid entity id: " + var0);
      }
   }

   int cM() {
      return this.jb != null ? this.jb : Settings.get().customEntitiesMaxPerChunk;
   }

   @Override
   public PermissionRuleOutcome method4941(Event var1, CustomEntityImpl var2, Player var3) {
      return this.method4942(var1, var2);
   }

   @Override
   public PermissionRuleOutcome method4942(Event var1, CustomEntityImpl var2) {
      Location var3 = var2.getLocation();
      boolean var4 = false;
      int var5 = this.cM();
      int var6 = 0;
      if (this.ja != null) {
         for (Entity var10 : var3.getChunk().getEntities()) {
            if (var10.getType() == this.ja) {
               if (++var6 >= var5) {
                  return PermissionRuleOutcome.cy();
               }
            }
         }

         var3.getWorld().spawnEntity(var3, this.ja);
         var4 = true;
      } else {
         for (Entity var15 : var3.getChunk().getEntities()) {
            if (CustomEntityManager.getInstance().isCustomEntityLoaded(var15)) {
               if (++var6 >= var5) {
                  return PermissionRuleOutcome.cy();
               }
            }
         }

         CustomEntityImpl var12 = CustomEntityManager.getInstance().spawnCustomEntitySimple(this.iZ, var3, true);
         if (var12 != null) {
            var4 = true;
         }
      }

      return var4 ? PermissionRuleOutcome.cz() : PermissionRuleOutcome.cz();
   }

   @Generated
   protected EntityPermissionRule(EntityPermissionRuleBuilder var1) {
      super(var1);
      this.iZ = var1.iZ;
      this.ja = var1.ja;
      this.jb = var1.jb;
   }
}
