package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.EntityMetadataPacketView;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.NativeEquipmentSlot;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.EntityMetadataFields.Entity;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import dev.lone.itemsadder.nms.NmsEntityAdapter;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class EntityMetadataListener implements Listener {
   private static EntityMetadataListener aNm;
   private static final Set aNn = ConcurrentHashMap.newKeySet();
   private static final ConcurrentMap aNo = new ConcurrentHashMap();
   private volatile PacketListenerHandle aNp;
   private volatile long bG;

   public static EntityMetadataListener Ng() {
      if (aNm == null) {
         aNm = new EntityMetadataListener();
      }

      return aNm;
   }

   public static boolean ct(int var0) {
      return aNo.containsKey(var0);
   }

   private synchronized void register() {
      if (this.aNp == null) {
         EventRegistrar.registerListener(this, Main.getInstance());
         long var1 = ++this.bG;
         this.aNp = NativePacketsHolder.PE()
            .method1888(
               this,
               PacketSelector.exact(PacketTypes.ENTITY_METADATA),
               ListenerPriority.NORMAL,
               false,
               ListenerFailurePolicy.PASS_ORIGINAL,
               var3 -> this.method2781(var3, (EntityMetadataPacketView)var3.view(EntityMetadataPacketView.class), var1)
            );
      }
   }

   private void method2781(PacketEvent var1, EntityMetadataPacketView var2, long var3) {
      if (var3 == this.bG && !var1.isCancelled() && NativePacketsHolder.PE().method1883(var1) != null) {
         int var5 = var2.entityId();
         Byte var6 = (Byte)aNo.get(var5);
         if (var6 != null) {
            byte var7 = method2782(var2, var6);
            aNo.replace(var5, var6, var7);
         }
      }
   }

   static byte method2782(EntityMetadataPacketView var0, byte var1) {
      if (var0.entryCount() == 0) {
         return var1;
      }

      Byte var2 = (Byte)var0.get(Entity.SHARED_FLAGS);
      byte var3 = NmsEntityAdapter.method3240(var2 == null ? var1 : var2, true);
      if (var2 != null && var2 == var3) {
         return var3;
      }

      var0.set(Entity.SHARED_FLAGS, var3);
      return var3;
   }

   private synchronized void unregister() {
      PacketListenerHandle var1 = this.aNp;
      if (var1 != null) {
         this.aNp = null;
         this.bG++;
         NativePacketsHolder.PE().method1889(this, var1);
         EventRegistrar.unregisterListener(this);
      }
   }

   public static void aM() {
      if (aNm != null) {
         aNm.Co();
      }
   }

   public void cf(Player var1) {
      this.register();
      aNn.add(var1);
      aNo.put(var1.getEntityId(), ProjectileTypeHelper.getAdapter().method3266(var1, true));
      ProjectileTypeHelper.getAdapter().cq(var1);
      ProjectileTypeHelper.getAdapter().method3267(var1, true);
   }

   public void cg(Player var1) {
      aNn.remove(var1);
      aNo.remove(var1.getEntityId());
      ProjectileTypeHelper.getAdapter().method3267(var1, false);
      ProjectileTypeHelper.getAdapter().cr(var1);
   }

   public void Co() {
      for (Player var2 : List.copyOf(aNn)) {
         this.cg(var2);
      }

      aNn.clear();
      aNo.clear();
      this.unregister();
   }

   @EventHandler
   private void method2783(PlayerJoinEvent var1) {
      for (Player var3 : aNn) {
         aNo.put(var3.getEntityId(), ProjectileTypeHelper.getAdapter().method3266(var3, true));
         ProjectileTypeHelper.getAdapter().method3267(var3, true);
         ProjectileTypeHelper.getAdapter().cq(var3);
      }
   }

   @EventHandler
   private void method2784(PlayerQuitEvent var1) {
      Player var2 = var1.getPlayer();
      aNn.remove(var2);
      aNo.remove(var2.getEntityId());
   }

   @EventHandler
   private void method2785(PlayerItemHeldEvent var1) {
      this.ch(var1.getPlayer());
   }

   @EventHandler
   private void method2786(PlayerSwapHandItemsEvent var1) {
      this.ch(var1.getPlayer());
   }

   private void ch(Player var1) {
      if (aNn.contains(var1)) {
         SchedulerUtils.runLaterSync(() -> {
            PlayerInteractionFacade.method5313(var1, false);
            ci(var1);
         }, 2L);
      }
   }

   private static void ci(Player var0) {
      method2787(var0, NativeEquipmentSlot.MAIN_HAND);
      method2787(var0, NativeEquipmentSlot.OFF_HAND);
   }

   private static void method2787(Player var0, NativeEquipmentSlot var1) {
      PlayerInteractionFacade.method5320(var0, var1, null);
   }
}
