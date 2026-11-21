package dev.lone.itemsadder.entity;

import java.util.function.DoubleConsumer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftLivingEntity;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.jetbrains.annotations.Nullable;

public final class ModelPartEntityGeneric extends LivingEntity implements ModelPartEntity {
   private final LivingEntity aXO;
   private final boolean aXP;
   private final float aXQ;
   private final DoubleConsumer aXR;
   private CraftLivingEntity aXS;
   private float xS = 0.01F;
   private float ut = 0.01F;
   private boolean broken;
   private boolean aWJ;

   ModelPartEntityGeneric(org.bukkit.entity.LivingEntity var1, @Nullable Float var2, float var3, DoubleConsumer var4) {
      super(((CraftLivingEntity)var1).getHandle().getType(), ((CraftLivingEntity)var1).getHandle().level());
      this.aXO = ((CraftLivingEntity)var1).getHandle();
      this.aXP = var2 != null;
      this.aXQ = var3;
      this.aXR = var4;
      if (var2 != null) {
         this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(var2.floatValue());
         this.setHealth(var2);
      }

      this.setNoGravity(true);
      this.noPhysics = true;
      this.setSilent(true);
      this.setInvisible(true);
      this.getBukkitEntity().setPersistent(false);
   }

   public CraftLivingEntity getBukkitEntity() {
      if (this.aXS == null) {
         this.aXS = (CraftLivingEntity)ModelPartTypeSpoofer.spoof(new ModelPartCraftEntityGeneric(this, (CraftServer)Bukkit.getServer(), this));
      }

      return this.aXS;
   }

   @Override
   public org.bukkit.entity.LivingEntity getLivingEntity() {
      return this.getBukkitEntity();
   }

   @Override
   public boolean isBroken() {
      return ((ServerLevel)this.level()).addFreshEntity(this);
   }

   @Override
   public void remove() {
      this.discard();
   }

   @Override
   public boolean isForwardingDamage() {
      return this.aWJ;
   }

   public boolean broadcastToPlayer(ServerPlayer player) {
      return false;
   }

   public void refreshEntityData(ServerPlayer player) {
   }

   public boolean shouldBeSaved() {
      return false;
   }

   public HumanoidArm getMainArm() {
      return HumanoidArm.RIGHT;
   }

   @Override
   public void a_(double x, double y, double z, float width, float height) {
      if (!this.isRemoved()) {
         if (this.xS != width || this.ut != height) {
            this.xS = width;
            this.ut = height;
            this.refreshDimensions();
         }

         if (this.getX() != x || this.getY() != y || this.getZ() != z) {
            this.setPos(x, y, z);
         }
      }
   }

   protected EntityDimensions getDefaultDimensions(Pose pose) {
      float var2 = this.getScale();
      return EntityDimensions.scalable(Math.max(0.01F, this.xS) / var2, Math.max(0.01F, this.ut) / var2);
   }

   public void tick() {
      if (!this.aXO.isRemoved() && this.aXO.isAlive()) {
         this.baseTick();
      } else {
         this.discard();
      }
   }

   public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
      if (this.broken || this.aXO.isRemoved() || this.aXO.isInvulnerableTo(level, source)) {
         return false;
      }

      if (!this.aXP) {
         boolean var16 = this.aWJ;
         this.aWJ = true;

         try {
            return this.aXO.hurtServer(level, source, amount * this.aXQ);
         } finally {
            this.aWJ = var16;
         }
      } else {
         if (!super.hurtServer(level, source, amount)) {
            return false;
         }

         if (!this.broken) {
            this.aXR.accept(this.getHealth());
         }

         if (this.aXQ > 0.0F) {
            boolean var4 = this.aWJ;
            this.aWJ = true;

            try {
               this.aXO.hurtServer(level, source, amount * this.aXQ);
            } finally {
               this.aWJ = var4;
            }
         }

         if (source.getEntity() instanceof LivingEntity var14) {
            this.aXO.setLastHurtByMob(var14);
         }

         if (source.getEntity() instanceof Player var15) {
            this.aXO.setLastHurtByPlayer(var15, 100);
         }

         return true;
      }
   }

   public void die(DamageSource source) {
      if (!this.broken) {
         this.broken = true;
         this.discard();
         this.aXR.accept(0.0);
      }
   }

   public float getHealth() {
      return this.aXO != null && !this.aXP ? this.aXO.getHealth() : super.getHealth();
   }

   public AttributeMap getAttributes() {
      return this.aXO != null && !this.aXP ? this.aXO.getAttributes() : super.getAttributes();
   }

   public ItemStack getItemBySlot(EquipmentSlot slot) {
      return this.aXO != null && !this.aXP ? this.aXO.getItemBySlot(slot) : super.getItemBySlot(slot);
   }

   public boolean onGround() {
      return this.aXO != null && !this.aXP ? this.aXO.onGround() : super.onGround();
   }

   public Vec3 getDeltaMovement() {
      return this.aXO != null && !this.aXP ? this.aXO.getDeltaMovement() : super.getDeltaMovement();
   }

   public void setDeltaMovement(Vec3 movement) {
      if (this.aXO != null && !this.aXP) {
         this.aXO.setDeltaMovement(movement);
      } else {
         super.setDeltaMovement(movement);
      }
   }

   public boolean addEffect(MobEffectInstance effect, @Nullable Entity source, Cause cause) {
      return this.aXO != null && !this.aXP ? this.aXO.addEffect(effect, source, cause) : super.addEffect(effect, source, cause);
   }

   public void igniteForTicks(int ticks) {
      if (this.aXO != null && !this.aXP) {
         this.aXO.igniteForTicks(ticks);
      } else {
         super.igniteForTicks(ticks);
      }
   }

   public void setRemainingFireTicks(int ticks) {
      if (this.aXO != null && !this.aXP) {
         this.aXO.setRemainingFireTicks(ticks);
      } else {
         super.setRemainingFireTicks(ticks);
      }
   }
}
