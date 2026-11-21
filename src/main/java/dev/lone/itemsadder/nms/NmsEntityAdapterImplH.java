package dev.lone.itemsadder.nms;

import beer.devs.nativepackets.api.EntityMetadataField;
import beer.devs.nativepackets.api.NativeEquipment;
import beer.devs.nativepackets.api.NativeEquipmentSlot;
import beer.devs.nativepackets.api.NativeItem;
import beer.devs.nativepackets.api.NativePacketFactory;
import com.mojang.logging.LogUtils;
import com.mojang.math.Transformation;
import dev.lone.itemsadder.entity.ModelPartEntity;
import dev.lone.itemsadder.entity.ModelPartEntityGenericDup2;
import dev.lone.itemsadder.entity.SpawnCategory;
import dev.lone.itemsadder.internal.SwitchMap51;
import dev.lone.itemsadder.loot.NbtDropTrigger;
import dev.lone.itemsadder.network.NativePacketsBridge;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.ServerCompat;
import dev.lone.itemsadder.world.BlockEffectsAdapterImplL;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.DoubleConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter.ScopedCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R7.attribute.CraftAttribute;
import org.bukkit.craftbukkit.v1_21_R7.damage.CraftDamageSource;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftEntityType;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftItemDisplay;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftMob;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftRaider;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Raider;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityTransformEvent.TransformReason;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class NmsEntityAdapterImplH implements NmsEntityAdapter {
   static ItemStack aVS = Items.AIR.getDefaultInstance();
   Field aVT = ServerCompat.findFieldByType(Entity.class, EntityDimensions.class);
   static Method aVU = ServerCompat.findMethodByReturnType(AbstractArrow.class, ItemStack.class, 0);
   EntityDataAccessor aWu = (EntityDataAccessor)ServerCompat.getFieldValueByTypeIndex(Entity.class, EntityDataAccessor.class, 0);

   @Override
   public ModelPartEntity method3232(LivingEntity owner, @Nullable Float health, float bodyDamage, DoubleConsumer healthChanged) {
      return new ModelPartEntityGenericDup2(owner, health, bodyDamage, healthChanged);
   }

   @Override
   public Object method3233(DamageSource source) {
      return ((CraftDamageSource)source).getHandle();
   }

   @Override
   public void method3234(org.bukkit.entity.Entity bukkitEntity, float yaw) {
      Entity var3 = ((CraftEntity)bukkitEntity).getHandle();
      if (var3.getYRot() != yaw) {
         var3.setYRot(yaw);
         if (bukkitEntity.getType() != EntityType.ARMOR_STAND) {
            SchedulerUtils.runOnMainThread(() -> {
               for (Player var4 : BlockFaceUtils.playersWithin(bukkitEntity.getLocation(), 25)) {
                  NativePacketsHolder.PE().method1868(var4, NativePacketsHolder.PE().Pv().rotateHead(var3.getId(), yaw));
               }
            });
         }
      }
   }

   @Override
   public float method3235(org.bukkit.entity.Entity entity) {
      return ((CraftEntity)entity).getHandle().getYRot();
   }

   @Override
   public float method3236(org.bukkit.entity.Entity entity) {
      return ((CraftEntity)entity).getHandle().getYHeadRot();
   }

   @Override
   public float ab(org.bukkit.entity.Entity entity) {
      return entity instanceof LivingEntity ? ((CraftLivingEntity)entity).getHandle().yBodyRot : ((CraftEntity)entity).getHandle().getYRot();
   }

   @Override
   public void method3237(org.bukkit.entity.Entity entity, float val) {
      ((CraftEntity)entity).getHandle().setYBodyRot(val);
   }

   @Override
   public void method3239(org.bukkit.entity.Entity entity, boolean val) {
      Entity var3 = ((CraftEntity)entity).getHandle();
      var3.setInvisible(val);
      this.method3267(entity, val);
   }

   @Override
   public void method3241(org.bukkit.entity.Entity entity, float val) {
      ((CraftLivingEntity)entity).getHandle().getAttribute(Attributes.STEP_HEIGHT).setBaseValue(val);
   }

   @Override
   public void method3242(org.bukkit.entity.Entity entity, double x, double y, double z) {
      ((CraftEntity)entity)
         .getHandle()
         .setBoundingBox(
            new AABB(
               entity.getLocation().getX() - x / 2.0,
               entity.getLocation().getY(),
               entity.getLocation().getZ() - z / 2.0,
               entity.getLocation().getX() + x / 2.0,
               entity.getLocation().getY() + y,
               entity.getLocation().getZ() + z / 2.0
            )
         );
   }

   @Override
   public void method3243(org.bukkit.entity.Entity entity, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
      ((CraftEntity)entity).getHandle().setBoundingBox(new AABB(xmin, ymin, zmin, xmax, ymax, zmax));
   }

   @Override
   public void method3245(org.bukkit.entity.Entity bukkitEntity, double x, double y, double z, float yaw, float pitch, boolean resetCallback) {
      Entity var11 = ((CraftEntity)bukkitEntity).getHandle();
      if (resetCallback) {
         var11.setLevelCallback(EntityInLevelCallback.NULL);
      }

      var11.absSnapTo(x, y, z, yaw, pitch);
      var11.setYHeadRot(yaw);
   }

   @Override
   public void method3246(org.bukkit.entity.Entity bukkitEntity, double x, double y, double z) {
      Entity var8 = ((CraftEntity)bukkitEntity).getHandle();
      var8.absSnapTo(x, y, z);
   }

   @Override
   public void ad(org.bukkit.entity.Entity bukkitEntity) {
      Entity var2 = ((CraftEntity)bukkitEntity).getHandle();
      var2.setLevelCallback(EntityInLevelCallback.NULL);
   }

   @Override
   public void method3247(Mob bukkitEntity, boolean value) {
      ((CraftMob)bukkitEntity).getHandle().aware = value;
   }

   @Override
   public void method3248(Mob bukkitEntity, Location location, int radius) {
      ((CraftMob)bukkitEntity).getHandle().setHomeTo(BlockPos.containing(location.getX(), location.getY(), location.getZ()), radius);
   }

   @Nullable
   @Override
   public Mob method3253(Mob source, EntityType targetType) {
      return source != null && targetType != null && targetType.getEntityClass() != null && Mob.class.isAssignableFrom(targetType.getEntityClass())
         ? NmsEntityAdapter.method3254(
            source,
            () -> {
               net.minecraft.world.entity.Mob var2x = ((CraftMob)source).getHandle();
               net.minecraft.world.entity.EntityType var3 = CraftEntityType.bukkitToMinecraft(targetType);
               Entity var4 = var2x.getVehicle();
               List var5 = List.copyOf(var2x.getPassengers());
               net.minecraft.world.entity.Mob[] var6 = new net.minecraft.world.entity.Mob[1];
               net.minecraft.world.entity.Mob var7 = var2x.convertTo(
                  var3,
                  ConversionParams.single(var2x, true, true),
                  EntitySpawnReason.CONVERSION,
                  var1xx -> var6[0] = var1xx,
                  TransformReason.METAMORPHOSIS,
                  SpawnReason.METAMORPHOSIS
               );
               if (var7 == null && var6[0] != null && !var2x.isRemoved()) {
                  net.minecraft.world.entity.Mob var8 = var6[0];
                  if (var4 != null && var2x.getVehicle() == null && var8.getVehicle() == var4) {
                     var8.stopRiding();
                     var2x.startRiding(var4, true, false);
                  }

                  for (Entity var10 : var5) {
                     if (var10.getVehicle() == var8) {
                        var10.stopRiding();
                        var10.startRiding(var2x, true, false);
                     }
                  }
               }

               return var7 == null ? null : (Mob)var7.getBukkitEntity();
            }
         )
         : null;
   }

   @Nullable
   @Override
   public Integer method3251(Mob mob) {
      return ((CraftMob)mob).getHandle() instanceof AgeableMob var3 ? var3.getAge() : null;
   }

   @Override
   public boolean method3252(Mob mob) {
      return ((CraftMob)mob).getHandle().isBaby();
   }

   @Override
   public float method3250(LivingEntity entity) {
      return ((CraftLivingEntity)entity).getHandle().getAgeScale();
   }

   @Override
   public void method3249(Mob bukkitEntity) {
      ((CraftMob)bukkitEntity).getHandle().clearHome();
   }

   @Override
   public boolean method3257(Mob mob) {
      return ((CraftMob)mob).getHandle().isAggressive();
   }

   @Override
   public void method3255(Mob mob, boolean value) {
      ((CraftMob)mob).getHandle().setCanPickUpLoot(value);
   }

   @Override
   public void method3256(Raider raider, boolean value) {
      ((CraftRaider)raider).getHandle().setCanJoinRaid(value);
   }

   @Override
   public void ae(org.bukkit.entity.Entity bukkitEntity) {
      ((CraftEntity)bukkitEntity).getHandle().refreshDimensions();
   }

   @Override
   public void method3259(org.bukkit.entity.Entity bukkitEntity, float width, float height) {
      if (this.aVT != null) {
         Entity var4 = ((CraftEntity)bukkitEntity).getHandle();
         ServerCompat.writeField(this.aVT, var4, new EntityDimensions(width, height, var4.getEyeHeight(), EntityAttachments.createDefault(width, height), true));
         var4.setPos(var4.position());
      } else {
         throw new RuntimeException("Can't set entity dimensions");
      }
   }

   @Override
   public boolean af(org.bukkit.entity.Entity bukkitEntity) {
      return bukkitEntity instanceof CraftMob;
   }

   @Override
   public void method3260(Player player, int id) {
      NativePacketsHolder.PE().method1868(player, NativePacketsHolder.PE().Pv().removeEntities(new int[]{id}));
   }

   @Override
   public void method3261(Player player, int... ids) {
      NativePacketsHolder.PE().method1868(player, NativePacketsHolder.PE().Pv().removeEntities(ids));
   }

   @Override
   public org.bukkit.inventory.ItemStack method3262(Projectile bukkitProjectile) {
      if (aVU == null) {
         return null;
      }

      if (bukkitProjectile instanceof CraftArrow var2) {
         try {
            Arrow var3 = var2.getHandle();
            return CraftItemStack.asBukkitCopy((ItemStack)aVU.invoke(var3));
         } catch (IllegalAccessException | InvocationTargetException var4) {
            var4.printStackTrace();
         }
      }

      return null;
   }

   @Override
   public void method3263(org.bukkit.entity.Entity bukkitEntity, byte eventId) {
      Entity var3 = ((CraftEntity)bukkitEntity).getHandle();
      var3.level().broadcastEntityEvent(var3, eventId);
   }

   @Override
   public int method3264(Location bukkitLocation) {
      ServerLevel var2 = ((CraftWorld)bukkitLocation.getWorld()).getHandle();
      Shulker var3 = new Shulker(net.minecraft.world.entity.EntityType.SHULKER, var2);
      var3.setPos(bukkitLocation.getX(), bukkitLocation.getY(), bukkitLocation.getZ());
      var3.setInvisible(true);
      var3.setNoGravity(true);
      var3.setInvulnerable(true);
      var3.setDeltaMovement(Vec3.ZERO);
      var3.setGlowingTag(true);
      NativePacketsBridge var4 = NativePacketsHolder.PE();
      NativePacketFactory var5 = var4.Pv();
      Vec3 var6 = var3.getDeltaMovement();
      Object var7 = var5.addEntity(
         var3.getId(),
         var3.getUUID(),
         var3.getType(),
         var3.trackingPosition().x(),
         var3.trackingPosition().y(),
         var3.trackingPosition().z(),
         var3.getXRot(),
         var3.getYHeadRot(),
         var3.getYHeadRot(),
         0,
         var6.x(),
         var6.y(),
         var6.z()
      );
      Object var8 = var5.metadata(
         var3.getId(),
         new EntityMetadataField[]{
            beer.devs.nativepackets.api.EntityMetadataFields.Entity.SHARED_FLAGS, beer.devs.nativepackets.api.EntityMetadataFields.Entity.NO_GRAVITY
         },
         new Object[]{var3.getEntityData().get(this.aWu), var3.isNoGravity()}
      );

      for (ServerPlayer var10 : var2.players()) {
         if (var10.distanceTo(var3) < 128.0F) {
            var4.method1868(var10.getBukkitEntity(), var7);
            var4.method1868(var10.getBukkitEntity(), var8);
         }
      }

      return var3.getId();
   }

   @Override
   public void method3265(Player bukkitPlayer, LivingEntity entity) {
      net.minecraft.world.entity.LivingEntity var3 = ((CraftLivingEntity)entity).getHandle();
      var3.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1.0);
      NativePacketsHolder.PE()
         .method1868(bukkitPlayer, NativePacketsHolder.PE().Pv().updateAttributes(var3.getId(), () -> var3.getAttributes().getSyncableAttributes()));
   }

   @Override
   public byte method3266(org.bukkit.entity.Entity entity, boolean invisible) {
      Entity var3 = ((CraftEntity)entity).getHandle();
      return NmsEntityAdapter.method3240((Byte)var3.getEntityData().get(this.aWu), invisible);
   }

   @Override
   public void method3267(org.bukkit.entity.Entity entity, boolean invisible) {
      Entity var3 = ((CraftEntity)entity).getHandle();
      Object var4 = NativePacketsHolder.PE()
         .Pv()
         .metadata(var3.getId(), beer.devs.nativepackets.api.EntityMetadataFields.Entity.SHARED_FLAGS, this.method3266(entity, invisible));
      NativePacketsHolder.PD().method5226(var3, (Packet)var4);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void cq(Player player) {
      ServerPlayer var2 = ((CraftPlayer)player).getHandle();
      ArrayList var3 = new ArrayList();

      for (EquipmentSlot var7 : EquipmentSlot.values()) {
         NativeEquipmentSlot var8 = switch (SwitchMap51.aWx[var7.ordinal()]) {
            case 1 -> NativeEquipmentSlot.MAIN_HAND;
            case 2 -> NativeEquipmentSlot.OFF_HAND;
            default -> NativeEquipmentSlot.valueOf(var7.name());
         };
         var3.add(new NativeEquipment(var8, NativePacketsHolder.PE().Py().nativeItem(aVS)));
      }

      Object var9 = NativePacketsHolder.PE().Pv().equipment(player.getEntityId(), var3);
      NativePacketsHolder.PD().method5226(var2, (Packet)var9);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void cr(Player player) {
      ServerPlayer var2 = ((CraftPlayer)player).getHandle();
      ArrayList var3 = new ArrayList();

      for (EquipmentSlot var7 : EquipmentSlot.values()) {
         ItemStack var8 = var2.getItemBySlot(var7);

         NativeEquipmentSlot var9 = switch (SwitchMap51.aWx[var7.ordinal()]) {
            case 1 -> NativeEquipmentSlot.MAIN_HAND;
            case 2 -> NativeEquipmentSlot.OFF_HAND;
            default -> NativeEquipmentSlot.valueOf(var7.name());
         };
         NativeItem var10 = NativePacketsHolder.PE().Py().nativeItem(var8);
         var3.add(new NativeEquipment(var9, var10));
      }

      Object var11 = NativePacketsHolder.PE().Pv().equipment(player.getEntityId(), var3);
      NativePacketsHolder.PD().method5226(var2, (Packet)var11);
   }

   @Override
   public boolean method3268(org.bukkit.entity.Entity bukkitEntity, NbtDropTrigger rule) {
      net.minecraft.world.entity.LivingEntity var3 = ((CraftLivingEntity)bukkitEntity).getHandle();
      CompoundTag var4 = new CompoundTag();
      TagValueOutput var5 = TagValueOutput.createWrappingGlobal(new ScopedCollector(LogUtils.getLogger()), var4);
      var3.saveWithoutId(var5);
      return BlockEffectsAdapterImplL.matchesNbtTrigger(var4, rule);
   }

   @Override
   public float ai(org.bukkit.entity.Entity bukkitEntity) {
      return !(bukkitEntity instanceof LivingEntity) ? 0.0F : ((CraftLivingEntity)bukkitEntity).getHandle().getAttackAnim(1.0F);
   }

   @Override
   public float aj(org.bukkit.entity.Entity bukkitEntity) {
      if (!(bukkitEntity instanceof LivingEntity)) {
         return 0.0F;
      }

      net.minecraft.world.entity.LivingEntity var2 = ((CraftLivingEntity)bukkitEntity).getHandle();
      if (!var2.swinging) {
         return 0.0F;
      }

      int var3 = var2.getItemInHand(var2.swingingArm != null ? var2.swingingArm : InteractionHand.MAIN_HAND).getSwingAnimation().duration();
      if (MobEffectUtil.hasDigSpeed(var2)) {
         var3 -= 1 + MobEffectUtil.getDigSpeedAmplification(var2);
      } else if (var2.hasEffect(MobEffects.MINING_FATIGUE)) {
         var3 += (1 + var2.getEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2;
      }

      return NmsEntityAdapter.method3269(true, (float)Math.max(0, var2.swingTime) / Math.max(1, var3), var3);
   }

   @Override
   public boolean ah(org.bukkit.entity.Entity bukkitEntity) {
      if (bukkitEntity instanceof Mob var2) {
         LivingEntity var3 = var2.getTarget();
         if (var3 != null && !var3.isDead()) {
            net.minecraft.world.entity.Mob var4 = ((CraftMob)bukkitEntity).getHandle();
            GoalSelector var5 = var4.goalSelector;
            return var5.getAvailableGoals().stream().anyMatch(var0 -> {
               Goal var1x = var0.getGoal();
               return var1x instanceof MeleeAttackGoal || var1x instanceof RangedAttackGoal || var1x instanceof RangedCrossbowAttackGoal;
            });
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public void method3272(LivingEntity bukkitEntity, NamespacedKey attributeKey, double value) {
      try {
         Attribute var5 = CraftAttribute.stringToBukkit(attributeKey.toString());
         if (var5 == null && "minecraft".equals(attributeKey.getNamespace())) {
            String var6 = attributeKey.getKey();
            String var7 = var6.startsWith("generic.") ? var6.substring(8) : "generic." + var6;
            var5 = CraftAttribute.stringToBukkit("minecraft:" + var7);
         }

         if (var5 == null) {
            return;
         }

         AttributeInstance var9 = bukkitEntity.getAttribute(var5);
         if (var9 == null) {
            NmsEntityAdapter.method3274(bukkitEntity, CraftAttribute.bukkitToMinecraftHolder(var5));
            var9 = bukkitEntity.getAttribute(var5);
            if (var9 == null) {
               return;
            }
         }

         var9.setBaseValue(value);
      } catch (Exception var8) {
         Log.error("Failed to set attribute " + attributeKey + " to entity " + bukkitEntity.getType() + ". Error: " + var8.getMessage());
      }
   }

   @Nullable
   @Override
   public Double method3273(LivingEntity bukkitEntity, Attribute attribute) {
      if (attribute == null) {
         return null;
      }

      net.minecraft.world.entity.LivingEntity var3 = ((CraftLivingEntity)bukkitEntity).getHandle();
      AttributeSupplier var4 = DefaultAttributes.getSupplier(var3.getType());
      if (var4 == null) {
         return null;
      }

      Holder var5 = CraftAttribute.bukkitToMinecraftHolder(attribute);
      return var4.hasAttribute(var5) ? var4.getBaseValue(var5) : null;
   }

   @Override
   public Location method3275(LivingEntity bukkitEntity) {
      if (!(bukkitEntity instanceof CraftMob)) {
         return null;
      } else {
         net.minecraft.world.entity.Mob var2 = ((CraftMob)bukkitEntity).getHandle();
         if (var2.getLookControl().getWantedX() == 0.0 && var2.getLookControl().getWantedY() == 0.0 && var2.getLookControl().getWantedZ() == 0.0) {
            return null;
         } else {
            return var2.getTarget() != null && var2.getTarget().isAlive()
               ? new Location(
                  bukkitEntity.getWorld(),
                  var2.getTarget().getEyePosition().x,
                  var2.getTarget().getEyePosition().y,
                  var2.getTarget().getEyePosition().z,
                  bukkitEntity.getLocation().getYaw(),
                  bukkitEntity.getLocation().getPitch()
               )
               : new Location(
                  bukkitEntity.getWorld(),
                  var2.getLookControl().getWantedX(),
                  var2.getLookControl().getWantedY(),
                  var2.getLookControl().getWantedZ(),
                  bukkitEntity.getLocation().getYaw(),
                  bukkitEntity.getLocation().getPitch()
               );
         }
      }
   }

   @Override
   public boolean method3276(@UnknownNullability LivingEntity bukkitEntity) {
      if (!(bukkitEntity instanceof CraftLivingEntity)) {
         return false;
      }

      net.minecraft.world.entity.LivingEntity var2 = ((CraftLivingEntity)bukkitEntity).getHandle();
      return var2.isJumping() || !var2.onGround() && var2.getDeltaMovement().y < -0.08;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public SpawnCategory method3277(EntityType entityType) {
      if (entityType == null) {
         return null;
      }

      return switch (SwitchMap51.aWy[CraftEntityType.bukkitToMinecraft(entityType).getCategory().ordinal()]) {
         case 1 -> SpawnCategory.MONSTER;
         case 2 -> SpawnCategory.CREATURE;
         case 3 -> SpawnCategory.AMBIENT;
         case 4 -> SpawnCategory.AXOLOTLS;
         case 5 -> SpawnCategory.UNDERGROUND_WATER_CREATURE;
         case 6 -> SpawnCategory.WATER_CREATURE;
         case 7 -> SpawnCategory.WATER_AMBIENT;
         default -> SpawnCategory.MISC;
      };
   }

   @Override
   public boolean method3278(LivingEntity bukkitSeeker, LivingEntity bukkitTarget) {
      if (((CraftLivingEntity)bukkitSeeker).getHandle() instanceof NeutralMob var4) {
         net.minecraft.world.entity.LivingEntity var5 = ((CraftLivingEntity)bukkitTarget).getHandle();
         return var4.isAngryAt(var5, var5.level().getMinecraftWorld());
      } else {
         return false;
      }
   }

   @Nullable
   @Override
   public Entity method3279(@NotNull World world, UUID uuid) {
      if (world instanceof CraftWorld var3) {
         ServerLevel var4 = var3.getHandle();
         Entity var5 = var4.getEntity(uuid);
         return var5 == null ? null : var5.getBukkitEntity();
      } else {
         return null;
      }
   }

   @Nullable
   @Override
   public Entity method3280(@NotNull World world, int id) {
      if (world instanceof CraftWorld var3) {
         ServerLevel var4 = var3.getHandle();
         Entity var5 = var4.getEntity(id);
         return var5 == null ? null : var5.getBukkitEntity();
      } else {
         return null;
      }
   }

   @Override
   public void method3281(ItemDisplay bukkitEntity, Vector3f translation, Quaternionf leftRotation) {
      net.minecraft.world.entity.Display.ItemDisplay var4 = ((CraftItemDisplay)bukkitEntity).getHandle();
      var4.setTransformation(new Transformation(translation, leftRotation, aVP, null));
   }

   static {
      if (aVU == null) {
         Log.error("Failed to find AbstractArrow getPickupItem method.");
      }
   }
}
