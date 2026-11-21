package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.MobGoalSettings;
import dev.lone.itemsadder.entity.MobPersistentKeys;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Vex;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

final class MobGoalTargetFilter extends AbstractMobGoal {
   private LivingEntity aae;
   private int aaf;
   private boolean aag;
   private boolean aah;

   MobGoalTargetFilter(Mob var1, MobGoalSettings var2, NavigatorAdapter var3) {
      super(var1, var2, var3);
   }

   @Override
   public EnumSet yW() {
      return this.aan.zq().equals("periodic_effect") ? EnumSet.noneOf(GoalFlag.class) : super.yW();
   }

   @Override
   protected boolean yX() {
      this.aae = this.aam.getTarget();
      if (this.aan.zq().equals("periodic_effect")) {
         if (this.aan.getString("target").equals("self")) {
            this.aae = this.aam;
            return true;
         }

         if (this.aan.getString("target").equals("nearby")) {
            return !this.zc().isEmpty();
         }
      }

      return this.yY();
   }

   private boolean yY() {
      return MobPersistentKeys.method9613(this.aam, this.aae, this.aan.getDouble("range"))
         && (!this.aan.getBoolean("require_line_of_sight") || this.aam.hasLineOfSight(this.aae));
   }

   @Override
   protected boolean yZ() {
      return this.aan.zq().equals("periodic_effect") && !this.aan.getString("target").equals("target") || this.yY();
   }

   @Override
   protected void za() {
      this.aaf = 0;
      this.aag = false;
      this.aah = false;
      this.bL("prepare");
      if (this.yW().contains(GoalFlag.MOVE)) {
         this.aao.stop();
      }
   }

   @Override
   protected void zb() {
      int var1 = this.aan.getInt("prepare_ticks");
      if (this.aae != null && this.aae != this.aam && this.aae.getWorld() == this.aam.getWorld()) {
         this.aao.method4592(this.aae.getEyeLocation());
      }

      if (this.ms < var1) {
         if (this.yW().contains(GoalFlag.MOVE)) {
            this.aao.stop();
         }
      } else {
         int var2 = this.ms - var1;
         if (this.aan.zq().equals("charge_attack")) {
            if (var2 == 0) {
               this.bL("execute");
            }

            if (var2 < this.aan.getInt("active_ticks")) {
               if (this.aan.getString("motion").equals("velocity")) {
                  Vector var11 = this.aae.getLocation().toVector().subtract(this.aam.getLocation().toVector());
                  if (var11.lengthSquared() > 0.001) {
                     this.aam
                        .setVelocity(
                           var11.normalize().multiply(this.aan.getDouble("velocity")).add(new Vector(0.0, this.aan.getDouble("vertical_velocity"), 0.0))
                        );
                  }
               } else {
                  this.method4571(this.aae.getLocation());
               }

               if (!this.aag && this.aam.getLocation().distanceSquared(this.aae.getLocation()) <= Math.pow(this.aan.getDouble("contact_radius"), 2.0)) {
                  MobPersistentKeys.method9619(this.aam, this.aae, this.aan.getDouble("damage"), this.aan.getDouble("knockback"), List.of());
                  this.aag = true;
               }
            } else {
               this.bl(var2 - this.aan.getInt("active_ticks"));
            }
         } else if (this.aan.zq().equals("projectile_attack")) {
            if (var2 % this.aan.getInt("burst_interval") == 0 && this.aaf < this.aan.getInt("burst_count")) {
               this.zd();
               this.aaf++;
               this.bL("execute");
            }

            int var10 = (this.aan.getInt("burst_count") - 1) * this.aan.getInt("burst_interval");
            if (var2 >= var10) {
               this.bl(var2 - var10);
            }
         } else {
            if (!this.aag) {
               this.bL("execute");
               switch (this.aan.zq()) {
                  case "beam_attack":
                     if (this.aan.getBoolean("beam_particles")) {
                        this.ze();
                     }

                     MobPersistentKeys.method9619(this.aam, this.aae, this.aan.getDouble("damage"), this.aan.getDouble("knockback"), this.aan.zn());
                     break;
                  case "area_attack":
                     for (LivingEntity var13 : this.zc()) {
                        MobPersistentKeys.method9619(this.aam, var13, this.aan.getDouble("damage"), this.aan.getDouble("knockback"), this.aan.zn());
                     }
                     break;
                  case "periodic_effect":
                     for (LivingEntity var7 : this.aan.getString("target").equals("nearby") ? this.zc() : List.of(this.aae)) {
                        if (var7 != this.aam) {
                           MobPersistentKeys.method9619(this.aam, var7, this.aan.getDouble("damage"), this.aan.getDouble("knockback"), this.aan.zn());
                        } else {
                           for (PotionEffect var9 : this.aan.zn()) {
                              this.aam.addPotionEffect(var9);
                           }

                           if (this.aan.getDouble("damage") > 0.0) {
                              this.aam.damage(this.aan.getDouble("damage"));
                           }
                        }

                        MobPersistentKeys.method9618(var7, this.aan.getDouble("heal_amount"));
                     }
                     break;
                  case "summon_attack":
                     this.zf();
                     break;
                  default:
                     throw new IllegalStateException("Unknown attack");
               }

               this.aag = true;
            }

            this.bl(var2);
         }
      }
   }

   private void bl(int var1) {
      if (!this.aah) {
         this.aah = true;
         if (this.yW().contains(GoalFlag.MOVE)) {
            this.aao.stop();
         }

         this.bL("recover");
      }

      if (var1 >= this.aan.getInt("recovery_ticks")) {
         this.aap = true;
      }
   }

   private List zc() {
      return MobPersistentKeys.method9616(this.aam, this.aan.getDouble("range"), this.aan.getList("entities"))
         .stream()
         .filter(
            var1 -> MobPersistentKeys.method9613(this.aam, var1, this.aan.getDouble("range"))
               && (this.aan.getBoolean("affect_group") || !MobPersistentKeys.method9615(this.aam, var1))
               && (!this.aan.getBoolean("require_line_of_sight") || this.aam.hasLineOfSight(var1))
         )
         .toList();
   }

   private void zd() {
      Vector var1 = this.aae.getEyeLocation().toVector().subtract(this.aam.getEyeLocation().toVector());
      if (!(var1.lengthSquared() < 0.001)) {
         var1.normalize();
         Location var2 = this.aam.getEyeLocation().add(var1.clone().multiply(0.7));
         Entity var3 = this.aam.getWorld().spawnEntity(var2, EntityType.valueOf(this.aan.getString("projectile")));
         if (var3 instanceof Projectile var4 && var3.isValid()) {
            var4.setShooter(this.aam);
            var4.setVelocity(var1.multiply(this.aan.getDouble("projectile_speed")));
            var4.getPersistentDataContainer().set(MobPersistentKeys.aaM, PersistentDataType.DOUBLE, this.aan.getDouble("damage"));
            var4.getPersistentDataContainer().set(MobPersistentKeys.keyOf("projectile_knockback"), PersistentDataType.DOUBLE, this.aan.getDouble("knockback"));
            var4.getPersistentDataContainer().set(MobPersistentKeys.aaN, PersistentDataType.STRING, MobGoalEventListener.method9607(this.aan.zn()));
            if (var3 instanceof Explosive var5) {
               var5.setYield((float)this.aan.getDouble("explosion_power"));
               var5.setIsIncendiary(this.aan.getBoolean("incendiary"));
            }

            if (var3 instanceof ShulkerBullet var10) {
               var10.setTarget(this.aae);
            }

            if (var3 instanceof ThrownPotion var11) {
               ItemStack var6 = new ItemStack(var3.getType().name().contains("LINGERING") ? Material.LINGERING_POTION : Material.SPLASH_POTION);
               PotionMeta var7 = (PotionMeta)var6.getItemMeta();

               for (PotionEffect var9 : this.aan.zn()) {
                  var7.addCustomEffect(var9, true);
               }

               var6.setItemMeta(var7);
               var11.setItem(var6);
            }

            if (var3 instanceof AbstractArrow var12) {
               var12.setPickupStatus(PickupStatus.DISALLOWED);
            }
         }
      }
   }

   private void ze() {
      Location var1 = this.aam.getEyeLocation();
      Location var2 = this.aae.getEyeLocation();
      Vector var3 = var2.toVector().subtract(var1.toVector());
      int var4 = Math.min(32, Math.max(1, (int)Math.ceil(var3.length() * 2.0)));
      var3.multiply(1.0 / var4);

      for (int var5 = 0; var5 < var4; var5++) {
         var1.add(var3);
         this.aam.getWorld().spawnParticle(Particle.END_ROD, var1, 1, 0.0, 0.0, 0.0, 0.0);
      }
   }

   private void zf() {
      NamespacedKey var1 = MobPersistentKeys.keyOf("summon_slots");
      String var2 = (String)this.aam.getPersistentDataContainer().getOrDefault(var1, PersistentDataType.STRING, "");
      ArrayList var3 = new ArrayList();

      for (String var7 : var2.split("\n")) {
         String[] var8 = var7.split(":", 2);
         if (var8.length == 2) {
            try {
               if (Long.parseLong(var8[1]) > this.aam.getWorld().getFullTime()) {
                  var3.add(var7);
               }
            } catch (NumberFormatException var13) {
               throw new IllegalStateException("Invalid summon slot data");
            }
         }
      }

      if (var3.size() < this.aan.getInt("summon_limit")) {
         for (int var14 = 0; var14 < this.aan.getInt("count") && var3.size() < this.aan.getInt("summon_limit"); var14++) {
            double var15 = (Math.PI * 2) * var14 / this.aan.getInt("count");
            Location var16 = this.aam
               .getLocation()
               .add(Math.cos(var15) * this.aan.getDouble("spawn_radius"), 0.0, Math.sin(var15) * this.aan.getDouble("spawn_radius"));
            if (this.aam.getWorld().isChunkLoaded(var16.getBlockX() >> 4, var16.getBlockZ() >> 4)
               && var16.getBlock().isPassable()
               && var16.clone().add(0.0, 1.0, 0.0).getBlock().isPassable()) {
               Object var17;
               if (this.aan.getString("entity").contains(":")) {
                  CustomEntityImpl var9 = CustomEntityManager.getInstance().spawnCustomEntitySimple(this.aan.getString("entity"), var16, true);
                  var17 = var9 == null ? null : var9.getBukkitEntity();
               } else {
                  var17 = this.aam.getWorld().spawnEntity(var16, EntityType.valueOf(this.aan.getString("entity")));
               }

               if (var17 instanceof Mob var18 && var18.isValid()) {
                  long var10 = this.aam.getWorld().getFullTime() + this.aan.getInt("summon_lifetime");
                  var18.getPersistentDataContainer().set(MobPersistentKeys.aaJ, PersistentDataType.STRING, this.aam.getUniqueId().toString());
                  var18.getPersistentDataContainer().set(MobPersistentKeys.aaK, PersistentDataType.LONG, var10);
                  var18.getPersistentDataContainer()
                     .set(
                        MobPersistentKeys.aaF,
                        PersistentDataType.STRING,
                        Objects.requireNonNullElse(MobPersistentKeys.method9614(this.aam), this.aam.getUniqueId().toString())
                     );
                  var18.setPersistent(true);
                  var18.setRemoveWhenFarAway(false);
                  if (var18 instanceof Vex var12) {
                     var12.setSummoner(this.aam);
                  }

                  if (MobPersistentKeys.method9613(var18, this.aae, this.aan.getDouble("range"))) {
                     MobPersistentKeys.method9617(var18, this.aae);
                  }

                  MobGoalEventListener.method9606(var18);
                  var3.add(var18.getUniqueId() + ":" + var10);
                  this.aam.getPersistentDataContainer().set(var1, PersistentDataType.STRING, String.join("\n", var3));
               }
            }
         }
      }
   }
}
