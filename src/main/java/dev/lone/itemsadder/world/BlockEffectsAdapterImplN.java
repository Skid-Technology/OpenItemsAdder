package dev.lone.itemsadder.world;

import com.mojang.logging.LogUtils;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.internal.SwitchMap36;
import dev.lone.itemsadder.loot.NbtDropTrigger;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NbtTagType;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.DataComponentPatch.Builder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter.ScopedCollector;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunk.EntityCreationType;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.TagValueOutput;
import org.bukkit.Chunk;
import org.bukkit.ExplosionResult;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R7.block.CraftBlock;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R7.util.CraftMagicNumbers;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

public class BlockEffectsAdapterImplN implements BlockEffectsAdapter {
   BlockState spawnerState = CraftMagicNumbers.getBlock(Material.SPAWNER).defaultBlockState();
   static final Direction[] NEIGHBOR_DIRECTIONS = new Direction[]{
      Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP
   };
   static final Direction[] LIGHT_DIRECTIONS = new Direction[]{Direction.DOWN, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.WEST};

   @Override
   public boolean handlesEvent(Event event) {
      return event instanceof BlockExplodeEvent var3 && var3.getExplosionResult() == ExplosionResult.TRIGGER_BLOCK
         || event instanceof EntityExplodeEvent var2 && var2.getExplosionResult() == ExplosionResult.TRIGGER_BLOCK;
   }

   @Override
   public void placeTileBlock(Block block, CustomBlockDefinition customBlock) {
      if (customBlock.anF == null) {
         Log.error("Failed to place TILE block. Can't load placeable model obj.");
      } else {
         Location var3 = block.getLocation();
         BlockPos var4 = new BlockPos(var3.getBlockX(), var3.getBlockY(), var3.getBlockZ());
         LevelChunk var5 = toNmsChunk(block.getChunk());
         ServerLevel var6 = ((CraftWorld)block.getWorld()).getHandle();
         var5.setBlockState(var4, this.spawnerState, 0);
         SpawnerBlockEntity var7 = (SpawnerBlockEntity)var5.getBlockEntity(var4, EntityCreationType.IMMEDIATE);
         if (var7 == null) {
            Log.error("Failed to place TILE block, spawner is null: " + LocationScanner.formatCoords(var3));
         } else {
            SpawnData var8 = (SpawnData)customBlock.anF.FD();
            if (var8 == null) {
               ArmorStand var9 = new ArmorStand(EntityTypes.ARMOR_STAND, var6);
               var9.setInvisible(true);
               var9.setNoBasePlate(true);
               var9.setMarker(true);
               var9.setYRot(0.0F);
               var9.setYHeadRot(0.0F);
               Item var10 = CraftMagicNumbers.getItem(customBlock.getType());
               ItemStack var11 = new ItemStack(var10, 1);
               CustomModelData var12 = new CustomModelData(List.of((float)customBlock.nz()), List.of(), List.of(), List.of());
               Builder var13 = DataComponentPatch.builder();
               var13.set(DataComponents.CUSTOM_MODEL_DATA, var12);
               var11.applyComponents(var13.build());
               var9.setItemSlot(EquipmentSlot.HEAD, var11);
               CompoundTag var14 = new CompoundTag();
               TagValueOutput var15 = TagValueOutput.createWrappingGlobal(new ScopedCollector(LogUtils.getLogger()), var14);
               var9.saveAsPassenger(var15);
               var8 = new SpawnData(var14, Optional.empty(), Optional.empty());
               customBlock.anF.method10032(var8);
            }

            BaseSpawner var16 = var7.getSpawner();
            var16.setNextSpawnData(var6, var4, var8);
            var16.requiredPlayerRange = 0;
            var16.maxNearbyEntities = 0;
            var16.minSpawnDelay = 0;
            var16.maxSpawnDelay = 0;
            var16.spawnCount = 0;
            var16.spawnDelay = 0;
            var7.setChanged();
            var5.setBlockEntity(var7);
         }
      }
   }

   public static LevelChunk toNmsChunk(Chunk var0) {
      return (LevelChunk)((CraftChunk)var0).getHandle(ChunkStatus.FULL);
   }

   @Override
   public Sound getBreakSound(Material material) {
      if (material == Material.AIR) {
         return null;
      }

      net.minecraft.world.level.block.Block var2 = CraftMagicNumbers.getBlock(material);
      SoundType var3 = var2.defaultBlockState().getSoundType();
      return CraftSound.minecraftToBukkit(var3.getBreakSound());
   }

   @Override
   public Sound getPlaceSound(Material material) {
      if (material == Material.AIR) {
         return null;
      }

      net.minecraft.world.level.block.Block var2 = CraftMagicNumbers.getBlock(material);
      SoundType var3 = var2.defaultBlockState().getSoundType();
      return CraftSound.minecraftToBukkit(var3.getPlaceSound());
   }

   @Override
   public Sound getHitSound(Material material) {
      if (material == Material.AIR) {
         return null;
      }

      net.minecraft.world.level.block.Block var2 = CraftMagicNumbers.getBlock(material);
      SoundType var3 = var2.defaultBlockState().getSoundType();
      return CraftSound.minecraftToBukkit(var3.getHitSound());
   }

   @Override
   public Sound getStepSound(Material material) {
      if (material == Material.AIR) {
         return null;
      }

      net.minecraft.world.level.block.Block var2 = CraftMagicNumbers.getBlock(material);
      SoundType var3 = var2.defaultBlockState().getSoundType();
      return CraftSound.minecraftToBukkit(var3.getStepSound());
   }

   @Override
   public Sound getFallSound(Material material) {
      if (material == Material.AIR) {
         return null;
      }

      net.minecraft.world.level.block.Block var2 = CraftMagicNumbers.getBlock(material);
      SoundType var3 = var2.defaultBlockState().getSoundType();
      return CraftSound.minecraftToBukkit(var3.getFallSound());
   }

   @Override
   public void updateNeighborShapes(Block block) {
      LevelChunk var2 = toNmsChunk(block.getChunk());
      BlockPos var3 = new BlockPos(block.getX(), block.getY(), block.getZ());
      BlockState var4 = var2.getBlockState(var3);
      MutableBlockPos var5 = new MutableBlockPos();
      Level var6 = var2.getLevel();

      for (Direction var10 : NEIGHBOR_DIRECTIONS) {
         var5.setWithOffset(var3, var10);
         BlockState var11 = var6.getBlockState(var5);
         Material var12 = CraftMagicNumbers.getMaterial(var11.getBlock());
         if (var12 != Material.CHORUS_PLANT
            && var12 != Material.MUSHROOM_STEM
            && var12 != Material.BROWN_MUSHROOM_BLOCK
            && var12 != Material.RED_MUSHROOM_BLOCK) {
            BlockState var13 = var11.updateShape(var2.getLevel(), var2.getLevel(), var5, var10, var3, var4, var2.getLevel().getRandom());
            net.minecraft.world.level.block.Block.updateOrDestroy(var11, var13, var6, var5, 0, 512);
         }
      }
   }

   @Override
   public void notifyBlockUpdate(Block block) {
      LevelChunk var2 = toNmsChunk(block.getChunk());
      ServerLevel var3 = ((CraftChunk)block.getChunk()).getCraftWorld().getHandle();
      BlockPos var4 = new BlockPos(block.getX(), block.getY(), block.getZ());
      BlockState var5 = var2.getBlockState(var4);
      net.minecraft.world.level.block.Block var6 = var5.getBlock();
      var3.scheduleTick(var4, var6, 2);
   }

   @Override
   public void setObserverPowered(Block block, boolean value) {
      LevelChunk var3 = toNmsChunk(block.getChunk());
      BlockPos var4 = new BlockPos(block.getX(), block.getY(), block.getZ());
      BlockState var5 = var3.getBlockState(var4);
      var5.setValue(ObserverBlock.POWERED, value);
   }

   @Override
   public boolean replaceFluidsAround(Block block) {
      ServerLevel var2 = ((CraftWorld)block.getWorld()).getHandle();
      BlockPos var3 = new BlockPos(block.getX(), block.getY(), block.getZ());
      boolean var4 = false;

      for (Direction var8 : NEIGHBOR_DIRECTIONS) {
         BlockPos var9 = var3.relative(var8);
         if (BlockFaceUtils.isChunkLoadedAtCoords(block.getWorld(), var9.getX() >> 4, var9.getZ() >> 4)) {
            FluidState var10 = var2.getFluidState(var9);
            if (!var10.isEmpty()) {
               Fluid var11 = var10.getType();
               var2.scheduleTick(var9, var11, var11.getTickDelay(var2));
               var4 = true;
            }
         }
      }

      return var4;
   }

   @Override
   public void setLightBlocks(Block block, int level) {
      ServerLevel var3 = ((CraftWorld)block.getWorld()).getHandle();
      BlockPos var4 = new BlockPos(block.getX(), block.getY(), block.getZ());
      if (level <= 0) {
         for (Direction var8 : LIGHT_DIRECTIONS) {
            BlockPos var9 = var4.relative(var8);
            BlockState var10 = var3.getBlockState(var9);
            if (var10.isAir() || var10.getBlock() == Blocks.LIGHT) {
               var3.removeBlock(var9, false);
            }
         }
      } else {
         BlockState var12 = Blocks.LIGHT.defaultBlockState();
         var12 = (BlockState)var12.setValue(LightBlock.LEVEL, Math.min(level, 15));

         for (Direction var17 : LIGHT_DIRECTIONS) {
            BlockPos var18 = var4.relative(var17);
            BlockState var11 = var3.getBlockState(var18);
            if (var11.isAir() || var11.getBlock() == Blocks.LIGHT) {
               var3.setBlock(var18, var12, 2, 0);
            }
         }
      }
   }

   @Override
   public boolean matchesNbtDrop(Block bukkitBlock, NbtDropTrigger rule) {
      Location var3 = bukkitBlock.getLocation();
      ServerLevel var4 = ((CraftWorld)bukkitBlock.getWorld()).getHandle();
      BlockPos var5 = new BlockPos(var3.getBlockX(), var3.getBlockY(), var3.getBlockZ());
      LevelChunk var6 = toNmsChunk(bukkitBlock.getChunk());
      SpawnerBlockEntity var7 = (SpawnerBlockEntity)var6.getBlockEntity(var5, EntityCreationType.IMMEDIATE);
      if (var7 == null) {
         return false;
      }

      CompoundTag var8 = var7.saveWithFullMetadata(var4.registryAccess());
      return matchesNbtTrigger(var8, rule);
   }

   @Override
   public boolean isHarvestableWith(Block block, org.bukkit.inventory.ItemStack tool) {
      BlockPos var3 = new BlockPos(block.getX(), block.getY(), block.getZ());
      BlockState var4 = ((CraftWorld)block.getWorld()).getHandle().getBlockState(var3);
      ItemStack var5 = CraftItemStack.asNMSCopy(tool);
      return var5.getDestroySpeed(var4) > 1.0F && (!var4.requiresCorrectToolForDrops() || var5.isCorrectToolForDrops(var4));
   }

   @Override
   public String getMinecraftId(@NotNull Block bukkitBlock) {
      if (bukkitBlock.getType() == Material.AIR) {
         return "minecraft:air";
      }

      Location var2 = bukkitBlock.getLocation();
      net.minecraft.world.level.block.Block var3 = ((CraftBlock)bukkitBlock).getBlockState().getBlock();
      return BuiltInRegistries.BLOCK.wrapAsHolder(var3).getRegisteredName();
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static boolean matchesNbtTrigger(CompoundTag var0, NbtDropTrigger var1) {
      NbtTagType var2 = var1.HA();

      TagType var3 = switch (SwitchMap36.aOM[var2.ordinal()]) {
         case 1 -> StringTag.TYPE;
         case 2 -> IntTag.TYPE;
         case 3 -> DoubleTag.TYPE;
         case 4 -> FloatTag.TYPE;
         case 5 -> ByteTag.TYPE;
         case 6 -> ShortTag.TYPE;
         default -> throw new RuntimeException("Unknown NBT type: " + var2);
      };
      String[] var4 = var1.Hz();
      Object var5 = var1.Hy();
      Tag var6 = var0.get(var4[0]);
      if (var6 == null) {
         return false;
      }

      TagType var7 = var6.getType();
      String var8 = var4[var4.length - 1];
      if (var7 == CompoundTag.TYPE) {
         var0 = (CompoundTag)var0.getCompound(var4[0]).get();

         for (int var9 = 1; var9 < var4.length - 1; var9++) {
            if (!var0.contains(var4[var9])) {
               return false;
            }

            var0 = (CompoundTag)var0.getCompound(var4[var9]).get();
         }

         Tag var10 = var0.get(var8);
         if (var10 == null) {
            return false;
         }

         var7 = var10.getType();
         if (var7 != var3) {
            return false;
         }
      }

      if (var7 == StringTag.TYPE) {
         return var0.getString(var8).map(var1x -> var1x.equals(var5)).orElse(false);
      } else if (var7 == IntTag.TYPE) {
         return var0.getInt(var8).map(var1x -> var1x.equals(var5)).orElse(false);
      } else if (var7 == DoubleTag.TYPE) {
         return var0.getDouble(var8).map(var1x -> var1x.equals(var5)).orElse(false);
      } else if (var7 == FloatTag.TYPE) {
         return var0.getFloat(var8).map(var1x -> var1x.equals(var5)).orElse(false);
      } else if (var7 == ByteTag.TYPE) {
         return var0.getByte(var8).map(var1x -> var1x.equals(var5)).orElse(false);
      } else {
         return var7 == ShortTag.TYPE ? var0.getShort(var8).map(var1x -> var1x.equals(var5)).orElse(false) : false;
      }
   }
}
