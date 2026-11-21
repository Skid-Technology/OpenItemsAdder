package dev.lone.itemsadder.network;

import dev.lone.itemsadder.util.Log;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.resources.MinecraftKey;

public class RunnablePacketB implements Packet {
   private final Runnable bnv;
   private static final PacketType bnw = new PacketType(EnumProtocolDirection.a, MinecraftKey.a("itemsadder", "mining_stop_callback"));

   private RunnablePacketB(Runnable var1) {
      this.bnv = var1;
   }

   public PacketType method14() {
      return bnw;
   }

   public void method15(PacketListener listener) {
      try {
         this.bnv.run();
      } catch (Exception | LinkageError var3) {
         Log.warn("Failed to finish native mining input", var3);
      }
   }

   public Runnable method16() {
      return this.bnv;
   }
}
