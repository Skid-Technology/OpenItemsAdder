package dev.lone.itemsadder.block;

import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.IntTableConstantsS;
import dev.lone.itemsadder.util.IntVector3;
import dev.lone.itemsadder.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import org.bukkit.block.BlockFace;

public class InventoryHookLoader {
   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static void loadDirectionalHooks(CustomBlockDefinition var0) {
      ContentManager var1 = ContentManager.method8084();
      InventoryHookMode var2 = parseDirectionalMode(var0.aU(true), var1x -> {
         if (var1x != null) {
            Log.error("[Items] '" + var0.getNamespacedID() + "'. Error: Unknown 'directional_mode' value '" + var1x + "'. File: " + var0.DJ());
         }
      });
      if (var2 != null && var0.anF != null) {
         if (var0.aps.isBlockBacked() && !var0.ny() && (var0.getTextures() == null || var0.getTextures().size() < 6)) {
            Log.warn("[Items] '" + var0.getNamespacedID() + "'. Not enough textures for directional block. Provide all 6 faces textures. File: " + var0.DJ());
            return;
         }

         ArrayList var3 = new ArrayList();
         var0.anF.setDirectionalMode(var2);

         for (BlockFace var7 : CustomBlockDefinition.anE) {
            IntVector3 var8;
            var8 = new IntVector3(0, 0, 0);
            label66:
            switch (IntTableConstantsS.SWITCH_MAP_B[var2.ordinal()]) {
               case 1:
                  switch (IntTableConstantsS.SWITCH_MAP_A[var7.ordinal()]) {
                     case 1:
                        var8.x = 90;
                        var8.y = 180;
                        break label66;
                     case 2:
                        var8.x = 90;
                        break label66;
                     case 3:
                        var8.x = 90;
                        var8.y = 90;
                        break label66;
                     case 4:
                        var8.x = 90;
                        var8.y = -90;
                        break label66;
                     case 5:
                        var8.x = 180;
                        var8.y = 180;
                     default:
                        break label66;
                  }
               case 2:
                  switch (IntTableConstantsS.SWITCH_MAP_A[var7.ordinal()]) {
                     case 1:
                     case 2:
                        var8.x = 90;
                        break label66;
                     case 3:
                     case 4:
                        var8.x = 90;
                        var8.y = 90;
                     default:
                        break label66;
                  }
               case 3:
                  switch (IntTableConstantsS.SWITCH_MAP_A[var7.ordinal()]) {
                     case 1:
                        var8.y = 180;
                     case 2:
                     default:
                        break label66;
                     case 3:
                        var8.y = 90;
                        break label66;
                     case 4:
                        var8.y = -90;
                        break label66;
                  }
               case 4:
                  switch (IntTableConstantsS.SWITCH_MAP_A[var7.ordinal()]) {
                     case 1:
                        var8.y = 180;
                     case 2:
                     default:
                        break;
                     case 3:
                        var8.y = 90;
                        break;
                     case 4:
                        var8.y = -90;
                        break;
                     case 5:
                        var8.x = 90;
                        var8.y = 180;
                        break;
                     case 6:
                        var8.x = -90;
                  }
            }

            if (!var8.isZero()) {
               CustomBlockDefinition var9 = var0.anF.findDirectionalBlock(var8.x, var8.y);
               if (var9 != null) {
                  Log.devLog(
                     "[Items] '"
                        + var0.getNamespacedID()
                        + "'. Using previously created directional block '"
                        + var9.getNamespacedID()
                        + "' for face '"
                        + var7.name()
                        + "'. File: "
                        + var0.DJ()
                  );
                  var0.anF.setDirectionalBlock(var7, var9);
               } else {
                  String var10 = var0.getId() + "_" + var7.name().toLowerCase();
                  CustomBlockDefinition var11 = ContentManager.method8084().method8160(var10);
                  if (var11 != null) {
                     Log.log("[Items] '" + var0.getNamespacedID() + "'. Using existing directional block '" + var11.getNamespacedID() + "'. File: " + var0.DJ());
                     var11.anF.setRotX(var8.x);
                     var11.anF.setRotY(var8.y);
                     var11.anF.setDirectional(true);
                  } else {
                     var11 = var0.cloneDirectional(var10, var8);
                     var3.add(var11);
                  }

                  var11.anF.setDirectional(true);
                  var0.anF.setDirectionalBlock(var7, var11);
               }
            }
         }

         for (CustomBlockDefinition var13 : var3) {
            var1.al.add(var13);
            var1.am.put(var13.getNamespacedID(), var0);
            if (var0.aps.isBlockBacked()) {
               var1.an.put(var0.anF.aqX, var0);
            }

            var1.field92.put(var13.getNamespacedID(), var13);
            var1.field95.put(var13.getId(), var13);
            var1.field94.put(var13.aol, var13);
            var1.ag.computeIfAbsent(var13.getType(), var0x -> new ArrayList<>()).add(var13);
            var1.ah.computeIfAbsent(var13.getType(), var0x -> new HashMap<>()).put(var13.getNamespacedID(), var13);
            var1.af.computeIfAbsent(var13.getNamespace(), var0x -> new HashMap<>()).put(var13.getId(), var13);
            Log.devLog("Created directional block '" + var13.getNamespacedID() + "' from '" + var0.getNamespacedID() + "'.");
         }

         Log.log("[Items] Created " + var3.size() + " directional items for block '" + var0.getNamespacedID() + "' ('" + var2.name() + "'). File: " + var0.DJ());
      }
   }

   static InventoryHookMode parseDirectionalMode(ConfigSectionWrapper var0, Consumer var1) {
      ConfigSectionWrapper var2 = var0.kc("specific_properties.block");
      if (var2 == null) {
         var2 = var0.kc("behaviours.block");
      }

      return var2 == null ? null : (InventoryHookMode)var2.method7098("placed_model.directional_mode", InventoryHookMode.class, null, var1);
   }
}
