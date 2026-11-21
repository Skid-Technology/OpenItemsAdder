package dev.lone.itemsadder.network;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.PacketFlow;

public class PacketListenerStubE implements PacketListener {
   public PacketFlow flow() {
      return PacketFlow.SERVERBOUND;
   }

   public ConnectionProtocol protocol() {
      return ConnectionProtocol.PLAY;
   }

   public void onDisconnect(DisconnectionDetails details) {
   }

   public boolean isAcceptingMessages() {
      return true;
   }
}
