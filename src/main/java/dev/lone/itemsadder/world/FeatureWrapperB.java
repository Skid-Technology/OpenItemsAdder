package dev.lone.itemsadder.world;

import com.mojang.serialization.Codec;
import dev.lone.itemsadder.util.MarkerAnnotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jspecify.annotations.NonNull;

class FeatureWrapperB extends Feature {
   FeatureWrapperB(Codec var1, Feature var2) {
      super(var1);
      this.delegate = var2;
   }

   @MarkerAnnotation
   public boolean place(@NonNull FeaturePlaceContext context) {
      return this.delegate
         .place(
            (NoneFeatureConfiguration)context.config(),
            TreeFeatureHookImplL.wrapGeneratorForDefaultStates(context.level()),
            context.chunkGenerator(),
            context.random(),
            context.origin()
         );
   }
}
