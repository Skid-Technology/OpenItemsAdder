package dev.lone.itemsadder.api;

import dev.lone.itemsadder.core.ContentManager;
import java.util.Collection;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public final class CustomParticle {
   private CustomParticle() {
   }

   public static AutoCloseable play(String namespacedId, Location origin, Collection viewers, int seed) {
      return ContentManager.method8084().field86.method2151(namespacedId, origin, viewers, seed);
   }

   public static AutoCloseable playAttached(String namespacedId, Entity parent, Collection viewers, int seed) {
      return ContentManager.method8084().field86.method2152(namespacedId, parent, viewers, seed);
   }
}
