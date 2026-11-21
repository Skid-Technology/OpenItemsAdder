package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;

public class VersionedPackEntry {
   JSONObject json;
   int hash;

   public VersionedPackEntry(int var1, JSONObject var2) {
      this.json = var2;
      this.hash = var1;
   }

   public static VersionedPackEntry fromJson(JSONObject var0) {
      int var1 = var0.hashCode();
      return new VersionedPackEntry(var1, var0);
   }
}
