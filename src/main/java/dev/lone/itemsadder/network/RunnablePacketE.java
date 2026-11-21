package dev.lone.itemsadder.network;

import dev.lone.itemsadder.util.Log;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.resources.Identifier;

public class RunnablePacketE implements Packet {
   private final Runnable bnR;
   private static final PacketType bnS = new PacketType(PacketFlow.SERVERBOUND, Identifier.fromNamespaceAndPath("itemsadder", "mining_stop_callback"));

   private RunnablePacketE(Runnable var1) {
      this.bnR = var1;
   }

   public PacketType type() {
      return bnS;
   }

   public void handle(PacketListener listener) {
      try {
         this.bnR.run();
      } catch (Exception | LinkageError var3) {
         Log.warn("Failed to finish native mining input", var3);
      }
   }

   public Runnable method16() {
      return this.bnR;
   }
}
