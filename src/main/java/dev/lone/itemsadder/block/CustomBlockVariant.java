package dev.lone.itemsadder.block;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemKind;
import dev.lone.itemsadder.nms.NmsBlockSetterService;
import dev.lone.itemsadder.util.IntTableConstantsR;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.MaterialTable;
import dev.lone.itemsadder.util.MaterialTableEntry;
import dev.lone.itemsadder.world.BlockEffectsService;
import dev.lone.itemsadder.world.InstrumentRegistry;
import dev.lone.itemsadder.world.MaterialCategory;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.jetbrains.annotations.Nullable;

public class CustomBlockVariant {
   public final CustomBlockDefinition aqW;
   public final int aqX;
   public final ItemKind aqY;
   public Material aqZ;
   public CustomItemDefinition ara;
   public boolean arb;
   public boolean arc;
   public boolean ard;
   public boolean are;
   private int arf;
   private int arg;
   @Nullable
   public final List arh;
   private InventoryHookMode ari = InventoryHookMode.NONE;
   Map arj = new HashMap();
   private boolean ark = false;
   @Nullable
   private BlockData aad;
   private Material arl;
   @Deprecated
   @Nullable
   private final HashMap arm;
   @Nullable
   private Object arn;
   private static final HashMap aro = new HashMap();
   private static final Map arp = new HashMap();
   private static final Map arq = new HashMap();

   public static void Co() {
   }

   public CustomBlockVariant(
      CustomBlockDefinition var1, int var2, ItemKind var3, boolean var4, boolean var5, boolean var6, boolean var7, int var8, int var9, @Nullable List var10
   ) {
      aro.put(Material.CLAY, Instrument.FLUTE);
      aro.put(Material.GOLD_BLOCK, Instrument.BELL);
      aro.put(Material.PACKED_ICE, Instrument.CHIME);
      aro.put(Material.BONE_BLOCK, Instrument.XYLOPHONE);
      aro.put(Material.IRON_BLOCK, Instrument.IRON_XYLOPHONE);
      aro.put(Material.SOUL_SAND, Instrument.COW_BELL);
      aro.put(Material.PUMPKIN, Instrument.DIDGERIDOO);
      aro.put(Material.EMERALD_BLOCK, Instrument.BIT);
      aro.put(Material.HAY_BLOCK, Instrument.BANJO);
      aro.put(Material.GLOWSTONE, Instrument.PLING);
      this.aqW = var1;
      this.aqX = var2;
      this.arb = var4;
      this.arc = var5;
      this.ard = var6;
      this.are = var7;
      this.arf = var8;
      this.arg = var9;
      this.arh = var10;
      if (!var1.getType().isBlock() && var3 == ItemKind.BLOCK) {
         this.aqY = ItemKind.ITEM;
      } else {
         this.aqY = var3;
      }

      this.arl = var1.aps == BlockRealizationMode.ENTITY ? Material.BARRIER : getMaterialById(var2);
      this.arm = new HashMap();
      if (var1.aps == BlockRealizationMode.ENTITY) {
         this.aad = Material.BARRIER.createBlockData();
      } else if (var1.aps == BlockRealizationMode.TILE) {
         this.arl = Material.SPAWNER;
      } else if (var1.aps == BlockRealizationMode.REAL_NOTE) {
         this.aad = bI(var2);
      } else if (var1.aps == BlockRealizationMode.REAL_WIRE) {
         this.aad = bH(var2);
      } else if (var1.aps != BlockRealizationMode.REAL && var1.aps != BlockRealizationMode.REAL_TRANSPARENT) {
         try {
            throw new Exception("Not implemented");
         } catch (Exception var12) {
            var12.printStackTrace();
         }
      } else {
         this.aad = method10024(var2, var1, this.arl, this.arm);
      }

      if (this.aad == null && var1.Ef()) {
         Log.error(
            String.format(
               "&cFailed to load BlockData of &b%s&c with internal id &b%d&c, type &b%s&c. &eCheck storage/blockstates/ and restart.",
               var1.getNamespacedID(),
               var2,
               var1.aps
            )
         );
      }
   }

   public void setDirectionalMode(InventoryHookMode var1) {
      if (var1 == null) {
         var1 = InventoryHookMode.NONE;
      }

      this.ari = var1;
   }

   public InventoryHookMode getDirectionalMode() {
      return this.ari;
   }

   public boolean Fw() {
      return this.ari != null && this.ari != InventoryHookMode.NONE;
   }

   public boolean Fx() {
      return this.ark;
   }

   public void setDirectional(boolean var1) {
      this.ark = var1;
   }

   public void setRotX(int var1) {
      this.arf = var1;
   }

   public void setRotY(int var1) {
      this.arg = var1;
   }

   public int getRotX() {
      return this.arf;
   }

   public int getRotY() {
      return this.arg;
   }

   public static Material getMaterialById(int var0) {
      MaterialTableEntry var1 = MaterialTable.getById(var0);
      return var1 == null ? Material.SPAWNER : var1.material();
   }

   public static BlockRealizationMode getRealizationModeById(int var0) {
      MaterialTableEntry var1 = MaterialTable.getById(var0);
      return var1 == null ? BlockRealizationMode.TILE : var1.type();
   }

   @Nullable
   public static BlockData resolveBlockData(int var0, BlockRealizationMode var1) {
      Map var2 = (Map)arp.get(var1);
      return var2 != null ? (BlockData)var2.get(var0) : null;
   }

   @Nullable
   public static Integer computeStateId(BlockData var0, BlockRealizationMode var1) {
      Map var2 = (Map)arq.get(var1);
      return var2 == null ? null : (Integer)var2.get(var0.getAsString());
   }

   public static BlockData bH(int var0) {
      return resolveBlockData(var0, BlockRealizationMode.REAL_WIRE);
   }

   public static BlockData bI(int var0) {
      return resolveBlockData(var0, BlockRealizationMode.REAL_NOTE);
   }

   @Nullable
   public static MultipleFacing method10023(int var0, @Nullable CustomBlockDefinition var1, Material var2) {
      return method10024(var0, var1, var2, null);
   }

   @Nullable
   public static MultipleFacing method10024(int var0, @Nullable CustomBlockDefinition var1, Material var2, HashMap var3) {
      MaterialTableEntry var4 = MaterialTable.getById(var0);
      if (var4 != null && var4.material() == var2) {
         MultipleFacing var5 = (MultipleFacing)resolveBlockData(var0, var4.type());
         if (var5 != null && var3 != null) {
            for (BlockFace var7 : List.of(BlockFace.DOWN, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.WEST)) {
               var3.put(var7, var5.hasFace(var7));
            }
         }

         return var5;
      } else {
         return null;
      }
   }

   public void method10025(Material var1) {
      this.aqZ = var1;
   }

   public void method10026(CustomItemDefinition var1) {
      this.ara = var1;
   }

   @Deprecated
   public HashMap FA() {
      return this.arm;
   }

   @Nullable
   public BlockData getBlockData() {
      return this.aad;
   }

   public Material getMaterial() {
      return this.arl;
   }

   public boolean method10027(Material var1) {
      return this.aqW.aps.resolveMaterial(this.arl).equals(var1);
   }

   public void method10028(Block var1, boolean var2) {
      this.method10029(var1, var2);
      if (Settings.get().fixLiquidsFlow && this.aqW.aps.isBlockBacked()) {
         BlockEffectsService.getAdapter().replaceFluidsAround(var1);
      }
   }

   public void method10029(Block var1, boolean var2) {
      if (!this.aF(var1)) {
         if (this.aad != null) {
            if (var2) {
               var1.setBlockData(this.aad, false);
            } else {
               NmsBlockSetterService.OD().setBlockData(var1, this.aad);
            }
         }
      }
   }

   public void aE(Block var1) {
      if (!this.aF(var1)) {
         if (this.aad != null) {
            NmsBlockSetterService.OD().method3403(var1, this.aad, true);
         }
      }
   }

   private boolean aF(Block var1) {
      if (this.aqW.aps == BlockRealizationMode.TILE) {
         BlockEffectsService.getAdapter().placeTileBlock(var1, this.aqW);
         return true;
      } else {
         return false;
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public void method10030(Location var1) {
      if (this.aqW.aps != BlockRealizationMode.REAL_WIRE) {
         if (this.aqZ != null) {
            var1.getWorld()
               .spawnParticle(
                  Particle.BLOCK,
                  var1.clone().add(0.5, 0.5, 0.5),
                  40,
                  0.2333333333333333,
                  0.2333333333333333,
                  0.2333333333333333,
                  0.09,
                  this.aqZ.createBlockData()
               );
         } else if (this.ara != null) {
            var1.getWorld()
               .spawnParticle(Particle.ITEM, var1.clone().add(0.5, 0.5, 0.5), 40, 0.2333333333333333, 0.2333333333333333, 0.2333333333333333, 0.09, this.ara);
         } else {
            switch (IntTableConstantsR.SWITCH_MAP_A[this.aqY.ordinal()]) {
               case 1:
                  var1.getWorld()
                     .spawnParticle(
                        Particle.BLOCK,
                        var1.clone().add(0.5, 0.5, 0.5),
                        40,
                        0.2333333333333333,
                        0.2333333333333333,
                        0.2333333333333333,
                        0.09,
                        this.aqW.getType().createBlockData()
                     );
                  break;
               case 2:
                  var1.getWorld()
                     .spawnParticle(
                        Particle.ITEM, var1.clone().add(0.5, 0.5, 0.5), 40, 0.2333333333333333, 0.2333333333333333, 0.2333333333333333, 0.09, this.aqW
                     );
                  break;
               case 3:
                  if (this.aad != null) {
                     BlockEffectsService.spawnBlockParticles(var1, this.aad);
                  }
            }
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static String method10031(Instrument var0) {
      return switch (IntTableConstantsR.SWITCH_MAP_B[var0.ordinal()]) {
         case 1 -> "harp";
         case 2 -> "basedrum";
         case 3 -> "snare";
         case 4 -> "hat";
         case 5 -> "bass";
         default -> var0.name().toLowerCase(Locale.ROOT);
      };
   }

   @Nullable
   public static Instrument db(String var0) {
      for (Instrument var4 : Instrument.values()) {
         if (method10031(var4).equals(var0)) {
            return var4;
         }
      }

      return null;
   }

   @Deprecated
   public static Instrument aG(Block var0) {
      return InstrumentRegistry.getAdapter().aX(var0);
   }

   @Deprecated
   public static Instrument aH(Block var0) {
      if (var0.getType() == Material.AIR) {
         return Instrument.PIANO;
      }

      Instrument var1 = Instrument.PIANO;
      if (aro.containsKey(var0.getType())) {
         var1 = (Instrument)aro.get(var0.getType());
      } else if (InstrumentRegistry.isTaggedBlock(var0, Tag.WOOL)) {
         var1 = Instrument.GUITAR;
      } else if (InstrumentRegistry.isCategoryAt(var0, MaterialCategory.STONE)) {
         var1 = Instrument.BASS_DRUM;
      } else if (InstrumentRegistry.isCategoryAt(var0, MaterialCategory.SAND)) {
         var1 = Instrument.SNARE_DRUM;
      } else if (InstrumentRegistry.isCategoryAt(var0, MaterialCategory.SHATTERABLE)) {
         var1 = Instrument.STICKS;
      } else if (InstrumentRegistry.isCategoryAt(var0, MaterialCategory.WOOD)) {
         var1 = Instrument.BASS_GUITAR;
      }

      return var1;
   }

   public Object FD() {
      return this.arn;
   }

   public void method10032(Object var1) {
      this.arn = var1;
   }

   public void setDirectionalBlock(BlockFace var1, CustomBlockDefinition var2) {
      this.arj.put(var1, var2);
   }

   public CustomBlockDefinition getDirectionalBlock(BlockFace var1) {
      CustomBlockDefinition var2 = (CustomBlockDefinition)this.arj.get(var1);
      return var2 == null ? this.aqW : var2;
   }

   public CustomBlockDefinition findDirectionalBlock(int var1, int var2) {
      for (Entry var4 : this.arj.entrySet()) {
         if (((CustomBlockDefinition)var4.getValue()).anF != null
            && ((CustomBlockDefinition)var4.getValue()).anF.arg == var2
            && ((CustomBlockDefinition)var4.getValue()).anF.arf == var1) {
            return (CustomBlockDefinition)var4.getValue();
         }
      }

      return null;
   }

   static {
      for (Material var1 : MaterialTable.getTableMaterials()) {
         if (var1 != Material.FIRE) {
            for (MaterialTableEntry var3 : MaterialTable.getEntriesForMaterial(var1)) {
               if ((!var3.requiresVersion20() || Version.isAtLeast(Version.v1_20_R2)) && (!var3.requiresVersion26() || Version.isAtLeast(Version.v26_1_1))) {
                  BlockData var4 = Bukkit.createBlockData(var3.toBlockStateString());
                  arp.computeIfAbsent(var3.type(), var0 -> new HashMap<>()).put(var3.id(), var4);
                  arq.computeIfAbsent(var3.type(), var0 -> new HashMap<>()).put(var4.getAsString(), var3.id());
               }
            }
         }
      }
   }
}
