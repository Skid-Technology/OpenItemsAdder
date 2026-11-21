package dev.lone.itemsadder.world;

import com.google.common.collect.Maps;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.internal.SwitchMap8;
import dev.lone.itemsadder.nms.NmsBlockAdapter;
import dev.lone.itemsadder.nms.NmsRegistry;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Chunk;
import org.bukkit.HeightMap;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

public final class InstrumentRegistry {
   private final NmsBlockAdapter adapter;
   private static InstrumentRegistry instance;
   public HashMap categoryValues = new HashMap();
   private static final Map instrumentsByOrdinal = Maps.newHashMap();

   @Nullable
   public static Instrument instrumentOf(byte var0) {
      return (Instrument)instrumentsByOrdinal.get(Integer.valueOf(var0));
   }

   InstrumentRegistry() {
      this.adapter = (NmsBlockAdapter)NmsRegistry.method2777(NmsBlockAdapter.class);

      for (MaterialCategory var4 : MaterialCategory.values()) {
         Field var5 = FieldUtils.getField(this.adapter.Nk(), var4.name(), true);
         if (var5 != null) {
            try {
               this.categoryValues.put(var4, FieldUtils.readStaticField(var5));
            } catch (IllegalAccessException var7) {
            }
         }
      }
   }

   public static NmsBlockAdapter getAdapter() {
      return getInstance().adapter;
   }

   private static InstrumentRegistry getInstance() {
      if (instance == null) {
         instance = new InstrumentRegistry();
      }

      return instance;
   }

   @Deprecated
   public static boolean isCategoryAt(Block block, MaterialCategory category) {
      return getInstance().adapter.method2794(block, category);
   }

   public static boolean isTaggedBlock(Block var0, Tag var1) {
      return var1.isTagged(var0.getType());
   }

   public static boolean aQ(Block block) {
      return getInstance().adapter.aQ(block);
   }

   public static boolean method2792(Material material) {
      return getInstance().adapter.method2795(material);
   }

   public static boolean aR(Block block) {
      return getInstance().adapter.aR(block);
   }

   public static boolean aP(Block block) {
      return getInstance().adapter.aP(block);
   }

   public static boolean isTallGrass(Block var0) {
      return var0.getType() == Material.TALL_GRASS || var0.getType() == Material.LARGE_FERN;
   }

   public static boolean aT(Block block) {
      return getInstance().adapter.aT(block);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static boolean isInstrumentBlocked(Block var0) {
      Material var1 = var0.getType();
      if (CustomBlockDefinition.isCustomBlockMaterial(var1) && ContentManager.isCustomBlock(var0)) {
         return false;
      }

      boolean var2 = var1.isInteractable();
      if (!var2) {
         return false;
      }

      switch (SwitchMap8.aNu[var1.ordinal()]) {
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
            return false;
         default:
            return true;
      }
   }

   public static boolean isFurnace(Block var0) {
      Material var1 = var0.getType();
      return var1 == Material.FURNACE || var1 == Material.BLAST_FURNACE || var1 == Material.SMOKER;
   }

   public static boolean aW(Block block) {
      return getInstance().adapter.aW(block);
   }

   public static int getHeightAt(Chunk chunk, int var1, int var2, HeightMap heightMap) {
      return getInstance().adapter.method2796(chunk, var1, var2, heightMap);
   }

   static {
      for (Instrument var3 : Instrument.values()) {
         instrumentsByOrdinal.put(var3.ordinal(), var3);
      }
   }
}
