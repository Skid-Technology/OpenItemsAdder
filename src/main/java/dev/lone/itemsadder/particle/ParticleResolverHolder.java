package dev.lone.itemsadder.particle;

import dev.lone.itemsadder.nms.NmsRegistry;

public final class ParticleResolverHolder {
   private final ParticleResolver bmS = (ParticleResolver)NmsRegistry.method2777(ParticleResolver.class);
   private static ParticleResolverHolder bmT;

   ParticleResolverHolder() {
   }

   private static ParticleResolverHolder PK() {
      if (bmT == null) {
         bmT = new ParticleResolverHolder();
      }

      return bmT;
   }

   public static ParticleResolver PL() {
      return PK().bmS;
   }
}
