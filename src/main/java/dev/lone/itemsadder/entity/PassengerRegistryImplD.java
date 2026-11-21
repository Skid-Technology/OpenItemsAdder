package dev.lone.itemsadder.entity;

import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutLogin;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutRespawn;

class PassengerRegistryImplD implements PassengerRegistry {
   @Override
   public int getEntityId(Object packet) {
      return packet instanceof PacketPlayOutMount var2 ? var2.e() : -1;
   }

   @Override
   public int[] getPassengerIds(Object packet) {
      return packet instanceof PacketPlayOutMount var2 ? var2.b() : null;
   }

   @Override
   public int[] getVehicleIds(Object packet) {
      return packet instanceof PacketPlayOutEntityDestroy var2 ? var2.b().toIntArray() : null;
   }

   @Override
   public Iterable getPassengers(Object packet) {
      return packet instanceof ClientboundBundlePacket var2 ? var2.b() : null;
   }

   @Override
   public boolean hasPassenger(Object packet) {
      return packet instanceof PacketPlayOutRespawn || packet instanceof PacketPlayOutLogin;
   }
}
