package dev.lone.itemsadder.network;

import dev.lone.itemsadder.util.Log;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.resources.Identifier;

public class RunnablePacketC implements Packet {
   private final Runnable bnJ;
   private static final PacketType bnK = new PacketType(PacketFlow.SERVERBOUND, Identifier.fromNamespaceAndPath("itemsadder", "mining_stop_callback"));

   private RunnablePacketC(Runnable var1) {
      this.bnJ = var1;
   }

   public PacketType type() {
      return bnK;
   }

   public void handle(PacketListener listener) {
      try {
         this.bnJ.run();
      } catch (Exception | LinkageError var3) {
         Log.warn("Failed to finish native mining input", var3);
      }
   }

   public Runnable method16() {
      return this.bnJ;
   }
}
