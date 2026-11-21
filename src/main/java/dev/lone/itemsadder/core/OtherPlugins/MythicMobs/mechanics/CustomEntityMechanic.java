package dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.mechanics;

import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
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

public class CustomEntityMechanic extends SkillMechanic implements ITargetedEntitySkill {
   private final String namespacedId;
   private final String playerSkin;
   private final String animationPlay;
   private final boolean animationStop;
   private final String animationIdle;
   private final String animationWalk;
   private final String animationAttack;
   private final String animationDeath;

   public CustomEntityMechanic(CustomMechanic holder, String line, MythicLineConfig mlc) {
      super(holder.getManager(), line, mlc);
      this.setAsyncSafe(false);
      this.playerSkin = mlc.getString(new String[]{"playerskin"}, null, new String[0]);
      if (this.playerSkin == null) {
         this.namespacedId = mlc.getString(new String[]{"id", "model", "entity"});
      } else {
         this.namespacedId = "_iainternal:player";
      }

      this.animationPlay = mlc.getString(new String[]{"play"});
      this.animationStop = mlc.getBoolean("stop");
      this.animationIdle = mlc.getString("idle");
      this.animationWalk = mlc.getString("walk");
      this.animationAttack = mlc.getString("attack");
      this.animationDeath = mlc.getString("death");
   }

   public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
      Entity var3 = BukkitAdapter.adapt(data.getCaster().getEntity());
      if (!(var3 instanceof LivingEntity)) {
         Log.error("Error loading MythicMobs entity: " + var3.getName() + ". You must use only living entities. For example: ZOMBIE, SKELETON...");
         return SkillResult.INVALID_TARGET;
      }

      this.setAsyncSafe(false);
      CustomEntityImpl var4;
      if (this.namespacedId != null) {
         if (!CustomEntityManager.getInstance().oN.containsKey(this.namespacedId)) {
            Log.error("Unknown custom entity id '" + this.namespacedId + "' in MythicMobs config.");
            return SkillResult.CONDITION_FAILED;
         }

         var4 = CustomEntityManager.getInstance().attachModelWithSkin(this.namespacedId, this.playerSkin, (LivingEntity)var3, true, true, false, true);
      } else {
         var4 = CustomEntityManager.getInstance().getByEntity(var3);
      }

      this.setAsyncSafe(true);
      if (var4 != null) {
         if (this.animationIdle != null) {
            var4.setIdleAnimation(var4.getModelDefinition(this.animationIdle));
         }

         if (this.animationWalk != null) {
            var4.setWalkAnimation(var4.getModelDefinition(this.animationWalk));
         }

         if (this.animationAttack != null) {
            var4.setAttackAnimation(var4.getModelDefinition(this.animationAttack));
         }

         if (this.animationDeath != null) {
            var4.setDeathAnimation(var4.getModelDefinition(this.animationDeath));
         }

         if (this.animationStop) {
            var4.getModelInstance().qN();
         } else if (this.animationPlay != null) {
            var4.playAnim(this.animationPlay, 0);
         }

         return SkillResult.SUCCESS;
      } else {
         String var5 = var3.getUniqueId() + " " + var3.getName() + " - " + var3.getLocation();
         Log.warn("Warning: MythicMobs entity " + var5 + " is not a custom entity!");
         return SkillResult.INVALID_TARGET;
      }
   }
}
