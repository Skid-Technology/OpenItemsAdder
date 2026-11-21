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
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftLivingEntity;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.jetbrains.annotations.Nullable;

public final class ModelPartEntityGenericDup3 extends LivingEntity implements ModelPartEntity {
   private final LivingEntity aXZ;
   private final boolean aYa;
   private final float aYb;
   private final DoubleConsumer aYc;
   private CraftLivingEntity aXS;
   private float xS = 0.01F;
   private float ut = 0.01F;
   private boolean broken;
   private boolean aWJ;

   ModelPartEntityGenericDup3(org.bukkit.entity.LivingEntity var1, @Nullable Float var2, float var3, DoubleConsumer var4) {
      super(((CraftLivingEntity)var1).getHandle().getType(), ((CraftLivingEntity)var1).getHandle().level());
      this.aXZ = ((CraftLivingEntity)var1).getHandle();
      this.aYa = var2 != null;
      this.aYb = var3;
      this.aYc = var4;
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
         this.aXS = (CraftLivingEntity)ModelPartTypeSpoofer.spoof(new ModelPartCraftEntityGenericDup3(this, (CraftServer)Bukkit.getServer(), this));
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
      if (!this.aXZ.isRemoved() && this.aXZ.isAlive()) {
         this.baseTick();
      } else {
         this.discard();
      }
   }

   public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
      if (this.broken || this.aXZ.isRemoved() || this.aXZ.isInvulnerableTo(level, source)) {
         return false;
      }

      if (!this.aYa) {
         boolean var16 = this.aWJ;
         this.aWJ = true;

         try {
            return this.aXZ.hurtServer(level, source, amount * this.aYb);
         } finally {
            this.aWJ = var16;
         }
      } else {
         if (!super.hurtServer(level, source, amount)) {
            return false;
         }

         if (!this.broken) {
            this.aYc.accept(this.getHealth());
         }

         if (this.aYb > 0.0F) {
            boolean var4 = this.aWJ;
            this.aWJ = true;

            try {
               this.aXZ.hurtServer(level, source, amount * this.aYb);
            } finally {
               this.aWJ = var4;
            }
         }

         if (source.getEntity() instanceof LivingEntity var14) {
            this.aXZ.setLastHurtByMob(var14);
         }

         if (source.getEntity() instanceof Player var15) {
            this.aXZ.setLastHurtByPlayer(var15, 100);
         }

         return true;
      }
   }

   public void die(DamageSource source) {
      if (!this.broken) {
         this.broken = true;
         this.discard();
         this.aYc.accept(0.0);
      }
   }

   public float getHealth() {
      return this.aXZ != null && !this.aYa ? this.aXZ.getHealth() : super.getHealth();
   }

   public AttributeMap getAttributes() {
      return this.aXZ != null && !this.aYa ? this.aXZ.getAttributes() : super.getAttributes();
   }

   public ItemStack getItemBySlot(EquipmentSlot slot) {
      return this.aXZ != null && !this.aYa ? this.aXZ.getItemBySlot(slot) : super.getItemBySlot(slot);
   }

   public boolean onGround() {
      return this.aXZ != null && !this.aYa ? this.aXZ.onGround() : super.onGround();
   }

   public Vec3 getDeltaMovement() {
      return this.aXZ != null && !this.aYa ? this.aXZ.getDeltaMovement() : super.getDeltaMovement();
   }

   public void setDeltaMovement(Vec3 movement) {
      if (this.aXZ != null && !this.aYa) {
         this.aXZ.setDeltaMovement(movement);
      } else {
         super.setDeltaMovement(movement);
      }
   }

   public void push(double x, double y, double z, @Nullable Entity pushingEntity) {
      if (pushingEntity != this.aXZ || !(this.aXZ instanceof EnderDragon)) {
         super.push(x, y, z, pushingEntity);
      }
   }

   public boolean addEffect(MobEffectInstance effect, @Nullable Entity source, Cause cause) {
      return this.aXZ != null && !this.aYa ? this.aXZ.addEffect(effect, source, cause) : super.addEffect(effect, source, cause);
   }

   public void igniteForTicks(int ticks) {
      if (this.aXZ != null && !this.aYa) {
         this.aXZ.igniteForTicks(ticks);
      } else {
         super.igniteForTicks(ticks);
      }
   }

   public void setRemainingFireTicks(int ticks) {
      if (this.aXZ != null && !this.aYa) {
         this.aXZ.setRemainingFireTicks(ticks);
      } else {
         super.setRemainingFireTicks(ticks);
      }
   }
}
