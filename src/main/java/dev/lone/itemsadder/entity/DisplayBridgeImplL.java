package dev.lone.itemsadder.entity;

import beer.devs.nativepackets.api.NativeSoundSource;
import beer.devs.nativepackets.api.NativeValue;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.ServerCompat;
import it.unimi.dsi.fastutil.ints.IntList;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ChunkMap.TrackedEntity;
import net.minecraft.server.network.ServerConnectionListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

public class DisplayBridgeImplL implements PacketDisplayBridge {
   private static final PassengerRegistry bmI = new PassengerRegistryImplL();
   private static final boolean bmJ = ServerCompat.findFieldByType(Entity.class, TrackedEntity.class) != null;
   private static final Field bmK = ServerCompat.findFieldByType(ClientboundSoundPacket.class, Holder.class);

   @Override
   public PassengerRegistry getPassengerRegistry() {
      return bmI;
   }

   @Override
   public DisplayIndexPair toDisplayIndex(NativeValue nativeChunkPosition) {
      ChunkPos var2 = (ChunkPos)nativeChunkPosition.nativeHandle();
      return new DisplayIndexPair(var2.x(), var2.z());
   }

   @Override
   public boolean isSupported() {
      return true;
   }

   @Override
   public Object getNmsHandle(ItemDisplay template) {
      Entity var2 = ((CraftEntity)template).getHandle();
      return List.copyOf(var2.getEntityData().packAll());
   }

   @Override
   public void sendPosition(Player viewer, DisplayEntityKey identity, double x, double y, double z, Object metadata) {
      ClientboundAddEntityPacket var10 = new ClientboundAddEntityPacket(
         identity.entityId(), identity.worldId(), x, y, z, 0.0F, 0.0F, EntityType.ITEM_DISPLAY, 0, Vec3.ZERO, 0.0
      );
      ClientboundSetEntityDataPacket var11 = new ClientboundSetEntityDataPacket(identity.entityId(), ar(metadata));
      NativePacketsHolder.PE().method1868(viewer, new ClientboundBundlePacket(List.of(var10, var11)));
   }

   @Override
   public void sendEntityData(Player viewer, int entityId, Object metadata) {
      NativePacketsHolder.PE().method1868(viewer, new ClientboundSetEntityDataPacket(entityId, ar(metadata)));
   }

   @Override
   public void sendMove(Player viewer, int entityId, double x, double y, double z) {
      NativePacketsHolder.PE()
         .method1868(viewer, new ClientboundTeleportEntityPacket(entityId, new PositionMoveRotation(new Vec3(x, y, z), Vec3.ZERO, 0.0F, 0.0F), Set.of(), false));
   }

   @Override
   public void sendPassengers(Player viewer, int[] entityIds, int count) {
      NativePacketsHolder.PE().method1868(viewer, new ClientboundRemoveEntitiesPacket(Arrays.copyOf(entityIds, count)));
   }

   @Override
   public void sendPassengerUpdate(Player viewer, List updates, int[] destroyEntityIds, int destroyCount) {
      ArrayList var5 = new ArrayList(updates.size() * 2 + (destroyCount > 0 ? 1 : 0));

      for (DisplayUpdateRecord var7 : updates) {
         if (var7.Os()) {
            var5.add(
               new ClientboundAddEntityPacket(
                  var7.key().entityId(), var7.key().worldId(), var7.ka(), var7.kb(), var7.kc(), 0.0F, 0.0F, EntityType.ITEM_DISPLAY, 0, Vec3.ZERO, 0.0
               )
            );
         } else if (var7.Pt()) {
            var5.add(
               new ClientboundTeleportEntityPacket(
                  var7.key().entityId(), new PositionMoveRotation(new Vec3(var7.ka(), var7.kb(), var7.kc()), Vec3.ZERO, 0.0F, 0.0F), Set.of(), false
               )
            );
         }

         if (var7.value() != null) {
            var5.add(new ClientboundSetEntityDataPacket(var7.key().entityId(), ar(var7.value())));
         }
      }

      if (destroyCount > 0) {
         var5.add(new ClientboundRemoveEntitiesPacket(Arrays.copyOf(destroyEntityIds, destroyCount)));
      }

      if (!var5.isEmpty()) {
         NativePacketsHolder.PE().method1868(viewer, new ClientboundBundlePacket(var5));
      }
   }

   private static List ar(Object var0) {
      return (List)var0;
   }

   public void method5245(Entity var1, Packet var2) {
      if (bmJ && var1.moonrise$getTrackedEntity() != null) {
         var1.moonrise$getTrackedEntity().sendToTrackingPlayersAndSelf(var2);
      } else {
         ((ServerChunkCache)var1.level().getChunkSource()).chunkMap.sendToTrackingPlayers(var1, var2);
         if (var1 instanceof ServerPlayer var3 && !var3.connection.isDisconnected()) {
            var3.connection.send(var2);
         }
      }
   }

   @Override
   public void setField(Object nmsPacket, Object nmsSound) {
      if (bmK != null) {
         ServerCompat.writeField(bmK, nmsPacket, Holder.direct(nmsSound));
      }
   }

   @Override
   public Object getField(String key) {
      return Holder.direct(SoundEvent.createVariableRangeEvent(Identifier.parse(key)));
   }

   @Override
   public String af(Object nmsPacket) {
      return ((SoundEvent)((ClientboundSoundPacket)nmsPacket).getSound().value()).location().toString();
   }

   @Override
   public void playSound(String sound, float volume, float pitch, SoundCategory bukkitCategory, Location bukkitLocation) {
      Collection var6 = BlockFaceUtils.playersInView(bukkitLocation);
      if (!var6.isEmpty()) {
         long var7 = ((CraftWorld)bukkitLocation.getWorld()).getHandle().getRandom().nextLong();
         Object var9 = NativePacketsHolder.PE()
            .Pv()
            .soundAt(
               sound,
               NativeSoundSource.valueOf(bukkitCategory.name()),
               bukkitLocation.getX(),
               bukkitLocation.getY(),
               bukkitLocation.getZ(),
               volume,
               pitch,
               var7
            );

         for (Player var11 : var6) {
            ServerPlayer var12 = ((CraftPlayer)var11).getHandle();
            NativePacketsHolder.PE().method1868(var12.getBukkitEntity(), var9);
         }
      }
   }

   @Override
   public void playSoundSimple(String sound, SoundCategory bukkitCategory, Location bukkitLocation) {
      Collection var4 = BlockFaceUtils.playersInView(bukkitLocation);
      if (!var4.isEmpty()) {
         Object var5 = NativePacketsHolder.PE().Pv().stopSound(sound, NativeSoundSource.valueOf(bukkitCategory.name()));

         for (Player var7 : var4) {
            ServerPlayer var8 = ((CraftPlayer)var7).getHandle();
            NativePacketsHolder.PE().method1868(var8.getBukkitEntity(), var5);
         }
      }
   }

   @Override
   public void playSoundDup2(String sound, float volume, float pitch, SoundCategory bukkitCategory, Location bukkitLocation) {
      Collection var6 = BlockFaceUtils.playersInView(bukkitLocation);
      if (!var6.isEmpty()) {
         long var7 = ((CraftWorld)bukkitLocation.getWorld()).getHandle().getRandom().nextLong();
         Object var9 = NativePacketsHolder.PE().Pv().stopSound(sound, NativeSoundSource.valueOf(bukkitCategory.name()));
         Object var10 = NativePacketsHolder.PE()
            .Pv()
            .soundAt(
               sound,
               NativeSoundSource.valueOf(bukkitCategory.name()),
               bukkitLocation.getX(),
               bukkitLocation.getY(),
               bukkitLocation.getZ(),
               volume,
               pitch,
               var7
            );

         for (Player var12 : var6) {
            ServerPlayer var13 = ((CraftPlayer)var12).getHandle();
            NativePacketsHolder.PE().method1868(var13.getBukkitEntity(), var9);
            NativePacketsHolder.PE().method1868(var13.getBukkitEntity(), var10);
         }
      }
   }

   @Override
   public void sendAnimation(Player bukkitPlayer, String address, int port) {
      DedicatedServerProperties var4 = ((DedicatedServer)MinecraftServer.getServer()).getProperties();

      try {
         Class var5 = var4.acceptsTransfers.getClass();
         Constructor var6 = var5.getDeclaredConstructors()[0];
         var6.setAccessible(true);
         Object var7 = var6.newInstance(var4, "", true, (Function<Object, Boolean>)var0 -> true);
         Field var8 = DedicatedServerProperties.class.getDeclaredField("acceptsTransfers");
         var8.setAccessible(true);
         var8.set(var4, var7);
         NativePacketsHolder.PE().method1868(bukkitPlayer, NativePacketsHolder.PE().Pv().transfer(address, port));
      } catch (Exception var9) {
         Log.error("Failed to set acceptsTransfers property", var9);
      }

      NativePacketsHolder.PE().method1868(bukkitPlayer, NativePacketsHolder.PE().Pv().transfer(address, port));
   }

   @Override
   public ItemDisplay spawnItemDisplay(Location bukkitLocation, Consumer consumer) {
      net.minecraft.world.entity.Display.ItemDisplay var3 = new net.minecraft.world.entity.Display.ItemDisplay(
         EntityType.ITEM_DISPLAY, ((CraftWorld)bukkitLocation.getWorld()).getHandle()
      );
      var3.setPos(bukkitLocation.getX(), bukkitLocation.getY(), bukkitLocation.getZ());
      ItemDisplay var4 = (ItemDisplay)var3.getBukkitEntity();
      if (consumer != null) {
         consumer.accept(var4);
      }

      return (ItemDisplay)var3.getBukkitEntity();
   }

   @Override
   public void mountFor(org.bukkit.entity.Entity bukkitEntity, Player bukkitViewer) {
      Entity var3 = ((CraftEntity)bukkitEntity).getHandle();
      Location var4 = bukkitEntity.getLocation();
      ClientboundBundlePacket var5 = new ClientboundBundlePacket(
         List.of(
            new ClientboundAddEntityPacket(
               var3.getId(),
               var3.getUUID(),
               var4.getX(),
               var4.getY(),
               var4.getZ(),
               bukkitEntity.getLocation().getPitch(),
               bukkitEntity.getLocation().getYaw(),
               var3.getType(),
               0,
               new Vec3(0.0, 0.0, 0.0),
               0.0
            ),
            new ClientboundSetEntityDataPacket(var3.getId(), var3.getEntityData().packAll())
         )
      );
      NativePacketsHolder.PE().method1868(bukkitViewer, var5);
   }

   @Override
   public void teleportEntity(org.bukkit.entity.Entity bukkitEntity, Location location) {
      Entity var3 = ((CraftEntity)bukkitEntity).getHandle();
      var3.setPosRaw(location.getX(), location.getY(), location.getZ());
      var3.setYRot(location.getYaw());
      var3.setYHeadRot(location.getYaw());
      var3.setXRot(location.getPitch());
      Object var4 = NativePacketsHolder.PE()
         .Pv()
         .teleport(var3.getId(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), false);
      Object var5 = NativePacketsHolder.PE().Pv().rotateHead(var3.getId(), location.getYaw());
      Object var6 = NativePacketsHolder.PE().Pv().bundle(List.of(var4, var5));

      for (net.minecraft.world.entity.player.Player var8 : var3.level().players()) {
         if (var8 instanceof ServerPlayer var9) {
            NativePacketsHolder.PE().method1868(var9.getBukkitEntity(), var6);
         }
      }
   }

   @Override
   public void teleportFor(org.bukkit.entity.Entity bukkitEntity, Player bukkitPlayer, Location location) {
      Entity var4 = ((CraftEntity)bukkitEntity).getHandle();
      var4.setPosRaw(location.getX(), location.getY(), location.getZ());
      var4.setYRot(location.getYaw());
      var4.setYHeadRot(location.getYaw());
      var4.setXRot(location.getPitch());
      Object var5 = NativePacketsHolder.PE()
         .Pv()
         .teleport(var4.getId(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), false);
      Object var6 = NativePacketsHolder.PE().Pv().rotateHead(var4.getId(), location.getYaw());
      Object var7 = NativePacketsHolder.PE().Pv().bundle(List.of(var5, var6));
      NativePacketsHolder.PE().method1868(bukkitPlayer, var7);
   }

   @Override
   public void setVisibilityFor(org.bukkit.entity.Entity bukkitEntity, Player bukkitPlayer, boolean onlyDirty) {
      Entity var4 = ((CraftEntity)bukkitEntity).getHandle();
      List var5;
      if (onlyDirty) {
         var5 = var4.getEntityData().packDirty();
         if (var5 == null) {
            return;
         }
      } else {
         var5 = var4.getEntityData().packAll();
         if (var5.isEmpty()) {
            return;
         }
      }

      NativePacketsHolder.PE().method1868(bukkitPlayer, new ClientboundSetEntityDataPacket(var4.getId(), var5));
   }

   @Override
   public void removeFor(org.bukkit.entity.Entity bukkitEntity, Player bukkitViewer) {
      Entity var3 = ((CraftEntity)bukkitEntity).getHandle();
      Object var4 = NativePacketsHolder.PE().Pv().removeEntities(new int[]{var3.getId()});
      NativePacketsHolder.PE().method1868(bukkitViewer, var4);
   }

   @Override
   public void removeEntity(org.bukkit.entity.Entity bukkitEntity) {
      Entity var2 = ((CraftEntity)bukkitEntity).getHandle();
      Object var3 = NativePacketsHolder.PE().Pv().removeEntities(new int[]{var2.getId()});

      for (net.minecraft.world.entity.player.Player var5 : var2.level().players()) {
         if (var5 instanceof ServerPlayer var6) {
            NativePacketsHolder.PE().method1868(var6.getBukkitEntity(), var3);
         }
      }
   }

   @Override
   public void sendRemove(Player bukkitPlayer, IntList toRemove) {
      Object var3 = NativePacketsHolder.PE().Pv().removeEntities(toRemove.toIntArray());
      NativePacketsHolder.PE().method1868(bukkitPlayer, var3);
   }

   @Override
   public List getDisplayPassengers() {
      CraftServer var1 = (CraftServer)Bukkit.getServer();
      ServerConnectionListener var2 = var1.getServer().getConnection();
      Field var3 = PacketDisplayBridge.lookupField(var2);
      return (List)var3.get(var2);
   }

   @Override
   public boolean hasNmsField(String context) {
      return "ON_SHELF".equals(context) || PacketDisplayBridge.super.hasNmsField(context);
   }

   @Override
   public void setItemModel(ItemDisplay template, String context) {
      if ("ON_SHELF".equals(context)) {
         ((net.minecraft.world.entity.Display.ItemDisplay)((CraftEntity)template).getHandle()).setItemTransform(ItemDisplayContext.ON_SHELF);
      } else {
         PacketDisplayBridge.super.setItemModel(template, context);
      }
   }
}
