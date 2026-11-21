package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.item.ItemSpec;
import dev.lone.itemsadder.item.ItemSpecWithAttributes;
import dev.lone.itemsadder.item.ItemSpecWithTexture;
import dev.lone.itemsadder.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

public class ModelConverterBlockStates extends ModelConverterCommon {
   public ModelConverterBlockStates(ItemSpec var1) {
      super(var1);
   }

   @Override
   void collectTextureModels(ItemSpecWithTexture var1) {
      String var2 = (String)var1.Fd().get("normal");
      if (var2 == null) {
         throw new IllegalArgumentException("Spear graphics must have a 'normal' texture: " + this.bHi.Eo());
      }

      JSONObject var3 = ModelJsonAsset.forMaterial(this.bHi.Ep()).copyAsObject();
      setLayer0Texture(var3, var2);
      this.bHj = new ModelDefinition(this.bHi.Ek(), this.bHi.Ej(), var3);
      JSONObject var4 = ModelJsonAsset.forName("spear_in_hand").copyAsObject();
      setLayer0Texture(var4, var1.Fd().getOrDefault("in_hand", var2));
      this.bHk.put("in_hand", new ModelDefinition(this.bHi.cV("in_hand"), this.bHi.cU("in_hand"), var4));
   }

   @Override
   void collectAttributeModels(ItemSpecWithAttributes var1) {
      super.collectAttributeModels(var1);
      if (!this.bHk.containsKey("in_hand")) {
         String[] var2 = this.bHi.Eo().split(":", 2);
         String var3 = var2[0] + ":item/ia_auto/" + var2[1] + "_in_hand";
         String var4 = "assets/" + var2[0] + "/models/item/ia_auto/" + var2[1] + "_in_hand.json";
         JSONObject var5 = ModelJsonAsset.forName("spear_in_hand").copyAsObject();
         this.bHk.put("in_hand", new ModelDefinition(var3, var4, buildInHandModel(this.bHj.bHo, this.bHj.bHq, var5)));
      }
   }

   @Override
   void buildMaterialJson() {
      JSONObject var1 = new MaterialJsonAsset(this.bHi.Ep()).copyAsObject();
      String var2 = this.bHi.Em() != null ? this.bHi.Em() : this.bHj.bHo;
      setSpearModels(var1.getJSONObject("model"), var2, this.getModelIdOrDefault("in_hand", this.bHj.bHo));
      this.applyItemProperties(var1);
      this.bHm = var1;
   }

   static void setSpearModels(JSONObject var0, String var1, String var2) {
      var0.getJSONArray("cases").getJSONObject(0).getJSONObject("model").put("model", var1);
      var0.getJSONObject("fallback").put("model", var2);
   }

   static void setLayer0Texture(JSONObject var0, String var1) {
      var0.put("textures", new JSONObject().fluentPut("layer0", var1));
   }

   static JSONObject buildInHandModel(String var0, @Nullable JSONObject var1, JSONObject var2) {
      JSONObject var3 = var1 == null ? null : var1.getJSONObject("display");
      JSONObject var4 = var2.getJSONObject("display");
      JSONObject var5 = new JSONObject();
      putHandDisplay(var5, var3, var4, "firstperson_righthand", -20, 90, 10);
      putHandDisplay(var5, var3, var4, "firstperson_lefthand", -20, -90, -10);
      putHandDisplay(var5, var3, var4, "thirdperson_righthand", 5, 270, 5);
      putHandDisplay(var5, var3, var4, "thirdperson_lefthand", 5, -270, -5);
      return new JSONObject().fluentPut("parent", var0).fluentPut("display", var5);
   }

   private static void putHandDisplay(JSONObject var0, @Nullable JSONObject var1, JSONObject var2, String var3, int var4, int var5, int var6) {
      JSONObject var7 = var1 == null ? null : var1.getJSONObject(var3);
      JSONObject var8 = JsonHelper.copyObject(var7 != null ? var7 : var2.getJSONObject(var3));
      var8.put("rotation", new JSONArray().fluentAdd(var4).fluentAdd(var5).fluentAdd(var6));
      var0.put(var3, var8);
   }
}
