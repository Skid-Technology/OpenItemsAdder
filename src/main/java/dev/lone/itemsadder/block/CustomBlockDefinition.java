package dev.lone.itemsadder.block;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemKind;
import dev.lone.itemsadder.item.ItemSoundEffects;
import dev.lone.itemsadder.item.ItemSpecWithTexture;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.item.PlacedBlockItemDefinition;
import dev.lone.itemsadder.nms.NmsBlockSetterService;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.IntVector3;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.Utils;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import dev.lone.itemsadder.world.InstrumentRegistry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.jetbrains.annotations.Nullable;

public class CustomBlockDefinition extends PlacedBlockItemDefinition {
   public static final int anz = 3;
   public static final String anA = "group_3x3x1";
   public int anB = -1;
   public Material anC = Material.GRAY_CONCRETE;
   private CustomBlockDefinition anD;
   public static final BlockFace[] anE = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
   @Nullable
   public CustomBlockVariant anF;
   public ItemSoundEffects acY;
   boolean adR;
   boolean anG;
   boolean anH;
   public int adO;
   public float anI;
   private float anJ;
   public boolean anK;
   ExplosiveBlockSpec anL;
   HashMap anM;
   HashMap anN;
   HashMap anO;
   HashMap anP;
   private boolean anQ;

   public CustomBlockDefinition(
      YamlConfig var1,
      String var2,
      String var3,
      Material var4,
      int var5,
      BlockRealizationMode var6,
      ItemKind var7,
      boolean var8,
      int var9,
      List var10,
      List var11,
      List var12,
      List var13,
      float var14,
      boolean var15,
      boolean var16,
      boolean var17,
      boolean var18,
      int var19,
      int var20,
      @Nullable List var21
   ) {
      super(var1, var2, var3, var4, var6);
      this.anF = new CustomBlockVariant(this, var5, var7, var15, var16, var17, var18, var19, var20, var21);
      this.acY = new ItemSoundEffects();
      this.adR = var8;
      this.adO = Utils.clampInt(var9, 0, 15);
      this.anI = var14;
      this.anM = new HashMap();

      for (String var23 : var10) {
         this.anM.put(var23, ContentManager.method8084().method8155(var23));
      }

      this.anN = new HashMap();

      for (String var27 : var11) {
         this.anN.put(var27, ContentManager.method8084().method8155(var27));
      }

      this.anO = new HashMap();

      for (String var28 : var12) {
         this.anO.put(var28, ContentManager.method8084().method8155(var28));
      }

      this.anP = new HashMap();

      for (String var29 : var13) {
         this.anP.put(var29, ContentManager.method8084().method8155(var29));
      }
   }

   public CustomBlockDefinition cloneDirectional(String var1, IntVector3 var2) {
      if (this.anF == null) {
         Log.error("ERROR cloning '" + this.getNamespacedID() + "'. Somehow the placedBlockModel is null, is this a template?");
         return null;
      } else {
         CustomBlockDefinition var3 = new CustomBlockDefinition(
            this.aoc,
            this.aoi,
            var1,
            this.getType(),
            this.aps.isBlockBacked() ? CustomBlockStateSettings.getInstance().resolveStateId(this.getType(), this.aps, this.aoi, var1) : -1,
            this.aps,
            this.anF.aqY,
            this.adR,
            this.adO,
            this.anM.keySet().stream().toList(),
            this.anN.keySet().stream().toList(),
            this.anO.keySet().stream().toList(),
            this.anP.keySet().stream().toList(),
            this.anI,
            this.anF.arb,
            this.anF.arc,
            this.anF.ard,
            this.anF.are,
            this.anF.getRotX(),
            this.anF.getRotY(),
            null
         );
         var3.method8793(this.aof);
         var3.anC = this.anC;
         var3.setItemMeta(this.getItemMeta());
         var3.acY = this.acY;
         var3.adR = this.adR;
         var3.anG = this.anG;
         var3.anH = this.anH;
         var3.anI = this.anI;
         var3.anK = this.anK;
         var3.anJ = this.anJ;
         var3.setBreakPermissionSuffix(this.getBreakPermissionSuffix());
         var3.setPlacePermissionSuffix(this.getPlacePermissionSuffix());
         var3.cJ(this.getPermissionSuffix());
         this.aoh.copyActionsTo(var3);
         this.aog.method9770(var3, false);
         var3.aV(false);
         var3.method8757(List.of());
         var3.cE(this.nC());
         var3.anD = this.anD == null ? this : this.anD;
         var3.anF.setRotX(var2.x);
         var3.anF.setRotY(var2.y);
         return var3;
      }
   }

   public String getEntityModelKey() {
      CustomBlockDefinition var1 = this.anD == null ? this : this.anD;
      return "_iainternal:entity_blocks/" + var1.getNamespace() + "/" + var1.getId();
   }

   public String getEntityModelGroupKey() {
      return this.getEntityModelKey() + "/group_3x3x1";
   }

   @Nullable
   public Map getEntityModelTextures() {
      CustomBlockDefinition var1 = this.anD == null ? this : this.anD;
      Map var2;
      if (var1.anZ instanceof ItemSpecWithTexture var3) {
         Map var9 = var3.Fd();
         if (isCubeAllModel(var3.Fc()) && var9.size() == 1) {
            var2 = Map.of("all", (String)var9.values().iterator().next());
         } else {
            if (!isCubeModel(var3.Fc())) {
               return null;
            }

            var2 = new HashMap();

            for (String var6 : List.of("down", "up", "north", "south", "west", "east")) {
               String var7 = (String)var9.get(var6);
               if (var7 == null) {
                  return null;
               }

               var2.put(var6, var7);
            }
         }
      } else {
         if (var1.anZ != null) {
            return null;
         }

         List var10 = var1.getTextures();
         if (!isCubeAllModel(var1.DO()) || var10 == null || var10.size() != 1) {
            return null;
         }

         var2 = Map.of("all", (String)var10.get(0));
      }

      for (String var11 : var2.values()) {
         if (ContentManager.method8199(var11) == null) {
            return null;
         }
      }

      return var2;
   }

   private static boolean isCubeAllModel(String var0) {
      return "block/cube_all".equals(var0) || "minecraft:block/cube_all".equals(var0);
   }

   private static boolean isCubeModel(String var0) {
      return "block/cube".equals(var0) || "minecraft:block/cube".equals(var0);
   }

   public void method8847(String var1, float var2, float var3) {
      this.acY.method9993(var1, var2, var3);
   }

   public void method8848(String var1, float var2, float var3) {
      this.acY.method9994(var1, var2, var3);
   }

   public void method8849(String var1, float var2, float var3) {
      this.acY.method9995(var1, var2, var3);
   }

   public void method8850(String var1, float var2, float var3) {
      this.acY.method9996(var1, var2, var3);
   }

   public void method8851(String var1, float var2, float var3) {
      this.acY.method9997(var1, var2, var3);
   }

   public boolean Dj() {
      return this.adR;
   }

   public boolean Dk() {
      return this.anG;
   }

   public boolean Dl() {
      return this.anH;
   }

   public int getLightLevel() {
      return this.adO;
   }

   public void aN(boolean var1) {
      this.anK = var1;
   }

   public void setBreakTime(float var1) {
      if (var1 == -1.0F) {
         this.anJ = this.anI * 3.0F;
      } else {
         this.anJ = var1;
      }
   }

   public float getBreakTime() {
      return this.anJ;
   }

   public void aO(boolean var1) {
      this.anG = var1;
   }

   public void aP(boolean var1) {
      this.anH = var1;
   }

   @Override
   public boolean placeBlockAt(Location var1) {
      return this.placeBlockForPlayerAt(null, var1);
   }

   public boolean placeBlockForPlayerAt(Player var1, Location var2) {
      return this.placeBlockAtLocation(var1, var2, true, false);
   }

   public boolean placeBlockForPlayer(Player var1, Block var2) {
      return this.placeBlockFull(var1, var2, true, false, true, true);
   }

   @Override
   public boolean placeBlockDefault(Block var1) {
      return this.placeBlockFull(null, var1, false, false, true, false);
   }

   @Override
   public boolean placeBlockWithPhysics(Block var1, boolean var2) {
      return this.placeBlockFull(null, var1, false, false, true, false);
   }

   @Override
   public boolean placeBlockNoPhysics(Block var1) {
      return this.placeBlockForPlayer(null, var1);
   }

   @Override
   public boolean placeBlock(Block var1, boolean var2) {
      return this.placeBlockFull(null, var1, true, false, true, true);
   }

   public boolean placeBlockAtLocation(Player var1, Location var2, boolean var3, boolean var4) {
      return this.placeBlockFull(var1, var2.getBlock(), var3, var4, true, true);
   }

   public boolean placeDirectionalAt(Player var1, Location var2, @Nullable BlockFace var3, boolean var4, boolean var5) {
      return this.placeBlockWithFace(var1, var2.getBlock(), var3, var4, var5, true, true, false);
   }

   public boolean placeBlockQuiet(Block var1) {
      return this.placeBlockFull(null, var1, true, false, false, true);
   }

   public boolean placeBlockFull(Player var1, Block var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      return this.placeBlockWithFace(var1, var2, null, var3, var4, var5, var6, true);
   }

   public boolean placeBlockWithFace(Player var1, Block var2, @Nullable BlockFace var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8) {
      if (this.anF == null) {
         Log.error("ERROR placing '" + this.getNamespacedID() + "'. Somehow the placedBlockModel is null, is this a template?");
         return false;
      }

      boolean var10 = var4 && this.anF.arb;
      Block var9;
      if (var10) {
         var9 = var2.getLocation().add(0.0, 1.0, 0.0).getBlock();
         if (var8 && var1 != null && !BlockFaceUtils.simulatePlace(var9, var1)) {
            return false;
         }
      } else {
         var9 = var2;
      }

      if (this.aps == BlockRealizationMode.ENTITY
         && Settings.method2945(this.aps)
         && !ContentManager.method8084()
            .ap
            .canPlaceBlockState(var9.getWorld().getUID(), var9.getX(), var9.getY(), var9.getZ(), Settings.get().blockEntityMaxBlocksPerChunk)) {
         if (var1 != null) {
            Log.sendLocalizedWith(
               var1, "max-furnitures-vehicles-reached", var0 -> var0.replace("{value}", Integer.toString(Settings.get().blockEntityMaxBlocksPerChunk))
            );
         }

         return false;
      } else {
         if (var10) {
            NmsBlockSetterService.OD().method3404(var2, true);
         }

         String var11 = this.getNamespacedID();
         if (!Settings.method2945(this.aps)) {
            if (var1 != null) {
               Log.sendLocalizedWith(var1, "custom-blocks-type-disabled", var1x -> var1x.replace("{type}", this.aps.toString()));
            }
         } else if (this.aps == BlockRealizationMode.TILE) {
            SchedulerUtils.runNowOrLater(() -> this.anF.method10028(var9, var6), var5);
         } else if (this.aps == BlockRealizationMode.REAL_WIRE) {
            this.anF.method10028(var9, var6);
            Block var12 = var9.getRelative(BlockFace.UP);
            if (InstrumentRegistry.isTallGrass(var12)) {
               var12.setType(Material.AIR, false);
            }
         } else if (var3 != null && this.anF.Fw()) {
            BlockFace var16 = var3;
            if (var1 != null) {
               if (this.anF.getDirectionalMode() == InventoryHookMode.DROPPER) {
                  BlockFace var14 = BlockFaceUtils.facingOf(var1);
                  BlockFace var15 = BlockFaceUtils.verticalFacingOf(var1);
                  if (var15 != BlockFace.SELF) {
                     if (var15 == BlockFace.UP) {
                        var16 = BlockFace.DOWN;
                     } else if (var15 == BlockFace.DOWN) {
                        var16 = BlockFace.UP;
                     }
                  } else {
                     var16 = var14.getOppositeFace();
                  }
               } else if (this.anF.getDirectionalMode() == InventoryHookMode.FURNACE) {
                  BlockFace var18 = BlockFaceUtils.facingOf(var1);
                  var16 = var18.getOppositeFace();
               }
            }

            CustomBlockDefinition var13 = this.anF.getDirectionalBlock(var16);
            if (var13 != null) {
               var13.anF.method10028(var9, var6);
               var11 = var13.getNamespacedID();
            }

            if (var13 == null) {
               Log.error("ERROR placing '" + this.getNamespacedID() + "'. Somehow the directional block is null, is this a template?");
               return false;
            }
         } else {
            this.anF.method10028(var9, var6);
         }

         if (this.adO != -1 && var7) {
            BlockUtils.markBlockState(var9, this.adO);
         }

         CustomBlockChunkStoreBase var17 = ContentManager.method8084().method8183(var9);
         var17.setCustomBlock(var9, var11);
         return true;
      }
   }

   @Nullable
   public static CustomBlockDefinition getDefinitionAt(Block var0) {
      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8183(var0);
      return (CustomBlockDefinition)ContentManager.method8084().method8154(var1.getCustomBlockId(var0));
   }

   public static boolean isCustomBlockType(Block var0) {
      return isCustomBlockMaterial(var0.getType());
   }

   @Deprecated
   public static boolean isCustomBlockTypeFlagged(Block var0, boolean var1) {
      return isRealBlockMaterial(var0.getType(), var1);
   }

   public static boolean isCustomBlockMaterial(Material var0) {
      return var0 == Material.NOTE_BLOCK
         || var0 == Material.BROWN_MUSHROOM_BLOCK
         || var0 == Material.RED_MUSHROOM_BLOCK
         || var0 == Material.MUSHROOM_STEM
         || var0 == Material.CHORUS_PLANT
         || var0 == Material.TRIPWIRE
         || var0 == Material.BARRIER
         || var0 == Material.SPAWNER;
   }

   public static boolean isCustomBlockMaterialOrString(Material var0) {
      return var0 == Material.STRING ? true : isCustomBlockMaterial(var0);
   }

   public static boolean isRealBlockMaterial(Material var0, boolean var1) {
      return !var1
         ? isCustomBlockMaterial(var0)
         : var0 == Material.BROWN_MUSHROOM_BLOCK || var0 == Material.RED_MUSHROOM_BLOCK || var0 == Material.MUSHROOM_STEM;
   }

   public static boolean isMushroomMaterial(Material var0) {
      return var0 == Material.BROWN_MUSHROOM_BLOCK || var0 == Material.RED_MUSHROOM_BLOCK || var0 == Material.MUSHROOM_STEM;
   }

   public static boolean isChorusMaterial(Material var0) {
      return var0 == Material.CHORUS_PLANT;
   }

   public static boolean isNoteMaterial(Material var0) {
      return var0 == Material.NOTE_BLOCK;
   }

   public boolean matchesPlacementItem(ItemStack var1) {
      boolean var2 = false;
      if (this.anN.size() == 0 && this.anM.size() == 0) {
         return true;
      }

      String var3 = CustomItemDefinition.isCustomItem(var1) ? CustomItemDefinition.method8750(var1) : null;
      if (this.anN.size() > 0) {
         for (Entry var5 : this.anN.entrySet()) {
            if (((String)var5.getKey()).equals("HAND") && !ItemStackUtils.ap(var1)) {
               var2 = true;
               break;
            }

            if ((Boolean)var5.getValue()) {
               if (CustomItemDefinition.method8783(var1, (String)var5.getKey())) {
                  var2 = true;
                  break;
               }
            } else if (var3 != null) {
               if (var3.contains((CharSequence)var5.getKey())) {
                  var2 = true;
                  break;
               }
            } else if (var1.getType().toString().contains((CharSequence)var5.getKey())) {
               var2 = true;
               break;
            }
         }
      } else {
         var2 = true;
      }

      if (this.anM.size() > 0) {
         for (Entry var7 : this.anM.entrySet()) {
            if (((String)var7.getKey()).equals("HAND")) {
               if (ItemStackUtils.ap(var1)) {
                  continue;
               }

               var2 = false;
            }

            if ((Boolean)var7.getValue()) {
               if (CustomItemDefinition.method8783(var1, (String)var7.getKey())) {
                  var2 = false;
                  break;
               }
            } else if (var3 != null) {
               if (var3.contains((CharSequence)var7.getKey())) {
                  var2 = false;
                  break;
               }
            } else if (var1.getType().toString().contains((CharSequence)var7.getKey())) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public boolean matchesRestrictedItem(ItemStack var1) {
      boolean var2 = false;
      if (this.anP.size() > 0) {
         for (Entry var4 : this.anP.entrySet()) {
            if (((String)var4.getKey()).equals("HAND") && !ItemStackUtils.ap(var1)) {
               var2 = true;
               break;
            }

            if ((Boolean)var4.getValue()) {
               if (CustomItemDefinition.method8783(var1, (String)var4.getKey())) {
                  var2 = true;
                  break;
               }
            } else if (CustomItemDefinition.isCustomItem(var1)) {
               if (CustomItemDefinition.method8750(var1).contains((CharSequence)var4.getKey())) {
                  var2 = true;
                  break;
               }
            } else if (var1.getType().toString().contains((CharSequence)var4.getKey())) {
               var2 = true;
               break;
            }
         }
      } else {
         var2 = true;
      }

      if (this.anO.size() > 0) {
         for (Entry var6 : this.anO.entrySet()) {
            if (((String)var6.getKey()).equals("HAND")) {
               if (ItemStackUtils.ap(var1)) {
                  continue;
               }

               var2 = false;
            }

            if ((Boolean)var6.getValue()) {
               if (CustomItemDefinition.method8783(var1, (String)var6.getKey())) {
                  var2 = false;
               }
            } else if (CustomItemDefinition.isCustomItem(var1)) {
               if (CustomItemDefinition.method8750(var1).contains((CharSequence)var6.getKey())) {
                  var2 = true;
                  break;
               }
            } else if (var1.getType().toString().contains((CharSequence)var6.getKey())) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public static boolean isCustomBlockDefinition(CustomItemDefinition var0) {
      return var0 instanceof CustomBlockDefinition;
   }

   public void applyInventoryBlockState(boolean var1) {
      this.anQ = var1;
      if (var1) {
         if (this.aps.isFullyRealized() && this.getItemMeta() instanceof BlockDataMeta var2) {
            BlockData var4 = this.anF.getBlockData();
            if (var4 == null) {
               throw new IllegalStateException("Missing inventory blockstate for " + this.getNamespacedID());
            }

            var2.setBlockData(var4);
            if (!this.setItemMeta(var2)) {
               throw new IllegalStateException("Cannot apply inventory blockstate for " + this.getNamespacedID());
            }

            this.DC();
         } else {
            throw new IllegalStateException("Block item representation is unavailable for " + this.getNamespacedID());
         }
      }
   }

   @Generated
   public ExplosiveBlockSpec getExplosiveSpec() {
      return this.anL;
   }

   @Generated
   public void setExplosiveSpec(ExplosiveBlockSpec var1) {
      this.anL = var1;
   }

   @Generated
   public boolean isInventoryBlockStateApplied() {
      return this.anQ;
   }
}
