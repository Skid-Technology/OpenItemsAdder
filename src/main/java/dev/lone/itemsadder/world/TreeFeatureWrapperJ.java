package dev.lone.itemsadder.world;

import com.mojang.serialization.Codec;
import dev.lone.itemsadder.util.MarkerAnnotation;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureEmptyConfiguration;
import org.jspecify.annotations.NonNull;

class TreeFeatureWrapperJ extends WorldGenerator {
   TreeFeatureWrapperJ(Codec var1, WorldGenerator var2) {
      super(var1);
      this.delegate = var2;
   }

   @MarkerAnnotation
   public boolean placeWithDefaultStates(@NonNull FeaturePlaceContext context) {
      return this.delegate
         .a(
            (WorldGenFeatureEmptyConfiguration)context.f(),
            TreeFeatureHookImplJ.wrapGeneratorForDefaultStates(context.b()),
            context.c(),
            context.d(),
            context.e()
         );
   }
}
