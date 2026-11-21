package dev.lone.itemsadder.entity;

import beer.devs.nativepackets.api.NativeValue;
import dev.lone.itemsadder.network.ChannelHandlerAdapter;
import dev.lone.itemsadder.network.NativePacketsHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import it.unimi.dsi.fastutil.ints.IntList;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface PacketDisplayBridge {
   String blg = "_injector";

   default PassengerRegistry getPassengerRegistry() {
      return null;
   }

   default DisplayEntityKey allocateKey(World var1) {
      this.registerHooks();
      ItemDisplay var2 = this.ac(new Location(var1, 0.0, 0.0, 0.0));
      return new DisplayEntityKey(var2.getEntityId(), var2.getUniqueId());
   }

   default Object createItemDisplay(World var1, ItemStack var2, int var3, int var4, float var5) {
      return this.createItemDisplayTransformed(var1, var2, var3, var4, var5, 2.0F, 2.0F, 2.0F, 1.0F, 1.0F);
   }

   default Object createItemDisplayTransformed(
      World var1, ItemStack var2, int var3, int var4, float var5, float var6, float var7, float var8, float var9, float var10
   ) {
      this.registerHooks();
      ItemDisplay var11 = this.ac(new Location(var1, 0.0, 0.0, 0.0));
      applyItemTransformed(var11, var2, var3, var4, var5, var6, var7, var8, var9, var10);
      return this.getNmsHandle(var11);
   }

   default Object createDisplayWithScale(World var1, float var2) {
      return this.createItemDisplay(var1, new ItemStack(Material.AIR), 0, 0, var2);
   }

   default boolean isSupported() {
      return false;
   }

   default boolean hasNmsField(String context) {
      try {
         ItemDisplayTransform.valueOf(context);
         return true;
      } catch (IllegalArgumentException var3) {
         return false;
      }
   }

   default void setItemModel(ItemDisplay template, String context) {
      template.setItemDisplayTransform(ItemDisplayTransform.valueOf(context));
   }

   default Object getNmsHandle(ItemDisplay template) {
      throw unsupportedOperation();
   }

   default void sendPosition(Player viewer, DisplayEntityKey identity, double var6, double var7, double var8, Object metadata) {
      throw unsupportedOperation();
   }

   default void sendEntityData(Player viewer, int entityId, Object metadata) {
      throw unsupportedOperation();
   }

   default void sendMove(Player viewer, int entityId, double var9, double var10, double var11) {
      throw unsupportedOperation();
   }

   default void sendPassengers(Player viewer, int[] entityIds, int count) {
      this.registerHooks();
      Object var4 = NativePacketsHolder.PE().Pv().removeEntities(Arrays.copyOf(entityIds, count));
      NativePacketsHolder.PE().method1868(viewer, var4);
   }

   default void sendPassengerUpdate(Player viewer, List updates, int[] destroyEntityIds, int destroyCount) {
      this.registerHooks();

      for (DisplayUpdateRecord var6 : updates) {
         if (var6.blt) {
            this.sendPosition(viewer, var6.blo, var6.blp, var6.blq, var6.blr, var6.bls);
         } else {
            if (var6.blu) {
               this.sendMove(viewer, var6.blo.entityId(), var6.blp, var6.blq, var6.blr);
            }

            if (var6.bls != null) {
               this.sendEntityData(viewer, var6.blo.entityId(), var6.bls);
            }
         }
      }

      if (destroyCount > 0) {
         this.sendPassengers(viewer, destroyEntityIds, destroyCount);
      }
   }

   private static UnsupportedOperationException unsupportedOperation() {
      return new UnsupportedOperationException("ENTITY blocks require a Minecraft 1.21.4+ NMS adapter.");
   }

   private void registerHooks() {
      if (!this.isSupported()) {
         throw unsupportedOperation();
      }
   }

   static void applyItem(ItemDisplay var0, ItemStack var1, int var2, int var3, float var4) {
      applyItemTransformed(var0, var1, var2, var3, var4, 2.0F, 2.0F, 2.0F, 1.0F, 1.0F);
   }

   static void applyItemTransformed(
      ItemDisplay var0, ItemStack var1, int var2, int var3, float var4, float var5, float var6, float var7, float var8, float var9
   ) {
      var0.setItemStack(var1);
      var0.setItemDisplayTransform(ItemDisplayTransform.FIXED);
      var0.setTransformation(
         new Transformation(
            new Vector3f(),
            new Quaternionf().rotationXYZ((float)Math.toRadians(var2), (float)Math.toRadians(var3), 0.0F),
            new Vector3f(var5, var6, var7),
            new Quaternionf()
         )
      );
      var0.setDisplayWidth(var8);
      var0.setDisplayHeight(var9);
      var0.setViewRange(var4);
      var0.setShadowRadius(0.0F);
      var0.setShadowStrength(0.0F);
      var0.setInterpolationDuration(0);
      var0.setTeleportDuration(0);
      var0.setGlowing(false);
   }

   void method5226(Object var1, Object var2);

   DisplayIndexPair toDisplayIndex(NativeValue var1);

   void setField(Object var1, Object var2);

   Object getField(String var1);

   String af(Object var1);

   void playSound(String var1, float var2, float var3, SoundCategory var4, Location var5);

   void playSoundSimple(String var1, SoundCategory var2, Location var3);

   void playSoundDup2(String var1, float var2, float var3, SoundCategory var4, Location var5);

   void sendAnimation(Player var1, String var2, int var3);

   ItemDisplay spawnItemDisplay(Location var1, Consumer var2);

   default ItemDisplay ac(Location var1) {
      return this.spawnItemDisplay(var1, null);
   }

   void mountFor(Entity var1, Player var2);

   void teleportEntity(Entity var1, Location var2);

   void teleportFor(Entity var1, Player var2, Location var3);

   void setVisibilityFor(Entity var1, Player var2, boolean var3);

   void removeFor(Entity var1, Player var2);

   default void removeEntity(Entity bukkitEntity) {
      for (Player var3 : Bukkit.getOnlinePlayers()) {
         this.removeFor(bukkitEntity, var3);
      }
   }

   void sendRemove(Player var1, IntList var2);

   static Field lookupField(Object var0) {
      for (Field var4 : var0.getClass().getDeclaredFields()) {
         var4.setAccessible(true);
         if (List.class.isAssignableFrom(var4.getType())) {
            if (var4.getGenericType() instanceof ParameterizedType var6) {
               Type[] var7 = var6.getActualTypeArguments();
               if (var7.length == 1 && var7[0] == ChannelFuture.class) {
                  return var4;
               }
            }

            if (var4.get(var0) instanceof List var9 && !var9.isEmpty() && var9.get(0) instanceof ChannelFuture) {
               return var4;
            }
         }
      }

      throw new IllegalStateException("Channels field not found");
   }

   static void injectPipeline(ChannelPipeline var0, String var1) {
      if (var0.get(var1) != null) {
         var0.remove(var1);
      }
   }

   default void registerPacketHook(String var1, @NonNull Key var2, String var3, @NonNull Supplier var4) {
      try {
         String var5 = var1 + "_injector";

         for (ChannelFuture var7 : this.getDisplayPassengers()) {
            Channel var8 = var7.channel();
            Runnable var9 = () -> {
               ChannelPipeline var6 = var8.pipeline();
               injectPipeline(var6, var5);
               var6.addFirst(var5, new ChannelHandlerAdapter(this, var1, var3, var4));
            };
            if (var8.isRegistered()) {
               var8.eventLoop().execute(var9);
            } else {
               var9.run();
            }
         }
      } catch (Throwable var10) {
         throw new RuntimeException("Failed to inject channel handler: " + var1, var10);
      }
   }

   static void injectChannelHandler(Channel var0, String var1, String var2, Supplier var3) {
      ChannelPipeline var4 = var0.pipeline();
      injectPipeline(var4, var1);
      ChannelHandler var5 = (ChannelHandler)var3.get();
      if (var2 != null && var4.get(var2) != null) {
         var4.addAfter(var2, var1, var5);
      } else if (var4.get("splitter") != null) {
         var4.addBefore("splitter", var1, var5);
      } else if (var4.get("decoder") != null) {
         var4.addBefore("decoder", var1, var5);
      } else {
         var4.addFirst(var1, var5);
      }
   }

   default void unregisterHook(String var1) {
      try {
         String var2 = var1 + "_injector";

         for (ChannelFuture var4 : this.getDisplayPassengers()) {
            Channel var5 = var4.channel();
            Runnable var6 = () -> {
               injectPipeline(var5.pipeline(), var2);
               injectPipeline(var5.pipeline(), var1);
            };
            if (var5.isRegistered()) {
               var5.eventLoop().execute(var6);
            } else {
               var6.run();
            }
         }
      } catch (Throwable var7) {
         throw new RuntimeException("Failed to uninject channel handler: " + var1, var7);
      }
   }

   List getDisplayPassengers();
}
