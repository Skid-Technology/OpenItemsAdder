package dev.lone.itemsadder.network;

import dev.lone.itemsadder.util.Log;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.resources.MinecraftKey;

class RunnablePacketA implements Packet {
   private final Runnable bnr;
   private static final PacketType bns = new PacketType(EnumProtocolDirection.a, MinecraftKey.a("itemsadder", "mining_stop_callback"));

   private RunnablePacketA(Runnable var1) {
      this.bnr = var1;
   }

   public PacketType method14() {
      return bns;
   }

   public void method15(PacketListener listener) {
      try {
         this.bnr.run();
      } catch (Exception | LinkageError var3) {
         Log.warn("Failed to finish native mining input", var3);
      }
   }

   public Runnable method16() {
      return this.bnr;
   }
}
