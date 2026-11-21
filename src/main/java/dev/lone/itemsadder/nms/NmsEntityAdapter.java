package dev.lone.itemsadder.nms;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.entity.ModelPartEntity;
import dev.lone.itemsadder.entity.SpawnCategory;
import dev.lone.itemsadder.loot.NbtDropTrigger;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Supplier;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Raider;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface NmsEntityAdapter {
   Vector3f aVP = new Vector3f(1.0F, 1.0F, 1.0F);

   default ModelPartEntity method3232(LivingEntity owner, @Nullable Float health, float bodyDamage, DoubleConsumer healthChanged) {
      throw new UnsupportedOperationException("Native model parts are unavailable on this adapter");
   }

   default Object method3233(DamageSource source) {
      return source;
   }

   void method3234(Entity var1, float var2);

   float method3235(Entity var1);

   float method3236(Entity var1);

   float ab(Entity var1);

   void method3237(Entity var1, float var2);

   default boolean method3238(Entity entity, float[] delays, float[] yawDegrees, float[] heights) {
      return false;
   }

   default float ac(Entity entity) {
      return Float.NaN;
   }

   void method3239(Entity var1, boolean var2);

   static byte method3240(byte var0, boolean var1) {
      byte var2 = 32;
      return (byte)(var1 ? var0 | var2 : var0 & ~var2);
   }

   void method3241(Entity var1, float var2);

   @Deprecated
   void method3242(Entity var1, double var2, double var4, double var6);

   @Deprecated
   void method3243(Entity var1, double var2, double var4, double var6, double var8, double var10, double var12);

   default void method3244(Entity var1, BoundingBox var2) {
      this.method3243(var1, var2.getMinX(), var2.getMaxX(), var2.getMinY(), var2.getMaxY(), var2.getMinZ(), var2.getMaxZ());
   }

   void method3245(Entity var1, double var2, double var4, double var6, float var8, float var9, boolean var10);

   void method3246(Entity var1, double var2, double var4, double var6);

   void ad(Entity var1);

   void method3247(Mob var1, boolean var2);

   void method3248(Mob var1, Location var2, int var3);

   void method3249(Mob var1);

   float method3250(LivingEntity var1);

   @Nullable
   Integer method3251(Mob var1);

   boolean method3252(Mob var1);

   @Nullable
   Mob method3253(Mob var1, EntityType var2);

   @Nullable
   static Mob method3254(Mob var0, Supplier/* @Nullable */ var1) {
      EntityEquipment var2 = var0.getEquipment();
      EquipmentSlot[] var3 = EquipmentSlot.values();
      ItemStack[] var4 = new ItemStack[var3.length];

      for (int var5 = 0; var5 < var3.length; var5++) {
         ItemStack var6 = var2.getItem(var3[var5]);
         if (var6 != null && !var6.getType().isAir() && var6.getAmount() > 0) {
            var4[var5] = var6.clone();
         }
      }

      Mob var8 = (Mob)var1.get();
      if (var8 == null && !var0.isDead()) {
         for (int var9 = 0; var9 < var3.length; var9++) {
            if (var4[var9] != null) {
               ItemStack var7 = var2.getItem(var3[var9]);
               if (var7 == null || var7.getType().isAir() || var7.getAmount() <= 0) {
                  var2.setItem(var3[var9], var4[var9]);
               }
            }
         }
      }

      return var8;
   }

   void method3255(Mob var1, boolean var2);

   void method3256(Raider var1, boolean var2);

   boolean method3257(Mob var1);

   default boolean method3258(EntityType entityType) {
      return true;
   }

   void method3259(Entity var1, float var2, float var3);

   void ae(Entity var1);

   boolean af(Entity var1);

   void method3260(Player var1, int var2);

   void method3261(Player var1, int... var2);

   @Nullable
   ItemStack method3262(Projectile var1);

   void method3263(Entity var1, byte var2);

   default void ag(Entity var1) {
      switch (var1) {
         case ArmorStand var4:
            this.method3263(var4, (byte)32);
            break;
         case ItemDisplay var5:
            var5.setInterpolationDelay(0);
            var5.setInterpolationDuration(2);
            float var6 = (float)Math.toRadians(3.0);
            Transformation var7 = var5.getTransformation();
            Quaternionf var8 = new Quaternionf(var7.getLeftRotation());
            Quaternionf var9 = new Quaternionf().fromAxisAngleRad(new Vector3f(0.0F, 1.0F, 0.0F), var6);
            Quaternionf var10 = new Quaternionf().fromAxisAngleRad(new Vector3f(0.0F, 1.0F, 0.0F), -var6);
            var7.getLeftRotation().mul(var9);
            var5.setTransformation(var7);
            SchedulerUtils.runLaterSync(() -> {
               var7.getLeftRotation().mul(var10);
               var5.setTransformation(var7);
               SchedulerUtils.runLaterSync(() -> {
                  var7.getLeftRotation().set(var8);
                  var5.setTransformation(var7);
               }, 2L);
            }, 2L);
            break;
         case null:
         default:
      }
   }

   int method3264(Location var1);

   void method3265(Player var1, LivingEntity var2);

   byte method3266(Entity var1, boolean var2);

   void method3267(Entity var1, boolean var2);

   void cq(Player var1);

   void cr(Player var1);

   boolean method3268(Entity var1, NbtDropTrigger var2);

   boolean ah(Entity var1);

   float ai(Entity var1);

   float aj(Entity var1);

   static float method3269(boolean var0, float var1, int var2) {
      return var0 ? Math.clamp(var1 + 1.0F / Math.max(1, var2), 0.0F, 1.0F) : 0.0F;
   }

   default void method3270(LivingEntity var1, double var2) {
      if (Version.isAtLeast(Version.v1_21_3)) {
         this.method3272(var1, new NamespacedKey("minecraft", "movement_speed"), var2);
      } else {
         this.method3272(var1, new NamespacedKey("minecraft", "generic.movement_speed"), var2);
      }
   }

   default void method3271(LivingEntity var1, double var2) {
      NamespacedKey var4 = NamespacedKey.minecraft(Version.isAtLeast(Version.v1_21_3) ? "flying_speed" : "generic.flying_speed");
      Attribute var5 = (Attribute)Registry.ATTRIBUTE.get(var4);
      if (var5 != null && this.method3273(var1, var5) != null) {
         this.method3272(var1, var4, var2);
      }
   }

   void method3272(LivingEntity var1, NamespacedKey var2, double var3);

   @Nullable
   Double method3273(LivingEntity var1, Attribute var2);

   static void method3274(LivingEntity var0, Object var1) {
      try {
         Object var2 = var0.getClass().getMethod("getHandle").invoke(var0);
         Object var3 = var2.getClass().getMethod("getAttributes").invoke(var2);
         synchronized (var3) {
            Method var5 = null;

            for (Method var9 : var3.getClass().getMethods()) {
               if (var9.getName().equals("registerAttribute") && var9.getParameterCount() == 1 && var9.getParameterTypes()[0].isInstance(var1)) {
                  var5 = var9;
                  break;
               }
            }

            if (var5 != null) {
               var5.invoke(var3, var1);
            } else {
               Field var20 = var3.getClass().getDeclaredField("attributes");
               var20.setAccessible(true);
               Map var21 = (Map)var20.get(var3);
               if (!var21.containsKey(var1)) {
                  Class var22 = Class.forName("net.minecraft.world.entity.ai.attributes.AttributeInstance", true, var3.getClass().getClassLoader());
                  Method var23 = null;

                  for (Method var13 : var3.getClass().getDeclaredMethods()) {
                     if (var13.getName().equals("onAttributeModified")
                        && var13.getParameterCount() == 1
                        && var13.getParameterTypes()[0].isAssignableFrom(var22)) {
                        var23 = var13;
                        var23.setAccessible(true);
                        break;
                     }
                  }

                  Method var24 = var23;
                  Consumer var25 = var2x -> {
                     if (var24 != null) {
                        try {
                           var24.invoke(var3, var2x);
                        } catch (ReflectiveOperationException var4) {
                           throw new IllegalStateException(var4);
                        }
                     }
                  };
                  Constructor var26 = null;

                  for (Constructor var16 : var22.getConstructors()) {
                     if (var16.getParameterCount() == 2
                        && var16.getParameterTypes()[0].isInstance(var1)
                        && Consumer.class.isAssignableFrom(var16.getParameterTypes()[1])) {
                        var26 = var16;
                        break;
                     }
                  }

                  if (var26 == null) {
                     throw new NoSuchMethodException("AttributeInstance(Holder, Consumer)");
                  }

                  var21.put(var1, var26.newInstance(var1, var25));
               }
            }
         }
      } catch (ReflectiveOperationException var19) {
         throw new IllegalStateException("Could not register missing entity attribute", var19);
      }
   }

   @Nullable
   Location method3275(LivingEntity var1);

   boolean method3276(@UnknownNullability LivingEntity var1);

   @Nullable
   SpawnCategory method3277(EntityType var1);

   boolean method3278(LivingEntity var1, LivingEntity var2);

   @Nullable
   Entity method3279(@NotNull World var1, UUID var2);

   @Nullable
   Entity method3280(@NotNull World var1, int var2);

   default void method3281(ItemDisplay bukkitEntity, Vector3f translation, Quaternionf leftRotation) {
      throw new RuntimeException("Not implemented for the current server version (1.21.4+ only)");
   }
}
