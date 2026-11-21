package dev.lone.itemsadder.network;

import dev.lone.itemsadder.util.Log;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.resources.Identifier;

public class RunnablePacketD implements Packet {
   private final Runnable bnN;
   private static final PacketType bnO = new PacketType(PacketFlow.SERVERBOUND, Identifier.fromNamespaceAndPath("itemsadder", "mining_stop_callback"));

   private RunnablePacketD(Runnable var1) {
      this.bnN = var1;
   }

   public PacketType type() {
      return bnO;
   }

   public void handle(PacketListener listener) {
      try {
         this.bnN.run();
      } catch (Exception | LinkageError var3) {
         Log.warn("Failed to finish native mining input", var3);
      }
   }

   public Runnable method16() {
      return this.bnN;
   }
}
