package dev.lone.itemsadder.util;

import dev.lone.itemsadder.block.BlockRealizationMode;
import dev.lone.itemsadder.internal.SwitchMap71;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public final class MaterialTable {
   private static final String[] NOTE_INSTRUMENTS = new String[]{
      "harp",
      "basedrum",
      "snare",
      "hat",
      "bass",
      "flute",
      "bell",
      "guitar",
      "chime",
      "xylophone",
      "iron_xylophone",
      "cow_bell",
      "didgeridoo",
      "bit",
      "banjo",
      "pling"
   };
   private static final String[] NOTE_MOB_HEADS = new String[]{"zombie", "skeleton", "creeper", "dragon", "wither_skeleton", "piglin", "custom_head"};
   private static final String[] NOTE_TRUMPETS = new String[]{"trumpet", "trumpet_exposed", "trumpet_oxidized", "trumpet_weathered"};
   private static final Map entriesById = new HashMap();
   private static final Map entriesByMaterial = new HashMap();
   private static final Map stateIndex = new HashMap();
   private static final Map entriesByMode = new HashMap();

   private MaterialTable() {
   }

   private static void registerEntry(int var0, BlockRealizationMode var1, Material var2, String var3, int var4) {
      MaterialTableEntry var5 = new MaterialTableEntry(var0, var1, var2, var3, var4);
      if (var1 != BlockRealizationMode.FIRE && entriesById.put(var0, var5) != null) {
         throw new IllegalStateException("Duplicate internal block ID " + var0);
      }

      if (stateIndex.computeIfAbsent(var2, var0x -> new HashMap<>()).put(normalizeState(var3), var5) != null) {
         throw new IllegalStateException("Duplicate block state " + var5.toBlockStateString());
      }

      entriesByMaterial.computeIfAbsent(var2, var0x -> new ArrayList<>()).add(var5);
      entriesByMode.computeIfAbsent(var1, var0x -> new ArrayList<>()).add(var5);
   }

   private static String formatFacingState(int var0) {
      return "[down="
         + getBit(var0, 5)
         + ",east="
         + getBit(var0, 4)
         + ",north="
         + getBit(var0, 3)
         + ",south="
         + getBit(var0, 2)
         + ",up="
         + getBit(var0, 1)
         + ",west="
         + getBit(var0, 0)
         + "]";
   }

   private static String formatTripwireState(int var0) {
      return "[attached="
         + getBit(var0, 6)
         + ",east="
         + getBit(var0, 5)
         + ",north="
         + getBit(var0, 4)
         + ",south="
         + getBit(var0, 3)
         + ",disarmed="
         + getBit(var0, 2)
         + ",west="
         + getBit(var0, 1)
         + ",powered="
         + getBit(var0, 0)
         + "]";
   }

   private static boolean getBit(int var0, int var1) {
      return (var0 & 1 << var1) != 0;
   }

   private static String formatNoteblockState(String var0, int var1, boolean var2) {
      return "[instrument=" + var0 + ",note=" + var1 + ",powered=" + var2 + "]";
   }

   @Nullable
   public static MaterialTableEntry getById(int var0) {
      return (MaterialTableEntry)entriesById.get(var0);
   }

   @Nullable
   public static MaterialTableEntry findById(BlockRealizationMode var0, int var1) {
      if (var0 == BlockRealizationMode.FIRE) {
         return findByState(Material.FIRE, "[age=" + var1 + "]");
      }

      MaterialTableEntry var2 = (MaterialTableEntry)entriesById.get(var1);
      return var2 != null && var2.type() == var0 ? var2 : null;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Nullable
   public static MaterialTableEntry findForState(BlockRealizationMode var0, Material var1, int var2) {
      return switch (SwitchMap71.bsM[var0.ordinal()]) {
         case 1, 2 -> findByState(var1, formatFacingState(var2));
         case 3 -> {
            if (var2 >= 256 && var2 < 1006) {
               int var3 = var2 - 256;
               boolean var4 = var3 >= 375;
               if (var4) {
                  var3 -= 375;
               }

               yield findByState(var1, formatNoteblockState(NOTE_INSTRUMENTS[1 + var3 / 25], var3 % 25, var4));
            } else {
               yield null;
            }
         }
         case 4 -> findByState(var1, formatTripwireState(var2 - 1007));
         case 5 -> findByState(var1, "[age=" + var2 + "]");
         default -> null;
      };
   }

   @Nullable
   public static MaterialTableEntry findByState(Material var0, String var1) {
      Map var2 = (Map)stateIndex.get(var0);
      return var2 == null ? null : (MaterialTableEntry)var2.get(normalizeState(var1));
   }

   public static List getEntriesForMaterial(Material var0) {
      return entriesByMaterial.getOrDefault(var0, List.of());
   }

   public static List getEntriesForMode(BlockRealizationMode var0) {
      return entriesByMode.getOrDefault(var0, List.of());
   }

   public static List getTableMaterials() {
      return List.of(
         Material.BROWN_MUSHROOM_BLOCK,
         Material.RED_MUSHROOM_BLOCK,
         Material.MUSHROOM_STEM,
         Material.CHORUS_PLANT,
         Material.NOTE_BLOCK,
         Material.TRIPWIRE,
         Material.FIRE
      );
   }

   @Nullable
   private static String normalizeState(String var0) {
      if (var0 != null && var0.startsWith("[") && var0.endsWith("]")) {
         TreeMap var1 = new TreeMap();

         for (String var5 : var0.substring(1, var0.length() - 1).split(",")) {
            int var6 = var5.indexOf(61);
            if (var6 <= 0 || var6 == var5.length() - 1) {
               return null;
            }

            String var7 = var5.substring(0, var6).trim();
            String var8 = var5.substring(var6 + 1).trim();
            if (var1.putIfAbsent(var7, var8) != null) {
               return null;
            }
         }

         return var1.toString();
      } else {
         return null;
      }
   }

   static {
      int var0 = 0;

      for (Material var2 : List.of(Material.BROWN_MUSHROOM_BLOCK, Material.RED_MUSHROOM_BLOCK, Material.MUSHROOM_STEM, Material.CHORUS_PLANT)) {
         for (int var3 = 0; var3 < 63; var3++) {
            registerEntry(
               var0++, var2 == Material.CHORUS_PLANT ? BlockRealizationMode.REAL_TRANSPARENT : BlockRealizationMode.REAL, var2, formatFacingState(var3), 19
            );
         }
      }

      for (String var4 : NOTE_INSTRUMENTS) {
         for (int var5 = 0; var5 <= 1; var5++) {
            for (int var6 = 0; var6 < 25; var6++) {
               if (!var4.equals("harp") || var5 != 0 || var6 != 0) {
                  registerEntry(var0++, BlockRealizationMode.REAL_NOTE, Material.NOTE_BLOCK, formatNoteblockState(var4, var6, var5 != 0), 19);
               }
            }
         }
      }

      for (String var18 : NOTE_MOB_HEADS) {
         for (int var20 = 0; var20 <= 1; var20++) {
            for (int var22 = 0; var22 < 25; var22++) {
               registerEntry(var0++, BlockRealizationMode.REAL_NOTE, Material.NOTE_BLOCK, formatNoteblockState(var18, var22, var20 != 0), 20);
            }
         }
      }

      for (String var19 : NOTE_TRUMPETS) {
         for (int var21 = 0; var21 <= 1; var21++) {
            for (int var23 = 0; var23 < 25; var23++) {
               registerEntry(var0++, BlockRealizationMode.REAL_NOTE, Material.NOTE_BLOCK, formatNoteblockState(var19, var23, var21 != 0), 26);
            }
         }
      }

      for (int var10 = 1; var10 < 128; var10++) {
         registerEntry(var0++, BlockRealizationMode.REAL_WIRE, Material.TRIPWIRE, formatTripwireState(var10), 19);
      }

      for (int var11 = 1; var11 <= 15; var11++) {
         registerEntry(var11, BlockRealizationMode.FIRE, Material.FIRE, "[age=" + var11 + "]", 19);
      }

      entriesByMaterial.replaceAll((var0x, var1) -> List.copyOf(var1));
      entriesByMode.replaceAll((var0x, var1) -> List.copyOf(var1));
   }
}
