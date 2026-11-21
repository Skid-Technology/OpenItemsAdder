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
import net.minecraft.commands.arguments.ArgumentAnchor.Anchor;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.PacketPlayOutCamera;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.a;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketPlayerTriggerG extends AbstractPlayerTrigger {
   private PacketListenerHandle bkN;
   private PacketListenerHandle bkO;
   private PacketListenerHandle cU;
   static MethodHandle bkP = ServerCompat.findConstructorHandle(PacketPlayOutMount.class, PacketDataSerializer.class);

   public static Object ae(Object var0) {
      try {
         return (PacketPlayOutMount)bkP.invokeExact((PacketDataSerializer)((PacketDataSerializer)var0));
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
                                 var12.a_(var13.getX(), var13.getY(), var13.getZ());
                                 var12.w(var6x);
                                 var12.x(var7);
                                 Vec3D var16 = new Vec3D(var4x.Pi().getX(), var4x.Pi().getY(), var4x.Pi().getZ());
                                 double var17 = var16.d - var12.dA();
                                 double var19 = var16.f - var12.dG();
                                 double var21 = var16.e - (var12.dC() + var12.cS());
                                 float var14 = (float)(Math.toDegrees(Math.atan2(var19, var17)) - 90.0);
                                 float var15 = (float)(-Math.toDegrees(Math.atan2(var21, Math.sqrt(var17 * var17 + var19 * var19))));
                                 EntityPlayer var23 = ((CraftPlayer)var5x).getHandle();
                                 var23.f
                                    .b(
                                       new PacketPlayOutEntityTeleport(
                                          var12.ao(),
                                          new PositionMoveRotation(new Vec3D(var13.getX(), var13.getY(), var13.getZ()), Vec3D.c, var14, var15),
                                          Set.of(),
                                          false
                                       )
                                    );
                                 var23.f.b(new PacketPlayOutEntityLook(var12.ao(), MathHelper.g(var14), MathHelper.g(var15), false));
                                 var23.f.b(new PacketPlayOutEntityHeadRotation(var12, MathHelper.g(var14)));
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

      EntityPlayer var3 = ((CraftPlayer)bukkitPlayer).getHandle();
      EntityZombie var4 = new EntityZombie(EntityTypes.bP, var3.dV());
      Location var5 = bukkitPlayer.getLocation().add(bukkitPlayer.getLocation().getDirection().normalize().multiply(-3.0));
      var4.o(var5.getX(), var5.getY(), var5.getZ());
      var4.f(true);
      var4.m(true);
      var4.k(true);
      Vec3D var6 = new Vec3D(focusLocation.getX(), focusLocation.getY(), focusLocation.getZ());
      double var7 = var6.d - var4.dA();
      double var9 = var6.f - var4.dG();
      double var11 = var6.e - (var4.dC() + var4.cS());
      float var13 = (float)(Math.toDegrees(Math.atan2(var9, var7)) - 90.0);
      float var14 = (float)(-Math.toDegrees(Math.atan2(var11, Math.sqrt(var7 * var7 + var9 * var9))));
      var4.w(var13);
      var4.x(var14);
      var4.r(var13);
      var4.a(Anchor.b, var6);
      this.method5147(var4, bukkitPlayer);
      PlayerTriggerContext var15 = new PlayerTriggerContext(bukkitPlayer, focusLocation.clone(), var4.getBukkitEntity(), bukkitPlayer.getLocation().clone());
      this.method5134(bukkitPlayer, var15);
      var3.f
         .b(
            new PacketPlayOutEntityTeleport(
               var3.ao(), new PositionMoveRotation(new Vec3D(var4.dA(), var4.dC(), var4.dG()), Vec3D.c, var4.dL(), var4.dN()), Set.of(), false
            )
         );
      var3.f.b(new PacketPlayOutEntityLook(var4.ao(), MathHelper.g(var4.dL()), MathHelper.g(var4.dN()), false));
      var3.f.b(new PacketPlayOutEntityHeadRotation(var4, MathHelper.g(var4.dL())));
      var3.f.b(new PacketPlayOutCamera(var4));
      int[] var16 = new int[]{var3.ao()};
      PacketDataSerializer var17 = new PacketDataSerializer(Unpooled.buffer());
      var17.c(var4.ao());
      var17.a(var16);
      PacketPlayOutMount var18 = (PacketPlayOutMount)ae(var17);
      var3.f.b(var18);
      var3.f.b(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.e, EnumGamemode.d.a()));
      NmsChatService.method2951(bukkitPlayer, "Press SHIFT to exit");
      return true;
   }

   @Override
   protected void method5137(PlayerTriggerContext orbit) {
      Player var2 = orbit.Ph();
      EntityPlayer var3 = ((CraftPlayer)var2).getHandle();
      Entity var4 = ((CraftEntity)orbit.Pj()).getHandle();
      PacketPlayOutCamera var5 = new PacketPlayOutCamera(var3);
      var3.f.b(var5);
      this.method5148(var2, var4.ao());
      var3.f.b(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.e, var3.h.b().a()));
      var3.f.b(new ClientboundPlayerInfoUpdatePacket(a.c, var3));
      if (var2.getWorld().equals(orbit.Pk().getWorld())) {
         SchedulerUtils.teleportOnMainThread(var2, orbit.Pk());
      }
   }

   @Override
   public void method5147(Object nmsEntity, Player bukkitPlayer) {
      EntityPlayer var3 = ((CraftPlayer)bukkitPlayer).getHandle();
      Entity var4 = (Entity)nmsEntity;
      var3.f
         .b(
            new PacketPlayOutSpawnEntity(
               var4.ao(), var4.cG(), var4.du().a(), var4.du().b(), var4.du().c(), var4.dN(), var4.dL(), var4.an(), 0, var4.dy(), var4.cA()
            )
         );
      var3.f.b(new PacketPlayOutEntityMetadata(var4.ao(), var4.ar().c()));
   }

   @Override
   public void method5148(Player bukkitPlayer, int... entityIds) {
      EntityPlayer var3 = ((CraftPlayer)bukkitPlayer).getHandle();
      var3.f.b(new PacketPlayOutEntityDestroy(entityIds));
   }
}
