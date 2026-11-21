package dev.lone.itemsadder.nms;

import dev.lone.itemsadder.internal.SwitchMap17;
import dev.lone.itemsadder.util.ServerCompat;
import dev.lone.itemsadder.world.InstrumentRegistry;
import dev.lone.itemsadder.world.MaterialCategory;
import dev.lone.itemsadder.world.NoteBlockData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.particles.Particles;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.tags.TagNetworkSerialization;
import net.minecraft.tags.TagsBlock;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.BlockNote;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ITileEntity;
import net.minecraft.world.level.block.entity.TileEntitySkull;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockPropertyInstrument;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.HeightMap.Type;
import net.minecraft.world.level.material.MaterialMapColor;
import org.bukkit.HeightMap;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_21_R5.CraftChunk;
import org.bukkit.craftbukkit.v1_21_R5.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R5.block.CraftBlock;
import org.bukkit.craftbukkit.v1_21_R5.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class NmsBlockAdapterImplI implements NmsBlockAdapter {
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
      return MaterialMapColor.class;
   }

   @Deprecated
   @Override
   public boolean method2794(Block block, MaterialCategory category) {
      throw new RuntimeException("Not supported on 1.20");
   }

   @Override
   public boolean aQ(Block block) {
      WorldServer var2 = ((CraftWorld)block.getWorld()).getHandle();
      BlockPosition var3 = new BlockPosition(block.getX(), block.getY(), block.getZ());
      return var2.r(var3);
   }

   @Override
   public boolean method2795(Material material) {
      return CraftMagicNumbers.getBlock(material) instanceof ITileEntity;
   }

   @Override
   public boolean aR(Block block) {
      WorldServer var2 = ((CraftWorld)block.getWorld()).getHandle();
      BlockPosition var3 = new BlockPosition(block.getX(), block.getY(), block.getZ());
      Chunk var4 = var2.d(block.getX() >> 4, block.getZ() >> 4);
      net.minecraft.world.level.block.Block var5 = var4.a_(var3).b();
      return var5 instanceof ITileEntity;
   }

   @Override
   public boolean aP(Block block) {
      return ((CraftBlock)block).getNMS().v();
   }

   @Override
   public boolean aT(Block block) {
      return ((CraftBlock)block).getNMS().n();
   }

   @Override
   public boolean aW(Block block) {
      return ((CraftBlock)block.getRelative(BlockFace.DOWN)).getNMS().l();
   }

   @Override
   public int method2796(org.bukkit.Chunk chunk, int x, int z, HeightMap heightMap) {
      return method2801(chunk).a(this.method2802(heightMap), x, z);
   }

   public static Chunk method2801(org.bukkit.Chunk var0) {
      return (Chunk)((CraftChunk)var0).getHandle(ChunkStatus.n);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public Type method2802(HeightMap var1) {
      return switch (SwitchMap17.aNL[var1.ordinal()]) {
         case 1 -> Type.e;
         case 2 -> Type.f;
         case 3 -> Type.d;
         case 4 -> Type.c;
         case 5 -> Type.b;
         case 6 -> Type.a;
         default -> throw new EnumConstantNotPresentException(Type.class, var1.name());
      };
   }

   @Override
   public void method2797(InstrumentRegistry blockInfo) {
   }

   @Override
   public int method2798(Material material) {
      Integer var2 = (Integer)aNv.get(material);
      if (var2 == null) {
         var2 = Item.a(CraftMagicNumbers.getItem(material));
         aNv.put(material, var2);
      }

      return var2;
   }

   @Override
   public Instrument aX(Block block) {
      BlockPropertyInstrument var2 = ((CraftBlock)block).getNMS().E();
      return InstrumentRegistry.instrumentOf((byte)var2.ordinal());
   }

   @Nullable
   @Override
   public NoteBlockData aY(Block bukkitBlock) {
      IBlockData var2 = ((CraftBlock)bukkitBlock).getNMS();
      WorldServer var3 = ((CraftWorld)bukkitBlock.getWorld()).getHandle();
      return this.method2803(var2, var3, new BlockPosition(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ()));
   }

   @Nullable
   @Override
   public NoteBlockData method2799(Block bukkitBlock, byte instrumentBelow) {
      IBlockData var3 = ((CraftBlock)bukkitBlock).getNMS();
      WorldServer var4 = ((CraftWorld)bukkitBlock.getWorld()).getHandle();
      return this.method2804(instrumentBelow, var3, var4, new BlockPosition(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ()));
   }

   @Override
   public Instrument aZ(Block bukkitBlock) {
      IBlockData var2 = ((CraftBlock)bukkitBlock).getNMS();
      WorldServer var3 = ((CraftWorld)bukkitBlock.getWorld()).getHandle();
      BlockPosition var4 = new BlockPosition(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
      BlockPropertyInstrument var5 = var3.a_(var4.e()).E();
      NoteBlockData var6 = this.method2805((byte)var5.ordinal(), var2, var3, var4);
      return var6 == null ? Instrument.PIANO : var6.instrument;
   }

   @Override
   public boolean method2800(Material type) {
      return this.aNC.contains(type);
   }

   @Nullable
   public NoteBlockData method2803(IBlockData var1, WorldServer var2, BlockPosition var3) {
      BlockPropertyInstrument var4 = var2.a_(var3.e()).E();
      return this.method2804((byte)var4.ordinal(), var1, var2, var3);
   }

   public NoteBlockData method2804(byte var1, IBlockData var2, WorldServer var3, BlockPosition var4) {
      NoteBlockData var5 = this.method2805(var1, var2, var3, var4);
      if (var5.active) {
         var3.a(Particles.ad, var4.u() + 0.5, var4.v() + 1.2, var4.w() + 0.5, 1, 0.0, 0.0, 0.0, var5.note / 24.0);
      }

      var3.a((EntityHuman)null, var4.u() + 0.5, var4.v() + 0.5, var4.w() + 0.5, (Holder)var5.handle, SoundCategory.c, 3.0F, var5.volume, var3.A.g());
      return var5;
   }

   public NoteBlockData method2805(byte var1, IBlockData var2, World var3, BlockPosition var4) {
      BlockPropertyInstrument var5 = var3.a_(var4.d()).E();
      BlockPropertyInstrument var6 = BlockPropertyInstrument.values()[var1];
      int var7 = (Integer)var2.c(BlockNote.d);
      NoteBlockData var8 = new NoteBlockData();
      var8.note = (byte)var7;
      Holder var9;
      if (var5.e()) {
         MinecraftKey var10 = method2812(var3, var4);
         if (var5.d() && var10 != null) {
            var9 = Holder.a(SoundEffect.a(var10));
            var8.customSound = var10.toString();
         } else {
            var9 = var5.a();
         }

         var8.instrument = InstrumentRegistry.instrumentOf((byte)var5.ordinal());
         var8.volume = var5.b() ? BlockNote.b(var7) : 1.0F;
      } else {
         var9 = var6.a();
         var8.instrument = InstrumentRegistry.instrumentOf((byte)var6.ordinal());
         var8.volume = var6.b() ? BlockNote.b(var7) : 1.0F;
         var8.active = true;
      }

      var8.handle = var9;
      return var8;
   }

   @Nullable
   private static MinecraftKey method2812(World var0, BlockPosition var1) {
      return var0.c_(var1.d()) instanceof TileEntitySkull var3 ? var3.d() : null;
   }

   @Override
   public void ck(@Nullable Player player) {
      MinecraftServer var2 = MinecraftServer.getServer();
      PlayerList var3 = var2.ag();
      if (this.aNB == null) {
         IRegistry var4 = var2.bb().a().f(TagsBlock.bE.a());
         Named var5 = (Named)var4.a(TagsBlock.bE).get();
         List var6 = (List)ServerCompat.getFieldValueOn(Named.class, List.class, var5, 0);
         ArrayList var7 = new ArrayList(var6);
         var7.removeIf(
            var0 -> ((net.minecraft.world.level.block.Block)var0.a()).equals(Blocks.la)
               || ((net.minecraft.world.level.block.Block)var0.a()).equals(Blocks.bf)
               || ((net.minecraft.world.level.block.Block)var0.a()).equals(Blocks.fl)
               || ((net.minecraft.world.level.block.Block)var0.a()).equals(Blocks.fm)
               || ((net.minecraft.world.level.block.Block)var0.a()).equals(Blocks.fn)
         );

         try {
            ServerCompat.setFieldByType(Named.class, List.class, var5, var7);
            this.aNB = new ClientboundUpdateTagsPacket(TagNetworkSerialization.a(var2.bb()));
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
         for (EntityPlayer var20 : var3.l) {
            var20.g.b(this.aNB);
         }
      } else {
         EntityPlayer var19 = var3.a(player.getUniqueId());
         if (var19 != null) {
            var19.g.b(this.aNB);
         }
      }
   }
}
