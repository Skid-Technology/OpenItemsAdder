package dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.mechanics;

import dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.MythicMobsHook;
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
import org.jetbrains.annotations.Nullable;

public class MountMechanic extends SkillMechanic implements ITargetedEntitySkill {
   private final String seatName;
   @Nullable
   private final Boolean locked;
   @Nullable
   private final Boolean control;

   public MountMechanic(CustomMechanic holder, String line, MythicLineConfig mlc) {
      super(holder.getManager(), line, mlc);
      this.setAsyncSafe(false);
      this.seatName = mlc.getString(new String[]{"bone"});
      this.locked = MythicMobsHook.getBoolean(mlc, new String[]{"locked", "lock"});
      this.control = MythicMobsHook.getBoolean(mlc, new String[]{"control"});
   }

   public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
      Entity var3 = BukkitAdapter.adapt(data.getCaster().getEntity());
      if (!(var3 instanceof LivingEntity)) {
         Log.error("Error loading MythicMobs entity: " + var3.getName() + ". You must use only living entities. For example: ZOMBIE, SKELETON...");
         return SkillResult.INVALID_TARGET;
      }

      CustomEntityImpl var4 = CustomEntityManager.getInstance().getByEntity(var3);
      if (var4 != null) {
         if (this.seatName != null) {
            ModelRiderController var6 = var4.getRiderFor(this.seatName);
            if (var6 != null) {
               if (target.getBukkitEntity() instanceof LivingEntity) {
                  if (this.locked != null) {
                     var6.setLocked(this.locked);
                  }

                  CustomEntityManager.getInstance().mountRider((LivingEntity)target.getBukkitEntity(), var6);
                  if (this.control != null) {
                     var6.setCanControl(this.control);
                  }

                  return SkillResult.SUCCESS;
               } else {
                  return SkillResult.INVALID_CONFIG;
               }
            } else {
               return SkillResult.INVALID_CONFIG;
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
