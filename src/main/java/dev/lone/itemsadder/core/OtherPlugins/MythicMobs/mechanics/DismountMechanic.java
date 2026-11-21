package dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.mechanics;

import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.ModelRiderController;
import dev.lone.itemsadder.util.Log;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.SkillMechanic;
import io.lumine.mythic.core.skills.mechanics.CustomMechanic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class DismountMechanic extends SkillMechanic implements ITargetedEntitySkill {
   private final String[] seatsNames;
   private final boolean all;

   public DismountMechanic(CustomMechanic holder, String line, MythicLineConfig mlc) {
      super(holder.getManager(), line, mlc);
      this.setAsyncSafe(false);
      String var4 = mlc.getString(new String[]{"bones"});
      if (var4.contains(",")) {
         this.seatsNames = var4.split(",");
      } else {
         this.seatsNames = new String[]{var4};
      }

      this.all = mlc.getBoolean("all");
   }

   public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
      Entity var3 = BukkitAdapter.adapt(data.getCaster().getEntity());
      if (!(var3 instanceof LivingEntity)) {
         Log.error("Error loading MythicMobs entity: " + var3.getName() + ". You must use only living entities. For example: ZOMBIE, SKELETON...");
         return SkillResult.INVALID_TARGET;
      }

      CustomEntityImpl var4 = CustomEntityManager.getInstance().getByEntity(var3);
      if (var4 != null) {
         if (this.all) {
            CustomEntityManager.getInstance().unmountAllRiders(var4);
            return SkillResult.SUCCESS;
         }

         if (this.seatsNames != null) {
            String[] var10 = this.seatsNames;
            int var6 = var10.length;
            byte var7 = 0;
            if (var7 < var6) {
               String var8 = var10[var7];
               ModelRiderController var9 = var4.getRiderFor(var8);
               if (var9 != null) {
                  if (target.getBukkitEntity() instanceof LivingEntity) {
                     CustomEntityManager.getInstance().unmountRider(var9);
                     return SkillResult.SUCCESS;
                  } else {
                     return SkillResult.INVALID_CONFIG;
                  }
               } else {
                  return SkillResult.INVALID_CONFIG;
               }
            } else {
               return SkillResult.CONDITION_FAILED;
            }
         } else {
            return SkillResult.INVALID_CONFIG;
         }
      } else {
         String var5 = var3.getUniqueId() + " " + var3.getName() + " - " + var3.getLocation();
         Log.warn("Warning: MythicMobs entity " + var5 + " is not a custom entity!");
         return SkillResult.INVALID_TARGET;
      }
   }
}
