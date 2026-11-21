package dev.lone.itemsadder.nms;

import dev.lone.itemsadder.block.CustomBlockVariant;
import dev.lone.itemsadder.world.InstrumentRegistry;
import dev.lone.itemsadder.world.MaterialCategory;
import dev.lone.itemsadder.world.NoteBlockData;
import java.util.HashMap;
import org.bukkit.Chunk;
import org.bukkit.HeightMap;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface NmsBlockAdapter {
   HashMap aNv = new HashMap();

   Class Nk();

   boolean method2794(Block var1, MaterialCategory var2);

   boolean aQ(Block var1);

   boolean method2795(Material var1);

   boolean aR(Block var1);

   boolean aP(Block var1);

   boolean aT(Block var1);

   boolean aW(Block var1);

   int method2796(Chunk var1, int var2, int var3, HeightMap var4);

   void method2797(InstrumentRegistry var1);

   int method2798(Material var1);

   default Instrument aX(Block block) {
      return CustomBlockVariant.aH(block);
   }

   @Nullable
   NoteBlockData aY(Block var1);

   @Nullable
   NoteBlockData method2799(Block var1, byte var2);

   default Instrument aZ(Block bukkitBlock) {
      return this.aX(bukkitBlock.getRelative(BlockFace.DOWN));
   }

   boolean method2800(Material var1);

   void ck(@Nullable Player var1);
}
