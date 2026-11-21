package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.block.AgeableBlockDefinition;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.internal.SwitchMap80;
import dev.lone.itemsadder.item.ColoredItemDefinition;
import dev.lone.itemsadder.item.LeatherColorDefinition;
import dev.lone.itemsadder.item.TexturedItemBase;
import dev.lone.itemsadder.item.TexturedItemDefinition;
import dev.lone.itemsadder.util.Log;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.Material;

public class PackDataIndex {
   private final HashMap bIi = new HashMap();
   final String[] bIj = new String[]{"down", "east", "north", "south", "up", "west"};
   final String[] bIk = new String[]{
      "fire_floor0", "fire_floor1", "fire_side_alt0", "fire_side_alt1", "fire_side0", "fire_side1", "fire_up_alt0", "fire_up_alt1", "fire_up0", "fire_up1"
   };
   final TexturedItemBase bIl;

   public PackDataIndex(TexturedItemBase var1) {
      this.bIl = var1;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public HashMap ZL() {
      switch (SwitchMap80.bIn[this.bIl.apf.ordinal()]) {
         case 1:
            this.bIi.put("", this.ZM());
            if (this.bIl.ape != null && this.bIl.ape.DP() != null && this.bIl.ape.DP().EQ() != null) {
               this.bIi.put("_broken", ModelJsonUtils.copyWithLayer0(this.ZM(), this.bIl.ape.DP().EQ()));
            }
            break;
         case 2:
            for (int var10 = 0; var10 < 4; var10++) {
               String var16 = "";
               if (var10 == 1) {
                  var16 = "_0";
               } else if (var10 == 2) {
                  var16 = "_1";
               } else if (var10 == 3) {
                  var16 = "_2";
               }

               this.bIi.put(var16, this.iQ(var16));
            }
            break;
         case 3:
            for (int var9 = 0; var9 < 6; var9++) {
               String var15 = "";
               if (var9 == 1) {
                  var15 = "_0";
               } else if (var9 == 2) {
                  var15 = "_1";
               } else if (var9 == 3) {
                  var15 = "_2";
               } else if (var9 == 4) {
                  var15 = "_charged";
               } else if (var9 == 5) {
                  var15 = "_firework";
               }

               this.bIi.put(var15, this.iQ(var15));
            }
            break;
         case 4:
            for (int var8 = 0; var8 <= 1; var8++) {
               String var14 = "";
               if (var8 == 1) {
                  var14 = "_cast";
               }

               this.bIi.put(var14, this.iQ(var14));
            }
            break;
         case 5:
            for (int var7 = 0; var7 <= 1; var7++) {
               String var13 = "";
               if (var7 == 1) {
                  var13 = "_blocking";
               }

               this.bIi.put(var13, this.iQ(var13));
            }
            break;
         case 6:
            for (int var6 = 0; var6 <= 1; var6++) {
               String var12 = "";
               if (var6 == 0) {
                  var12 = "_in_hand";
               }

               if (var6 == 1) {
                  var12 = "_throwing";
               }

               this.bIi.put(var12, this.iQ(var12));
            }
            break;
         default:
            if (this.bIl.apf == Material.BUNDLE) {
               this.bIi.put("", this.iQ(""));
               this.bIi.put("_filled", this.iQ("_filled"));
            } else if (this.bIl.ape instanceof AgeableBlockDefinition) {
               for (String var4 : this.bIk) {
                  ModelVariantAsset var5 = new ModelVariantAsset(String.format("internal/fire/%s.json", var4));
                  this.bIi.put("_" + var4, this.method6861(var5));
               }

               this.bIi.put("", this.ZM());
            } else {
               this.bIi.put("", this.ZM());
            }
      }

      HashMap var11 = new HashMap();

      for (Entry var18 : this.bIi.entrySet()) {
         JSONObject var19 = (JSONObject)var18.getValue();
         String var20 = (String)var18.getKey();
         var11.put("assets/" + this.bIl.getNamespace() + "/models/" + this.bIl.buildTexturePath(false, false) + var20 + ".json", var19);
      }

      return var11;
   }

   private JSONObject method6861(ModelVariantAsset var1) {
      assert this.bIl.ape != null;
      var1.getTextures().put("fire", this.bIl.ape.getTextures().get(0));
      return var1.copyAsObject();
   }

   private JSONObject ZM() {
      return this.iQ("");
   }

   private JSONObject iQ(String var1) {
      JSONObject var2 = new JSONObject();
      var2.put("parent", this.bIl.DO());
      JSONObject var3 = new JSONObject();
      List var4 = this.bIl.getTextures();
      if (this.bIl instanceof TexturedItemDefinition) {
         method6862(var2, this.bIl, var1, var4, var3);
      } else if (!(this.bIl.ape instanceof CustomBlockDefinition) && this.bIl.apf.isBlock()) {
         if (!var4.isEmpty()) {
            ModelJsonAsset var7 = ModelJsonAsset.forMaterial(this.bIl.apf);
            if (!var7.hasError()) {
               String var6 = var7.firstTextureKey();
               if (var6 != null) {
                  var3.put(var6, ((String)var4.get(0)).replace(".png", "") + var1);
               }
            }
         }
      } else if (!this.bIl.apf.isBlock() && !(this.bIl.ape instanceof CustomBlockDefinition)) {
         method6862(var2, this.bIl, var1, var4, var3);
      } else if (this.bIl.apf == Material.PLAYER_HEAD) {
         var3.put("layer0", ((String)var4.get(0)).replace(".png", "") + var1);
      } else {
         if (var4.size() == 1) {
            var3.put("all", ((String)var4.get(0)).replace(".png", "") + var1);
         } else {
            for (int var5 = 0; var5 < var4.size(); var5++) {
               var3.put(this.bIj[var5], ((String)var4.get(var5)).replace(".png", "") + var1);
            }
         }

         if (var4.isEmpty()) {
            if (this.bIl.ape != null) {
               Log.error("&cItem &6" + this.bIl.getNamespacedID() + " &chas no textures set. Please check the file &6" + this.bIl.ape.DJ());
            }
         } else {
            var3.put("particle", ((String)var4.get(0)).replace(".png", "") + var1);
         }
      }

      var2.put("textures", var3);
      return var2;
   }

   private static void method6862(JSONObject var0, TexturedItemBase var1, String var2, List var3, JSONObject var4) {
      for (int var5 = 0; var5 < var3.size(); var5++) {
         var4.put("layer" + var5, ((String)var3.get(var5)).replace(".png", "") + var2);
      }

      Material var7 = var1.apf;
      if (var1.ape instanceof ColoredItemDefinition) {
         var0.put("parent", "minecraft:item/generated");
         if (!var4.containsKey("layer0")) {
            var4.put("layer0", "item/" + var7.toString().toLowerCase().replace("leather_", "iron_"));
            var4.put("layer1", "item/empty");
         } else {
            LeatherColorDefinition var6 = ((ColoredItemDefinition)var1.ape).De();
            if (var6 != null && !var6.apK) {
               var4.put("layer1", var4.getString("layer0"));
               var4.put("layer0", "item/empty");
            }
         }
      }
   }
}
