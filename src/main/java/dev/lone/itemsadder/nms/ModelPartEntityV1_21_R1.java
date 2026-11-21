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
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.jetbrains.annotations.Nullable;

final class ModelPartEntityV1_21_R1 extends EntityLiving implements ModelPartEntity {
   private final EntityLiving aWL;
   private final boolean aWM;
   private final float aWN;
   private final DoubleConsumer aWO;
   private CraftLivingEntity aWP;
   private final EnumMap aWQ = new EnumMap(EnumItemSlot.class);
   private float xS = 0.01F;
   private float ut = 0.01F;
   private boolean broken;
   private boolean aWJ;

   ModelPartEntityV1_21_R1(LivingEntity var1, @Nullable Float var2, float var3, DoubleConsumer var4) {
      super(((CraftLivingEntity)var1).getHandle().am(), ((CraftLivingEntity)var1).getHandle().dO());
      this.aWL = ((CraftLivingEntity)var1).getHandle();
      this.aWM = var2 != null;
      this.aWN = var3;
      this.aWO = var4;
      if (var2 != null) {
         this.f(GenericAttributes.s).a(var2.floatValue());
         this.v(var2);
      }

      this.f(true);
      this.ag = true;
      this.e(true);
      this.k(true);
      this.Ov().setPersistent(false);
   }

   public CraftLivingEntity Ov() {
      if (this.aWP == null) {
         this.aWP = (CraftLivingEntity)ModelPartTypeSpoofer.spoof(new LivingEquipmentV1_21_R1(this, (CraftServer)Bukkit.getServer(), this));
      }

      return this.aWP;
   }

   @Override
   public LivingEntity getLivingEntity() {
      return this.Ov();
   }

   @Override
   public boolean isBroken() {
      return ((WorldServer)this.dO()).b(this);
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

   public boolean dM() {
      return false;
   }

   public EnumMainHand fq() {
      return EnumMainHand.b;
   }

   @Override
   public void a_(double x, double y, double z, float width, float height) {
      if (!this.dJ()) {
         if (this.xS != width || this.ut != height) {
            this.xS = width;
            this.ut = height;
            this.i_();
         }

         if (this.dt() != x || this.dv() != y || this.dz() != z) {
            this.a_(x, y, z);
         }
      }
   }

   protected EntitySize method3354(EntityPose pose) {
      float var2 = this.eb();
      return EntitySize.b(Math.max(0.01F, this.xS) / var2, Math.max(0.01F, this.ut) / var2);
   }

   public void method3362() {
      if (!this.aWL.dJ() && this.aWL.bE()) {
         this.aw();
      } else {
         this.aq();
      }
   }

   public boolean method3363(DamageSource source, float amount) {
      if (this.broken || this.aWL.dJ() || this.aWL.b(source)) {
         return false;
      }

      if (!this.aWM) {
         boolean var15 = this.aWJ;
         this.aWJ = true;

         try {
            return this.aWL.a(source, amount * this.aWN);
         } finally {
            this.aWJ = var15;
         }
      } else {
         if (!super.a(source, amount)) {
            return false;
         }

         if (!this.broken) {
            this.aWO.accept(this.ew());
         }

         if (this.aWN > 0.0F) {
            boolean var3 = this.aWJ;
            this.aWJ = true;

            try {
               this.aWL.a(source, amount * this.aWN);
            } finally {
               this.aWJ = var3;
            }
         }

         if (source.d() instanceof EntityLiving var13) {
            this.aWL.a(var13);
         }

         if (source.d() instanceof EntityHuman var14) {
            this.aWL.c(var14);
         }

         return true;
      }
   }

   public void method3357(DamageSource source) {
      if (!this.broken) {
         this.broken = true;
         this.aq();
         this.aWO.accept(0.0);
      }
   }

   public float ew() {
      return this.aWL != null && !this.aWM ? this.aWL.ew() : super.ew();
   }

   public AttributeMapBase eS() {
      return this.aWL != null && !this.aWM ? this.aWL.eS() : super.eS();
   }

   public ItemStack method3358(EnumItemSlot slot) {
      return this.aWL != null && !this.aWM ? this.aWL.a(slot) : this.aWQ.getOrDefault(slot, ItemStack.l);
   }

   public Iterable eV() {
      return this.aWL != null && !this.aWM
         ? this.aWL.eV()
         : List.of(this.method3358(EnumItemSlot.f), this.method3358(EnumItemSlot.e), this.method3358(EnumItemSlot.d), this.method3358(EnumItemSlot.c));
   }

   public void method3364(EnumItemSlot slot, ItemStack stack) {
      if (this.aWL != null && !this.aWM) {
         this.aWL.a(slot, stack);
      } else {
         this.aWQ.put(slot, stack);
      }
   }

   public boolean aF() {
      return this.aWL != null && !this.aWM ? this.aWL.aF() : super.aF();
   }

   public Vec3D dr() {
      return this.aWL != null && !this.aWM ? this.aWL.dr() : super.dr();
   }

   public void method3359(Vec3D movement) {
      if (this.aWL != null && !this.aWM) {
         this.aWL.i(movement);
      } else {
         super.i(movement);
      }
   }

   public boolean addEffect(MobEffect effect, @Nullable Entity source, Cause cause) {
      return this.aWL != null && !this.aWM ? this.aWL.addEffect(effect, source, cause) : super.addEffect(effect, source, cause);
   }

   public void method3360(int ticks) {
      if (this.aWL != null && !this.aWM) {
         this.aWL.g(ticks);
      } else {
         super.g(ticks);
      }
   }

   public void method3361(int ticks) {
      if (this.aWL != null && !this.aWM) {
         this.aWL.h(ticks);
      } else {
         super.h(ticks);
      }
   }
}
