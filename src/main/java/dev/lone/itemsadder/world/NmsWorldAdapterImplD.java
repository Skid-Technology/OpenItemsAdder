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
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.RegistryMaterials;
import net.minecraft.core.Holder.c;
import net.minecraft.core.IRegistryCustom.Dimension;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeFog;
import net.minecraft.world.level.biome.BiomeFog.a;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_21_R7.CraftChunk;
import org.bukkit.craftbukkit.v1_21_R7.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class NmsWorldAdapterImplD implements NmsWorldAdapter {
   @Nullable
   @Override
   public Object method3193(String namespace, String id) {
      return this.findBiomeHolder(namespace, id);
   }

   @Nullable
   public Holder findBiomeHolder(String var1, String var2) {
      IRegistry var3 = MinecraftServer.getServer().bc().f(Registries.aS);
      Optional var4 = var3.a(ResourceKey.a(Registries.aS, MinecraftKey.a(var1, var2)));
      return (Holder)var4.orElse(null);
   }

   @Override
   public String bj(Block block) {
      Chunk var2 = toNmsChunk(block.getChunk());
      if (var2 != null) {
         Holder var3 = var2.I().z(new BlockPosition(block.getX(), block.getY(), block.getZ()));
         IRegistry var4 = MinecraftServer.getServer().bc().f(Registries.aS);
         MinecraftKey var5 = var4.b((BiomeBase)var3.a());
         return var5 == null ? null : var5.toString();
      } else {
         return null;
      }
   }

   public static Chunk toNmsChunk(org.bukkit.Chunk var0) {
      return (Chunk)((CraftChunk)var0).getHandle(ChunkStatus.n);
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
            Chunk var7 = toNmsChunk(loc.getChunk());
            ChunkSection var8 = var7.b(var7.f(loc.getBlockY()));

            for (int var9 = 0; var9 < 4; var9++) {
               for (int var10 = 0; var10 < 4; var10++) {
                  for (int var11 = 0; var11 < 4; var11++) {
                     var8.setBiome(var9, var10, var11, var6);
                  }
               }
            }

            var7.i();
            var5.getHandle().p().a.b(List.of(var7));
         }
      }
   }

   @Override
   public void method3196(org.bukkit.Chunk chunk, Player bukkitPlayer) {
      Chunk var3 = toNmsChunk(chunk);
      PlayerConnection var4 = ((CraftPlayer)bukkitPlayer).getHandle().g;
      if (!var4.isDisconnected()) {
         var4.b(new ClientboundLevelChunkWithLightPacket(var3, var3.I().ac().r(), null, null));
      }
   }

   @Override
   public boolean method3198(List customBiomeInfos) {
      if (customBiomeInfos.isEmpty()) {
         return false;
      }

      Field var2 = ServerCompat.findFieldByTypeIndex(RegistryMaterials.class, Map.class, 5);
      if (var2 == null) {
         throw new RuntimeException("Error injecting blocks: field_unregisteredIntrusiveHolders is null");
      }

      MinecraftServer var3 = MinecraftServer.getServer();
      Dimension var4 = var3.bc();
      RegistryMaterials var5 = (RegistryMaterials)var4.f(Registries.aS);

      Field var6;
      try {
         var6 = ServerCompat.findFieldByTypeIndex(RegistryMaterials.class, boolean.class, 0);
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
               MinecraftKey var11 = MinecraftKey.a(var10.namespace(), var10.id());
               ResourceKey var12 = ResourceKey.a(Registries.aS, var11);
               if (var5.e(var12)) {
                  if (!var8.get()) {
                     int var13 = var10.waterColor();
                     var5.a(var12).ifPresent(var3x -> {
                        BiomeBase var4x = (BiomeBase)var3x.a();
                        if (var4x.i() != var13) {
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
                     Integer var14 = (Integer)((BiomeBase)var38.a()).h().b().orElse(null);
                     Integer var15 = (Integer)((BiomeBase)var38.a()).h().d().orElse(null);
                     a var16 = new a().a(var10.waterColor()).c(((BiomeBase)var38.a()).e()).a(((BiomeBase)var38.a()).h().e());
                     if (var14 != null) {
                        var16.b(var14);
                     }

                     if (var15 != null) {
                        var16.d(var15);
                     }

                     BiomeFog var17 = var16.a();
                     BiomeBase var18 = new net.minecraft.world.level.biome.BiomeBase.a()
                        .a(((BiomeBase)var38.a()).i.a())
                        .a(((BiomeBase)var38.a()).f())
                        .b(((BiomeBase)var38.a()).i.d())
                        .a(var17)
                        .a(((BiomeBase)var38.a()).a())
                        .a(((BiomeBase)var38.a()).c())
                        .a(((BiomeBase)var38.a()).i.c())
                        .a(((BiomeBase)var38.a()).i.b())
                        .a();
                     var5.f(var18);
                     c var19 = var5.a(var12, var18, RegistrationInfo.a);
                     HashSet var20 = new HashSet();
                     Holder.a(var18).c().forEach(var20::add);
                     Method var21 = ServerCompat.findDeclaredMethod(c.class, "bindTags", Collection.class);
                     if (var21 == null) {
                        var21 = ServerCompat.findMethodByAccess(void.class, c.class, AccessLevel.PACKAGE_PRIVATE, Collection.class);
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
