package dev.lone.itemsadder.network;

import dev.lone.itemsadder.util.Log;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.resources.Identifier;

public class RunnablePacketF implements Packet {
   private final Runnable bnF;
   private static final PacketType bnG = new PacketType(PacketFlow.SERVERBOUND, Identifier.fromNamespaceAndPath("itemsadder", "mining_stop_callback"));

   private RunnablePacketF(Runnable var1) {
      this.bnF = var1;
   }

   public PacketType type() {
      return bnG;
   }

   public void handle(PacketListener listener) {
      try {
         this.bnF.run();
      } catch (Exception | LinkageError var3) {
         Log.warn("Failed to finish native mining input", var3);
      }
   }

   public Runnable method16() {
      return this.bnF;
   }
}
