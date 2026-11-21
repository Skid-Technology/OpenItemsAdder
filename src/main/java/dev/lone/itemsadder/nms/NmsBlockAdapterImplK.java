package dev.lone.itemsadder.nms;

import dev.lone.itemsadder.internal.SwitchMap19;
import dev.lone.itemsadder.util.ServerCompat;
import dev.lone.itemsadder.world.InstrumentRegistry;
import dev.lone.itemsadder.world.MaterialCategory;
import dev.lone.itemsadder.world.NoteBlockData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagNetworkSerialization;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.MapColor;
import org.bukkit.Chunk;
import org.bukkit.HeightMap;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R7.block.CraftBlock;
import org.bukkit.craftbukkit.v1_21_R7.util.CraftMagicNumbers;
import org.jetbrains.annotations.Nullable;

public class NmsBlockAdapterImplK implements NmsBlockAdapter {
   private Packet aNB;
   List aNC = Arrays.asList(
      Material.SHORT_GRASS,
      Material.FERN,
      Material.DEAD_BUSH,
      Material.VINE,
      Material.GLOW_LICHEN,
      Material.SUNFLOWER,
      Material.LILAC,
      Material.ROSE_BUSH,
      Material.PEONY,
      Material.TALL_GRASS,
      Material.LARGE_FERN,
      Material.HANGING_ROOTS,
      Material.PITCHER_PLANT,
      Material.SEAGRASS
   );

   @Deprecated
   @Override
   public Class Nk() {
      return MapColor.class;
   }

   @Deprecated
   @Override
   public boolean method2794(Block block, MaterialCategory category) {
      throw new RuntimeException("Not supported on 1.20");
   }

   @Override
   public boolean aQ(Block block) {
      ServerLevel var2 = ((CraftWorld)block.getWorld()).getHandle();
      BlockPos var3 = new BlockPos(block.getX(), block.getY(), block.getZ());
      return var2.isRainingAt(var3);
   }

   @Override
   public boolean method2795(Material material) {
      return CraftMagicNumbers.getBlock(material) instanceof EntityBlock;
   }

   @Override
   public boolean aR(Block block) {
      ServerLevel var2 = ((CraftWorld)block.getWorld()).getHandle();
      BlockPos var3 = new BlockPos(block.getX(), block.getY(), block.getZ());
      LevelChunk var4 = var2.getChunk(block.getX() >> 4, block.getZ() >> 4);
      net.minecraft.world.level.block.Block var5 = var4.getBlockState(var3).getBlock();
      return var5 instanceof EntityBlock;
   }

   @Override
   public boolean aP(Block block) {
      return ((CraftBlock)block).getBlockState().canBeReplaced();
   }

   @Override
   public boolean aT(Block block) {
      return ((CraftBlock)block).getBlockState().liquid();
   }

   @Override
   public boolean aW(Block block) {
      return ((CraftBlock)block.getRelative(BlockFace.DOWN)).getBlockState().isAir();
   }

   @Override
   public int method2796(Chunk chunk, int x, int z, HeightMap heightMap) {
      return method2816(chunk).getHeight(this.method2817(heightMap), x, z);
   }

   public static LevelChunk method2816(Chunk var0) {
      return (LevelChunk)((CraftChunk)var0).getHandle(ChunkStatus.FULL);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public Types method2817(HeightMap var1) {
      return switch (SwitchMap19.aNN[var1.ordinal()]) {
         case 1 -> Types.MOTION_BLOCKING;
         case 2 -> Types.MOTION_BLOCKING_NO_LEAVES;
         case 3 -> Types.OCEAN_FLOOR;
         case 4 -> Types.OCEAN_FLOOR_WG;
         case 5 -> Types.WORLD_SURFACE;
         case 6 -> Types.WORLD_SURFACE_WG;
         default -> throw new EnumConstantNotPresentException(Types.class, var1.name());
      };
   }

   @Override
   public void method2797(InstrumentRegistry blockInfo) {
   }

   @Override
   public int method2798(Material material) {
      Integer var2 = (Integer)aNv.get(material);
      if (var2 == null) {
         var2 = Item.getId(CraftMagicNumbers.getItem(material));
         aNv.put(material, var2);
      }

      return var2;
   }

   @Override
   public Instrument aX(Block block) {
      NoteBlockInstrument var2 = ((CraftBlock)block).getBlockState().instrument();
      return InstrumentRegistry.instrumentOf((byte)var2.ordinal());
   }

   @Nullable
   @Override
   public NoteBlockData aY(Block bukkitBlock) {
      BlockState var2 = ((CraftBlock)bukkitBlock).getBlockState();
      ServerLevel var3 = ((CraftWorld)bukkitBlock.getWorld()).getHandle();
      return this.method2818(var2, var3, new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ()));
   }

   @Nullable
   @Override
   public NoteBlockData method2799(Block bukkitBlock, byte instrumentBelow) {
      BlockState var3 = ((CraftBlock)bukkitBlock).getBlockState();
      ServerLevel var4 = ((CraftWorld)bukkitBlock.getWorld()).getHandle();
      return this.method2819(instrumentBelow, var3, var4, new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ()));
   }

   @Override
   public Instrument aZ(Block bukkitBlock) {
      BlockState var2 = ((CraftBlock)bukkitBlock).getBlockState();
      ServerLevel var3 = ((CraftWorld)bukkitBlock.getWorld()).getHandle();
      BlockPos var4 = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
      NoteBlockInstrument var5 = var3.getBlockState(var4.below()).instrument();
      NoteBlockData var6 = this.method2820((byte)var5.ordinal(), var2, var3, var4);
      return var6 == null ? Instrument.PIANO : var6.instrument;
   }

   @Override
   public boolean method2800(Material type) {
      return this.aNC.contains(type);
   }

   @Nullable
   public NoteBlockData method2818(BlockState var1, ServerLevel var2, BlockPos var3) {
      NoteBlockInstrument var4 = var2.getBlockState(var3.below()).instrument();
      return this.method2819((byte)var4.ordinal(), var1, var2, var3);
   }

   public NoteBlockData method2819(byte var1, BlockState var2, ServerLevel var3, BlockPos var4) {
      NoteBlockData var5 = this.method2820(var1, var2, var3, var4);
      if (var5.active) {
         var3.sendParticles(ParticleTypes.NOTE, var4.getX() + 0.5, var4.getY() + 1.2, var4.getZ() + 0.5, 1, 0.0, 0.0, 0.0, var5.note / 24.0);
      }

      var3.playSeededSound(
         (Player)null,
         var4.getX() + 0.5,
         var4.getY() + 0.5,
         var4.getZ() + 0.5,
         (Holder)var5.handle,
         SoundSource.RECORDS,
         3.0F,
         var5.volume,
         var3.getRandom().nextLong()
      );
      return var5;
   }

   public NoteBlockData method2820(byte var1, BlockState var2, Level var3, BlockPos var4) {
      NoteBlockInstrument var5 = var3.getBlockState(var4.above()).instrument();
      NoteBlockInstrument var6 = NoteBlockInstrument.values()[var1];
      int var7 = (Integer)var2.getValue(NoteBlock.NOTE);
      NoteBlockData var8 = new NoteBlockData();
      var8.note = (byte)var7;
      Holder var9;
      if (var5.worksAboveNoteBlock()) {
         Identifier var10 = method2821(var3, var4);
         if (var5.hasCustomSound() && var10 != null) {
            var9 = Holder.direct(SoundEvent.createVariableRangeEvent(var10));
            var8.customSound = var10.toString();
         } else {
            var9 = var5.getSoundEvent();
         }

         var8.instrument = InstrumentRegistry.instrumentOf((byte)var5.ordinal());
         var8.volume = var5.isTunable() ? NoteBlock.getPitchFromNote(var7) : 1.0F;
      } else {
         var9 = var6.getSoundEvent();
         var8.instrument = InstrumentRegistry.instrumentOf((byte)var6.ordinal());
         var8.volume = var6.isTunable() ? NoteBlock.getPitchFromNote(var7) : 1.0F;
         var8.active = true;
      }

      var8.handle = var9;
      return var8;
   }

   @Nullable
   private static Identifier method2821(Level var0, BlockPos var1) {
      return var0.getBlockEntity(var1.above()) instanceof SkullBlockEntity var3 ? var3.getNoteBlockSound() : null;
   }

   @Override
   public void ck(@Nullable Player player) {
      MinecraftServer var2 = MinecraftServer.getServer();
      PlayerList var3 = var2.getPlayerList();
      if (this.aNB == null) {
         Registry var4 = var2.registries().compositeAccess().lookupOrThrow(BlockTags.MINEABLE_WITH_AXE.registry());
         Named var5 = (Named)var4.get(BlockTags.MINEABLE_WITH_AXE).get();
         List var6 = (List)ServerCompat.getFieldValueOn(Named.class, List.class, var5, 0);
         ArrayList var7 = new ArrayList(var6);
         var7.removeIf(
            var0 -> ((net.minecraft.world.level.block.Block)var0.value()).equals(Blocks.CHORUS_PLANT)
               || ((net.minecraft.world.level.block.Block)var0.value()).equals(Blocks.NOTE_BLOCK)
               || ((net.minecraft.world.level.block.Block)var0.value()).equals(Blocks.BROWN_MUSHROOM_BLOCK)
               || ((net.minecraft.world.level.block.Block)var0.value()).equals(Blocks.RED_MUSHROOM_BLOCK)
               || ((net.minecraft.world.level.block.Block)var0.value()).equals(Blocks.MUSHROOM_STEM)
         );

         try {
            ServerCompat.setFieldByType(Named.class, List.class, var5, var7);
            this.aNB = new ClientboundUpdateTagsPacket(TagNetworkSerialization.serializeTagsToNetwork(var2.registries()));
         } catch (IllegalAccessException var16) {
            throw new RuntimeException(var16);
         } finally {
            try {
               ServerCompat.setFieldByType(Named.class, List.class, var5, var6);
            } catch (IllegalAccessException var15) {
               throw new RuntimeException(var15);
            }
         }
      }

      if (player == null) {
         for (ServerPlayer var20 : var3.players) {
            var20.connection.send(this.aNB);
         }
      } else {
         ServerPlayer var19 = var3.getPlayer(player.getUniqueId());
         if (var19 != null) {
            var19.connection.send(this.aNB);
         }
      }
   }
}
