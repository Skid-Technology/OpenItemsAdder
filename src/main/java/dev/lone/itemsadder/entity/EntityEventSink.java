package dev.lone.itemsadder.entity;

import org.bukkit.entity.Entity;

public interface EntityEventSink {
   void onEntityEvent(Entity var1, int var2);
}
