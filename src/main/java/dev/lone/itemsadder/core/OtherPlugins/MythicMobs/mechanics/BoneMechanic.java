package dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.mechanics;

import dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.MythicMobsHook;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.ModelPartDisplay;
import dev.lone.itemsadder.util.Log;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.SkillMechanic;
import io.lumine.mythic.core.skills.mechanics.CustomMechanic;
import java.util.Map.Entry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class BoneMechanic extends SkillMechanic implements ITargetedEntitySkill {
   @Nullable
   private final Boolean all;
   @Nullable
   private final String boneName;
   @Nullable
   private final Boolean visible;
   @Nullable
   private final Integer color;
   @Nullable
   private final Boolean enchant;

   public BoneMechanic(CustomMechanic holder, String line, MythicLineConfig mlc) {
      super(holder.getManager(), line, mlc);
      this.setAsyncSafe(false);
      this.all = MythicMobsHook.getBoolean(mlc, new String[]{"all"});
      this.boneName = mlc.getString("name");
      this.visible = MythicMobsHook.getBoolean(mlc, new String[]{"visible", "vis"});
      this.color = MythicMobsHook.getInteger(mlc, new String[]{"color", "col"});
      this.enchant = MythicMobsHook.getBoolean(mlc, new String[]{"enchant", "ench"});
   }

   public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
      Entity var3 = BukkitAdapter.adapt(data.getCaster().getEntity());
      if (!(var3 instanceof LivingEntity)) {
         Log.error("Error loading MythicMobs entity: " + var3.getName() + ". You must use only living entities. For example: ZOMBIE, SKELETON...");
         return SkillResult.INVALID_TARGET;
      }

      CustomEntityImpl var4 = CustomEntityManager.getInstance().getByEntity(var3);
      if (var4 == null) {
         if (var3.isDead()) {
            return SkillResult.SUCCESS;
         }

         String var9 = var3.getUniqueId() + " " + var3.getName() + " - " + var3.getLocation();
         Log.warn("Warning: MythicMobs entity " + var9 + " is not a custom entity!");
         return SkillResult.INVALID_TARGET;
      } else if (this.all != null && this.all) {
         for (Entry var6 : var4.rQ.entrySet()) {
            ModelPartDisplay var7 = (ModelPartDisplay)var6.getValue();
            this.handle(var4, var7);
         }

         return SkillResult.SUCCESS;
      } else if (this.boneName == null) {
         return SkillResult.INVALID_CONFIG;
      } else {
         ModelPartDisplay var5 = var4.getModelPart(this.boneName);
         if (var5 != null) {
            this.handle(var4, var5);
            return SkillResult.SUCCESS;
         } else {
            return SkillResult.INVALID_CONFIG;
         }
      }
   }

   private void handle(CustomEntityImpl renderer, ModelPartDisplay boneRenderer) {
      if (this.visible != null) {
         boneRenderer.setEnchantedWithViewers(renderer.getViewers(), this.visible);
      }

      if (this.enchant != null) {
         boneRenderer.setEnchanted(this.enchant);
      }

      if (this.color != null) {
         boneRenderer.setColor(this.color);
      }
   }
}
