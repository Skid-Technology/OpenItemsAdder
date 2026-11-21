package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.MobGoalSettings;
import dev.lone.itemsadder.entity.MobPersistentKeys;
import dev.lone.itemsadder.entity.ModelDefinitionData;
import dev.lone.itemsadder.util.Log;
import java.util.EnumSet;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

public abstract class AbstractMobGoal implements CustomGoal {
   protected final Mob aam;
   protected final MobGoalSettings aan;
   protected final NavigatorAdapter aao;
   protected int ms;
   protected boolean aap;
   private int aaq;
   private boolean aar;
   private String aas;

   protected AbstractMobGoal(Mob var1, MobGoalSettings var2, NavigatorAdapter var3) {
      this.aam = var1;
      this.aan = var2;
      this.aao = var3;
   }

   public static CustomGoal method4568(Mob var0, MobGoalSettings var1, NavigatorAdapter var2) {
      return switch (var1.zq()) {
         case "gather_items", "transport_items" -> new MobGoalItemCollect(var0, var1, var2);
         case "seek_land", "seek_shelter", "avoid_blocks", "rest_at_block", "investigate_disturbance" -> new MobGoalBlockSearch(var0, var1, var2);
         case "charge_attack", "projectile_attack", "beam_attack", "area_attack", "summon_attack", "periodic_effect" -> new MobGoalTargetFilter(
            var0, var1, var2
         );
         case "defensive_state", "timed_despawn" -> new MobGoalTimer(var0, var1, var2);
         case "follow_group", "share_target", "orbit_target" -> new MobGoalFollowEntity(var0, var1, var2);
         case "work_block", "harvest_crop" -> new MobGoalBlockInteract(var0, var1, var2);
         default -> throw new IllegalArgumentException("Unknown behaviour " + var1.zq());
      };
   }

   @Override
   public EnumSet yW() {
      return EnumSet.of(GoalFlag.MOVE, GoalFlag.LOOK);
   }

   protected int zh() {
      return this.aam.getTicksLived();
   }

   protected abstract boolean yX();

   protected abstract void zb();

   protected void za() {
      this.bL("active");
   }

   protected void es() {
   }

   protected boolean yZ() {
      return true;
   }

   @Override
   public final boolean zi() {
      if (!this.aar && MobPersistentKeys.method9612(this.aam) && this.zh() >= this.aaq) {
         this.aaq = this.zh() + this.aan.getInt("interval");

         try {
            return this.yX();
         } catch (RuntimeException var2) {
            this.method4569(var2);
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public final boolean zj() {
      if (!this.aar && !this.aap && MobPersistentKeys.method9612(this.aam) && (this.ms < this.aan.getInt("duration") || this.aan.zq().equals("timed_despawn"))) {
         try {
            return this.yZ();
         } catch (RuntimeException var2) {
            this.method4569(var2);
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public final void start() {
      this.ms = 0;
      this.aap = false;

      try {
         this.za();
      } catch (RuntimeException var2) {
         this.method4569(var2);
      }
   }

   @Override
   public final void tick() {
      if (!this.aar && !this.aap) {
         try {
            this.zb();
            this.ms++;
         } catch (RuntimeException var2) {
            this.method4569(var2);
         }
      }
   }

   @Override
   public final void stop() {
      try {
         this.es();
         this.stopAnimation();
      } catch (RuntimeException var5) {
         this.method4569(var5);
      } finally {
         if (this.yW().contains(GoalFlag.MOVE)) {
            this.aao.stop();
         }

         this.aaq = this.zh() + this.aan.getInt("cooldown");
      }
   }

   private void method4569(RuntimeException var1) {
      if (!this.aar) {
         Log.warn("Disabled behaviour '" + this.aan.zq() + "' on " + this.aam.getUniqueId() + ": " + var1);
      }

      this.aar = true;
      this.aap = true;
   }

   protected boolean method4570(Location var1) {
      return var1.getWorld() == this.aam.getWorld()
         && var1.distanceSquared(this.aam.getLocation()) <= this.aan.getDouble("arrival_distance") * this.aan.getDouble("arrival_distance");
   }

   protected boolean method4571(Location var1) {
      this.aao.method4592(var1);
      if (this.ms % this.aan.getInt("repath_interval") != 0) {
         return true;
      } else if (!this.aao.method4591(var1, this.aan.getDouble("speed_modifier"))) {
         this.aap = true;
         return false;
      } else {
         return true;
      }
   }

   protected void bL(String var1) {
      if (this.aan.zm().get(var1) instanceof String var3) {
         CustomEntityImpl var4 = CustomEntityManager.getInstance().getByEntity(this.aam);
         if (var4 != null && var4.getModelDefinition(var3) != null && var4.getModelInstance() != null) {
            this.aas = var3;
            var4.getModelInstance().method8913(var4.getModelDefinition(var3));
         }
      }
   }

   private void stopAnimation() {
      if (this.aas != null) {
         CustomEntityImpl var1 = CustomEntityManager.getInstance().getByEntity(this.aam);
         if (var1 != null && var1.getModelInstance() != null && var1.getModelInstance().bl(this.aas)) {
            ModelDefinitionData var2 = var1.getModelDefinition(this.aas);
            if (var2 != null) {
               if (!"base".equals(var2.getLayer())) {
                  var1.getModelInstance().bo(var2.getLayer());
               } else if (var1.getIdleAnimation() != null) {
                  var1.getModelInstance().method8913(var1.getIdleAnimation());
               }

               this.aas = null;
            }
         }
      }
   }
}
