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
import net.minecraft.core.BlockPosition;
import net.minecraft.core.RegistryMaterials;
import net.minecraft.core.Holder.c;
import net.minecraft.core.IRegistryCustom.Dimension;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.tags.TagsBlock;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.levelgen.feature.WorldGenFeatureConfigured;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureEmptyConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureMushroomConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.WorldGenFeatureStateProvider;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;

public class TreeFeatureHookImplA implements TreeFeatureHook {
   private final ReplaceableRegistry replaceableRegistry = new ReplaceableRegistry();
   RandomSource random = RandomSource.a();
   List invalidBaseBlocks = Arrays.asList(
      Blocks.bt, Blocks.bu, Blocks.bv, Blocks.ff, Blocks.fg, Blocks.iD, Blocks.iE, Blocks.iF, Blocks.iG, Blocks.iH, Blocks.iI, Blocks.sG, Blocks.kC, Blocks.bw
   );

   @Override
   public boolean growTree(Location loc, TreeType type, BlockDataSetter delegate, boolean highestBlock) {
      CraftWorld var5 = (CraftWorld)loc.getWorld();
      WorldServer var6 = var5.getHandle();
      Chunk var7 = var6.d(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
      BlockPosition var8 = new BlockPosition(
         loc.getBlockX(),
         highestBlock ? InstrumentRegistry.getHeightAt(loc.getChunk(), loc.getBlockX() >> 4, loc.getBlockZ() >> 4, HeightMap.WORLD_SURFACE) : loc.getBlockY(),
         loc.getBlockZ()
      );
      IBlockData var9 = var7.a_(var8);
      if (var9.k()) {
         return false;
      }

      BlockPosition var10 = new BlockPosition(var8.u(), var8.v() - 1, var8.w());
      IBlockData var11 = var7.a_(var10);
      if (!var11.k() && !var11.i() && !var11.a(TagsBlock.t) && !var11.a(TagsBlock.O) && !this.invalidBaseBlocks.contains(var11.b())) {
         var7.setBlockState(var10, Blocks.i.o(), false, false);
         var6.captureTreeGeneration = true;
         var6.captureBlockStates = true;
         boolean var12 = var5.generateTree(var6, var6.getMinecraftWorld().l().g(), var8, this.random, type);
         var6.captureBlockStates = false;
         var6.captureTreeGeneration = false;
         if (var12) {
            for (BlockState var14 : var6.capturedBlockStates.values()) {
               delegate.setBlockData(var14.getBlock(), var14.getBlockData());
            }
         }

         var6.capturedBlockStates.clear();
         var7.setBlockState(var10, var11, false, false);
         return var12;
      } else {
         return false;
      }
   }

   @Override
   public void injectDefaultBlockstateFeatures() {
      if (TreeFeatureHookHolder.featuresInjected.compareAndSet(false, true)) {
         try {
            Dimension var1 = MinecraftServer.getServer().bc();
            RegistryMaterials var2 = (RegistryMaterials)var1.d(Registries.aC);
            this.wrapChorusPlantFeature(var2);
            this.injectMushroomFeature(var2, "huge_brown_mushroom", WorldGenerator.r, Blocks.eT.o());
            this.injectMushroomFeature(var2, "huge_red_mushroom", WorldGenerator.q, Blocks.eU.o());
         } catch (Throwable var3) {
            this.restoreDefaultBlockstateFeatures();
            Log.warn("Failed to inject force-default-blockstate tree features: " + var3.getMessage());
         }
      }
   }

   private void wrapChorusPlantFeature(RegistryMaterials var1) {
      ResourceKey var2 = minecraftFeatureKey("chorus_plant");
      this.replaceFeature(var1, var2, wrapWithForcedDefaultState((WorldGenFeatureConfigured)var1.f(var2)));
   }

   private void injectMushroomFeature(RegistryMaterials var1, String var2, WorldGenerator var3, IBlockData var4) {
      ResourceKey var5 = minecraftFeatureKey(var2);
      WorldGenFeatureMushroomConfiguration var6 = (WorldGenFeatureMushroomConfiguration)((WorldGenFeatureConfigured)var1.f(var5)).c();
      WorldGenFeatureMushroomConfiguration var7 = new WorldGenFeatureMushroomConfiguration(
         WorldGenFeatureStateProvider.a(var4), WorldGenFeatureStateProvider.a(Blocks.eV.o()), var6.d
      );
      this.replaceFeature(var1, var5, new WorldGenFeatureConfigured(var3, var7));
   }

   @Override
   public void restoreDefaultBlockstateFeatures() {
      try {
         Dimension var1 = MinecraftServer.getServer().bc();
         RegistryMaterials var2 = (RegistryMaterials)var1.d(Registries.aC);
         this.replaceableRegistry.restoreAll(var2::f, (var1x, var2x) -> replaceRegistryEntry(var2, var1x, var2x));
      } catch (Throwable var6) {
         Log.warn("Failed to restore force-default-blockstate tree features: " + var6.getMessage());
      } finally {
         this.replaceableRegistry.clear();
         TreeFeatureHookHolder.featuresInjected.set(false);
      }
   }

   private void replaceFeature(RegistryMaterials var1, ResourceKey var2, WorldGenFeatureConfigured var3) {
      WorldGenFeatureConfigured var4 = (WorldGenFeatureConfigured)var1.f(var2);
      this.replaceableRegistry.register(var2, var4, var3);
      replaceRegistryEntry(var1, var2, var3);
   }

   private static ResourceKey minecraftFeatureKey(String var0) {
      return ResourceKey.a(Registries.aC, MinecraftKey.a("minecraft", var0));
   }

   private static WorldGenFeatureConfigured wrapWithForcedDefaultState(WorldGenFeatureConfigured var0) {
      WorldGenerator var1 = var0.b();
      WorldGenFeatureEmptyConfiguration var2 = (WorldGenFeatureEmptyConfiguration)var0.c();
      return new WorldGenFeatureConfigured(new TreeFeatureWrapperA(WorldGenFeatureEmptyConfiguration.a, var1), var2);
   }

   private static GeneratorAccessSeed wrapGeneratorForDefaultStates(GeneratorAccessSeed var0) {
      return (GeneratorAccessSeed)Proxy.newProxyInstance(
         GeneratorAccessSeed.class.getClassLoader(), new Class[]{GeneratorAccessSeed.class}, (var1, var2, var3) -> {
            if ("setBlock".equals(var2.getName()) && var3 != null && var3.length >= 3 && var3[1] instanceof IBlockData var4) {
               IBlockData var7 = forceBlockDefaultState(var4);
               if (var7 != null) {
                  var3[1] = var7;
               }
            }

            try {
               return var2.invoke(var0, var3);
            } catch (InvocationTargetException var6) {
               throw var6.getCause();
            }
         }
      );
   }

   private static IBlockData forceBlockDefaultState(IBlockData var0) {
      return var0.a(Blocks.ku) ? Blocks.ku.o() : null;
   }

   private static void replaceRegistryEntry(RegistryMaterials var0, ResourceKey var1, Object var2) {
      Object var3 = var0.f(var1);
      c var4 = var0.g(var1);
      Field var5 = ServerCompat.findFieldByTypeIndex(c.class, Object.class, 0);
      Field var6 = ServerCompat.findFieldByTypeIndex(RegistryMaterials.class, Map.class, 2);
      Field var7 = ServerCompat.findFieldByTypeIndex(RegistryMaterials.class, Reference2IntMap.class, 0);
      if (var5 != null && var6 != null && var7 != null) {
         Map var8 = (Map)var6.get(var0);
         Reference2IntMap var9 = (Reference2IntMap)var7.get(var0);
         int var10 = var0.a(var3);
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
