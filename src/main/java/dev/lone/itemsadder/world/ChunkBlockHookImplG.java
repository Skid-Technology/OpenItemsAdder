package dev.lone.itemsadder.world;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.fontimage.FontImageSettingsRecord;
import dev.lone.itemsadder.gui.GuiEntryRecord;
import dev.lone.itemsadder.util.AccessLevel;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.EnvironmentDetector;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.ServerCompat;
import io.papermc.paper.configuration.WorldConfiguration;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.RegistryMaterials;
import net.minecraft.core.Holder.c;
import net.minecraft.core.IRegistryCustom.Dimension;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.HeightMap.Type;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_21_R4.CraftChunk;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R4.block.data.CraftBlockData;
import org.jetbrains.annotations.Nullable;

public class ChunkBlockHookImplG implements ChunkBlockHook {
   static Predicate caveAirPredicate;
   static Block caveAirBlock;
   static Predicate airPredicate;
   static Block airBlock;
   static Predicate voidAirPredicate;
   static Block voidAirBlock;
   static Predicate noteBlockPredicate;
   static Block noteBlock;
   private static final HashSet trackedBlockPredicates;
   private static final HashSet trackedBlocks;
   static Method paperConfigMethod;

   private static Predicate blockPredicateOf(Material var0) {
      Block var1 = toNmsBlock(var0);
      return var1x -> var1.equals(var1x.b());
   }

   public static Chunk toNmsChunk(org.bukkit.Chunk var0) {
      return (Chunk)((CraftChunk)var0).getHandle(ChunkStatus.n);
   }

   private static Block toNmsBlock(Material var0) {
      IBlockData var1 = ((CraftBlockData)Bukkit.createBlockData(var0)).getState();
      return var1.b();
   }

   @Override
   public boolean isAntiXrayEnabled(World world) {
      if (Main.aEZ) {
         return true;
      }

      if (!EnvironmentDetector.paperAdventure) {
         return false;
      }

      if (paperConfigMethod == null) {
         return false;
      }

      WorldServer var2 = ((CraftWorld)world).getHandle();

      try {
         WorldConfiguration var3 = (WorldConfiguration)paperConfigMethod.invoke(var2);
         return var3.anticheat.antiXray.enabled;
      } catch (Exception var4) {
         var4.printStackTrace();
         return false;
      }
   }

   @Override
   public List findAirBlocksInRange(org.bukkit.Chunk chunk, int minHeight, int maxHeight) {
      World var4 = chunk.getWorld();
      Chunk var5 = toNmsChunk(chunk);
      ChunkSection[] var6 = var5.d();
      int var7 = chunk.getX() << 4;
      int var8 = chunk.getZ() << 4;
      ArrayList var9 = new ArrayList();

      for (int var10 = 0; var10 < var6.length; var10++) {
         ChunkSection var11 = var6[var10];
         if (var11 != null && !var11.c()) {
            int var12 = var5.r.h(var10) << 4;
            int var13 = var12 + 15;
            if (var13 >= minHeight && var12 <= maxHeight) {
               int var14 = Math.max(var12, minHeight);
               int var15 = Math.min(var13, maxHeight);
               if (var11.a(caveAirPredicate) || var11.a(airPredicate) || var11.a(voidAirPredicate)) {
                  this.collectAirBlocksInSection(var4, var5, var7, var8, var9, var11, var14, var15);
               }
            }
         }
      }

      return var9;
   }

   private void collectAirBlocksInSection(World var1, Chunk var2, int var3, int var4, List var5, ChunkSection var6, int var7, int var8) {
      for (int var9 = 0; var9 < 16; var9++) {
         int var10 = var3 + var9;

         for (int var11 = 0; var11 < 16; var11++) {
            int var12 = var4 + var11;
            int var13 = var2.a(Type.b, var9, var11);
            int var14 = Math.min(var8, var13);

            for (int var15 = var7; var15 <= var14; var15++) {
               Block var16 = var6.a(var9, var15 & 15, var11).b();
               if (var16 == caveAirBlock || var16 == airBlock || var16 == voidAirBlock) {
                  var5.add(var1.getBlockAt(var10, var15, var12));
               }
            }
         }
      }
   }

   @Override
   public List findTrackedBlocksInChunk(org.bukkit.Chunk chunk) {
      World var2 = chunk.getWorld();
      Chunk var3 = toNmsChunk(chunk);
      ChunkSection[] var4 = var3.d();
      int var5 = chunk.getX() << 4;
      int var6 = chunk.getZ() << 4;
      ArrayList var7 = new ArrayList();

      for (int var8 = 0; var8 < var4.length; var8++) {
         ChunkSection var9 = var4[var8];
         if (var9 != null && !var9.c()) {
            int var10 = var3.r.h(var8) << 4;
            boolean var11 = false;

            for (Predicate var13 : trackedBlockPredicates) {
               if (var9.a(var13)) {
                  var11 = true;
                  break;
               }
            }

            if (var11) {
               for (int var16 = var5; var16 < var5 + 16; var16++) {
                  for (int var17 = var6; var17 < var6 + 16; var17++) {
                     for (int var14 = var10; var14 < var10 + 16; var14++) {
                        Block var15 = var9.a(var16 & 15, var14 & 15, var17 & 15).b();
                        if (trackedBlocks.contains(var15)) {
                           var7.add(new Location(var2, var16, var14, var17));
                        }
                     }
                  }
               }
            }
         }
      }

      return var7;
   }

   @Override
   public List findNoteBlocksInColumn(Location loc) {
      org.bukkit.Chunk var2 = loc.getChunk();
      World var3 = var2.getWorld();
      Chunk var4 = toNmsChunk(var2);
      ChunkSection[] var5 = var4.d();
      int var6 = loc.getBlockX();
      int var7 = loc.getBlockZ();
      ArrayList var8 = new ArrayList();

      for (int var9 = 0; var9 < var5.length; var9++) {
         ChunkSection var10 = var5[var9];
         if (var10 != null && !var10.c()) {
            int var11 = var4.r.h(var9) << 4;
            if (var10.a(noteBlockPredicate)) {
               for (int var12 = var11; var12 < var11 + 16; var12++) {
                  Block var13 = var10.a(var6 & 15, var12 & 15, var7 & 15).b();
                  if (var13 == noteBlock) {
                     var8.add(new Location(var3, var6, var12, var7));
                  }
               }
            }
         }
      }

      return var8;
   }

   @Nullable
   @Override
   public Object getBiomeKeyAt(org.bukkit.block.Block bukkitBlock) {
      Location var2 = bukkitBlock.getLocation();
      World var3 = var2.getWorld();
      if (var3 == null) {
         return null;
      }

      int var4 = var2.getBlockX();
      int var5 = var2.getBlockY();
      int var6 = var2.getBlockZ();
      WorldServer var7 = ((CraftWorld)var3).getHandle();
      Optional var8 = var7.getNoiseBiome(var4 >> 2, var5 >> 2, var6 >> 2).e();
      if (var8.isEmpty()) {
         return null;
      }

      ResourceKey var9 = (ResourceKey)var8.get();
      return var9.a();
   }

   @Override
   public Object toMinecraftKey(NamespacedKey bukkitKey) {
      return MinecraftKey.a(bukkitKey.getNamespace(), bukkitKey.getKey());
   }

   @Override
   public NamespacedKey parseBiomeKey(String bukkitEnumStr) {
      if (bukkitEnumStr == null) {
         return null;
      }

      Biome var2 = (Biome)Registry.BIOME.get(NamespacedKey.fromString(bukkitEnumStr.toLowerCase(Locale.ROOT)));
      if (var2 != null) {
         return var2.getKey();
      }

      var2 = this.mapLegacyBiome(bukkitEnumStr);
      return var2 != null ? var2.getKey() : null;
   }

   @Override
   public void injectSoundsIntoRegistry(List sounds) {
      Field var2 = ServerCompat.findFieldByTypeIndex(RegistryMaterials.class, Map.class, 5);
      if (var2 == null) {
         throw new RuntimeException("Error injecting blocks: field_unregisteredIntrusiveHolders is null");
      }

      Dimension var3 = MinecraftServer.getServer().ba();
      RegistryMaterials var4 = (RegistryMaterials)var3.f(Registries.aV);

      Field var5;
      try {
         var5 = ServerCompat.findFieldByTypeIndex(RegistryMaterials.class, boolean.class, 0);
         if (var5 == null) {
            throw new RuntimeException("Error injecting JukeboxSong: field_frozen is null");
         }

         var5.set(var4, false);
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NullPointerException var11) {
         Log.warn("Error injecting JukeboxSong: " + var11.getMessage());
         throw new RuntimeException(var11);
      }

      try {
         var2.set(var4, new IdentityHashMap());
      } catch (Throwable var10) {
         throw new RuntimeException("Failed to reset unregisteredIntrusiveHolders", var10);
      }

      AtomicBoolean var6 = new AtomicBoolean();
      sounds.forEach(
         var2x -> {
            try {
               MinecraftKey var3x = MinecraftKey.a(var2x.Go(), var2x.nk());
               ResourceKey var4x = ResourceKey.a(Registries.aV, var3x);
               Float var5x = ((FontImageSettingsRecord)var2x.Gt().getFirst()).GD();
               if (var5x == null) {
                  Log.warn("Jukebox song '" + var2x.cV() + "' does not have a duration set. Defaulting to 10 seconds. This might be a bug.");
               }

               float var6x = var5x == null ? 10.0F : var5x;
               if (var4.e(var4x)) {
                  if (!var6.get()) {
                     var4.a(var4x)
                        .ifPresent(
                           var3xx -> {
                              JukeboxSong var4xx = (JukeboxSong)var3xx.a();
                              if (var4xx.e() != (var2x.Gs() == null ? 0 : var2x.Gs())
                                 || var4xx.d() != var6x
                                 || !var4xx.c().equals(AdventureText.toNative(var2x.Gn()))) {
                                 Log.warn("Jukebox song '" + var2x.cV() + "' properties changed. Restart the server to apply.");
                                 var6.set(true);
                              }
                           }
                        );
                  }
               } else {
                  JukeboxSong var7 = new JukeboxSong(
                     Holder.a(SoundEffect.a(ResourceKey.a(Registries.ap, MinecraftKey.a(var2x.Go(), var2x.nk())).a())),
                     (IChatBaseComponent)AdventureText.toNative(var2x.Gn()),
                     var6x,
                     var2x.Gs() == null ? 0 : var2x.Gs()
                  );
                  var4.f(var7);
                  c var8x = var4.a(var4x, var7, RegistrationInfo.a);
                  HashSet var9x = new HashSet();
                  Holder.a(var7).c().forEach(var9x::add);
                  Method var10x = ServerCompat.findDeclaredMethod(c.class, "bindTags", Collection.class);
                  if (var10x == null) {
                     var10x = ServerCompat.findMethodByAccess(void.class, c.class, AccessLevel.PACKAGE_PRIVATE, Collection.class);
                  }

                  if (var10x == null) {
                     throw new RuntimeException("Error injecting jukebox song: method_bindTags is null");
                  }

                  var10x.invoke(var8x, var9x);
                  Log.devLog("Injected jukebox song into Minecraft Registry: " + var2x.cV());
               }
            } catch (Throwable var11x) {
               Log.warn("Error registering jukebox song '" + var2x.cV() + "'.");
               throw new RuntimeException(var11x);
            }
         }
      );

      try {
         var2.set(var4, null);
      } catch (IllegalAccessException var9) {
         Log.warn("Failed to reset unregisteredIntrusiveHolders: " + var9.getMessage());
         throw new RuntimeException(var9);
      }

      try {
         var5.set(var4, true);
      } catch (IllegalAccessException var8) {
         Log.warn("Failed to freeze registry: " + var8.getMessage());
         throw new RuntimeException(var8);
      }
   }

   static {
      if (EnvironmentDetector.paperAdventure) {
         try {
            paperConfigMethod = WorldServer.class.getMethod("paperConfig");
         } catch (NoSuchMethodException var4) {
            Log.error("Failed to get ServerLevel#paperConfig() method.", var4);
         }
      }

      IBlockData var0 = ((CraftBlockData)Bukkit.createBlockData(Material.CAVE_AIR)).getState();
      caveAirBlock = var0.b();
      caveAirPredicate = var1x -> Objects.equals(var1x, var0);
      IBlockData var1 = ((CraftBlockData)Bukkit.createBlockData(Material.AIR)).getState();
      airBlock = var1.b();
      airPredicate = var1x -> Objects.equals(var1x, var1);
      IBlockData var2 = ((CraftBlockData)Bukkit.createBlockData(Material.VOID_AIR)).getState();
      voidAirBlock = var2.b();
      voidAirPredicate = var1x -> Objects.equals(var1x, var2);
      IBlockData var3 = ((CraftBlockData)Bukkit.createBlockData(Material.NOTE_BLOCK)).getState();
      noteBlock = var3.b();
      noteBlockPredicate = var0x -> noteBlock.equals(var0x.b());
      trackedBlockPredicates = new HashSet();
      trackedBlockPredicates.addAll(
         Arrays.asList(
            blockPredicateOf(Material.NOTE_BLOCK),
            blockPredicateOf(Material.CHORUS_PLANT),
            blockPredicateOf(Material.BROWN_MUSHROOM_BLOCK),
            blockPredicateOf(Material.RED_MUSHROOM_BLOCK),
            blockPredicateOf(Material.MUSHROOM_STEM),
            blockPredicateOf(Material.TRIPWIRE)
         )
      );
      trackedBlocks = new HashSet();
      trackedBlocks.addAll(
         Arrays.asList(
            toNmsBlock(Material.NOTE_BLOCK),
            toNmsBlock(Material.CHORUS_PLANT),
            toNmsBlock(Material.BROWN_MUSHROOM_BLOCK),
            toNmsBlock(Material.RED_MUSHROOM_BLOCK),
            toNmsBlock(Material.MUSHROOM_STEM),
            toNmsBlock(Material.TRIPWIRE)
         )
      );
   }
}
