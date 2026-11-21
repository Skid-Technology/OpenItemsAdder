package dev.lone.itemsadder.nms;

import dev.lone.itemsadder.entity.ModelPartEntity;
import dev.lone.itemsadder.entity.ModelPartTypeSpoofer;
import java.util.function.DoubleConsumer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EntitySize;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.EnumMainHand;
import net.minecraft.world.entity.ai.attributes.AttributeMapBase;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R4.CraftServer;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.jetbrains.annotations.Nullable;

final class ModelPartEntityV1_21_R4 extends EntityLiving implements ModelPartEntity {
   private final EntityLiving aXs;
   private final boolean aXt;
   private final float aXu;
   private final DoubleConsumer aXv;
   private CraftLivingEntity aXw;
   private float xS = 0.01F;
   private float ut = 0.01F;
   private boolean broken;
   private boolean aWJ;

   ModelPartEntityV1_21_R4(LivingEntity var1, @Nullable Float var2, float var3, DoubleConsumer var4) {
      super(((CraftLivingEntity)var1).getHandle().an(), ((CraftLivingEntity)var1).getHandle().dV());
      this.aXs = ((CraftLivingEntity)var1).getHandle();
      this.aXt = var2 != null;
      this.aXu = var3;
      this.aXv = var4;
      if (var2 != null) {
         this.g(GenericAttributes.s).a(var2.floatValue());
         this.d(var2);
      }

      this.f(true);
      this.ad = true;
      this.e(true);
      this.k(true);
      this.OA().setPersistent(false);
   }

   public CraftLivingEntity OA() {
      if (this.aXw == null) {
         this.aXw = (CraftLivingEntity)ModelPartTypeSpoofer.spoof(new LivingEquipmentV1_21_R4(this, (CraftServer)Bukkit.getServer(), this));
      }

      return this.aXw;
   }

   @Override
   public LivingEntity getLivingEntity() {
      return this.OA();
   }

   @Override
   public boolean isBroken() {
      return ((WorldServer)this.dV()).b(this);
   }

   @Override
   public void remove() {
      this.aq();
   }

   @Override
   public boolean isForwardingDamage() {
      return this.aWJ;
   }

   public boolean method3353(EntityPlayer player) {
      return false;
   }

   public void refreshEntityData(EntityPlayer player) {
   }

   public boolean dT() {
      return false;
   }

   public EnumMainHand fy() {
      return EnumMainHand.b;
   }

   @Override
   public void a_(double x, double y, double z, float width, float height) {
      if (!this.dQ()) {
         if (this.xS != width || this.ut != height) {
            this.xS = width;
            this.ut = height;
            this.h_();
         }

         if (this.dA() != x || this.dC() != y || this.dG() != z) {
            this.a_(x, y, z);
         }
      }
   }

   protected EntitySize method3354(EntityPose pose) {
      float var2 = this.el();
      return EntitySize.b(Math.max(0.01F, this.xS) / var2, Math.max(0.01F, this.ut) / var2);
   }

   public void method3355() {
      if (!this.aXs.dQ() && this.aXs.bJ()) {
         this.aw();
      } else {
         this.aq();
      }
   }

   public boolean method3356(WorldServer level, DamageSource source, float amount) {
      if (this.broken || this.aXs.dQ() || this.aXs.a(level, source)) {
         return false;
      }

      if (!this.aXt) {
         boolean var16 = this.aWJ;
         this.aWJ = true;

         try {
            return this.aXs.a(level, source, amount * this.aXu);
         } finally {
            this.aWJ = var16;
         }
      } else {
         if (!super.a(level, source, amount)) {
            return false;
         }

         if (!this.broken) {
            this.aXv.accept(this.eG());
         }

         if (this.aXu > 0.0F) {
            boolean var4 = this.aWJ;
            this.aWJ = true;

            try {
               this.aXs.a(level, source, amount * this.aXu);
            } finally {
               this.aWJ = var4;
            }
         }

         if (source.d() instanceof EntityLiving var14) {
            this.aXs.a(var14);
         }

         if (source.d() instanceof EntityHuman var15) {
            this.aXs.a(var15, 100);
         }

         return true;
      }
   }

   public void method3357(DamageSource source) {
      if (!this.broken) {
         this.broken = true;
         this.aq();
         this.aXv.accept(0.0);
      }
   }

   public float eG() {
      return this.aXs != null && !this.aXt ? this.aXs.eG() : super.eG();
   }

   public AttributeMapBase fa() {
      return this.aXs != null && !this.aXt ? this.aXs.fa() : super.fa();
   }

   public ItemStack method3358(EnumItemSlot slot) {
      return this.aXs != null && !this.aXt ? this.aXs.a(slot) : super.a(slot);
   }

   public boolean aH() {
      return this.aXs != null && !this.aXt ? this.aXs.aH() : super.aH();
   }

   public Vec3D dy() {
      return this.aXs != null && !this.aXt ? this.aXs.dy() : super.dy();
   }

   public void method3359(Vec3D movement) {
      if (this.aXs != null && !this.aXt) {
         this.aXs.i(movement);
      } else {
         super.i(movement);
      }
   }

   public boolean addEffect(MobEffect effect, @Nullable Entity source, Cause cause) {
      return this.aXs != null && !this.aXt ? this.aXs.addEffect(effect, source, cause) : super.addEffect(effect, source, cause);
   }

   public void method3360(int ticks) {
      if (this.aXs != null && !this.aXt) {
         this.aXs.g(ticks);
      } else {
         super.g(ticks);
      }
   }

   public void method3361(int ticks) {
      if (this.aXs != null && !this.aXt) {
         this.aXs.h(ticks);
      } else {
         super.h(ticks);
      }
   }
}
