package dev.lone.itemsadder.event;

import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.PlayerLookPacketView;
import beer.devs.nativepackets.api.PlayerMovementPacketView;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.text.NmsChatService;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.ServerCompat;
import io.netty.buffer.Unpooled;
import java.lang.invoke.MethodHandle;
import java.util.Set;
import java.util.UUID;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Rot;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.Action;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketPlayerTriggerM extends AbstractPlayerTrigger {
   private PacketListenerHandle bkN;
   private PacketListenerHandle bkO;
   private PacketListenerHandle cU;
   static MethodHandle bkP = ServerCompat.findConstructorHandle(ClientboundSetPassengersPacket.class, FriendlyByteBuf.class);

   public static Object ae(Object var0) {
      try {
         return (ClientboundSetPassengersPacket)bkP.invokeExact((FriendlyByteBuf)((FriendlyByteBuf)var0));
      } catch (Throwable var2) {
         throw new RuntimeException(var2);
      }
   }

   @Override
   protected void Pc() {
      this.Pf();
      if (this.bkN != null) {
         NativePacketsHolder.PE().method1889(this.Pg(), this.bkN);
         this.bkN = null;
      }

      if (this.bkO != null) {
         NativePacketsHolder.PE().method1889(this.Pg(), this.bkO);
         this.bkO = null;
      }

      if (this.cU != null) {
         NativePacketsHolder.PE().method1889(this.Pg(), this.cU);
         this.cU = null;
      }

      this.Pd();
   }

   @Override
   protected void bX() {
      EventRegistrar.registerWithMainPlugin(this);
      long var1 = this.Pe();
      this.bkN = NativePacketsHolder.PE()
         .method1888(
            this.Pg(), PacketSelector.exact(PacketTypes.PLAYER_POSITION), ListenerPriority.NORMAL, true, ListenerFailurePolicy.PASS_ORIGINAL, var3 -> {
               var3.view(PlayerMovementPacketView.class);
               UUID var4 = NativePacketsHolder.PE().method1883(var3);
               if (this.method5135(var4, var1)) {
                  var3.cancel();
               }
            }
         );
      this.bkO = NativePacketsHolder.PE()
         .method1888(
            this.Pg(),
            PacketSelector.exact(PacketTypes.PLAYER_POSITION_ROTATION),
            ListenerPriority.NORMAL,
            true,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var3 -> {
               var3.view(PlayerMovementPacketView.class);
               UUID var4 = NativePacketsHolder.PE().method1883(var3);
               if (this.method5135(var4, var1)) {
                  var3.cancel();
               }
            }
         );
      this.cU = NativePacketsHolder.PE()
         .method1888(
            this.Pg(),
            PacketSelector.exact(PacketTypes.PLAYER_LOOK),
            ListenerPriority.NORMAL,
            true,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var3 -> {
               PlayerLookPacketView var4 = (PlayerLookPacketView)var3.view(PlayerLookPacketView.class);
               UUID var5 = NativePacketsHolder.PE().method1883(var3);
               if (this.method5135(var5, var1)) {
                  PlayerTriggerKey var6 = new PlayerTriggerKey(var5, var3.connection().id(), var4.yaw(), var4.pitch(), var1);
                  var3.cancel();
                  var3.sync(
                     (var2, var3x) -> {
                        PlayerTriggerContext var4x = this.method5136(var6, var3x.player());
                        if (var4x != null) {
                           Player var5x = var4x.Ph();
                           float var6x = var6.bm();
                           float var7 = var6.method66();
                           float var8 = var4x.method5206(var6x);
                           float var9 = var4x.method5207(var7);
                           float var10 = A(var6x - var8);
                           float var11 = A(var7 - var9);
                           var4x.method5208(var6x);
                           var4x.method5209(var7);
                           if (var10 != 0.0F || var11 != 0.0F) {
                              if (!(Math.abs(var10) > 50.0) && !(Math.abs(var11) > 50.0)) {
                                 Entity var12 = ((CraftEntity)var4x.Pj()).getHandle();
                                 Location var13 = a(var4x.Pi(), var6x, var7);
                                 var12.setPos(var13.getX(), var13.getY(), var13.getZ());
                                 var12.setYRot(var6x);
                                 var12.setXRot(var7);
                                 Vec3 var16 = new Vec3(var4x.Pi().getX(), var4x.Pi().getY(), var4x.Pi().getZ());
                                 double var17 = var16.x - var12.getX();
                                 double var19 = var16.z - var12.getZ();
                                 double var21 = var16.y - (var12.getY() + var12.getEyeHeight());
                                 float var14 = (float)(Math.toDegrees(Math.atan2(var19, var17)) - 90.0);
                                 float var15 = (float)(-Math.toDegrees(Math.atan2(var21, Math.sqrt(var17 * var17 + var19 * var19))));
                                 ServerPlayer var23 = ((CraftPlayer)var5x).getHandle();
                                 var23.connection
                                    .send(
                                       new ClientboundTeleportEntityPacket(
                                          var12.getId(),
                                          new PositionMoveRotation(new Vec3(var13.getX(), var13.getY(), var13.getZ()), Vec3.ZERO, var14, var15),
                                          Set.of(),
                                          false
                                       )
                                    );
                                 var23.connection.send(new Rot(var12.getId(), Mth.packDegrees(var14), Mth.packDegrees(var15), false));
                                 var23.connection.send(new ClientboundRotateHeadPacket(var12, Mth.packDegrees(var14)));
                              }
                           }
                        }
                     }
                  );
               }
            }
         );
   }

   @Override
   public boolean method5132(Player bukkitPlayer, Location focusLocation) {
      if (cz(bukkitPlayer)) {
         return false;
      }

      ServerPlayer var3 = ((CraftPlayer)bukkitPlayer).getHandle();
      Zombie var4 = new Zombie(EntityTypes.ZOMBIE, var3.level());
      Location var5 = bukkitPlayer.getLocation().add(bukkitPlayer.getLocation().getDirection().normalize().multiply(-3.0));
      var4.setPosRaw(var5.getX(), var5.getY(), var5.getZ());
      var4.setNoGravity(true);
      var4.setInvulnerable(true);
      var4.setInvisible(true);
      Vec3 var6 = new Vec3(focusLocation.getX(), focusLocation.getY(), focusLocation.getZ());
      double var7 = var6.x - var4.getX();
      double var9 = var6.z - var4.getZ();
      double var11 = var6.y - (var4.getY() + var4.getEyeHeight());
      float var13 = (float)(Math.toDegrees(Math.atan2(var9, var7)) - 90.0);
      float var14 = (float)(-Math.toDegrees(Math.atan2(var11, Math.sqrt(var7 * var7 + var9 * var9))));
      var4.setYRot(var13);
      var4.setXRot(var14);
      var4.setYHeadRot(var13);
      var4.lookAt(Anchor.EYES, var6);
      this.method5147(var4, bukkitPlayer);
      PlayerTriggerContext var15 = new PlayerTriggerContext(bukkitPlayer, focusLocation.clone(), var4.getBukkitEntity(), bukkitPlayer.getLocation().clone());
      this.method5134(bukkitPlayer, var15);
      var3.connection
         .send(
            new ClientboundTeleportEntityPacket(
               var3.getId(),
               new PositionMoveRotation(new Vec3(var4.getX(), var4.getY(), var4.getZ()), Vec3.ZERO, var4.getYRot(), var4.getXRot()),
               Set.of(),
               false
            )
         );
      var3.connection.send(new Rot(var4.getId(), Mth.packDegrees(var4.getYRot()), Mth.packDegrees(var4.getXRot()), false));
      var3.connection.send(new ClientboundRotateHeadPacket(var4, Mth.packDegrees(var4.getYRot())));
      var3.connection.send(new ClientboundSetCameraPacket(var4));
      int[] var16 = new int[]{var3.getId()};
      FriendlyByteBuf var17 = new FriendlyByteBuf(Unpooled.buffer());
      var17.writeVarInt(var4.getId());
      var17.writeVarIntArray(var16);
      ClientboundSetPassengersPacket var18 = (ClientboundSetPassengersPacket)ae(var17);
      var3.connection.send(var18);
      var3.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.CHANGE_GAME_MODE, GameType.SPECTATOR.getId()));
      NmsChatService.method2951(bukkitPlayer, "Press SHIFT to exit");
      return true;
   }

   @Override
   protected void method5137(PlayerTriggerContext orbit) {
      Player var2 = orbit.Ph();
      ServerPlayer var3 = ((CraftPlayer)var2).getHandle();
      Entity var4 = ((CraftEntity)orbit.Pj()).getHandle();
      ClientboundSetCameraPacket var5 = new ClientboundSetCameraPacket(var3);
      var3.connection.send(var5);
      this.method5148(var2, var4.getId());
      var3.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.CHANGE_GAME_MODE, var3.gameMode.getGameModeForPlayer().getId()));
      var3.connection.send(new ClientboundPlayerInfoUpdatePacket(Action.UPDATE_GAME_MODE, var3));
      if (var2.getWorld().equals(orbit.Pk().getWorld())) {
         SchedulerUtils.teleportOnMainThread(var2, orbit.Pk());
      }
   }

   @Override
   public void method5147(Object nmsEntity, Player bukkitPlayer) {
      ServerPlayer var3 = ((CraftPlayer)bukkitPlayer).getHandle();
      Entity var4 = (Entity)nmsEntity;
      var3.connection
         .send(
            new ClientboundAddEntityPacket(
               var4.getId(),
               var4.getUUID(),
               var4.trackingPosition().x(),
               var4.trackingPosition().y(),
               var4.trackingPosition().z(),
               var4.getXRot(),
               var4.getYRot(),
               var4.getType(),
               0,
               var4.getDeltaMovement(),
               var4.getYHeadRot()
            )
         );
      var3.connection.send(new ClientboundSetEntityDataPacket(var4.getId(), var4.getEntityData().getNonDefaultValues()));
   }

   @Override
   public void method5148(Player bukkitPlayer, int... entityIds) {
      ServerPlayer var3 = ((CraftPlayer)bukkitPlayer).getHandle();
      var3.connection.send(new ClientboundRemoveEntitiesPacket(entityIds));
   }
}
