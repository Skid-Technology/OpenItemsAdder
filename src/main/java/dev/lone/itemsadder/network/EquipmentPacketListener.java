package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.EntityEquipmentPacketView;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.NativeEquipment;
import beer.devs.nativepackets.api.NativeEquipmentSlot;
import beer.devs.nativepackets.api.NativeItem;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.SetSlotPacketView;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.entity.CustomEntityManager;
import java.util.List;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class EquipmentPacketListener implements AutoCloseable {
   private final PacketListenerHandle UI;
   private final PacketListenerHandle UJ;
   private final NativeItem UK;
   private final List UL;
   private volatile boolean cV = true;

   EquipmentPacketListener() {
      this.UK = NativePacketsHolder.PE().Py().item(new ItemStack(Material.AIR));
      this.UL = List.of(
         new NativeEquipment(NativeEquipmentSlot.HEAD, this.UK),
         new NativeEquipment(NativeEquipmentSlot.CHEST, this.UK),
         new NativeEquipment(NativeEquipmentSlot.LEGS, this.UK),
         new NativeEquipment(NativeEquipmentSlot.FEET, this.UK),
         new NativeEquipment(NativeEquipmentSlot.MAIN_HAND, this.UK),
         new NativeEquipment(NativeEquipmentSlot.OFF_HAND, this.UK)
      );
      this.UI = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.ENTITY_EQUIPMENT),
            ListenerPriority.HIGHEST,
            false,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var1 -> this.method1698(var1, (EntityEquipmentPacketView)var1.view(EntityEquipmentPacketView.class))
         );
      this.UJ = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.SET_SLOT),
            ListenerPriority.HIGHEST,
            false,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var1 -> this.method1699(var1, (SetSlotPacketView)var1.view(SetSlotPacketView.class))
         );
   }

   private void method1698(PacketEvent var1, EntityEquipmentPacketView var2) {
      if (this.cV && !var1.isCancelled() && (CustomEntityManager.getInstance().hasEntityId(var2.entityId()) || EntityMetadataListener.ct(var2.entityId()))) {
         var1.replaceNativePacket(NativePacketsHolder.PE().Pv().equipment(var2.entityId(), this.UL));
      }
   }

   private void method1699(PacketEvent var1, SetSlotPacketView var2) {
      if (this.cV && !var1.isCancelled()) {
         int var3 = var2.slot();
         if (ax(var3)) {
            UUID var4 = NativePacketsHolder.PE().method1883(var1);
            if (var4 != null && CustomEntityManager.getInstance().method8313(var4)) {
               long var5 = var1.connection().id();
               var1.sync(
                  (var5x, var6) -> {
                     if (this.cV
                        && var6.player() instanceof Player var7
                        && var7.isOnline()
                        && NativePacketsHolder.PE().method1885(var4, var5) == var7
                        && method1700(var3, var7.getInventory().getHeldItemSlot())
                        && CustomEntityManager.getInstance().method8312(var7) != null) {
                        ((SetSlotPacketView)var5x.view(SetSlotPacketView.class)).item(this.UK);
                     }
                  }
               );
            }
         }
      }
   }

   static boolean method1700(int var0, int var1) {
      return ay(var0) || var1 == var0;
   }

   private static boolean ax(int var0) {
      return var0 >= 0 && var0 <= 8 || var0 == 45;
   }

   private static boolean ay(int var0) {
      return var0 >= 5 && var0 <= 8 || var0 == 45;
   }

   @Override
   public void close() {
      this.cV = false;
      NativePacketsHolder.PE().method1889(Main.getInstance(), this.UI);
      NativePacketsHolder.PE().method1889(Main.getInstance(), this.UJ);
   }
}
