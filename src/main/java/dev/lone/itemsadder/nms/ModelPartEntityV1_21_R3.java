package dev.lone.itemsadder.nms;

import dev.lone.itemsadder.entity.ModelPartEntity;
import dev.lone.itemsadder.entity.ModelPartTypeSpoofer;
import java.util.EnumMap;
import java.util.List;
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
import org.bukkit.craftbukkit.v1_21_R3.CraftServer;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.jetbrains.annotations.Nullable;

final class ModelPartEntityV1_21_R3 extends EntityLiving implements ModelPartEntity {
   private final EntityLiving aXl;
   private final boolean aXm;
   private final float aXn;
   private final DoubleConsumer aXo;
   private CraftLivingEntity aXp;
   private final EnumMap aXq = new EnumMap(EnumItemSlot.class);
   private float xS = 0.01F;
   private float ut = 0.01F;
   private boolean broken;
   private boolean aWJ;

   ModelPartEntityV1_21_R3(LivingEntity var1, @Nullable Float var2, float var3, DoubleConsumer var4) {
      super(((CraftLivingEntity)var1).getHandle().aq(), ((CraftLivingEntity)var1).getHandle().dV());
      this.aXl = ((CraftLivingEntity)var1).getHandle();
      this.aXm = var2 != null;
      this.aXn = var3;
      this.aXo = var4;
      if (var2 != null) {
         this.g(GenericAttributes.s).a(var2.floatValue());
         this.x(var2);
      }

      this.f(true);
      this.ad = true;
      this.e(true);
      this.k(true);
      this.Oz().setPersistent(false);
   }

   public CraftLivingEntity Oz() {
      if (this.aXp == null) {
         this.aXp = (CraftLivingEntity)ModelPartTypeSpoofer.spoof(new LivingEquipmentV1_21_R3(this, (CraftServer)Bukkit.getServer(), this));
      }

      return this.aXp;
   }

   @Override
   public LivingEntity getLivingEntity() {
      return this.Oz();
   }

   @Override
   public boolean isBroken() {
      return ((WorldServer)this.dV()).b(this);
   }

   @Override
   public void remove() {
      this.at();
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
            this.m_();
         }

         if (this.dA() != x || this.dC() != y || this.dG() != z) {
            this.a_(x, y, z);
         }
      }
   }

   protected EntitySize method3354(EntityPose pose) {
      float var2 = this.ej();
      return EntitySize.b(Math.max(0.01F, this.xS) / var2, Math.max(0.01F, this.ut) / var2);
   }

   public void method3369() {
      if (!this.aXl.dQ() && this.aXl.bL()) {
         this.az();
      } else {
         this.at();
      }
   }

   public boolean method3356(WorldServer level, DamageSource source, float amount) {
      if (this.broken || this.aXl.dQ() || this.aXl.a(level, source)) {
         return false;
      }

      if (!this.aXm) {
         boolean var16 = this.aWJ;
         this.aWJ = true;

         try {
            return this.aXl.a(level, source, amount * this.aXn);
         } finally {
            this.aWJ = var16;
         }
      } else {
         if (!super.a(level, source, amount)) {
            return false;
         }

         if (!this.broken) {
            this.aXo.accept(this.eD());
         }

         if (this.aXn > 0.0F) {
            boolean var4 = this.aWJ;
            this.aWJ = true;

            try {
               this.aXl.a(level, source, amount * this.aXn);
            } finally {
               this.aWJ = var4;
            }
         }

         if (source.d() instanceof EntityLiving var14) {
            this.aXl.a(var14);
         }

         if (source.d() instanceof EntityHuman var15) {
            this.aXl.c(var15);
         }

         return true;
      }
   }

   public void method3357(DamageSource source) {
      if (!this.broken) {
         this.broken = true;
         this.at();
         this.aXo.accept(0.0);
      }
   }

   public float eD() {
      return this.aXl != null && !this.aXm ? this.aXl.eD() : super.eD();
   }

   public AttributeMapBase eY() {
      return this.aXl != null && !this.aXm ? this.aXl.eY() : super.eY();
   }

   public ItemStack method3358(EnumItemSlot slot) {
      return this.aXl != null && !this.aXm ? this.aXl.a(slot) : this.aXq.getOrDefault(slot, ItemStack.j);
   }

   public Iterable fb() {
      return this.aXl != null && !this.aXm
         ? this.aXl.fb()
         : List.of(this.method3358(EnumItemSlot.f), this.method3358(EnumItemSlot.e), this.method3358(EnumItemSlot.d), this.method3358(EnumItemSlot.c));
   }

   public void method3364(EnumItemSlot slot, ItemStack stack) {
      if (this.aXl != null && !this.aXm) {
         this.aXl.a(slot, stack);
      } else {
         this.aXq.put(slot, stack);
      }
   }

   public boolean aJ() {
      return this.aXl != null && !this.aXm ? this.aXl.aJ() : super.aJ();
   }

   public Vec3D dy() {
      return this.aXl != null && !this.aXm ? this.aXl.dy() : super.dy();
   }

   public void method3359(Vec3D movement) {
      if (this.aXl != null && !this.aXm) {
         this.aXl.i(movement);
      } else {
         super.i(movement);
      }
   }

   public boolean addEffect(MobEffect effect, @Nullable Entity source, Cause cause) {
      return this.aXl != null && !this.aXm ? this.aXl.addEffect(effect, source, cause) : super.addEffect(effect, source, cause);
   }

   public void method3360(int ticks) {
      if (this.aXl != null && !this.aXm) {
         this.aXl.g(ticks);
      } else {
         super.g(ticks);
      }
   }

   public void method3361(int ticks) {
      if (this.aXl != null && !this.aXm) {
         this.aXl.h(ticks);
      } else {
         super.h(ticks);
      }
   }
}
