package dev.lone.itemsadder.entity;

public interface PassengerRegistry {
   int getEntityId(Object var1);

   int[] getPassengerIds(Object var1);

   int[] getVehicleIds(Object var1);

   Iterable getPassengers(Object var1);

   boolean hasPassenger(Object var1);
}
