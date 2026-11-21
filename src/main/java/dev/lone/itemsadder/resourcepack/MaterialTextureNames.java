package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.NamespacedIdentity;
import dev.lone.itemsadder.internal.SwitchMap81;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public class MaterialTextureNames {
   private JSONObject Cy;
   private final Set bIr = new TreeSet();
   private final Material bIs;

   public MaterialTextureNames(Material var1, List var2) {
      this.bIs = var1;
      ModelJsonAsset var3 = ModelJsonAsset.resolveForMaterial(var1, Version.v1_20_6, true);
      if (var3.hasError()) {
         Log.error("Error reading vanilla model for material " + var1 + ", skip: " + var3.getError());
         Log.error("Choose another material, " + var1 + " is not supported for these items: " + ContentManager.method8191(var2, 5, "...and more"));
      } else {
         this.Cy = var3.copyAsObject();
         JSONArray var4 = new JSONArray();
         ArrayList var5 = new ArrayList();

         for (NamespacedIdentity var7 : var2) {
            if (!(var7 instanceof CustomItemDefinition var8 && var8.DI())) {
               JSONArray var12 = this.method6868(var7);
               if (var12 != null) {
                  var4.addAll(var12);
                  var5.add(var7.nz());
                  this.bIr.add(var7.nz());
               }
            }
         }

         if (!this.Cy.containsKey("overrides")) {
            this.Cy.put("overrides", var4);
         } else {
            Iterator var10 = this.Cy.getJSONArray("overrides").iterator();

            while (var10.hasNext()) {
               try {
                  JSONObject var11 = (JSONObject)var10.next();
                  int var13 = var11.getJSONObject("predicate").getInteger("custom_model_data");
                  if (var5.contains(var13)) {
                     var10.remove();
                  }
               } catch (NullPointerException var9) {
               }
            }

            this.Cy.getJSONArray("overrides").addAll(var4);
         }
      }
   }

   public Set ZO() {
      return this.bIr;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Nullable
   JSONArray method6868(NamespacedIdentity var1) {
      if (var1.nz() < 0) {
         return null;
      }

      JSONArray var2 = new JSONArray();
      ModelJsonAsset var4;
      if (this.bIs.equals(Material.TRIDENT)) {
         var4 = ModelJsonAsset.forNameVersion("trident_in_hand", Version.v1_20_6);
      } else {
         var4 = ModelJsonAsset.resolveForMaterial(this.bIs, Version.v1_20_6, true);
      }

      if (var4.hasError()) {
         Log.error("Error reading vanilla model for material '" + this.bIs + "': " + var4.getError());
         return null;
      }

      JSONObject var3 = var4.copyAsObject();
      JSONObject var5 = new JSONObject();
      JSONObject var6 = new JSONObject();
      var6.put("custom_model_data", var1.nz());
      var5.put("predicate", var6);
      var5.put("model", var1.method8734(false, true));
      var2.add(var5);
      if (var3.containsKey("overrides")) {
         for (Object var8 : var3.getJSONArray("overrides")) {
            JSONObject var9 = new JSONObject();
            JSONObject var10 = ((JSONObject)var8).getJSONObject("predicate");
            var10.put("custom_model_data", var1.nz());
            var9.put("predicate", var10);
            String var11 = "";
            switch (SwitchMap81.bIt[this.bIs.ordinal()]) {
               case 1:
                  if (var10.containsKey("broken") && var1 instanceof CustomItemDefinition var12 && var12.DP() != null && var12.DP().EQ() != null) {
                     var11 = "_broken";
                  } else if (!var10.containsKey("broken")) {
                     var10.put("broken", 0);
                  }
                  break;
               case 2:
                  if (var10.containsKey("blocking")) {
                     var11 = "_blocking";
                  } else {
                     var10.put("blocking", 0);
                  }
                  break;
               case 3:
                  if (var10.containsKey("pulling")) {
                     if (var10.containsKey("pull")) {
                        if (var10.getString("pull").equals("0.65")) {
                           var11 = "_1";
                        } else if (var10.getString("pull").equals("0.9")) {
                           var11 = "_2";
                        }
                     } else {
                        var11 = "_0";
                     }
                  } else {
                     var10.put("pulling", 0);
                  }
                  break;
               case 4:
                  if (var10.containsKey("pulling")) {
                     if (var10.containsKey("pull")) {
                        if (var10.getString("pull").equals("0.58")) {
                           var11 = "_1";
                        } else if (var10.getString("pull").equals("1.0")) {
                           var11 = "_2";
                        }
                     } else {
                        var11 = "_0";
                     }
                  } else if (var10.containsKey("firework")) {
                     if (var10.getInteger("firework") == 1) {
                        var11 = "_firework";
                     }
                  } else if (var10.containsKey("charged")) {
                     if (var10.getInteger("charged") == 1) {
                        var11 = "_charged";
                     }
                  } else {
                     var10.put("pulling", 0);
                  }
                  break;
               case 5:
                  if (var10.containsKey("cast")) {
                     if (var10.getInteger("cast") == 1) {
                        var11 = "_cast";
                     }
                  } else {
                     var10.put("cast", 0);
                  }
                  break;
               case 6:
                  if (var10.containsKey("throwing")) {
                     var11 = "_throwing";
                  } else {
                     var11 = "_in_hand";
                     var10.put("throwing", 0);
                  }
                  break;
               default:
                  if (this.bIs.toString().equals("BUNDLE")) {
                     if (var10.containsKey("filled")) {
                        var11 = "_filled";
                     } else {
                        var10.put("filled", 1.0E-7);
                     }
                  }
            }

            if (!var1.nB() && var1.ny()) {
               if (!var1.nC().startsWith("minecraft:")) {
                  var9.put("model", method6869(var1, var11));
                  String var15 = ContentManager.method8195(var1.getNamespace(), var1.method8734(false, false) + var11 + ".json");
                  File var13 = ContentManager.method8198(var15);
                  this.method6873("------------------ Checking if the file " + var15);
                  if (var13 == null) {
                     if (var1 instanceof CustomItemDefinition var14) {
                        Log.error(ChatColor.RED + "[Pack] Model '" + var15 + "' not found in your contents. File: " + var14.DJ());
                     } else {
                        Log.error(ChatColor.RED + "[Pack] Model '" + var15 + "' not found in your contents.");
                     }
                  } else {
                     this.method6873("------------------ The file " + var15 + " exists in the resource pack.");
                  }
               } else {
                  var9.put("model", var1.nC() + var11);
               }
            } else {
               var9.put("model", var1.getNamespace() + ":item/ia_auto/" + var1.getId() + var11);
            }

            var2.add(var9);
         }
      }

      return var2;
   }

   static String method6869(NamespacedIdentity var0, String var1) {
      String var2 = var0.nC();
      return (var2.contains(":") ? var2 : var0.getNamespace() + ":" + var2) + var1;
   }

   public String bI(boolean var1) {
      return method6870(this.bIs) + (var1 ? ".json" : "");
   }

   public String ZP() {
      return this.bI(false);
   }

   public String ZQ() {
      return method6871(this.bIs);
   }

   public static String method6870(Material var0) {
      return var0.toString().toLowerCase();
   }

   public static String method6871(Material var0) {
      return "minecraft:item/" + method6870(var0);
   }

   public static String method6872(Material var0) {
      return ModelJsonAsset.forMaterialVersion(var0, Version.v1_20_6).getParent();
   }

   public JSONObject pd() {
      return this.Cy;
   }

   public static JSONArray ag(JSONObject var0) {
      JSONArray var1 = var0.getJSONArray("overrides");
      JSONArray var2 = new JSONArray();
      var2.addAll(var1);
      var2.sort((var0x, var1x) -> {
         try {
            int var2x = -100;
            int var3 = -100;
            if (((JSONObject)var0x).getJSONObject("predicate").containsKey("custom_model_data")) {
               var2x = ((JSONObject)var0x).getJSONObject("predicate").getInteger("custom_model_data");
            }

            if (((JSONObject)var1x).getJSONObject("predicate").containsKey("custom_model_data")) {
               var3 = ((JSONObject)var1x).getJSONObject("predicate").getInteger("custom_model_data");
            }

            return Integer.compare(var2x, var3);
         } catch (Exception var4) {
            var4.printStackTrace();
            return 0;
         }
      });
      return var2;
   }

   public static void ah(JSONObject var0) {
      var0.put("overrides", ag(var0));
   }

   private void method6873(String var1) {
   }
}
