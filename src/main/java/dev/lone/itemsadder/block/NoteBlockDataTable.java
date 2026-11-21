package dev.lone.itemsadder.block;

import dev.lone.itemsadder.util.IntTableConstantsH;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.type.Fire;
import org.bukkit.block.data.type.NoteBlock;

public class NoteBlockDataTable {
   public static final BlockData agL = method9655(Material.BROWN_MUSHROOM_BLOCK);
   public static final BlockData agM = method9655(Material.RED_MUSHROOM_BLOCK);
   public static final BlockData agN = method9655(Material.MUSHROOM_STEM);
   public static final BlockData agO = method9655(Material.CHORUS_PLANT);
   public static final NoteBlock agP = (NoteBlock)method9655(Material.NOTE_BLOCK);
   public static final BlockData agQ = method9655(Material.FIRE);
   public static final BlockData agR = method9655(Material.TRIPWIRE);
   public static final BlockData agS = Bukkit.createBlockData(Material.AIR);

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static BlockData method9655(Material var0) {
      switch (IntTableConstantsH.SWITCH_MAP[var0.ordinal()]) {
         case 1:
            NoteBlock var1 = (NoteBlock)Bukkit.createBlockData(var0);
            var1.setNote(new Note(0));
            var1.setPowered(false);
            var1.setInstrument(Instrument.PIANO);
            return var1;
         case 2:
         case 3:
         case 4:
         case 5:
            BlockData var2 = Bukkit.createBlockData(var0);
            MultipleFacing var3 = (MultipleFacing)var2;
            var3.setFace(BlockFace.NORTH, true);
            var3.setFace(BlockFace.SOUTH, true);
            var3.setFace(BlockFace.EAST, true);
            var3.setFace(BlockFace.WEST, true);
            var3.setFace(BlockFace.DOWN, true);
            var3.setFace(BlockFace.UP, true);
            return var3;
         case 6:
            Fire var4 = (Fire)Bukkit.createBlockData(var0);
            var4.setAge(0);
            return var4;
         case 7:
            return Bukkit.createBlockData(var0);
         default:
            return Bukkit.createBlockData(var0);
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static BlockData method9656(Material var0) {
      switch (IntTableConstantsH.SWITCH_MAP[var0.ordinal()]) {
         case 1:
            return agP;
         case 2:
            return agN;
         case 3:
            return agL;
         case 4:
            return agM;
         case 5:
            return agO;
         case 6:
            return agQ;
         case 7:
            return agR;
         default:
            return null;
      }
   }
}
