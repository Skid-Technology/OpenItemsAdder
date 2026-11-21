package dev.lone.itemsadder.entity;

import beer.devs.nativepackets.api.NativeSoundSource;
import beer.devs.nativepackets.api.NativeValue;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.ServerCompat;
import it.unimi.dsi.fastutil.ints.IntList;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutNamedSoundEffect;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerChunkMap.EntityTracker;
import net.minecraft.server.network.ServerConnection;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.craftbukkit.v1_21_R2.CraftServer;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftPlayer;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

public class DisplayBridgeImplE implements PacketDisplayBridge {
   private static final PassengerRegistry bmn = new PassengerRegistryImplE();
   private static final boolean bmo = ServerCompat.findFieldByType(Entity.class, EntityTracker.class) != null;
   private static final Field bmp = ServerCompat.findFieldByType(PacketPlayOutNamedSoundEffect.class, Holder.class);

   @Override
   public PassengerRegistry getPassengerRegistry() {
      return bmn;
   }

   @Override
   public DisplayIndexPair toDisplayIndex(NativeValue nativeChunkPosition) {
      ChunkCoordIntPair var2 = (ChunkCoordIntPair)nativeChunkPosition.nativeHandle();
      return new DisplayIndexPair(var2.h, var2.i);
   }

   public void method5245(Entity var1, Packet var2) {
      if (bmo && var1.moonrise$getTrackedEntity() != null) {
         var1.moonrise$getTrackedEntity().b(var2);
      } else {
         ((ChunkProviderServer)var1.dW().R()).a.a(var1, var2);
         if (var1 instanceof EntityPlayer var3 && !var3.f.isDisconnected()) {
            var3.f.b(var2);
         }
      }
   }

   @Override
   public void setField(Object nmsPacket, Object nmsSound) {
      if (bmp != null) {
         ServerCompat.writeField(bmp, nmsPacket, Holder.a(nmsSound));
      }
   }

   @Override
   public Object getField(String key) {
      return Holder.a(SoundEffect.a(MinecraftKey.a(key)));
   }

   @Override
   public String af(Object nmsPacket) {
      return ((SoundEffect)((PacketPlayOutNamedSoundEffect)nmsPacket).b().a()).a().toString();
   }

   @Override
   public void playSound(String sound, float volume, float pitch, SoundCategory bukkitCategory, Location bukkitLocation) {
      Collection var6 = BlockFaceUtils.playersInView(bukkitLocation);
      if (!var6.isEmpty()) {
         long var7 = ((CraftWorld)bukkitLocation.getWorld()).getHandle().A.g();
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
            EntityPlayer var12 = ((CraftPlayer)var11).getHandle();
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
            EntityPlayer var8 = ((CraftPlayer)var7).getHandle();
            NativePacketsHolder.PE().method1868(var8.getBukkitEntity(), var5);
         }
      }
   }

   @Override
   public void playSoundDup2(String sound, float volume, float pitch, SoundCategory bukkitCategory, Location bukkitLocation) {
      Collection var6 = BlockFaceUtils.playersInView(bukkitLocation);
      if (!var6.isEmpty()) {
         long var7 = ((CraftWorld)bukkitLocation.getWorld()).getHandle().A.g();
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
            EntityPlayer var13 = ((CraftPlayer)var12).getHandle();
            NativePacketsHolder.PE().method1868(var13.getBukkitEntity(), var9);
            NativePacketsHolder.PE().method1868(var13.getBukkitEntity(), var10);
         }
      }
   }

   @Override
   public void sendAnimation(Player bukkitPlayer, String address, int port) {
      ((DedicatedServer)MinecraftServer.getServer()).s.a().ab = true;
      NativePacketsHolder.PE().method1868(bukkitPlayer, NativePacketsHolder.PE().Pv().transfer(address, port));
   }

   @Override
   public ItemDisplay spawnItemDisplay(Location bukkitLocation, Consumer consumer) {
      net.minecraft.world.entity.Display.ItemDisplay var3 = new net.minecraft.world.entity.Display.ItemDisplay(
         EntityTypes.as, ((CraftWorld)bukkitLocation.getWorld()).getHandle()
      );
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
            new PacketPlayOutSpawnEntity(
               var3.ar(),
               var3.cG(),
               var4.getX(),
               var4.getY(),
               var4.getZ(),
               bukkitEntity.getLocation().getPitch(),
               bukkitEntity.getLocation().getYaw(),
               var3.aq(),
               0,
               new Vec3D(0.0, 0.0, 0.0),
               0.0
            ),
            new PacketPlayOutEntityMetadata(var3.ar(), var3.au().packAll())
         )
      );
      NativePacketsHolder.PE().method1868(bukkitViewer, var5);
   }

   @Override
   public void teleportEntity(org.bukkit.entity.Entity bukkitEntity, Location location) {
      Entity var3 = ((CraftEntity)bukkitEntity).getHandle();
      var3.o(location.getX(), location.getY(), location.getZ());
      var3.v(location.getYaw());
      var3.q(location.getYaw());
      var3.w(location.getPitch());
      Object var4 = NativePacketsHolder.PE()
         .Pv()
         .teleport(var3.ar(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), false);
      Object var5 = NativePacketsHolder.PE().Pv().rotateHead(var3.ar(), location.getYaw());
      Object var6 = NativePacketsHolder.PE().Pv().bundle(List.of(var4, var5));

      for (EntityHuman var8 : var3.dW().y()) {
         if (var8 instanceof EntityPlayer var9) {
            NativePacketsHolder.PE().method1868(var9.getBukkitEntity(), var6);
         }
      }
   }

   @Override
   public void teleportFor(org.bukkit.entity.Entity bukkitEntity, Player bukkitPlayer, Location location) {
      Entity var4 = ((CraftEntity)bukkitEntity).getHandle();
      var4.o(location.getX(), location.getY(), location.getZ());
      var4.v(location.getYaw());
      var4.q(location.getYaw());
      var4.w(location.getPitch());
      Object var5 = NativePacketsHolder.PE()
         .Pv()
         .teleport(var4.ar(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), false);
      Object var6 = NativePacketsHolder.PE().Pv().rotateHead(var4.ar(), location.getYaw());
      Object var7 = NativePacketsHolder.PE().Pv().bundle(List.of(var5, var6));
      NativePacketsHolder.PE().method1868(bukkitPlayer, var7);
   }

   @Override
   public void setVisibilityFor(org.bukkit.entity.Entity bukkitEntity, Player bukkitPlayer, boolean onlyDirty) {
      Entity var4 = ((CraftEntity)bukkitEntity).getHandle();
      List var5;
      if (onlyDirty) {
         var5 = var4.au().b();
         if (var5 == null) {
            return;
         }
      } else {
         var5 = var4.au().packAll();
         if (var5.isEmpty()) {
            return;
         }
      }

      NativePacketsHolder.PE().method1868(bukkitPlayer, new PacketPlayOutEntityMetadata(var4.ar(), var5));
   }

   @Override
   public void removeFor(org.bukkit.entity.Entity bukkitEntity, Player bukkitViewer) {
      Entity var3 = ((CraftEntity)bukkitEntity).getHandle();
      Object var4 = NativePacketsHolder.PE().Pv().removeEntities(new int[]{var3.ar()});
      NativePacketsHolder.PE().method1868(bukkitViewer, var4);
   }

   @Override
   public void sendRemove(Player bukkitPlayer, IntList toRemove) {
      Object var3 = NativePacketsHolder.PE().Pv().removeEntities(toRemove.toIntArray());
      NativePacketsHolder.PE().method1868(bukkitPlayer, var3);
   }

   @Override
   public List getDisplayPassengers() {
      CraftServer var1 = (CraftServer)Bukkit.getServer();
      ServerConnection var2 = var1.getServer().ah();
      Field var3 = PacketDisplayBridge.lookupField(var2);
      return (List)var3.get(var2);
   }
}
