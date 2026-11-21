package dev.lone.itemsadder.particle;

import java.util.HashMap;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;

public class ParticleResolverImplA implements ParticleResolver {
   static final HashMap bmU = new HashMap();

   @Override
   public Particle fh(String name) {
      if (!name.contains(":") && !name.startsWith("minecraft:")) {
         name = "minecraft:" + name;
      }

      return (Particle)bmU.get(name);
   }

   static {
      for (Particle var3 : Particle.values()) {
         try {
            NamespacedKey var4 = var3.getKey();
            bmU.put(var4.getNamespace() + ":" + var4.getKey(), var3);
         } catch (UnsupportedOperationException var5) {
         }
      }

      bmU.put("minecraft:ambient_entity_effect", Particle.ENTITY_EFFECT);
   }
}
