package dev.lone.itemsadder.nms;

import beer.devs.nativepackets.api.ContainerClickPacketView;

public interface ContainerClickAdapter extends InventoryAdapter {
   default int method3555(ContainerClickPacketView packet) {
      return packet.stateId();
   }

   default int method3556(ContainerClickPacketView packet) {
      return packet.slot();
   }

   default int method3557(ContainerClickPacketView packet) {
      return packet.button();
   }

   long method3558(Object var1, int var2);

   long method3559(Object var1, int var2);

   Iterable method3560(Object var1);

   Object OQ();

   Object method3561(Object var1);

   Object ab(Object var1);

   boolean ac(Object var1);

   Object method3562(Object var1, int var2);

   Object method3563(Object var1, float var2, int var3, boolean var4, String var5, boolean var6);
}
