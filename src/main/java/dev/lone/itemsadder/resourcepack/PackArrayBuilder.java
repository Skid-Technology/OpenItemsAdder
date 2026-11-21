package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.util.Log;
import java.util.Collection;

public class PackArrayBuilder {
   private final Collection bIo;
   public JSONArray bIp;
   public JSONArray bIq = null;

   public PackArrayBuilder(Collection var1) {
      this.bIo = var1;
      JsonAssetWithCallbacks.loadWithCallbacks(
         "internal/negative_spaces.json",
         var1x -> this.bIp = var1x.asArray(),
         var0 -> Log.error("Unable to read negative_spaces.json file from the plugin Jar. Try to redownload the plugin. Error: " + var0.getError())
      );
      String var2 = Main.config.getBoolean("resource_pack.font.enabled") ? Main.config.getString("resource_pack.font.mode") : "vanilla";
      if (var2.equals("custom")) {
         this.bIq = new JSONArray();
         JSONObject var3 = new JSONObject();
         var3.put("type", "ttf");
         var3.put("file", Main.config.getString("resource_pack.font.custom.name"));
         var3.put("oversample", Main.config.getFloat("resource_pack.font.custom.oversample"));
         var3.put("size", Main.config.getFloat("resource_pack.font.custom.size"));
         var3.put("shift", Main.config.method7053("resource_pack.font.custom.shift", null));
         this.bIq.add(var3);
      } else if (var2.equals("thin")) {
         JsonAssetWithCallbacks.loadWithCallbacks(
            "internal/thin-font.json",
            var1x -> this.bIq = var1x.asArray(),
            var0 -> Log.error("Error reading thin-font font plugin jar. Try to redownload the plugin. Error: " + var0.getError())
         );
      }
   }

   public JSONObject ZN() {
      JSONObject var1 = new JSONObject();
      JSONArray var2 = new JSONArray();
      var1.put("providers", this.method6863(var2));
      if (this.bIq != null) {
         var2.addAll(this.bIq);
      }

      if (this.bIp != null) {
         var2.addAll(this.bIp);
      }

      return var1;
   }

   private JSONArray method6863(JSONArray var1) {
      for (CustomTexture var3 : this.bIo) {
         JSONObject var4 = new JSONObject();
         var4.put("file", var3.getFullId());
         JSONArray var5 = new JSONArray();
         var5.add(var3.getDisplayText(false));
         var4.put("chars", var5);
         var4.put("height", var3.getHeight());
         var4.put("ascent", var3.getYPosition());
         var4.put("type", "bitmap");
         var1.add(var4);
      }

      return var1;
   }
}
