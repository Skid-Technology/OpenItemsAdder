package dev.lone.itemsadder.item;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;

public class CustomItemDataEntry {
   private final NamespacedKey aqz;
   @Nullable
   private final String aqA;
   @Nullable
   private final String aqB;
   @Nullable
   private final String aqC;

   public CustomItemDataEntry(NamespacedKey var1, String var2, String var3) {
      this(var1, var2, var3, null);
   }

   public CustomItemDataEntry(NamespacedKey var1, @Nullable String var2, @Nullable String var3, @Nullable String var4) {
      this.aqz = var1;
      this.aqA = var2;
      this.aqB = var3;
      this.aqC = var4;
   }

   public static CustomItemDataEntry method75(NamespacedKey var0, String var1) {
      return new CustomItemDataEntry(var0, null, null, var1);
   }

   public boolean EV() {
      return this.aqC != null;
   }

   public JSONObject EW() {
      JSONObject var1 = new JSONObject();
      if (this.EV()) {
         var1.put("wings", da(this.aqz.toString()));
      } else {
         var1.put("humanoid", da(this.aqz.toString()));
         var1.put("humanoid_leggings", da(this.aqz.toString()));
      }

      return new JSONObject().fluentPut("layers", var1);
   }

   private static JSONArray da(String var0) {
      return new JSONArray().fluentAdd(new JSONObject().fluentPut("texture", var0));
   }

   public NamespacedKey EX() {
      return this.aqz;
   }

   @Nullable
   public String EY() {
      return this.aqA;
   }

   @Nullable
   public String EZ() {
      return this.aqB;
   }

   @Nullable
   public String Fa() {
      return this.aqC;
   }
}
