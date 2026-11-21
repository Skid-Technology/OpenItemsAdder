package dev.lone.itemsadder.entity;

import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;

class PassengerRegistryImplK implements PassengerRegistry {
   @Override
   public int getEntityId(Object packet) {
      return packet instanceof ClientboundSetPassengersPacket var2 ? var2.getVehicle() : -1;
   }

   @Override
   public int[] getPassengerIds(Object packet) {
      return packet instanceof ClientboundSetPassengersPacket var2 ? var2.getPassengers() : null;
   }

   @Override
   public int[] getVehicleIds(Object packet) {
      return packet instanceof ClientboundRemoveEntitiesPacket var2 ? var2.getEntityIds().toIntArray() : null;
   }

   @Override
   public Iterable getPassengers(Object packet) {
      return packet instanceof ClientboundBundlePacket var2 ? var2.subPackets() : null;
   }

   @Override
   public boolean hasPassenger(Object packet) {
      return packet instanceof ClientboundRespawnPacket || packet instanceof ClientboundLoginPacket;
   }
}
