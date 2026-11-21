package dev.lone.itemsadder.particle;

import java.util.List;

@FunctionalInterface
interface ObstacleProvider {
   List at(ParticleBounds var1);
}
