package dev.lone.itemsadder.world;

import dev.lone.itemsadder.nms.NmsWorldAdapter;
import dev.lone.itemsadder.util.AccessLevel;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.ServerCompat;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.RegistryAccess.Frozen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.Biome.BiomeBuilder;
import net.minecraft.world.level.biome.BiomeSpecialEffects.Builder;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class NmsWorldAdapterImplM implements NmsWorldAdapter {
   @Nullable
   @Override
   public Object method3193(String namespace, String id) {
      return this.findBiomeHolder(namespace, id);
   }

   @Nullable
   public Holder findBiomeHolder(String var1, String var2) {
      Registry var3 = MinecraftServer.getServer().registryAccess().lookupOrThrow(Registries.BIOME);
      Optional var4 = var3.get(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(var1, var2)));
      return (Holder)var4.orElse(null);
   }

   @Override
   public String bj(Block block) {
      LevelChunk var2 = toNmsChunk(block.getChunk());
      if (var2 != null) {
         Holder var3 = var2.getLevel().getBiome(new BlockPos(block.getX(), block.getY(), block.getZ()));
         Registry var4 = MinecraftServer.getServer().registryAccess().lookupOrThrow(Registries.BIOME);
         Identifier var5 = var4.getKey((Biome)var3.value());
         return var5 == null ? null : var5.toString();
      } else {
         return null;
      }
   }

   public static LevelChunk toNmsChunk(Chunk var0) {
      return (LevelChunk)((CraftChunk)var0).getHandle(ChunkStatus.FULL);
   }

   @Override
   public void method3194(Block block, String namespace, String id) {
      this.method3195(block.getLocation(), namespace, id, true);
   }

   @Override
   public void method3195(Location loc, String namespace, String id, boolean sendBiomeChangeToNearPlayers) {
      CraftWorld var5 = (CraftWorld)loc.getWorld();
      Holder var6 = this.findBiomeHolder(namespace, id);
      if (var6 != null) {
         if (var5 != null) {
            LevelChunk var7 = toNmsChunk(loc.getChunk());
            LevelChunkSection var8 = var7.getSection(var7.getSectionIndex(loc.getBlockY()));

            for (int var9 = 0; var9 < 4; var9++) {
               for (int var10 = 0; var10 < 4; var10++) {
                  for (int var11 = 0; var11 < 4; var11++) {
                     var8.setNoiseBiome(var9, var10, var11, var6);
                  }
               }
            }

            var7.markUnsaved();
            var5.getHandle().getChunkSource().chunkMap.resendBiomesForChunks(List.of(var7));
         }
      }
   }

   @Override
   public void method3196(Chunk chunk, Player bukkitPlayer) {
      LevelChunk var3 = toNmsChunk(chunk);
      ServerGamePacketListenerImpl var4 = ((CraftPlayer)bukkitPlayer).getHandle().connection;
      if (!var4.isDisconnected()) {
         var4.send(new ClientboundLevelChunkWithLightPacket(var3, var3.getLevel().getChunkSource().getLightEngine(), null, null));
      }
   }

   @Override
   public boolean method3198(List customBiomeInfos) {
      if (customBiomeInfos.isEmpty()) {
         return false;
      }

      Field var2 = ServerCompat.findFieldByTypeIndex(MappedRegistry.class, Map.class, 5);
      if (var2 == null) {
         throw new RuntimeException("Error injecting blocks: field_unregisteredIntrusiveHolders is null");
      }

      MinecraftServer var3 = MinecraftServer.getServer();
      Frozen var4 = var3.registryAccess();
      MappedRegistry var5 = (MappedRegistry)var4.lookupOrThrow(Registries.BIOME);

      Field var6;
      try {
         var6 = ServerCompat.findFieldByTypeIndex(MappedRegistry.class, boolean.class, 0);
         if (var6 == null) {
            throw new RuntimeException("Error injecting Biome: field_frozen is null");
         }

         var6.set(var5, false);
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NullPointerException var37) {
         Log.warn("Error injecting Biome: " + var37.getMessage());
         throw new RuntimeException(var37);
      }

      AtomicInteger var7 = new AtomicInteger();

      try {
         try {
            var2.set(var5, new IdentityHashMap());
         } catch (Throwable var35) {
            throw new RuntimeException("Failed to reset unregisteredIntrusiveHolders", var35);
         }

         AtomicBoolean var8 = new AtomicBoolean();

         for (BiomeWaterColor var10 : customBiomeInfos) {
            try {
               Identifier var11 = Identifier.fromNamespaceAndPath(var10.namespace(), var10.id());
               ResourceKey var12 = ResourceKey.create(Registries.BIOME, var11);
               if (var5.containsKey(var12)) {
                  if (!var8.get()) {
                     int var13 = var10.waterColor();
                     var5.get(var12).ifPresent(var3x -> {
                        Biome var4x = (Biome)var3x.value();
                        if (var4x.getWaterColor() != var13) {
                           Log.warn("Biome '" + var10.namespace() + ":" + var10.id() + "' properties changed. Restart the server to apply.");
                           var8.set(true);
                        }
                     });
                  }
               } else {
                  Holder var38 = this.findBiomeHolder("minecraft", "plains");
                  if (var38 == null) {
                     Log.warn("Failed to find plains biome in Minecraft registry. Cannot register custom biome '" + var10.namespace() + ":" + var10.id() + "'.");
                  } else {
                     Integer var14 = (Integer)((Biome)var38.value()).getSpecialEffects().foliageColorOverride().orElse(null);
                     Integer var15 = (Integer)((Biome)var38.value()).getSpecialEffects().grassColorOverride().orElse(null);
                     Builder var16 = new Builder()
                        .waterColor(var10.waterColor())
                        .dryFoliageColorOverride(((Biome)var38.value()).getDryFoliageColor())
                        .grassColorModifier(((Biome)var38.value()).getSpecialEffects().grassColorModifier());
                     if (var14 != null) {
                        var16.foliageColorOverride(var14);
                     }

                     if (var15 != null) {
                        var16.grassColorOverride(var15);
                     }

                     BiomeSpecialEffects var17 = var16.build();
                     Biome var18 = new BiomeBuilder()
                        .hasPrecipitation(((Biome)var38.value()).climateSettings.hasPrecipitation())
                        .temperature(((Biome)var38.value()).getBaseTemperature())
                        .downfall(((Biome)var38.value()).climateSettings.downfall())
                        .specialEffects(var17)
                        .mobSpawnSettings(((Biome)var38.value()).getMobSettings())
                        .generationSettings(((Biome)var38.value()).getGenerationSettings())
                        .temperatureAdjustment(((Biome)var38.value()).climateSettings.temperatureModifier())
                        .temperature(((Biome)var38.value()).climateSettings.temperature())
                        .build();
                     var5.createIntrusiveHolder(var18);
                     Reference var19 = var5.register(var12, var18, RegistrationInfo.BUILT_IN);
                     HashSet var20 = new HashSet();
                     Holder.direct(var18).tags().forEach(var20::add);
                     Method var21 = ServerCompat.findDeclaredMethod(Reference.class, "bindTags", Collection.class);
                     if (var21 == null) {
                        var21 = ServerCompat.findMethodByAccess(void.class, Reference.class, AccessLevel.PACKAGE_PRIVATE, Collection.class);
                     }

                     if (var21 == null) {
                        throw new RuntimeException("Error injecting Biome: method_bindTags is null");
                     }

                     var21.invoke(var19, var20);
                     Log.devLog("Injected Biome into Minecraft Registry: " + var10.namespace() + ":" + var10.id());
                     var7.incrementAndGet();
                  }
               }
            } catch (Throwable var34) {
               Log.warn("Error registering Biome '" + var10.namespace() + ":" + var10.id() + "'.", var34);
            }
         }

         try {
            var2.set(var5, null);
         } catch (IllegalAccessException var33) {
            Log.warn("Failed to reset unregisteredIntrusiveHolders: " + var33.getMessage(), var33);
            return false;
         }
      } finally {
         try {
            var6.set(var5, true);
         } catch (IllegalAccessException var32) {
            Log.warn("Failed to freeze registry: " + var32.getMessage(), var32);
         }
      }

      return var7.get() > 0;
   }
}
