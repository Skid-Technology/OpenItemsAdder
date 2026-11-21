package dev.lone.itemsadder.block;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.internal.SwitchMap79;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ToolDurabilitySpec;
import dev.lone.itemsadder.resourcepack.JsonAssetWithCallbacks;
import dev.lone.itemsadder.util.JsonHelper;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.MaterialTable;
import dev.lone.itemsadder.util.MaterialTableEntry;
import dev.lone.itemsadder.util.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Nullable;

public class BlockStateMapper {
   private final Collection bIb;
   private final JSONArray bIc;
   private static final List bId = new ArrayList();

   public BlockStateMapper(Collection var1) {
      this.bIb = var1;
      this.bIc = new JsonAssetWithCallbacks("internal/multipart_entry/fire.json").getArray("multipart");
   }

   private void mergeBlockstateFile(File var1, String var2, JSONObject var3) {
      File var4 = new File(var1, var2 + ".json");
      if (var4.exists()) {
         try {
            JSONObject var5 = JsonHelper.readJsonFile(var4);
            if (var5.containsKey("variants")) {
               JSONObject var6 = var5.getJSONObject("variants");
               if (var3.containsKey("variants")) {
                  JSONObject var7 = var3.getJSONObject("variants");

                  for (Entry var9 : var6.entrySet()) {
                     String var10 = getBlockStateString(var2, (String)var9.getKey());
                     if (var7.containsKey(var10)) {
                        Log.warn("Duplicate variant '" + (String)var9.getKey() + "' for blockstate '" + var2 + "'. File: " + Utils.shortPath(var4));
                     } else {
                        var3.getJSONObject("variants").put((String)var9.getKey(), var9.getValue());
                     }
                  }
               } else {
                  var3.put("variants", var6);
               }

               for (Entry var18 : var6.entrySet()) {
                  BlockData var19 = createBlockDataForState(var2, (String)var18.getKey());
                  BlockRealizationMode var20 = null;
                  switch (var2) {
                     case "note_block":
                        var20 = BlockRealizationMode.REAL_NOTE;
                        break;
                     case "brown_mushroom_block":
                     case "red_mushroom_block":
                     case "mushroom_stem":
                        var20 = BlockRealizationMode.REAL;
                        break;
                     case "chorus_plant":
                        var20 = BlockRealizationMode.REAL_TRANSPARENT;
                        break;
                     case "tripwire":
                        var20 = BlockRealizationMode.REAL_WIRE;
                  }

                  if (var20 != null) {
                     Integer var21 = CustomBlockVariant.computeStateId(var19, var20);
                     CustomBlockDefinition var22 = (CustomBlockDefinition)ContentManager.method8084().an.get(var21);
                     String var13 = null;
                     if (var22 != null) {
                        if (var18.getValue() instanceof JSONObject) {
                           JSONObject var14 = (JSONObject)var18.getValue();
                           if (var14.containsKey("model")) {
                              String var15 = var14.getString("model");
                              var15 = var15.replace("auto_generated", "ia_auto");
                              if (var15.equals(var22.method8734(false, true))) {
                                 continue;
                              }
                           }

                           var13 = ((JSONObject)var18.getValue()).getString("model");
                        } else if (var18.getValue() instanceof JSONArray) {
                           if (var22.anF != null && var22.anF.arh != null) {
                              continue;
                           }

                           var13 = ((JSONArray)var18.getValue()).getJSONObject(0).getString("model");
                        }

                        if (var13 != null) {
                           Log.warn(
                              "Manual '"
                                 + var2
                                 + ".json' block variant is already used by ItemsAdder custom block '"
                                 + var22.getNamespacedID()
                                 + "'\n-> Variant attribute: "
                                 + (String)var18.getKey()
                                 + "\n-> Model attribute: "
                                 + var13
                                 + "\n-> Block config: "
                                 + var22.DJ()
                                 + "\n-> Blockstate file: "
                                 + Utils.shortPath(var4)
                           );
                        }

                        CustomBlockStateSettings.getInstance().registerCustomBlockOwner(var21, var13 + ". File: " + Utils.shortPath(var4));
                     }
                  }
               }

               if (var3.getJSONObject("variants").entrySet().size() > 1 && var3.getJSONObject("variants").containsKey("")) {
                  var3.getJSONObject("variants").remove("");
               }
            }

            if (var5.containsKey("multipart")) {
               Log.warn(
                  "Custom multipart blockstate not supported for now by ItemsAdder, it will be ignored. Consider using 'variants'. File: "
                     + Utils.shortPath(var4)
               );
            }
         } catch (Throwable var16) {
            Log.error("Error opening your custom blockstates file '" + Utils.shortPath(var4) + "'. Please make sure it's valid or delete it.");
            var16.printStackTrace();
         }
      }
   }

   public static BlockData createBlockDataForState(String var0, String var1) {
      return Bukkit.createBlockData(var0 + "[" + var1 + "]");
   }

   public static String getBlockStateString(String var0, String var1) {
      return createBlockDataForState(var0, var1).getAsString().replace("minecraft:", "");
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public HashMap buildBlockstateOverlays() {
      CustomBlockStateSettings.getInstance().clearBlockOwners();
      JSONObject var1 = new JsonAssetWithCallbacks("internal/blockstates/note_block.json").copyAsObject();
      JSONObject var2 = new JsonAssetWithCallbacks("internal/blockstates/chorus_plant.json").copyAsObject();
      JSONObject var3 = new JsonAssetWithCallbacks("internal/blockstates/brown_mushroom_block.json").copyAsObject();
      JSONObject var4 = new JsonAssetWithCallbacks("internal/blockstates/red_mushroom_block.json").copyAsObject();
      JSONObject var5 = new JsonAssetWithCallbacks("internal/blockstates/mushroom_stem.json").copyAsObject();
      JSONObject var6 = new JsonAssetWithCallbacks("internal/blockstates/tripwire.json").copyAsObject();
      JSONObject var7 = new JSONObject();
      JSONObject var8 = JsonHelper.getObjectOr(var1, "variants", new JSONObject());
      JSONObject var9 = JsonHelper.getObjectOr(var2, "variants", new JSONObject());
      JSONObject var10 = JsonHelper.getObjectOr(var3, "variants", new JSONObject());
      JSONObject var11 = JsonHelper.getObjectOr(var4, "variants", new JSONObject());
      JSONObject var12 = JsonHelper.getObjectOr(var5, "variants", new JSONObject());
      JSONObject var13 = JsonHelper.getObjectOr(var6, "variants", new JSONObject());
      JSONArray var14 = new JSONArray();
      ArrayList var15 = new ArrayList();
      ArrayList var16 = new ArrayList();

      for (File var19 : ContentManager.method8084().field87) {
         File var20 = new File(var19, "minecraft/blockstates");
         if (var20.exists()) {
            this.mergeBlockstateFile(var20, "note_block", var1);
            this.mergeBlockstateFile(var20, "chorus_plant", var2);
            this.mergeBlockstateFile(var20, "brown_mushroom_block", var3);
            this.mergeBlockstateFile(var20, "red_mushroom_block", var4);
            this.mergeBlockstateFile(var20, "mushroom_stem", var5);
            this.mergeBlockstateFile(var20, "tripwire", var6);
         }
      }

      ArrayList var28 = new ArrayList();

      for (CustomItemDefinition var32 : this.bIb) {
         if (var32 instanceof CustomBlockDefinition var35) {
            switch (SwitchMap79.bIf[var35.aps.ordinal()]) {
               case 1:
                  if (Settings.get().customBlocksMushroom) {
                     JSONObject var40 = switch (SwitchMap79.bIe[var35.anF.getMaterial().ordinal()]) {
                        case 1 -> var10;
                        case 2 -> var11;
                        case 3 -> var12;
                        default -> null;
                     };
                     if (var40 != null) {
                        this.addCustomVariant(var35.anF, var40, var35.anF.aqX, var35);
                     }
                  }
                  break;
               case 2:
                  if (Settings.get().customBlocksChorus) {
                     this.addCustomVariant(var35.anF, var9, var35.anF.aqX, var35);
                  }
                  break;
               case 3:
                  if (Settings.get().customBlocksNote) {
                     MaterialTableEntry var22 = MaterialTable.getById(var35.anF.aqX);
                     if (var22 != null && var22.requiresVersion26()) {
                        var16.add(var35);
                     } else if (var22 != null && var22.requiresVersion20()) {
                        var15.add(var35);
                     } else {
                        this.addCustomVariant(var35.anF, var8, var35.anF.aqX, var35);
                     }
                  }
                  break;
               case 4:
                  if (Settings.get().customBlocksWire) {
                     this.addCustomVariant(var35.anF, var13, var35.anF.aqX, var35);
                  }
               case 5:
               case 6:
               case 7:
            }
         } else if (var32 instanceof AgeableBlockDefinition && Settings.get().customBlocksFire) {
            AgeableBlockDefinition var21 = (AgeableBlockDefinition)var32;
            if (var21.apn.ticks >= 1 && var21.apn.ticks <= 15) {
               var14.addAll(this.buildFireMultipart(var21, var21.apn.ticks));
               var28.add(var21.apn.ticks);
            } else {
               Log.error("Error: Seems you used all the available FIRE blocks! (max is 15).");
            }
         }
      }

      for (Material var33 : MaterialTable.getTableMaterials()) {
         if (var33 != Material.FIRE
            && (var33 != Material.NOTE_BLOCK || Settings.get().customBlocksNote)
            && (var33 != Material.TRIPWIRE || Settings.get().customBlocksWire)
            && (var33 != Material.CHORUS_PLANT || Settings.get().customBlocksChorus)
            && (var33 == Material.NOTE_BLOCK || var33 == Material.TRIPWIRE || var33 == Material.CHORUS_PLANT || Settings.get().customBlocksMushroom)) {
            JSONObject var36 = switch (SwitchMap79.bIe[var33.ordinal()]) {
               case 1 -> var10;
               case 2 -> var11;
               case 3 -> var12;
               case 4 -> var9;
               case 5 -> var8;
               case 6 -> var13;
               default -> throw new IllegalStateException("Unexpected block-state material " + var33);
            };

            String var41 = switch (SwitchMap79.bIe[var33.ordinal()]) {
               case 1 -> "block/original/brown_mushroom_block";
               case 2 -> "block/original/red_mushroom_block";
               case 3 -> "block/original/mushroom_stem";
               case 4 -> "block/original/chorus_plant";
               case 5 -> "block/original/note_block";
               case 6 -> "block/tripwire_attached_n";
               default -> throw new IllegalStateException("Unexpected block-state material " + var33);
            };

            for (MaterialTableEntry var24 : MaterialTable.getEntriesForMaterial(var33)) {
               if (var24.minClientVersion() == 19) {
                  this.addVariant(var36, var24.id(), var24.type(), var33, var41);
               }
            }
         }
      }

      JSONObject var31 = JsonHelper.copyObject(var1);
      JSONObject var34 = JsonHelper.copyObject(var8);

      for (MaterialTableEntry var42 : MaterialTable.getEntriesForMaterial(Material.NOTE_BLOCK)) {
         if (var42.requiresVersion20()) {
            this.addVariant(var34, var42.id(), var42.type(), Material.NOTE_BLOCK, "block/original/note_block");
         }
      }

      for (CustomBlockDefinition var43 : var15) {
         this.addCustomVariant(var43.anF, var34, var43.anF.aqX, var43);
      }

      var31.put("variants", var34);
      JSONObject var39 = JsonHelper.copyObject(var1);
      JSONObject var44 = JsonHelper.copyObject(var34);

      for (MaterialTableEntry var49 : MaterialTable.getEntriesForMaterial(Material.NOTE_BLOCK)) {
         if (var49.requiresVersion26()) {
            this.addVariant(var44, var49.id(), var49.type(), Material.NOTE_BLOCK, "block/original/note_block");
         }
      }

      for (CustomBlockDefinition var50 : var16) {
         this.addCustomVariant(var50.anF, var44, var50.anF.aqX, var50);
      }

      var39.put("variants", var44);
      if (Settings.get().customBlocksFire) {
         for (int var47 = 0; var47 <= 15; var47++) {
            if (!var28.contains(var47)) {
               JSONArray var51 = JsonHelper.copyArray(this.bIc);

               for (Object var26 : var51) {
                  JSONObject var27 = (JSONObject)var26;
                  this.setAgeCondition(var27, var47);
               }

               var14.addAll(var51);
            }
         }
      }

      var3.put("variants", var10);
      var4.put("variants", var11);
      var5.put("variants", var12);
      var2.put("variants", var9);
      var1.put("variants", var8);
      var7.put("multipart", var14);
      var6.put("variants", var13);
      HashMap var48 = new HashMap();
      if (Settings.get().customBlocksMushroom) {
         var48.putAll(this.wrapBlockstateFile(Material.BROWN_MUSHROOM_BLOCK, var3));
         var48.putAll(this.wrapBlockstateFile(Material.RED_MUSHROOM_BLOCK, var4));
         var48.putAll(this.wrapBlockstateFile(Material.MUSHROOM_STEM, var5));
      }

      if (Settings.get().customBlocksChorus) {
         var48.putAll(this.wrapBlockstateFile(Material.CHORUS_PLANT, var2));
      }

      if (Settings.get().customBlocksNote) {
         var48.putAll(this.wrapBlockstateFile(Material.NOTE_BLOCK, var1));
         var48.put("ia_overlay_1_20_blockstates/assets/minecraft/blockstates/note_block.json", var31);
         var48.put("ia_overlay_26_1_blockstates/assets/minecraft/blockstates/note_block.json", var39);
      }

      if (Settings.get().customBlocksFire) {
         var48.putAll(this.wrapBlockstateFile(Material.FIRE, var7));
      }

      if (Settings.get().customBlocksWire) {
         var48.putAll(this.wrapBlockstateFile(Material.TRIPWIRE, var6));
      }

      return var48;
   }

   private void addVariant(JSONObject var1, int var2, BlockRealizationMode var3, @Nullable Material var4, String var5) {
      this.addVariantInternal(null, var1, var2, var3, var4, var5);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void addVariantInternal(
      @Nullable CustomBlockVariant var1, JSONObject var2, int var3, BlockRealizationMode var4, @Nullable Material var5, String var6
   ) {
      switch (SwitchMap79.bIf[var4.ordinal()]) {
         case 1:
         case 2:
         case 3:
         case 4:
            this.putVariantModel(var1, var2, var6, this.resolveVariantKey(var4, var5, var3));
            return;
         default:
            throw new RuntimeException("Not implemented!");
      }
   }

   private void addCustomVariant(CustomBlockVariant var1, JSONObject var2, int var3, CustomBlockDefinition var4) {
      this.addVariantInternal(var1, var2, var3, var4.aps, null, var4.method8734(false, true));
   }

   private void addVariantNoMaterial(JSONObject var1, int var2, BlockRealizationMode var3, String var4) {
      this.addVariant(var1, var2, var3, null, var4);
   }

   private void putVariantModel(@Nullable CustomBlockVariant var1, JSONObject var2, String var3, String var4) {
      if (var1 == null) {
         JSONObject var10 = new JSONObject();
         var10.put("model", var3);
         var2.putIfAbsent(var4, var10);
      } else {
         if (var1 != null) {
            if (var1.arh != null) {
               JSONArray var5 = new JSONArray();

               for (ToolDurabilitySpec var7 : var1.arh) {
                  JSONObject var8 = new JSONObject();
                  var8.put("model", var7.RU);
                  var8.put("weight", var7.apN);
                  var8.put("x", var7.qx);
                  var8.put("y", var7.qy);
                  var8.put("uvlock", var7.apO);
                  var5.add(var8);
               }

               var2.put(var4, var5);
            } else {
               JSONObject var9 = new JSONObject();
               var9.put("model", var3);
               if (var1.getRotX() % 90 == 0) {
                  if (var1.getRotX() != 0) {
                     var9.put("x", var1.getRotX());
                  }
               } else {
                  Log.error("Invalid 'rotx' for '" + var1.aqW.getNamespacedID() + "' block model. Allowed values: 0,90,180,270. File: " + var1.aqW.DJ());
               }

               if (var1.getRotY() % 90 == 0) {
                  if (var1.getRotY() != 0) {
                     var9.put("y", var1.getRotY());
                  }
               } else {
                  Log.error("Invalid 'roty' for '" + var1.aqW.getNamespacedID() + "' block model. Allowed values: 0,90,180,270. File: " + var1.aqW.DJ());
               }

               var2.put(var4, var9);
            }
         }
      }
   }

   private String resolveVariantKey(BlockRealizationMode var1, Material var2, int var3) {
      MaterialTableEntry var4 = MaterialTable.findById(var1, var3);
      if (var4 != null && (var2 == null || var4.material() == var2)) {
         return var4.getStateProperties();
      } else {
         throw new IllegalArgumentException("Unknown block state ID " + var1 + "/" + var3);
      }
   }

   private JSONArray buildFireMultipart(AgeableBlockDefinition var1, int var2) {
      return this.buildMultipartEntry(var2, var1.getNamespace() + ":" + var1.nC(), var1.aps);
   }

   private JSONArray buildMultipartEntry(int var1, String var2, BlockRealizationMode var3) {
      if (var3 != BlockRealizationMode.FIRE) {
         JSONArray var11 = new JSONArray();
         JSONObject var12 = new JSONObject();
         JSONObject var13 = buildFaceConditions(var1);
         var12.put("when", var13);
         JSONObject var14 = new JSONObject();
         var14.put("model", var2);
         var12.put("apply", var14);
         var11.add(var12);
         return var11;
      }

      int var4 = 0;
      JSONArray var5 = JsonHelper.copyArray(this.bIc);

      for (Object var7 : var5) {
         JSONObject var8 = (JSONObject)var7;
         this.setAgeCondition(var8, var1);

         for (Object var10 : var8.getJSONArray("apply")) {
            ((JSONObject)var10).put("model", var2 + "_" + (String)bId.get(var4));
            var4++;
         }
      }

      return var5;
   }

   public static JSONObject buildFaceConditions(int var0) {
      JSONObject var1 = new JSONObject();
      String[] var2 = new String[]{"down", "east", "north", "south", "up", "west"};

      for (int var3 = 0; var3 < var2.length; var3++) {
         var1.put(var2[var2.length - 1 - var3], (var0 & 1 << var3) != 0);
      }

      return var1;
   }

   private void setAgeCondition(JSONObject var1, int var2) {
      JSONObject var3 = var1.getJSONObject("when");
      if (var3.containsKey("OR")) {
         for (Object var5 : var3.getJSONArray("OR")) {
            ((JSONObject)var5).put("age", String.valueOf(var2));
         }
      } else {
         var3.put("age", String.valueOf(var2));
      }
   }

   public HashMap wrapBlockstateFile(Material var1, JSONObject var2) {
      HashMap var3 = new HashMap();
      var3.put("assets/minecraft/blockstates/" + var1.toString().toLowerCase() + ".json", var2);
      return var3;
   }

   static {
      bId.add("fire_floor0");
      bId.add("fire_floor1");
      bId.add("fire_side0");
      bId.add("fire_side1");
      bId.add("fire_side_alt0");
      bId.add("fire_side_alt1");
      bId.add("fire_side0");
      bId.add("fire_side1");
      bId.add("fire_side_alt0");
      bId.add("fire_side_alt1");
      bId.add("fire_side0");
      bId.add("fire_side1");
      bId.add("fire_side_alt0");
      bId.add("fire_side_alt1");
      bId.add("fire_side0");
      bId.add("fire_side1");
      bId.add("fire_side_alt0");
      bId.add("fire_side_alt1");
      bId.add("fire_up0");
      bId.add("fire_up1");
      bId.add("fire_up_alt0");
      bId.add("fire_up_alt1");
   }
}
