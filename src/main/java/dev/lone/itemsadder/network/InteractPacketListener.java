package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.EntityInteractionAction;
import beer.devs.nativepackets.api.InteractEntityPacketView;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.SchedulerAccess;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.Citizens.CitizensHook;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.EntityViewerKey;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.util.Log;
import java.util.UUID;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public final class InteractPacketListener implements AutoCloseable {
   private final PacketListenerHandle UM;
   private volatile boolean cV = true;

   public InteractPacketListener() {
      this.UM = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.INTERACT_ENTITY),
            ListenerPriority.MONITOR,
            false,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var1 -> this.method1704(var1, (InteractEntityPacketView)var1.view(InteractEntityPacketView.class))
         );
   }

   private InteractPacketListener(PacketListenerHandle var1) {
      this.UM = var1;
   }

   private void method1704(PacketEvent var1, InteractEntityPacketView var2) {
      int var3 = var2.entityId();
      long var4 = var1.connection().id();
      UUID var6 = NativePacketsHolder.PE().method1883(var1);
      EntityViewerKey var7 = CustomEntityManager.getInstance().getViewerKey(var3);
      if (this.cV && var7 != null && var6 != null) {
         EntityInteractionEvent var8 = EntityInteractionEvent.method114(var6, var4, var7.uuid(), var2);

         try {
            NativePacketsHolder.PE().IP().entity(Main.getInstance(), var7.entity(), this.method1705(var8), () -> this.method1708(var8));
         } catch (RuntimeException | LinkageError var10) {
            Log.warn("Failed to schedule custom entity interaction", var10);
         }
      }
   }

   private Runnable method1705(EntityInteractionEvent var1) {
      return () -> {
         if (this.cV) {
            this.method1706(var1);
         }
      };
   }

   private void method1706(EntityInteractionEvent var1) {
      Player var2 = NativePacketsHolder.PE().method1885(var1.bk(), var1.bl());
      SchedulerAccess var3 = NativePacketsHolder.PE().IP();
      CustomEntityImpl var4 = az(var1.jI());
      LivingEntity var5 = var4 == null ? null : var4.getBukkitEntity();
      if (this.cV) {
         if (var2 == null) {
            Log.warn("Skipped custom entity interaction because the player connection changed: " + var1.bk());
         } else if (var5 != null) {
            if (var3.ownsEntity(var2) && var3.ownsEntity(var5)) {
               if (var2.isOnline() && var1.gp().equals(var5.getUniqueId()) && !CustomEntityManager.getInstance().isRiderOf(var2, var4) && !var2.equals(var5)) {
                  if (var1.vN() == EntityInteractionAction.ATTACK) {
                     PlayerInteractionFacade.getHandler().interactEntity(var2, var5);
                  } else {
                     method1707(var2, var5, var1);
                     if (!Main.aFc || !CitizensHook.isNPC(var5)) {
                        if (var4.qK.vH
                           && var4.hasRiders()
                           && !var2.isSneaking()
                           && !CustomEntityManager.getInstance().isRidden(var2)
                           && !CustomEntityManager.getInstance().mountOnEntity(var2, var4)) {
                           Log.sendLocalized(var2, "all-mounts-occupied");
                        }
                     }
                  }
               }
            } else {
               Log.warn("Skipped custom entity interaction outside shared entity ownership: " + var1.gp());
            }
         }
      }
   }

   private static void method1707(Player var0, LivingEntity var1, EntityInteractionEvent var2) {
      if (var2.vP()) {
         PlayerInteractionFacade.getHandler().interactEntityAt(var0, var1, new Vector(var2.ka(), var2.kb(), var2.kc()), var2.vO());
      } else {
         PlayerInteractionFacade.getHandler().interactEntityWithSlotDup2(var0, var1, var2.vO());
      }
   }

   private static CustomEntityImpl az(int var0) {
      CustomEntityImpl var1 = CustomEntityManager.getInstance().getLifecycleById(var0);
      if (var1 != null && var1.getBukkitEntity() != null && (var1.qR || var0 == var1.getInteractionEntityId()) && !var1.qQ) {
         return var1;
      }

      var1 = CustomEntityManager.getInstance().getById(var0);
      return var1 != null && var1.getBukkitEntity() != null && !var1.qQ ? var1 : null;
   }

   private void method1708(EntityInteractionEvent var1) {
      if (this.cV) {
         Log.warn("Skipped custom entity interaction because target ownership retired: " + var1.gp());
      }
   }

   @Override
   public void close() {
      this.cV = false;
      NativePacketsHolder.PE().method1889(Main.getInstance(), this.UM);
   }
}
