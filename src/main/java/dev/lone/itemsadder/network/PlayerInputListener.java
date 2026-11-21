package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.PlayerInputPacketView;
import beer.devs.nativepackets.api.SchedulerAccess;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.CustomMobSettings;
import dev.lone.itemsadder.entity.ModelRiderController;
import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import java.util.UUID;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public final class PlayerInputListener implements AutoCloseable {
   private final PacketListenerHandle Vc;
   private volatile boolean cV = true;

   PlayerInputListener() {
      this.Vc = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.PLAYER_INPUT),
            ListenerPriority.MONITOR,
            false,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var1 -> this.method1829(var1, (PlayerInputPacketView)var1.view(PlayerInputPacketView.class))
         );
   }

   private void method1829(PacketEvent var1, PlayerInputPacketView var2) {
      UUID var3 = NativePacketsHolder.PE().method1883(var1);
      if (this.cV && var3 != null) {
         PlayerInputState var4 = new PlayerInputState(
            var3, var1.connection().id(), var2.jumping(), var2.dismounting(), var2.sideways(), var2.forward(), System.nanoTime()
         );
         Player var5 = NativePacketsHolder.PE().method1885(var3, var4.bl());
         if (var5 != null) {
            try {
               NativePacketsHolder.PE().IP().entity(Main.getInstance(), var5, () -> this.method1830(var4), () -> {});
            } catch (RuntimeException | LinkageError var7) {
            }
         }
      }
   }

   private void method1830(PlayerInputState var1) {
      Player var2 = NativePacketsHolder.PE().method1885(var1.bk(), var1.bl());
      SchedulerAccess var3 = NativePacketsHolder.PE().IP();
      if (this.cV && var2 != null && var3.ownsEntity(var2)) {
         Entity var4 = var2.getVehicle();
         if (var4 == null || var4 instanceof ArmorStand || var4 instanceof ItemDisplay) {
            ModelRiderController var5 = CustomEntityManager.getInstance().getRiderOf(var2);
            LivingEntity var6 = var5 == null ? null : var5.xo;
            if (var6 != null && var3.ownsEntity(var6) && var2.isOnline() && var6.isValid() && var6.getWorld() == var2.getWorld()) {
               CustomMobSettings var7 = var5.getCustomEntity().qK;
               boolean var8 = var7.getFloatValue() > 0.0F;
               boolean var9 = var8 && var7.vL;
               boolean var10 = var8 && (!var9 || var6.isInWater());
               boolean var11 = var1.vR();
               if (var9 && var5.handleThrowInput(var11, var1.vU())) {
                  CustomEntityManager.getInstance().removePassenger(var2);
               } else {
                  if (var5.canControl()) {
                     Vector var12 = var6.getVelocity();
                     boolean var13 = var1.kp();
                     if (var13 && var6.isOnGround() && !var9) {
                        var12.add(new Vector(0.0, 0.5, 0.0));
                     } else if (var13 && var10) {
                        var12.add(new Vector(0.0, 0.1, 0.0));
                     }

                     if (var11) {
                        if (!var8) {
                           if (!var5.isLocked()) {
                              CustomEntityManager.getInstance().removePassenger(var2);
                           }
                        } else if (var10) {
                           var12.add(new Vector(0.0, -0.05, 0.0));
                        }
                     }

                     float var14 = var7.vN
                        .method67(
                           ProjectileTypeHelper.getAdapter().method3235(var6),
                           var2.getEyeLocation().getYaw(),
                           var1.vS(),
                           var1.vS() != 0.0F || var1.vT() != 0.0F
                        );
                     double var15 = var10 && !var6.isOnGround() ? var7.getFloatValue() : var7.getDoubleValue();
                     Vector var17 = var7.vN.method68(var14, var15, var1.vT(), var1.vS());
                     var12.setX(var17.getX()).setZ(var17.getZ());
                     if (var9 && var10) {
                        var12.setY(var11 ? -var7.getFloatValue() : (var13 ? var7.getFloatValue() : 0.0F));
                     }

                     var6.setVelocity(var12);
                     var6.setRotation(var14, var6.getEyeLocation().getPitch());
                  } else if (var11 && !var8 && !var5.isLocked()) {
                     CustomEntityManager.getInstance().removePassenger(var2);
                  }
               }
            }
         }
      }
   }

   @Override
   public void close() {
      this.cV = false;
      NativePacketsHolder.PE().method1889(Main.getInstance(), this.Vc);
   }
}
