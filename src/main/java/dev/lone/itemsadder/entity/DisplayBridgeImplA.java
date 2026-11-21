package dev.lone.itemsadder.entity;

import beer.devs.nativepackets.api.NativeSoundSource;
import beer.devs.nativepackets.api.NativeValue;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.ServerCompat;
import it.unimi.dsi.fastutil.ints.IntList;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
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
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

public class DisplayBridgeImplA implements PacketDisplayBridge {
   private static final PassengerRegistry bmb = new PassengerRegistryImplA();
   private static final boolean bmc = ServerCompat.findFieldByType(Entity.class, EntityTracker.class) != null;
   private static final Field bmd = ServerCompat.findFieldByType(PacketPlayOutNamedSoundEffect.class, Holder.class);

   @Override
   public PassengerRegistry getPassengerRegistry() {
      return bmb;
   }

   @Override
   public DisplayIndexPair toDisplayIndex(NativeValue nativeChunkPosition) {
      ChunkCoordIntPair var2 = (ChunkCoordIntPair)nativeChunkPosition.nativeHandle();
      return new DisplayIndexPair(var2.e, var2.f);
   }

   public void method5245(Entity var1, Packet var2) {
      if (bmc && var1.tracker != null) {
         var1.tracker.b(var2);
      } else {
         ((ChunkProviderServer)var1.dP().N()).a.a(var1, var2);
         if (var1 instanceof EntityPlayer var3 && !var3.c.isDisconnected()) {
            var3.c.b(var2);
         }
      }
   }

   @Override
   public void setField(Object nmsPacket, Object nmsSound) {
      if (bmd != null) {
         ServerCompat.writeField(bmd, nmsPacket, Holder.a(nmsSound));
      }
   }

   @Override
   public Object getField(String key) {
      return Holder.a(SoundEffect.a(new MinecraftKey(key)));
   }

   @Override
   public String af(Object nmsPacket) {
      return ((SoundEffect)((PacketPlayOutNamedSoundEffect)nmsPacket).b().a()).a().toString();
   }

   @Override
   public void playSound(String sound, float volume, float pitch, SoundCategory bukkitCategory, Location bukkitLocation) {
      int var6 = bukkitLocation.getWorld().getViewDistance() * 16;
      long var7 = ((CraftWorld)bukkitLocation.getWorld()).getHandle().z.g();
      Object var9 = NativePacketsHolder.PE()
         .Pv()
         .soundAt(
            sound, NativeSoundSource.valueOf(bukkitCategory.name()), bukkitLocation.getX(), bukkitLocation.getY(), bukkitLocation.getZ(), volume, pitch, var7
         );

      for (EntityPlayer var11 : ((CraftWorld)bukkitLocation.getWorld()).getHandle().x()) {
         if (var11.i(bukkitLocation.getX(), bukkitLocation.getY(), bukkitLocation.getZ()) < var6 * var6) {
            NativePacketsHolder.PE().method1868(var11.getBukkitEntity(), var9);
         }
      }
   }

   @Override
   public void playSoundSimple(String sound, SoundCategory bukkitCategory, Location bukkitLocation) {
      int var4 = bukkitLocation.getWorld().getViewDistance() * 16;
      Object var5 = NativePacketsHolder.PE().Pv().stopSound(sound, NativeSoundSource.valueOf(bukkitCategory.name()));

      for (EntityPlayer var7 : ((CraftWorld)bukkitLocation.getWorld()).getHandle().x()) {
         if (var7.i(bukkitLocation.getX(), bukkitLocation.getY(), bukkitLocation.getZ()) < var4 * var4) {
            NativePacketsHolder.PE().method1868(var7.getBukkitEntity(), var5);
         }
      }
   }

   @Override
   public void playSoundDup2(String sound, float volume, float pitch, SoundCategory bukkitCategory, Location bukkitLocation) {
      int var6 = bukkitLocation.getWorld().getViewDistance() * 16;
      long var7 = ((CraftWorld)bukkitLocation.getWorld()).getHandle().z.g();
      Object var9 = NativePacketsHolder.PE().Pv().stopSound(sound, NativeSoundSource.valueOf(bukkitCategory.name()));
      Object var10 = NativePacketsHolder.PE()
         .Pv()
         .soundAt(
            sound, NativeSoundSource.valueOf(bukkitCategory.name()), bukkitLocation.getX(), bukkitLocation.getY(), bukkitLocation.getZ(), volume, pitch, var7
         );

      for (EntityPlayer var12 : ((CraftWorld)bukkitLocation.getWorld()).getHandle().x()) {
         if (var12.i(bukkitLocation.getX(), bukkitLocation.getY(), bukkitLocation.getZ()) < var6 * var6) {
            NativePacketsHolder.PE().method1868(var12.getBukkitEntity(), var9);
            NativePacketsHolder.PE().method1868(var12.getBukkitEntity(), var10);
         }
      }
   }

   @Override
   public void sendAnimation(Player bukkitPlayer, String address, int port) {
      ((DedicatedServer)MinecraftServer.getServer()).r.a().aa = true;
      NativePacketsHolder.PE().method1868(bukkitPlayer, NativePacketsHolder.PE().Pv().transfer(address, port));
   }

   @Override
   public ItemDisplay spawnItemDisplay(Location bukkitLocation, Consumer consumer) {
      net.minecraft.world.entity.Display.ItemDisplay var3 = new net.minecraft.world.entity.Display.ItemDisplay(
         EntityTypes.ah, ((CraftWorld)bukkitLocation.getWorld()).getHandle()
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
               var3.al(),
               var3.cz(),
               var4.getX(),
               var4.getY(),
               var4.getZ(),
               bukkitEntity.getLocation().getPitch(),
               bukkitEntity.getLocation().getYaw(),
               var3.ak(),
               0,
               new Vec3D(0.0, 0.0, 0.0),
               0.0
            ),
            new PacketPlayOutEntityMetadata(var3.al(), var3.ap().packAll()),
            new PacketPlayOutEntityTeleport(var3),
            new PacketPlayOutEntityHeadRotation(var3, (byte)MathHelper.d(bukkitEntity.getLocation().getYaw() * 256.0F / 360.0F))
         )
      );
      NativePacketsHolder.PE().method1868(bukkitViewer, var5);
   }

   @Override
   public void teleportEntity(org.bukkit.entity.Entity bukkitEntity, Location location) {
      Entity var3 = ((CraftEntity)bukkitEntity).getHandle();
      var3.p(location.getX(), location.getY(), location.getZ());
      var3.r(location.getYaw());
      var3.n(location.getYaw());
      var3.s(location.getPitch());
      Object var4 = NativePacketsHolder.PE()
         .Pv()
         .teleport(var3.al(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), false);
      Object var5 = NativePacketsHolder.PE().Pv().rotateHead(var3.al(), location.getYaw());
      Object var6 = NativePacketsHolder.PE().Pv().bundle(List.of(var4, var5));

      for (EntityHuman var8 : var3.dP().x()) {
         if (var8 instanceof EntityPlayer var9) {
            NativePacketsHolder.PE().method1868(var9.getBukkitEntity(), var6);
         }
      }
   }

   @Override
   public void teleportFor(org.bukkit.entity.Entity bukkitEntity, Player bukkitPlayer, Location location) {
      Entity var4 = ((CraftEntity)bukkitEntity).getHandle();
      var4.p(location.getX(), location.getY(), location.getZ());
      var4.r(location.getYaw());
      var4.n(location.getYaw());
      var4.s(location.getPitch());
      Object var5 = NativePacketsHolder.PE()
         .Pv()
         .teleport(var4.al(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), false);
      Object var6 = NativePacketsHolder.PE().Pv().rotateHead(var4.al(), location.getYaw());
      Object var7 = NativePacketsHolder.PE().Pv().bundle(List.of(var5, var6));
      NativePacketsHolder.PE().method1868(bukkitPlayer, var7);
   }

   @Override
   public void setVisibilityFor(org.bukkit.entity.Entity bukkitEntity, Player bukkitPlayer, boolean onlyDirty) {
      Entity var4 = ((CraftEntity)bukkitEntity).getHandle();
      List var5;
      if (onlyDirty) {
         var5 = var4.ap().b();
         if (var5 == null) {
            return;
         }
      } else {
         var5 = var4.ap().packAll();
         if (var5.isEmpty()) {
            return;
         }
      }

      NativePacketsHolder.PE().method1868(bukkitPlayer, new PacketPlayOutEntityMetadata(var4.al(), var5));
   }

   @Override
   public void removeFor(org.bukkit.entity.Entity bukkitEntity, Player bukkitViewer) {
      Entity var3 = ((CraftEntity)bukkitEntity).getHandle();
      Object var4 = NativePacketsHolder.PE().Pv().removeEntities(new int[]{var3.al()});
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
      ServerConnection var2 = var1.getServer().ai();
      Field var3 = PacketDisplayBridge.lookupField(var2);
      return (List)var3.get(var2);
   }
}
