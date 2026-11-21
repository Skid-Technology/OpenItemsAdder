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
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketPlayerTriggerA extends AbstractPlayerTrigger {
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
         .method1888(this.Pg(), PacketSelector.exact(PacketTypes.PLAYER_LOOK), ListenerPriority.NORMAL, true, ListenerFailurePolicy.PASS_ORIGINAL, var3 -> {
            PlayerLookPacketView var4 = (PlayerLookPacketView)var3.view(PlayerLookPacketView.class);
            UUID var5 = NativePacketsHolder.PE().method1883(var3);
            if (this.method5135(var5, var1)) {
               PlayerTriggerKey var6 = new PlayerTriggerKey(var5, var3.connection().id(), var4.yaw(), var4.pitch(), var1);
               var3.cancel();
               var3.sync((var2, var3x) -> {
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
                           var12.r(var6x);
                           var12.s(var7);
                           Vec3D var16 = new Vec3D(var4x.Pi().getX(), var4x.Pi().getY(), var4x.Pi().getZ());
                           double var17 = var16.c - var12.du();
                           double var19 = var16.e - var12.dA();
                           double var21 = var16.d - (var12.dw() + var12.cL());
                           float var14 = (float)(Math.toDegrees(Math.atan2(var19, var17)) - 90.0);
                           float var15 = (float)(-Math.toDegrees(Math.atan2(var21, Math.sqrt(var17 * var17 + var19 * var19))));
                           EntityPlayer var23 = ((CraftPlayer)var5x).getHandle();
                           var23.c.b(new PacketPlayOutEntityTeleport(var12));
                           var23.c.b(new PacketPlayOutEntityLook(var12.al(), method5149(var14), method5149(var15), false));
                           var23.c.b(new PacketPlayOutEntityHeadRotation(var12, method5149(var14)));
                        }
                     }
                  }
               });
            }
         });
   }

   public static byte method5149(float var0) {
      return (byte)MathHelper.d(var0 * 256.0F / 360.0F);
   }

   @Override
   public boolean method5132(Player bukkitPlayer, Location focusLocation) {
      if (cz(bukkitPlayer)) {
         return false;
      }

      EntityPlayer var3 = ((CraftPlayer)bukkitPlayer).getHandle();
      EntityZombie var4 = new EntityZombie(EntityTypes.bu, var3.dP());
      Location var5 = bukkitPlayer.getLocation().add(bukkitPlayer.getLocation().getDirection().normalize().multiply(-3.0));
      var4.p(var5.getX(), var5.getY(), var5.getZ());
      var4.f(true);
      var4.n(true);
      var4.k(true);
      Vec3D var6 = new Vec3D(focusLocation.getX(), focusLocation.getY(), focusLocation.getZ());
      double var7 = var6.c - var4.du();
      double var9 = var6.e - var4.dA();
      double var11 = var6.d - (var4.dw() + var4.cL());
      float var13 = (float)(Math.toDegrees(Math.atan2(var9, var7)) - 90.0);
      float var14 = (float)(-Math.toDegrees(Math.atan2(var11, Math.sqrt(var7 * var7 + var9 * var9))));
      var4.r(var13);
      var4.s(var14);
      var4.n(var13);
      var4.a(Anchor.b, var6);
      this.method5147(var4, bukkitPlayer);
      PlayerTriggerContext var15 = new PlayerTriggerContext(bukkitPlayer, focusLocation.clone(), var4.getBukkitEntity(), bukkitPlayer.getLocation().clone());
      this.method5134(bukkitPlayer, var15);
      var3.c.b(new PacketPlayOutEntityTeleport(var4));
      var3.c.b(new PacketPlayOutEntityLook(var4.al(), method5149(var4.dF()), method5149(var4.dH()), false));
      var3.c.b(new PacketPlayOutEntityHeadRotation(var4, method5149(var4.dF())));
      var3.c.b(new PacketPlayOutCamera(var4));
      int[] var16 = new int[]{var3.al()};
      PacketDataSerializer var17 = new PacketDataSerializer(Unpooled.buffer());
      var17.c(var4.al());
      var17.a(var16);
      PacketPlayOutMount var18 = (PacketPlayOutMount)ae(var17);
      var3.c.b(var18);
      var3.c.b(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.e, EnumGamemode.d.a()));
      NmsChatService.method2951(bukkitPlayer, "Press SHIFT to exit");
      return true;
   }

   @Override
   protected void method5137(PlayerTriggerContext orbit) {
      Player var2 = orbit.Ph();
      EntityPlayer var3 = ((CraftPlayer)var2).getHandle();
      Entity var4 = ((CraftEntity)orbit.Pj()).getHandle();
      PacketPlayOutCamera var5 = new PacketPlayOutCamera(var3);
      var3.c.b(var5);
      this.method5148(var2, var4.al());
      var3.c.b(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.e, var3.e.b().a()));
      var3.c.b(new ClientboundPlayerInfoUpdatePacket(a.c, var3));
      if (var2.getWorld().equals(orbit.Pk().getWorld())) {
         SchedulerUtils.teleportOnMainThread(var2, orbit.Pk());
      }
   }

   @Override
   public void method5147(Object nmsEntity, Player bukkitPlayer) {
      EntityPlayer var3 = ((CraftPlayer)bukkitPlayer).getHandle();
      Entity var4 = (Entity)nmsEntity;
      var3.c
         .b(
            new PacketPlayOutSpawnEntity(
               var4.al(), var4.cz(), var4.do_().a(), var4.do_().b(), var4.do_().c(), var4.dH(), var4.dF(), var4.ak(), 0, var4.ds(), var4.cs()
            )
         );
      var3.c.b(new PacketPlayOutEntityMetadata(var4.al(), var4.ap().c()));
   }

   @Override
   public void method5148(Player bukkitPlayer, int... entityIds) {
      EntityPlayer var3 = ((CraftPlayer)bukkitPlayer).getHandle();
      var3.c.b(new PacketPlayOutEntityDestroy(entityIds));
   }
}
