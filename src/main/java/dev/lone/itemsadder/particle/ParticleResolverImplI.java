package dev.lone.itemsadder.particle;

import java.util.HashMap;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;

public class ParticleResolverImplI implements ParticleResolver {
   static final HashMap bnc = new HashMap();

   @Override
   public Particle fh(String name) {
      if (!name.contains(":") && !name.startsWith("minecraft:")) {
         name = "minecraft:" + name;
      }

      return (Particle)bnc.get(name);
   }

   static {
      for (Particle var3 : Particle.values()) {
         try {
            NamespacedKey var4 = var3.getKey();
            bnc.put(var4.getNamespace() + ":" + var4.getKey(), var3);
         } catch (UnsupportedOperationException var5) {
         }
      }

      bnc.put("minecraft:ambient_entity_effect", Particle.ENTITY_EFFECT);
   }
}
