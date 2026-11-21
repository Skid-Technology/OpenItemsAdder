package dev.lone.itemsadder.world;

import com.mojang.serialization.MapCodec;
import dev.lone.itemsadder.util.MarkerAnnotation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.jspecify.annotations.NonNull;

class FeatureWrapperD implements Feature {
   FeatureWrapperD(Feature var1) {
      this.delegate = var1;
   }

   @MarkerAnnotation
   public MapCodec codec() {
      return this.delegate.codec();
   }

   @MarkerAnnotation
   public boolean place(@NonNull WorldGenLevel level, @NonNull ChunkGenerator generator, @NonNull RandomSource random, @NonNull BlockPos origin) {
      return this.delegate.place(TreeFeatureHookImplN.wrapGeneratorForDefaultStates(level), generator, random, origin);
   }
}
