package dev.lone.itemsadder.world;

import dev.lone.itemsadder.util.BlockDataSetter;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.ServerCompat;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.RegistryAccess.Frozen;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R7.util.BlockStateListPopulator;

public class TreeFeatureHookImplL implements TreeFeatureHook {
   private final ReplaceableRegistry replaceableRegistry = new ReplaceableRegistry();
   RandomSource random = RandomSource.create();
   List invalidBaseBlocks = Arrays.asList(
      Blocks.SHORT_GRASS,
      Blocks.FERN,
      Blocks.DEAD_BUSH,
      Blocks.VINE,
      Blocks.GLOW_LICHEN,
      Blocks.SUNFLOWER,
      Blocks.LILAC,
      Blocks.ROSE_BUSH,
      Blocks.PEONY,
      Blocks.TALL_GRASS,
      Blocks.LARGE_FERN,
      Blocks.HANGING_ROOTS,
      Blocks.PITCHER_PLANT,
      Blocks.SEAGRASS
   );

   @Override
   public boolean growTree(Location loc, TreeType type, BlockDataSetter delegate, boolean highestBlock) {
      CraftWorld var5 = (CraftWorld)loc.getWorld();
      ServerLevel var6 = var5.getHandle();
      LevelChunk var7 = var6.getChunk(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
      BlockPos var8 = new BlockPos(
         loc.getBlockX(),
         highestBlock ? InstrumentRegistry.getHeightAt(loc.getChunk(), loc.getBlockX() >> 4, loc.getBlockZ() >> 4, HeightMap.WORLD_SURFACE) : loc.getBlockY(),
         loc.getBlockZ()
      );
      BlockState var9 = var7.getBlockState(var8);
      if (var9.liquid()) {
         return false;
      }

      BlockPos var10 = new BlockPos(var8.getX(), var8.getY() - 1, var8.getZ());
      BlockState var11 = var7.getBlockState(var10);
      if (!var11.liquid() && !var11.isAir() && !var11.is(BlockTags.LOGS) && !var11.is(BlockTags.LEAVES) && !this.invalidBaseBlocks.contains(var11.getBlock())) {
         var7.setBlockState(var10, Blocks.GRASS_BLOCK.defaultBlockState(), 0);
         BlockStateListPopulator var12 = new BlockStateListPopulator(var6);
         boolean var13 = var5.generateTree(var12, var6.getChunkSource().getGenerator(), var8, this.random, type);
         if (var13) {
            for (org.bukkit.block.BlockState var15 : var12.getSnapshotBlocks()) {
               delegate.setBlockData(var15.getBlock(), var15.getBlockData());
            }
         }

         var7.setBlockState(var10, var11, 0);
         return var13;
      } else {
         return false;
      }
   }

   @Override
   public void injectDefaultBlockstateFeatures() {
      if (TreeFeatureHookHolder.featuresInjected.compareAndSet(false, true)) {
         try {
            Frozen var1 = MinecraftServer.getServer().registryAccess();
            MappedRegistry var2 = (MappedRegistry)var1.lookupOrThrow(Registries.CONFIGURED_FEATURE);
            this.wrapChorusPlantFeature(var2);
            this.injectMushroomFeature(var2, "huge_brown_mushroom", Feature.HUGE_BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState());
            this.injectMushroomFeature(var2, "huge_red_mushroom", Feature.HUGE_RED_MUSHROOM, Blocks.RED_MUSHROOM_BLOCK.defaultBlockState());
         } catch (Throwable var3) {
            this.restoreDefaultBlockstateFeatures();
            Log.warn("Failed to inject force-default-blockstate tree features: " + var3.getMessage());
         }
      }
   }

   private void wrapChorusPlantFeature(MappedRegistry var1) {
      ResourceKey var2 = minecraftFeatureKey("chorus_plant");
      this.replaceFeature(var1, var2, wrapWithForcedDefaultState((ConfiguredFeature)var1.getValueOrThrow(var2)));
   }

   private void injectMushroomFeature(MappedRegistry var1, String var2, Feature var3, BlockState var4) {
      ResourceKey var5 = minecraftFeatureKey(var2);
      HugeMushroomFeatureConfiguration var6 = (HugeMushroomFeatureConfiguration)((ConfiguredFeature)var1.getValueOrThrow(var5)).config();
      HugeMushroomFeatureConfiguration var7 = new HugeMushroomFeatureConfiguration(
         BlockStateProvider.simple(var4), BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState()), var6.foliageRadius(), var6.canPlaceOn()
      );
      this.replaceFeature(var1, var5, new ConfiguredFeature(var3, var7));
   }

   @Override
   public void restoreDefaultBlockstateFeatures() {
      try {
         Frozen var1 = MinecraftServer.getServer().registryAccess();
         MappedRegistry var2 = (MappedRegistry)var1.lookupOrThrow(Registries.CONFIGURED_FEATURE);
         this.replaceableRegistry.restoreAll(var2::getValueOrThrow, (var1x, var2x) -> replaceRegistryEntry(var2, var1x, var2x));
      } catch (Throwable var6) {
         Log.warn("Failed to restore force-default-blockstate tree features: " + var6.getMessage());
      } finally {
         this.replaceableRegistry.clear();
         TreeFeatureHookHolder.featuresInjected.set(false);
      }
   }

   private void replaceFeature(MappedRegistry var1, ResourceKey var2, ConfiguredFeature var3) {
      ConfiguredFeature var4 = (ConfiguredFeature)var1.getValueOrThrow(var2);
      this.replaceableRegistry.register(var2, var4, var3);
      replaceRegistryEntry(var1, var2, var3);
   }

   private static ResourceKey minecraftFeatureKey(String var0) {
      return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath("minecraft", var0));
   }

   private static ConfiguredFeature wrapWithForcedDefaultState(ConfiguredFeature var0) {
      Feature var1 = var0.feature();
      NoneFeatureConfiguration var2 = (NoneFeatureConfiguration)var0.config();
      return new ConfiguredFeature(new FeatureWrapperB(NoneFeatureConfiguration.CODEC, var1), var2);
   }

   private static WorldGenLevel wrapGeneratorForDefaultStates(WorldGenLevel var0) {
      return (WorldGenLevel)Proxy.newProxyInstance(WorldGenLevel.class.getClassLoader(), new Class[]{WorldGenLevel.class}, (var1, var2, var3) -> {
         if ("setBlock".equals(var2.getName()) && var3 != null && var3.length >= 3 && var3[1] instanceof BlockState var4) {
            BlockState var7 = forceBlockDefaultState(var4);
            if (var7 != null) {
               var3[1] = var7;
            }
         }

         try {
            return var2.invoke(var0, var3);
         } catch (InvocationTargetException var6) {
            throw var6.getCause();
         }
      });
   }

   private static BlockState forceBlockDefaultState(BlockState var0) {
      return var0.is(Blocks.CHORUS_PLANT) ? Blocks.CHORUS_PLANT.defaultBlockState() : null;
   }

   private static void replaceRegistryEntry(MappedRegistry var0, ResourceKey var1, Object var2) {
      Object var3 = var0.getValueOrThrow(var1);
      Reference var4 = (Reference)var0.get(var1).orElseThrow();
      Field var5 = ServerCompat.findFieldByTypeIndex(Reference.class, Object.class, 0);
      Field var6 = ServerCompat.findFieldByTypeIndex(MappedRegistry.class, Map.class, 2);
      Field var7 = ServerCompat.findFieldByTypeIndex(MappedRegistry.class, Reference2IntMap.class, 0);
      if (var5 != null && var6 != null && var7 != null) {
         Map var8 = (Map)var6.get(var0);
         Reference2IntMap var9 = (Reference2IntMap)var7.get(var0);
         int var10 = var0.getId(var3);
         var8.remove(var3);
         var9.removeInt(var3);
         var5.set(var4, var2);
         var8.put(var2, var4);
         var9.put(var2, var10);
      } else {
         throw new NoSuchFieldException("MappedRegistry/Holder fields changed");
      }
   }
}
