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
import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.jetbrains.annotations.Nullable;

final class ModelPartEntityV1_20_R4 extends EntityLiving implements ModelPartEntity {
   private final EntityLiving aWD;
   private final boolean aWE;
   private final float aWF;
   private final DoubleConsumer aWG;
   private CraftLivingEntity aWH;
   private final EnumMap aWI = new EnumMap(EnumItemSlot.class);
   private float xS = 0.01F;
   private float ut = 0.01F;
   private boolean broken;
   private boolean aWJ;

   ModelPartEntityV1_20_R4(LivingEntity var1, @Nullable Float var2, float var3, DoubleConsumer var4) {
      super(((CraftLivingEntity)var1).getHandle().ak(), ((CraftLivingEntity)var1).getHandle().dP());
      this.aWD = ((CraftLivingEntity)var1).getHandle();
      this.aWE = var2 != null;
      this.aWF = var3;
      this.aWG = var4;
      if (var2 != null) {
         this.f(GenericAttributes.q).a(var2.floatValue());
         this.t(var2);
      }

      this.f(true);
      this.ag = true;
      this.e(true);
      this.k(true);
      this.Ou().setPersistent(false);
   }

   public CraftLivingEntity Ou() {
      if (this.aWH == null) {
         this.aWH = (CraftLivingEntity)ModelPartTypeSpoofer.spoof(new LivingEquipmentV1_20_R4(this, (CraftServer)Bukkit.getServer(), this));
      }

      return this.aWH;
   }

   @Override
   public LivingEntity getLivingEntity() {
      return this.Ou();
   }

   @Override
   public boolean isBroken() {
      return ((WorldServer)this.dP()).b(this);
   }

   @Override
   public void remove() {
      this.ao();
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

   public boolean dN() {
      return false;
   }

   public EnumMainHand fu() {
      return EnumMainHand.b;
   }

   @Override
   public void a_(double x, double y, double z, float width, float height) {
      if (!this.dK()) {
         if (this.xS != width || this.ut != height) {
            this.xS = width;
            this.ut = height;
            this.j_();
         }

         if (this.du() != x || this.dw() != y || this.dA() != z) {
            this.a_(x, y, z);
         }
      }
   }

   protected EntitySize method3354(EntityPose pose) {
      float var2 = this.ee();
      return EntitySize.b(Math.max(0.01F, this.xS) / var2, Math.max(0.01F, this.ut) / var2);
   }

   public void method3362() {
      if (!this.aWD.dK() && this.aWD.bD()) {
         this.au();
      } else {
         this.ao();
      }
   }

   public boolean method3363(DamageSource source, float amount) {
      if (this.broken || this.aWD.dK() || this.aWD.b(source)) {
         return false;
      }

      if (!this.aWE) {
         boolean var15 = this.aWJ;
         this.aWJ = true;

         try {
            return this.aWD.a(source, amount * this.aWF);
         } finally {
            this.aWJ = var15;
         }
      } else {
         if (!super.a(source, amount)) {
            return false;
         }

         if (!this.broken) {
            this.aWG.accept(this.eA());
         }

         if (this.aWF > 0.0F) {
            boolean var3 = this.aWJ;
            this.aWJ = true;

            try {
               this.aWD.a(source, amount * this.aWF);
            } finally {
               this.aWJ = var3;
            }
         }

         if (source.d() instanceof EntityLiving var13) {
            this.aWD.a(var13);
         }

         if (source.d() instanceof EntityHuman var14) {
            this.aWD.c(var14);
         }

         return true;
      }
   }

   public void method3357(DamageSource source) {
      if (!this.broken) {
         this.broken = true;
         this.ao();
         this.aWG.accept(0.0);
      }
   }

   public float eA() {
      return this.aWD != null && !this.aWE ? this.aWD.eA() : super.eA();
   }

   public AttributeMapBase eW() {
      return this.aWD != null && !this.aWE ? this.aWD.eW() : super.eW();
   }

   public ItemStack method3358(EnumItemSlot slot) {
      return this.aWD != null && !this.aWE ? this.aWD.a(slot) : this.aWI.getOrDefault(slot, ItemStack.l);
   }

   public Iterable eZ() {
      return this.aWD != null && !this.aWE
         ? this.aWD.eZ()
         : List.of(this.method3358(EnumItemSlot.f), this.method3358(EnumItemSlot.e), this.method3358(EnumItemSlot.d), this.method3358(EnumItemSlot.c));
   }

   public void method3364(EnumItemSlot slot, ItemStack stack) {
      if (this.aWD != null && !this.aWE) {
         this.aWD.a(slot, stack);
      } else {
         this.aWI.put(slot, stack);
      }
   }

   public boolean aE() {
      return this.aWD != null && !this.aWE ? this.aWD.aE() : super.aE();
   }

   public Vec3D ds() {
      return this.aWD != null && !this.aWE ? this.aWD.ds() : super.ds();
   }

   public void method3365(Vec3D movement) {
      if (this.aWD != null && !this.aWE) {
         this.aWD.h(movement);
      } else {
         super.h(movement);
      }
   }

   public boolean addEffect(MobEffect effect, @Nullable Entity source, Cause cause) {
      return this.aWD != null && !this.aWE ? this.aWD.addEffect(effect, source, cause) : super.addEffect(effect, source, cause);
   }

   public void method3361(int ticks) {
      if (this.aWD != null && !this.aWE) {
         this.aWD.h(ticks);
      } else {
         super.h(ticks);
      }
   }

   public void method3366(int ticks) {
      if (this.aWD != null && !this.aWE) {
         this.aWD.i(ticks);
      } else {
         super.i(ticks);
      }
   }
}
