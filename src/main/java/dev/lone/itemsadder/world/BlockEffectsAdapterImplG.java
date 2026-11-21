package dev.lone.itemsadder.world;

import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.internal.SwitchMap29;
import dev.lone.itemsadder.loot.NbtDropTrigger;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NbtTagType;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.BlockPosition.MutableBlockPosition;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.DataComponentPatch.a;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagType;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.MobSpawnerAbstract;
import net.minecraft.world.level.MobSpawnerData;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.BlockObserver;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.SoundEffectType;
import net.minecraft.world.level.block.entity.TileEntityMobSpawner;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.Chunk.EnumTileEntityState;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidType;
import org.bukkit.ExplosionResult;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_21_R4.CraftChunk;
import org.bukkit.craftbukkit.v1_21_R4.CraftSound;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R4.block.CraftBlock;
import org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R4.util.CraftMagicNumbers;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

public class BlockEffectsAdapterImplG implements BlockEffectsAdapter {
   IBlockData spawnerState = CraftMagicNumbers.getBlock(Material.SPAWNER).m();
   static final EnumDirection[] NEIGHBOR_DIRECTIONS = new EnumDirection[]{
      EnumDirection.e, EnumDirection.f, EnumDirection.c, EnumDirection.d, EnumDirection.a, EnumDirection.b
   };
   static final EnumDirection[] LIGHT_DIRECTIONS = new EnumDirection[]{
      EnumDirection.a, EnumDirection.f, EnumDirection.c, EnumDirection.d, EnumDirection.b, EnumDirection.e
   };

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
         BlockPosition var4 = new BlockPosition(var3.getBlockX(), var3.getBlockY(), var3.getBlockZ());
         Chunk var5 = toNmsChunk(block.getChunk());
         WorldServer var6 = ((CraftWorld)block.getWorld()).getHandle();
         var5.a(var4, this.spawnerState, 0);
         TileEntityMobSpawner var7 = (TileEntityMobSpawner)var5.a(var4, EnumTileEntityState.a);
         if (var7 == null) {
            Log.error("Failed to place TILE block, spawner is null: " + LocationScanner.formatCoords(var3));
         } else {
            MobSpawnerData var8 = (MobSpawnerData)customBlock.anF.FD();
            if (var8 == null) {
               EntityArmorStand var9 = new EntityArmorStand(EntityTypes.g, var6);
               var9.k(true);
               var9.b(true);
               var9.u(true);
               var9.w(0.0F);
               var9.r(0.0F);
               Item var10 = CraftMagicNumbers.getItem(customBlock.getType());
               ItemStack var11 = new ItemStack(var10, 1);
               CustomModelData var12 = new CustomModelData(List.of((float)customBlock.nz()), List.of(), List.of(), List.of());
               a var13 = DataComponentPatch.a();
               var13.a(DataComponents.p, var12);
               var11.b(var13.a());
               var9.a(EnumItemSlot.f, var11);
               NBTTagCompound var14 = new NBTTagCompound();
               var9.f(var14);
               var8 = new MobSpawnerData(var14, Optional.empty(), Optional.empty());
               customBlock.anF.method10032(var8);
            }

            MobSpawnerAbstract var15 = var7.c();
            var15.a(var6, var4, var8);
            var15.t = 0;
            var15.s = 0;
            var15.o = 0;
            var15.p = 0;
            var15.q = 0;
            var15.j = 0;
            var7.e();
            var5.a(var7);
         }
      }
   }

   public static Chunk toNmsChunk(org.bukkit.Chunk var0) {
      return (Chunk)((CraftChunk)var0).getHandle(ChunkStatus.n);
   }

   @Override
   public Sound getBreakSound(Material material) {
      if (material == Material.AIR) {
         return null;
      }

      net.minecraft.world.level.block.Block var2 = CraftMagicNumbers.getBlock(material);
      SoundEffectType var3 = var2.m().A();
      return CraftSound.minecraftToBukkit(var3.c());
   }

   @Override
   public Sound getPlaceSound(Material material) {
      if (material == Material.AIR) {
         return null;
      }

      net.minecraft.world.level.block.Block var2 = CraftMagicNumbers.getBlock(material);
      SoundEffectType var3 = var2.m().A();
      return CraftSound.minecraftToBukkit(var3.e());
   }

   @Override
   public Sound getHitSound(Material material) {
      if (material == Material.AIR) {
         return null;
      }

      net.minecraft.world.level.block.Block var2 = CraftMagicNumbers.getBlock(material);
      SoundEffectType var3 = var2.m().A();
      return CraftSound.minecraftToBukkit(var3.f());
   }

   @Override
   public Sound getStepSound(Material material) {
      if (material == Material.AIR) {
         return null;
      }

      net.minecraft.world.level.block.Block var2 = CraftMagicNumbers.getBlock(material);
      SoundEffectType var3 = var2.m().A();
      return CraftSound.minecraftToBukkit(var3.d());
   }

   @Override
   public Sound getFallSound(Material material) {
      if (material == Material.AIR) {
         return null;
      }

      net.minecraft.world.level.block.Block var2 = CraftMagicNumbers.getBlock(material);
      SoundEffectType var3 = var2.m().A();
      return CraftSound.minecraftToBukkit(var3.g());
   }

   @Override
   public void updateNeighborShapes(Block block) {
      Chunk var2 = toNmsChunk(block.getChunk());
      BlockPosition var3 = new BlockPosition(block.getX(), block.getY(), block.getZ());
      IBlockData var4 = var2.a_(var3);
      MutableBlockPosition var5 = new MutableBlockPosition();
      World var6 = var2.H();

      for (EnumDirection var10 : NEIGHBOR_DIRECTIONS) {
         var5.a(var3, var10);
         IBlockData var11 = var6.a_(var5);
         Material var12 = CraftMagicNumbers.getMaterial(var11.b());
         if (var12 != Material.CHORUS_PLANT
            && var12 != Material.MUSHROOM_STEM
            && var12 != Material.BROWN_MUSHROOM_BLOCK
            && var12 != Material.RED_MUSHROOM_BLOCK) {
            IBlockData var13 = var11.a(var2.r, var2.r, var5, var10, var3, var4, var2.r.A);
            net.minecraft.world.level.block.Block.a(var11, var13, var6, var5, 0, 512);
         }
      }
   }

   @Override
   public void notifyBlockUpdate(Block block) {
      Chunk var2 = toNmsChunk(block.getChunk());
      WorldServer var3 = ((CraftChunk)block.getChunk()).getCraftWorld().getHandle();
      BlockPosition var4 = new BlockPosition(block.getX(), block.getY(), block.getZ());
      IBlockData var5 = var2.a_(var4);
      net.minecraft.world.level.block.Block var6 = var5.b();
      var3.a(var4, var6, 2);
   }

   @Override
   public void setObserverPowered(Block block, boolean value) {
      Chunk var3 = toNmsChunk(block.getChunk());
      BlockPosition var4 = new BlockPosition(block.getX(), block.getY(), block.getZ());
      IBlockData var5 = var3.a_(var4);
      var5.b(BlockObserver.c, value);
   }

   @Override
   public boolean replaceFluidsAround(Block block) {
      WorldServer var2 = ((CraftWorld)block.getWorld()).getHandle();
      BlockPosition var3 = new BlockPosition(block.getX(), block.getY(), block.getZ());
      boolean var4 = false;

      for (EnumDirection var8 : NEIGHBOR_DIRECTIONS) {
         BlockPosition var9 = var3.a(var8);
         if (BlockFaceUtils.isChunkLoadedAtCoords(block.getWorld(), var9.u() >> 4, var9.w() >> 4)) {
            Fluid var10 = var2.b_(var9);
            if (!var10.c()) {
               FluidType var11 = var10.a();
               var2.a(var9, var11, var11.a(var2));
               var4 = true;
            }
         }
      }

      return var4;
   }

   @Override
   public void setLightBlocks(Block block, int level) {
      WorldServer var3 = ((CraftWorld)block.getWorld()).getHandle();
      BlockPosition var4 = new BlockPosition(block.getX(), block.getY(), block.getZ());
      if (level <= 0) {
         for (EnumDirection var8 : LIGHT_DIRECTIONS) {
            BlockPosition var9 = var4.a(var8);
            IBlockData var10 = var3.a_(var9);
            if (var10.l() || var10.b() == Blocks.iz) {
               var3.a(var9, false);
            }
         }
      } else {
         IBlockData var12 = Blocks.iz.m();
         var12 = (IBlockData)var12.b(LightBlock.c, Math.min(level, 15));

         for (EnumDirection var17 : LIGHT_DIRECTIONS) {
            BlockPosition var18 = var4.a(var17);
            IBlockData var11 = var3.a_(var18);
            if (var11.l() || var11.b() == Blocks.iz) {
               var3.a(var18, var12, 2, 0);
            }
         }
      }
   }

   @Override
   public boolean matchesNbtDrop(Block bukkitBlock, NbtDropTrigger rule) {
      Location var3 = bukkitBlock.getLocation();
      WorldServer var4 = ((CraftWorld)bukkitBlock.getWorld()).getHandle();
      BlockPosition var5 = new BlockPosition(var3.getBlockX(), var3.getBlockY(), var3.getBlockZ());
      Chunk var6 = toNmsChunk(bukkitBlock.getChunk());
      TileEntityMobSpawner var7 = (TileEntityMobSpawner)var6.a(var5, EnumTileEntityState.a);
      if (var7 == null) {
         return false;
      }

      NBTTagCompound var8 = var7.b(var4.J_());
      return matchesNbtTrigger(var8, rule);
   }

   @Override
   public boolean isHarvestableWith(Block block, org.bukkit.inventory.ItemStack tool) {
      BlockPosition var3 = new BlockPosition(block.getX(), block.getY(), block.getZ());
      IBlockData var4 = ((CraftWorld)block.getWorld()).getHandle().a_(var3);
      ItemStack var5 = CraftItemStack.asNMSCopy(tool);
      return var5.a(var4) > 1.0F && (!var4.C() || var5.b(var4));
   }

   @Override
   public String getMinecraftId(@NotNull Block bukkitBlock) {
      if (bukkitBlock.getType() == Material.AIR) {
         return "minecraft:air";
      }

      Location var2 = bukkitBlock.getLocation();
      net.minecraft.world.level.block.Block var3 = ((CraftBlock)bukkitBlock)
         .getHandle()
         .a_(new BlockPosition(var2.getBlockX(), var2.getBlockY(), var2.getBlockZ()))
         .b();
      return BuiltInRegistries.e.e(var3).g();
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static boolean matchesNbtTrigger(NBTTagCompound var0, NbtDropTrigger var1) {
      NbtTagType var2 = var1.HA();

      NBTTagType var3 = switch (SwitchMap29.aOq[var2.ordinal()]) {
         case 1 -> NBTTagString.a;
         case 2 -> NBTTagInt.a;
         case 3 -> NBTTagDouble.b;
         case 4 -> NBTTagFloat.b;
         case 5 -> NBTTagByte.a;
         case 6 -> NBTTagShort.a;
         default -> throw new RuntimeException("Unknown NBT type: " + var2);
      };
      String[] var4 = var1.Hz();
      Object var5 = var1.Hy();
      NBTBase var6 = var0.a(var4[0]);
      if (var6 == null) {
         return false;
      }

      NBTTagType var7 = var6.c();
      String var8 = var4[var4.length - 1];
      if (var7 == NBTTagCompound.b) {
         var0 = (NBTTagCompound)var0.m(var4[0]).get();

         for (int var9 = 1; var9 < var4.length - 1; var9++) {
            if (!var0.b(var4[var9])) {
               return false;
            }

            var0 = (NBTTagCompound)var0.m(var4[var9]).get();
         }

         NBTBase var10 = var0.a(var8);
         if (var10 == null) {
            return false;
         }

         var7 = var10.c();
         if (var7 != var3) {
            return false;
         }
      }

      if (var7 == NBTTagString.a) {
         return var0.i(var8).map(var1x -> var1x.equals(var5)).orElse(false);
      } else if (var7 == NBTTagInt.a) {
         return var0.e(var8).map(var1x -> var1x.equals(var5)).orElse(false);
      } else if (var7 == NBTTagDouble.b) {
         return var0.h(var8).map(var1x -> var1x.equals(var5)).orElse(false);
      } else if (var7 == NBTTagFloat.b) {
         return var0.g(var8).map(var1x -> var1x.equals(var5)).orElse(false);
      } else if (var7 == NBTTagByte.a) {
         return var0.c(var8).map(var1x -> var1x.equals(var5)).orElse(false);
      } else {
         return var7 == NBTTagShort.a ? var0.d(var8).map(var1x -> var1x.equals(var5)).orElse(false) : false;
      }
   }
}
