package dev.lone.itemsadder.nms;

import beer.devs.nativepackets.api.EntityMetadataField;
import beer.devs.nativepackets.api.NativeEquipment;
import beer.devs.nativepackets.api.NativeEquipmentSlot;
import beer.devs.nativepackets.api.NativeItem;
import beer.devs.nativepackets.api.NativePacketFactory;
import dev.lone.itemsadder.entity.ModelPartEntity;
import dev.lone.itemsadder.entity.SpawnCategory;
import dev.lone.itemsadder.internal.SwitchMap41;
import dev.lone.itemsadder.loot.NbtDropTrigger;
import dev.lone.itemsadder.network.NativePacketsBridge;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.ServerCompat;
import dev.lone.itemsadder.world.BlockEffectsAdapterImplB;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.DoubleConsumer;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAgeable;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntitySize;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.IEntityAngerable;
import net.minecraft.world.entity.ai.attributes.AttributeDefaults;
import net.minecraft.world.entity.ai.attributes.AttributeProvider;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalArrowAttack;
import net.minecraft.world.entity.ai.goal.PathfinderGoalCrossbowAttack;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.world.entity.monster.EntityShulker;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.projectile.EntityTippedArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.attribute.CraftAttribute;
import org.bukkit.craftbukkit.v1_21_R1.damage.CraftDamageSource;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEnderDragon;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntityType;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftMob;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftRaider;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.EntityType;
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

public class NmsEntityAdapterImplA implements NmsEntityAdapter {
   private static final VarHandle aVY = ServerCompat.findVarHandle(EntityLiving.class, ServerCompat.isPaper() ? "jumping" : "bn", boolean.class);
   static ItemStack aVS = Items.a.w();
   Field aVT = ServerCompat.findFieldByType(Entity.class, EntitySize.class);
   static Method aVU = ServerCompat.findMethodByReturnType(EntityArrow.class, ItemStack.class, 0);
   DataWatcherObject aVV = (DataWatcherObject)ServerCompat.getFieldValueByTypeIndex(Entity.class, DataWatcherObject.class, 0);

   @Override
   public ModelPartEntity method3232(LivingEntity owner, @Nullable Float health, float bodyDamage, DoubleConsumer healthChanged) {
      return new ModelPartEntityV1_21_R1(owner, health, bodyDamage, healthChanged);
   }

   @Override
   public Object method3233(DamageSource source) {
      return ((CraftDamageSource)source).getHandle();
   }

   @Override
   public void method3234(org.bukkit.entity.Entity bukkitEntity, float yaw) {
      Entity var3 = ((CraftEntity)bukkitEntity).getHandle();
      if (var3.dE() != yaw) {
         var3.t(yaw);
         if (bukkitEntity.getType() != EntityType.ARMOR_STAND) {
            SchedulerUtils.runOnMainThread(() -> {
               for (Player var4 : BlockFaceUtils.playersWithin(bukkitEntity.getLocation(), 25)) {
                  NativePacketsHolder.PE().method1868(var4, NativePacketsHolder.PE().Pv().rotateHead(var3.an(), yaw));
               }
            });
         }
      }
   }

   @Override
   public float method3235(org.bukkit.entity.Entity entity) {
      return ((CraftEntity)entity).getHandle().dE();
   }

   @Override
   public float method3236(org.bukkit.entity.Entity entity) {
      return ((CraftEntity)entity).getHandle().ct();
   }

   @Override
   public float ab(org.bukkit.entity.Entity entity) {
      return entity instanceof LivingEntity ? ((CraftLivingEntity)entity).getHandle().aY : ((CraftEntity)entity).getHandle().dE();
   }

   @Override
   public boolean method3238(org.bukkit.entity.Entity entity, float[] delays, float[] yawDegrees, float[] heights) {
      if (entity instanceof CraftEnderDragon var5 && delays.length == yawDegrees.length && delays.length == heights.length) {
         EntityEnderDragon var6 = var5.getHandle();
         if (var6.d < 0) {
            return false;
         }

         for (int var7 = 0; var7 < delays.length; var7++) {
            int var8 = (int)Math.floor(delays[var7]);
            float var9 = delays[var7] - var8;
            double[] var10 = var6.c[var6.d - var8 & 63];
            double[] var11 = var6.c[var6.d - var8 - 1 & 63];
            yawDegrees[var7] = (float)(var10[0] + MathHelper.d(var11[0] - var10[0]) * var9);
            heights[var7] = (float)(var10[1] + (var11[1] - var10[1]) * var9);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public float ac(org.bukkit.entity.Entity entity) {
      if (entity instanceof CraftEnderDragon var2) {
         float var3 = var2.getHandle().cc % 1.0F;
         return var3 < 0.0F ? var3 + 1.0F : var3;
      } else {
         return Float.NaN;
      }
   }

   @Override
   public void method3237(org.bukkit.entity.Entity entity, float val) {
      ((CraftEntity)entity).getHandle().p(val);
   }

   @Override
   public void method3239(org.bukkit.entity.Entity entity, boolean val) {
      Entity var3 = ((CraftEntity)entity).getHandle();
      var3.k(val);
      this.method3267(entity, val);
   }

   @Override
   public void method3241(org.bukkit.entity.Entity entity, float val) {
      ((CraftLivingEntity)entity).getHandle().f(GenericAttributes.B).a(val);
   }

   @Override
   public void method3242(org.bukkit.entity.Entity entity, double x, double y, double z) {
      ((CraftEntity)entity)
         .getHandle()
         .a(
            new AxisAlignedBB(
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
      ((CraftEntity)entity).getHandle().a(new AxisAlignedBB(xmin, ymin, zmin, xmax, ymax, zmax));
   }

   @Override
   public void method3245(org.bukkit.entity.Entity bukkitEntity, double x, double y, double z, float yaw, float pitch, boolean resetCallback) {
      Entity var11 = ((CraftEntity)bukkitEntity).getHandle();
      if (resetCallback) {
         var11.a(EntityInLevelCallback.a);
      }

      var11.a(x, y, z, yaw, pitch);
      var11.o(yaw);
   }

   @Override
   public void method3246(org.bukkit.entity.Entity bukkitEntity, double x, double y, double z) {
      Entity var8 = ((CraftEntity)bukkitEntity).getHandle();
      var8.h(x, y, z);
   }

   @Override
   public void ad(org.bukkit.entity.Entity bukkitEntity) {
      Entity var2 = ((CraftEntity)bukkitEntity).getHandle();
      var2.a(EntityInLevelCallback.a);
   }

   @Override
   public void method3247(Mob bukkitEntity, boolean value) {
      ((CraftMob)bukkitEntity).getHandle().aware = value;
   }

   @Override
   public void method3248(Mob bukkitEntity, Location location, int radius) {
      ((CraftMob)bukkitEntity).getHandle().a(BlockPosition.a(location.getX(), location.getY(), location.getZ()), radius);
   }

   @Nullable
   @Override
   public Mob method3253(Mob source, EntityType targetType) {
      return source != null && targetType != null && targetType.getEntityClass() != null && Mob.class.isAssignableFrom(targetType.getEntityClass())
         ? NmsEntityAdapter.method3254(
            source,
            () -> {
               EntityInsentient var2x = ((CraftMob)source)
                  .getHandle()
                  .convertTo(CraftEntityType.bukkitToMinecraft(targetType), true, TransformReason.METAMORPHOSIS, SpawnReason.METAMORPHOSIS);
               return var2x == null ? null : (Mob)var2x.getBukkitEntity();
            }
         )
         : null;
   }

   @Nullable
   @Override
   public Integer method3251(Mob mob) {
      return ((CraftMob)mob).getHandle() instanceof EntityAgeable var3 ? var3.g() : null;
   }

   @Override
   public boolean method3252(Mob mob) {
      return ((CraftMob)mob).getHandle().o_();
   }

   @Override
   public float method3250(LivingEntity entity) {
      return ((CraftLivingEntity)entity).getHandle().ea();
   }

   @Override
   public void method3249(Mob bukkitEntity) {
      ((CraftMob)bukkitEntity).getHandle().fX();
   }

   @Override
   public boolean method3257(Mob mob) {
      return ((CraftMob)mob).getHandle().gb();
   }

   @Override
   public void method3255(Mob mob, boolean value) {
      ((CraftMob)mob).getHandle().a_(value);
   }

   @Override
   public void method3256(Raider raider, boolean value) {
      ((CraftRaider)raider).getHandle().A(value);
   }

   @Override
   public void ae(org.bukkit.entity.Entity bukkitEntity) {
      ((CraftEntity)bukkitEntity).getHandle().i_();
   }

   @Override
   public void method3259(org.bukkit.entity.Entity bukkitEntity, float width, float height) {
      if (this.aVT != null) {
         Entity var4 = ((CraftEntity)bukkitEntity).getHandle();
         ServerCompat.writeField(this.aVT, var4, new EntitySize(width, height, var4.cL(), EntityAttachments.a(width, height), true));
         var4.c(var4.dm());
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
            EntityTippedArrow var3 = var2.getHandle();
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
      var3.dO().a(var3, eventId);
   }

   @Override
   public int method3264(Location bukkitLocation) {
      WorldServer var2 = ((CraftWorld)bukkitLocation.getWorld()).getHandle();
      EntityShulker var3 = new EntityShulker(EntityTypes.aK, var2);
      var3.a_(bukkitLocation.getX(), bukkitLocation.getY(), bukkitLocation.getZ());
      var3.k(true);
      var3.f(true);
      var3.n(true);
      var3.i(Vec3D.b);
      var3.j(true);
      NativePacketsBridge var4 = NativePacketsHolder.PE();
      NativePacketFactory var5 = var4.Pv();
      Vec3D var6 = var3.dr();
      Object var7 = var5.addEntity(
         var3.an(), var3.cz(), var3.am(), var3.dn().a(), var3.dn().b(), var3.dn().c(), var3.dG(), var3.ct(), var3.ct(), 0, var6.a(), var6.b(), var6.c()
      );
      Object var8 = var5.metadata(
         var3.an(),
         new EntityMetadataField[]{
            beer.devs.nativepackets.api.EntityMetadataFields.Entity.SHARED_FLAGS, beer.devs.nativepackets.api.EntityMetadataFields.Entity.NO_GRAVITY
         },
         new Object[]{var3.ar().a(this.aVV), var3.aY()}
      );

      for (EntityPlayer var10 : var2.x()) {
         if (var10.f(var3) < 128.0F) {
            var4.method1868(var10.getBukkitEntity(), var7);
            var4.method1868(var10.getBukkitEntity(), var8);
         }
      }

      return var3.an();
   }

   @Override
   public void method3265(Player bukkitPlayer, LivingEntity entity) {
      EntityLiving var3 = ((CraftLivingEntity)entity).getHandle();
      var3.f(GenericAttributes.s).a(1.0);
      NativePacketsHolder.PE().method1868(bukkitPlayer, NativePacketsHolder.PE().Pv().updateAttributes(var3.an(), () -> var3.eS().c()));
   }

   @Override
   public byte method3266(org.bukkit.entity.Entity entity, boolean invisible) {
      Entity var3 = ((CraftEntity)entity).getHandle();
      return NmsEntityAdapter.method3240((Byte)var3.ar().a(this.aVV), invisible);
   }

   @Override
   public void method3267(org.bukkit.entity.Entity entity, boolean invisible) {
      Entity var3 = ((CraftEntity)entity).getHandle();
      Object var4 = NativePacketsHolder.PE()
         .Pv()
         .metadata(var3.an(), beer.devs.nativepackets.api.EntityMetadataFields.Entity.SHARED_FLAGS, this.method3266(entity, invisible));
      NativePacketsHolder.PD().method5226(var3, (Packet)var4);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void cq(Player player) {
      EntityPlayer var2 = ((CraftPlayer)player).getHandle();
      ArrayList var3 = new ArrayList();

      for (EnumItemSlot var7 : EnumItemSlot.values()) {
         NativeEquipmentSlot var8 = switch (SwitchMap41.aVZ[var7.ordinal()]) {
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
      EntityPlayer var2 = ((CraftPlayer)player).getHandle();
      ArrayList var3 = new ArrayList();

      for (EnumItemSlot var7 : EnumItemSlot.values()) {
         ItemStack var8 = var2.a(var7);

         NativeEquipmentSlot var9 = switch (SwitchMap41.aVZ[var7.ordinal()]) {
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
      EntityLiving var3 = ((CraftLivingEntity)bukkitEntity).getHandle();
      NBTTagCompound var4 = new NBTTagCompound();
      var3.f(var4);
      return BlockEffectsAdapterImplB.matchesNbtTrigger(var4, rule);
   }

   @Override
   public float ai(org.bukkit.entity.Entity bukkitEntity) {
      return !(bukkitEntity instanceof LivingEntity) ? 0.0F : ((CraftLivingEntity)bukkitEntity).getHandle().B(1.0F);
   }

   @Override
   public float aj(org.bukkit.entity.Entity bukkitEntity) {
      if (!(bukkitEntity instanceof LivingEntity)) {
         return 0.0F;
      }

      EntityLiving var2 = ((CraftLivingEntity)bukkitEntity).getHandle();
      if (!var2.aJ) {
         return 0.0F;
      }

      int var3 = 6;
      if (MobEffectUtil.a(var2)) {
         var3 -= 1 + MobEffectUtil.b(var2);
      } else if (var2.b(MobEffects.d)) {
         var3 += (1 + var2.c(MobEffects.d).e()) * 2;
      }

      return NmsEntityAdapter.method3269(true, (float)Math.max(0, var2.aL) / Math.max(1, var3), var3);
   }

   @Override
   public boolean ah(org.bukkit.entity.Entity bukkitEntity) {
      if (bukkitEntity instanceof Mob var2) {
         LivingEntity var3 = var2.getTarget();
         if (var3 != null && !var3.isDead()) {
            EntityInsentient var4 = ((CraftMob)bukkitEntity).getHandle();
            PathfinderGoalSelector var5 = var4.bW;
            return var5.b().stream().anyMatch(var0 -> {
               PathfinderGoal var1x = var0.k();
               return var1x instanceof PathfinderGoalMeleeAttack || var1x instanceof PathfinderGoalArrowAttack || var1x instanceof PathfinderGoalCrossbowAttack;
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

      EntityLiving var3 = ((CraftLivingEntity)bukkitEntity).getHandle();
      AttributeProvider var4 = AttributeDefaults.a(var3.am());
      if (var4 == null) {
         return null;
      }

      Holder var5 = CraftAttribute.bukkitToMinecraftHolder(attribute);
      return var4.c(var5) ? var4.b(var5) : null;
   }

   @Override
   public Location method3275(LivingEntity bukkitEntity) {
      if (!(bukkitEntity instanceof CraftMob)) {
         return null;
      } else {
         EntityInsentient var2 = ((CraftMob)bukkitEntity).getHandle();
         if (var2.I().e() == 0.0 && var2.I().f() == 0.0 && var2.I().g() == 0.0) {
            return null;
         } else {
            return var2.p() != null && var2.p().bE()
               ? new Location(
                  bukkitEntity.getWorld(),
                  var2.p().by().c,
                  var2.p().by().d,
                  var2.p().by().e,
                  bukkitEntity.getLocation().getYaw(),
                  bukkitEntity.getLocation().getPitch()
               )
               : new Location(
                  bukkitEntity.getWorld(), var2.I().e(), var2.I().f(), var2.I().g(), bukkitEntity.getLocation().getYaw(), bukkitEntity.getLocation().getPitch()
               );
         }
      }
   }

   @Override
   public boolean method3276(@UnknownNullability LivingEntity bukkitEntity) {
      if (!(bukkitEntity instanceof CraftLivingEntity)) {
         return false;
      }

      EntityLiving var2 = ((CraftLivingEntity)bukkitEntity).getHandle();
      return (boolean)aVY.get((EntityLiving)var2) || !var2.aF() && var2.dr().d < -0.08;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public SpawnCategory method3277(EntityType entityType) {
      if (entityType == null) {
         return null;
      }

      return switch (SwitchMap41.aWa[CraftEntityType.bukkitToMinecraft(entityType).f().ordinal()]) {
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
      if (((CraftLivingEntity)bukkitSeeker).getHandle() instanceof IEntityAngerable var4) {
         EntityLiving var5 = ((CraftLivingEntity)bukkitTarget).getHandle();
         return var4.a_(var5);
      } else {
         return false;
      }
   }

   @Nullable
   @Override
   public Entity method3279(@NotNull World world, UUID uuid) {
      if (world instanceof CraftWorld var3) {
         WorldServer var4 = var3.getHandle();
         Entity var5 = var4.a(uuid);
         return var5 == null ? null : var5.getBukkitEntity();
      } else {
         return null;
      }
   }

   @Nullable
   @Override
   public Entity method3280(@NotNull World world, int id) {
      if (world instanceof CraftWorld var3) {
         WorldServer var4 = var3.getHandle();
         Entity var5 = var4.a(id);
         return var5 == null ? null : var5.getBukkitEntity();
      } else {
         return null;
      }
   }

   static {
      if (aVU == null) {
         Log.error("Failed to find AbstractArrow getPickupItem method.");
      }
   }
}
