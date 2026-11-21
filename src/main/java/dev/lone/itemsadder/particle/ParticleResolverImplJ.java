package dev.lone.itemsadder.particle;

import java.util.HashMap;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;

public class ParticleResolverImplJ implements ParticleResolver {
   static final HashMap bnd = new HashMap();

   @Override
   public Particle fh(String name) {
      if (!name.contains(":") && !name.startsWith("minecraft:")) {
         name = "minecraft:" + name;
      }

      return (Particle)bnd.get(name);
   }

   static {
      for (Particle var3 : Particle.values()) {
         try {
            NamespacedKey var4 = var3.getKey();
            bnd.put(var4.getNamespace() + ":" + var4.getKey(), var3);
         } catch (UnsupportedOperationException var5) {
         }
      }

      bnd.put("minecraft:ambient_entity_effect", Particle.ENTITY_EFFECT);
   }
}
